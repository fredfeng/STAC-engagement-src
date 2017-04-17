/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.validate;

import infotrader.parser.Options;
import infotrader.parser.model.AbstractCitation;
import infotrader.parser.model.ChangeDate;
import infotrader.parser.model.Note;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.model.UserReference;
import infotrader.parser.validate.AbstractValidator;
import infotrader.parser.validate.CitationValidator;
import infotrader.parser.validate.DuplicateEliminator;
import infotrader.parser.validate.InfoTraderValidator;
import java.util.List;

class NoteValidator
extends AbstractValidator {
    private final Note n;
    private final int i;

    public NoteValidator(InfoTraderValidator rootValidator, int i, Note n) {
        this.rootValidator = rootValidator;
        this.i = i;
        this.n = n;
    }

    @Override
    protected void validate() {
        if (Options.isCollectionInitializationEnabled() && this.n.getLines() == null) {
            if (this.rootValidator.isAutorepairEnabled()) {
                this.n.getLines(true).clear();
                this.addInfo("Lines of text collection on note was null - autorepaired");
            } else {
                this.addError("Lines of text collection on note is null", this.n);
                return;
            }
        }
        if (this.n.getXref() == null && (this.n.getLines() == null || this.n.getLines().isEmpty())) {
            this.addError("Note " + this.i + " without xref has no lines", this.n);
        }
        this.checkOptionalString(this.n.getRecIdNumber(), "automated record id", (Object)this.n);
        List<AbstractCitation> citations = this.n.getCitations();
        if (citations == null && Options.isCollectionInitializationEnabled()) {
            if (this.rootValidator.isAutorepairEnabled()) {
                this.n.getCitations(true).clear();
                this.addInfo("Source citations collection on note was null - autorepaired");
            } else {
                this.addError("Source citations collection on note is null", this.n);
            }
        } else {
            int dups;
            if (this.rootValidator.isAutorepairEnabled() && (dups = new DuplicateEliminator<AbstractCitation>(citations).process()) > 0) {
                this.rootValidator.addInfo("" + dups + " duplicate citations found and removed", this.n);
            }
            if (citations != null) {
                for (AbstractCitation c : citations) {
                    new CitationValidator(this.rootValidator, c).validate();
                }
            }
        }
        if (this.n.getUserReferences() == null && Options.isCollectionInitializationEnabled()) {
            if (this.rootValidator.isAutorepairEnabled()) {
                this.n.getUserReferences(true).clear();
                this.addInfo("User references collection on note was null - autorepaired");
            } else {
                this.addError("User references collection on note is null", this.n);
            }
        } else {
            this.checkUserReferences(this.n.getUserReferences(), this.n);
        }
        this.checkChangeDate(this.n.getChangeDate(), this.n);
    }
}

