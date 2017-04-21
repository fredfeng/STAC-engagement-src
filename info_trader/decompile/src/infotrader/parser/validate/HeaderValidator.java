/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.validate;

import infotrader.parser.Options;
import infotrader.parser.io.encoding.Encoding;
import infotrader.parser.model.Address;
import infotrader.parser.model.CharacterSet;
import infotrader.parser.model.Corporation;
import infotrader.parser.model.Header;
import infotrader.parser.model.HeaderSourceData;
import infotrader.parser.model.InfoTrader;
import infotrader.parser.model.InfoTraderVersion;
import infotrader.parser.model.Note;
import infotrader.parser.model.SourceSystem;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.model.Submission;
import infotrader.parser.model.Submitter;
import infotrader.parser.model.SupportedVersion;
import infotrader.parser.validate.AbstractValidator;
import infotrader.parser.validate.AddressValidator;
import infotrader.parser.validate.InfoTraderValidator;
import infotrader.parser.validate.NotesValidator;
import infotrader.parser.validate.SubmitterValidator;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class HeaderValidator
extends AbstractValidator {
    private final Header header;

    public HeaderValidator(InfoTraderValidator gedcomValidator, Header header) {
        this.rootValidator = gedcomValidator;
        this.header = header;
    }

    @Override
    protected void validate() {
        this.checkCharacterSet();
        if (this.header.getCopyrightData() == null && Options.isCollectionInitializationEnabled()) {
            if (this.rootValidator.isAutorepairEnabled()) {
                this.header.getCopyrightData(true).clear();
                this.rootValidator.addInfo("Copyright data collection was null - repaired", this.header);
            } else {
                this.rootValidator.addError("Copyright data collection is null - must be at least an empty collection", this.header);
            }
        }
        this.checkCustomTags(this.header);
        this.checkOptionalString(this.header.getDate(), "date", (Object)this.header);
        this.checkOptionalString(this.header.getDestinationSystem(), "destination system", (Object)this.header);
        this.checkOptionalString(this.header.getFileName(), "filename", (Object)this.header);
        if (this.header.getInfoTraderVersion() == null) {
            if (this.rootValidator.isAutorepairEnabled()) {
                this.header.setInfoTraderVersion(new InfoTraderVersion());
                this.rootValidator.addInfo("InfoTrader version in header was null - repaired", this.header);
            } else {
                this.rootValidator.addError("InfoTrader version in header must be specified", this.header);
                return;
            }
        }
        if (this.header.getInfoTraderVersion().getVersionNumber() == null) {
            if (this.rootValidator.isAutorepairEnabled()) {
                this.header.getInfoTraderVersion().setVersionNumber(SupportedVersion.V5_5_1);
                this.rootValidator.addInfo("InfoTrader version number in header was null - repaired", this.header);
            } else {
                this.rootValidator.addError("InfoTrader version number in header must be specified", this.header);
                return;
            }
        }
        this.checkCustomTags(this.header.getInfoTraderVersion());
        this.checkOptionalString(this.header.getLanguage(), "language", (Object)this.header);
        new NotesValidator(this.rootValidator, this.header, this.header.getNotes()).validate();
        this.checkOptionalString(this.header.getPlaceHierarchy(), "place hierarchy", (Object)this.header);
        this.checkSourceSystem();
        if (this.header.getSubmitter() == null) {
            if (this.rootValidator.isAutorepairEnabled()) {
                if (this.rootValidator.gedcom.getSubmitters() == null || this.rootValidator.gedcom.getSubmitters().isEmpty()) {
                    this.rootValidator.addError("Submitter not specified in header, and autorepair could not find a submitter to select as default", this.header);
                } else {
                    this.header.setSubmitter(this.rootValidator.gedcom.getSubmitters().values().iterator().next());
                }
            } else {
                this.rootValidator.addError("Submitter not specified in header", this.header);
            }
            return;
        }
        new SubmitterValidator(this.rootValidator, this.header.getSubmitter()).validate();
        if (this.header.getSubmission() != null) {
            this.rootValidator.validateSubmission(this.header.getSubmission());
        }
        this.checkOptionalString(this.header.getTime(), "time", (Object)this.header);
    }

    private void checkCharacterSet() {
        if (this.header.getCharacterSet() == null) {
            if (this.rootValidator.isAutorepairEnabled()) {
                this.header.setCharacterSet(new CharacterSet());
                this.rootValidator.addInfo("Header did not have a character set defined - corrected.", this.header);
            } else {
                this.rootValidator.addError("Header has no character set defined", this.header);
                return;
            }
        }
        if (this.header.getCharacterSet().getCharacterSetName() == null) {
            if (this.rootValidator.isAutorepairEnabled()) {
                this.header.getCharacterSet().setCharacterSetName(new StringWithCustomTags("ANSEL"));
                this.rootValidator.addInfo("Character set name was not defined", this.header.getCharacterSet());
            } else {
                this.rootValidator.addError("Character set name was not defined", this.header.getCharacterSet());
                return;
            }
        }
        if (!Encoding.isValidCharacterSetName(this.header.getCharacterSet().getCharacterSetName().getValue())) {
            this.rootValidator.addError("Character set name is not one of the supported encodings (" + Encoding.getSupportedCharacterSetNames() + ")", this.header.getCharacterSet().getCharacterSetName());
        }
        this.checkOptionalString(this.header.getCharacterSet().getCharacterSetName(), "character set name", (Object)this.header.getCharacterSet());
        this.checkOptionalString(this.header.getCharacterSet().getVersionNum(), "character set version number", (Object)this.header.getCharacterSet());
        this.checkCustomTags(this.header.getCharacterSet());
    }

    private void checkSourceSystem() {
        SourceSystem ss = this.header.getSourceSystem();
        if (ss == null) {
            if (this.rootValidator.isAutorepairEnabled()) {
                ss = new SourceSystem();
                this.header.setSourceSystem(ss);
                this.rootValidator.addInfo("No source system specified in header - repaired", this.header);
            } else {
                this.rootValidator.addError("No source system specified in header", this.header);
                return;
            }
        }
        this.checkCustomTags(ss);
        if (ss.getCorporation() != null) {
            Corporation c = ss.getCorporation();
            this.checkCustomTags(c);
            if (c.getAddress() != null) {
                new AddressValidator(this.rootValidator, c.getAddress()).validate();
            }
            if (c.getBusinessName() == null || c.getBusinessName().trim().length() == 0) {
                if (this.rootValidator.isAutorepairEnabled()) {
                    c.setBusinessName("UNSPECIFIED");
                    this.rootValidator.addInfo("Corporation for source system exists but had no name - repaired", c);
                } else {
                    this.rootValidator.addError("Corporation for source system exists but has no name", c);
                }
            }
        }
        this.checkOptionalString(ss.getProductName(), "product name", (Object)ss);
        if (ss.getSourceData() != null) {
            HeaderSourceData sd = ss.getSourceData();
            if (sd.getName() == null || sd.getName().trim().length() == 0) {
                if (this.rootValidator.isAutorepairEnabled()) {
                    sd.setName("UNSPECIFIED");
                    this.rootValidator.addInfo("Source data was specified for source system, but name of source data was not specified - repaired", sd);
                } else {
                    this.rootValidator.addError("Source data is specified for source system, but name of source data is not specified", sd);
                }
            }
            this.checkOptionalString(sd.getCopyright(), "copyright", (Object)sd);
            this.checkOptionalString(sd.getPublishDate(), "publish date", (Object)sd);
            this.checkCustomTags(sd);
        }
        if (ss.getSystemId() == null) {
            if (this.rootValidator.isAutorepairEnabled()) {
                ss.setSystemId("UNSPECIFIED");
                this.rootValidator.addInfo("System ID was not specified in source system in header - repaired", ss);
            } else {
                this.rootValidator.addError("System ID must be specified in source system in header", ss);
            }
        }
        this.checkOptionalString(ss.getVersionNum(), "source system version number", (Object)ss);
    }
}

