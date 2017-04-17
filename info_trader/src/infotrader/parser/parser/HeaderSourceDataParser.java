/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.parser;

import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.HeaderSourceData;
import infotrader.parser.model.StringTree;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.parser.AbstractParser;
import infotrader.parser.parser.InfoTraderParser;
import infotrader.parser.parser.Tag;
import java.util.List;

class HeaderSourceDataParser
extends AbstractParser<HeaderSourceData> {
    HeaderSourceDataParser(InfoTraderParser gedcomParser, StringTree stringTree, HeaderSourceData loadInto) {
        super(gedcomParser, stringTree, loadInto);
    }

    @Override
    void parse() {
        ((HeaderSourceData)this.loadInto).setName(this.stringTree.getValue());
        if (this.stringTree.getChildren() != null) {
            for (StringTree ch : this.stringTree.getChildren()) {
                if (Tag.DATE.equalsText(ch.getTag())) {
                    ((HeaderSourceData)this.loadInto).setPublishDate(new StringWithCustomTags(ch));
                    continue;
                }
                this.unknownTag(ch, (AbstractElement)this.loadInto);
            }
        }
    }
}

