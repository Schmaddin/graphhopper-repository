package com.graphhopper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.carrotsearch.hppc.cursors.IntCursor;
import com.graphhopper.chilango.ChilangoWeighting;
import com.graphhopper.chilango.EdgeInformation;
import com.graphhopper.routing.Path;
import com.graphhopper.routing.util.DefaultEdgeFilter;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.weighting.Weighting;
import com.graphhopper.storage.Graph;
import com.graphhopper.storage.SPTEntry;
import com.graphhopper.util.EdgeExplorer;
import com.graphhopper.util.EdgeIterator;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.FinishInstruction;
import com.graphhopper.util.Instruction;
import com.graphhopper.util.InstructionAnnotation;
import com.graphhopper.util.InstructionList;
import com.graphhopper.util.PointList;
import com.graphhopper.util.RoundaboutInstruction;
import com.graphhopper.util.Translation;

public class ChilangoPath extends Path {

	private int minute = 12;
	private int hour = 12;

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public HashMap<Integer, Set<Integer>> getBreakMap() {
		return breakMap;
	}

	public void setBreakMap(HashMap<Integer, Set<Integer>> breakMap) {
		this.breakMap = breakMap;
	}

	private HashMap<Integer, Set<Integer>> breakMap;

	public ChilangoPath(Graph graph, Weighting weighting) {
		super(graph, weighting);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Calls getDistance and adds the edgeId.
	 */
	@Override
	protected void processEdge(int edgeId, int adjNode, int prevEdgeId) {

		EdgeIteratorState iter = graph.getEdgeIteratorState(edgeId, adjNode);

		distance += iter.getDistance();
		if (weighting instanceof ChilangoWeighting)
			time += ((ChilangoWeighting) weighting).calcMillis(iter, false, prevEdgeId, false);
		else
			time += weighting.calcMillis(iter, false, prevEdgeId);

		System.out.println("edgeId: " + edgeId + " adj: " + adjNode + " prev: " + prevEdgeId + "  distance: "
				+ iter.getDistance() + "  +time:  " + weighting.calcMillis(iter, false, prevEdgeId) + " time: " + time);
		addEdge(edgeId);
	}

	/**
	 * Extracts the Path from the shortest-path-tree determined by sptEntry.
	 */
	@Override
	public Path extract() {
		super.extract();
		time = extractHelper(this, breakMap, hour, minute);

		ArrayList<Integer> edgesWithOperator = new ArrayList<>(); // saves the
		// edgesID
		// which
		// have
		// operators
		ArrayList<Set<Integer>> routesAvailableAtIndex = new ArrayList<>(); // edges
		// and
		// their
		// operators
		// ->
		// find
		// edgeId
		// by
		// using
		// index
		// on
		// edgesWithOperator

		// get through all edges
		List<EdgeIteratorState> edges = calcEdges();
		int prevEdge = EdgeIterator.NO_EDGE;

		long firstCalculation = 0;
		int loop = 0;
		for (EdgeIteratorState edge : edges) {
			long plusTime = ((ChilangoWeighting) getWeighting()).calcMillis(edge, false, prevEdge, false); // TODO
																												// Time
																												// dependent
			System.out.println("/////////////" + edge.getName() + " " + firstCalculation + " " + edge.getBaseNode()
					+ " " + edge.getAdjNode() + " " + getEdgeIds().get(loop));

			firstCalculation += plusTime;
			prevEdge = getEdgeIds().get(loop);

			// TODO at current time ;-) AT THE MOMENT JUST which are generally
			// available at this edge... edge should be almost safe as well ^^.
			long edgeOSM = EdgeInformation.getOriginalOSM(getEdgeIds().get(loop));
			if (edgeOSM > 0 && edgeOSM <= EdgeInformation.maxIndex) {
				Integer[] validate = EdgeInformation.getRoutes((int) edgeOSM);
				routesAvailableAtIndex.add(new HashSet<Integer>(Arrays.asList(validate)));
				edgesWithOperator.add(getEdgeIds().get(loop));
			} else {
			}

			loop++;
		}

		time = firstCalculation; // reset time

		System.out.println("//////total: " + firstCalculation);

		ArrayList<Set<Integer>> routesSameThanOneBefore = new ArrayList<>(); // Sets
		// for
		// finding
		// elements,
		// which
		// have
		// been
		// both
		// edges
		// (before
		// ->
		// current)
		routesSameThanOneBefore.add(new HashSet(routesAvailableAtIndex.get(0)));

		for (int loopCounter = 0; loopCounter < routesAvailableAtIndex.size(); loopCounter++) {
			routesSameThanOneBefore.add(routesAvailableAtIndex.get(loopCounter));
		}

		for (int loopCounter = 0; loopCounter < routesSameThanOneBefore.size() - 1; loopCounter++) {
			routesSameThanOneBefore.get(loopCounter).retainAll(routesAvailableAtIndex.get(loopCounter));

		}

		Set<Integer> toUse = new HashSet<Integer>();
		Set<Integer> bufferSet = new HashSet<Integer>();
		breakMap = new HashMap<Integer, Set<Integer>>();
		int lastBreak = 0;

		toUse = routesAvailableAtIndex.get(0);
		for (int loopCounter = 0; loopCounter < routesAvailableAtIndex.size(); loopCounter++) {
			System.out.print("No " + loopCounter + " ALL: ");
			for (Integer route : routesAvailableAtIndex.get(loopCounter))
				System.out.print(route + " ");
			System.out.println();

			System.out.print("SET last->now: ");
			for (Integer route : routesSameThanOneBefore.get(loopCounter))
				System.out.print(route + " ");
			System.out.println();

			bufferSet.clear();
			bufferSet.addAll(toUse);
			toUse.retainAll(routesSameThanOneBefore.get(loopCounter));

			if (loopCounter == routesAvailableAtIndex.size() - 1) {
				toUse.clear();
			}

			if (toUse.size() == 0) {
				System.out.println("Break at: " + loopCounter);

				System.out.print("USE TILL THAT: ");
				int waiting = 0;
				for (Integer route : bufferSet) {
					System.out.print(route + " ");

					// TODO correct time
					waiting += EdgeInformation.getRouteEdgeValue(route, hour, minute);
				}
				if (bufferSet.size() > 0) // TODO warum 0?
					waiting = waiting / bufferSet.size() / bufferSet.size();
				System.out.println("------We had to enter & wait at: " + lastBreak + " for them: " + waiting);
				System.out.println();

				time += waiting * 60000; // add waiting TODO time dependent

				// put into the the break Map (look at each ID, whether
				// something is in the break Map !!!)
				breakMap.put(edgesWithOperator.get(lastBreak), new HashSet(bufferSet));

				lastBreak = loopCounter;

				// reset toUse-Map to new Values
				toUse.addAll(routesAvailableAtIndex.get(loopCounter));

			}
		}


		return this;
	}

	public static long extractHelper(Path path, HashMap<Integer, Set<Integer>> breakMap, int hour, int minute) {
		ArrayList<Integer> edgesWithOperator = new ArrayList<>(); // saves the
																	// edgesID
																	// which
																	// have
																	// operators
		ArrayList<Set<Integer>> routesAvailableAtIndex = new ArrayList<>(); // edges
																			// and
																			// their
																			// operators
																			// ->
																			// find
																			// edgeId
																			// by
																			// using
																			// index
																			// on
																			// edgesWithOperator

		// get through all edges
		List<EdgeIteratorState> edges = path.calcEdges();
		int prevEdge = EdgeIterator.NO_EDGE;

		long firstCalculation = 0;
		int loop = 0;
		for (EdgeIteratorState edge : edges) {
			long plusTime = ((ChilangoWeighting) path.getWeighting()).calcMillis(edge, false, prevEdge, false); // TODO
																												// Time
																												// dependent
			System.out.println("/////////////" + edge.getName() + " " + firstCalculation + " " + edge.getBaseNode()
					+ " " + edge.getAdjNode() + " " + path.getEdgeIds().get(loop));

			firstCalculation += plusTime;
			prevEdge = path.getEdgeIds().get(loop);

			// TODO at current time ;-) AT THE MOMENT JUST which are generally
			// available at this edge... edge should be almost safe as well ^^.
			long edgeOSM = EdgeInformation.getOriginalOSM(path.getEdgeIds().get(loop));
			if (edgeOSM > 0 && edgeOSM <= EdgeInformation.maxIndex) {
				Integer[] validate = EdgeInformation.getRoutes((int) edgeOSM);
				routesAvailableAtIndex.add(new HashSet<Integer>(Arrays.asList(validate)));
				edgesWithOperator.add(path.getEdgeIds().get(loop));
			} else {
			}

			loop++;
		}

		long time = firstCalculation; // reset time

		System.out.println("//////total: " + firstCalculation);

		ArrayList<Set<Integer>> routesSameThanOneBefore = new ArrayList<>(); // Sets
																				// for
																				// finding
																				// elements,
																				// which
																				// have
																				// been
																				// both
																				// edges
																				// (before
																				// ->
																				// current)
		routesSameThanOneBefore.add(new HashSet(routesAvailableAtIndex.get(0)));

		for (int loopCounter = 0; loopCounter < routesAvailableAtIndex.size(); loopCounter++) {
			routesSameThanOneBefore.add(routesAvailableAtIndex.get(loopCounter));
		}

		for (int loopCounter = 0; loopCounter < routesSameThanOneBefore.size() - 1; loopCounter++) {
			routesSameThanOneBefore.get(loopCounter).retainAll(routesAvailableAtIndex.get(loopCounter));

		}

		Set<Integer> toUse = new HashSet<Integer>();
		Set<Integer> bufferSet = new HashSet<Integer>();
		breakMap = new HashMap<Integer, Set<Integer>>();
		int lastBreak = 0;

		toUse = routesAvailableAtIndex.get(0);
		for (int loopCounter = 0; loopCounter < routesAvailableAtIndex.size(); loopCounter++) {
			System.out.print("No " + loopCounter + " ALL: ");
			for (Integer route : routesAvailableAtIndex.get(loopCounter))
				System.out.print(route + " ");
			System.out.println();

			System.out.print("SET last->now: ");
			for (Integer route : routesSameThanOneBefore.get(loopCounter))
				System.out.print(route + " ");
			System.out.println();

			bufferSet.clear();
			bufferSet.addAll(toUse);
			toUse.retainAll(routesSameThanOneBefore.get(loopCounter));

			if (loopCounter == routesAvailableAtIndex.size() - 1) {
				toUse.clear();
			}

			if (toUse.size() == 0) {
				System.out.println("Break at: " + loopCounter);

				System.out.print("USE TILL THAT: ");
				int waiting = 0;
				for (Integer route : bufferSet) {
					System.out.print(route + " ");

					// TODO correct time
					waiting += EdgeInformation.getRouteEdgeValue(route, hour, minute);
				}
				if (bufferSet.size() > 0) // TODO warum 0?
					waiting = waiting / bufferSet.size() / bufferSet.size();
				System.out.println("------We had to enter & wait at: " + lastBreak + " for them: " + waiting);
				System.out.println();

				time += waiting * 60000; // add waiting TODO time dependent

				// put into the the break Map (look at each ID, whether
				// something is in the break Map !!!)
				breakMap.put(edgesWithOperator.get(lastBreak), new HashSet(bufferSet));

				lastBreak = loopCounter;

				// reset toUse-Map to new Values
				toUse.addAll(routesAvailableAtIndex.get(loopCounter));

			}
		}

		return time;
	}

	/**
	 * @return the list of instructions for this path.
	 */
	@Override
	public InstructionList calcInstructions(final Translation tr) {
		final InstructionList ways = new InstructionList(edgeIds.size() / 4, tr);
		if (edgeIds.isEmpty()) {
			if (isFound()) {
				ways.add(new FinishInstruction(nodeAccess, endNode));
			}
			return ways;
		}

		final int tmpNode = getFromNode();
		forEveryEdge(new EdgeVisitor() {
			/*
			 * We need three points to make directions
			 *
			 * (1)----(2) / / (0)
			 *
			 * 0 is the node visited at t-2, 1 is the node visited at t-1 and 2
			 * is the node being visited at instant t. orientation is the angle
			 * of the vector(1->2) expressed as atan2, while previousOrientation
			 * is the angle of the vector(0->1) Intuitively, if orientation is
			 * smaller than previousOrientation, then we have to turn right,
			 * while if it is greater we have to turn left. To make this
			 * algorithm work, we need to make the comparison by considering
			 * orientation belonging to the interval [ - pi +
			 * previousOrientation , + pi + previousOrientation ]
			 */
			private double prevLat = nodeAccess.getLatitude(tmpNode);
			private double prevLon = nodeAccess.getLongitude(tmpNode);
			private double doublePrevLat, doublePrevLong; // Lat and Lon of node
															// t-2
			private int prevNode = -1;
			private double prevOrientation;
			private Instruction prevInstruction;
			private boolean prevInRoundabout = false;
			private String name, prevName = null;
			private InstructionAnnotation annotation, prevAnnotation;
			private EdgeExplorer outEdgeExplorer = graph
					.createEdgeExplorer(new DefaultEdgeFilter(encoder, false, true));
			private int lastEdge = EdgeIterator.NO_EDGE;
			private long currentWaiting = 0;
			//
			private int internEDGE = -1;

			@Override
			public void next(EdgeIteratorState edge, int index) {
				// baseNode is the current node and adjNode is the next
				int adjNode = edge.getAdjNode();
				int baseNode = edge.getBaseNode();
				long flags = edge.getFlags();
				double adjLat = nodeAccess.getLatitude(adjNode);
				double adjLon = nodeAccess.getLongitude(adjNode);
				double latitude, longitude;

				PointList wayGeo = edge.fetchWayGeometry(3);
				boolean isRoundabout = encoder.isBool(flags, FlagEncoder.K_ROUNDABOUT);

				if (wayGeo.getSize() <= 2) {
					latitude = adjLat;
					longitude = adjLon;
				} else {
					latitude = wayGeo.getLatitude(1);
					longitude = wayGeo.getLongitude(1);
					assert Double.compare(prevLat, nodeAccess.getLatitude(baseNode)) == 0;
					assert Double.compare(prevLon, nodeAccess.getLongitude(baseNode)) == 0;
				}

				name = edge.getName();
				annotation = encoder.getAnnotation(flags, tr);

				// OWN
				internEDGE = edgeIds.get(index);
				currentWaiting = 0;
				if (breakMap.containsKey(internEDGE)) {
					String operators = "";
					int i = 0;
					for (Integer op : breakMap.get(internEDGE)) {
						if (i != 0)
							operators += " or ";
						operators += EdgeInformation.getRoute(op).getHeadSign();

						i++;

					}
					currentWaiting = EdgeInformation.getWaiting(breakMap.get(internEDGE), hour, minute);
					name = "  \n Get into Bus: " + operators + " (wait ~: " + currentWaiting + "min)" + "\n at street: "
							+ name.split("operated by:")[0];

					if (index > 0)
						lastEdge = edgeIds.get(index - 1);
					else
						lastEdge = EdgeIterator.NO_EDGE;

				}
				//

				if ((prevName == null) && (!isRoundabout)) // very first
															// instruction (if
															// not in
															// Roundabout)
				{

					int sign = Instruction.CONTINUE_ON_STREET;
					prevInstruction = new Instruction(sign, name, annotation, new PointList(10, nodeAccess.is3D()));
					ways.add(prevInstruction);
					prevName = name;
					prevAnnotation = annotation;

				} else if (isRoundabout) {
					// remark: names and annotations within roundabout are
					// ignored
					if (!prevInRoundabout) // just entered roundabout
					{
						int sign = Instruction.USE_ROUNDABOUT;
						RoundaboutInstruction roundaboutInstruction = new RoundaboutInstruction(sign, name, annotation,
								new PointList(10, nodeAccess.is3D()));
						if (prevName != null) {
							// check if there is an exit at the same node the
							// roundabout was entered
							EdgeIterator edgeIter = outEdgeExplorer.setBaseNode(baseNode);
							while (edgeIter.next()) {
								if ((edgeIter.getAdjNode() != prevNode)
										&& !encoder.isBool(edgeIter.getFlags(), FlagEncoder.K_ROUNDABOUT)) {
									roundaboutInstruction.increaseExitNumber();
									break;
								}
							}

							// previous orientation is last orientation before
							// entering roundabout
							prevOrientation = AC.calcOrientation(doublePrevLat, doublePrevLong, prevLat, prevLon);

							// calculate direction of entrance turn to determine
							// direction of rotation
							// right turn == counterclockwise and vice versa
							double orientation = AC.calcOrientation(prevLat, prevLon, latitude, longitude);
							orientation = AC.alignOrientation(prevOrientation, orientation);
							double delta = (orientation - prevOrientation);
							roundaboutInstruction.setDirOfRotation(delta);

						} else // first instructions is roundabout instruction
						{
							prevOrientation = AC.calcOrientation(prevLat, prevLon, latitude, longitude);
							prevName = name;
							prevAnnotation = annotation;
						}
						prevInstruction = roundaboutInstruction;
						ways.add(prevInstruction);
					}

					// Add passed exits to instruction. A node is counted if
					// there is at least one outgoing edge
					// out of the roundabout
					EdgeIterator edgeIter = outEdgeExplorer.setBaseNode(adjNode);
					while (edgeIter.next()) {
						if (!encoder.isBool(edgeIter.getFlags(), FlagEncoder.K_ROUNDABOUT)) {
							((RoundaboutInstruction) prevInstruction).increaseExitNumber();
							break;
						}
					}

				} else if (prevInRoundabout) // previously in roundabout but not
												// anymore
				{

					prevInstruction.setName(name);

					// calc angle between roundabout entrance and exit
					double orientation = AC.calcOrientation(prevLat, prevLon, latitude, longitude);
					orientation = AC.alignOrientation(prevOrientation, orientation);
					double deltaInOut = (orientation - prevOrientation);

					// calculate direction of exit turn to determine direction
					// of rotation
					// right turn == counterclockwise and vice versa
					double recentOrientation = AC.calcOrientation(doublePrevLat, doublePrevLong, prevLat, prevLon);
					orientation = AC.alignOrientation(recentOrientation, orientation);
					double deltaOut = (orientation - recentOrientation);

					prevInstruction = ((RoundaboutInstruction) prevInstruction).setRadian(deltaInOut)
							.setDirOfRotation(deltaOut).setExited();

					prevName = name;
					prevAnnotation = annotation;

				} else if (breakMap.containsKey(internEDGE)
						|| (((!name.equals(prevName)) || (!annotation.equals(prevAnnotation)))
								&& (EdgeInformation.getOriginalOSM(internEDGE) < 0
										|| EdgeInformation.getOriginalOSM(internEDGE) > EdgeInformation.maxIndex))) {
					prevOrientation = AC.calcOrientation(doublePrevLat, doublePrevLong, prevLat, prevLon);
					double orientation = AC.calcOrientation(prevLat, prevLon, latitude, longitude);
					orientation = AC.alignOrientation(prevOrientation, orientation);
					double delta = orientation - prevOrientation;
					double absDelta = Math.abs(delta);
					int sign;

					if (absDelta < 0.2) {
						// 0.2 ~= 11°
						sign = Instruction.CONTINUE_ON_STREET;

					} else if (absDelta < 0.8) {
						// 0.8 ~= 40°
						if (delta > 0)
							sign = Instruction.TURN_SLIGHT_LEFT;
						else
							sign = Instruction.TURN_SLIGHT_RIGHT;

					} else if (absDelta < 1.8) {
						// 1.8 ~= 103°
						if (delta > 0)
							sign = Instruction.TURN_LEFT;
						else
							sign = Instruction.TURN_RIGHT;

					} else if (delta > 0)
						sign = Instruction.TURN_SHARP_LEFT;
					else
						sign = Instruction.TURN_SHARP_RIGHT;
					prevInstruction = new Instruction(sign, name, annotation, new PointList(10, nodeAccess.is3D()));
					ways.add(prevInstruction);
					prevName = name;
					prevAnnotation = annotation;

				}

				updatePointsAndInstruction(edge, wayGeo);

				if (wayGeo.getSize() <= 2) {
					doublePrevLat = prevLat;
					doublePrevLong = prevLon;
				} else {
					int beforeLast = wayGeo.getSize() - 2;
					doublePrevLat = wayGeo.getLatitude(beforeLast);
					doublePrevLong = wayGeo.getLongitude(beforeLast);
				}

				prevInRoundabout = isRoundabout;
				prevNode = baseNode;
				prevLat = adjLat;
				prevLon = adjLon;

				boolean lastEdge = index == edgeIds.size() - 1;
				if (lastEdge) {
					if (isRoundabout) {
						// calc angle between roundabout entrance and finish
						double orientation = AC.calcOrientation(doublePrevLat, doublePrevLong, prevLat, prevLon);
						orientation = AC.alignOrientation(prevOrientation, orientation);
						double delta = (orientation - prevOrientation);
						((RoundaboutInstruction) prevInstruction).setRadian(delta);

					}
					ways.add(new FinishInstruction(nodeAccess, adjNode));
				}
			}

			private void updatePointsAndInstruction(EdgeIteratorState edge, PointList pl) {
				// skip adjNode
				int len = pl.size() - 1;
				for (int i = 0; i < len; i++) {
					prevInstruction.getPoints().add(pl, i);
				}
				double newDist = edge.getDistance();
				prevInstruction.setDistance(newDist + prevInstruction.getDistance());
				if (weighting instanceof ChilangoWeighting) {
					prevInstruction.setTime(((ChilangoWeighting) weighting).calcMillis(edge, false, lastEdge, false)
							+ prevInstruction.getTime() + currentWaiting * 60000);

				} else
					prevInstruction.setTime(
							weighting.calcMillis(edge, false, EdgeIterator.NO_EDGE) + prevInstruction.getTime());
			}
		});

		return ways;
	}

}
