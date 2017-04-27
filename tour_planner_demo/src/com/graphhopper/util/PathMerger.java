/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

import com.graphhopper.GHResponse;
import com.graphhopper.routing.Path;
import com.graphhopper.util.DouglasPeucker;
import com.graphhopper.util.Instruction;
import com.graphhopper.util.InstructionList;
import com.graphhopper.util.PointList;
import com.graphhopper.util.Translation;
import com.graphhopper.util.ViaInstruction;
import java.util.List;

public class PathMerger {
    private boolean enableInstructions = true;
    private boolean simplifyResponse = true;
    private DouglasPeucker douglasPeucker;
    private boolean calcPoints = true;

    public void doWork(GHResponse rsp, List<Path> paths, Translation tr) {
        int origPoints = 0;
        long fullTimeInMillis = 0;
        double fullWeight = 0.0;
        double fullDistance = 0.0;
        boolean allFound = true;
        InstructionList fullInstructions = new InstructionList(tr);
        PointList fullPoints = PointList.EMPTY;
        for (int pathIndex = 0; pathIndex < paths.size(); ++pathIndex) {
            Path path = paths.get(pathIndex);
            fullTimeInMillis += path.getTime();
            fullDistance += path.getDistance();
            fullWeight += path.getWeight();
            if (this.enableInstructions) {
                InstructionList il = path.calcInstructions(tr);
                if (!il.isEmpty()) {
                    if (fullPoints.isEmpty()) {
                        PointList pl = il.get(0).getPoints();
                        fullPoints = new PointList(il.size() * Math.min(10, pl.size()), pl.is3D());
                    }
                    for (Instruction i : il) {
                        if (this.simplifyResponse) {
                            origPoints += i.getPoints().size();
                            this.douglasPeucker.simplify(i.getPoints());
                        }
                        fullInstructions.add(i);
                        fullPoints.add(i.getPoints());
                    }
                    if (pathIndex + 1 < paths.size()) {
                        ViaInstruction newInstr = new ViaInstruction(fullInstructions.get(fullInstructions.size() - 1));
                        newInstr.setViaCount(pathIndex + 1);
                        fullInstructions.replaceLast(newInstr);
                    }
                }
            } else if (this.calcPoints) {
                PointList tmpPoints = path.calcPoints();
                if (fullPoints.isEmpty()) {
                    fullPoints = new PointList(tmpPoints.size(), tmpPoints.is3D());
                }
                if (this.simplifyResponse) {
                    origPoints = tmpPoints.getSize();
                    this.douglasPeucker.simplify(tmpPoints);
                }
                fullPoints.add(tmpPoints);
            }
            allFound = allFound && path.isFound();
        }
        if (!fullPoints.isEmpty()) {
            String debug = rsp.getDebugInfo() + ", simplify (" + origPoints + "->" + fullPoints.getSize() + ")";
            rsp.setDebugInfo(debug);
        }
        if (this.enableInstructions) {
            rsp.setInstructions(fullInstructions);
        }
        if (!allFound) {
            rsp.addError(new RuntimeException("Connection between locations not found"));
        }
        rsp.setPoints(fullPoints).setRouteWeight(fullWeight).setDistance(fullDistance).setTime(fullTimeInMillis);
    }

    public PathMerger setCalcPoints(boolean calcPoints) {
        this.calcPoints = calcPoints;
        return this;
    }

    public PathMerger setDouglasPeucker(DouglasPeucker douglasPeucker) {
        this.douglasPeucker = douglasPeucker;
        return this;
    }

    public PathMerger setSimplifyResponse(boolean simplifyRes) {
        this.simplifyResponse = simplifyRes;
        return this;
    }

    public PathMerger setEnableInstructions(boolean enableInstructions) {
        this.enableInstructions = enableInstructions;
        return this;
    }
}

