/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.Options;
import infotrader.parser.model.StringTree;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractElement
implements Serializable {
    private static final long serialVersionUID = -983667065483378388L;
    protected List<StringTree> customTags;

    public AbstractElement() {
        this.customTags = this.getCustomTags(Options.isCollectionInitializationEnabled());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        AbstractElement other = (AbstractElement)obj;
        if (this.getCustomTags() == null ? other.getCustomTags() != null : !this.getCustomTags().equals(other.getCustomTags())) {
            return false;
        }
        return true;
    }

    public List<StringTree> getCustomTags() {
        return this.customTags;
    }

    public List<StringTree> getCustomTags(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.customTags == null) {
            this.customTags = new ArrayList<StringTree>(0);
        }
        return this.customTags;
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + (this.getCustomTags() == null ? 0 : this.getCustomTags().hashCode());
        return result;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AbstractElement [");
        if (this.customTags != null) {
            builder.append("customTags=");
            builder.append(this.customTags);
        }
        builder.append("]");
        return builder.toString();
    }
}

