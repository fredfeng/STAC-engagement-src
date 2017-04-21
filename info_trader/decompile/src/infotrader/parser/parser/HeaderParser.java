/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.parser;

import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.CharacterSet;
import infotrader.parser.model.Header;
import infotrader.parser.model.Note;
import infotrader.parser.model.SourceSystem;
import infotrader.parser.model.StringTree;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.model.Submitter;
import infotrader.parser.parser.AbstractParser;
import infotrader.parser.parser.InfoTraderParser;
import infotrader.parser.parser.NoteListParser;
import infotrader.parser.parser.SourceSystemParser;
import infotrader.parser.parser.Tag;
import java.util.List;

class HeaderParser
extends AbstractParser<Header> {
    HeaderParser(InfoTraderParser gedcomParser, StringTree stringTree, Header loadInto) {
        super(gedcomParser, stringTree, loadInto);
    }

    @Override
    void parse() {
        if (this.stringTree.getChildren() != null) {
            for (StringTree ch : this.stringTree.getChildren()) {
                if (Tag.SOURCE.equalsText(ch.getTag())) {
                    SourceSystem sourceSystem = new SourceSystem();
                    ((Header)this.loadInto).setSourceSystem(sourceSystem);
                    new SourceSystemParser(this.InfoTraderParser, ch, sourceSystem).parse();
                    continue;
                }
                if (Tag.DATE.equalsText(ch.getTag())) {
                    ((Header)this.loadInto).setDate(new StringWithCustomTags(ch));
                    if (ch.getChildren() == null || ch.getChildren().isEmpty()) continue;
                    ((Header)this.loadInto).setTime(new StringWithCustomTags(ch.getChildren().get(0)));
                    continue;
                }
                if (Tag.CHARACTER_SET.equalsText(ch.getTag())) {
                    ((Header)this.loadInto).setCharacterSet(new CharacterSet());
                    ((Header)this.loadInto).getCharacterSet().setCharacterSetName(new StringWithCustomTags(ch));
                    if (ch.getChildren() == null || ch.getChildren().isEmpty()) continue;
                    ((Header)this.loadInto).getCharacterSet().setVersionNum(new StringWithCustomTags(ch.getChildren().get(0)));
                    continue;
                }
                if (Tag.SUBMITTER.equalsText(ch.getTag())) {
                    ((Header)this.loadInto).setSubmitter(this.getSubmitter(ch.getValue()));
                    continue;
                }
                if (Tag.FILE.equalsText(ch.getTag())) {
                    ((Header)this.loadInto).setFileName(new StringWithCustomTags(ch));
                    continue;
                }
                if (Tag.LANGUAGE.equalsText(ch.getTag())) {
                    ((Header)this.loadInto).setLanguage(new StringWithCustomTags(ch));
                    continue;
                }
                if (Tag.PLACE.equalsText(ch.getTag())) {
                    ((Header)this.loadInto).setPlaceHierarchy(new StringWithCustomTags(ch.getChildren().get(0)));
                    continue;
                }
                if (Tag.NOTE.equalsText(ch.getTag())) {
                    new NoteListParser(this.InfoTraderParser, ch, ((Header)this.loadInto).getNotes(true)).parse();
                    continue;
                }
                this.unknownTag(ch, (AbstractElement)this.loadInto);
            }
        }
    }
}

