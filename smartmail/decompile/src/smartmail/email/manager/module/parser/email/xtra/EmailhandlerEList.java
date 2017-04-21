/*
 * Decompiled with CFR 0_121.
 */
package smartmail.email.manager.module.parser.email.xtra;

import java.util.List;
import java.util.Set;
import smartmail.datamodel.EmailAddress;
import smartmail.datamodel.EmailEvent;

public class EmailhandlerEList {
    public String lastPacket;
    EmailEvent lastEvent;
    int lastEventSt = 1;

    public int lastEventStatus() {
        int ret = this.lastEventSt;
        this.lastEventSt = 1;
        return ret;
    }

    public EmailEvent lastEvent() {
        EmailEvent ret = this.lastEvent;
        this.lastEvent = null;
        return ret;
    }

    public String toString() {
        String ret = this.lastPacket;
        this.lastPacket = null;
        return ret;
    }

    void setAddrs(String prot, String src, String dest, EmailEvent evt) {
        EmailAddress srcaddr = new EmailAddress();
        srcaddr.setProtocol(prot);
        srcaddr.setValue(src);
        Set<EmailAddress> othersource = evt.getOthersource();
        othersource.add(srcaddr);
        EmailAddress destaddr = new EmailAddress();
        destaddr.setProtocol(prot);
        destaddr.setValue(dest);
        List<EmailAddress> otherdest = evt.getOtherdestination();
        otherdest.add(destaddr);
    }
}

