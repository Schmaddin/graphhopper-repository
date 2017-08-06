package com.graphhopper.chilango.data.database;

import java.io.Serializable;

public enum SubmitType implements Serializable {
	route_not_found(0), route_not_exist(1), route_meta_wrong(2), route_time_wrong(3), route_points_wrong(
			4), route_frequency(5), route_alright(6), timeTable_alright(7), frequency_alright(8), route_ok(
					100), invalid(-1), route_gps_Validation(20), submit_undefined_task(999), submit_route(
							1000), submit_new_gps_route(1001), submit_new_draw_route(
									1002), submit_route_indication(1005), submit_base_indication(1006), moderation(15);

	private final int value;

	SubmitType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	

	public static SubmitType getByValue(int value) {
		switch (value) {
		case 0:
			return route_not_found;
		case 1:
			return route_not_exist;
		case 2:
			return route_meta_wrong;
		case 3:
			return route_time_wrong;
		case 4:
			return route_points_wrong;
		case 5:
			return route_frequency;
		case 6:
			return route_alright;
		case 7:
			return timeTable_alright;
		case 8:
			return frequency_alright;
		case 15:
			return moderation;
		case 20:
			return route_gps_Validation;
		case 100:
			return route_ok;
		case 999:
			return submit_undefined_task;
		case 1000:
			return submit_route;
		case 1001:
			return submit_new_gps_route;
		case 1002:
			return submit_new_draw_route;
		case 1005:
			return submit_route_indication;
		case 1006:
			return submit_base_indication;

		default:
			return invalid;

		}

	}
}
