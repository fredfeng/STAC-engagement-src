/*
 * Decompiled with CFR 0_121.
 */
package infotrader.dataprocessing;

import infotrader.parser.exception.InfoTraderWriterException;

public class DocumentDisplayGenerator {
    StringBuffer res = new StringBuffer();

    public String getResult() {
        return this.res.toString();
    }

    public void appendln(String line) throws InfoTraderWriterException {
        this.res.append(line);
        this.res.append("/n");
    }

    public void end() {
        this.res.append("/end");
    }
}

