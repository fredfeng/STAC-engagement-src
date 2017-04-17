/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.model.SupportedVersion;
import java.util.List;

public class InfoTraderVersion
extends AbstractElement {
    private static final long serialVersionUID = -8766863038155122803L;
    private StringWithCustomTags InfoTraderForm = new StringWithCustomTags("LINEAGE-LINKED");
    private SupportedVersion versionNumber = SupportedVersion.V5_5_1;

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
        InfoTraderVersion other = (InfoTraderVersion)obj;
        if (this.InfoTraderForm == null ? other.InfoTraderForm != null : !this.InfoTraderForm.equals(other.InfoTraderForm)) {
            return false;
        }
        if (this.versionNumber == null ? other.versionNumber != null : !this.versionNumber.equals((Object)other.versionNumber)) {
            return false;
        }
        return true;
    }

    public StringWithCustomTags getInfoTraderForm() {
        return this.InfoTraderForm;
    }

    public SupportedVersion getVersionNumber() {
        return this.versionNumber;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = super.hashCode();
        result = 31 * result + (this.InfoTraderForm == null ? 0 : this.InfoTraderForm.hashCode());
        result = 31 * result + (this.versionNumber == null ? 0 : this.versionNumber.hashCode());
        return result;
    }

    public void setInfoTraderForm(StringWithCustomTags InfoTraderForm) {
        this.InfoTraderForm = InfoTraderForm;
    }

    public void setVersionNumber(SupportedVersion versionNumber) {
        this.versionNumber = versionNumber;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("InfoTraderVersion [");
        if (this.InfoTraderForm != null) {
            builder.append("InfoTraderForm=");
            builder.append(this.InfoTraderForm);
            builder.append(", ");
        }
        if (this.versionNumber != null) {
            builder.append("versionNumber=");
            builder.append((Object)this.versionNumber);
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

