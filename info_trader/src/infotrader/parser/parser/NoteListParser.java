/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.parser;

import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.InfoTrader;
import infotrader.parser.model.Note;
import infotrader.parser.model.StringTree;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.parser.AbstractParser;
import infotrader.parser.parser.InfoTraderParser;
import infotrader.parser.parser.Tag;
import java.util.List;
import java.util.Map;

class NoteListParser
extends AbstractParser<List<Note>> {
    NoteListParser(InfoTraderParser gedcomParser, StringTree stringTree, List<Note> loadInto) {
        super(gedcomParser, stringTree, loadInto);
    }

    @Override
    void parse() {
        Note note;
        if (this.stringTree.getId() == null && this.referencesAnotherNode(this.stringTree)) {
            Note note2 = this.getNote(this.stringTree.getValue());
            ((List)this.loadInto).add(note2);
            return;
        }
        if (this.stringTree.getId() == null) {
            note = new Note();
            ((List)this.loadInto).add(note);
        } else {
            if (this.referencesAnotherNode(this.stringTree)) {
                this.addWarning("NOTE line has both an XREF_ID (" + this.stringTree.getId() + ") and SUBMITTER_TEXT (" + this.stringTree.getValue() + ") value between @ signs - treating SUBMITTER_TEXT as string, not a cross-reference");
            }
            note = this.getNote(this.stringTree.getId());
        }
        note.getLines(true).add(this.stringTree.getValue());
        if (this.stringTree.getChildren() != null) {
            for (StringTree ch : this.stringTree.getChildren()) {
                if (Tag.CONCATENATION.equalsText(ch.getTag())) {
                    if (note.getLines().isEmpty()) {
                        note.getLines(true).add(ch.getValue());
                        continue;
                    }
                    String lastNote = note.getLines().get(note.getLines().size() - 1);
                    if (lastNote == null || lastNote.length() == 0) {
                        note.getLines().set(note.getLines().size() - 1, ch.getValue());
                        continue;
                    }
                    note.getLines().set(note.getLines().size() - 1, lastNote + ch.getValue());
                    continue;
                }
                if (Tag.CONTINUATION.equalsText(ch.getTag())) {
                    note.getLines(true).add(ch.getValue() == null ? "" : ch.getValue());
                    continue;
                }
                if (Tag.RECORD_ID_NUMBER.equalsText(ch.getTag())) {
                    note.setRecIdNumber(new StringWithCustomTags(ch));
                    continue;
                }
                this.unknownTag(ch, note);
            }
        }
    }

    private Note getNote(String xref) {
        Note note = this.InfoTraderParser.getInfoTrader().getNotes().get(xref);
        if (note == null) {
            note = new Note();
            note.setXref(xref);
            this.InfoTraderParser.getInfoTrader().getNotes().put(xref, note);
        }
        return note;
    }
}

