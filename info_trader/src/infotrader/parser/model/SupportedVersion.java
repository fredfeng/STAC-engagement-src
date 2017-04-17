/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.exception.UnsupportedVersionException;

public enum SupportedVersion {
    V5_5("5.5"),
    V5_5_1("5.5.1");
    
    private String stringRepresentation;

    public static SupportedVersion forString(String string) throws UnsupportedVersionException {
        if (SupportedVersion.V5_5.stringRepresentation.equals(string)) {
            return V5_5;
        }
        if (SupportedVersion.V5_5_1.stringRepresentation.equals(string)) {
            return V5_5_1;
        }
        throw new UnsupportedVersionException("Unsupported version: " + string);
    }

    private SupportedVersion(String stringRep) {
        this.stringRepresentation = stringRep.intern();
    }

    public String toString() {
        return this.stringRepresentation.intern();
    }
}

