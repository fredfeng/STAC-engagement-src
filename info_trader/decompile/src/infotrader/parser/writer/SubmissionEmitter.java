/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.writer;

import infotrader.parser.exception.InfoTraderWriterException;
import infotrader.parser.exception.WriterCancelledException;
import infotrader.parser.model.StringTree;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.model.Submission;
import infotrader.parser.model.Submitter;
import infotrader.parser.writer.AbstractEmitter;
import infotrader.parser.writer.InfoTraderWriter;
import java.util.List;

class SubmissionEmitter
extends AbstractEmitter<Submission> {
    SubmissionEmitter(InfoTraderWriter baseWriter, int startLevel, Submission writeFrom) throws WriterCancelledException {
        super(baseWriter, startLevel, writeFrom);
    }

    @Override
    protected void emit() throws InfoTraderWriterException {
        if (this.writeFrom == null) {
            return;
        }
        this.emitTag(0, ((Submission)this.writeFrom).getXref(), "SUBN");
        if (((Submission)this.writeFrom).getSubmitter() != null) {
            this.emitTagWithOptionalValue(1, "SUBM", ((Submission)this.writeFrom).getSubmitter().getXref());
        }
        this.emitTagIfValueNotNull(1, "FAMF", ((Submission)this.writeFrom).getNameOfFamilyFile());
        this.emitTagIfValueNotNull(1, "TEMP", ((Submission)this.writeFrom).getTempleCode());
        this.emitTagIfValueNotNull(1, "ANCE", ((Submission)this.writeFrom).getAncestorsCount());
        this.emitTagIfValueNotNull(1, "DESC", ((Submission)this.writeFrom).getDescendantsCount());
        this.emitTagIfValueNotNull(1, "ORDI", ((Submission)this.writeFrom).getOrdinanceProcessFlag());
        this.emitTagIfValueNotNull(1, "RIN", ((Submission)this.writeFrom).getRecIdNumber());
        this.emitCustomTags(1, ((Submission)this.writeFrom).getCustomTags());
    }
}

