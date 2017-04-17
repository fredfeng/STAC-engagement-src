/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.parser;

import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.EventRecorded;
import infotrader.parser.model.Multimedia;
import infotrader.parser.model.Note;
import infotrader.parser.model.RepositoryCitation;
import infotrader.parser.model.Source;
import infotrader.parser.model.SourceCallNumber;
import infotrader.parser.model.SourceData;
import infotrader.parser.model.StringTree;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.parser.AbstractParser;
import infotrader.parser.parser.InfoTraderParser;
import infotrader.parser.parser.MultimediaLinkParser;
import infotrader.parser.parser.NoteListParser;
import infotrader.parser.parser.Tag;
import java.util.List;

class SourceParser
extends AbstractParser<Source> {
    SourceParser(InfoTraderParser gedcomParser, StringTree stringTree, Source loadInto) {
        super(gedcomParser, stringTree, loadInto);
    }

    @Override
    void parse() {
        if (this.stringTree.getChildren() != null) {
            for (StringTree ch : this.stringTree.getChildren()) {
                if (Tag.DATA_FOR_SOURCE.equalsText(ch.getTag())) {
                    ((Source)this.loadInto).setData(new SourceData());
                    this.loadSourceData(ch, ((Source)this.loadInto).getData());
                    continue;
                }
                if (Tag.TITLE.equalsText(ch.getTag())) {
                    this.loadMultiLinesOfText(ch, ((Source)this.loadInto).getTitle(true), (AbstractElement)this.loadInto);
                    continue;
                }
                if (Tag.TEXT.equalsText(ch.getTag())) {
                    this.loadMultiLinesOfText(ch, ((Source)this.loadInto).getSourceText(true), (AbstractElement)this.loadInto);
                    continue;
                }
                if (Tag.ABBREVIATION.equalsText(ch.getTag())) {
                    ((Source)this.loadInto).setSourceFiledBy(new StringWithCustomTags(ch));
                    continue;
                }
                if (Tag.AUTHORS.equalsText(ch.getTag())) {
                    this.loadMultiLinesOfText(ch, ((Source)this.loadInto).getOriginatorsAuthors(true), (AbstractElement)this.loadInto);
                    continue;
                }
                if (Tag.REPOSITORY.equalsText(ch.getTag())) {
                    ((Source)this.loadInto).setRepositoryCitation(this.loadRepositoryCitation(ch));
                    continue;
                }
                if (Tag.NOTE.equalsText(ch.getTag())) {
                    List<Note> notes = ((Source)this.loadInto).getNotes(true);
                    new NoteListParser(this.InfoTraderParser, ch, notes).parse();
                    continue;
                }
                if (Tag.OBJECT_MULTIMEDIA.equalsText(ch.getTag())) {
                    List<Multimedia> multimedia = ((Source)this.loadInto).getMultimedia(true);
                    new MultimediaLinkParser(this.InfoTraderParser, ch, multimedia).parse();
                    continue;
                }
                if (Tag.RECORD_ID_NUMBER.equalsText(ch.getTag())) {
                    ((Source)this.loadInto).setRecIdNumber(new StringWithCustomTags(ch));
                    continue;
                }
                this.unknownTag(ch, (AbstractElement)this.loadInto);
            }
        }
    }

    private RepositoryCitation loadRepositoryCitation(StringTree repo) {
        RepositoryCitation r = new RepositoryCitation();
        r.setRepositoryXref(repo.getValue());
        if (repo.getChildren() != null) {
            for (StringTree ch : repo.getChildren()) {
                if (Tag.NOTE.equalsText(ch.getTag())) {
                    List<Note> notes = r.getNotes(true);
                    new NoteListParser(this.InfoTraderParser, ch, notes).parse();
                    continue;
                }
                if (Tag.CALL_NUMBER.equalsText(ch.getTag())) {
                    SourceCallNumber scn = new SourceCallNumber();
                    r.getCallNumbers(true).add(scn);
                    scn.setCallNumber(new StringWithCustomTags(ch.getValue()));
                    if (ch.getChildren() == null) continue;
                    for (StringTree gch : ch.getChildren()) {
                        if (Tag.MEDIA.equalsText(gch.getTag())) {
                            scn.setMediaType(new StringWithCustomTags(gch));
                            continue;
                        }
                        this.unknownTag(gch, scn.getCallNumber());
                    }
                    continue;
                }
                this.unknownTag(ch, r);
            }
        }
        return r;
    }

    private void loadSourceData(StringTree dataNode, SourceData sourceData) {
        if (dataNode.getChildren() != null) {
            for (StringTree ch : dataNode.getChildren()) {
                if (Tag.NOTE.equalsText(ch.getTag())) {
                    List<Note> notes = sourceData.getNotes(true);
                    new NoteListParser(this.InfoTraderParser, ch, notes).parse();
                    continue;
                }
                this.unknownTag(ch, sourceData);
            }
        }
    }

    private void loadSourceDataEventRecorded(StringTree dataNode, SourceData sourceData) {
        EventRecorded e = new EventRecorded();
        sourceData.getEventsRecorded(true).add(e);
        e.setEventType(dataNode.getValue());
        if (dataNode.getChildren() != null) {
            for (StringTree ch : dataNode.getChildren()) {
                if (Tag.DATE.equalsText(ch.getTag())) {
                    e.setDatePeriod(new StringWithCustomTags(ch));
                    continue;
                }
                if (Tag.PLACE.equalsText(ch.getTag())) {
                    e.setJurisdiction(new StringWithCustomTags(ch));
                    continue;
                }
                this.unknownTag(ch, sourceData);
            }
        }
    }
}

