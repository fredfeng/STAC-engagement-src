/*
 * Decompiled with CFR 0_121.
 */
package infotrader.dataprocessing;

import infotrader.datamodel.DocumentI;
import infotrader.dataprocessing.Extractor;

public class ContentExtractor
extends Extractor {
    public ContentExtractor(DocumentI doc) {
        super(doc);
    }

    @Override
    public void extract(String doc) {
    }

    public void addLink(String link) {
        this.doc.addLink(link);
    }
}

