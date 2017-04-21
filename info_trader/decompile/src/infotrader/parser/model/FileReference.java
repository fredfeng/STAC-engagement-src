/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.StringWithCustomTags;
import java.util.List;

public class FileReference
extends AbstractElement {
    private static final long serialVersionUID = -5131568367094232521L;
    private StringWithCustomTags format;
    private StringWithCustomTags mediaType;
    private StringWithCustomTags referenceToFile;
    private StringWithCustomTags title;

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
        FileReference other = (FileReference)obj;
        if (this.format == null ? other.format != null : !this.format.equals(other.format)) {
            return false;
        }
        if (this.mediaType == null ? other.mediaType != null : !this.mediaType.equals(other.mediaType)) {
            return false;
        }
        if (this.referenceToFile == null ? other.referenceToFile != null : !this.referenceToFile.equals(other.referenceToFile)) {
            return false;
        }
        if (this.title == null ? other.title != null : !this.title.equals(other.title)) {
            return false;
        }
        return true;
    }

    public StringWithCustomTags getFormat() {
        return this.format;
    }

    public StringWithCustomTags getMediaType() {
        return this.mediaType;
    }

    public StringWithCustomTags getReferenceToFile() {
        return this.referenceToFile;
    }

    public StringWithCustomTags getTitle() {
        return this.title;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = super.hashCode();
        result = 31 * result + (this.format == null ? 0 : this.format.hashCode());
        result = 31 * result + (this.mediaType == null ? 0 : this.mediaType.hashCode());
        result = 31 * result + (this.referenceToFile == null ? 0 : this.referenceToFile.hashCode());
        result = 31 * result + (this.title == null ? 0 : this.title.hashCode());
        return result;
    }

    public void setFormat(StringWithCustomTags format) {
        this.format = format;
    }

    public void setMediaType(StringWithCustomTags mediaType) {
        this.mediaType = mediaType;
    }

    public void setReferenceToFile(StringWithCustomTags referenceToFile) {
        this.referenceToFile = referenceToFile;
    }

    public void setTitle(StringWithCustomTags title) {
        this.title = title;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FileReference [");
        if (this.format != null) {
            builder.append("format=");
            builder.append(this.format);
            builder.append(", ");
        }
        if (this.mediaType != null) {
            builder.append("mediaType=");
            builder.append(this.mediaType);
            builder.append(", ");
        }
        if (this.referenceToFile != null) {
            builder.append("referenceToFile=");
            builder.append(this.referenceToFile);
            builder.append(", ");
        }
        if (this.title != null) {
            builder.append("title=");
            builder.append(this.title);
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

