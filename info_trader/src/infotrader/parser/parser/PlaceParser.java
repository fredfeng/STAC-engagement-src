/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.parser;

import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.Note;
import infotrader.parser.model.Place;
import infotrader.parser.model.StringTree;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.parser.AbstractParser;
import infotrader.parser.parser.InfoTraderParser;
import infotrader.parser.parser.NoteListParser;
import infotrader.parser.parser.Tag;
import java.util.List;

class PlaceParser
extends AbstractParser<Place> {
    PlaceParser(InfoTraderParser gedcomParser, StringTree stringTree, Place loadInto) {
        super(gedcomParser, stringTree, loadInto);
    }

    @Override
    void parse() {
        ((Place)this.loadInto).setPlaceName(this.stringTree.getValue());
        if (this.stringTree.getChildren() != null) {
            for (StringTree ch : this.stringTree.getChildren()) {
                if (Tag.FORM.equalsText(ch.getTag())) {
                    ((Place)this.loadInto).setPlaceFormat(new StringWithCustomTags(ch));
                    continue;
                }
                if (Tag.NOTE.equalsText(ch.getTag())) {
                    List<Note> notes = ((Place)this.loadInto).getNotes(true);
                    new NoteListParser(this.InfoTraderParser, ch, notes).parse();
                    continue;
                }
                if (Tag.CONCATENATION.equalsText(ch.getTag())) {
                    ((Place)this.loadInto).setPlaceName(((Place)this.loadInto).getPlaceName() + (ch.getValue() == null ? "" : ch.getValue()));
                    continue;
                }
                if (Tag.CONTINUATION.equalsText(ch.getTag())) {
                    ((Place)this.loadInto).setPlaceName(((Place)this.loadInto).getPlaceName() + "\n" + (ch.getValue() == null ? "" : ch.getValue()));
                    continue;
                }
                if (Tag.MAP.equalsText(ch.getTag())) {
                    if (this.g55()) {
                        this.addWarning("InfoTrader version is 5.5 but a map coordinate was specified on a place on line " + ch.getLineNum() + ", which is a InfoTrader 5.5.1 feature." + "  Data loaded but cannot be re-written unless GEDCOM version changes.");
                    }
                    if (ch.getChildren() == null) continue;
                    for (StringTree gch : ch.getChildren()) {
                        if (Tag.LATITUDE.equalsText(gch.getTag())) {
                            ((Place)this.loadInto).setLatitude(new StringWithCustomTags(gch));
                            continue;
                        }
                        if (Tag.LONGITUDE.equalsText(gch.getTag())) {
                            ((Place)this.loadInto).setLongitude(new StringWithCustomTags(gch));
                            continue;
                        }
                        this.unknownTag(gch, (AbstractElement)this.loadInto);
                    }
                    continue;
                }
                this.unknownTag(ch, (AbstractElement)this.loadInto);
            }
        }
    }
}

