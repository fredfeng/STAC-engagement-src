/*
 * Decompiled with CFR 0_121.
 */
package stac.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import stac.client.Console;
import stac.communications.Communications;
import stac.communications.packets.RequestPacket;
import stac.crypto.Key;
import stac.crypto.PrivateKey;
import stac.crypto.PublicKey;
import stac.util.Hex;

public class Screen {
    private final String TERMINATOR = System.lineSeparator() + "EOM" + System.lineSeparator();
    private PrintStream output;
    private PrintStream error;
    private InputStream input;
    private Console redrawHandler;

    void setOutput(PrintStream output) {
        this.output = output;
    }

    void setError(PrintStream error) {
        this.error = error;
    }

    void setInput(InputStream input) {
        this.input = input;
    }

    public String postMessage(RequestPacket packet) throws IOException {
        this.printMessage(packet);
        this.redrawHandler.redraw();
        return null;
    }

    public String[] getMessage() throws IOException {
        int read;
        int read2;
        this.output.print("Enter the name of the receiver (max 62 bytes): ");
        ByteArrayOutputStream nameStream = new ByteArrayOutputStream();
        while ((read2 = this.input.read()) != System.lineSeparator().charAt(0) && read2 != -1 && nameStream.size() <= 62) {
            nameStream.write(read2);
        }
        this.output.println("Enter a message (max 4096 bytes) followed by EOM on a blank line:");
        ByteArrayOutputStream messageStream = new ByteArrayOutputStream();
        while ((read = this.input.read()) != -1) {
            messageStream.write(read);
            if (messageStream.toString().length() < 5 || !this.TERMINATOR.equals(messageStream.toString().substring(messageStream.toString().length() - 5))) continue;
            break;
        }
        return new String[]{nameStream.toString(), messageStream.toString().substring(0, messageStream.toString().length() - 4)};
    }

    private void printMessage(RequestPacket packet) {
        StringBuilder b = new StringBuilder();
        this.messageFromHeader(packet, b);
        this.messageToHeader(packet, b);
        this.messageBody(packet, b);
        this.output.println(b.toString());
    }

    private void messageBody(RequestPacket packet, StringBuilder b) {
        b.append("===== Begin Message =====").append(System.lineSeparator());
        b.append(packet.getMessage()).append(System.lineSeparator());
        b.append("=====  End Message  =====").append(System.lineSeparator());
    }

    private void messageToHeader(RequestPacket packet, StringBuilder b) {
        PublicKey receiverKey = packet.getCommunications().getListenerKey().toPublicKey();
        byte[] fingerPrint = receiverKey != null ? receiverKey.getFingerPrint() : " < null > ".getBytes();
        b.append("Message is for [");
        b.append(packet.getReceiverName());
        b.append(":");
        b.append(Hex.bytesToHex(fingerPrint, 0, 8));
        b.append("]");
        b.append(System.lineSeparator());
    }

    private void messageFromHeader(RequestPacket packet, StringBuilder b) {
        Key senderKey = packet.getSenderKey();
        byte[] fingerPrint = senderKey != null ? senderKey.getFingerPrint() : " < null > ".getBytes();
        b.append("Message From [");
        b.append(packet.getSenderName());
        b.append(":");
        b.append(Hex.bytesToHex(fingerPrint, 0, 8));
        b.append("]");
        b.append(System.lineSeparator());
    }

    void postCommandError(String command) {
        if (command != null && command.length() > 0) {
            this.output.println(command + ": no such command. For help try 'help'");
        }
    }

    public void registerRedrawHandler(Console handler) {
        this.redrawHandler = handler;
    }

    public void postError(String s) {
        this.postError(s, true);
    }

    public void postError(String s, boolean b) {
        this.error.println(s);
        if (b) {
            this.redrawHandler.redraw();
        }
    }

    public void postOutput(String s) {
        this.postOutput(s, true);
    }

    public void postOutput(String s, boolean b) {
        this.output.println(s);
        if (b) {
            this.redrawHandler.redraw();
        }
    }
}

