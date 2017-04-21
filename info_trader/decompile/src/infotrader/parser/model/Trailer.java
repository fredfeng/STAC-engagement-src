/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import java.io.Serializable;

public class Trailer
implements Serializable {
    private static final long serialVersionUID = 2300633464080707042L;

    public boolean equals(Object obj) {
        return obj instanceof Trailer;
    }

    public int hashCode() {
        return 1;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Trailer []");
        return builder.toString();
    }
}

