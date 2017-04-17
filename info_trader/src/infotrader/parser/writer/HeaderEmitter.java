/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.writer;

import infotrader.parser.exception.InfoTraderWriterException;
import infotrader.parser.exception.WriterCancelledException;
import infotrader.parser.model.Address;
import infotrader.parser.model.CharacterSet;
import infotrader.parser.model.Corporation;
import infotrader.parser.model.Header;
import infotrader.parser.model.HeaderSourceData;
import infotrader.parser.model.InfoTraderVersion;
import infotrader.parser.model.Note;
import infotrader.parser.model.SourceSystem;
import infotrader.parser.model.StringTree;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.model.Submission;
import infotrader.parser.model.Submitter;
import infotrader.parser.model.SupportedVersion;
import infotrader.parser.writer.AbstractEmitter;
import infotrader.parser.writer.AddressEmitter;
import infotrader.parser.writer.InfoTraderWriter;
import infotrader.parser.writer.NotesEmitter;
import java.util.Collection;
import java.util.List;

class HeaderEmitter
extends AbstractEmitter<Header> {
    HeaderEmitter(InfoTraderWriter baseWriter, int startLevel, Header writeFrom) throws WriterCancelledException {
        super(baseWriter, startLevel, writeFrom);
    }

    @Override
    protected void emit() throws InfoTraderWriterException {
        Header header = (Header)this.writeFrom;
        if (header == null) {
            header = new Header();
        }
        this.baseWriter.lines.add("0 HEAD");
        this.emitSourceSystem(header.getSourceSystem());
        this.emitTagIfValueNotNull(1, "DEST", header.getDestinationSystem());
        if (header.getDate() != null) {
            this.emitTagIfValueNotNull(1, "DATE", header.getDate());
            this.emitTagIfValueNotNull(2, "TIME", header.getTime());
        }
        if (header.getSubmitter() != null) {
            this.emitTagWithRequiredValue(1, "SUBM", header.getSubmitter().getXref());
        }
        if (header.getSubmission() != null) {
            this.emitTagWithRequiredValue(1, "SUBN", header.getSubmission().getXref());
        }
        this.emitTagIfValueNotNull(1, "FILE", header.getFileName());
        this.emitLinesOfText(1, "COPR", header.getCopyrightData());
        this.emitTag(1, "GEDC");
        this.emitTagWithRequiredValue(2, "VERS", header.getInfoTraderVersion().getVersionNumber().toString());
        this.emitTagWithRequiredValue(2, "FORM", header.getInfoTraderVersion().getInfoTraderForm());
        this.emitTagWithRequiredValue(1, "CHAR", header.getCharacterSet().getCharacterSetName());
        this.emitTagIfValueNotNull(2, "VERS", header.getCharacterSet().getVersionNum());
        this.emitTagIfValueNotNull(1, "LANG", header.getLanguage());
        if (header.getPlaceHierarchy() != null && header.getPlaceHierarchy().getValue() != null && header.getPlaceHierarchy().getValue().length() > 0) {
            this.emitTag(1, "PLAC");
            this.emitTagWithRequiredValue(2, "FORM", header.getPlaceHierarchy());
        }
        new NotesEmitter(this.baseWriter, 1, header.getNotes()).emit();
        this.emitCustomTags(1, header.getCustomTags());
    }

    private void emitSourceSystem(SourceSystem sourceSystem) throws InfoTraderWriterException {
        HeaderSourceData sourceData;
        if (sourceSystem == null) {
            return;
        }
        this.emitTagWithRequiredValue(1, "SOUR", sourceSystem.getSystemId());
        this.emitTagIfValueNotNull(2, "VERS", sourceSystem.getVersionNum());
        this.emitTagIfValueNotNull(2, "NAME", sourceSystem.getProductName());
        Corporation corporation = sourceSystem.getCorporation();
        if (corporation != null) {
            this.emitTagWithOptionalValue(2, "CORP", corporation.getBusinessName());
            new AddressEmitter(this.baseWriter, 3, corporation.getAddress()).emit();
            this.emitStringsWithCustomTags(3, corporation.getPhoneNumbers(), "PHON");
            this.emitStringsWithCustomTags(3, corporation.getFaxNumbers(), "FAX");
            this.emitStringsWithCustomTags(3, corporation.getWwwUrls(), "WWW");
            this.emitStringsWithCustomTags(3, corporation.getEmails(), "EMAIL");
        }
        if ((sourceData = sourceSystem.getSourceData()) != null) {
            this.emitTagIfValueNotNull(2, "DATA", sourceData.getName());
            this.emitTagIfValueNotNull(3, "DATE", sourceData.getPublishDate());
            this.emitTagIfValueNotNull(3, "COPR", sourceData.getCopyright());
        }
        this.emitCustomTags(1, sourceSystem.getCustomTags());
    }
}

