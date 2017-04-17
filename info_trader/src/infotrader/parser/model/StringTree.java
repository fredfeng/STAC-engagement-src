/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.Options;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StringTree
implements Serializable {
    private static final long serialVersionUID = 7858738185646682895L;
    private List<StringTree> children;
    private String id;
    private int level;
    private int lineNum;
    private StringTree parent;
    private String tag;
    private String value;

    public StringTree() {
        this.children = this.getChildren(Options.isCollectionInitializationEnabled());
        this.parent = null;
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
        StringTree other = (StringTree)obj;
        if (this.children == null ? other.children != null : !this.children.equals(other.children)) {
            return false;
        }
        if (this.id == null ? other.id != null : !this.id.equals(other.id)) {
            return false;
        }
        if (this.level != other.level) {
            return false;
        }
        if (this.lineNum != other.lineNum) {
            return false;
        }
        if (this.tag == null ? other.tag != null : !this.tag.equals(other.tag)) {
            return false;
        }
        if (this.value == null ? other.value != null : !this.value.equals(other.value)) {
            return false;
        }
        return true;
    }

    public List<StringTree> getChildren() {
        return this.children;
    }

    public List<StringTree> getChildren(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.children == null) {
            this.children = new ArrayList<StringTree>(0);
        }
        return this.children;
    }

    public String getId() {
        return this.id;
    }

    public int getLevel() {
        return this.level;
    }

    public int getLineNum() {
        return this.lineNum;
    }

    public StringTree getParent() {
        return this.parent;
    }

    public String getTag() {
        return this.tag;
    }

    public String getValue() {
        return this.value;
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + (this.children == null ? 0 : this.children.hashCode());
        result = 31 * result + (this.id == null ? 0 : this.id.hashCode());
        result = 31 * result + this.level;
        result = 31 * result + this.lineNum;
        result = 31 * result + (this.tag == null ? 0 : this.tag.hashCode());
        result = 31 * result + (this.value == null ? 0 : this.value.hashCode());
        return result;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }

    public void setParent(StringTree parent) {
        this.parent = parent;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Line " + this.lineNum + ": " + this.level + (this.id == null ? "" : new StringBuilder().append(" ").append(this.id).toString()) + " " + (this.tag == null ? "(null tag)" : this.tag) + " " + (this.value == null ? "(null value)" : this.value));
        if (this.children != null) {
            for (StringTree ch : this.children) {
                sb.append("\n").append(ch);
            }
        }
        return sb.toString();
    }
}

