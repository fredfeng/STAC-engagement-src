/*
 * Decompiled with CFR 0_121.
 */
package smartmail.email.manager.module.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.codec.DecoderUtil;
import org.apache.james.mime4j.dom.address.Address;
import org.apache.james.mime4j.dom.address.AddressList;
import org.apache.james.mime4j.dom.address.Mailbox;
import org.apache.james.mime4j.dom.address.MailboxList;
import org.apache.james.mime4j.dom.field.AddressListField;
import org.apache.james.mime4j.dom.field.DateTimeField;
import org.apache.james.mime4j.dom.field.MailboxListField;
import org.apache.james.mime4j.dom.field.ParsedField;
import org.apache.james.mime4j.dom.field.UnstructuredField;
import org.apache.james.mime4j.field.LenientFieldParser;
import org.apache.james.mime4j.parser.ContentHandler;
import org.apache.james.mime4j.stream.BodyDescriptor;
import org.apache.james.mime4j.stream.Field;
import org.apache.james.mime4j.util.ByteSequence;
import smartmail.datamodel.EmailAddress;
import smartmail.datamodel.EmailEvent;

public class MailContentHandler
implements ContentHandler {
    private boolean strictParsing = false;
    HashMap<String, String> mdata;
    private boolean inPart = false;
    EmailEvent email;
    static int inc = 0;

    public MailContentHandler(boolean strictParsing, EmailEvent email) {
        this.strictParsing = strictParsing;
        this.email = email;
        this.mdata = new HashMap();
    }

    @Override
    public void body(BodyDescriptor body, InputStream is) throws MimeException, IOException {
        int rsz;
        this.mdata.put("CONTENT_TYPE", body.getMimeType());
        this.mdata.put("CONTENT_ENCODING", body.getCharset());
        int bufferSize = 1024;
        char[] buffer = new char[1024];
        StringBuilder out = new StringBuilder();
        InputStreamReader in = new InputStreamReader(is, "UTF-8");
        while ((rsz = in.read(buffer, 0, buffer.length)) >= 0) {
            out.append(buffer, 0, rsz);
        }
        String bodystr = out.toString();
        System.out.println("bodystr:" + bodystr);
        this.email.setContent(bodystr);
    }

    @Override
    public void endBodyPart() throws MimeException {
    }

    @Override
    public void endHeader() throws MimeException {
    }

    @Override
    public void startMessage() throws MimeException {
    }

    @Override
    public void endMessage() throws MimeException {
    }

    @Override
    public void endMultipart() throws MimeException {
        this.inPart = false;
    }

    @Override
    public void epilogue(InputStream is) throws MimeException, IOException {
    }

    @Override
    public void field(Field field) throws MimeException {
        if (this.inPart) {
            return;
        }
        try {
            String fieldname = field.getName();
            ParsedField parsedField = LenientFieldParser.getParser().parse(field, DecodeMonitor.SILENT);
            if (fieldname.equalsIgnoreCase("From")) {
                MailboxListField fromField = (MailboxListField)parsedField;
                MailboxList mailboxList = fromField.getMailboxList();
                if (fromField.isValidField() && mailboxList != null) {
                    for (Address address : mailboxList) {
                        String from = this.getDisplayString(address);
                        this.mdata.put("MESSAGE_FROM", from);
                        EmailAddress efrom = new EmailAddress(0);
                        efrom.setValue(address.toString());
                        efrom.setProtocol(fieldname);
                        this.email.setSource(efrom);
                    }
                } else {
                    String from = this.stripOutFieldPrefix(field, "From:");
                    if (from.startsWith("<")) {
                        from = from.substring(1);
                    }
                    if (from.endsWith(">")) {
                        from = from.substring(0, from.length() - 1);
                    }
                    this.mdata.put("MESSAGE_FROM", from);
                    EmailAddress efrom = new EmailAddress(0);
                    efrom.setValue(from);
                    efrom.setProtocol(fieldname);
                    this.email.setSource(efrom);
                }
            } else if (fieldname.equalsIgnoreCase("Subject")) {
                this.email.setSubject(((UnstructuredField)parsedField).getValue());
                this.mdata.put("TRANSITION_SUBJECT_TO_DC_TITLE", ((UnstructuredField)parsedField).getValue());
            } else if (fieldname.equalsIgnoreCase("To")) {
                this.processAddressList(parsedField, "To:", "MESSAGE_TO");
            } else if (fieldname.equalsIgnoreCase("CC")) {
                this.processAddressList(parsedField, "Cc:", "MESSAGE_CC");
            } else if (fieldname.equalsIgnoreCase("BCC")) {
                this.processAddressList(parsedField, "Bcc:", "MESSAGE_BCC");
            } else if (fieldname.equalsIgnoreCase("Date")) {
                DateTimeField dateField = (DateTimeField)parsedField;
                this.mdata.put("CREATED", dateField.getDate().toString());
            }
        }
        catch (RuntimeException me) {
            if (this.strictParsing) {
                throw me;
            }
        }
        catch (IOException ex) {
            Logger.getLogger(MailContentHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void processAddressList(ParsedField field, String addressListType, String metadataField) throws MimeException, IOException {
        AddressListField toField = (AddressListField)field;
        if (toField.isValidField()) {
            AddressList addressList = toField.getAddressList();
            for (Address address : addressList) {
                Mailbox m = (Mailbox)address;
                this.mdata.put(metadataField, this.getDisplayString(address));
                EmailAddress efrom = new EmailAddress(0);
                efrom.setValue(m.toString());
                EmailAddress e = EmailAddress.checkAddress(efrom);
                this.email.addOtherdestination(e);
            }
        } else {
            String to = this.stripOutFieldPrefix(field, addressListType);
            for (String eachTo : to.split(",")) {
                this.mdata.put(metadataField, eachTo.trim());
                EmailAddress efrom = new EmailAddress(0);
                efrom.setValue(eachTo.trim());
                efrom.setProtocol(addressListType);
                this.email.addOtherdestination(efrom);
            }
        }
    }

    private String getDisplayString(Address address) {
        if (address instanceof Mailbox) {
            Mailbox mailbox = (Mailbox)address;
            String name = mailbox.getName();
            if (name != null && name.length() > 0) {
                name = DecoderUtil.decodeEncodedWords(name, DecodeMonitor.SILENT);
                return name + " ";
            }
            return mailbox.getAddress();
        }
        return address.toString();
    }

    @Override
    public void preamble(InputStream is) throws MimeException, IOException {
    }

    @Override
    public void raw(InputStream is) throws MimeException, IOException {
        System.out.println(is.available());
    }

    @Override
    public void startBodyPart() throws MimeException {
    }

    @Override
    public void startHeader() throws MimeException {
    }

    @Override
    public void startMultipart(BodyDescriptor descr) throws MimeException {
        this.inPart = true;
        System.out.println("startMultipart:" + descr.getContentLength());
    }

    private String stripOutFieldPrefix(Field field, String fieldname) {
        String temp = field.getRaw().toString();
        int loc = fieldname.length();
        while (temp.charAt(loc) == ' ') {
            ++loc;
        }
        return temp.substring(loc);
    }
}

