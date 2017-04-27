/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing.util;

import com.graphhopper.coll.GHBitSetImpl;
import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.storage.GraphHopperStorage;
import com.graphhopper.util.EdgeExplorer;
import com.graphhopper.util.EdgeIterator;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.stack.array.TIntArrayStack;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TarjansStronglyConnectedComponentsAlgorithm {
    private final GraphHopperStorage graph;
    private final TIntArrayStack nodeStack;
    private final GHBitSetImpl onStack;
    private final int[] nodeIndex;
    private final int[] nodeLowLink;
    private final ArrayList<TIntArrayList> components = new ArrayList();
    private int index = 1;
    private final EdgeFilter edgeFilter;

    public TarjansStronglyConnectedComponentsAlgorithm(GraphHopperStorage graph, EdgeFilter edgeFilter) {
        this.graph = graph;
        this.nodeStack = new TIntArrayStack();
        this.onStack = new GHBitSetImpl(graph.getNodes());
        this.nodeIndex = new int[graph.getNodes()];
        this.nodeLowLink = new int[graph.getNodes()];
        this.edgeFilter = edgeFilter;
    }

    public List<TIntArrayList> findComponents() {
        int nodes = this.graph.getNodes();
        for (int start = 0; start < nodes; ++start) {
            if (this.nodeIndex[start] != 0 || this.graph.isNodeRemoved(start)) continue;
            this.strongConnect(start);
        }
        return this.components;
    }

    private void strongConnect(int firstNode) {
        Stack<TarjanState> stateStack = new Stack<TarjanState>();
        stateStack.push(TarjanState.startState(firstNode));
        block0 : while (!stateStack.empty()) {
            EdgeIterator iter;
            int node;
            TarjanState state = (TarjanState)stateStack.pop();
            int start = state.start;
            if (state.isStart()) {
                this.nodeIndex[start] = this.index;
                this.nodeLowLink[start] = this.index++;
                this.nodeStack.push(start);
                this.onStack.set(start);
                iter = this.graph.createEdgeExplorer(this.edgeFilter).setBaseNode(start);
            } else {
                iter = state.iter;
                int prevConnectedId = iter.getAdjNode();
                this.nodeLowLink[start] = Math.min(this.nodeLowLink[start], this.nodeLowLink[prevConnectedId]);
            }
            while (iter.next()) {
                int connectedId = iter.getAdjNode();
                if (this.nodeIndex[connectedId] == 0) {
                    stateStack.push(TarjanState.resumeState(start, iter));
                    stateStack.push(TarjanState.startState(connectedId));
                    continue block0;
                }
                if (!this.onStack.contains(connectedId)) continue;
                this.nodeLowLink[start] = Math.min(this.nodeLowLink[start], this.nodeIndex[connectedId]);
            }
            if (this.nodeIndex[start] != this.nodeLowLink[start]) continue;
            TIntArrayList component = new TIntArrayList();
            while ((node = this.nodeStack.pop()) != start) {
                component.add(node);
                this.onStack.clear(node);
            }
            component.add(start);
            component.trimToSize();
            this.onStack.clear(start);
            this.components.add(component);
        }
    }

    private static class TarjanState {
        final int start;
        final EdgeIterator iter;

        boolean isStart() {
            return this.iter == null;
        }

        private TarjanState(int start, EdgeIterator iter) {
            this.start = start;
            this.iter = iter;
        }

        public static TarjanState startState(int start) {
            return new TarjanState(start, null);
        }

        public static TarjanState resumeState(int start, EdgeIterator iter) {
            return new TarjanState(start, iter);
        }
    }

}

