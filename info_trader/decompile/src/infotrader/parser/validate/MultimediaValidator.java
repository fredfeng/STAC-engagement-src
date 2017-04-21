/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.validate;

import infotrader.parser.Options;
import infotrader.parser.exception.InfoTraderValidationException;
import infotrader.parser.model.AbstractCitation;
import infotrader.parser.model.ChangeDate;
import infotrader.parser.model.FileReference;
import infotrader.parser.model.Header;
import infotrader.parser.model.InfoTrader;
import infotrader.parser.model.InfoTraderVersion;
import infotrader.parser.model.Multimedia;
import infotrader.parser.model.Note;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.model.SupportedVersion;
import infotrader.parser.model.UserReference;
import infotrader.parser.validate.AbstractValidator;
import infotrader.parser.validate.CitationValidator;
import infotrader.parser.validate.DuplicateEliminator;
import infotrader.parser.validate.InfoTraderValidator;
import infotrader.parser.validate.NotesValidator;
import java.util.List;
import java.util.Map;

class MultimediaValidator
extends AbstractValidator {
    private final Multimedia mm;
    private SupportedVersion gedcomVersion;

    public MultimediaValidator(InfoTraderValidator rootValidator, Multimedia multimedia) {
        this.rootValidator = rootValidator;
        if (rootValidator == null) {
            throw new InfoTraderValidationException("Root validator passed in to MultimediaValidator constructor was null");
        }
        this.mm = multimedia;
        if (rootValidator.gedcom == null || rootValidator.gedcom.getHeader() == null || rootValidator.gedcom.getHeader().getInfoTraderVersion() == null || rootValidator.gedcom.getHeader().getInfoTraderVersion().getVersionNumber() == null) {
            if (rootValidator.isAutorepairEnabled()) {
                this.gedcomVersion = SupportedVersion.V5_5_1;
                rootValidator.addInfo("Was not able to determine InfoTrader version - assuming 5.5.1", rootValidator.gedcom);
            } else {
                rootValidator.addError("Was not able to determine InfoTrader version - cannot validate multimedia objects", rootValidator.gedcom);
            }
        } else {
            this.gedcomVersion = rootValidator.gedcom.getHeader().getInfoTraderVersion().getVersionNumber();
        }
    }

    @Override
    protected void validate() {
        this.validateCommon();
        if (this.v551()) {
            this.validate551();
        } else {
            this.validate55();
        }
    }

    private void checkFileReference(FileReference fr) {
        if (fr == null) {
            if (this.rootValidator.isAutorepairEnabled()) {
                this.addError("Null file reference in list of file references in multimedia object - cannot repair", this.mm);
            } else {
                this.addError("Null file reference in list of file references in multimedia object", this.mm);
            }
            return;
        }
        this.checkRequiredString(fr.getFormat(), "format", (Object)fr);
        this.checkOptionalString(fr.getMediaType(), "media type", (Object)fr);
        this.checkOptionalString(fr.getTitle(), "title", (Object)fr);
        this.checkRequiredString(fr.getReferenceToFile(), "reference to file", (Object)fr);
    }

    private void checkUserReferences() {
        int dups;
        List<UserReference> userReferences = this.mm.getUserReferences();
        if (Options.isCollectionInitializationEnabled() && userReferences == null) {
            if (this.rootValidator.isAutorepairEnabled()) {
                this.mm.getUserReferences(true).clear();
                this.rootValidator.addInfo("List of user references on multimedia object was null - repaired", this.mm);
            } else {
                this.rootValidator.addError("List of user references on multimedia object is null", this.mm);
                return;
            }
        }
        if (this.rootValidator.isAutorepairEnabled() && (dups = new DuplicateEliminator<UserReference>(userReferences).process()) > 0) {
            this.rootValidator.addInfo("" + dups + " duplicate user references found and removed", this.mm);
        }
        if (userReferences != null) {
            for (UserReference u : userReferences) {
                this.checkCustomTags(u);
                if (u.getReferenceNum() != null) continue;
                if (this.rootValidator.isAutorepairEnabled()) {
                    this.addError("User reference is has a null or blank reference number - cannot repair", u);
                    continue;
                }
                this.addError("User reference is has a null or blank reference number", u);
            }
        }
    }

    private void checkXref() {
        if (this.mm.getXref() == null || this.mm.getXref().trim().length() == 0) {
            if (this.rootValidator.isAutorepairEnabled()) {
                this.addError("Multimedia object must have xref - cannot autorepair", this.mm);
            } else {
                this.addError("Multimedia object must have xref", this.mm);
            }
            return;
        }
        if (this.rootValidator.gedcom.getMultimedia().get(this.mm.getXref()) != this.mm) {
            if (this.rootValidator.isAutorepairEnabled()) {
                this.rootValidator.gedcom.getMultimedia().put(this.mm.getXref(), this.mm);
                this.rootValidator.addInfo("Multimedia object not keyed by xref in map - repaired", this.mm);
            } else {
                this.rootValidator.addError("Multimedia object not keyed by xref in map", this.mm);
            }
            return;
        }
    }

    private boolean v551() {
        return SupportedVersion.V5_5_1.equals((Object)this.gedcomVersion);
    }

    private void validate55() {
        if (this.mm.getBlob() == null || this.mm.getBlob().isEmpty()) {
            if (this.rootValidator.isAutorepairEnabled()) {
                this.addError("Embedded media object has an empty blob object - cannot repair", this.mm);
            } else {
                this.addError("Embedded media object has an empty blob object", this.mm);
            }
        }
        this.checkRequiredString(this.mm.getEmbeddedMediaFormat(), "embedded media format", (Object)this.mm);
        if (this.mm.getCitations() != null && !this.mm.getCitations().isEmpty()) {
            if (this.rootValidator.isAutorepairEnabled()) {
                this.mm.getCitations(true).clear();
                this.rootValidator.addInfo("Citations collection was populated, but not allowed in v5.5 of gedcom - repaired (cleared)", this.mm);
            } else {
                this.rootValidator.addError("Citations collection is populated, but not allowed in v5.5 of gedcom", this.mm);
            }
        }
    }

    private void validate551() {
        if (Options.isCollectionInitializationEnabled() && this.mm.getFileReferences() == null) {
            if (this.rootValidator.isAutorepairEnabled()) {
                this.mm.getFileReferences(true).clear();
                this.rootValidator.addInfo("Multimedia object did not have list of file references - repaired", this.mm);
            } else {
                this.rootValidator.addError("Multimedia object does not have list of file references", this.mm);
                return;
            }
        }
        if (this.mm.getFileReferences() != null) {
            for (FileReference fr : this.mm.getFileReferences()) {
                this.checkFileReference(fr);
            }
        }
        if (this.mm.getBlob() != null && !this.mm.getBlob().isEmpty()) {
            if (this.rootValidator.isAutorepairEnabled()) {
                this.mm.getBlob().clear();
                this.addInfo("Embedded media object had a populated blob object, which is not allowed in InfoTrader 5.5.1 - repaired (cleared)", this.mm);
            } else {
                this.addError("Embedded media object has a populated blob object, which is not allowed in InfoTrader 5.5.1", this.mm);
            }
        }
        if (this.mm.getEmbeddedMediaFormat() != null) {
            if (this.rootValidator.isAutorepairEnabled()) {
                this.mm.setEmbeddedMediaFormat(null);
                this.rootValidator.addInfo("Multimedia object had a format for embedded media, which is not allowed in InfoTrader 5.5.1 - repaired (cleared)", this.mm);
            } else {
                this.rootValidator.addError("Multimedia object has a format for embedded media, which is not allowed in InfoTrader 5.5.1", this.mm);
            }
        }
        if (this.mm.getCitations() != null) {
            for (AbstractCitation c : this.mm.getCitations()) {
                new CitationValidator(this.rootValidator, c).validate();
            }
        }
    }

    private void validateCommon() {
        this.checkXref();
        this.checkOptionalString(this.mm.getRecIdNumber(), "record id number", (Object)this.mm);
        this.checkChangeDate(this.mm.getChangeDate(), this.mm);
        this.checkUserReferences();
        if (Options.isCollectionInitializationEnabled() && this.mm.getCitations() == null) {
            if (this.rootValidator.isAutorepairEnabled()) {
                this.mm.getCitations(true).clear();
                this.addInfo("citations collection for multimedia object was null - rootValidator.autorepaired", this.mm);
            } else {
                this.addError("citations collection for multimedia object is null", this.mm);
            }
        }
        if (this.mm.getContinuedObject() != null) {
            new MultimediaValidator(this.rootValidator, this.mm.getContinuedObject()).validate();
        }
        if (Options.isCollectionInitializationEnabled() && this.mm.getBlob() == null) {
            if (this.rootValidator.isAutorepairEnabled()) {
                this.mm.getBlob(true).clear();
                this.rootValidator.addInfo("Embedded blob was null - repaired", this.mm);
            } else {
                this.rootValidator.addError("Embedded blob is null", this.mm);
            }
        }
        new NotesValidator(this.rootValidator, this.mm, this.mm.getNotes()).validate();
    }
}

