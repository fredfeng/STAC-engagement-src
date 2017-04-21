/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.writer;

import infotrader.parser.exception.InfoTraderWriterException;
import infotrader.parser.exception.WriterCancelledException;
import infotrader.parser.model.Address;
import infotrader.parser.model.ChangeDate;
import infotrader.parser.model.Multimedia;
import infotrader.parser.model.StringTree;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.model.Submitter;
import infotrader.parser.writer.AbstractEmitter;
import infotrader.parser.writer.AddressEmitter;
import infotrader.parser.writer.ChangeDateEmitter;
import infotrader.parser.writer.InfoTraderWriter;
import infotrader.parser.writer.MultimediaLinksEmitter;
import java.util.Collection;
import java.util.List;

class SubmittersEmitter
extends AbstractEmitter<Collection<Submitter>> {
    SubmittersEmitter(InfoTraderWriter baseWriter, int startLevel, Collection<Submitter> writeFrom) throws WriterCancelledException {
        super(baseWriter, startLevel, writeFrom);
    }

    @Override
    protected void emit() throws InfoTraderWriterException {
        if (this.writeFrom == null) {
            return;
        }
        for (Submitter s : this.writeFrom) {
            this.emitTag(0, s.getXref(), "SUBM");
            this.emitTagWithOptionalValueAndCustomSubtags(1, "NAME", s.getName());
            new AddressEmitter(this.baseWriter, 1, s.getAddress()).emit();
            new MultimediaLinksEmitter(this.baseWriter, 1, s.getMultimedia()).emit();
            if (s.getLanguagePref() != null) {
                for (StringWithCustomTags l : s.getLanguagePref()) {
                    this.emitTagWithRequiredValue(1, "LANG", l);
                }
            }
            this.emitStringsWithCustomTags(1, s.getPhoneNumbers(), "PHON");
            this.emitStringsWithCustomTags(1, s.getWwwUrls(), "WWW");
            this.emitStringsWithCustomTags(1, s.getFaxNumbers(), "FAX");
            this.emitStringsWithCustomTags(1, s.getEmails(), "EMAIL");
            this.emitTagIfValueNotNull(1, "RFN", s.getRegFileNumber());
            this.emitTagIfValueNotNull(1, "RIN", s.getRecIdNumber());
            new ChangeDateEmitter(this.baseWriter, 1, s.getChangeDate()).emit();
            this.emitCustomTags(1, s.getCustomTags());
        }
    }
}

