/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.Options;
import infotrader.parser.model.AbstractCitation;
import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.AbstractNameVariation;
import infotrader.parser.model.Note;
import infotrader.parser.model.StringWithCustomTags;
import java.util.ArrayList;
import java.util.List;

public class Place
extends AbstractElement {
    private static final long serialVersionUID = 7023491338742765865L;
    private List<AbstractCitation> citations;
    private StringWithCustomTags latitude;
    private StringWithCustomTags longitude;
    private List<Note> notes;
    private List<AbstractNameVariation> phonetic;
    private StringWithCustomTags placeFormat;
    private String placeName;
    private List<AbstractNameVariation> romanized;

    public Place() {
        this.citations = this.getCitations(Options.isCollectionInitializationEnabled());
        this.notes = this.getNotes(Options.isCollectionInitializationEnabled());
        this.phonetic = this.getPhonetic(Options.isCollectionInitializationEnabled());
        this.romanized = this.getRomanized(Options.isCollectionInitializationEnabled());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Place other = (Place)obj;
        if (this.citations == null ? other.citations != null : !this.citations.equals(other.citations)) {
            return false;
        }
        if (this.latitude == null ? other.latitude != null : !this.latitude.equals(other.latitude)) {
            return false;
        }
        if (this.longitude == null ? other.longitude != null : !this.longitude.equals(other.longitude)) {
            return false;
        }
        if (this.notes == null ? other.notes != null : !this.notes.equals(other.notes)) {
            return false;
        }
        if (this.phonetic == null ? other.phonetic != null : !this.phonetic.equals(other.phonetic)) {
            return false;
        }
        if (this.placeFormat == null ? other.placeFormat != null : !this.placeFormat.equals(other.placeFormat)) {
            return false;
        }
        if (this.placeName == null ? other.placeName != null : !this.placeName.equals(other.placeName)) {
            return false;
        }
        if (this.romanized == null ? other.romanized != null : !this.romanized.equals(other.romanized)) {
            return false;
        }
        return true;
    }

    public List<AbstractCitation> getCitations() {
        return this.citations;
    }

    public List<AbstractCitation> getCitations(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.citations == null) {
            this.citations = new ArrayList<AbstractCitation>(0);
        }
        return this.citations;
    }

    public StringWithCustomTags getLatitude() {
        return this.latitude;
    }

    public StringWithCustomTags getLongitude() {
        return this.longitude;
    }

    public List<Note> getNotes() {
        return this.notes;
    }

    public List<Note> getNotes(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.notes == null) {
            this.notes = new ArrayList<Note>(0);
        }
        return this.notes;
    }

    public List<AbstractNameVariation> getPhonetic() {
        return this.phonetic;
    }

    public List<AbstractNameVariation> getPhonetic(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.phonetic == null) {
            this.phonetic = new ArrayList<AbstractNameVariation>(0);
        }
        return this.phonetic;
    }

    public StringWithCustomTags getPlaceFormat() {
        return this.placeFormat;
    }

    public String getPlaceName() {
        return this.placeName;
    }

    public List<AbstractNameVariation> getRomanized() {
        return this.romanized;
    }

    public List<AbstractNameVariation> getRomanized(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.romanized == null) {
            this.romanized = new ArrayList<AbstractNameVariation>(0);
        }
        return this.romanized;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = super.hashCode();
        result = 31 * result + (this.citations == null ? 0 : this.citations.hashCode());
        result = 31 * result + (this.latitude == null ? 0 : this.latitude.hashCode());
        result = 31 * result + (this.longitude == null ? 0 : this.longitude.hashCode());
        result = 31 * result + (this.notes == null ? 0 : this.notes.hashCode());
        result = 31 * result + (this.phonetic == null ? 0 : this.phonetic.hashCode());
        result = 31 * result + (this.placeFormat == null ? 0 : this.placeFormat.hashCode());
        result = 31 * result + (this.placeName == null ? 0 : this.placeName.hashCode());
        result = 31 * result + (this.romanized == null ? 0 : this.romanized.hashCode());
        return result;
    }

    public void setLatitude(StringWithCustomTags latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(StringWithCustomTags longitude) {
        this.longitude = longitude;
    }

    public void setPlaceFormat(StringWithCustomTags placeFormat) {
        this.placeFormat = placeFormat;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Place [");
        if (this.citations != null) {
            builder.append("citations=");
            builder.append(this.citations);
            builder.append(", ");
        }
        if (this.latitude != null) {
            builder.append("latitude=");
            builder.append(this.latitude);
            builder.append(", ");
        }
        if (this.longitude != null) {
            builder.append("longitude=");
            builder.append(this.longitude);
            builder.append(", ");
        }
        if (this.notes != null) {
            builder.append("notes=");
            builder.append(this.notes);
            builder.append(", ");
        }
        if (this.phonetic != null) {
            builder.append("phonetic=");
            builder.append(this.phonetic);
            builder.append(", ");
        }
        if (this.placeFormat != null) {
            builder.append("placeFormat=");
            builder.append(this.placeFormat);
            builder.append(", ");
        }
        if (this.placeName != null) {
            builder.append("placeName=");
            builder.append(this.placeName);
            builder.append(", ");
        }
        if (this.romanized != null) {
            builder.append("romanized=");
            builder.append(this.romanized);
            builder.append(", ");
        }
        if (this.customTags != null) {
            builder.append("customTags=");
            builder.append(this.customTags);
        }
        builder.append("]");
        return builder.toString();
    }
}

