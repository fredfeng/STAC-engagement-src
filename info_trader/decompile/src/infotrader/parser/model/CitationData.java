/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.Options;
import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.StringWithCustomTags;
import java.util.ArrayList;
import java.util.List;

public class CitationData
extends AbstractElement {
    private static final long serialVersionUID = -423230002337524774L;
    private StringWithCustomTags entryDate;
    private List<List<String>> sourceText;

    public CitationData() {
        this.sourceText = this.getSourceText(Options.isCollectionInitializationEnabled());
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
        CitationData other = (CitationData)obj;
        if (this.entryDate == null ? other.entryDate != null : !this.entryDate.equals(other.entryDate)) {
            return false;
        }
        if (this.sourceText == null ? other.sourceText != null : !this.sourceText.equals(other.sourceText)) {
            return false;
        }
        return true;
    }

    public StringWithCustomTags getEntryDate() {
        return this.entryDate;
    }

    public List<List<String>> getSourceText() {
        return this.sourceText;
    }

    public List<List<String>> getSourceText(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.sourceText == null) {
            this.sourceText = new ArrayList<List<String>>(0);
        }
        return this.sourceText;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = super.hashCode();
        result = 31 * result + (this.entryDate == null ? 0 : this.entryDate.hashCode());
        result = 31 * result + (this.sourceText == null ? 0 : this.sourceText.hashCode());
        return result;
    }

    public void setEntryDate(StringWithCustomTags entryDate) {
        this.entryDate = entryDate;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CitationData [");
        if (this.entryDate != null) {
            builder.append("entryDate=");
            builder.append(this.entryDate);
            builder.append(", ");
        }
        if (this.sourceText != null) {
            builder.append("sourceText=");
            builder.append(this.sourceText);
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

