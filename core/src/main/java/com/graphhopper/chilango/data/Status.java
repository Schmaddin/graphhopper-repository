package com.graphhopper.chilango.data;


public enum Status {
	Not_Registered(-1),Beginner(0), Advanzed(10), Expert(20), Moderator(30), Master(50);

	private int statusValue;

	public int getStatusValue() {
		return statusValue;
	}


	Status(int statusValue) {
		this.statusValue = statusValue;
	}
	
	public static Status getStatusByValue(int statusValue){
		switch(statusValue){
		case 0:
			return Status.Beginner;
		case 10:
			return Status.Advanzed;
		case 20: 
			return Status.Expert;
		case 30:
			return Status.Moderator;
		case 50:
			return Status.Master;
		case -1:
			return Status.Not_Registered;
		default:
			return Status.Beginner;
		}
	}

}
