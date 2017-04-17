/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.Options;
import infotrader.parser.model.AbstractCitation;
import infotrader.parser.model.Note;
import java.util.ArrayList;
import java.util.List;

public class CitationWithoutSource
extends AbstractCitation {
    private static final long serialVersionUID = -3562494505830574223L;
    private List<String> description;
    private List<List<String>> textFromSource;

    public CitationWithoutSource() {
        this.description = this.getDescription(Options.isCollectionInitializationEnabled());
        this.textFromSource = this.getTextFromSource(Options.isCollectionInitializationEnabled());
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
        CitationWithoutSource other = (CitationWithoutSource)obj;
        if (this.description == null ? other.description != null : !this.description.equals(other.description)) {
            return false;
        }
        if (this.getNotes() == null ? other.getNotes() != null : !this.getNotes().equals(other.getNotes())) {
            return false;
        }
        if (this.textFromSource == null ? other.textFromSource != null : !this.textFromSource.equals(other.textFromSource)) {
            return false;
        }
        return true;
    }

    public List<String> getDescription() {
        return this.description;
    }

    public List<String> getDescription(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.description == null) {
            this.description = new ArrayList<String>(0);
        }
        return this.description;
    }

    public List<List<String>> getTextFromSource() {
        return this.textFromSource;
    }

    public List<List<String>> getTextFromSource(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.textFromSource == null) {
            this.textFromSource = new ArrayList<List<String>>(0);
        }
        return this.textFromSource;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = super.hashCode();
        result = 31 * result + (this.description == null ? 0 : this.description.hashCode());
        result = 31 * result + (this.getNotes() == null ? 0 : this.getNotes().hashCode());
        result = 31 * result + (this.textFromSource == null ? 0 : this.textFromSource.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CitationWithoutSource [");
        if (this.description != null) {
            builder.append("description=");
            builder.append(this.description);
            builder.append(", ");
        }
        if (this.textFromSource != null) {
            builder.append("textFromSource=");
            builder.append(this.textFromSource);
        }
        builder.append("]");
        return builder.toString();
    }
}

