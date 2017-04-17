/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.validate;

import infotrader.parser.model.Header;
import infotrader.parser.model.InfoTrader;
import infotrader.parser.model.Multimedia;
import infotrader.parser.model.Note;
import infotrader.parser.model.Repository;
import infotrader.parser.model.Source;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.model.Submission;
import infotrader.parser.model.Submitter;
import infotrader.parser.model.Trailer;
import infotrader.parser.validate.AbstractValidator;
import infotrader.parser.validate.HeaderValidator;
import infotrader.parser.validate.InfoTraderValidationFinding;
import infotrader.parser.validate.MultimediaValidator;
import infotrader.parser.validate.NoteValidator;
import infotrader.parser.validate.NotesValidator;
import infotrader.parser.validate.RepositoryValidator;
import infotrader.parser.validate.Severity;
import infotrader.parser.validate.SourceValidator;
import infotrader.parser.validate.SubmitterValidator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InfoTraderValidator
extends AbstractValidator {
    private boolean autorepairEnabled = true;
    private final List<InfoTraderValidationFinding> findings = new ArrayList<InfoTraderValidationFinding>();
    protected InfoTrader gedcom = null;

    public InfoTraderValidator(InfoTrader gedcom) {
        this.gedcom = gedcom;
        this.rootValidator = this;
    }

    public List<InfoTraderValidationFinding> getFindings() {
        return this.findings;
    }

    public boolean hasErrors() {
        for (InfoTraderValidationFinding finding : this.rootValidator.findings) {
            if (finding.getSeverity() != Severity.ERROR) continue;
            return true;
        }
        return false;
    }

    public boolean hasInfo() {
        for (InfoTraderValidationFinding finding : this.rootValidator.findings) {
            if (finding.getSeverity() != Severity.INFO) continue;
            return true;
        }
        return false;
    }

    public boolean hasWarnings() {
        for (InfoTraderValidationFinding finding : this.rootValidator.findings) {
            if (finding.getSeverity() != Severity.WARNING) continue;
            return true;
        }
        return false;
    }

    public boolean isAutorepairEnabled() {
        return this.autorepairEnabled;
    }

    public void setAutorepairEnabled(boolean autorepair) {
        this.autorepairEnabled = autorepair;
    }

    @Override
    public void validate() {
        this.findings.clear();
        if (this.gedcom == null) {
            this.addError("InfoTrader structure is null");
            return;
        }
        this.validateSubmitters();
        this.validateHeader();
        this.validateRepositories();
        this.validateMultimedia();
        this.validateNotes();
        this.validateSources();
        this.validateSubmission(this.gedcom.getSubmission());
        this.validateTrailer();
        new NotesValidator(this.rootValidator, this.gedcom, new ArrayList<Note>(this.gedcom.getNotes().values())).validate();
    }

    void validateSubmission(Submission s) {
        if (s == null) {
            this.addError("Submission record on root InfoTrader is null", this.gedcom);
            return;
        }
        this.checkXref(s);
        this.checkOptionalString(s.getAncestorsCount(), "Ancestor count", (Object)s);
        this.checkOptionalString(s.getDescendantsCount(), "Descendant count", (Object)s);
        this.checkOptionalString(s.getNameOfFamilyFile(), "Name of family file", (Object)s);
        this.checkOptionalString(s.getOrdinanceProcessFlag(), "Ordinance process flag", (Object)s);
        this.checkOptionalString(s.getRecIdNumber(), "Automated record id", (Object)s);
        this.checkOptionalString(s.getTempleCode(), "Temple code", (Object)s);
    }

    private void validateHeader() {
        if (this.gedcom.getHeader() == null) {
            if (this.autorepairEnabled) {
                this.gedcom.setHeader(new Header());
                this.addInfo("Header was null - autorepaired");
            } else {
                this.addError("InfoTrader Header is null");
                return;
            }
        }
        new HeaderValidator(this.rootValidator, this.gedcom.getHeader()).validate();
    }

    private void validateMultimedia() {
        if (this.gedcom.getMultimedia() != null) {
            for (Multimedia m : this.gedcom.getMultimedia().values()) {
                MultimediaValidator mv = new MultimediaValidator(this, m);
                mv.validate();
            }
        }
    }

    private void validateNotes() {
        int i = 0;
        for (Note n : this.gedcom.getNotes().values()) {
            new NoteValidator(this.rootValidator, ++i, n).validate();
        }
    }

    private void validateRepositories() {
        for (Map.Entry<String, Repository> e : this.gedcom.getRepositories().entrySet()) {
            if (e.getKey() == null) {
                this.addError("Entry in repositories collection has null key", e);
                return;
            }
            if (e.getValue() == null) {
                this.addError("Entry in repositories collection has null value", e);
                return;
            }
            if (!e.getKey().equals(e.getValue().getXref())) {
                this.addError("Entry in repositories collection is not keyed by the Repository's xref", e);
                return;
            }
            new RepositoryValidator(this.rootValidator, e.getValue()).validate();
        }
    }

    private void validateSources() {
        for (Map.Entry<String, Source> e : this.gedcom.getSources().entrySet()) {
            if (e.getKey() == null) {
                this.addError("Entry in sources collection has null key", e);
                return;
            }
            if (e.getValue() == null) {
                this.addError("Entry in sources collection has null value", e);
                return;
            }
            if (!e.getKey().equals(e.getValue().getXref())) {
                this.addError("Entry in sources collection is not keyed by the individual's xref", e);
                return;
            }
            new SourceValidator(this.rootValidator, e.getValue()).validate();
        }
    }

    private void validateSubmitters() {
        if (this.gedcom.getSubmitters().isEmpty()) {
            if (this.autorepairEnabled) {
                Submitter s = new Submitter();
                s.setXref("@SUBM0000@");
                s.setName(new StringWithCustomTags("UNSPECIFIED"));
                this.gedcom.getSubmitters().put(s.getXref(), s);
                this.addInfo("Submitters collection was empty - repaired", this.gedcom);
            } else {
                this.addError("Submitters collection is empty", this.gedcom);
            }
        }
        for (Submitter s : this.gedcom.getSubmitters().values()) {
            new SubmitterValidator(this.rootValidator, s).validate();
        }
    }

    private void validateTrailer() {
        if (this.gedcom.getTrailer() == null) {
            if (this.rootValidator.autorepairEnabled) {
                this.gedcom.setTrailer(new Trailer());
                this.rootValidator.addInfo("InfoTrader had no trailer - repaired", this.gedcom);
            } else {
                this.rootValidator.addError("InfoTrader has no trailer", this.gedcom);
            }
        }
    }
}

