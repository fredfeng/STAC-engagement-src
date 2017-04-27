/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

import com.graphhopper.coll.GHBitSet;
import com.graphhopper.util.EdgeExplorer;
import com.graphhopper.util.EdgeIterator;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.SimpleIntDeque;
import com.graphhopper.util.XFirstSearch;

public class BreadthFirstSearch
extends XFirstSearch {
    @Override
    public void start(EdgeExplorer explorer, int startNode) {
        SimpleIntDeque fifo = new SimpleIntDeque();
        GHBitSet visited = this.createBitSet();
        visited.add(startNode);
        fifo.push(startNode);
        while (!fifo.isEmpty()) {
            int current = fifo.pop();
            if (!this.goFurther(current)) continue;
            EdgeIterator iter = explorer.setBaseNode(current);
            while (iter.next()) {
                int connectedId = iter.getAdjNode();
                if (!this.checkAdjacent(iter) || visited.contains(connectedId)) continue;
                visited.add(connectedId);
                fifo.push(connectedId);
            }
        }
    }
}

