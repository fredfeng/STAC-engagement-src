/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.writer;

import infotrader.parser.exception.InfoTraderWriterException;
import infotrader.parser.exception.WriterCancelledException;
import infotrader.parser.model.Address;
import infotrader.parser.model.StringTree;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.writer.AbstractEmitter;
import infotrader.parser.writer.InfoTraderWriter;
import java.util.List;

class AddressEmitter
extends AbstractEmitter<Address> {
    AddressEmitter(InfoTraderWriter baseWriter, int startLevel, Address writeFrom) throws WriterCancelledException {
        super(baseWriter, startLevel, writeFrom);
    }

    @Override
    protected void emit() throws InfoTraderWriterException {
        if (this.writeFrom == null) {
            return;
        }
        this.emitLinesOfText(this.startLevel, "ADDR", ((Address)this.writeFrom).getLines());
        this.emitTagIfValueNotNull(this.startLevel + 1, "ADR1", ((Address)this.writeFrom).getAddr1());
        this.emitTagIfValueNotNull(this.startLevel + 1, "ADR2", ((Address)this.writeFrom).getAddr2());
        this.emitTagIfValueNotNull(this.startLevel + 1, "CITY", ((Address)this.writeFrom).getCity());
        this.emitTagIfValueNotNull(this.startLevel + 1, "STAE", ((Address)this.writeFrom).getStateProvince());
        this.emitTagIfValueNotNull(this.startLevel + 1, "POST", ((Address)this.writeFrom).getPostalCode());
        this.emitTagIfValueNotNull(this.startLevel + 1, "CTRY", ((Address)this.writeFrom).getCountry());
        this.emitCustomTags(this.startLevel + 1, ((Address)this.writeFrom).getCustomTags());
    }
}

