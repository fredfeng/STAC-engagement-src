/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser;

public final class Options {
    private static boolean collectionInitializationEnabled = false;

    public static boolean isCollectionInitializationEnabled() {
        return collectionInitializationEnabled;
    }

    public static void resetToDefaults() {
        collectionInitializationEnabled = false;
    }

    public static void setCollectionInitializationEnabled(boolean collectionInitializationEnabled) {
        Options.collectionInitializationEnabled = collectionInitializationEnabled;
    }

    private Options() {
    }
}

