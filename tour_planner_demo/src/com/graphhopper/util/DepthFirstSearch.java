/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

import com.graphhopper.coll.GHBitSet;
import com.graphhopper.util.EdgeExplorer;
import com.graphhopper.util.EdgeIterator;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.XFirstSearch;
import gnu.trove.stack.array.TIntArrayStack;

public class DepthFirstSearch
extends XFirstSearch {
    @Override
    public void start(EdgeExplorer explorer, int startNode) {
        TIntArrayStack stack = new TIntArrayStack();
        GHBitSet explored = this.createBitSet();
        stack.push(startNode);
        while (stack.size() > 0) {
            int current = stack.pop();
            if (explored.contains(current) || !this.goFurther(current)) continue;
            EdgeIterator iter = explorer.setBaseNode(current);
            while (iter.next()) {
                int connectedId = iter.getAdjNode();
                if (!this.checkAdjacent(iter)) continue;
                stack.push(connectedId);
            }
            explored.add(current);
        }
    }
}

