package com.graphhopper.chilango.data;

public class TransportTypeHelper {

    public static boolean checkTransportType(String transportType, TransportType type) {
        TransportType official=null;
        TransportType thisType=null;

        switch (transportType.toLowerCase())
        {
            case "metro":
                official= TransportType.official;
                break;
            case "trolebús":

                official= TransportType.official;
                break;

            case "mb":

                official= TransportType.official;
                break;

        }


        if(official==type)
            return true;
        if(type==thisType)
            return true;
        else return false;

    }

    public static TransportType getTransportType(String type)
    {
        switch (type.toLowerCase())
        {
            case "metro":
                return TransportType.metro;
            case "trolebús":
                return TransportType.trolebús;
            case "ste":
                return TransportType.trolebús;
            case "mb":
                return TransportType.metrobús;
            case "vagoneta/combi":
                return TransportType.vagoneta;
            case "autobãºs":
                return TransportType.autobús;
            case "microbãºs":
                return TransportType.microbús;
            case "mexibús":
                return TransportType.mexibús;
            case "metrobús":
                return TransportType.metrobús;
            case "rtp":
                return TransportType.rtp;
            case "tren ligero":
                return TransportType.trenligero;
            case "microbús":
                return TransportType.microbús;
            case "tren suburbano":
                return TransportType.tren_suburbano;
            case "sub":
            	return TransportType.tren_suburbano;
            case "pumabús":
                return TransportType.pumabús;
            default:
                return TransportType.autobús;

        }
    }

    public static String pricePerUnit(TransportType type){
        switch (type){
            case official:
                return "2 - 6 pesos";
            case metro:
                return "5 pesos";
            case metrobús:
                return "6 pesos";
            case microbús:
                return "5 - 6.50 pesos";
            case autobús:
                return "6 - 7.50 pesos";
            case vagoneta:
                return "4.50 - 6 pesos";
            case trolebús:
                return "2 - 4 pesos";
            case rtp:
                return "2 - 5 pesos";
            case mexibús:
                return "7 pesos";
            case pumabús:
                return "0 pesos";
            case tren_suburbano:
                return "7.50 / 17.50 pesos";

            default:
                return "~ 5 - 7 pesos";
        }
    }
    
    
    public static float maxPricePerUnit(TransportType type){
        switch (type){
            case official:
                return 6.0f;
            case metro:
                return 5.0f;
            case metrobús:
                return 6.0f;
            case microbús:
                return 6.5f;
            case autobús:
                return 7.5f;
            case vagoneta:
                return 6.0f;
            case trolebús:
                return 4.0f;
            case rtp:
                return 5.0f;
            case mexibús:
                return 7.0f;
            case pumabús:
                return 0.0f;
            case tren_suburbano:
                return 17.5f;

            default:
                return 7.0f;
        }
    }
}
