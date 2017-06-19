package com.graphhopper.chilango.data;

import java.io.Serializable;

/**
 * first Index: 0-6 monday-sunday second Index: 0-47
 * 
 * @author martinwurflein
 *
 */
public class RouteFrequency implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final byte frequency[][] = new byte[7][48];
	public byte trustLevel;

	public RouteFrequency() {
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 48; j++) {
				frequency[i][j] = 0;
			}
		}
	}

	/*
	public void setByOther(RouteFrequency other) {
		frequency = other.frequency;
		trust = other.trust;
	}*/

	public void printIndex() {
		System.out.println("/////////////////");
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 46; j += 2) {
				System.out.print("\t" + frequency[i][j] + " " + frequency[i][j + 1]);
			}

			System.out.println();

		}
		System.out.println("---------");
	}

	public void applyByBound(RouteTimeBound timeBound, byte sFrequency) {
		for (int i = 0; i < 7; i++) {

			int lastHour = timeBound.lastHour[i];
			if (lastHour < timeBound.startHour[i])
				lastHour += 24;
			for (byte h = timeBound.startHour[i]; h < lastHour; h++) {

				byte mIndex = 0;
				if (h == timeBound.startHour[i]) {
					if (timeBound.startMinute[i] < 20) {
						mIndex = 0;
					} else {
						mIndex = 1;
					}
				}

				byte mLastIndex = 1;
				if (h == lastHour) {
					if (timeBound.lastMinute[i] < 35)
						mLastIndex = 0;
				}

				for (; mIndex <= mLastIndex; mIndex++) {
					frequency[i][(h * 2 + mIndex) % 48] = sFrequency;
				}

			}
		}
	}
}
