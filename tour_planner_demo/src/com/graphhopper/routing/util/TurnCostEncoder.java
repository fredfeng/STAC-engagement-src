/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing.util;

public interface TurnCostEncoder {
    public boolean isTurnRestricted(long var1);

    public double getTurnCost(long var1);

    public long getTurnFlags(boolean var1, double var2);

    public static class NoTurnCostsEncoder
    implements TurnCostEncoder {
        @Override
        public boolean isTurnRestricted(long flags) {
            return false;
        }

        @Override
        public double getTurnCost(long flags) {
            return 0.0;
        }

        @Override
        public long getTurnFlags(boolean restriction, double costs) {
            return 0;
        }
    }

}

