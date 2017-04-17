/*
 * Decompiled with CFR 0_121.
 */
package infotrader.messaging.controller.module;

import infotrader.userinteraction.SitemapServlet;
import java.io.File;
import java.io.PrintStream;

public class RunInfoTrader {
    public static void main(String[] args) throws Exception {
        File dirs = new File("./dirs");
        if (!dirs.exists()) {
            System.out.println("The dirs file does not exist. Did you not untar the dirs.tar file?");
            System.exit(-1);
        }
        RunInfoTrader.clean("./reports");
        SitemapServlet.mainx(args);
    }

    public static void clean(String dir) {
        String[] entries;
        File dirf = new File(dir);
        dirf.mkdir();
        for (String s : entries = dirf.list()) {
            File currentFile = new File(dirf.getPath(), s);
            currentFile.delete();
        }
    }
}

