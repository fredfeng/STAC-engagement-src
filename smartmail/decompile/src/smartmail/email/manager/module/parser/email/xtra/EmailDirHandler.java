/*
 * Decompiled with CFR 0_121.
 */
package smartmail.email.manager.module.parser.email.xtra;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.parser.ContentHandler;
import org.apache.james.mime4j.parser.MimeStreamParser;
import org.apache.james.mime4j.stream.MimeConfig;
import smartmail.datamodel.EmailEvent;
import smartmail.email.manager.module.parser.MailContentHandler;

class EmailDirHandler {
    private File emaildir;
    File[] listFiles;
    int index = 0;

    EmailDirHandler() {
    }

    void close() {
    }

    void openOffline(String absolutePath, StringBuilder errbuf) {
        this.emaildir = new File(absolutePath);
        this.listFiles = this.emaildir.listFiles();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    boolean getNext(EmailEvent emailevent, StringBuilder errbuf) {
        boolean foundf = false;
        while (!foundf && this.index < this.listFiles.length) {
            if (this.listFiles[this.index].getName().endsWith(".mime")) {
                System.out.println("opening email:" + this.listFiles[this.index].getName());
                MailContentHandler handler = new MailContentHandler(true, emailevent);
                MimeConfig config = new MimeConfig();
                MimeStreamParser parser = new MimeStreamParser(config);
                parser.setContentHandler(handler);
                InputStream instream = null;
                try {
                    System.out.println("opening email:" + this.listFiles[this.index]);
                    instream = new FileInputStream(this.listFiles[this.index]);
                    parser.parse(instream);
                }
                catch (MimeException ex) {
                    Logger.getLogger(EmailDirHandler.class.getName()).log(Level.SEVERE, null, ex);
                    boolean bl = false;
                    return bl;
                }
                catch (IOException ex) {
                    Logger.getLogger(EmailDirHandler.class.getName()).log(Level.SEVERE, null, ex);
                    boolean bl = false;
                    return bl;
                }
                finally {
                    try {
                        instream.close();
                    }
                    catch (IOException ex) {
                        Logger.getLogger(EmailDirHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                foundf = true;
            }
            ++this.index;
        }
        return foundf;
    }
}

