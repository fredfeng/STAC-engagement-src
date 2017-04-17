/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.StringWithCustomTags;
import java.util.List;

public abstract class AbstractNameVariation
extends AbstractElement {
    private static final long serialVersionUID = 5302060855856746189L;
    protected String variation;
    protected StringWithCustomTags variationType;

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
        AbstractNameVariation other = (AbstractNameVariation)obj;
        if (this.variation == null ? other.variation != null : !this.variation.equals(other.variation)) {
            return false;
        }
        if (this.variationType == null ? other.variationType != null : !this.variationType.equals(other.variationType)) {
            return false;
        }
        return true;
    }

    public String getVariation() {
        return this.variation;
    }

    public StringWithCustomTags getVariationType() {
        return this.variationType;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = super.hashCode();
        result = 31 * result + (this.variation == null ? 0 : this.variation.hashCode());
        result = 31 * result + (this.variationType == null ? 0 : this.variationType.hashCode());
        return result;
    }

    public void setVariation(String variation) {
        this.variation = variation;
    }

    public void setVariationType(StringWithCustomTags variationType) {
        this.variationType = variationType;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AbstractNameVariation [");
        if (this.variation != null) {
            builder.append("variation=");
            builder.append(this.variation);
            builder.append(", ");
        }
        if (this.variationType != null) {
            builder.append("variationType=");
            builder.append(this.variationType);
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

