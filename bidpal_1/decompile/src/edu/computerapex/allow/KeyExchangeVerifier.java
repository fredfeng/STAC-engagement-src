/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.allow;

import edu.computerapex.allow.KeyExchangeServer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.math.BigInteger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class KeyExchangeVerifier {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            KeyExchangeVerifier.mainGuide();
        }
        try {
            KeyExchangeServer keyExchangeServer = new KeyExchangeServer(args[0]);
            String serversPublicKeyString = KeyExchangeVerifier.obtainServerKey(args[1]);
            BigInteger serversPublicKey = serversPublicKeyString.startsWith("0x") ? new BigInteger(serversPublicKeyString.substring(2), 16) : new BigInteger(serversPublicKeyString);
            BigInteger masterSecret = keyExchangeServer.generateMasterSecret(serversPublicKey);
            System.out.println("Computed user's public key: ");
            System.out.println("\tpaste this in to the server when prompted.");
            System.out.println(keyExchangeServer.pullPublicKey());
            System.out.println("\nExpected response: ");
            System.out.println("\tmake sure this is the server's response.");
            System.out.println(masterSecret.toString(10));
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Error: keys must be hexadecimal or decimal numbers");
        }
    }

    private static void mainGuide() {
        throw new IllegalArgumentException("Must specify <user's private key> <server's public key file>");
    }

    private static String obtainServerKey(String serverKeyPath) throws IOException {
        File serverKeyFile = new File(serverKeyPath);
        FileInputStream inputStream = FileUtils.openInputStream(serverKeyFile);
        if (inputStream != null) {
            StringWriter stringWriter = new StringWriter();
            IOUtils.copy(inputStream, stringWriter, null);
            return stringWriter.toString();
        }
        return null;
    }
}

