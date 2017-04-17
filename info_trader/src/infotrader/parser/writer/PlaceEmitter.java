/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.writer;

import infotrader.parser.exception.InfoTraderWriterException;
import infotrader.parser.exception.InfoTraderWriterVersionDataMismatchException;
import infotrader.parser.exception.WriterCancelledException;
import infotrader.parser.model.AbstractCitation;
import infotrader.parser.model.AbstractNameVariation;
import infotrader.parser.model.Note;
import infotrader.parser.model.Place;
import infotrader.parser.model.StringTree;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.writer.AbstractEmitter;
import infotrader.parser.writer.InfoTraderWriter;
import infotrader.parser.writer.NotesEmitter;
import infotrader.parser.writer.SourceCitationEmitter;
import java.util.Collection;
import java.util.List;

class PlaceEmitter
extends AbstractEmitter<Place> {
    PlaceEmitter(InfoTraderWriter baseWriter, int startLevel, Place writeFrom) throws WriterCancelledException {
        super(baseWriter, startLevel, writeFrom);
    }

    @Override
    protected void emit() throws InfoTraderWriterException {
        if (this.writeFrom == null) {
            return;
        }
        this.emitTagWithOptionalValue(this.startLevel, "PLAC", ((Place)this.writeFrom).getPlaceName());
        this.emitTagIfValueNotNull(this.startLevel + 1, "FORM", ((Place)this.writeFrom).getPlaceFormat());
        new SourceCitationEmitter(this.baseWriter, this.startLevel + 1, ((Place)this.writeFrom).getCitations()).emit();
        new NotesEmitter(this.baseWriter, this.startLevel + 1, ((Place)this.writeFrom).getNotes()).emit();
        if (((Place)this.writeFrom).getRomanized() != null) {
            for (AbstractNameVariation nv : ((Place)this.writeFrom).getRomanized()) {
                if (this.g55()) {
                    throw new InfoTraderWriterVersionDataMismatchException("InfoTrader version is 5.5, but romanized variation was specified on place " + ((Place)this.writeFrom).getPlaceName() + ", which is only allowed in InfoTrader 5.5.1");
                }
                this.emitTagWithRequiredValue(this.startLevel + 1, "ROMN", nv.getVariation());
                this.emitTagIfValueNotNull(this.startLevel + 2, "TYPE", nv.getVariationType());
            }
        }
        if (((Place)this.writeFrom).getPhonetic() != null) {
            for (AbstractNameVariation nv : ((Place)this.writeFrom).getPhonetic()) {
                if (this.g55()) {
                    throw new InfoTraderWriterVersionDataMismatchException("InfoTrader version is 5.5, but phonetic variation was specified on place " + ((Place)this.writeFrom).getPlaceName() + ", which is only allowed in InfoTrader 5.5.1");
                }
                this.emitTagWithRequiredValue(this.startLevel + 1, "FONE", nv.getVariation());
                this.emitTagIfValueNotNull(this.startLevel + 2, "TYPE", nv.getVariationType());
            }
        }
        if (((Place)this.writeFrom).getLatitude() != null || ((Place)this.writeFrom).getLongitude() != null) {
            this.emitTag(this.startLevel + 1, "MAP");
            this.emitTagWithRequiredValue(this.startLevel + 2, "LATI", ((Place)this.writeFrom).getLatitude());
            this.emitTagWithRequiredValue(this.startLevel + 2, "LONG", ((Place)this.writeFrom).getLongitude());
            if (this.g55()) {
                throw new InfoTraderWriterVersionDataMismatchException("InfoTrader version is 5.5, but map coordinates were specified on place " + ((Place)this.writeFrom).getPlaceName() + ", which is only allowed in InfoTrader 5.5.1");
            }
        }
        this.emitCustomTags(this.startLevel + 1, ((Place)this.writeFrom).getCustomTags());
    }
}

