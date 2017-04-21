/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.parser;

import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.Address;
import infotrader.parser.model.Corporation;
import infotrader.parser.model.StringTree;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.parser.AbstractParser;
import infotrader.parser.parser.AddressParser;
import infotrader.parser.parser.InfoTraderParser;
import infotrader.parser.parser.Tag;
import java.util.List;

class CorporationParser
extends AbstractParser<Corporation> {
    CorporationParser(InfoTraderParser gedcomParser, StringTree stringTree, Corporation loadInto) {
        super(gedcomParser, stringTree, loadInto);
    }

    @Override
    void parse() {
        ((Corporation)this.loadInto).setBusinessName(this.stringTree.getValue());
        if (this.stringTree.getChildren() != null) {
            for (StringTree ch : this.stringTree.getChildren()) {
                if (Tag.ADDRESS.equalsText(ch.getTag())) {
                    Address address = new Address();
                    ((Corporation)this.loadInto).setAddress(address);
                    new AddressParser(this.InfoTraderParser, ch, address).parse();
                    continue;
                }
                if (Tag.PHONE.equalsText(ch.getTag())) {
                    ((Corporation)this.loadInto).getPhoneNumbers(true).add(new StringWithCustomTags(ch));
                    continue;
                }
                if (Tag.WEB_ADDRESS.equalsText(ch.getTag())) {
                    ((Corporation)this.loadInto).getWwwUrls(true).add(new StringWithCustomTags(ch));
                    if (!this.g55()) continue;
                    this.addWarning("InfoTrader version is 5.5 but WWW URL was specified for the corporation in the source system on line " + ch.getLineNum() + ", which is a InfoTrader 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    continue;
                }
                if (Tag.EMAIL.equalsText(ch.getTag())) {
                    ((Corporation)this.loadInto).getEmails(true).add(new StringWithCustomTags(ch));
                    if (!this.g55()) continue;
                    this.addWarning("InfoTrader version is 5.5 but emails was specified for the corporation in the source system on line " + ch.getLineNum() + ", which is a InfoTrader 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    continue;
                }
                this.unknownTag(ch, (AbstractElement)this.loadInto);
            }
        }
    }
}

