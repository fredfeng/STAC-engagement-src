/*
 * Decompiled with CFR 0_121.
 */
package smartmail.logging.module;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class SecureTermMonitor {
    public static void doubleEntryError() {
        System.out.println("ERROR");
        try {
            String emessage = "ERROR: should not appear twice.";
            Files.write(Paths.get("logs/policyerrors.txt", new String[0]), emessage.getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        }
        catch (IOException emessage) {
            // empty catch block
        }
    }
}

