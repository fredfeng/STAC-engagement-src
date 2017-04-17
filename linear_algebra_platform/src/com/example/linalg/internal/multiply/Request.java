/*
 * Decompiled with CFR 0_121.
 */
package com.example.linalg.internal.multiply;

import com.example.linalg.util.Pair;
import java.util.List;

public class Request {
    public String call;
    public List<Pair<Integer, Integer>> updatePoints;
    public List<Double> updates;

    public Request(String call, List<Pair<Integer, Integer>> updatePoints, List<Double> updates) {
        this.call = call;
        this.updatePoints = updatePoints;
        this.updates = updates;
    }
}

