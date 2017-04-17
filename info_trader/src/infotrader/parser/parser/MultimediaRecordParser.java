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
import infotrader.parser.parser.MultimediaLinkParser;
import infotrader.parser.parser.NoteListParser;
import infotrader.parser.parser.Tag;
import java.util.ArrayList;
import java.util.List;

class MultimediaRecordParser
extends AbstractParser<Multimedia> {
    MultimediaRecordParser(InfoTraderParser gedcomParser, StringTree stringTree, Multimedia loadInto) {
        super(gedcomParser, stringTree, loadInto);
    }

    @Override
    void parse() {
        int fileTagCount = 0;
        if (this.stringTree.getChildren() != null) {
            for (StringTree ch : this.stringTree.getChildren()) {
                if (!Tag.FILE.equalsText(ch.getTag())) continue;
                ++fileTagCount;
            }
        }
        if (fileTagCount > 0) {
            if (this.g55()) {
                this.addWarning("InfoTrader version was 5.5, but a 5.5.1-style multimedia record was found at line " + this.stringTree.getLineNum() + ". " + "Data will be loaded, but might have problems being written until the version is for the data is changed to 5.5.1");
            }
            this.loadMultimediaRecord551(this.stringTree);
        } else {
            if (!this.g55()) {
                this.addWarning("InfoTrader version is 5.5.1, but a 5.5-style multimedia record was found at line " + this.stringTree.getLineNum() + ". " + "Data will be loaded, but might have problems being written until the version is for the data is changed to 5.5.1");
            }
            this.loadMultimediaRecord55(this.stringTree);
        }
    }

    private void loadMultimediaRecord55(StringTree obje) {
        if (obje.getChildren() == null) {
            this.addError("Root level multimedia record at line " + obje.getLineNum() + " had no child records");
        } else {
            for (StringTree ch : obje.getChildren()) {
                if (Tag.FORM.equalsText(ch.getTag())) {
                    ((Multimedia)this.loadInto).setEmbeddedMediaFormat(new StringWithCustomTags(ch));
                    continue;
                }
                if (Tag.TITLE.equalsText(ch.getTag())) {
                    ((Multimedia)this.loadInto).setEmbeddedTitle(new StringWithCustomTags(ch));
                    continue;
                }
                if (Tag.NOTE.equalsText(ch.getTag())) {
                    List<Note> notes = ((Multimedia)this.loadInto).getNotes(true);
                    new NoteListParser(this.InfoTraderParser, ch, notes).parse();
                    continue;
                }
                if (Tag.BLOB.equalsText(ch.getTag())) {
                    this.loadMultiLinesOfText(ch, ((Multimedia)this.loadInto).getBlob(true), (AbstractElement)this.loadInto);
                    if (this.g55()) continue;
                    this.addWarning("InfoTrader version is 5.5.1, but a BLOB tag was found at line " + ch.getLineNum() + ". " + "Data will be loaded but will not be writeable unless InfoTrader version is changed to 5.5.1");
                    continue;
                }
                if (Tag.OBJECT_MULTIMEDIA.equalsText(ch.getTag())) {
                    ArrayList<Multimedia> continuedObjects = new ArrayList<Multimedia>();
                    new MultimediaLinkParser(this.InfoTraderParser, ch, continuedObjects).parse();
                    ((Multimedia)this.loadInto).setContinuedObject(continuedObjects.get(0));
                    if (this.g55()) continue;
                    this.addWarning("InfoTrader version is 5.5.1, but a chained OBJE tag was found at line " + ch.getLineNum() + ". " + "Data will be loaded but will not be writeable unless InfoTrader version is changed to 5.5.1");
                    continue;
                }
                if (Tag.RECORD_ID_NUMBER.equalsText(ch.getTag())) {
                    ((Multimedia)this.loadInto).setRecIdNumber(new StringWithCustomTags(ch));
                    continue;
                }
                this.unknownTag(ch, (AbstractElement)this.loadInto);
            }
        }
    }

    private void loadMultimediaRecord551(StringTree obje) {
        Multimedia m = this.getMultimedia(obje.getId());
        if (obje.getChildren() != null) {
            for (StringTree ch : obje.getChildren()) {
                if (Tag.FILE.equalsText(ch.getTag())) {
                    FileReference fr = new FileReference();
                    m.getFileReferences(true).add(fr);
                    new FileReference551Parser(this.InfoTraderParser, ch, fr).parse();
                    continue;
                }
                if (Tag.NOTE.equalsText(ch.getTag())) {
                    List<Note> notes = m.getNotes(true);
                    new NoteListParser(this.InfoTraderParser, ch, notes).parse();
                    continue;
                }
                if (Tag.RECORD_ID_NUMBER.equalsText(ch.getTag())) {
                    m.setRecIdNumber(new StringWithCustomTags(ch));
                    continue;
                }
                this.unknownTag(ch, m);
            }
        }
    }
}

