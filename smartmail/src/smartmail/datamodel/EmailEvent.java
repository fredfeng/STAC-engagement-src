/*
 * Decompiled with CFR 0_121.
 */
package smartmail.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import smartmail.datamodel.EmailAddress;
import smartmail.datamodel.MailingList;

public class EmailEvent
implements Serializable {
    private Set<EmailAddress> othersource;
    private List<EmailAddress> otherdestination = new ArrayList<EmailAddress>();
    private EmailAddress source;
    private EmailAddress destination;
    private String protocol;
    private String type;
    private String subj;
    private String body;

    public Set<EmailAddress> getOthersource() {
        return this.othersource;
    }

    public void setOthersource(Set<EmailAddress> othersource) {
        this.othersource = othersource;
    }

    public List<EmailAddress> getOtherdestination() {
        return this.otherdestination;
    }

    public void setOtherdestination(List<EmailAddress> otherdestination) {
        this.otherdestination = otherdestination;
    }

    public void addOtherdestination(EmailAddress otherdestination) {
        if (otherdestination instanceof MailingList) {
            MailingList ml = (MailingList)otherdestination;
            Set<EmailAddress> members = ml.getMembers();
            this.otherdestination.addAll(members);
        } else {
            this.otherdestination.add(otherdestination);
        }
    }

    public EmailAddress getSource() {
        return this.source;
    }

    public void setSource(EmailAddress source) {
        this.source = source;
    }

    public EmailAddress getDestination() {
        return this.destination;
    }

    public void setDestination(EmailAddress destination) {
        this.destination = destination;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContent(String bodystr) {
        this.body = bodystr;
    }

    public String getContent() {
        return this.body;
    }

    public void setSubject(String subj) {
        this.subj = subj;
    }

    public String getSubject() {
        return this.subj;
    }
}

