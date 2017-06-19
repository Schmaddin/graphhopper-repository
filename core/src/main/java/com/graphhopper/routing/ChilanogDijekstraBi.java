package com.graphhopper.routing;

import com.graphhopper.ChilangoPath;
import com.graphhopper.chilango.ChilangoWeighting;
import com.graphhopper.routing.util.TraversalMode;
import com.graphhopper.routing.weighting.Weighting;
import com.graphhopper.storage.Graph;

/*
 *  Licensed to GraphHopper GmbH under one or more contributor
 *  license agreements. See the NOTICE file distributed with this work for 
 *  additional information regarding copyright ownership.
 * 
 *  GraphHopper GmbH licenses this file to you under the Apache License, 
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

public class ChilanogDijekstraBi extends DijkstraBidirectionRef {

	public ChilanogDijekstraBi(Graph graph, Weighting weighting, TraversalMode tMode) {
		super(graph, weighting, tMode);

		if (weighting instanceof ChilangoWeighting) {
			ChilangoWeighting chilangoWeighting = (ChilangoWeighting) weighting;
			chilangoWeighting.setGraph(graph);
		}
	}
	
	/*
	@Override
	protected Path extractPath() {
		Path path=bestPath;
		if (finished())
		{
			path= bestPath.extractChilango();
			ChilangoPath cPath=new ChilangoPath(bestPath.getGraph(),bestPath.getWeighting());
			cPath.setEncoder(bestPath.getEncoder());
			cPath.setSPTEntry(bestPath.getSPTEntry());
			cPath.setEdgeIds(bestPath.getEdgeIds());
			cPath.setDistance(bestPath.getDistance());
			cPath.setEndNode(bestPath.getEndNode());
			cPath.setFromNode(bestPath.getFromNode());
			cPath.setReverseOrder(bestPath.reverseOrder);
			cPath.setNodeAccess(bestPath.getNodeAccess());
			cPath.setTime(bestPath.getTime());
			ChilangoPath.extractHelper(bestPath, cPath.getBreakMap(),cPath.getHour(),cPath.getMinute());
			ChilangoPath.extractHelper(cPath, cPath.getBreakMap(),cPath.getHour(),cPath.getMinute());
			
			path=cPath;
		}
			
		return path;
	}
	*/


}
