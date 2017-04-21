/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.parser;

import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.ChangeDate;
import infotrader.parser.model.Note;
import infotrader.parser.model.StringTree;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.parser.AbstractParser;
import infotrader.parser.parser.InfoTraderParser;
import infotrader.parser.parser.NoteListParser;
import infotrader.parser.parser.Tag;
import java.util.List;

class ChangeDateParser
extends AbstractParser<ChangeDate> {
    ChangeDateParser(InfoTraderParser gedcomParser, StringTree stringTree, ChangeDate loadInto) {
        super(gedcomParser, stringTree, loadInto);
    }

    @Override
    void parse() {
        if (this.stringTree.getChildren() != null) {
            for (StringTree ch : this.stringTree.getChildren()) {
                if (Tag.DATE.equalsText(ch.getTag())) {
                    ((ChangeDate)this.loadInto).setDate(new StringWithCustomTags(ch.getValue()));
                    if (ch.getChildren().isEmpty()) continue;
                    ((ChangeDate)this.loadInto).setTime(new StringWithCustomTags(ch.getChildren().get(0)));
                    continue;
                }
                if (Tag.NOTE.equalsText(ch.getTag())) {
                    List<Note> notes = ((ChangeDate)this.loadInto).getNotes(true);
                    new NoteListParser(this.InfoTraderParser, ch, notes).parse();
                    continue;
                }
                this.unknownTag(ch, (AbstractElement)this.loadInto);
            }
        }
    }
}

