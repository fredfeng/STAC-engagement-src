/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex;

import com.networkapex.DES;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.binary.Base64;

public class DESHelper {
    public static String obtainEncryptedString(String message, String key) {
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedBytes = DES.encrypt(messageBytes, key);
        return Base64.encodeBase64String(encryptedBytes);
    }
}

