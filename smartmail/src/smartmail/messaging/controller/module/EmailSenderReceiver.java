/*
 * Decompiled with CFR 0_121.
 */
package smartmail.messaging.controller.module;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import smartmail.datamodel.EmailAddress;
import smartmail.datamodel.EmailEvent;
import smartmail.datamodel.MailingList;
import smartmail.email.manager.module.addressbook.AddressBook;
import smartmail.messaging.controller.module.RunSmartMail;
import smartmail.process.controller.module.PipelineController;
import smartmail.process.controller.module.seqfile.EmailParseException;

public class EmailSenderReceiver
extends HttpServlet {
    private final AddressBook abook = AddressBook.getAddressBook();

    public EmailSenderReceiver() throws IOException {
    }

    @Override
    protected synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path;
        StringBuffer requestURL = request.getRequestURL();
        URL url = new URL(requestURL.toString());
        System.out.println("path:" + url.getPath());
        switch (path = url.getPath()) {
            case "/email.cgi": {
                this.doEmail(request, response);
                break;
            }
            case "/mbox.cgi": {
                this.getMBox(request, response);
                break;
            }
            case "/address.cgi": {
                this.getAddress(request, response);
                break;
            }
            default: {
                throw new IllegalArgumentException("ERROR:Unknown request type");
            }
        }
    }

    private void getMBox(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mbox = request.getParameter("mbox");
        if (mbox.length() > 25 + "@smartmail.com".length() || !mbox.endsWith("@smartmail.com")) {
            mbox = mbox.toLowerCase();
            response.getWriter().println("ERROR: Invalid input");
            return;
        }
        EmailAddress mboxaddress = AddressBook.getAddressBook().lookupExistingAddress(mbox);
        EmailEvent fromMailBox = mboxaddress.getFromMailBox();
        if (fromMailBox == null) {
            response.getWriter().println("Sorry, Your MailBox is empty.");
        } else {
            EmailAddress source = fromMailBox.getSource();
            response.getWriter().println("From:" + source.getValue());
            response.getWriter().println("Subject:" + fromMailBox.getSubject());
            response.getWriter().println("Message:" + fromMailBox.getContent());
        }
    }

    private void getAddress(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String list = request.getParameter("list");
        System.out.println("list:" + list);
        if (list.length() > 25 + "@smartmail.com".length() || !list.endsWith("@smartmail.com")) {
            list = list.toLowerCase();
            response.getWriter().println("ERROR: Invalid input");
            return;
        }
        EmailAddress lookupAddress = this.abook.lookupAddress(list);
        StringBuilder addresses = new StringBuilder();
        if (lookupAddress instanceof MailingList) {
            MailingList ml = (MailingList)lookupAddress;
            Set<EmailAddress> members = ml.getPublicMembers();
            Iterator<EmailAddress> it = members.iterator();
            while (it.hasNext()) {
                addresses.append(it.next().getValue() + ";");
            }
        }
        response.getWriter().println(addresses);
    }

    private void doEmail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String from = request.getParameter("from");
        System.out.println(from);
        from = from.toLowerCase();
        if (from.length() > 25 + "@smartmail.com".length() || !from.endsWith("@smartmail.com")) {
            response.getWriter().println("ERROR: Invalid input");
            return;
        }
        String to = request.getParameter("to");
        if (to.length() > (25 + "@smartmail.com".length() + 1) * 10) {
            response.getWriter().println("ERROR: Invalid input, too long:" + to.length());
            return;
        }
        String[] tos = to.split(";");
        List<String> toList = Arrays.asList(tos);
        ArrayList<String> toListLower = new ArrayList<String>();
        for (int i = 0; i < toList.size(); ++i) {
            String toitem = toList.get(i);
            if (toitem.length() > 25 + "@smartmail.com".length() || !toitem.endsWith("@smartmail.com")) {
                response.getWriter().println("ERROR: Invalid input");
                return;
            }
            toitem = toitem.toLowerCase();
            toListLower.add(toitem);
        }
        String subj = request.getParameter("subj");
        if (subj.length() > 125) {
            response.getWriter().println("ERROR: Invalid input");
            return;
        }
        String content = request.getParameter("content");
        if (content.length() > 250) {
            response.getWriter().println("ERROR: Invalid input");
            return;
        }
        long emailtime = System.currentTimeMillis();
        String makeEmail = this.makeEmail(from, emailtime, toListLower, subj, content);
        String timeString = Long.toString(emailtime);
        RunSmartMail.clean("./mail");
        File email = new File("mail/" + timeString + ".mime");
        PrintWriter out = new PrintWriter(email);
        out.print(makeEmail);
        out.close();
        try {
            PipelineController.main(null);
        }
        catch (EmailParseException ex) {
            Logger.getLogger(EmailSenderReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (email.exists()) {
            email.delete();
        }
        response.getWriter().println("OK");
    }

    public String makeEmail(String from, long time, List<String> tofields, String subj, String content) {
        StringBuilder mimemessage = new StringBuilder();
        mimemessage.append("MIME-Version: 1.0\n");
        Date date = new Date(time);
        mimemessage.append("From: NAME <" + from + ">\n");
        for (String t : tofields) {
            mimemessage.append("To: NAME <" + t + ">\n");
        }
        mimemessage.append("Subject:" + subj + "\n");
        mimemessage.append("Content-Type: text/plain\n\n");
        mimemessage.append(content + "\n");
        return mimemessage.toString();
    }
}

