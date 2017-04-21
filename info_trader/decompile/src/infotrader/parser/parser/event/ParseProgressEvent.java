/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.parser.event;

import infotrader.parser.model.InfoTrader;
import infotrader.parser.model.Multimedia;
import infotrader.parser.model.Note;
import infotrader.parser.model.Repository;
import infotrader.parser.model.Source;
import infotrader.parser.model.Submitter;
import java.util.EventObject;
import java.util.Map;

public class ParseProgressEvent
extends EventObject {
    private static final long serialVersionUID = 996714620347714206L;
    private final boolean complete;
    private final int linesParsed;
    private final int multimediaProcessed;
    private final int notesProcessed;
    private final int repositoriesProcessed;
    private final int sourcesProcessed;
    private final int submittersProcessed;

    public ParseProgressEvent(Object source, InfoTrader g, boolean complete, int linesParsed) {
        super(source);
        this.multimediaProcessed = g.getMultimedia().size();
        this.notesProcessed = g.getNotes().size();
        this.repositoriesProcessed = g.getRepositories().size();
        this.sourcesProcessed = g.getSources().size();
        this.submittersProcessed = g.getSubmitters().size();
        this.complete = complete;
        this.linesParsed = linesParsed;
    }

    public int getLinesParsed() {
        return this.linesParsed;
    }

    public int getMultimediaProcessed() {
        return this.multimediaProcessed;
    }

    public int getNotesProcessed() {
        return this.notesProcessed;
    }

    public int getRepositoriesProcessed() {
        return this.repositoriesProcessed;
    }

    public int getSourcesProcessed() {
        return this.sourcesProcessed;
    }

    public int getSubmittersProcessed() {
        return this.submittersProcessed;
    }

    public boolean isComplete() {
        return this.complete;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ParseProgressEvent [complete=");
        builder.append(this.complete);
        builder.append(", linesParsed=");
        builder.append(this.linesParsed);
        builder.append(", multimediaProcessed=");
        builder.append(this.multimediaProcessed);
        builder.append(", notesProcessed=");
        builder.append(this.notesProcessed);
        builder.append(", repositoriesProcessed=");
        builder.append(this.repositoriesProcessed);
        builder.append(", sourcesProcessed=");
        builder.append(this.sourcesProcessed);
        builder.append(", submittersProcessed=");
        builder.append(this.submittersProcessed);
        builder.append("]");
        return builder.toString();
    }
}

