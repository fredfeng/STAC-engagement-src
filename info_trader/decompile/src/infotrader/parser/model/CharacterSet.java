/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.io.encoding.Encoding;
import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.StringWithCustomTags;
import java.util.List;

public class CharacterSet
extends AbstractElement {
    private static final long serialVersionUID = 1745150676480256110L;
    private StringWithCustomTags characterSetName = new StringWithCustomTags(Encoding.ANSEL.toString());
    private StringWithCustomTags versionNum;

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
        CharacterSet other = (CharacterSet)obj;
        if (this.characterSetName == null ? other.characterSetName != null : !this.characterSetName.equals(other.characterSetName)) {
            return false;
        }
        if (this.versionNum == null ? other.versionNum != null : !this.versionNum.equals(other.versionNum)) {
            return false;
        }
        return true;
    }

    public StringWithCustomTags getCharacterSetName() {
        return this.characterSetName;
    }

    public StringWithCustomTags getVersionNum() {
        return this.versionNum;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = super.hashCode();
        result = 31 * result + (this.characterSetName == null ? 0 : this.characterSetName.hashCode());
        result = 31 * result + (this.versionNum == null ? 0 : this.versionNum.hashCode());
        return result;
    }

    public void setCharacterSetName(StringWithCustomTags characterSetName) {
        this.characterSetName = characterSetName;
    }

    public void setVersionNum(StringWithCustomTags versionNum) {
        this.versionNum = versionNum;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CharacterSet [");
        if (this.characterSetName != null) {
            builder.append("characterSetName=");
            builder.append(this.characterSetName);
            builder.append(", ");
        }
        if (this.versionNum != null) {
            builder.append("versionNum=");
            builder.append(this.versionNum);
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

