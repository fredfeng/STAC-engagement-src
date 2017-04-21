/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.parser;

import infotrader.parser.exception.InfoTraderParserException;
import infotrader.parser.exception.ParserCancelledException;
import infotrader.parser.io.event.FileProgressEvent;
import infotrader.parser.io.event.FileProgressListener;
import infotrader.parser.io.reader.InfoTraderFileReader;
import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.Header;
import infotrader.parser.model.InfoTrader;
import infotrader.parser.model.Multimedia;
import infotrader.parser.model.Note;
import infotrader.parser.model.Source;
import infotrader.parser.model.StringTree;
import infotrader.parser.model.Trailer;
import infotrader.parser.parser.AbstractParser;
import infotrader.parser.parser.HeaderParser;
import infotrader.parser.parser.MultimediaRecordParser;
import infotrader.parser.parser.NoteListParser;
import infotrader.parser.parser.SourceParser;
import infotrader.parser.parser.StringTreeBuilder;
import infotrader.parser.parser.Tag;
import infotrader.parser.parser.event.ParseProgressEvent;
import infotrader.parser.parser.event.ParseProgressListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class InfoTraderParser
extends AbstractParser<InfoTrader> {
    private final List<String> errors = new ArrayList<String>();
    public final InfoTrader infoTrader = new InfoTrader();
    private boolean strictCustomTags = true;
    private boolean strictLineBreaks = true;
    private final List<String> warnings = new ArrayList<String>();
    private boolean cancelled;
    private int readNotificationRate = 500;
    private final List<WeakReference<FileProgressListener>> fileObservers = new CopyOnWriteArrayList<WeakReference<FileProgressListener>>();
    private final List<WeakReference<ParseProgressListener>> parseObservers = new CopyOnWriteArrayList<WeakReference<ParseProgressListener>>();
    private int parseNotificationRate = 500;
    private StringTreeBuilder stringTreeBuilder;
    private int lineNum;

    public InfoTraderParser() {
        super(null, null, null);
        this.InfoTraderParser = this;
    }

    public void cancel() {
        this.cancelled = true;
    }

    public List<String> getErrors() {
        return this.errors;
    }

    public List<WeakReference<FileProgressListener>> getFileObservers() {
        return this.fileObservers;
    }

    public InfoTrader getInfoTrader() {
        return this.infoTrader;
    }

    public int getParseNotificationRate() {
        return this.parseNotificationRate;
    }

    public List<WeakReference<ParseProgressListener>> getParseObservers() {
        return this.parseObservers;
    }

    public int getReadNotificationRate() {
        return this.readNotificationRate;
    }

    public List<String> getWarnings() {
        return this.warnings;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public boolean isStrictCustomTags() {
        return this.strictCustomTags;
    }

    public boolean isStrictLineBreaks() {
        return this.strictLineBreaks;
    }

    public void load(BufferedInputStream bytes) throws IOException, InfoTraderParserException {
        this.lineNum = 0;
        this.errors.clear();
        this.warnings.clear();
        this.cancelled = false;
        if (this.cancelled) {
            throw new ParserCancelledException("File load/parse cancelled");
        }
        InfoTraderFileReader gfr = new InfoTraderFileReader(this, bytes);
        this.stringTreeBuilder = new StringTreeBuilder(this);
        String line = gfr.nextLine();
        while (line != null) {
            if (line.charAt(0) == '0') {
                this.parseAndLoadPreviousStringTree();
            }
            ++this.lineNum;
            this.stringTreeBuilder.appendLine(line);
            line = gfr.nextLine();
            if (this.cancelled) {
                throw new ParserCancelledException("File load/parse is cancelled");
            }
            if (this.lineNum % this.parseNotificationRate != 0) continue;
            this.notifyParseObservers(new ParseProgressEvent(this, this.infoTrader, false, this.lineNum));
        }
        this.parseAndLoadPreviousStringTree();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void load(File filename) throws IOException, InfoTraderParserException {
        FileInputStream fis = new FileInputStream(filename);
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(fis);
            this.load(bis);
        }
        finally {
            if (bis != null) {
                bis.close();
            }
            fis.close();
        }
    }

    public void notifyFileObservers(FileProgressEvent e) {
        int i = 0;
        while (i < this.fileObservers.size()) {
            WeakReference<FileProgressListener> observerRef = this.fileObservers.get(i);
            if (observerRef == null) {
                this.fileObservers.remove(observerRef);
                continue;
            }
            FileProgressListener l = observerRef.get();
            if (l != null) {
                l.progressNotification(e);
            }
            ++i;
        }
    }

    public void registerFileObserver(FileProgressListener observer) {
        this.fileObservers.add(new WeakReference<FileProgressListener>(observer));
    }

    public void registerParseObserver(ParseProgressListener observer) {
        this.parseObservers.add(new WeakReference<ParseProgressListener>(observer));
    }

    public void setParseNotificationRate(int parseNotificationRate) {
        if (parseNotificationRate < 1) {
            throw new IllegalArgumentException("Parse Notification Rate must be at least 1");
        }
        this.parseNotificationRate = parseNotificationRate;
    }

    public void setReadNotificationRate(int readNotificationRate) {
        if (readNotificationRate < 1) {
            throw new IllegalArgumentException("Read Notification Rate must be at least 1");
        }
        this.readNotificationRate = readNotificationRate;
    }

    public void setStrictCustomTags(boolean strictCustomTags) {
        this.strictCustomTags = strictCustomTags;
    }

    public void setStrictLineBreaks(boolean strictLineBreaks) {
        this.strictLineBreaks = strictLineBreaks;
    }

    public void unregisterFileObserver(FileProgressListener observer) {
        int i = 0;
        while (i < this.fileObservers.size()) {
            WeakReference<FileProgressListener> observerRef = this.fileObservers.get(i);
            if (observerRef == null || observerRef.get() == observer) {
                this.fileObservers.remove(observerRef);
                continue;
            }
            ++i;
        }
        this.fileObservers.add(new WeakReference<FileProgressListener>(observer));
    }

    public void unregisterParseObserver(ParseProgressListener observer) {
        int i = 0;
        while (i < this.parseObservers.size()) {
            WeakReference<ParseProgressListener> observerRef = this.parseObservers.get(i);
            if (observerRef == null || observerRef.get() == observer) {
                this.parseObservers.remove(observerRef);
                continue;
            }
            ++i;
        }
        this.parseObservers.add(new WeakReference<ParseProgressListener>(observer));
    }

    int getLineNum() {
        return this.lineNum;
    }

    @Override
    void parse() {
    }

    private void loadRootItem(StringTree rootLevelItem) throws InfoTraderParserException {
        if (Tag.HEADER.equalsText(rootLevelItem.getTag())) {
            Header header = this.infoTrader.getHeader();
            if (header == null) {
                header = new Header();
                this.infoTrader.setHeader(header);
            }
            new HeaderParser(this, rootLevelItem, header).parse();
        } else if (Tag.NOTE.equalsText(rootLevelItem.getTag())) {
            ArrayList<Note> dummyList = new ArrayList<Note>();
            new NoteListParser(this, rootLevelItem, dummyList).parse();
            if (!dummyList.isEmpty()) {
                throw new InfoTraderParserException("At root level NOTE structures should have @ID@'s");
            }
        } else if (Tag.TRAILER.equalsText(rootLevelItem.getTag())) {
            this.infoTrader.setTrailer(new Trailer());
        } else if (Tag.SOURCE.equalsText(rootLevelItem.getTag())) {
            Source s = this.getSource(rootLevelItem.getId());
            new SourceParser(this, rootLevelItem, s).parse();
        } else if (Tag.OBJECT_MULTIMEDIA.equalsText(rootLevelItem.getTag())) {
            Multimedia multimedia = this.getMultimedia(rootLevelItem.getId());
            new MultimediaRecordParser(this, rootLevelItem, multimedia).parse();
        } else {
            this.unknownTag(rootLevelItem, this.infoTrader);
        }
    }

    private void notifyParseObservers(ParseProgressEvent e) {
        int i = 0;
        while (i < this.parseObservers.size()) {
            WeakReference<ParseProgressListener> observerRef = this.parseObservers.get(i);
            if (observerRef == null) {
                this.parseObservers.remove(observerRef);
                continue;
            }
            ParseProgressListener l = observerRef.get();
            if (l != null) {
                l.progressNotification(e);
            }
            ++i;
        }
    }

    private void parseAndLoadPreviousStringTree() throws InfoTraderParserException {
        StringTree tree = this.stringTreeBuilder.getTree();
        if (tree != null && tree.getLevel() == -1 && tree.getChildren() != null && tree.getChildren().size() == 1) {
            StringTree rootLevelItem = this.stringTreeBuilder.getTree().getChildren().get(0);
            if (rootLevelItem.getLevel() != 0) {
                throw new InfoTraderParserException("Expected a root level item in the buffer, but found " + rootLevelItem.getLevel() + " " + rootLevelItem.getTag() + " from line " + this.lineNum);
            }
            this.loadRootItem(rootLevelItem);
            this.stringTreeBuilder = new StringTreeBuilder(this);
        }
    }
}

