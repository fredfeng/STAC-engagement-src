/*
 * Decompiled with CFR 0_121.
 */
package smartmail.messaging.controller.module;

import java.io.File;
import smartmail.messaging.controller.module.RunServer;

public class RunSmartMail {
    public static void main(String[] args) throws Exception {
        RunSmartMail.clean("./mail");
        RunSmartMail.clean("./logs");
        RunServer.main(args);
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

