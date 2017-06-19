/*
 *  Licensed to GraphHopper and Peter Karich under one or more contributor
 *  license agreements. See the NOTICE file distributed with this work for
 *  additional information regarding copyright ownership.
 *
 *  GraphHopper licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except in
 *  compliance with the License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.graphhopper.reader.osm;

import com.graphhopper.GraphHopper;
import com.graphhopper.chilango.ChilangoFlacEncoder;
import com.graphhopper.chilango.ChilangoWeighting;
import com.graphhopper.reader.DataReader;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.HintsMap;
import com.graphhopper.routing.weighting.Weighting;
import com.graphhopper.storage.DataAccess;
import com.graphhopper.storage.Directory;
import com.graphhopper.storage.Graph;
import com.graphhopper.storage.GraphHopperStorage;
import com.graphhopper.util.BitUtil;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.PointList;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import java.util.Map;

/**
 *
 * @author Peter Karich
 * @author Martin Würflein
 */
public class GraphHopperOSMConverter extends GraphHopperOSM {
	
	
	@Override
	public GraphHopper forMobile(){
		super.forMobile();
        ChilangoFlacEncoder encoder = new ChilangoFlacEncoder();
        this.setEncodingManager(new EncodingManager(encoder));
        this.getCHFactoryDecorator().setEnabled(false);
        
        HintsMap hintsMap = new HintsMap();
        hintsMap.setWeighting("chilango_weighting");
        hintsMap.setVehicle("chilango");
        hintsMap.put("algorithm","chilango_dijkstra");
    
		return this;
		}

	// mapping of internal edge ID to OSM way ID
	private DataAccess edgeMapping;
	private BitUtil bitUtil;

	private DataAccess nodeMapping;

	// Slow
	private Map<Long, Integer> nodeMappingRev = new HashMap<>();
	private Map<String, Integer> edgeMappingRev = new HashMap<>();

	@Override
	public boolean load(String graphHopperFolder) {
		boolean loaded = super.load(graphHopperFolder);

		Directory dir = getGraphHopperStorage().getDirectory();
		bitUtil = BitUtil.get(dir.getByteOrder());
		edgeMapping = dir.find("edge_mapping");
		nodeMapping = dir.find("node_mapping");

		if (loaded) {
			edgeMapping.loadExisting();
			nodeMapping.loadExisting();
		}

		try {
			FileInputStream fileInputStream = new FileInputStream(
					getGraphHopperStorage().getDirectory() + "/internToOSM.data");
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			nodeMappingRev = (Map<Long, Integer>) objectInputStream.readObject();
			edgeMappingRev = (Map<String, Integer>) objectInputStream.readObject();
			objectInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return loaded;
	}

	@Override
	public Weighting createWeighting(HintsMap hintsMap, FlagEncoder encoder, Graph graph) {
	Weighting weighting=super.createWeighting(hintsMap, encoder, graph);
	if(weighting instanceof ChilangoWeighting)
	{
		((ChilangoWeighting) weighting).setHopper(this);
	}
	return weighting;
	}
	@Override
	protected DataReader createReader(GraphHopperStorage ghStorage) {

		OSMReader reader = new OSMReader(ghStorage) {

			{
				System.out.println("OSMConverter: createReader");
				edgeMapping.create(1000);

				// TODO eigenes
				nodeMapping.create(1000);

			}

			// this method is only in >0.6 protected, before it was private
			@Override
			protected void storeOsmWayID(int edgeId, long osmWayId) {
				super.storeOsmWayID(edgeId, osmWayId);

				long pointer = 8L * edgeId;
				edgeMapping.ensureCapacity(pointer + 8L);

				edgeMapping.setInt(pointer, bitUtil.getIntLow(osmWayId));
				edgeMapping.setInt(pointer + 4, bitUtil.getIntHigh(osmWayId));

			}

			protected EdgeIteratorState addEdge(int fromIndex, int toIndex, PointList pointList, long flags,
					long wayOsmId) {
				EdgeIteratorState edge = super.addEdge(fromIndex, toIndex, pointList, flags, wayOsmId);

				long adj = super.nodeIdToOsmNodeIdMap.get(edge.getAdjNode());
				long base = super.nodeIdToOsmNodeIdMap.get(edge.getBaseNode());

				edgeMappingRev.put(base + "-" + adj, edge.getEdge());
				return edge;
			}

			// TODO
			@Override
			protected void storeOsmNodeID(int nodeId, long osmNodeId) {
				if (nodeId >= 0) {
					super.storeOsmNodeID(nodeId, osmNodeId);

					long pointer = 8L * nodeId;
					nodeMapping.ensureCapacity(pointer + 8L);

					nodeMapping.setInt(pointer, bitUtil.getIntLow(osmNodeId));
					nodeMapping.setInt(pointer + 4, bitUtil.getIntHigh(osmNodeId));

					if (nodeMappingRev.containsKey(osmNodeId)) {
						if (nodeMappingRev.get(osmNodeId) != nodeId)
							throw new NullPointerException();
					} else {

						nodeMappingRev.put(osmNodeId, nodeId);
					}
				}
			}

			@Override
			protected void finishedReading() {
				super.finishedReading();

				edgeMapping.flush();

				// TODO
				nodeMapping.flush();
				try {
					FileOutputStream fileOutputStream = new FileOutputStream(
							getGraphHopperStorage().getDirectory() + "/internToOSM.data");
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
					objectOutputStream.writeObject(nodeMappingRev);
					objectOutputStream.writeObject(edgeMappingRev);
					objectOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				try {
					Files.write(Paths.get("test.txt"), info);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		};

		return initDataReader(reader);
	}

	@Override
	public long getOSMWay(int internalEdgeId) {
		long pointer = 8L * internalEdgeId;
		return bitUtil.combineIntsToLong(edgeMapping.getInt(pointer), edgeMapping.getInt(pointer + 4L));
	}

	@Override
	public long getOSMNode(int internalNodeId) {
		long pointer = 8L * internalNodeId;
		return bitUtil.combineIntsToLong(nodeMapping.getInt(pointer), nodeMapping.getInt(pointer + 4L));
	}

}
