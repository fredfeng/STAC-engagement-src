/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.io.encoding;

import java.util.Set;
import java.util.TreeSet;

public enum Encoding {
    ASCII("ASCII"),
    ANSEL("ANSEL"),
    UNICODE_BIG_ENDIAN("UNICODE"),
    UNICODE_LITTLE_ENDIAN("UNICODE"),
    UTF_8("UTF-8");
    
    private final String characterSetName;

    public static Set<String> getSupportedCharacterSetNames() {
        TreeSet<String> result = new TreeSet<String>();
        for (Encoding e : Encoding.values()) {
            result.add(e.characterSetName);
        }
        return result;
    }

    public static boolean isValidCharacterSetName(String characterSetName) {
        for (Encoding e : Encoding.values()) {
            if (!e.characterSetName.equals(characterSetName)) continue;
            return true;
        }
        return false;
    }

    private Encoding(String characterSetName) {
        this.characterSetName = characterSetName.intern();
    }

    public String getCharacterSetName() {
        return this.characterSetName.intern();
    }
}

