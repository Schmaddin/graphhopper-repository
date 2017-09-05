package com.graphhopper.chilango.data;

import com.graphhopper.chilango.data.database.SubmitType;

public class PointSystem {
	public final static double HOME_FACTOR = 1.2;
	public final static double UNKNOWN_AREA = 2.0;

	public final static int CHECK_IN_POINTS_PER_KM = 20;
	public final static int FEEDBACK_NORMAL = 100;
	public final static int FEEDBACK_WITH_CHANGE = 250;

	public final static int INDICATE_ROUTE = 500;
	public final static int INDICATE_BUS_STOP = 300;
	public final static int RECORD_ROUTE = 2000;
	public final static int DRAW_ROUTE = 1500;

	public final static int MODERATION = 200;

	public static int getRevisorPoints(SubmitType type, boolean change) {
		switch (type) {
		case frequency_alright:
			return FEEDBACK_NORMAL;

		case invalid:
			return 0;

		case moderation:
			return MODERATION;

		case route_alright:
			return FEEDBACK_NORMAL;

		case route_frequency:
			return FEEDBACK_NORMAL;

		case route_gps_Validation: // needs to be calculated by the KM
			return CHECK_IN_POINTS_PER_KM;

		case route_meta_wrong:
			return FEEDBACK_NORMAL;

		case route_not_exist:
			return FEEDBACK_NORMAL;

		case route_not_found:
			return FEEDBACK_NORMAL;
		case route_ok:
			return FEEDBACK_NORMAL;

		case route_points_wrong:
			return FEEDBACK_NORMAL;

		case route_time_wrong:
			return FEEDBACK_NORMAL;

		case submit_route_indication:
			return INDICATE_ROUTE;
		case submit_undefined_task:
			break;
		case timeTable_alright:
			return FEEDBACK_NORMAL;

		default:
			break;

		}

		return 0;
	}

	public static int getCreationPoints(SubmitType type, boolean change) {
		switch (type) {
		case submit_base_indication:
			return INDICATE_BUS_STOP;

		case submit_new_draw_route:
			return DRAW_ROUTE;

		case submit_new_gps_route:
			return RECORD_ROUTE;

		case submit_route:
			return DRAW_ROUTE;

		case route_points_wrong:
			if(change)
			return FEEDBACK_WITH_CHANGE-FEEDBACK_NORMAL;
			break;

		case route_time_wrong:
			if(change)
			return FEEDBACK_WITH_CHANGE-FEEDBACK_NORMAL;
			break;

		case route_meta_wrong:
			if(change)
			return FEEDBACK_WITH_CHANGE-FEEDBACK_NORMAL;
			break;
		}

		return 0;
	}
}
