/*
 * Decompiled with CFR 0_121.
 */
package infotrader.dataprocessing;

import infotrader.datamodel.DocumentI;

public abstract class Extractor {
    DocumentI doc;

    public Extractor(DocumentI doc) {
        this.doc = doc;
    }

    public abstract void extract(String var1);
}

