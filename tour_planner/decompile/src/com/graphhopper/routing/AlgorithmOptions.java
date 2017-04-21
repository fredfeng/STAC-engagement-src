/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing;

import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.TraversalMode;
import com.graphhopper.routing.util.Weighting;
import com.graphhopper.util.PMap;

public class AlgorithmOptions {
    public static final String DIJKSTRA_BI = "dijkstrabi";
    public static final String DIJKSTRA = "dijkstra";
    public static final String DIJKSTRA_ONE_TO_MANY = "dijkstraOneToMany";
    public static final String ASTAR = "astar";
    public static final String ASTAR_BI = "astarbi";
    private String algorithm = "dijkstrabi";
    private Weighting weighting;
    private TraversalMode traversalMode = TraversalMode.NODE_BASED;
    private FlagEncoder flagEncoder;
    private final PMap hints = new PMap(5);

    private AlgorithmOptions() {
    }

    public AlgorithmOptions(String algorithm, FlagEncoder flagEncoder, Weighting weighting) {
        this.algorithm = algorithm;
        this.weighting = weighting;
        this.flagEncoder = flagEncoder;
    }

    public AlgorithmOptions(String algorithm, FlagEncoder flagEncoder, Weighting weighting, TraversalMode tMode) {
        this.algorithm = algorithm;
        this.weighting = weighting;
        this.flagEncoder = flagEncoder;
        this.traversalMode = tMode;
    }

    public TraversalMode getTraversalMode() {
        return this.traversalMode;
    }

    public Weighting getWeighting() {
        this.assertNotNull(this.weighting, "weighting");
        return this.weighting;
    }

    public String getAlgorithm() {
        this.assertNotNull(this.algorithm, "algorithm");
        return this.algorithm;
    }

    public FlagEncoder getFlagEncoder() {
        this.assertNotNull(this.flagEncoder, "flagEncoder");
        return this.flagEncoder;
    }

    public PMap getHints() {
        return this.hints;
    }

    private void assertNotNull(Object optionValue, String optionName) {
        if (optionValue == null) {
            throw new NullPointerException("Option '" + optionName + "' must NOT be null");
        }
    }

    public String toString() {
        return this.algorithm + ", " + this.weighting + ", " + this.flagEncoder + ", " + (Object)((Object)this.traversalMode);
    }

    public static Builder start() {
        return new Builder();
    }

    public static Builder start(AlgorithmOptions opts) {
        Builder b = new Builder();
        if (opts.algorithm != null) {
            b.algorithm(opts.getAlgorithm());
        }
        if (opts.flagEncoder != null) {
            b.flagEncoder(opts.getFlagEncoder());
        }
        if (opts.traversalMode != null) {
            b.traversalMode(opts.getTraversalMode());
        }
        if (opts.weighting != null) {
            b.weighting(opts.getWeighting());
        }
        return b;
    }

    public static class Builder {
        private final AlgorithmOptions opts = new AlgorithmOptions();

        public Builder traversalMode(TraversalMode traversalMode) {
            if (traversalMode == null) {
                throw new IllegalArgumentException("null as traversal mode is not allowed");
            }
            this.opts.traversalMode = traversalMode;
            return this;
        }

        public Builder weighting(Weighting weighting) {
            this.opts.weighting = weighting;
            return this;
        }

        public Builder algorithm(String algorithm) {
            this.opts.algorithm = algorithm;
            return this;
        }

        public Builder flagEncoder(FlagEncoder flagEncoder) {
            this.opts.flagEncoder = flagEncoder;
            return this;
        }

        public Builder hints(PMap hints) {
            this.opts.hints.put(hints);
            return this;
        }

        public AlgorithmOptions build() {
            return this.opts;
        }
    }

}

