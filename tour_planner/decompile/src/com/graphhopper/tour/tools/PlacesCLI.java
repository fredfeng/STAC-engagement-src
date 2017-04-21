/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.tour.tools;

import com.graphhopper.tour.Places;
import com.graphhopper.tour.tools.Command;
import com.graphhopper.util.CmdArgs;
import com.graphhopper.util.shapes.GHPlace;
import java.io.File;
import java.io.PrintStream;
import java.util.List;

public class PlacesCLI
extends Command {
    @Override
    public void run() throws Exception {
        List<GHPlace> places = Places.load(this.cmdArgs);
        if (this.ownArgs.size() == 1 && ((String)this.ownArgs.get(0)).endsWith(".txt")) {
            places = Places.selectByName(places, new File((String)this.ownArgs.get(0)));
        } else if (this.ownArgs.size() > 0) {
            places = Places.selectByName(places, this.ownArgs);
        }
        Places.writeCsv(places, System.out);
    }

    public static void main(String[] args) throws Exception {
        new PlacesCLI().parseArgs(args).run();
    }
}

