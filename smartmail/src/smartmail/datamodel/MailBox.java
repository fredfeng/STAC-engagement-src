/*
 * Decompiled with CFR 0_121.
 */
package smartmail.datamodel;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Vector;
import smartmail.datamodel.EmailEvent;

class MailBox
implements Serializable {
    Vector<EmailEvent> mbox = new Vector();

    public void add(EmailEvent e) {
        this.mbox.add(e);
    }

    public EmailEvent get() {
        EmailEvent e = null;
        try {
            e = this.mbox.firstElement();
            if (e != null) {
                this.mbox.remove(e);
            }
        }
        catch (NoSuchElementException noSuchElementException) {
            // empty catch block
        }
        return e;
    }
}

