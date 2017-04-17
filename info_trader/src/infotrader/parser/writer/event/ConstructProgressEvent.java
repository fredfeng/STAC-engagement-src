/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.writer.event;

import java.util.EventObject;

public class ConstructProgressEvent
extends EventObject {
    private static final long serialVersionUID = -8214346212885031181L;
    private final boolean complete;
    private final int linesProcessed;

    public ConstructProgressEvent(Object source, int linesProcessed, boolean complete) {
        super(source);
        this.linesProcessed = linesProcessed;
        this.complete = complete;
    }

    public int getLinesProcessed() {
        return this.linesProcessed;
    }

    public boolean isComplete() {
        return this.complete;
    }

    @Override
    public String toString() {
        return "FileProgressEvent [complete=" + this.complete + ", linesProcessed=" + this.linesProcessed + "]";
    }
}

