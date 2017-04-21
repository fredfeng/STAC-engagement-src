/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.io.event;

import java.util.EventObject;

public class FileProgressEvent
extends EventObject {
    private static final long serialVersionUID = 996714620347714206L;
    private final boolean complete;
    private final int linesProcessed;
    private final int bytesProcessed;

    public FileProgressEvent(Object source, int linesProcessed, int bytesProcessed, boolean complete) {
        super(source);
        this.linesProcessed = linesProcessed;
        this.bytesProcessed = bytesProcessed;
        this.complete = complete;
    }

    public int getBytesProcessed() {
        return this.bytesProcessed;
    }

    public int getLinesProcessed() {
        return this.linesProcessed;
    }

    public boolean isComplete() {
        return this.complete;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FileProgressEvent [complete=");
        builder.append(this.complete);
        builder.append(", linesProcessed=");
        builder.append(this.linesProcessed);
        builder.append(", bytesProcessed=");
        builder.append(this.bytesProcessed);
        builder.append("]");
        return builder.toString();
    }
}

