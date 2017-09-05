package com.graphhopper.chilango.data;

public enum Status {
	Not_Registered(-1), Beginner(0), Advanzed(15), Expert(30), Moderator(50), Master(60);

	public static final float MOD_POINT = 40000.0f;
	public static final float PROFI_POINT = 18000.0f;
	public static final float ADVANZED_POINT = 8000.0f;
	private int statusValue;

	public int getStatusValue() {
		return statusValue;
	}

	Status(int statusValue) {
		this.statusValue = statusValue;
	}

	public void setStatus(int statusValue) {
		this.statusValue = statusValue;
	}

	public static Status getStatusByValue(int statusValue) {

		if (statusValue < 15)

			return Status.Beginner;
		else if (statusValue < 30)
			return Status.Advanzed;
		else if (statusValue < 50)
			return Status.Expert;
		else if (statusValue < 60)
			return Status.Moderator;
		else if (statusValue == 60)
			return Status.Master;
		else
			return Status.Beginner;
	}

	public static int calculateStatusValue(int points,int status) {
		if(status==60)  // moderater and master can not decreaseA
			return 60;
		if(status>=50 && points <40000) 
				return 50;
		
		if(points<ADVANZED_POINT) // beginner
			return (int)((float)points/ADVANZED_POINT*15);
		if(points<PROFI_POINT) // advanzed
			return 15+((int)(((float)points-ADVANZED_POINT)/(PROFI_POINT-ADVANZED_POINT)*15.0f));
		if(points<MOD_POINT)  // profi
			return 30+((int)(((float)points-PROFI_POINT)/(MOD_POINT-PROFI_POINT)*20.0f));
		else
			return 50;
	}

}
