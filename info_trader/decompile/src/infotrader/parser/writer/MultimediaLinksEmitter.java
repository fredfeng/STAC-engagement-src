/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.writer;

import infotrader.parser.exception.InfoTraderWriterException;
import infotrader.parser.exception.InfoTraderWriterVersionDataMismatchException;
import infotrader.parser.exception.WriterCancelledException;
import infotrader.parser.model.FileReference;
import infotrader.parser.model.Multimedia;
import infotrader.parser.model.Note;
import infotrader.parser.model.StringTree;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.writer.AbstractEmitter;
import infotrader.parser.writer.InfoTraderWriter;
import infotrader.parser.writer.NotesEmitter;
import java.util.Collection;
import java.util.List;

class MultimediaLinksEmitter
extends AbstractEmitter<List<Multimedia>> {
    MultimediaLinksEmitter(InfoTraderWriter baseWriter, int startLevel, List<Multimedia> writeFrom) throws WriterCancelledException {
        super(baseWriter, startLevel, writeFrom);
    }

    @Override
    protected void emit() throws InfoTraderWriterException {
        if (this.writeFrom == null) {
            return;
        }
        for (Multimedia m : this.writeFrom) {
            if (m.getXref() == null) {
                if (this.g55()) {
                    this.emitTag(this.startLevel, "OBJE");
                    if (m.getFileReferences().size() > 1) {
                        throw new InfoTraderWriterVersionDataMismatchException("InfoTrader version is 5.5, but multimedia link references multiple files, which is only allowed in InfoTrader 5.5.1");
                    }
                    if (m.getFileReferences().size() == 1) {
                        FileReference fr = m.getFileReferences().get(0);
                        if (fr.getFormat() == null) {
                            this.emitTagWithRequiredValue(this.startLevel + 1, "FORM", m.getEmbeddedMediaFormat());
                        } else {
                            this.emitTagWithRequiredValue(this.startLevel + 1, "FORM", fr.getFormat());
                        }
                        this.emitTagIfValueNotNull(this.startLevel + 1, "TITL", m.getEmbeddedTitle());
                        this.emitTagWithRequiredValue(this.startLevel + 1, "FILE", fr.getReferenceToFile());
                    } else {
                        this.emitTagWithRequiredValue(this.startLevel + 1, "FORM", m.getEmbeddedMediaFormat());
                        this.emitTagIfValueNotNull(this.startLevel + 1, "TITL", m.getEmbeddedTitle());
                    }
                    new NotesEmitter(this.baseWriter, this.startLevel + 1, m.getNotes()).emit();
                } else {
                    for (FileReference fr : m.getFileReferences()) {
                        this.emitTagWithRequiredValue(this.startLevel + 1, "FILE", fr.getReferenceToFile());
                        this.emitTagIfValueNotNull(this.startLevel + 2, "FORM", fr.getFormat());
                        this.emitTagIfValueNotNull(this.startLevel + 3, "MEDI", fr.getMediaType());
                        this.emitTagIfValueNotNull(this.startLevel + 1, "TITL", fr.getTitle());
                    }
                    if (!m.getNotes().isEmpty()) {
                        throw new InfoTraderWriterVersionDataMismatchException("InfoTrader version is 5.5.1, but multimedia link has notes which are no longer allowed in 5.5");
                    }
                }
            } else {
                this.emitTagWithRequiredValue(this.startLevel, "OBJE", m.getXref());
            }
            this.emitCustomTags(this.startLevel + 1, m.getCustomTags());
        }
    }
}

