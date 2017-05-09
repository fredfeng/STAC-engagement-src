/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

public class InstructionAnnotation {
    public static final InstructionAnnotation EMPTY = new InstructionAnnotation();
    private boolean empty;
    private int importance;
    private String message;

    private InstructionAnnotation() {
        this.setEmpty();
    }

    public InstructionAnnotation(int importance, String message) {
        if (message.isEmpty() && importance == 0) {
            this.setEmpty();
        } else {
            this.empty = false;
            this.importance = importance;
            this.message = message;
        }
    }

    private void setEmpty() {
        this.empty = true;
        this.importance = 0;
        this.message = "";
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public int getImportance() {
        return this.importance;
    }

    public String getMessage() {
        return this.message;
    }

    public String toString() {
        return "" + this.importance + ": " + this.getMessage();
    }

    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + this.importance;
        hash = 83 * hash + (this.message != null ? this.message.hashCode() : 0);
        return hash;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        InstructionAnnotation other = (InstructionAnnotation)obj;
        if (this.importance != other.importance) {
            return false;
        }
        if (this.message == null ? other.message != null : !this.message.equals(other.message)) {
            return false;
        }
        return true;
    }
}

