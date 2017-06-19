package com.graphhopper.routing;

import com.graphhopper.ChilangoPath;
import com.graphhopper.chilango.ChilangoWeighting;
import com.graphhopper.routing.util.TraversalMode;
import com.graphhopper.routing.weighting.Weighting;
import com.graphhopper.storage.Graph;

public class ChilangoDijkstra extends Dijkstra {

	public ChilangoDijkstra(Graph graph, Weighting weighting, TraversalMode tMode) {
		super(graph, weighting, tMode);

		if (weighting instanceof ChilangoWeighting) {
			ChilangoWeighting chilangoWeighting = (ChilangoWeighting) weighting;
			chilangoWeighting.setGraph(graph);
		}

	}
	
    @Override
    protected Path extractPath() {
        if (currEdge == null || !finished())
            return createEmptyPath();

        return new ChilangoPath(graph, weighting).
                setWeight(currEdge.weight).setSPTEntry(currEdge).extract();
    }

}
