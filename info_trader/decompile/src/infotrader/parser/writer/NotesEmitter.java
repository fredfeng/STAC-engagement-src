/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.writer;

import infotrader.parser.exception.InfoTraderWriterException;
import infotrader.parser.exception.WriterCancelledException;
import infotrader.parser.model.AbstractCitation;
import infotrader.parser.model.ChangeDate;
import infotrader.parser.model.Note;
import infotrader.parser.model.StringTree;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.model.UserReference;
import infotrader.parser.writer.AbstractEmitter;
import infotrader.parser.writer.ChangeDateEmitter;
import infotrader.parser.writer.InfoTraderWriter;
import infotrader.parser.writer.SourceCitationEmitter;
import java.util.Collection;
import java.util.List;

class NotesEmitter
extends AbstractEmitter<Collection<Note>> {
    NotesEmitter(InfoTraderWriter baseWriter, int startLevel, Collection<Note> collection) throws WriterCancelledException {
        super(baseWriter, startLevel, collection);
    }

    @Override
    protected void emit() throws InfoTraderWriterException {
        if (this.writeFrom != null) {
            for (Note n : this.writeFrom) {
                this.emitNote(this.startLevel, n);
                this.emitCustomTags(this.startLevel + 1, n.getCustomTags());
                if (!this.baseWriter.isCancelled()) continue;
                throw new WriterCancelledException("Construction and writing of InfoTrader cancelled");
            }
        }
    }

    private void emitNote(int level, Note note) throws InfoTraderWriterException {
        if (level > 0 && note.getXref() != null) {
            this.emitTagWithRequiredValue(level, "NOTE", note.getXref());
            return;
        }
        this.emitLinesOfText(level, note.getXref(), "NOTE", note.getLines());
        new SourceCitationEmitter(this.baseWriter, level + 1, note.getCitations()).emit();
        if (note.getUserReferences() != null) {
            for (UserReference u : note.getUserReferences()) {
                this.emitTagWithRequiredValue(level + 1, "REFN", u.getReferenceNum());
                this.emitTagIfValueNotNull(level + 2, "TYPE", u.getType());
            }
        }
        this.emitTagIfValueNotNull(level + 1, "RIN", note.getRecIdNumber());
        new ChangeDateEmitter(this.baseWriter, level + 1, note.getChangeDate()).emit();
        this.emitCustomTags(level + 1, note.getCustomTags());
    }
}

