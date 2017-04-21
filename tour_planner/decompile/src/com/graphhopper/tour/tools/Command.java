/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.tour.tools;

import com.graphhopper.util.CmdArgs;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Command {
    protected final Logger logger;
    protected CmdArgs cmdArgs;
    protected List<String> ownArgs;

    public Command() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    public Command parseArgs(String[] args) {
        ArrayList<String> ghArgs = new ArrayList<String>();
        this.ownArgs = new ArrayList<String>();
        for (String arg : args) {
            if (arg.contains("=")) {
                ghArgs.add(arg);
                continue;
            }
            this.ownArgs.add(arg);
        }
        this.cmdArgs = CmdArgs.read(ghArgs.toArray(new String[ghArgs.size()]));
        this.checkArgs();
        return this;
    }

    public abstract void run() throws Exception;

    protected void checkArgs() {
    }
}

