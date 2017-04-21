/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.Corporation;
import infotrader.parser.model.HeaderSourceData;
import infotrader.parser.model.StringWithCustomTags;
import java.util.List;

public class SourceSystem
extends AbstractElement {
    private static final long serialVersionUID = -1121533380857695596L;
    private Corporation corporation;
    private StringWithCustomTags productName;
    private HeaderSourceData sourceData;
    private String systemId = "UNSPECIFIED";
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
        SourceSystem other = (SourceSystem)obj;
        if (this.corporation == null ? other.corporation != null : !this.corporation.equals(other.corporation)) {
            return false;
        }
        if (this.productName == null ? other.productName != null : !this.productName.equals(other.productName)) {
            return false;
        }
        if (this.sourceData == null ? other.sourceData != null : !this.sourceData.equals(other.sourceData)) {
            return false;
        }
        if (this.systemId == null ? other.systemId != null : !this.systemId.equals(other.systemId)) {
            return false;
        }
        if (this.versionNum == null ? other.versionNum != null : !this.versionNum.equals(other.versionNum)) {
            return false;
        }
        return true;
    }

    public Corporation getCorporation() {
        return this.corporation;
    }

    public StringWithCustomTags getProductName() {
        return this.productName;
    }

    public HeaderSourceData getSourceData() {
        return this.sourceData;
    }

    public String getSystemId() {
        return this.systemId;
    }

    public StringWithCustomTags getVersionNum() {
        return this.versionNum;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = super.hashCode();
        result = 31 * result + (this.corporation == null ? 0 : this.corporation.hashCode());
        result = 31 * result + (this.productName == null ? 0 : this.productName.hashCode());
        result = 31 * result + (this.sourceData == null ? 0 : this.sourceData.hashCode());
        result = 31 * result + (this.systemId == null ? 0 : this.systemId.hashCode());
        result = 31 * result + (this.versionNum == null ? 0 : this.versionNum.hashCode());
        return result;
    }

    public void setCorporation(Corporation corporation) {
        this.corporation = corporation;
    }

    public void setProductName(StringWithCustomTags productName) {
        this.productName = productName;
    }

    public void setSourceData(HeaderSourceData sourceData) {
        this.sourceData = sourceData;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public void setVersionNum(StringWithCustomTags versionNum) {
        this.versionNum = versionNum;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SourceSystem [");
        if (this.corporation != null) {
            builder.append("corporation=");
            builder.append(this.corporation);
            builder.append(", ");
        }
        if (this.productName != null) {
            builder.append("productName=");
            builder.append(this.productName);
            builder.append(", ");
        }
        if (this.sourceData != null) {
            builder.append("sourceData=");
            builder.append(this.sourceData);
            builder.append(", ");
        }
        if (this.systemId != null) {
            builder.append("systemId=");
            builder.append(this.systemId);
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

