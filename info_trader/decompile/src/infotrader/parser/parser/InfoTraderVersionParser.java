/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.parser;

import infotrader.parser.exception.UnsupportedVersionException;
import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.InfoTraderVersion;
import infotrader.parser.model.StringTree;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.model.SupportedVersion;
import infotrader.parser.parser.AbstractParser;
import infotrader.parser.parser.InfoTraderParser;
import infotrader.parser.parser.Tag;
import java.util.List;

class InfoTraderVersionParser
extends AbstractParser<InfoTraderVersion> {
    InfoTraderVersionParser(InfoTraderParser gedcomParser, StringTree stringTree, InfoTraderVersion loadInto) {
        super(gedcomParser, stringTree, loadInto);
    }

    @Override
    void parse() {
        if (this.stringTree.getChildren() != null) {
            for (StringTree ch : this.stringTree.getChildren()) {
                if (Tag.VERSION.equalsText(ch.getTag())) {
                    SupportedVersion vn = null;
                    try {
                        vn = SupportedVersion.forString(ch.getValue());
                    }
                    catch (UnsupportedVersionException e) {
                        this.addError(e.getMessage());
                    }
                    ((InfoTraderVersion)this.loadInto).setVersionNumber(vn);
                    continue;
                }
                if (Tag.FORM.equalsText(ch.getTag())) {
                    ((InfoTraderVersion)this.loadInto).setInfoTraderForm(new StringWithCustomTags(ch));
                    continue;
                }
                this.unknownTag(ch, (AbstractElement)this.loadInto);
            }
        }
    }
}

