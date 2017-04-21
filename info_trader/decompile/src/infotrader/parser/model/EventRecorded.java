/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.StringWithCustomTags;
import java.util.List;

public class EventRecorded
extends AbstractElement {
    private static final long serialVersionUID = -6640977866006853891L;
    private StringWithCustomTags datePeriod;
    private String eventType;
    private StringWithCustomTags jurisdiction;

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
        EventRecorded other = (EventRecorded)obj;
        if (this.datePeriod == null ? other.datePeriod != null : !this.datePeriod.equals(other.datePeriod)) {
            return false;
        }
        if (this.eventType == null ? other.eventType != null : !this.eventType.equals(other.eventType)) {
            return false;
        }
        if (this.jurisdiction == null ? other.jurisdiction != null : !this.jurisdiction.equals(other.jurisdiction)) {
            return false;
        }
        return true;
    }

    public StringWithCustomTags getDatePeriod() {
        return this.datePeriod;
    }

    public String getEventType() {
        return this.eventType;
    }

    public StringWithCustomTags getJurisdiction() {
        return this.jurisdiction;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = super.hashCode();
        result = 31 * result + (this.datePeriod == null ? 0 : this.datePeriod.hashCode());
        result = 31 * result + (this.eventType == null ? 0 : this.eventType.hashCode());
        result = 31 * result + (this.jurisdiction == null ? 0 : this.jurisdiction.hashCode());
        return result;
    }

    public void setDatePeriod(StringWithCustomTags datePeriod) {
        this.datePeriod = datePeriod;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setJurisdiction(StringWithCustomTags jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EventRecorded [");
        if (this.datePeriod != null) {
            builder.append("datePeriod=");
            builder.append(this.datePeriod);
            builder.append(", ");
        }
        if (this.eventType != null) {
            builder.append("eventType=");
            builder.append(this.eventType);
            builder.append(", ");
        }
        if (this.jurisdiction != null) {
            builder.append("jurisdiction=");
            builder.append(this.jurisdiction);
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

