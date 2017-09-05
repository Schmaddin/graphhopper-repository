package com.graphhopper.chilango.data;

import java.util.List;

public class TrustLevel {
	
	public static byte calculateTrust(byte contributionValue,byte ratingValue,int n){
		
		if(n<=2)
			return contributionValue;
		
		double percentageUser=Math.pow(n-2, 2)/Math.pow(n, 2);
		
		return (byte) (percentageUser*ratingValue+(1.0-percentageUser)*contributionValue);
	}
	
	public static byte calculateRatingValue(List<Float> values){
		float sum=0;
		for(float value:values)
			sum+=Math.abs(value);
		
		float positiveSum=0.0f;
		
		for(float value:values)
			positiveSum+=value>0?value:0;
			
		return (byte)((positiveSum/sum)*100.0f);
	}
	
	public static byte trustByDistance(float distance) {
		byte trust=0;
		if(distance<500)
			trust+=40;
		else if(distance <3000)
		{
			trust+=(byte)(40.0f-((distance-500.0f)/2500.0f*40.0f));
		}
		return trust;
	}

}
