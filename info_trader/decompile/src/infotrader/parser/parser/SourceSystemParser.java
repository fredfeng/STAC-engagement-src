/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.parser;

import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.Corporation;
import infotrader.parser.model.HeaderSourceData;
import infotrader.parser.model.SourceSystem;
import infotrader.parser.model.StringTree;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.parser.AbstractParser;
import infotrader.parser.parser.CorporationParser;
import infotrader.parser.parser.HeaderSourceDataParser;
import infotrader.parser.parser.InfoTraderParser;
import infotrader.parser.parser.Tag;
import java.util.List;

class SourceSystemParser
extends AbstractParser<SourceSystem> {
    SourceSystemParser(InfoTraderParser gedcomParser, StringTree stringTree, SourceSystem loadInto) {
        super(gedcomParser, stringTree, loadInto);
    }

    @Override
    void parse() {
        ((SourceSystem)this.loadInto).setSystemId(this.stringTree.getValue());
        if (this.stringTree.getChildren() != null) {
            for (StringTree ch : this.stringTree.getChildren()) {
                if (Tag.VERSION.equalsText(ch.getTag())) {
                    ((SourceSystem)this.loadInto).setVersionNum(new StringWithCustomTags(ch));
                    continue;
                }
                if (Tag.NAME.equalsText(ch.getTag())) {
                    ((SourceSystem)this.loadInto).setProductName(new StringWithCustomTags(ch));
                    continue;
                }
                if (Tag.CORPORATION.equalsText(ch.getTag())) {
                    Corporation corporation = new Corporation();
                    ((SourceSystem)this.loadInto).setCorporation(corporation);
                    new CorporationParser(this.InfoTraderParser, ch, corporation).parse();
                    continue;
                }
                if (Tag.DATA_FOR_CITATION.equalsText(ch.getTag())) {
                    HeaderSourceData headerSourceData = new HeaderSourceData();
                    ((SourceSystem)this.loadInto).setSourceData(headerSourceData);
                    new HeaderSourceDataParser(this.InfoTraderParser, ch, headerSourceData).parse();
                    continue;
                }
                this.unknownTag(ch, (AbstractElement)this.loadInto);
            }
        }
    }
}

