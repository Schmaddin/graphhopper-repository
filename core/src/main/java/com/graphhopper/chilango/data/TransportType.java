package com.graphhopper.chilango.data;

public enum TransportType{
    official(0,"official"), metrobús(2,"Metrobús"), rtp(3,"RTP"), metro(1,"Metro"), trolebús(6,"Trolebús"), microbús(4,"Microbús"), autobús(11,"Autobús"),vagoneta(12,"Combi"),other(-1,""), mexibús(10,"mexibús"), trenligero(5,"Tren Ligero"),pumabús(7,"Pumabús"),tren_suburbano(8,"Suburbano");

    private final int value;
    private final String name;
    
    private TransportType(int value,String name) {
        this.value = value;
        this.name = name;
    }
    
    public static TransportType getTypeByValue(int value){
        for(TransportType type:values()){
            if(value==type.getValue())
                return type;
        }
        return TransportType.other;
    }

    public int getValue() {
        return value;
    }
    
    public String getName() {
    	return name;
    }
    
    public boolean isOfficial(){
    	if(this==official || this == metro || this == rtp || this == trolebús || this == mexibús || this == tren_suburbano || this == pumabús || this == trenligero || this == metrobús)
    		return true;
    	
		return false;
    	
    }
}
