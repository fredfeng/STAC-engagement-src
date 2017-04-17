/*
 * Decompiled with CFR 0_121.
 */
package smartmail.email.manager.module.addressbook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import smartmail.datamodel.EmailAddress;
import smartmail.datamodel.MailingList;
import smartmail.datamodel.SecureEmailAddress;

public class AddressBook {
    static String[] securedaddresses;
    static String[] mlists;
    Map<String, EmailAddress> abook = new HashMap<String, EmailAddress>();
    Map<Integer, EmailAddress> abookbyID = new HashMap<Integer, EmailAddress>();
    public static int lastuniqueaddressid;
    static AddressBook theabook;

    public static AddressBook getAddressBook() throws IOException {
        if (theabook == null) {
            securedaddresses = AddressBook.loadList("addresses");
            mlists = AddressBook.loadList("mlists");
            theabook = new AddressBook();
        }
        return theabook;
    }

    private AddressBook() throws IOException {
        this.populateAddressBook();
    }

    public EmailAddress lookupExistingAddress(String val) {
        EmailAddress address = this.abook.get(val);
        if (address == null) {
            System.out.println("existing address null");
        }
        return address;
    }

    public EmailAddress lookupAddress(String val) {
        EmailAddress address = this.abook.get(val);
        if (address == null) {
            System.out.println("null");
            address = new EmailAddress();
            address.setValue(val);
        }
        return address;
    }

    boolean isSecretAddress(EmailAddress a) throws IOException {
        SecureEmailAddress sea = new SecureEmailAddress(lastuniqueaddressid);
        return SecureEmailAddress.isSecretAddress(a);
    }

    public void populateAddressBook() throws IOException {
        int i;
        for (i = 0; i < securedaddresses.length; ++i) {
            int indexOf = securedaddresses[i].indexOf(58);
            if (indexOf > 0) {
                String eadd1 = securedaddresses[i].substring(0, indexOf);
                String eadd2 = securedaddresses[i].substring(indexOf + 1, securedaddresses[i].length());
                EmailAddress emailAddress1 = this.makeEmail(lastuniqueaddressid, eadd1);
                EmailAddress emailAddress2 = this.makeEmail(lastuniqueaddressid, eadd2);
                this.abook.put(eadd1, emailAddress1);
                this.abook.put(eadd2, emailAddress2);
                this.abookbyID.put(lastuniqueaddressid, emailAddress1);
            } else {
                EmailAddress emailAddress1 = this.makeEmail(lastuniqueaddressid, securedaddresses[i]);
                this.abook.put(securedaddresses[i], emailAddress1);
                this.abookbyID.put(lastuniqueaddressid, emailAddress1);
            }
            ++lastuniqueaddressid;
        }
        for (i = 0; i < mlists.length; ++i) {
            String[] split = mlists[i].split(":");
            MailingList mailingList = new MailingList(lastuniqueaddressid);
            mailingList.setValue(split[0]);
            for (int j = 1; j < split.length; ++j) {
                EmailAddress existingAddress = this.lookupExistingAddress(split[j]);
                mailingList.addMember(existingAddress);
            }
            this.abook.put(split[0], mailingList);
            this.abookbyID.put(lastuniqueaddressid, mailingList);
            ++lastuniqueaddressid;
        }
    }

    public Set<EmailAddress> getAddresses(String pword) {
        Set<String> keySet = this.abook.keySet();
        Iterator<String> it = keySet.iterator();
        while (it.hasNext()) {
            it.next();
        }
        return null;
    }

    private EmailAddress makeEmail(int lastuniqueaddressid, String eadd1) throws IOException {
        EmailAddress email = null;
        email = SecureEmailAddress.isSecretAddress(eadd1) ? new SecureEmailAddress(lastuniqueaddressid) : new EmailAddress(lastuniqueaddressid);
        email.setValue(eadd1);
        return email;
    }

    public static String[] loadList(String fname) throws IOException {
        ArrayList<String> list;
        list = new ArrayList<String>();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("data/" + fname);
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        Throwable throwable = null;
        try {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        }
        catch (Throwable x2) {
            throwable = x2;
            throw x2;
        }
        finally {
            if (br != null) {
                if (throwable != null) {
                    try {
                        br.close();
                    }
                    catch (Throwable x2) {
                        throwable.addSuppressed(x2);
                    }
                } else {
                    br.close();
                }
            }
        }
        String[] lista = new String[list.size()];
        list.toArray(lista);
        return lista;
    }

    static {
        lastuniqueaddressid = 0;
        theabook = null;
    }
}

