/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.validate;

import infotrader.parser.Options;
import infotrader.parser.model.AbstractCitation;
import infotrader.parser.model.AbstractNameVariation;
import infotrader.parser.model.Note;
import infotrader.parser.model.Place;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.validate.AbstractValidator;
import infotrader.parser.validate.CitationValidator;
import infotrader.parser.validate.DuplicateEliminator;
import infotrader.parser.validate.InfoTraderValidator;
import infotrader.parser.validate.NameVariationValidator;
import infotrader.parser.validate.NotesValidator;
import java.util.List;

class PlaceValidator
extends AbstractValidator {
    private final Place place;

    public PlaceValidator(InfoTraderValidator rootValidator, Place place) {
        this.rootValidator = rootValidator;
        this.place = place;
    }

    @Override
    protected void validate() {
        List<AbstractNameVariation> phonetic;
        int dups;
        List<AbstractNameVariation> romanized;
        if (this.place == null) {
            this.addError("Place is null and cannot be validated or repaired");
            return;
        }
        if (this.place.getCitations() == null && Options.isCollectionInitializationEnabled()) {
            if (this.rootValidator.isAutorepairEnabled()) {
                this.place.getCitations(true).clear();
                this.rootValidator.addInfo("Event had null list of citations - repaired", this.place);
            } else {
                this.rootValidator.addError("Event has null list of citations", this.place);
            }
        }
        if (this.place.getCitations() != null) {
            for (AbstractCitation c : this.place.getCitations()) {
                new CitationValidator(this.rootValidator, c).validate();
            }
        }
        this.checkCustomTags(this.place);
        this.checkOptionalString(this.place.getLatitude(), "latitude", (Object)this.place);
        this.checkOptionalString(this.place.getLongitude(), "longitude", (Object)this.place);
        new NotesValidator(this.rootValidator, this.place, this.place.getNotes()).validate();
        this.checkOptionalString(this.place.getPlaceFormat(), "place format", (Object)this.place);
        if (this.place.getPlaceName() == null) {
            if (this.rootValidator.isAutorepairEnabled()) {
                this.addError("Place name was unspecified and cannot be repaired");
            } else {
                this.addError("Place name was unspecified");
            }
        }
        if ((phonetic = this.place.getPhonetic()) == null && Options.isCollectionInitializationEnabled()) {
            if (this.rootValidator.isAutorepairEnabled()) {
                this.place.getPhonetic(true).clear();
                this.rootValidator.addInfo("Event had null list of phonetic name variations - repaired", this.place);
            } else {
                this.rootValidator.addError("Event has null list of phonetic name variations", this.place);
            }
        }
        if (phonetic != null) {
            int dups2;
            if (this.rootValidator.isAutorepairEnabled() && (dups2 = new DuplicateEliminator<AbstractNameVariation>(phonetic).process()) > 0) {
                this.rootValidator.addInfo("" + dups2 + " duplicate phonetic variations found and removed", this.place);
            }
            for (AbstractNameVariation nv : phonetic) {
                new NameVariationValidator(this.rootValidator, nv).validate();
            }
        }
        if ((romanized = this.place.getRomanized()) == null && Options.isCollectionInitializationEnabled()) {
            if (this.rootValidator.isAutorepairEnabled()) {
                this.place.getRomanized(true).clear();
                this.rootValidator.addInfo("Event had null list of romanized name variations - repaired", this.place);
            } else {
                this.rootValidator.addError("Event has null list of romanized name variations", this.place);
            }
        }
        if (this.rootValidator.isAutorepairEnabled() && (dups = new DuplicateEliminator<AbstractNameVariation>(romanized).process()) > 0) {
            this.rootValidator.addInfo("" + dups + " duplicate romanized variations found and removed", this.place);
        }
        if (romanized != null) {
            for (AbstractNameVariation nv : romanized) {
                new NameVariationValidator(this.rootValidator, nv).validate();
            }
        }
    }
}

