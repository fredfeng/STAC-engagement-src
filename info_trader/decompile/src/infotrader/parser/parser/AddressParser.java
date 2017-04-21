/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.parser;

import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.Address;
import infotrader.parser.model.StringTree;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.parser.AbstractParser;
import infotrader.parser.parser.InfoTraderParser;
import infotrader.parser.parser.Tag;
import java.util.List;

class AddressParser
extends AbstractParser<Address> {
    AddressParser(InfoTraderParser InfoTraderParser2, StringTree stringTree, Address loadInto) {
        super(InfoTraderParser2, stringTree, loadInto);
    }

    @Override
    void parse() {
        if (this.stringTree.getValue() != null) {
            ((Address)this.loadInto).getLines(true).add(this.stringTree.getValue());
        }
        if (this.stringTree.getChildren() != null) {
            for (StringTree ch : this.stringTree.getChildren()) {
                if (Tag.CITY.equalsText(ch.getTag())) {
                    ((Address)this.loadInto).setCity(new StringWithCustomTags(ch));
                    continue;
                }
                if (Tag.STATE.equalsText(ch.getTag())) {
                    ((Address)this.loadInto).setStateProvince(new StringWithCustomTags(ch));
                    continue;
                }
                if (Tag.POSTAL_CODE.equalsText(ch.getTag())) {
                    ((Address)this.loadInto).setPostalCode(new StringWithCustomTags(ch));
                    continue;
                }
                if (Tag.COUNTRY.equalsText(ch.getTag())) {
                    ((Address)this.loadInto).setCountry(new StringWithCustomTags(ch));
                    continue;
                }
                if (Tag.CONCATENATION.equalsText(ch.getTag())) {
                    if (((Address)this.loadInto).getLines(true).isEmpty()) {
                        ((Address)this.loadInto).getLines().add(ch.getValue());
                        continue;
                    }
                    ((Address)this.loadInto).getLines().set(((Address)this.loadInto).getLines().size() - 1, ((Address)this.loadInto).getLines().get(((Address)this.loadInto).getLines().size() - 1) + ch.getValue());
                    continue;
                }
                if (Tag.CONTINUATION.equalsText(ch.getTag())) {
                    ((Address)this.loadInto).getLines(true).add(ch.getValue() == null ? "" : ch.getValue());
                    continue;
                }
                this.unknownTag(ch, (AbstractElement)this.loadInto);
            }
        }
    }
}

