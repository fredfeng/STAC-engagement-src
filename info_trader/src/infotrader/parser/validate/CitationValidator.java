/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.validate;

import infotrader.parser.Options;
import infotrader.parser.model.AbstractCitation;
import infotrader.parser.model.CitationWithSource;
import infotrader.parser.model.CitationWithoutSource;
import infotrader.parser.model.Note;
import infotrader.parser.model.Source;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.validate.AbstractValidator;
import infotrader.parser.validate.DuplicateEliminator;
import infotrader.parser.validate.InfoTraderValidator;
import infotrader.parser.validate.NotesValidator;
import java.util.List;

class CitationValidator
extends AbstractValidator {
    private final AbstractCitation citation;

    public CitationValidator(InfoTraderValidator rootValidator, AbstractCitation citation) {
        this.rootValidator = rootValidator;
        this.citation = citation;
    }

    @Override
    protected void validate() {
        if (this.citation == null) {
            this.addError("Citation is null");
            return;
        }
        if (this.citation instanceof CitationWithSource) {
            CitationWithSource c = (CitationWithSource)this.citation;
            if (c.getSource() == null) {
                this.addError("CitationWithSource requires a non-null source reference", c);
            }
            this.checkOptionalString(c.getWhereInSource(), "where within source", (Object)c);
            this.checkOptionalString(c.getEventCited(), "event type cited from", (Object)c);
            if (c.getEventCited() == null) {
                if (c.getRoleInEvent() != null) {
                    this.addError("CitationWithSource has role in event but a null event");
                }
            } else {
                this.checkOptionalString(c.getRoleInEvent(), "role in event", (Object)c);
            }
            this.checkOptionalString(c.getCertainty(), "certainty/quality", (Object)c);
        } else if (this.citation instanceof CitationWithoutSource) {
            CitationWithoutSource c = (CitationWithoutSource)this.citation;
            this.checkStringList(c.getDescription(), "description on a citation without a source", true);
            List<List<String>> textFromSource = c.getTextFromSource();
            if (textFromSource == null && Options.isCollectionInitializationEnabled()) {
                if (this.rootValidator.isAutorepairEnabled()) {
                    c.getTextFromSource(true).clear();
                    this.addInfo("Text from source collection (the list of lists) was null in CitationWithoutSource - autorepaired", this.citation);
                } else {
                    this.addError("Text from source collection (the list of lists) is null in CitationWithoutSource", this.citation);
                }
            } else {
                int dups;
                if (this.rootValidator.isAutorepairEnabled() && (dups = new DuplicateEliminator<List<String>>(textFromSource).process()) > 0) {
                    this.rootValidator.addInfo("" + dups + " duplicate texts from source found and removed", this.citation);
                }
                if (textFromSource != null) {
                    for (List<String> sl : textFromSource) {
                        if (sl == null) {
                            this.addError("Text from source collection (the list of lists) in CitationWithoutSource contains a null", this.citation);
                            continue;
                        }
                        this.checkStringList(sl, "one of the sublists in the textFromSource collection on a source citation", true);
                    }
                }
            }
        } else {
            throw new IllegalStateException("AbstractCitation references must be either CitationWithSource instances or CitationWithoutSource instances");
        }
        if (this.citation.getNotes() == null && Options.isCollectionInitializationEnabled()) {
            if (this.rootValidator.isAutorepairEnabled()) {
                this.citation.getNotes(true).clear();
                this.addInfo("Notes collection was null on " + this.citation.getClass().getSimpleName() + " - autorepaired");
            } else {
                this.addError("Notes collection is null on " + this.citation.getClass().getSimpleName());
            }
        } else {
            new NotesValidator(this.rootValidator, this.citation, this.citation.getNotes()).validate();
        }
    }
}

