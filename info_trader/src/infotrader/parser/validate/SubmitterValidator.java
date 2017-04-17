/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.validate;

import infotrader.parser.Options;
import infotrader.parser.model.Address;
import infotrader.parser.model.Note;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.model.Submitter;
import infotrader.parser.validate.AbstractValidator;
import infotrader.parser.validate.AddressValidator;
import infotrader.parser.validate.DuplicateEliminator;
import infotrader.parser.validate.InfoTraderValidator;
import infotrader.parser.validate.NotesValidator;
import java.util.List;

class SubmitterValidator
extends AbstractValidator {
    private final Submitter submitter;

    public SubmitterValidator(InfoTraderValidator rootValidator, Submitter submitter) {
        this.rootValidator = rootValidator;
        this.submitter = submitter;
    }

    @Override
    protected void validate() {
        if (this.submitter == null) {
            this.addError("Submitter being validated is null");
            return;
        }
        this.checkXref(this.submitter);
        this.checkRequiredString(this.submitter.getName(), "name", (Object)this.submitter);
        this.checkLanguagePreferences();
        this.checkOptionalString(this.submitter.getRecIdNumber(), "record id number", (Object)this.submitter);
        this.checkOptionalString(this.submitter.getRegFileNumber(), "submitter registered rfn", (Object)this.submitter);
        if (this.submitter.getAddress() != null) {
            new AddressValidator(this.rootValidator, this.submitter.getAddress()).validate();
        }
        new NotesValidator(this.rootValidator, this.submitter, this.submitter.getNotes()).validate();
    }

    private void checkLanguagePreferences() {
        int dups;
        List<StringWithCustomTags> languagePref = this.submitter.getLanguagePref();
        if (this.rootValidator.isAutorepairEnabled() && (dups = new DuplicateEliminator<StringWithCustomTags>(languagePref).process()) > 0) {
            this.rootValidator.addInfo("" + dups + " duplicate language preferences found and removed", this.submitter);
        }
        if (this.submitter.getLanguagePref(Options.isCollectionInitializationEnabled()) != null) {
            if (languagePref.size() > 3) {
                this.addError("Submitter exceeds limit on language preferences (3)", this.submitter);
            }
            for (StringWithCustomTags s : languagePref) {
                this.checkRequiredString(s, "language pref", (Object)this.submitter);
            }
        }
    }
}

