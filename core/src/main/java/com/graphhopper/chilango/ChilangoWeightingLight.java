package com.graphhopper.chilango;

import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.HintsMap;
import com.graphhopper.util.EdgeIteratorState;

public class ChilangoWeightingLight extends ChilangoWeighting {
	
	public final static String name="chilango_weighting_light";

	public ChilangoWeightingLight(FlagEncoder encoder) {
		super(encoder);
		System.out.println("Chilango-Weighting-Light");
	}
	
	public ChilangoWeightingLight(FlagEncoder encoder, HintsMap hintsMap) {
		super(encoder,hintsMap);

		System.out.println("Chilango-Weighting-Light");
	}

	@Override
	public double calcWeight(EdgeIteratorState edgeState, boolean reverse, int prevOrNextEdgeId) {
		double speed = reverse ? flagEncoder.getReverseSpeed(edgeState.getFlags())
				: flagEncoder.getSpeed(edgeState.getFlags());
		if (speed == 0)
			return Double.POSITIVE_INFINITY;

		double time = edgeState.getDistance() * 3600 / speed * SPEED_CONV;
		
		
		// get whether it is a chilangoRoute
		int type = (int) this.flagEncoder.getLong(edgeState.getFlags(), ChilangoFlacEncoder.K_CHILANGO);
		
		if(type==0)
			return time;
		
		int oldType = -1;
		if (graph != null && prevOrNextEdgeId > 0) { // richtige Richtung ???
			oldType = (int) this.flagEncoder.getLong(
					graph.getEdgeIteratorState(prevOrNextEdgeId, Integer.MIN_VALUE).getFlags(),
					ChilangoFlacEncoder.K_CHILANGO);
		}

		if (oldType != -1 && oldType != type)
			time += EdgeCostCalculator.getWaitingTime(-1, (int) type, clock, hopper.getOSMWay(edgeState.getEdge()));
			
		// add direction penalties at start/stop/via points
		
		boolean unfavoredEdge = edgeState.getBool(EdgeIteratorState.K_UNFAVORED_EDGE, false);
		if (unfavoredEdge)
			time += headingPenalty;

		return time;
	}

	@Override
	public String getName() {
		return name;
	}
	

}
