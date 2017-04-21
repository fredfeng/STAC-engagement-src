/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.writer;

import infotrader.parser.exception.InfoTraderWriterException;
import infotrader.parser.exception.WriterCancelledException;
import infotrader.parser.model.Address;
import infotrader.parser.model.ChangeDate;
import infotrader.parser.model.Note;
import infotrader.parser.model.Repository;
import infotrader.parser.model.StringTree;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.model.UserReference;
import infotrader.parser.writer.AbstractEmitter;
import infotrader.parser.writer.AddressEmitter;
import infotrader.parser.writer.ChangeDateEmitter;
import infotrader.parser.writer.InfoTraderWriter;
import infotrader.parser.writer.NotesEmitter;
import java.util.Collection;
import java.util.List;

class RepositoryEmitter
extends AbstractEmitter<Collection<Repository>> {
    RepositoryEmitter(InfoTraderWriter baseWriter, int startLevel, Collection<Repository> writeFrom) throws WriterCancelledException {
        super(baseWriter, startLevel, writeFrom);
    }

    @Override
    protected void emit() throws InfoTraderWriterException {
        for (Repository r : this.writeFrom) {
            this.emitTag(0, r.getXref(), "REPO");
            this.emitTagIfValueNotNull(1, "NAME", r.getName());
            new AddressEmitter(this.baseWriter, 1, r.getAddress()).emit();
            new NotesEmitter(this.baseWriter, 1, r.getNotes()).emit();
            if (r.getUserReferences() != null) {
                for (UserReference u : r.getUserReferences()) {
                    this.emitTagWithRequiredValue(1, "REFN", u.getReferenceNum());
                    this.emitTagIfValueNotNull(2, "TYPE", u.getType());
                }
            }
            this.emitTagIfValueNotNull(1, "RIN", r.getRecIdNumber());
            this.emitStringsWithCustomTags(1, r.getPhoneNumbers(), "PHON");
            this.emitStringsWithCustomTags(1, r.getWwwUrls(), "WWW");
            this.emitStringsWithCustomTags(1, r.getFaxNumbers(), "FAX");
            this.emitStringsWithCustomTags(1, r.getEmails(), "EMAIL");
            new ChangeDateEmitter(this.baseWriter, 1, r.getChangeDate()).emit();
            this.emitCustomTags(1, r.getCustomTags());
        }
    }
}

