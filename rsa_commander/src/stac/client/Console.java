/*
 * Decompiled with CFR 0_121.
 */
package stac.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import stac.client.Screen;
import stac.communications.Communications;
import stac.communications.PacketParserException;

public class Console
implements Runnable {
    private final Communications communications;
    private final Screen screen;
    private InputStream input;
    private PrintStream output;
    private PrintStream error;

    Console(Communications communications, Screen screen) {
        this.communications = communications;
        this.screen = screen;
        this.screen.registerRedrawHandler(this);
    }

    public InputStream getInput() {
        return this.input;
    }

    void setInput(InputStream input) {
        this.input = input;
    }

    public OutputStream getOutput() {
        return this.output;
    }

    void setOutput(PrintStream output) {
        this.output = output;
    }

    public OutputStream getError() {
        return this.error;
    }

    void setError(PrintStream error) {
        this.error = error;
    }

    @Override
    public void run() {
        try {
            boolean running = true;
            do {
                this.sendPrompt();
                String consoleLine = this.getUserInput();
                try {
                    running = this.handleUserInput(consoleLine);
                }
                catch (Exception e) {
                    if (e instanceof NullPointerException) {
                        throw new IOException(e);
                    }
                    this.output.println("That command failed. Type help to see options.");
                    e.printStackTrace(this.output);
                }
            } while (running);
        }
        catch (IOException e) {
            this.output.println("Client parse command failed");
            e.printStackTrace(this.output);
        }
        this.communications.stop();
    }

    private boolean handleUserInput(String consoleLine) throws IOException, PacketParserException {
        String[] split = consoleLine.split("\\s+");
        String command = split.length > 0 ? split[0] : "";
        String address = split.length > 1 ? split[1] : "";
        String port = split.length > 2 ? split[2] : "";
        switch (command) {
            case "help": {
                this.displayHelp();
                break;
            }
            case "send": {
                String[] message = this.screen.getMessage();
                this.communications.sendMessage(address, port, message[1], message[0]);
                break;
            }
            case "exit": {
                return false;
            }
            default: {
                this.screen.postCommandError(command);
            }
        }
        return true;
    }

    private String getUserInput() throws IOException {
        int c = 0;
        ByteArrayOutputStream build = new ByteArrayOutputStream();
        while ((c = this.input.read()) != System.lineSeparator().charAt(0) && c != -1) {
            build.write(c);
        }
        return build.toString();
    }

    private void sendPrompt() {
        this.output.print("client> ");
    }

    private void displayHelp() {
        this.output.println("help : display help.");
        this.output.println("send <dest address> <port> : send a message to the server/client.");
        this.output.println("exit : quit the client.");
    }

    public void redraw() {
        this.sendPrompt();
    }
}

