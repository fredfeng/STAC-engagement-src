/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.writer;

import infotrader.parser.exception.InfoTraderWriterException;
import infotrader.parser.exception.WriterCancelledException;
import infotrader.parser.model.ChangeDate;
import infotrader.parser.model.EventRecorded;
import infotrader.parser.model.Multimedia;
import infotrader.parser.model.Note;
import infotrader.parser.model.RepositoryCitation;
import infotrader.parser.model.Source;
import infotrader.parser.model.SourceCallNumber;
import infotrader.parser.model.SourceData;
import infotrader.parser.model.StringTree;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.model.UserReference;
import infotrader.parser.writer.AbstractEmitter;
import infotrader.parser.writer.ChangeDateEmitter;
import infotrader.parser.writer.InfoTraderWriter;
import infotrader.parser.writer.MultimediaLinksEmitter;
import infotrader.parser.writer.NotesEmitter;
import java.util.Collection;
import java.util.List;

class SourceEmitter
extends AbstractEmitter<Collection<Source>> {
    SourceEmitter(InfoTraderWriter baseWriter, int startLevel, Collection<Source> writeFrom) throws WriterCancelledException {
        super(baseWriter, startLevel, writeFrom);
    }

    @Override
    protected void emit() throws InfoTraderWriterException {
        for (Source s : this.writeFrom) {
            this.emitTag(0, s.getXref(), "SOUR");
            SourceData d = s.getData();
            if (d != null) {
                this.emitTag(1, "DATA");
                for (EventRecorded e : d.getEventsRecorded()) {
                    this.emitTagWithOptionalValue(2, "EVEN", e.getEventType());
                    this.emitTagIfValueNotNull(3, "DATE", e.getDatePeriod());
                    this.emitTagIfValueNotNull(3, "PLAC", e.getJurisdiction());
                }
                this.emitTagIfValueNotNull(2, "AGNC", d.getRespAgency());
                new NotesEmitter(this.baseWriter, 2, d.getNotes()).emit();
            }
            this.emitLinesOfText(1, "AUTH", s.getOriginatorsAuthors());
            this.emitLinesOfText(1, "TITL", s.getTitle());
            this.emitTagIfValueNotNull(1, "ABBR", s.getSourceFiledBy());
            this.emitLinesOfText(1, "PUBL", s.getPublicationFacts());
            this.emitLinesOfText(1, "TEXT", s.getSourceText());
            this.emitRepositoryCitation(1, s.getRepositoryCitation());
            new MultimediaLinksEmitter(this.baseWriter, 1, s.getMultimedia()).emit();
            new NotesEmitter(this.baseWriter, 1, s.getNotes()).emit();
            if (s.getUserReferences() != null) {
                for (UserReference u : s.getUserReferences()) {
                    this.emitTagWithRequiredValue(1, "REFN", u.getReferenceNum());
                    this.emitTagIfValueNotNull(2, "TYPE", u.getType());
                }
            }
            this.emitTagIfValueNotNull(1, "RIN", s.getRecIdNumber());
            new ChangeDateEmitter(this.baseWriter, 1, s.getChangeDate()).emit();
            this.emitCustomTags(1, s.getCustomTags());
        }
    }

    private void emitRepositoryCitation(int level, RepositoryCitation repositoryCitation) throws InfoTraderWriterException {
        if (repositoryCitation != null) {
            if (repositoryCitation.getRepositoryXref() == null) {
                throw new InfoTraderWriterException("Repository Citation has null repository reference");
            }
            this.emitTagWithRequiredValue(level, "REPO", repositoryCitation.getRepositoryXref());
            new NotesEmitter(this.baseWriter, level + 1, repositoryCitation.getNotes()).emit();
            if (repositoryCitation.getCallNumbers() != null) {
                for (SourceCallNumber scn : repositoryCitation.getCallNumbers()) {
                    this.emitTagWithRequiredValue(level + 1, "CALN", scn.getCallNumber());
                    this.emitTagIfValueNotNull(level + 2, "MEDI", scn.getMediaType());
                }
            }
            this.emitCustomTags(level + 1, repositoryCitation.getCustomTags());
        }
    }
}

