/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.writer;

import infotrader.parser.exception.InfoTraderWriterException;
import infotrader.parser.exception.WriterCancelledException;
import infotrader.parser.model.AbstractCitation;
import infotrader.parser.model.CitationData;
import infotrader.parser.model.CitationWithSource;
import infotrader.parser.model.CitationWithoutSource;
import infotrader.parser.model.Multimedia;
import infotrader.parser.model.Note;
import infotrader.parser.model.Source;
import infotrader.parser.model.StringTree;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.writer.AbstractEmitter;
import infotrader.parser.writer.InfoTraderWriter;
import infotrader.parser.writer.MultimediaLinksEmitter;
import infotrader.parser.writer.NotesEmitter;
import java.util.Collection;
import java.util.List;

class SourceCitationEmitter
extends AbstractEmitter<List<AbstractCitation>> {
    protected SourceCitationEmitter(InfoTraderWriter baseWriter, int startLevel, List<AbstractCitation> writeFrom) throws WriterCancelledException {
        super(baseWriter, startLevel, writeFrom);
    }

    @Override
    protected void emit() throws InfoTraderWriterException {
        if (this.writeFrom == null) {
            return;
        }
        for (AbstractCitation c : this.writeFrom) {
            if (c instanceof CitationWithoutSource) {
                this.emitCitationWithoutSource(this.startLevel, c);
                continue;
            }
            if (!(c instanceof CitationWithSource)) continue;
            this.emitCitationWithSource(this.startLevel, (CitationWithSource)c);
        }
    }

    private void emitCitationWithoutSource(int level, AbstractCitation c) throws InfoTraderWriterException {
        CitationWithoutSource cws = (CitationWithoutSource)c;
        this.emitLinesOfText(level, "SOUR", cws.getDescription());
        if (cws.getTextFromSource() != null) {
            for (List<String> linesOfText : cws.getTextFromSource()) {
                this.emitLinesOfText(level + 1, "TEXT", linesOfText);
            }
        }
        new NotesEmitter(this.baseWriter, level + 1, cws.getNotes()).emit();
        this.emitCustomTags(level + 1, cws.getCustomTags());
    }

    private void emitCitationWithSource(int level, CitationWithSource cws) throws InfoTraderWriterException {
        Source source = cws.getSource();
        if (source == null || source.getXref() == null || source.getXref().length() == 0) {
            throw new InfoTraderWriterException("Citation with source must have a source record with an xref/id");
        }
        this.emitTagWithRequiredValue(level, "SOUR", source.getXref());
        this.emitTagIfValueNotNull(level + 1, "PAGE", cws.getWhereInSource());
        this.emitTagIfValueNotNull(level + 1, "EVEN", cws.getEventCited());
        this.emitTagIfValueNotNull(level + 2, "ROLE", cws.getRoleInEvent());
        if (cws.getData() != null && !cws.getData().isEmpty()) {
            this.emitTag(level + 1, "DATA");
            for (CitationData cd : cws.getData()) {
                this.emitTagIfValueNotNull(level + 2, "DATE", cd.getEntryDate());
                for (List<String> linesOfText : cd.getSourceText()) {
                    this.emitLinesOfText(level + 2, "TEXT", linesOfText);
                }
            }
        }
        this.emitTagIfValueNotNull(level + 1, "QUAY", cws.getCertainty());
        new MultimediaLinksEmitter(this.baseWriter, level + 1, cws.getMultimedia()).emit();
        new NotesEmitter(this.baseWriter, level + 1, cws.getNotes()).emit();
        this.emitCustomTags(level + 1, cws.getCustomTags());
    }
}

