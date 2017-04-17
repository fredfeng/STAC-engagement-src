/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.validate;

import infotrader.parser.Options;
import infotrader.parser.exception.InfoTraderValidationException;
import infotrader.parser.model.Note;
import infotrader.parser.validate.AbstractValidator;
import infotrader.parser.validate.DuplicateEliminator;
import infotrader.parser.validate.InfoTraderValidator;
import infotrader.parser.validate.NoteValidator;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class NotesValidator
extends AbstractValidator {
    private final List<Note> notes;
    private final Object parentObject;

    public NotesValidator(InfoTraderValidator rootValidator, Object parentObject, List<Note> notes) {
        this.rootValidator = rootValidator;
        this.parentObject = parentObject;
        this.notes = notes;
    }

    @Override
    protected void validate() {
        if (this.notes == null && Options.isCollectionInitializationEnabled()) {
            if (this.rootValidator.isAutorepairEnabled()) {
                try {
                    Field f = this.parentObject.getClass().getField("notes");
                    f.set(this.parentObject, new ArrayList(0));
                    this.addInfo("Notes collection on " + this.parentObject.getClass().getSimpleName() + " was null - autorepaired");
                }
                catch (SecurityException e) {
                    throw new InfoTraderValidationException("Could not autorepair null notes collection on " + this.parentObject.getClass().getSimpleName(), e);
                }
                catch (NoSuchFieldException e) {
                    throw new InfoTraderValidationException("Could not autorepair null notes collection on " + this.parentObject.getClass().getSimpleName(), e);
                }
                catch (IllegalArgumentException e) {
                    throw new InfoTraderValidationException("Could not autorepair null notes collection on " + this.parentObject.getClass().getSimpleName(), e);
                }
                catch (IllegalAccessException e) {
                    throw new InfoTraderValidationException("Could not autorepair null notes collection on " + this.parentObject.getClass().getSimpleName(), e);
                }
            } else {
                this.addError("Notes collection on " + this.parentObject.getClass().getSimpleName() + " is null");
            }
        } else {
            int i = 0;
            if (this.notes != null) {
                int dups;
                if (this.rootValidator.isAutorepairEnabled() && (dups = new DuplicateEliminator<Note>(this.notes).process()) > 0) {
                    this.rootValidator.addInfo("" + dups + " duplicate notes found and removed", this.notes);
                }
                for (Note n : this.notes) {
                    new NoteValidator(this.rootValidator, ++i, n).validate();
                }
            }
        }
    }
}

