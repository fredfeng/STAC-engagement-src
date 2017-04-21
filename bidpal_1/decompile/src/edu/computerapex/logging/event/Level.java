/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.logging.event;

public enum Level {
    ERROR(40, "ERROR"),
    WARN(30, "WARN"),
    INFO(20, "INFO"),
    DEBUG(10, "DEBUG"),
    TRACE(0, "TRACE");
    
    private int levelInt;
    private String levelStr;

    private Level(int k, String s) {
        this.levelInt = k;
        this.levelStr = s;
    }

    public int toInt() {
        return this.levelInt;
    }

    public String toString() {
        return this.levelStr;
    }
}

