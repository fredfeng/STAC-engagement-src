/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.writer;

import infotrader.parser.exception.InfoTraderWriterException;
import infotrader.parser.exception.InfoTraderWriterVersionDataMismatchException;
import infotrader.parser.exception.WriterCancelledException;
import infotrader.parser.model.ChangeDate;
import infotrader.parser.model.FileReference;
import infotrader.parser.model.Multimedia;
import infotrader.parser.model.Note;
import infotrader.parser.model.StringTree;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.model.UserReference;
import infotrader.parser.writer.AbstractEmitter;
import infotrader.parser.writer.ChangeDateEmitter;
import infotrader.parser.writer.InfoTraderWriter;
import infotrader.parser.writer.NotesEmitter;
import java.util.Collection;
import java.util.List;

class Multimedia55Emitter
extends AbstractEmitter<Collection<Multimedia>> {
    Multimedia55Emitter(InfoTraderWriter baseWriter, int startLevel, Collection<Multimedia> writeFrom) throws WriterCancelledException {
        super(baseWriter, startLevel, writeFrom);
    }

    @Override
    protected void emit() throws InfoTraderWriterException {
        if (this.writeFrom == null) {
            return;
        }
        for (Multimedia m : this.writeFrom) {
            this.emitTag(0, m.getXref(), "OBJE");
            this.emitTagWithRequiredValue(1, "FORM", m.getEmbeddedMediaFormat());
            this.emitTagIfValueNotNull(1, "TITL", m.getEmbeddedTitle());
            new NotesEmitter(this.baseWriter, 1, m.getNotes()).emit();
            this.emitTag(1, "BLOB");
            for (String b : m.getBlob()) {
                this.emitTagWithRequiredValue(2, "CONT", b);
            }
            if (m.getContinuedObject() != null && m.getContinuedObject().getXref() != null) {
                this.emitTagWithRequiredValue(1, "OBJE", m.getContinuedObject().getXref());
            }
            if (m.getUserReferences() != null) {
                for (UserReference u : m.getUserReferences()) {
                    this.emitTagWithRequiredValue(1, "REFN", u.getReferenceNum());
                    this.emitTagIfValueNotNull(2, "TYPE", u.getType());
                }
            }
            this.emitTagIfValueNotNull(1, "RIN", m.getRecIdNumber());
            new ChangeDateEmitter(this.baseWriter, 1, m.getChangeDate()).emit();
            if (m.getFileReferences() != null && !m.getFileReferences().isEmpty()) {
                throw new InfoTraderWriterVersionDataMismatchException("InfoTrader version is 5.5, but found file references in multimedia object " + m.getXref() + " which are not allowed until InfoTrader 5.5.1");
            }
            this.emitCustomTags(1, m.getCustomTags());
        }
    }
}

