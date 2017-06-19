package com.graphhopper.chilango;

import com.carrotsearch.hppc.IntSet;
import com.graphhopper.GraphHopper;
import com.graphhopper.coll.GHIntHashSet;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.weighting.AbstractWeighting;
import com.graphhopper.routing.weighting.FastestWeighting;
import com.graphhopper.storage.Graph;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.PMap;
import com.graphhopper.util.Parameters.Routing;

// own weighting (has to be registered in Graphhopper
public class ChilangoWeighting extends AbstractWeighting {
	/**
	 * Converting to seconds is not necessary but makes adding other penalties
	 * easier (e.g. turn costs or traffic light costs etc)
	 */
	protected final static double SPEED_CONV = 3.6;

	public final static String name="chilango_weighting";
	
	public int clock = 100;
	private int day = 0;

	protected final double headingPenalty;
	private final long headingPenaltyMillis;
	private final double maxSpeed;

	private int operatorOld = -1;
	private int operatorNew = -1;

	protected final IntSet visitedEdges = new GHIntHashSet();

	private double edgePenaltyFactor = 5.0;

	protected Graph graph = null;

	protected GraphHopper hopper;

	private boolean withOperators;

	public ChilangoWeighting(FlagEncoder encoder) {
		this(encoder, new PMap(0));
	}

	public ChilangoWeighting(FlagEncoder encoder, PMap pMap) {
		super(encoder);
		headingPenalty = pMap.getDouble(Routing.HEADING_PENALTY, Routing.DEFAULT_HEADING_PENALTY);
		headingPenaltyMillis = Math.round(headingPenalty * 1000);
		maxSpeed = encoder.getMaxSpeed() / SPEED_CONV;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;

		System.err.println("EDGEINFORMATION: " + (int) EdgeInformation.getEdgeValue(1, 14, 3));
	}

	public void setHopper(GraphHopper hopper) {
		this.hopper = hopper;
		System.err.println(hopper.getGraphHopperLocation());
		System.err.println("Hopper Set!");
		EdgeInformation.initialize(hopper);

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

		if (type == 0) {
			// add direction penalties at start/stop/via points
			boolean unfavoredEdge = edgeState.getBool(EdgeIteratorState.K_UNFAVORED_EDGE, false);
			if (unfavoredEdge)
				time += headingPenalty;

			return time;

		} 

		int oldType = -1;
		if (graph != null && prevOrNextEdgeId > 0) { // richtige Richtung ???
			oldType = (int) this.flagEncoder.getLong(
					graph.getEdgeIteratorState(prevOrNextEdgeId, Integer.MIN_VALUE).getFlags(),
					ChilangoFlacEncoder.K_CHILANGO);
		}

		if (oldType != -1 && oldType != type)
			time += /*
					 * RouteData.getExitTime(-1, (int) oldType, clock) +
					 * RouteData.getAccessTime(-1, (int) type, clock, true)
					 */
					+EdgeCostCalculator.getWaitingTime(-1, (int) type, clock, hopper.getOSMWay(edgeState.getEdge()));
		else
			time += EdgeCostCalculator.getContinueTime((int) type, clock); //

		// add direction penalties at start/stop/via points
		boolean unfavoredEdge = edgeState.getBool(EdgeIteratorState.K_UNFAVORED_EDGE, false);
		if (unfavoredEdge)
			time += headingPenalty;

		return time;
	}

	public long calcMillis(EdgeIteratorState edgeState, boolean reverse, int prevOrNextEdgeId, int currentOperator,
			int oldOperator, int currentHour, int currentMinute) {
		operatorOld = operatorOld;
		operatorNew = operatorNew;
		withOperators = true;
		long time = calcMillis(edgeState, reverse, prevOrNextEdgeId);
		withOperators = false;
		return time;
	}

	public long calcMillis(EdgeIteratorState edgeState, boolean reverse, int prevOrNextEdgeId, boolean addWaiting) {
		if (!withOperators) {
			operatorOld = -1;
			operatorNew = -1;
		}
		// TODO move this to AbstractWeighting?
		long time = 0;
		boolean unfavoredEdge = edgeState.getBool(EdgeIteratorState.K_UNFAVORED_EDGE, false);

		if (unfavoredEdge)
			time += headingPenaltyMillis;

		// get whether it is a chilangoRoute
		int type = (int) this.flagEncoder.getLong(edgeState.getFlags(), ChilangoFlacEncoder.K_CHILANGO);

		System.out.println("" + type + " " + EdgeCostCalculator.operatorToString(type) + "  " + edgeState.getName()
				+ " originalEdge: " + hopper.getOSMWay(edgeState.getEdge()));
		int oldType = -1;
		if (graph != null && prevOrNextEdgeId > 0) { // richtige Richtung ???
			oldType = (int) this.flagEncoder.getLong(
					graph.getEdgeIteratorState(prevOrNextEdgeId, Integer.MIN_VALUE).getFlags(),
					ChilangoFlacEncoder.K_CHILANGO);
		}

		if (oldType != -1 && oldType != type) {
			time += EdgeCostCalculator.getExitTime(-1, (int) oldType, clock)
					+ EdgeCostCalculator.getAccessTime(-1, (int) type, clock, true);
			if (addWaiting == true)
				time += EdgeCostCalculator.getWaitingTime(-1, (int) type, clock, hopper.getOSMWay(edgeState.getEdge()));

		} else
			time += EdgeCostCalculator.getContinueTime((int) type, clock);

		return time + super.calcMillis(edgeState, reverse, prevOrNextEdgeId);
	}

	@Override
	public long calcMillis(EdgeIteratorState edgeState, boolean reverse, int prevOrNextEdgeId) {
		return calcMillis(edgeState, reverse, prevOrNextEdgeId, true);
	}

	@Override
	public double getMinWeight(double distance) {
		return distance / maxSpeed;
	}
	
 

}
