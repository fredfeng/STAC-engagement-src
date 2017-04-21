/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.validate;

import infotrader.parser.model.Address;
import infotrader.parser.model.ChangeDate;
import infotrader.parser.model.Note;
import infotrader.parser.model.Repository;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.model.UserReference;
import infotrader.parser.validate.AbstractValidator;
import infotrader.parser.validate.AddressValidator;
import infotrader.parser.validate.InfoTraderValidator;
import infotrader.parser.validate.NotesValidator;
import java.util.List;

class RepositoryValidator
extends AbstractValidator {
    private final Repository repository;

    public RepositoryValidator(InfoTraderValidator gedcomValidator, Repository repository) {
        this.rootValidator = gedcomValidator;
        this.repository = repository;
    }

    @Override
    protected void validate() {
        if (this.repository == null) {
            this.addError("Repository being validated is null");
            return;
        }
        this.checkXref(this.repository);
        this.checkOptionalString(this.repository.getName(), "name", (Object)this.repository);
        this.checkChangeDate(this.repository.getChangeDate(), this.repository);
        this.checkStringTagList(this.repository.getEmails(), "email list", false);
        this.checkUserReferences(this.repository.getUserReferences(), this.repository);
        this.checkOptionalString(this.repository.getRecIdNumber(), "automated record id", (Object)this.repository);
        this.checkStringTagList(this.repository.getPhoneNumbers(), "phone numbers", false);
        new NotesValidator(this.rootValidator, this.repository, this.repository.getNotes()).validate();
        Address a = this.repository.getAddress();
        if (a != null) {
            new AddressValidator(this.rootValidator, a).validate();
        }
    }
}

