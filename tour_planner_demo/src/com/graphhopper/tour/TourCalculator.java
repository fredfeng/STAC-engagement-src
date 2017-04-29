/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.tour;

import com.graphhopper.GraphHopper;
import com.graphhopper.routing.util.DefaultEdgeFilter;
import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.storage.index.LocationIndex;
import com.graphhopper.storage.index.QueryResult;
import com.graphhopper.tour.Matrix;
import com.graphhopper.tour.TourResponse;
import com.graphhopper.tour.util.Edge;
import com.graphhopper.tour.util.Graph;
import com.graphhopper.tour.util.ProgressReporter;
import com.graphhopper.util.DistanceCalc;
import com.graphhopper.util.DistanceCalcEarth;
import com.graphhopper.util.shapes.GHPlace;
import com.graphhopper.util.shapes.GHPoint;
import com.graphhopper.util.shapes.GHPoint3D;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TourCalculator<P extends GHPlace> {
    private final Logger logger;
    private final Matrix<P> matrix;
    private final GraphHopper graphHopper;
    private final List<Edge<P>> sortedEdges;
    private final Map<GHPoint, P> knownPoints;
    private final Map<GHPoint, QueryResult> queryResults;

    public TourCalculator(Matrix<P> matrix) {
        this(matrix, null);
    }

    public TourCalculator(Matrix<P> matrix, GraphHopper graphHopper) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.matrix = matrix;
        this.graphHopper = graphHopper;
        this.sortedEdges = matrix.symmetricEdges();
        Collections.sort(this.sortedEdges, new Edge.WeightComparator());
        LocationIndex locationIndex = graphHopper.getLocationIndex();
        EncodingManager encodingManager = graphHopper.getEncodingManager();
        FlagEncoder flagEncoder = encodingManager.fetchEdgeEncoders().get(0);
        DefaultEdgeFilter edgeFilter = new DefaultEdgeFilter(flagEncoder);
        this.knownPoints = new HashMap<GHPoint, P>();
        this.queryResults = new HashMap<GHPoint, QueryResult>();
        for (P p : matrix.getPoints()) {
            this.knownPoints.put(p, p);
            this.queryResults.put(p, locationIndex.findClosest(p.lat, p.lon, edgeFilter));
        }
    }

    public TourResponse<P> calcTour(List<? extends GHPoint> points) {
        return this.calcTour(points, null);
    }

    public TourResponse<P> calcTour(List<? extends GHPoint> points, ProgressReporter progressReporter) {
        TourResponse<P> rsp = new TourResponse<>();
        GHPoint p = points.get(0);
        this.knownPoints.put(p, (P)p);
        P root = this.knownPoints.get(p);
        HashSet<GHPoint> reqPoints = new HashSet<GHPoint>();
        reqPoints.addAll(points);
        Graph<P> minSpanningTree = this.calcMinSpanningTree(root, reqPoints, progressReporter);
        List<P> rspPoints = minSpanningTree.depthFirstWalk(root);
        rspPoints.add(root);
        rsp.setPoints(rspPoints);
        return rsp;
    }

    protected Graph<P> calcMinSpanningTree(P root, Set<GHPoint> reqPoints, ProgressReporter progressReporter) {
        Graph<P> result = new Graph<P>().add(root);
        while (result.size() < reqPoints.size()) {
            for (Edge<P> e : this.sortedEdges) {
                if (result.contains(e.to) && !result.contains(e.from)) {
                    e.reverse();
                }
                result.add(e);
            }
        }
        return result;
    }
}

