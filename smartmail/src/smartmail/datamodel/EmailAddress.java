/*
 * Decompiled with CFR 0_121.
 */
package smartmail.datamodel;

import java.io.IOException;
import smartmail.datamodel.EmailEvent;
import smartmail.datamodel.MailBox;
import smartmail.datamodel.MessageWord;
import smartmail.email.manager.module.addressbook.AddressBook;

public class EmailAddress
extends MessageWord
implements Comparable<EmailAddress> {
    private Integer id;
    private String protocol;
    static AddressBook abook;
    int lastuniqueaddressid;
    MailBox mbox;

    public static EmailAddress checkAddress(EmailAddress a) throws IOException {
        abook = AddressBook.getAddressBook();
        EmailAddress lookupAddress = abook.lookupAddress(a.getValue());
        return lookupAddress;
    }

    public EmailAddress(int lastuniqueaddressid) {
        this.lastuniqueaddressid = lastuniqueaddressid;
        this.mbox = new MailBox();
    }

    public EmailAddress() {
        this.lastuniqueaddressid = 0;
    }

    public String getUniqueValue() {
        return this.getValue() + ":" + this.lastuniqueaddressid;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getPK() {
        return this.id;
    }

    @Override
    public int compareTo(EmailAddress a) {
        EmailAddress ae = a;
        int v = this.getUniqueValue().compareTo(ae.getUniqueValue());
        return v;
    }

    public boolean addToMailBox(EmailEvent e) {
        if (this.mbox != null) {
            this.mbox.add(e);
            return true;
        }
        return false;
    }

    public EmailEvent getFromMailBox() {
        if (this.mbox != null) {
            EmailEvent e = this.mbox.get();
            return e;
        }
        return null;
    }

    @Override
    public void setValue(String value) {
        int indexOfAt;
        String mbox = (value = value.toLowerCase()).substring(0, indexOfAt = value.indexOf(64));
        boolean hasNonAlpha = mbox.matches("^.*[^a-z].*$");
        if (!hasNonAlpha) {
            super.setValue(value);
        }
    }
}

