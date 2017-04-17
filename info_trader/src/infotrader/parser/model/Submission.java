/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.model.Submitter;
import java.util.List;

public class Submission
extends AbstractElement {
    private static final long serialVersionUID = -2881845846882881000L;
    private StringWithCustomTags ancestorsCount;
    private StringWithCustomTags descendantsCount;
    private StringWithCustomTags nameOfFamilyFile;
    private StringWithCustomTags ordinanceProcessFlag;
    private StringWithCustomTags recIdNumber;
    private Submitter submitter;
    private StringWithCustomTags templeCode;
    private String xref;

    public Submission(String xref) {
        this.xref = xref;
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
        Submission other = (Submission)obj;
        if (this.ancestorsCount == null ? other.ancestorsCount != null : !this.ancestorsCount.equals(other.ancestorsCount)) {
            return false;
        }
        if (this.descendantsCount == null ? other.descendantsCount != null : !this.descendantsCount.equals(other.descendantsCount)) {
            return false;
        }
        if (this.nameOfFamilyFile == null ? other.nameOfFamilyFile != null : !this.nameOfFamilyFile.equals(other.nameOfFamilyFile)) {
            return false;
        }
        if (this.ordinanceProcessFlag == null ? other.ordinanceProcessFlag != null : !this.ordinanceProcessFlag.equals(other.ordinanceProcessFlag)) {
            return false;
        }
        if (this.recIdNumber == null ? other.recIdNumber != null : !this.recIdNumber.equals(other.recIdNumber)) {
            return false;
        }
        if (this.submitter == null ? other.submitter != null : !this.submitter.equals(other.submitter)) {
            return false;
        }
        if (this.templeCode == null ? other.templeCode != null : !this.templeCode.equals(other.templeCode)) {
            return false;
        }
        if (this.xref == null ? other.xref != null : !this.xref.equals(other.xref)) {
            return false;
        }
        return true;
    }

    public StringWithCustomTags getAncestorsCount() {
        return this.ancestorsCount;
    }

    public StringWithCustomTags getDescendantsCount() {
        return this.descendantsCount;
    }

    public StringWithCustomTags getNameOfFamilyFile() {
        return this.nameOfFamilyFile;
    }

    public StringWithCustomTags getOrdinanceProcessFlag() {
        return this.ordinanceProcessFlag;
    }

    public StringWithCustomTags getRecIdNumber() {
        return this.recIdNumber;
    }

    public Submitter getSubmitter() {
        return this.submitter;
    }

    public StringWithCustomTags getTempleCode() {
        return this.templeCode;
    }

    public String getXref() {
        return this.xref;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = super.hashCode();
        result = 31 * result + (this.ancestorsCount == null ? 0 : this.ancestorsCount.hashCode());
        result = 31 * result + (this.descendantsCount == null ? 0 : this.descendantsCount.hashCode());
        result = 31 * result + (this.nameOfFamilyFile == null ? 0 : this.nameOfFamilyFile.hashCode());
        result = 31 * result + (this.ordinanceProcessFlag == null ? 0 : this.ordinanceProcessFlag.hashCode());
        result = 31 * result + (this.recIdNumber == null ? 0 : this.recIdNumber.hashCode());
        result = 31 * result + (this.submitter == null ? 0 : this.submitter.hashCode());
        result = 31 * result + (this.templeCode == null ? 0 : this.templeCode.hashCode());
        result = 31 * result + (this.xref == null ? 0 : this.xref.hashCode());
        return result;
    }

    public void setAncestorsCount(StringWithCustomTags ancestorsCount) {
        this.ancestorsCount = ancestorsCount;
    }

    public void setDescendantsCount(StringWithCustomTags descendantsCount) {
        this.descendantsCount = descendantsCount;
    }

    public void setNameOfFamilyFile(StringWithCustomTags nameOfFamilyFile) {
        this.nameOfFamilyFile = nameOfFamilyFile;
    }

    public void setOrdinanceProcessFlag(StringWithCustomTags ordinanceProcessFlag) {
        this.ordinanceProcessFlag = ordinanceProcessFlag;
    }

    public void setRecIdNumber(StringWithCustomTags recIdNumber) {
        this.recIdNumber = recIdNumber;
    }

    public void setSubmitter(Submitter submitter) {
        this.submitter = submitter;
    }

    public void setTempleCode(StringWithCustomTags templeCode) {
        this.templeCode = templeCode;
    }

    public void setXref(String xref) {
        this.xref = xref;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Submission [");
        if (this.ancestorsCount != null) {
            builder.append("ancestorsCount=");
            builder.append(this.ancestorsCount);
            builder.append(", ");
        }
        if (this.descendantsCount != null) {
            builder.append("descendantsCount=");
            builder.append(this.descendantsCount);
            builder.append(", ");
        }
        if (this.nameOfFamilyFile != null) {
            builder.append("nameOfFamilyFile=");
            builder.append(this.nameOfFamilyFile);
            builder.append(", ");
        }
        if (this.ordinanceProcessFlag != null) {
            builder.append("ordinanceProcessFlag=");
            builder.append(this.ordinanceProcessFlag);
            builder.append(", ");
        }
        if (this.recIdNumber != null) {
            builder.append("recIdNumber=");
            builder.append(this.recIdNumber);
            builder.append(", ");
        }
        if (this.submitter != null) {
            builder.append("submitter=");
            builder.append(this.submitter);
            builder.append(", ");
        }
        if (this.templeCode != null) {
            builder.append("templeCode=");
            builder.append(this.templeCode);
            builder.append(", ");
        }
        if (this.xref != null) {
            builder.append("xref=");
            builder.append(this.xref);
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

