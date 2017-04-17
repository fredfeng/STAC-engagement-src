/*
 * Decompiled with CFR 0_121.
 */
package smartmail.logging.module;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import smartmail.datamodel.EmailAddress;
import smartmail.datamodel.EmailEvent;
import smartmail.logging.module.crypto.CryptoException;
import smartmail.logging.module.crypto.CryptoUtils;
import smartmail.process.controller.module.PipelineController;

class LogWriter {
    LogWriter() {
    }

    public static void writelogandplaceinmailbox(String logm, EmailAddress lookupExistingAddress, Iterator<EmailEvent> it, int cnt, boolean sec) {
        try {
            String strencrypt = CryptoUtils.strencrypt("thekeyisbigenoug", logm);
            CryptoUtils.write(strencrypt, cnt, sec);
            while (it.hasNext()) {
                EmailEvent next1 = it.next();
                if (lookupExistingAddress == null) continue;
                lookupExistingAddress.addToMailBox(next1);
            }
        }
        catch (CryptoException ex) {
            Logger.getLogger(PipelineController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

