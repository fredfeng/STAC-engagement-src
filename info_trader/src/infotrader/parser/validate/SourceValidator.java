/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.validate;

import infotrader.parser.Options;
import infotrader.parser.model.ChangeDate;
import infotrader.parser.model.EventRecorded;
import infotrader.parser.model.Multimedia;
import infotrader.parser.model.Note;
import infotrader.parser.model.RepositoryCitation;
import infotrader.parser.model.Source;
import infotrader.parser.model.SourceCallNumber;
import infotrader.parser.model.SourceData;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.model.UserReference;
import infotrader.parser.validate.AbstractValidator;
import infotrader.parser.validate.DuplicateEliminator;
import infotrader.parser.validate.InfoTraderValidator;
import infotrader.parser.validate.MultimediaValidator;
import infotrader.parser.validate.NotesValidator;
import java.util.List;

class SourceValidator
extends AbstractValidator {
    private final Source source;

    public SourceValidator(InfoTraderValidator rootValidator, Source source) {
        this.rootValidator = rootValidator;
        this.source = source;
    }

    @Override
    protected void validate() {
        List<Multimedia> multimedia;
        if (this.source == null) {
            this.addError("Source is null and cannot be validated");
            return;
        }
        this.checkXref(this.source);
        this.checkChangeDate(this.source.getChangeDate(), this.source);
        if (this.source.getData() != null) {
            SourceData sd = this.source.getData();
            new NotesValidator(this.rootValidator, sd, sd.getNotes()).validate();
            this.checkOptionalString(sd.getRespAgency(), "responsible agency", (Object)sd);
            List<EventRecorded> eventsRecorded = sd.getEventsRecorded();
            if (eventsRecorded == null) {
                if (this.rootValidator.isAutorepairEnabled()) {
                    sd.getEventsRecorded(true).clear();
                    this.addInfo("Collection of recorded events in source data structure was null - autorepaired", sd);
                } else {
                    this.addError("Collection of recorded events in source data structure is null", sd);
                }
            } else {
                int dups;
                if (this.rootValidator.isAutorepairEnabled() && (dups = new DuplicateEliminator<EventRecorded>(eventsRecorded).process()) > 0) {
                    this.rootValidator.addInfo("" + dups + " duplicate recorded events found and removed", sd);
                }
                for (EventRecorded er : eventsRecorded) {
                    this.checkOptionalString(er.getDatePeriod(), "date period", (Object)er);
                    this.checkOptionalString(er.getEventType(), "event type", (Object)er);
                    this.checkOptionalString(er.getJurisdiction(), "jurisdiction", (Object)er);
                }
            }
        }
        if ((multimedia = this.source.getMultimedia()) == null && Options.isCollectionInitializationEnabled()) {
            if (this.rootValidator.isAutorepairEnabled()) {
                this.source.getMultimedia(true).clear();
                this.addInfo("Multimedia collection on source was null - autorepaired", this.source);
            }
            this.addError("Multimedia collection on source is null", this.source);
        } else {
            int dups;
            if (this.rootValidator.isAutorepairEnabled() && (dups = new DuplicateEliminator<Multimedia>(multimedia).process()) > 0) {
                this.rootValidator.addInfo("" + dups + " duplicate multimedia found and removed", this.source);
            }
            if (multimedia != null) {
                for (Multimedia mm : multimedia) {
                    new MultimediaValidator(this.rootValidator, mm).validate();
                }
            }
        }
        new NotesValidator(this.rootValidator, this.source, this.source.getNotes()).validate();
        this.checkStringList(this.source.getOriginatorsAuthors(), "originators/authors", false);
        this.checkStringList(this.source.getPublicationFacts(), "publication facts", false);
        this.checkOptionalString(this.source.getRecIdNumber(), "automated record id", (Object)this.source);
        this.checkStringList(this.source.getSourceText(), "source text", true);
        this.checkOptionalString(this.source.getSourceFiledBy(), "source filed by", (Object)this.source);
        this.checkStringList(this.source.getTitle(), "title", true);
        this.checkUserReferences(this.source.getUserReferences(), this.source);
        RepositoryCitation c = this.source.getRepositoryCitation();
        if (c != null) {
            new NotesValidator(this.rootValidator, c, c.getNotes()).validate();
            this.checkRequiredString(c.getRepositoryXref(), "repository xref", (Object)c);
            this.checkCallNumbers(c);
        }
    }

    private void checkCallNumbers(RepositoryCitation citation) {
        List<SourceCallNumber> callNumbers = citation.getCallNumbers();
        if (callNumbers == null && Options.isCollectionInitializationEnabled()) {
            if (this.rootValidator.isAutorepairEnabled()) {
                citation.getCallNumbers(true).clear();
                this.addInfo("Call numbers collection on repository citation was null - autorepaired", citation);
            } else {
                this.addError("Call numbers collection on repository citation is null", citation);
            }
        } else {
            int dups;
            if (this.rootValidator.isAutorepairEnabled() && (dups = new DuplicateEliminator<SourceCallNumber>(callNumbers).process()) > 0) {
                this.rootValidator.addInfo("" + dups + " duplicate source call numbers found and removed", citation);
            }
            if (callNumbers != null) {
                for (SourceCallNumber scn : callNumbers) {
                    this.checkOptionalString(scn.getCallNumber(), "call number", (Object)scn);
                    if (scn.getCallNumber() == null) {
                        if (scn.getMediaType() == null) continue;
                        this.addError("You cannot specify media type without a call number in a SourceCallNumber structure", scn);
                        continue;
                    }
                    this.checkOptionalString(scn.getMediaType(), "media type", (Object)scn);
                }
            }
        }
    }
}

