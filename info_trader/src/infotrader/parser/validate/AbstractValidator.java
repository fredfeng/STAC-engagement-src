/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.validate;

import infotrader.parser.Options;
import infotrader.parser.exception.InfoTraderValidationException;
import infotrader.parser.model.ChangeDate;
import infotrader.parser.model.Note;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.model.UserReference;
import infotrader.parser.validate.DuplicateEliminator;
import infotrader.parser.validate.InfoTraderValidationFinding;
import infotrader.parser.validate.InfoTraderValidator;
import infotrader.parser.validate.NotesValidator;
import infotrader.parser.validate.Severity;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

abstract class AbstractValidator {
    protected InfoTraderValidator rootValidator;

    AbstractValidator() {
    }

    protected void addError(String description) {
        this.rootValidator.getFindings().add(new InfoTraderValidationFinding(description, Severity.ERROR, null));
    }

    protected void addError(String description, Object o) {
        this.rootValidator.getFindings().add(new InfoTraderValidationFinding(description, Severity.ERROR, o));
    }

    protected void addInfo(String description) {
        this.rootValidator.getFindings().add(new InfoTraderValidationFinding(description, Severity.INFO, null));
    }

    protected void addInfo(String description, Object o) {
        this.rootValidator.getFindings().add(new InfoTraderValidationFinding(description, Severity.INFO, o));
    }

    protected void addWarning(String description) {
        this.rootValidator.getFindings().add(new InfoTraderValidationFinding(description, Severity.WARNING, null));
    }

    protected void addWarning(String description, Object o) {
        this.rootValidator.getFindings().add(new InfoTraderValidationFinding(description, Severity.WARNING, o));
    }

    protected void checkChangeDate(ChangeDate changeDate, Object objectWithChangeDate) {
        if (changeDate == null) {
            return;
        }
        this.checkRequiredString(changeDate.getDate(), "change date", objectWithChangeDate);
        this.checkOptionalString(changeDate.getTime(), "change time", objectWithChangeDate);
        if (changeDate.getNotes() == null && Options.isCollectionInitializationEnabled()) {
            if (this.rootValidator.isAutorepairEnabled()) {
                changeDate.getNotes(true).clear();
                this.addInfo("Notes collection was null on " + changeDate.getClass().getSimpleName() + " - autorepaired");
            } else {
                this.addError("Notes collection is null on " + changeDate.getClass().getSimpleName());
            }
        } else {
            new NotesValidator(this.rootValidator, changeDate, changeDate.getNotes()).validate();
        }
    }

    protected void checkCustomTags(Object o) {
        Method customTagsGetter = null;
        try {
            customTagsGetter = o.getClass().getMethod("getCustomTags", new Class[0]);
        }
        catch (SecurityException unusedAndIgnored) {
            this.addError("Cannot access getter named 'getCustomTags' on object of type " + o.getClass().getSimpleName() + ".", o);
            return;
        }
        catch (NoSuchMethodException unusedAndIgnored) {
            this.addError("Cannot find getter named 'getCustomTags' on object of type " + o.getClass().getSimpleName() + ".", o);
            return;
        }
        Object fldVal = null;
        try {
            fldVal = customTagsGetter.invoke(o, new Object[0]);
        }
        catch (IllegalArgumentException e) {
            this.addError("Cannot get value of customTags attribute on object of type " + o.getClass().getSimpleName() + " - " + e.getMessage(), o);
            return;
        }
        catch (IllegalAccessException e) {
            this.addError("Cannot get value of customTags attribute on object of type " + o.getClass().getSimpleName() + " - " + e.getMessage(), o);
            return;
        }
        catch (InvocationTargetException e) {
            this.addError("Cannot get value of customTags attribute on object of type " + o.getClass().getSimpleName() + " - " + e.getMessage(), o);
            return;
        }
        if (fldVal == null && Options.isCollectionInitializationEnabled()) {
            if (this.rootValidator.isAutorepairEnabled()) {
                try {
                    customTagsGetter.invoke(o, true);
                }
                catch (IllegalArgumentException e) {
                    this.addError("Cannot autorepair value of customTags attribute on object of type " + o.getClass().getSimpleName() + " - " + e.getMessage(), o);
                    return;
                }
                catch (IllegalAccessException e) {
                    this.addError("Cannot autorepair value of customTags attribute on object of type " + o.getClass().getSimpleName() + " - " + e.getMessage(), o);
                    return;
                }
                catch (InvocationTargetException e) {
                    this.addError("Cannot autorepair value of customTags attribute on object of type " + o.getClass().getSimpleName() + " - " + e.getMessage(), o);
                    return;
                }
                this.rootValidator.addInfo("Custom tag collection was null - repaired", o);
            } else {
                this.rootValidator.addError("Custom tag collection is null - must be at least an empty collection", o);
            }
        } else if (fldVal != null && !(fldVal instanceof List)) {
            this.rootValidator.addError("Custom tag collection is not a List", o);
        }
    }

    protected void checkOptionalString(String optionalString, String fieldDescription, Object objectContainingField) {
        if (optionalString != null && !this.isSpecified(optionalString)) {
            this.addError(fieldDescription + " on " + objectContainingField.getClass().getSimpleName() + " is specified, but has a blank value", objectContainingField);
        }
    }

    protected void checkOptionalString(StringWithCustomTags optionalString, String fieldDescription, Object objectContainingField) {
        if (optionalString != null && optionalString.getValue() != null && !this.isSpecified(optionalString.getValue())) {
            this.addError(fieldDescription + " on " + objectContainingField.getClass().getSimpleName() + " is specified, but has a blank value", objectContainingField);
        }
        this.checkStringWithCustomTags(optionalString, fieldDescription);
    }

    protected void checkRequiredString(String requiredString, String fieldDescription, Object objectContainingField) {
        if (!this.isSpecified(requiredString)) {
            this.addError(fieldDescription + " on " + objectContainingField.getClass().getSimpleName() + " is required, but is either null or blank", objectContainingField);
        }
    }

    protected void checkRequiredString(StringWithCustomTags requiredString, String fieldDescription, Object objectContainingField) {
        if (requiredString == null || requiredString.getValue() == null || requiredString.getValue().trim().length() == 0) {
            this.addError(fieldDescription + " on " + objectContainingField.getClass().getSimpleName() + " is required, but is either null or blank", objectContainingField);
        }
        this.checkStringWithCustomTags(requiredString, fieldDescription);
    }

    protected void checkStringList(List<String> stringList, String description, boolean blanksAllowed) {
        int i = 0;
        if (stringList != null) {
            while (i < stringList.size()) {
                String a = stringList.get(i);
                if (a == null) {
                    if (this.rootValidator.isAutorepairEnabled()) {
                        this.addInfo("String list (" + description + ") contains null entry - removed", stringList);
                        stringList.remove(i);
                        continue;
                    }
                    this.addError("String list (" + description + ") contains null entry", stringList);
                } else if (!blanksAllowed && !this.isSpecified(a)) {
                    if (this.rootValidator.isAutorepairEnabled()) {
                        this.addInfo("String list (" + description + ") contains blank entry where none are allowed - removed", stringList);
                        stringList.remove(i);
                        continue;
                    }
                    this.addError("String list (" + description + ") contains blank entry where none are allowed", stringList);
                }
                ++i;
            }
        }
    }

    protected void checkStringTagList(List<StringWithCustomTags> stringList, String description, boolean blanksAllowed) {
        int dups;
        int i = 0;
        if (this.rootValidator.isAutorepairEnabled() && (dups = new DuplicateEliminator<StringWithCustomTags>(stringList).process()) > 0) {
            this.rootValidator.addInfo("" + dups + " duplicate tagged strings found and removed", stringList);
        }
        if (stringList != null) {
            while (i < stringList.size()) {
                StringWithCustomTags a = stringList.get(i);
                if (a == null || a.getValue() == null) {
                    if (this.rootValidator.isAutorepairEnabled()) {
                        this.addInfo("String list (" + description + ") contains null entry - removed", stringList);
                        stringList.remove(i);
                        continue;
                    }
                    this.addError("String list (" + description + ") contains null entry", stringList);
                } else if (!blanksAllowed && a.getValue().trim().length() == 0) {
                    if (this.rootValidator.isAutorepairEnabled()) {
                        this.addInfo("String list (" + description + ") contains blank entry where none are allowed - removed", stringList);
                        stringList.remove(i);
                        continue;
                    }
                    this.addError("String list (" + description + ") contains blank entry where none are allowed", stringList);
                }
                ++i;
            }
        }
    }

    protected void checkUserReferences(List<UserReference> userReferences, Object objectWithUserReferences) {
        if (userReferences != null) {
            for (UserReference userReference : userReferences) {
                if (userReference == null) {
                    this.addError("Null user reference in collection on " + objectWithUserReferences.getClass().getSimpleName(), objectWithUserReferences);
                    continue;
                }
                this.checkRequiredString(userReference.getReferenceNum(), "reference number", (Object)userReference);
                this.checkOptionalString(userReference.getType(), "reference type", (Object)userReference);
            }
        }
    }

    protected void checkXref(Object objectContainingXref) {
        this.checkXref(objectContainingXref, "xref");
    }

    protected void checkXref(Object objectContainingXref, String xrefFieldName) {
        String getterName = "get" + xrefFieldName.substring(0, 1).toUpperCase() + xrefFieldName.substring(1);
        try {
            Method xrefGetter = objectContainingXref.getClass().getMethod(getterName, new Class[0]);
            String xref = (String)xrefGetter.invoke(objectContainingXref, new Object[0]);
            this.checkRequiredString(xref, xrefFieldName, objectContainingXref);
            if (xref != null) {
                if (xref.length() < 3) {
                    this.addError("xref on " + objectContainingXref.getClass().getSimpleName() + " is too short to be a valid xref", objectContainingXref);
                } else if (xref.charAt(0) != '@') {
                    this.addError("xref on " + objectContainingXref.getClass().getSimpleName() + " is doesn't start with an at-sign (@)", objectContainingXref);
                }
                if (!xref.endsWith("@")) {
                    this.addError("xref on " + objectContainingXref.getClass().getSimpleName() + " is doesn't end with an at-sign (@)", objectContainingXref);
                }
            }
        }
        catch (SecurityException e) {
            throw new InfoTraderValidationException(objectContainingXref.getClass().getSimpleName() + " doesn't have an xref getter named " + getterName + " that can be accessed to validate", e);
        }
        catch (ClassCastException e) {
            throw new InfoTraderValidationException(objectContainingXref.getClass().getSimpleName() + " doesn't have an xref getter of the right type named " + getterName + " to validate", e);
        }
        catch (IllegalArgumentException e) {
            throw new InfoTraderValidationException(objectContainingXref.getClass().getSimpleName() + " doesn't have an xref getter named " + getterName + " to validate", e);
        }
        catch (IllegalAccessException e) {
            throw new InfoTraderValidationException(objectContainingXref.getClass().getSimpleName() + " doesn't have an xref getter named " + getterName + " that can be accessed to validate", e);
        }
        catch (InvocationTargetException e) {
            throw new InfoTraderValidationException(objectContainingXref.getClass().getSimpleName() + " doesn't have an xref getter named " + getterName + " to validate", e);
        }
        catch (NoSuchMethodException e) {
            throw new InfoTraderValidationException(objectContainingXref.getClass().getSimpleName() + " doesn't have an xref getter named " + getterName + " to validate", e);
        }
    }

    protected abstract void validate();

    private void checkStringWithCustomTags(StringWithCustomTags swct, String fieldDescription) {
        if (swct == null) {
            return;
        }
        if (swct.getValue() == null || !this.isSpecified(swct.getValue())) {
            this.addError("A string with custom tags object (" + fieldDescription + ") was defined with no value", swct);
        }
        this.checkCustomTags(swct);
    }

    private boolean isSpecified(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        for (int i = 0; i < s.length(); ++i) {
            if (Character.isWhitespace(s.charAt(i))) continue;
            return true;
        }
        return false;
    }
}

