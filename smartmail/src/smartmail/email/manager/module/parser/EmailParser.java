/*
 * Decompiled with CFR 0_121.
 */
package smartmail.email.manager.module.parser;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.james.mime4j.dom.BinaryBody;
import org.apache.james.mime4j.dom.Body;
import org.apache.james.mime4j.dom.Entity;
import org.apache.james.mime4j.dom.Header;
import org.apache.james.mime4j.dom.Multipart;
import org.apache.james.mime4j.dom.TextBody;
import org.apache.james.mime4j.message.BodyPart;
import org.apache.james.mime4j.stream.Field;

public class EmailParser {
    private StringBuffer txtBody;
    private StringBuffer htmlBody;
    private ArrayList<BodyPart> attachments;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void parseMessage(String fileName) {
        FileInputStream fis = null;
        this.txtBody = new StringBuffer();
        this.htmlBody = new StringBuffer();
        this.attachments = new ArrayList();
        try {
            fis = new FileInputStream(fileName);
            Entity mimeMsg = null;
            Field priorityFld = mimeMsg.getHeader().getField("X-Priority");
            if (priorityFld != null) {
                System.out.println("Priority: " + priorityFld.getBody());
            }
            if (!mimeMsg.isMultipart()) {
                String text = this.getTxtPart(mimeMsg);
                this.txtBody.append(text);
            }
            for (BodyPart attach : this.attachments) {
                String attName = attach.getFilename();
                FileOutputStream fos = new FileOutputStream(attName);
                try {
                    BinaryBody bb = (BinaryBody)attach.getBody();
                    bb.writeTo(fos);
                }
                finally {
                    fos.close();
                }
            }
        }
        catch (IOException ex) {
            ex.fillInStackTrace();
        }
        finally {
            if (fis != null) {
                try {
                    fis.close();
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void parseBodyParts(Multipart multipart) throws IOException {
        for (Entity part : multipart.getBodyParts()) {
            if (!part.isMultipart()) continue;
            this.parseBodyParts((Multipart)part.getBody());
        }
    }

    private String getTxtPart(Entity part) throws IOException {
        TextBody tb = (TextBody)part.getBody();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        tb.writeTo(baos);
        return new String(baos.toByteArray());
    }
}

