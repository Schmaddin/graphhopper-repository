package com.graphhopper.chilango;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

import com.graphhopper.GraphHopper;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.HintsMap;
import com.graphhopper.routing.weighting.Weighting;
import com.graphhopper.storage.DataAccess;
import com.graphhopper.storage.Directory;
import com.graphhopper.storage.Graph;
import com.graphhopper.util.BitUtil;
import com.graphhopper.util.Parameters.Algorithms;

public class ChilangoHopper extends GraphHopper {

	// mapping of internal edge ID to OSM way ID
	private DataAccess edgeMapping;
	private BitUtil bitUtil;

	private DataAccess nodeMapping;

	@Override
	public GraphHopper forMobile() {
		super.forMobile();
		ChilangoFlacEncoder encoder = new ChilangoFlacEncoder();
		this.setEncodingManager(new EncodingManager(encoder));
		this.getCHFactoryDecorator().setEnabled(false);

		HintsMap hintsMap = new HintsMap();
		hintsMap.setWeighting("chilango_weighting_light");
		hintsMap.setVehicle("chilango");
		hintsMap.put("algorithm", Algorithms.DIJKSTRA_CHILANGO);

		return this;
	}

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

		/*
		 * try { FileInputStream fileInputStream = new FileInputStream(
		 * getGraphHopperStorage().getDirectory() + "/internToOSM.data");
		 * ObjectInputStream objectInputStream = new
		 * ObjectInputStream(fileInputStream); nodeMappingRev = (Map<Long,
		 * Integer>) objectInputStream.readObject(); edgeMappingRev =
		 * (Map<String, Integer>) objectInputStream.readObject();
		 * objectInputStream.close(); } catch (IOException e) {
		 * e.printStackTrace(); } catch (ClassNotFoundException e) {
		 * e.printStackTrace(); }
		 */
		return loaded;
	}

	@Override
	public Weighting createWeighting(HintsMap hintsMap, FlagEncoder encoder, Graph graph) {
		Weighting weighting = super.createWeighting(hintsMap, encoder, graph);
		if (weighting instanceof ChilangoWeighting) {
			((ChilangoWeighting) weighting).setHopper(this);
		}
		return weighting;
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
