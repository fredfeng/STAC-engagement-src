/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.writer;

import infotrader.parser.exception.InfoTraderWriterException;
import infotrader.parser.exception.InfoTraderWriterVersionDataMismatchException;
import infotrader.parser.exception.WriterCancelledException;
import infotrader.parser.io.event.FileProgressEvent;
import infotrader.parser.io.event.FileProgressListener;
import infotrader.parser.io.writer.InfoTraderFileWriter;
import infotrader.parser.model.CharacterSet;
import infotrader.parser.model.Corporation;
import infotrader.parser.model.Header;
import infotrader.parser.model.InfoTrader;
import infotrader.parser.model.InfoTraderVersion;
import infotrader.parser.model.Multimedia;
import infotrader.parser.model.Note;
import infotrader.parser.model.Repository;
import infotrader.parser.model.Source;
import infotrader.parser.model.SourceSystem;
import infotrader.parser.model.StringTree;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.model.Submission;
import infotrader.parser.model.Submitter;
import infotrader.parser.model.SupportedVersion;
import infotrader.parser.validate.InfoTraderValidationFinding;
import infotrader.parser.validate.InfoTraderValidator;
import infotrader.parser.validate.Severity;
import infotrader.parser.writer.AbstractEmitter;
import infotrader.parser.writer.HeaderEmitter;
import infotrader.parser.writer.Multimedia551Emitter;
import infotrader.parser.writer.Multimedia55Emitter;
import infotrader.parser.writer.NotesEmitter;
import infotrader.parser.writer.RepositoryEmitter;
import infotrader.parser.writer.SourceEmitter;
import infotrader.parser.writer.SubmissionEmitter;
import infotrader.parser.writer.SubmittersEmitter;
import infotrader.parser.writer.event.ConstructProgressEvent;
import infotrader.parser.writer.event.ConstructProgressListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class InfoTraderWriter
extends AbstractEmitter<InfoTrader> {
    List<String> lines = new ArrayList<String>();
    boolean validationSuppressed = true;
    private boolean autorepair = false;
    private boolean cancelled;
    private int constructionNotificationRate = 500;
    private final List<WeakReference<ConstructProgressListener>> constructObservers = new CopyOnWriteArrayList<WeakReference<ConstructProgressListener>>();
    private int fileNotificationRate = 500;
    private final List<WeakReference<FileProgressListener>> fileObservers = new CopyOnWriteArrayList<WeakReference<FileProgressListener>>();
    private int lastLineCountNotified = 0;
    private boolean useLittleEndianForUnicode = true;
    private List<InfoTraderValidationFinding> validationFindings;

    public InfoTraderWriter(InfoTrader gedcom) throws WriterCancelledException {
        super(null, 0, gedcom);
        this.baseWriter = this;
    }

    public void cancel() {
        this.cancelled = true;
    }

    public int getConstructionNotificationRate() {
        return this.constructionNotificationRate;
    }

    public int getFileNotificationRate() {
        return this.fileNotificationRate;
    }

    public List<InfoTraderValidationFinding> getValidationFindings() {
        return this.validationFindings;
    }

    public boolean isAutorepair() {
        return this.autorepair;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public boolean isUseLittleEndianForUnicode() {
        return this.useLittleEndianForUnicode;
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

    public void registerConstructObserver(ConstructProgressListener observer) {
        this.constructObservers.add(new WeakReference<ConstructProgressListener>(observer));
    }

    public void registerFileObserver(FileProgressListener observer) {
        this.fileObservers.add(new WeakReference<FileProgressListener>(observer));
    }

    public void setAutorepair(boolean autorepair) {
        this.autorepair = autorepair;
    }

    public void setConstructionNotificationRate(int constructionNotificationRate) {
        if (constructionNotificationRate < 1) {
            throw new IllegalArgumentException("Construction Notification Rate must be at least 1");
        }
        this.constructionNotificationRate = constructionNotificationRate;
    }

    public void setFileNotificationRate(int fileNotificationRate) {
        if (fileNotificationRate < 1) {
            throw new IllegalArgumentException("File Notification Rate must be at least 1");
        }
        this.fileNotificationRate = fileNotificationRate;
    }

    public void setUseLittleEndianForUnicode(boolean useLittleEndianForUnicode) {
        this.useLittleEndianForUnicode = useLittleEndianForUnicode;
    }

    public void unregisterConstructObserver(ConstructProgressListener observer) {
        int i = 0;
        while (i < this.constructObservers.size()) {
            WeakReference<ConstructProgressListener> observerRef = this.constructObservers.get(i);
            if (observerRef == null || observerRef.get() == observer) {
                this.constructObservers.remove(observerRef);
                continue;
            }
            ++i;
        }
        this.constructObservers.add(new WeakReference<ConstructProgressListener>(observer));
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

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void write(File file) throws IOException, InfoTraderWriterException {
        ((InfoTrader)this.writeFrom).getHeader().setFileName(new StringWithCustomTags(file.getName()));
        if (!(file.exists() || file.getCanonicalFile().getParentFile().exists() || file.getCanonicalFile().getParentFile().mkdirs() || file.createNewFile())) {
            throw new IOException("Unable to create file " + file.getName());
        }
        FileOutputStream o = new FileOutputStream(file);
        try {
            this.write(o);
            o.flush();
        }
        finally {
            o.close();
        }
    }

    public void write(OutputStream out) throws InfoTraderWriterException {
        this.emit();
        try {
            InfoTraderFileWriter gfw = new InfoTraderFileWriter(this, this.lines);
            gfw.setUseLittleEndianForUnicode(this.useLittleEndianForUnicode);
            gfw.write(out);
        }
        catch (IOException e) {
            throw new InfoTraderWriterException("Unable to write file", e);
        }
    }

    public void write(String filename) throws IOException, InfoTraderWriterException {
        File f = new File(filename);
        this.write(f);
    }

    @Override
    protected void emit() throws InfoTraderWriterException {
        if (!this.validationSuppressed) {
            InfoTraderValidator gv = new InfoTraderValidator((InfoTrader)this.writeFrom);
            gv.setAutorepairEnabled(this.autorepair);
            gv.validate();
            this.validationFindings = gv.getFindings();
            int numErrorFindings = 0;
            for (InfoTraderValidationFinding f : this.validationFindings) {
                if (f.getSeverity() != Severity.ERROR) continue;
                ++numErrorFindings;
            }
            if (numErrorFindings > 0) {
                throw new InfoTraderWriterException("Cannot write file - " + numErrorFindings + " error(s) found during validation.  Review the validation findings to determine root cause.");
            }
        }
        this.checkVersionCompatibility();
        new HeaderEmitter(this.baseWriter, 0, ((InfoTrader)this.writeFrom).getHeader()).emit();
        new SubmissionEmitter(this.baseWriter, 0, ((InfoTrader)this.writeFrom).getSubmission()).emit();
        if (this.g55()) {
            new Multimedia55Emitter(this.baseWriter, 0, ((InfoTrader)this.writeFrom).getMultimedia().values()).emit();
        } else {
            new Multimedia551Emitter(this.baseWriter, 0, ((InfoTrader)this.writeFrom).getMultimedia().values()).emit();
        }
        new NotesEmitter(this.baseWriter, 0, ((InfoTrader)this.writeFrom).getNotes().values()).emit();
        new RepositoryEmitter(this.baseWriter, 0, ((InfoTrader)this.writeFrom).getRepositories().values()).emit();
        new SourceEmitter(this.baseWriter, 0, ((InfoTrader)this.writeFrom).getSources().values()).emit();
        new SubmittersEmitter(this, 0, ((InfoTrader)this.writeFrom).getSubmitters().values()).emit();
        this.emitTrailer();
        this.emitCustomTags(1, ((InfoTrader)this.writeFrom).getCustomTags());
    }

    @Override
    void emitCustomTags(int level, List<StringTree> customTags) {
        if (customTags != null) {
            for (StringTree st : customTags) {
                StringBuilder line = new StringBuilder(Integer.toString(level));
                line.append(" ");
                if (st.getId() != null && st.getId().trim().length() > 0) {
                    line.append(st.getId()).append(" ");
                }
                line.append(st.getTag());
                if (st.getValue() != null && st.getValue().trim().length() > 0) {
                    line.append(" ").append(st.getValue());
                }
                this.baseWriter.lines.add(line.toString());
                this.emitCustomTags(level + 1, st.getChildren());
            }
        }
    }

    void notifyConstructObserversIfNeeded() {
        if (this.lines.size() - this.lastLineCountNotified > this.constructionNotificationRate) {
            this.notifyConstructObservers(new ConstructProgressEvent(this, this.lines.size(), true));
        }
    }

    private void checkVersionCompatibility() throws InfoTraderWriterException {
        if (((InfoTrader)this.writeFrom).getHeader().getInfoTraderVersion() == null) {
            ((InfoTrader)this.writeFrom).getHeader().setInfoTraderVersion(new InfoTraderVersion());
        }
        if (SupportedVersion.V5_5.equals((Object)((InfoTrader)this.writeFrom).getHeader().getInfoTraderVersion().getVersionNumber())) {
            this.checkVersionCompatibility55();
        } else {
            this.checkVersionCompatibility551();
        }
    }

    private void checkVersionCompatibility55() throws InfoTraderWriterVersionDataMismatchException {
        if (((InfoTrader)this.writeFrom).getHeader().getCopyrightData() != null && ((InfoTrader)this.writeFrom).getHeader().getCopyrightData().size() > 1) {
            throw new InfoTraderWriterVersionDataMismatchException("InfoTrader version is 5.5, but has multi-line copyright data in header");
        }
        if (((InfoTrader)this.writeFrom).getHeader().getCharacterSet() != null && ((InfoTrader)this.writeFrom).getHeader().getCharacterSet().getCharacterSetName() != null && "UTF-8".equals(((InfoTrader)this.writeFrom).getHeader().getCharacterSet().getCharacterSetName().getValue())) {
            throw new InfoTraderWriterVersionDataMismatchException("InfoTrader version is 5.5, but data is encoded using UTF-8");
        }
        if (((InfoTrader)this.writeFrom).getHeader().getSourceSystem() != null && ((InfoTrader)this.writeFrom).getHeader().getSourceSystem().getCorporation() != null) {
            Corporation c = ((InfoTrader)this.writeFrom).getHeader().getSourceSystem().getCorporation();
            if (c.getWwwUrls() != null && !c.getWwwUrls().isEmpty()) {
                throw new InfoTraderWriterVersionDataMismatchException("InfoTrader version is 5.5, but source system corporation has www urls");
            }
            if (c.getFaxNumbers() != null && !c.getFaxNumbers().isEmpty()) {
                throw new InfoTraderWriterVersionDataMismatchException("InfoTrader version is 5.5, but source system corporation has fax numbers");
            }
            if (c.getEmails() != null && !c.getEmails().isEmpty()) {
                throw new InfoTraderWriterVersionDataMismatchException("InfoTrader version is 5.5, but source system corporation has emails");
            }
        }
    }

    private void checkVersionCompatibility551() throws InfoTraderWriterVersionDataMismatchException {
        for (Multimedia m : ((InfoTrader)this.writeFrom).getMultimedia().values()) {
            if (m.getBlob() == null || m.getBlob().isEmpty()) continue;
            throw new InfoTraderWriterVersionDataMismatchException("InfoTrader version is 5.5.1, but multimedia item " + m.getXref() + " contains BLOB data which is unsupported in 5.5.1");
        }
    }

    private void emitTrailer() {
        this.lines.add("0 TRLR");
        this.notifyConstructObservers(new ConstructProgressEvent(this, this.lines.size(), true));
    }

    private void notifyConstructObservers(ConstructProgressEvent e) {
        int i = 0;
        this.lastLineCountNotified = e.getLinesProcessed();
        while (i < this.constructObservers.size()) {
            WeakReference<ConstructProgressListener> observerRef = this.constructObservers.get(i);
            if (observerRef == null) {
                this.constructObservers.remove(observerRef);
                continue;
            }
            ConstructProgressListener l = observerRef.get();
            if (l != null) {
                l.progressNotification(e);
            }
            ++i;
        }
    }
}

