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
package com.graphhopper.chilango;

import com.graphhopper.reader.ReaderRelation;
import com.graphhopper.reader.ReaderWay;
import com.graphhopper.routing.util.AbstractFlagEncoder;
import com.graphhopper.routing.util.EncodedDoubleValue;
import com.graphhopper.routing.util.EncodedValue;
import com.graphhopper.routing.util.PriorityCode;
import com.graphhopper.routing.weighting.PriorityWeighting;
import com.graphhopper.util.PMap;

import java.util.*;

import static com.graphhopper.routing.util.PriorityCode.*;

/**
 * Defines bit layout for pedestrians (speed, access, surface, ...). Here we put
 * a penalty on unsafe roads only. If you wish to also prefer routes due to
 * beauty like hiking routes use the HikeFlagEncoder instead.
 * <p>
 *
 * @author Peter Karich
 * @author Nop
 * @author Karl Hübner
 */
public class ChilangoFlacEncoder extends AbstractFlagEncoder {
	static final int SLOW_SPEED_FOOT = 2;
	static final int MEAN_SPEED_FOOT = 4;
	static final int MEAN_BUS_SPEED = 20;
	static final int MAX_BUS_SPEED = 60;
	static final int MEAN_METROBUS = 40;
	static final int FERRY_SPEED = 10;
	static final int MEAN_METRO = 60;

	// for setting the bit
	public static final int K_CHILANGO = 10;
	final Set<String> safeHighwayTags = new HashSet<String>();
	final Set<String> allowedHighwayTags = new HashSet<String>();
	final Set<String> avoidHighwayTags = new HashSet<String>();
	// convert network tag of hiking routes into a way route code
	final Map<String, Integer> hikingNetworkToCode = new HashMap<String, Integer>();
	protected HashSet<String> sidewalkValues = new HashSet<String>(5);
	protected HashSet<String> sidewalksNoValues = new HashSet<String>(5);
	private EncodedValue priorityWayEncoder;
	private EncodedValue relationCodeEncoder;
	private Set<String> chilangoTags = new HashSet<String>();

	EncodedValue chilangoTransportType;

	/**
	 * Should be only instantiated via EncodingManager
	 */
	public ChilangoFlacEncoder() {
		this(5, 2);
	}

	public ChilangoFlacEncoder(PMap properties) {
		this((int) properties.getLong("speedBits", 5), properties.getDouble("speedFactor", 2));
		this.properties = properties;
		this.setBlockFords(properties.getBool("block_fords", true));
	}

	public ChilangoFlacEncoder(String propertiesStr) {
		this(new PMap(propertiesStr));
	}

	public ChilangoFlacEncoder(int speedBits, double speedFactor) {
		super(speedBits, speedFactor, 0);
		restrictions.addAll(Arrays.asList("foot", "access"));
		restrictedValues.add("private");
		restrictedValues.add("no");
		restrictedValues.add("restricted");
		restrictedValues.add("military");
		restrictedValues.add("emergency");

		intendedValues.add("yes");
		intendedValues.add("designated");
		intendedValues.add("official");
		intendedValues.add("permissive");

		sidewalksNoValues.add("no");
		sidewalksNoValues.add("none");
		// see #712
		sidewalksNoValues.add("separate");

		sidewalkValues.add("yes");
		sidewalkValues.add("both");
		sidewalkValues.add("left");
		sidewalkValues.add("right");

		setBlockByDefault(false);
		potentialBarriers.add("gate");

		safeHighwayTags.add("footway");
		safeHighwayTags.add("path");
		safeHighwayTags.add("steps");
		safeHighwayTags.add("pedestrian");
		safeHighwayTags.add("living_street");
		safeHighwayTags.add("track");
		safeHighwayTags.add("residential");
		safeHighwayTags.add("service");

		avoidHighwayTags.add("trunk");
		avoidHighwayTags.add("trunk_link");
		avoidHighwayTags.add("primary");
		avoidHighwayTags.add("primary_link");
		avoidHighwayTags.add("secondary");
		avoidHighwayTags.add("secondary_link");
		avoidHighwayTags.add("tertiary");
		avoidHighwayTags.add("tertiary_link");

		// for weight maps
		chilangoTags.add("hoponoff");
		chilangoTags.add("metrobus");
		chilangoTags.add("metro");
		chilangoTags.add("RTP");

		// for now no explicit avoiding #257
		// avoidHighwayTags.add("cycleway");
		allowedHighwayTags.addAll(safeHighwayTags);
		allowedHighwayTags.addAll(avoidHighwayTags);
		allowedHighwayTags.add("cycleway");
		allowedHighwayTags.add("unclassified");
		allowedHighwayTags.add("road");
		allowedHighwayTags.addAll(chilangoTags);

		// disallowed in some countries
		// allowedHighwayTags.add("bridleway");

		hikingNetworkToCode.put("iwn", UNCHANGED.getValue());
		hikingNetworkToCode.put("nwn", UNCHANGED.getValue());
		hikingNetworkToCode.put("rwn", UNCHANGED.getValue());
		hikingNetworkToCode.put("lwn", UNCHANGED.getValue());

		maxPossibleSpeed = MAX_BUS_SPEED;

		init();
	}

	@Override
	public int getVersion() {
		return 2;
	}

	// wird über den EncodeManager gesammelt und gehandelt (index und shift)
	// speist sich aber auch aus den ergebnissen!
	// wie viel wir füllen können weiß ich nicht genau! Müsste aber etwas puffer
	// sein. z.b. public route && night-mode (um früher auszusondieren für
	// Zeiten zwischen 12 und 5 Uhr früh)
	@Override
	public int defineWayBits(int index, int shift) {
		// first two bits are reserved for route handling in superclass
		shift = super.defineWayBits(index, shift);
		// larger value required - ferries are faster than pedestrians
		speedEncoder = new EncodedDoubleValue("Speed", shift, speedBits, speedFactor, MEAN_SPEED_FOOT,
				maxPossibleSpeed);
		shift += speedEncoder.getBits();

		priorityWayEncoder = new EncodedValue("PreferWay", shift, 3, 1, 0, 7);
		shift += priorityWayEncoder.getBits();

		chilangoTransportType = new EncodedValue("chilangoType", shift, 3, 1, 0, 7);
		shift += chilangoTransportType.getBits();

		System.out.println("ChilangoFlacEncoder: Way bits used: "+shift);
		return shift;
	}

	@Override
	public long getLong(long flags, int key) {
		if (key == K_CHILANGO) {
			return chilangoTransportType.getValue(flags);
		}

		return super.getLong(flags, key);
	}

	@Override
	public long setLong(long flags, int key, long value) {
		if (key == K_CHILANGO) {
			return chilangoTransportType.setValue(flags, value);
		}
		return super.setLong(flags, key, value);
	}

	@Override
	public int defineRelationBits(int index, int shift) {
		relationCodeEncoder = new EncodedValue("RelationCode", shift, 3, 1, 0, 7);
		return shift + relationCodeEncoder.getBits();
	}

	/**
	 * Foot flag encoder does not provide any turn cost / restrictions
	 */
	@Override
	public int defineTurnBits(int index, int shift) {
		return shift;
	}

	/**
	 * Foot flag encoder does not provide any turn cost / restrictions
	 * <p>
	 *
	 * @return <code>false</code>
	 */
	@Override
	public boolean isTurnRestricted(long flag) {
		return false;
	}

	/**
	 * Foot flag encoder does not provide any turn cost / restrictions
	 * <p>
	 *
	 * @return 0
	 */
	@Override
	public double getTurnCost(long flag) {
		return 0;
	}

	@Override
	public long getTurnFlags(boolean restricted, double costs) {
		return 0;
	}
	/*
	 * @Override public long setBool(long flags, int key, boolean value) { if
	 * (key == K_CHILANGO) { return value ? flags | chilangoTransportType :
	 * flags & ~chilangoTransportType;
	 * 
	 * }
	 * 
	 * return super.setBool(flags, key, value); }
	 * 
	 * @Override public boolean isBool(long flags, int key) { if (key ==
	 * K_CHILANGO) return (flags & chilangoTransportType) != 0;
	 * 
	 * return super.isBool(flags, key);
	 * 
	 * }
	 */

	/**
	 * Some ways are okay but not separate for pedestrians.
	 * 
	 * Here the way tags are parsed and some are thrown out. Through adding
	 * "hoponoff" in the allowed list, our routes are not thrown out.
	 * <p>
	 */
	@Override
	public long acceptWay(ReaderWay way) {
		String highwayValue = way.getTag("highway");
		if (highwayValue == null) {
			if (way.hasTag("route", ferries)) {
				String footTag = way.getTag("foot");
				if (footTag == null || "yes".equals(footTag))
					return acceptBit | ferryBit;
			}

			// special case not for all acceptedRailways, only platform
			if (way.hasTag("railway", "platform"))
				return acceptBit;

			return 0;
		}

		String sacScale = way.getTag("sac_scale");
		if (sacScale != null) {
			if (!"hiking".equals(sacScale) && !"mountain_hiking".equals(sacScale)
					&& !"demanding_mountain_hiking".equals(sacScale) && !"alpine_hiking".equals(sacScale))
				// other scales are too dangerous, see
				// http://wiki.openstreetmap.org/wiki/Key:sac_scale
				return 0;
		}

		if (way.hasTag("sidewalk", sidewalkValues))
			return acceptBit;

		// no need to evaluate ferries or fords - already included here
		if (way.hasTag("foot", intendedValues))
			return acceptBit;

		////////// highway v="?" muss in den allowedHighwayTags drin sein!
		if (!allowedHighwayTags.contains(highwayValue))
			return 0;

		if (way.hasTag("motorroad", "yes"))
			return 0;

		// do not get our feet wet, "yes" is already included above
		if (isBlockFords() && (way.hasTag("highway", "ford") || way.hasTag("ford")))
			return 0;

		// check access restrictions
		if (way.hasTag(restrictions, restrictedValues)
				&& !getConditionalTagInspector().isRestrictedWayConditionallyPermitted(way))
			return 0;

		if (getConditionalTagInspector().isPermittedWayConditionallyRestricted(way))
			return 0;
		else
			return acceptBit;
	}

	// wir müssen ja keine Relatio nutzen? oder? -> ignorieren!
	@Override
	public long handleRelationTags(ReaderRelation relation, long oldRelationFlags) {
		int code = 0;
		if (relation.hasTag("route", "hiking") || relation.hasTag("route", "foot")) {
			Integer val = hikingNetworkToCode.get(relation.getTag("network"));
			if (val != null)
				code = val;
			else
				code = hikingNetworkToCode.get("lwn");
		} else if (relation.hasTag("route", "ferry")) {
			code = PriorityCode.AVOID_IF_POSSIBLE.getValue();
		}

		int oldCode = (int) relationCodeEncoder.getValue(oldRelationFlags);
		if (oldCode < code)
			return relationCodeEncoder.setValue(0, code);
		return oldRelationFlags;
	}

	@Override
	public long handleWayTags(ReaderWay way, long allowed, long relationFlags) {
		if (!isAccept(allowed))
			return 0;

		long flags = 0;
		if (!isFerry(allowed)) {
			String sacScale = way.getTag("sac_scale");
			if (sacScale != null) {
				if ("hiking".equals(sacScale))
					flags = speedEncoder.setDoubleValue(flags, MEAN_SPEED_FOOT);
				else
					flags = speedEncoder.setDoubleValue(flags, SLOW_SPEED_FOOT);
			} else {
				flags = speedEncoder.setDoubleValue(flags, MEAN_SPEED_FOOT);
			}
			flags |= directionBitMask;// setzt er es automatisch auf both ways?
										// 11 ->denke ja!

			/*
			 * aus CarFlagEncoder if (isOneway(way) || isRoundabout) { if
			 * (isBackwardOneway(way)) flags |= backwardBit;
			 * 
			 * if (isForwardOneway(way)) flags |= forwardBit; } else flags |=
			 * directionBitMask;
			 */
			boolean isRoundabout = way.hasTag("junction", "roundabout");
			if (isRoundabout)
				flags = setBool(flags, K_ROUNDABOUT, true);

			String highway = way.getTag("highway");
			if (chilangoTags.contains(highway)) {
				if (highway.equals("hoponoff")) {
					flags = speedEncoder.setDoubleValue(flags, MEAN_BUS_SPEED);
					flags = setLong(flags, K_CHILANGO, 1);
				}
				if (highway.equals("metrobus")) {
					flags = speedEncoder.setDoubleValue(flags, MEAN_METROBUS);
					flags = setLong(flags, K_CHILANGO, 2);
				}
				if (highway.equals("metro")) {
					flags = speedEncoder.setDoubleValue(flags, MEAN_METRO);
					flags = setLong(flags, K_CHILANGO, 3);
				}
				// TODO public system

			} else
				flags = setLong(flags, K_CHILANGO, 0);
		} else {
			double ferrySpeed = getFerrySpeed(way, SLOW_SPEED_FOOT, MEAN_SPEED_FOOT, FERRY_SPEED);
			flags = setSpeed(flags, ferrySpeed);
			flags |= directionBitMask;
		}

		int priorityFromRelation = 0;
		if (relationFlags != 0)
			priorityFromRelation = (int) relationCodeEncoder.getValue(relationFlags);

		flags = priorityWayEncoder.setValue(flags, handlePriority(way, priorityFromRelation));
		return flags;
	}

	@Override
	public double getDouble(long flags, int key) {
		switch (key) {
		case PriorityWeighting.KEY:
			return (double) priorityWayEncoder.getValue(flags) / BEST.getValue();
		default:
			return super.getDouble(flags, key);
		}
	}

	protected int handlePriority(ReaderWay way, int priorityFromRelation) {
		TreeMap<Double, Integer> weightToPrioMap = new TreeMap<Double, Integer>();
		if (priorityFromRelation == 0)
			weightToPrioMap.put(0d, UNCHANGED.getValue());
		else
			weightToPrioMap.put(110d, priorityFromRelation);

		collect(way, weightToPrioMap);

		// pick priority with biggest order value
		return weightToPrioMap.lastEntry().getValue();
	}

	/**
	 * @param weightToPrioMap
	 *            associate a weight with every priority. This sorted map allows
	 *            subclasses to 'insert' more important priorities as well as
	 *            overwrite determined priorities.
	 */
	void collect(ReaderWay way, TreeMap<Double, Integer> weightToPrioMap) {
		String highway = way.getTag("highway");
		if (way.hasTag("foot", "designated"))
			weightToPrioMap.put(100d, PREFER.getValue());

		double maxSpeed = getMaxSpeed(way);
		if (safeHighwayTags.contains(highway) || maxSpeed > 0 && maxSpeed <= 20) {
			weightToPrioMap.put(40d, PREFER.getValue());
			if (way.hasTag("tunnel", intendedValues)) {
				if (way.hasTag("sidewalk", sidewalksNoValues))
					weightToPrioMap.put(40d, AVOID_IF_POSSIBLE.getValue());
				else
					weightToPrioMap.put(40d, UNCHANGED.getValue());
			}
		} else if (maxSpeed > 50 || avoidHighwayTags.contains(highway)) {
			if (!way.hasTag("sidewalk", sidewalkValues))
				weightToPrioMap.put(45d, AVOID_IF_POSSIBLE.getValue());
		}

		if (way.hasTag("bicycle", "official") || way.hasTag("bicycle", "designated"))
			weightToPrioMap.put(44d, AVOID_IF_POSSIBLE.getValue());

		if (chilangoTags.contains(highway))
			weightToPrioMap.put(120.0, BEST.getValue());

	}

	@Override
	public boolean supports(Class<?> feature) {
		if (super.supports(feature))
			return true;

		return PriorityWeighting.class.isAssignableFrom(feature);
	}

	@Override
	public String toString() {
		return "chilango";
	}
}
