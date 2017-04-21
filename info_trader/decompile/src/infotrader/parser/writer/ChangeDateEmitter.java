/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.writer;

import infotrader.parser.exception.InfoTraderWriterException;
import infotrader.parser.exception.WriterCancelledException;
import infotrader.parser.model.ChangeDate;
import infotrader.parser.model.Note;
import infotrader.parser.model.StringTree;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.writer.AbstractEmitter;
import infotrader.parser.writer.InfoTraderWriter;
import infotrader.parser.writer.NotesEmitter;
import java.util.Collection;
import java.util.List;

class ChangeDateEmitter
extends AbstractEmitter<ChangeDate> {
    ChangeDateEmitter(InfoTraderWriter baseWriter, int startLevel, ChangeDate writeFrom) throws WriterCancelledException {
        super(baseWriter, startLevel, writeFrom);
    }

    @Override
    protected void emit() throws InfoTraderWriterException {
        if (this.writeFrom != null) {
            this.emitTag(this.startLevel, "CHAN");
            this.emitTagWithRequiredValue(this.startLevel + 1, "DATE", ((ChangeDate)this.writeFrom).getDate());
            this.emitTagIfValueNotNull(this.startLevel + 2, "TIME", ((ChangeDate)this.writeFrom).getTime());
            new NotesEmitter(this.baseWriter, this.startLevel + 1, ((ChangeDate)this.writeFrom).getNotes()).emit();
            this.emitCustomTags(this.startLevel + 1, ((ChangeDate)this.writeFrom).getCustomTags());
        }
    }
}

