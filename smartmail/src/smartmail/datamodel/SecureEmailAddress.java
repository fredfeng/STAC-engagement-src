/*
 * Decompiled with CFR 0_121.
 */
package smartmail.datamodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import smartmail.datamodel.EmailAddress;
import smartmail.email.manager.module.addressbook.AddressBook;

public class SecureEmailAddress
extends EmailAddress {
    static String[] securedaddresses;
    static List<String> securedl;

    public SecureEmailAddress(int lastuniqueaddressid) throws IOException {
        super(lastuniqueaddressid);
        if (securedl == null) {
            securedaddresses = AddressBook.loadList("securedaddresses");
            securedl = new ArrayList<String>();
            for (int i = 0; i < securedaddresses.length; ++i) {
                securedl.add(securedaddresses[i]);
            }
        }
    }

    public static boolean isSecretAddress(EmailAddress a) throws IOException {
        if (securedl == null) {
            securedaddresses = AddressBook.loadList("securedaddresses");
            securedl = new ArrayList<String>();
            for (int i = 0; i < securedaddresses.length; ++i) {
                securedl.add(securedaddresses[i]);
            }
        }
        boolean contains = securedl.contains(a.getValue());
        return contains;
    }

    public static boolean isSecretAddress(String add) throws IOException {
        if (securedl == null) {
            securedaddresses = AddressBook.loadList("securedaddresses");
            securedl = new ArrayList<String>();
            for (int i = 0; i < securedaddresses.length; ++i) {
                securedl.add(securedaddresses[i]);
            }
        }
        boolean contains = securedl.contains(add);
        return contains;
    }
}

