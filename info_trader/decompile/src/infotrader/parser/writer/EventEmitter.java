/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.writer;

import infotrader.parser.exception.InfoTraderWriterException;
import infotrader.parser.exception.WriterCancelledException;
import infotrader.parser.model.AbstractCitation;
import infotrader.parser.model.AbstractEvent;
import infotrader.parser.model.Address;
import infotrader.parser.model.Multimedia;
import infotrader.parser.model.Note;
import infotrader.parser.model.Place;
import infotrader.parser.model.StringTree;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.writer.AbstractEmitter;
import infotrader.parser.writer.AddressEmitter;
import infotrader.parser.writer.InfoTraderWriter;
import infotrader.parser.writer.MultimediaLinksEmitter;
import infotrader.parser.writer.NotesEmitter;
import infotrader.parser.writer.PlaceEmitter;
import infotrader.parser.writer.SourceCitationEmitter;
import java.util.Collection;
import java.util.List;

class EventEmitter
extends AbstractEmitter<AbstractEvent> {
    EventEmitter(InfoTraderWriter baseWriter, int startLevel, AbstractEvent writeFrom) throws WriterCancelledException {
        super(baseWriter, startLevel, writeFrom);
    }

    @Override
    protected void emit() throws InfoTraderWriterException {
        this.emitTagIfValueNotNull(this.startLevel, "TYPE", ((AbstractEvent)this.writeFrom).getSubType());
        this.emitTagIfValueNotNull(this.startLevel, "DATE", ((AbstractEvent)this.writeFrom).getDate());
        new PlaceEmitter(this.baseWriter, this.startLevel, ((AbstractEvent)this.writeFrom).getPlace()).emit();
        new AddressEmitter(this.baseWriter, this.startLevel, ((AbstractEvent)this.writeFrom).getAddress()).emit();
        this.emitTagIfValueNotNull(this.startLevel, "AGE", ((AbstractEvent)this.writeFrom).getAge());
        this.emitTagIfValueNotNull(this.startLevel, "AGNC", ((AbstractEvent)this.writeFrom).getRespAgency());
        this.emitTagIfValueNotNull(this.startLevel, "CAUS", ((AbstractEvent)this.writeFrom).getCause());
        this.emitTagIfValueNotNull(this.startLevel, "RELI", ((AbstractEvent)this.writeFrom).getReligiousAffiliation());
        this.emitTagIfValueNotNull(this.startLevel, "RESN", ((AbstractEvent)this.writeFrom).getRestrictionNotice());
        new SourceCitationEmitter(this.baseWriter, this.startLevel, ((AbstractEvent)this.writeFrom).getCitations()).emit();
        new MultimediaLinksEmitter(this.baseWriter, this.startLevel, ((AbstractEvent)this.writeFrom).getMultimedia()).emit();
        new NotesEmitter(this.baseWriter, this.startLevel, ((AbstractEvent)this.writeFrom).getNotes()).emit();
        this.emitCustomTags(this.startLevel, ((AbstractEvent)this.writeFrom).getCustomTags());
    }
}

