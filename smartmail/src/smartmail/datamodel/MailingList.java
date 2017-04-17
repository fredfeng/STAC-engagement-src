/*
 * Decompiled with CFR 0_121.
 */
package smartmail.datamodel;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import smartmail.datamodel.EmailAddress;
import smartmail.datamodel.SecureEmailAddress;

public class MailingList
extends EmailAddress {
    private Set<EmailAddress> members = new TreeSet<EmailAddress>();

    public MailingList(int uid) {
        super(uid);
    }

    public Set<EmailAddress> getMembers() {
        return this.members;
    }

    public void setMember(Set<EmailAddress> members) {
        this.members = members;
    }

    public void addMember(EmailAddress member) {
        this.members.add(member);
    }

    public Set<EmailAddress> getPublicMembers() throws IOException {
        TreeSet<EmailAddress> pmembers = new TreeSet<EmailAddress>();
        for (EmailAddress next : this.members) {
            boolean secretAddress = SecureEmailAddress.isSecretAddress(next);
            if (secretAddress) continue;
            pmembers.add(next);
        }
        return pmembers;
    }
}

