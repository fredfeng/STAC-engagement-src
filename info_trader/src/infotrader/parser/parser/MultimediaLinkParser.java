/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.parser;

import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.FileReference;
import infotrader.parser.model.Multimedia;
import infotrader.parser.model.Note;
import infotrader.parser.model.StringTree;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.parser.AbstractParser;
import infotrader.parser.parser.FileReference551Parser;
import infotrader.parser.parser.InfoTraderParser;
import infotrader.parser.parser.NoteListParser;
import infotrader.parser.parser.Tag;
import java.util.List;

class MultimediaLinkParser
extends AbstractParser<List<Multimedia>> {
    public MultimediaLinkParser(InfoTraderParser gedcomParser, StringTree stringTree, List<Multimedia> loadInto) {
        super(gedcomParser, stringTree, loadInto);
    }

    @Override
    void parse() {
        Multimedia m;
        if (this.referencesAnotherNode(this.stringTree)) {
            m = this.getMultimedia(this.stringTree.getValue());
        } else {
            m = new Multimedia();
            this.loadFileReferences(m, this.stringTree);
        }
        ((List)this.loadInto).add(m);
    }

    private void loadFileReferences(Multimedia m, StringTree obje) {
        int fileTagCount = 0;
        int formTagCount = 0;
        if (obje.getChildren() != null) {
            for (StringTree ch : obje.getChildren()) {
                if (Tag.FILE.equalsText(ch.getTag())) {
                    ++fileTagCount;
                }
                if (!Tag.FORM.equalsText(ch.getTag())) continue;
                ++formTagCount;
            }
        }
        if (this.g55()) {
            if (fileTagCount > 1) {
                this.addWarning("InfoTrader version is 5.5, but multiple files referenced in multimedia reference on line " + obje.getLineNum() + ", which is only allowed in 5.5.1. " + "Data will be loaded, but cannot be written back out unless the InfoTrader version is changed to 5.5.1");
            }
            if (formTagCount == 0) {
                this.addWarning("InfoTrader version is 5.5, but there is not a FORM tag in the multimedia link on line " + obje.getLineNum() + ", a scenario which is only allowed in 5.5.1. " + "Data will be loaded, but cannot be written back out unless the InfoTrader version is changed to 5.5.1");
            }
        }
        if (formTagCount > 1) {
            this.addError("Multiple FORM tags were found for a multimedia file reference at line " + obje.getLineNum() + " - this is not compliant with any InfoTrader standard - data not loaded");
            return;
        }
        if (fileTagCount > 1 || formTagCount < fileTagCount) {
            this.loadFileReferences551(m, obje.getChildren());
        } else {
            this.loadFileReferences55(m, obje.getChildren());
        }
    }

    private void loadFileReferences55(Multimedia m, List<StringTree> objeChildren) {
        FileReference currentFileRef = new FileReference();
        m.getFileReferences(true).add(currentFileRef);
        if (objeChildren != null) {
            for (StringTree ch : objeChildren) {
                if (Tag.FORM.equalsText(ch.getTag())) {
                    currentFileRef.setFormat(new StringWithCustomTags(ch));
                    continue;
                }
                if (Tag.TITLE.equalsText(ch.getTag())) {
                    m.setEmbeddedTitle(new StringWithCustomTags(ch));
                    continue;
                }
                if (Tag.FILE.equalsText(ch.getTag())) {
                    currentFileRef.setReferenceToFile(new StringWithCustomTags(ch));
                    continue;
                }
                if (Tag.NOTE.equalsText(ch.getTag())) {
                    List<Note> notes = m.getNotes(true);
                    new NoteListParser(this.InfoTraderParser, ch, notes).parse();
                    continue;
                }
                this.unknownTag(ch, m);
            }
        }
    }

    private void loadFileReferences551(Multimedia m, List<StringTree> objeChildren) {
        if (objeChildren != null) {
            for (StringTree ch : objeChildren) {
                if (Tag.FILE.equalsText(ch.getTag())) {
                    FileReference fr = new FileReference();
                    m.getFileReferences(true).add(fr);
                    new FileReference551Parser(this.InfoTraderParser, ch, fr).parse();
                    continue;
                }
                if (Tag.TITLE.equalsText(ch.getTag())) {
                    if (m.getFileReferences() == null) continue;
                    for (FileReference fr : m.getFileReferences()) {
                        fr.setTitle(new StringWithCustomTags(ch.getTag().intern()));
                    }
                    continue;
                }
                if (Tag.NOTE.equalsText(ch.getTag())) {
                    List<Note> notes = m.getNotes(true);
                    new NoteListParser(this.InfoTraderParser, ch, notes).parse();
                    if (this.g55()) continue;
                    this.addWarning("InfoTrader version was 5.5.1, but a NOTE was found on a multimedia link on line " + ch.getLineNum() + ", which is no longer supported. " + "Data will be loaded, but cannot be written back out unless the InfoTrader version is changed to 5.5");
                    continue;
                }
                this.unknownTag(ch, m);
            }
        }
    }
}

