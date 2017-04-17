/*
 * Decompiled with CFR 0_121.
 */
package smartmail.logging.module;

import com.google.common.collect.Multimap;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hadoop.io.Text;
import smartmail.datamodel.EmailAddress;
import smartmail.datamodel.EmailEvent;
import smartmail.email.manager.module.addressbook.AddressBook;
import smartmail.logging.module.LogWriter;

public class LogGenerator {
    public static void checkValuesandOutput(Multimap output, List<EmailEvent> es) {
        TreeSet keys = new TreeSet(output.keySet());
        Iterator keyit = keys.iterator();
        while (keyit.hasNext()) {
            try {
                Text next = (Text)keyit.next();
                Collection get = output.get(next);
                Iterator iterator = get.iterator();
                String redval = (String)iterator.next();
                String[] split = redval.split(":");
                int cnt = Integer.valueOf(split[0]);
                boolean sec = Boolean.valueOf(split[1]);
                String logm = next.toString() + ":" + cnt;
                if (logm.length() < 100) {
                    int diff = 100 - logm.length();
                    SecureRandom random = new SecureRandom();
                    String pad = new BigInteger(diff, random).toString(32);
                    logm = logm + ":" + pad;
                }
                Iterator<EmailEvent> it = es.iterator();
                EmailAddress lookupExistingAddress = AddressBook.getAddressBook().lookupExistingAddress(next.toString());
                LogWriter.writelogandplaceinmailbox(logm, lookupExistingAddress, it, cnt, sec);
            }
            catch (IOException ex) {
                Logger.getLogger(LogGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

