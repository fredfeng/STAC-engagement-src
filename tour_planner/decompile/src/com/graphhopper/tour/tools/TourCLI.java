/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.tour.tools;

import com.graphhopper.GraphHopper;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.tour.Matrix;
import com.graphhopper.tour.Places;
import com.graphhopper.tour.TourCalculator;
import com.graphhopper.tour.TourResponse;
import com.graphhopper.tour.tools.Command;
import com.graphhopper.tour.util.ProgressReporter;
import com.graphhopper.util.CmdArgs;
import com.graphhopper.util.shapes.GHPlace;
import com.graphhopper.util.shapes.GHPoint;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import javax.xml.stream.XMLStreamException;

public class TourCLI
extends Command {
    @Override
    protected void checkArgs() {
        if (this.ownArgs.size() < 2) {
            throw new IllegalArgumentException("At least two place names must be specified");
        }
    }

    @Override
    public void run() throws IOException, XMLStreamException {
        List<GHPlace> placesToVisit;
        List<GHPlace> places;
        this.cmdArgs = CmdArgs.readFromConfigAndMerge(this.cmdArgs, "config", "graphhopper.config");
        GraphHopper hopper = new GraphHopper().forServer().init(this.cmdArgs).setEncodingManager(new EncodingManager("car")).importOrLoad();
        Matrix<GHPlace> matrix = Matrix.load(this.cmdArgs);
        TourCalculator<GHPlace> tourCalculator = new TourCalculator<GHPlace>(matrix, hopper);
        TourResponse<GHPlace> rsp = tourCalculator.calcTour(placesToVisit = Places.selectByName(places = matrix.getPoints(), this.ownArgs), ProgressReporter.STDERR);
        if (rsp.hasErrors()) {
            for (Throwable ex : rsp.getErrors()) {
                System.err.println(ex);
            }
        } else {
            for (GHPlace p : rsp.getPoints()) {
                System.out.println(p);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new TourCLI().parseArgs(args).run();
    }
}

