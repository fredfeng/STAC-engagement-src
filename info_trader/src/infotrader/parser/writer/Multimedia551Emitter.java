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

class Multimedia551Emitter
extends AbstractEmitter<Collection<Multimedia>> {
    Multimedia551Emitter(InfoTraderWriter baseWriter, int startLevel, Collection<Multimedia> writeFrom) throws WriterCancelledException {
        super(baseWriter, startLevel, writeFrom);
    }

    @Override
    protected void emit() throws InfoTraderWriterException {
        if (this.writeFrom == null) {
            return;
        }
        for (Multimedia m : this.writeFrom) {
            this.emitTag(0, m.getXref(), "OBJE");
            if (m.getFileReferences() != null) {
                for (FileReference fr : m.getFileReferences()) {
                    this.emitTagWithRequiredValue(1, "FILE", fr.getReferenceToFile());
                    this.emitTagWithRequiredValue(2, "FORM", fr.getFormat());
                    this.emitTagIfValueNotNull(3, "TYPE", fr.getMediaType());
                    this.emitTagIfValueNotNull(2, "TITL", fr.getTitle());
                }
            }
            if (m.getUserReferences() != null) {
                for (UserReference u : m.getUserReferences()) {
                    this.emitTagWithRequiredValue(1, "REFN", u.getReferenceNum());
                    this.emitTagIfValueNotNull(2, "TYPE", u.getType());
                }
            }
            this.emitTagIfValueNotNull(1, "RIN", m.getRecIdNumber());
            new NotesEmitter(this.baseWriter, 1, m.getNotes()).emit();
            new ChangeDateEmitter(this.baseWriter, 1, m.getChangeDate()).emit();
            this.emitCustomTags(1, m.getCustomTags());
            if (m.getBlob() != null && !m.getBlob().isEmpty()) {
                throw new InfoTraderWriterVersionDataMismatchException("InfoTrader version is 5.5.1, but BLOB data on multimedia item " + m.getXref() + " was found.  This is only allowed in InfoTrader 5.5");
            }
            if (m.getContinuedObject() != null) {
                throw new InfoTraderWriterVersionDataMismatchException("InfoTrader version is 5.5.1, but BLOB continuation data on multimedia item " + m.getXref() + " was found.  This is only allowed in InfoTrader 5.5");
            }
            if (m.getEmbeddedMediaFormat() != null) {
                throw new InfoTraderWriterVersionDataMismatchException("InfoTrader version is 5.5.1, but format on multimedia item " + m.getXref() + " was found.  This is only allowed in InfoTrader 5.5");
            }
            if (m.getEmbeddedTitle() == null) continue;
            throw new InfoTraderWriterVersionDataMismatchException("InfoTrader version is 5.5.1, but title on multimedia item " + m.getXref() + " was found.  This is only allowed in InfoTrader 5.5");
        }
    }
}

