package com.graphhopper.tools;

import com.graphhopper.GraphHopper;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.reader.osm.GraphHopperOSMConverter;
import com.graphhopper.util.CmdArgs;

public class ImportChilango {
	public static void main(String[] strs) throws Exception {
        CmdArgs args = CmdArgs.read(strs);
        GraphHopper hopper = new GraphHopperOSMConverter().init(args);
        hopper.importOrLoad();
        hopper.close();
    }
}
