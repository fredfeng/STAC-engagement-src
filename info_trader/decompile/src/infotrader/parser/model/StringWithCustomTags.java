/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.StringTree;
import java.util.Collection;
import java.util.List;

public class StringWithCustomTags
extends AbstractElement {
    private static final long serialVersionUID = -2479578654715820890L;
    private String value;

    public StringWithCustomTags() {
    }

    public StringWithCustomTags(String string) {
        this.value = string;
    }

    public StringWithCustomTags(StringTree s) {
        this.value = s.getValue();
        List<StringTree> children = s.getChildren();
        if (children != null && !children.isEmpty()) {
            this.getCustomTags(true).addAll(children);
        }
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
        StringWithCustomTags other = (StringWithCustomTags)obj;
        if (this.value == null ? other.value != null : !this.value.equals(other.value)) {
            return false;
        }
        return true;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = super.hashCode();
        result = 31 * result + (this.value == null ? 0 : this.value.hashCode());
        return result;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.value == null ? "null" : this.value);
        if (this.getCustomTags() != null) {
            for (StringTree ct : this.getCustomTags()) {
                sb.append("\n");
                sb.append("" + ct.getLevel() + (ct.getId() == null ? "" : new StringBuilder().append(" ").append(ct.getId()).toString()) + " " + ct.getTag() + " " + ct.getValue());
            }
        }
        return sb.toString();
    }

    public String trim() {
        if (this.value == null) {
            return null;
        }
        return this.value.trim();
    }
}

