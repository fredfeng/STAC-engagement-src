/*
 * Decompiled with CFR 0_121.
 */
package smartmail.email.manager.module.parser.email.xtra;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import smartmail.datamodel.EmailEvent;
import smartmail.email.manager.module.parser.email.xtra.EmailDirHandler;
import smartmail.process.controller.module.seqfile.EmailParseException;

public class EmailParserUtil {
    private EmailDirHandler email;

    public static List<EmailEvent> parseemails(File filen) throws EmailParseException {
        ArrayList<EmailEvent> ces = new ArrayList<EmailEvent>();
        System.out.println("starting parser");
        EmailParserUtil emaill = new EmailParserUtil();
        emaill.init(filen);
        EmailEvent emailevent = null;
        int eventorder = 0;
        do {
            emailevent = emaill.getNextEmailEvent(1, ces, eventorder);
            ++eventorder;
        } while (emailevent != null);
        System.out.println("done parsing");
        emaill.close();
        return ces;
    }

    public EmailEvent getNextEmailEvent(int numloops, List<EmailEvent> ces, int eventorder) {
        EmailEvent emailevent = new EmailEvent();
        StringBuilder errbuf = new StringBuilder();
        if (this.email.getNext(emailevent, errbuf)) {
            ces.add(emailevent);
            return emailevent;
        }
        return null;
    }

    public void init(File file) throws EmailParseException {
        StringBuilder errbuf = new StringBuilder();
        this.email = new EmailDirHandler();
        this.email.openOffline(file.getAbsolutePath(), errbuf);
    }

    private void close() {
        this.email.close();
    }
}

