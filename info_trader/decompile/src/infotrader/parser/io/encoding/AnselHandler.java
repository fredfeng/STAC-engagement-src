/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.io.encoding;

import infotrader.parser.io.encoding.AnselMapping;

public class AnselHandler {
    private static final int ANSEL_EXTENDED_BEGIN_AT = 160;
    private static final char ANSEL_DIACRITICS_BEGIN_AT = '\u00e0';

    public String toAnsel(String utf16) {
        char[] ansel = new char[512];
        int anselIdx = 0;
        for (int i = 0; i < utf16.length(); ++i) {
            char c = utf16.charAt(i);
            char oneCharAhead = '\u0000';
            char twoCharAhead = '\u0000';
            if (i + 1 < utf16.length()) {
                oneCharAhead = utf16.charAt(i + 1);
                if (AnselMapping.isUnicodeCombiningDiacritic(oneCharAhead)) {
                    if (i + 2 < utf16.length() && !AnselMapping.isUnicodeCombiningDiacritic(twoCharAhead = utf16.charAt(i + 2))) {
                        twoCharAhead = '\u0000';
                    }
                } else {
                    oneCharAhead = '\u0000';
                }
            }
            if (twoCharAhead != '\u0000') {
                ansel[anselIdx++] = AnselMapping.encode(oneCharAhead);
                ansel[anselIdx++] = AnselMapping.encode(twoCharAhead);
                ansel[anselIdx++] = c;
                i += 2;
                continue;
            }
            if (oneCharAhead != '\u0000') {
                ansel[anselIdx++] = AnselMapping.encode(oneCharAhead);
                ++i;
                ansel[anselIdx++] = c;
                continue;
            }
            if (c < '\u00a0') {
                ansel[anselIdx++] = c;
                continue;
            }
            char ec = AnselMapping.encode(c);
            if (ec < '\u00e0' && ec != c) {
                ansel[anselIdx++] = AnselMapping.encode(c);
                continue;
            }
            char[] breakdown = this.getBrokenDownGlyph(Character.valueOf(c));
            if (breakdown == null) {
                ansel[anselIdx++] = AnselMapping.encode(c);
                continue;
            }
            if (breakdown.length > 1 && breakdown[1] > '\u0000') {
                ansel[anselIdx++] = breakdown[1];
            }
            if (breakdown.length > 2 && breakdown[2] > '\u0000') {
                ansel[anselIdx++] = breakdown[2];
            }
            ansel[anselIdx++] = breakdown[0];
        }
        return new String(ansel).substring(0, anselIdx);
    }

    public String toUtf16(String ansel) {
        char[] utf16 = new char[512];
        int anselIndex = 0;
        int utfIdx = 0;
        int len = ansel.length();
        while (anselIndex < len) {
            char combined;
            char c;
            if ((c = ansel.charAt(anselIndex++)) < '\u00e0' || anselIndex >= len) {
                utf16[utfIdx++] = AnselMapping.decode(c);
                continue;
            }
            char diacritic2 = '\u0000';
            char diacritic1 = c;
            if (anselIndex >= len) {
                utf16[utfIdx++] = AnselMapping.decode(c);
                continue;
            }
            if ((c = ansel.charAt(anselIndex++)) >= '\u00e0') {
                diacritic2 = c;
                if (anselIndex >= len) {
                    utf16[utfIdx++] = AnselMapping.decode(c);
                    continue;
                }
                c = ansel.charAt(anselIndex++);
            }
            if ((combined = this.getCombinedGlyph(c, diacritic1, diacritic2)) == '\u0000') {
                utf16[utfIdx++] = AnselMapping.decode(c);
                utf16[utfIdx++] = AnselMapping.decode(diacritic1);
                if (diacritic2 == '\u0000') continue;
                utf16[utfIdx++] = AnselMapping.decode(diacritic1);
                continue;
            }
            utf16[utfIdx++] = combined;
        }
        return new String(utf16).substring(0, utfIdx);
    }

    private char[] getBrokenDownGlyph(Character c) {
        switch (c.charValue()) {
            case '\u1ea2': {
                return new char[]{'A', '\u00e0', '\u0000'};
            }
            case '\u00c0': {
                return new char[]{'A', '\u00e1', '\u0000'};
            }
            case '\u00c1': {
                return new char[]{'A', '\u00e2', '\u0000'};
            }
            case '\u00c2': {
                return new char[]{'A', '\u00e3', '\u0000'};
            }
            case '\u1ea8': {
                return new char[]{'A', '\u00e3', '\u00e0'};
            }
            case '\u1ea6': {
                return new char[]{'A', '\u00e3', '\u00e1'};
            }
            case '\u1ea4': {
                return new char[]{'A', '\u00e3', '\u00e2'};
            }
            case '\u1eaa': {
                return new char[]{'A', '\u00e3', '\u00e4'};
            }
            case '\u1eac': {
                return new char[]{'A', '\u00e3', '\u00f2'};
            }
            case '\u00c3': {
                return new char[]{'A', '\u00e4', '\u0000'};
            }
            case '\u0100': {
                return new char[]{'A', '\u00e5', '\u0000'};
            }
            case '\u0102': {
                return new char[]{'A', '\u00e6', '\u0000'};
            }
            case '\u1eb2': {
                return new char[]{'A', '\u00e6', '\u00e0'};
            }
            case '\u1eb0': {
                return new char[]{'A', '\u00e6', '\u00e1'};
            }
            case '\u1eae': {
                return new char[]{'A', '\u00e6', '\u00e2'};
            }
            case '\u1eb4': {
                return new char[]{'A', '\u00e6', '\u00e4'};
            }
            case '\u1eb6': {
                return new char[]{'A', '\u00e6', '\u00f2'};
            }
            case '\u0226': {
                return new char[]{'A', '\u00e7', '\u0000'};
            }
            case '\u01e0': {
                return new char[]{'A', '\u00e7', '\u00e5'};
            }
            case '\u00c4': {
                return new char[]{'A', '\u00e8', '\u0000'};
            }
            case '\u01de': {
                return new char[]{'A', '\u00e8', '\u00e5'};
            }
            case '\u01cd': {
                return new char[]{'A', '\u00e9', '\u0000'};
            }
            case '\u00c5': {
                return new char[]{'A', '\u00ea', '\u0000'};
            }
            case '\u01fa': {
                return new char[]{'A', '\u00ea', '\u00e2'};
            }
            case '\u0104': {
                return new char[]{'A', '\u00f1', '\u0000'};
            }
            case '\u1ea0': {
                return new char[]{'A', '\u00f2', '\u0000'};
            }
            case '\u1e00': {
                return new char[]{'A', '\u00f4', '\u0000'};
            }
            case '\u1e02': {
                return new char[]{'B', '\u00e7', '\u0000'};
            }
            case '\u1e04': {
                return new char[]{'B', '\u00f2', '\u0000'};
            }
            case '\u0106': {
                return new char[]{'C', '\u00e2', '\u0000'};
            }
            case '\u0108': {
                return new char[]{'C', '\u00e3', '\u0000'};
            }
            case '\u010a': {
                return new char[]{'C', '\u00e7', '\u0000'};
            }
            case '\u010c': {
                return new char[]{'C', '\u00e9', '\u0000'};
            }
            case '\u00c7': {
                return new char[]{'C', '\u00f0', '\u0000'};
            }
            case '\u1e08': {
                return new char[]{'C', '\u00f0', '\u00e2'};
            }
            case '\u1e0a': {
                return new char[]{'D', '\u00e7', '\u0000'};
            }
            case '\u010e': {
                return new char[]{'D', '\u00e9', '\u0000'};
            }
            case '\u1e10': {
                return new char[]{'D', '\u00f0', '\u0000'};
            }
            case '\u1e0c': {
                return new char[]{'D', '\u00f2', '\u0000'};
            }
            case '\u1eba': {
                return new char[]{'E', '\u00e0', '\u0000'};
            }
            case '\u00c8': {
                return new char[]{'E', '\u00e1', '\u0000'};
            }
            case '\u00c9': {
                return new char[]{'E', '\u00e2', '\u0000'};
            }
            case '\u00ca': {
                return new char[]{'E', '\u00e3', '\u0000'};
            }
            case '\u1ec2': {
                return new char[]{'E', '\u00e3', '\u00e0'};
            }
            case '\u1ec0': {
                return new char[]{'E', '\u00e3', '\u00e1'};
            }
            case '\u1ebe': {
                return new char[]{'E', '\u00e3', '\u00e2'};
            }
            case '\u1ec4': {
                return new char[]{'E', '\u00e3', '\u00e4'};
            }
            case '\u1ec6': {
                return new char[]{'E', '\u00e3', '\u00f2'};
            }
            case '\u1ebc': {
                return new char[]{'E', '\u00e4', '\u0000'};
            }
            case '\u0112': {
                return new char[]{'E', '\u00e5', '\u0000'};
            }
            case '\u1e14': {
                return new char[]{'E', '\u00e5', '\u00e1'};
            }
            case '\u1e16': {
                return new char[]{'E', '\u00e5', '\u00e2'};
            }
            case '\u0114': {
                return new char[]{'E', '\u00e6', '\u0000'};
            }
            case '\u0116': {
                return new char[]{'E', '\u00e7', '\u0000'};
            }
            case '\u00cb': {
                return new char[]{'E', '\u00e8', '\u0000'};
            }
            case '\u011a': {
                return new char[]{'E', '\u00e9', '\u0000'};
            }
            case '\u0228': {
                return new char[]{'E', '\u00f0', '\u0000'};
            }
            case '\u1e1c': {
                return new char[]{'E', '\u00f0', '\u00e6'};
            }
            case '\u0118': {
                return new char[]{'E', '\u00f1', '\u0000'};
            }
            case '\u1eb8': {
                return new char[]{'E', '\u00f2', '\u0000'};
            }
            case '\u1e1e': {
                return new char[]{'F', '\u00e7', '\u0000'};
            }
            case '\u01f4': {
                return new char[]{'G', '\u00e2', '\u0000'};
            }
            case '\u011c': {
                return new char[]{'G', '\u00e3', '\u0000'};
            }
            case '\u1e20': {
                return new char[]{'G', '\u00e5', '\u0000'};
            }
            case '\u011e': {
                return new char[]{'G', '\u00e6', '\u0000'};
            }
            case '\u0120': {
                return new char[]{'G', '\u00e7', '\u0000'};
            }
            case '\u01e6': {
                return new char[]{'G', '\u00e9', '\u0000'};
            }
            case '\u0122': {
                return new char[]{'G', '\u00f0', '\u0000'};
            }
            case '\u0124': {
                return new char[]{'H', '\u00e3', '\u0000'};
            }
            case '\u1e22': {
                return new char[]{'H', '\u00e7', '\u0000'};
            }
            case '\u1e26': {
                return new char[]{'H', '\u00e8', '\u0000'};
            }
            case '\u021e': {
                return new char[]{'H', '\u00e9', '\u0000'};
            }
            case '\u1e28': {
                return new char[]{'H', '\u00f0', '\u0000'};
            }
            case '\u1e24': {
                return new char[]{'H', '\u00f2', '\u0000'};
            }
            case '\u1e2a': {
                return new char[]{'H', '\u00f9', '\u0000'};
            }
            case '\u1ec8': {
                return new char[]{'I', '\u00e0', '\u0000'};
            }
            case '\u00cc': {
                return new char[]{'I', '\u00e1', '\u0000'};
            }
            case '\u00cd': {
                return new char[]{'I', '\u00e2', '\u0000'};
            }
            case '\u00ce': {
                return new char[]{'I', '\u00e3', '\u0000'};
            }
            case '\u0128': {
                return new char[]{'I', '\u00e4', '\u0000'};
            }
            case '\u012a': {
                return new char[]{'I', '\u00e5', '\u0000'};
            }
            case '\u012c': {
                return new char[]{'I', '\u00e6', '\u0000'};
            }
            case '\u0130': {
                return new char[]{'I', '\u00e7', '\u0000'};
            }
            case '\u00cf': {
                return new char[]{'I', '\u00e8', '\u0000'};
            }
            case '\u1e2e': {
                return new char[]{'I', '\u00e8', '\u00e2'};
            }
            case '\u01cf': {
                return new char[]{'I', '\u00e9', '\u0000'};
            }
            case '\u012e': {
                return new char[]{'I', '\u00f1', '\u0000'};
            }
            case '\u1eca': {
                return new char[]{'I', '\u00f2', '\u0000'};
            }
            case '\u0134': {
                return new char[]{'J', '\u00e3', '\u0000'};
            }
            case '\u1e30': {
                return new char[]{'K', '\u00e2', '\u0000'};
            }
            case '\u01e8': {
                return new char[]{'K', '\u00e9', '\u0000'};
            }
            case '\u0136': {
                return new char[]{'K', '\u00f0', '\u0000'};
            }
            case '\u1e32': {
                return new char[]{'K', '\u00f2', '\u0000'};
            }
            case '\u0139': {
                return new char[]{'L', '\u00e2', '\u0000'};
            }
            case '\u013d': {
                return new char[]{'L', '\u00e9', '\u0000'};
            }
            case '\u013b': {
                return new char[]{'L', '\u00f0', '\u0000'};
            }
            case '\u1e36': {
                return new char[]{'L', '\u00f2', '\u0000'};
            }
            case '\u1e38': {
                return new char[]{'L', '\u00f2', '\u00e5'};
            }
            case '\u1e3e': {
                return new char[]{'M', '\u00e2', '\u0000'};
            }
            case '\u1e40': {
                return new char[]{'M', '\u00e7', '\u0000'};
            }
            case '\u1e42': {
                return new char[]{'M', '\u00f2', '\u0000'};
            }
            case '\u01f8': {
                return new char[]{'N', '\u00e1', '\u0000'};
            }
            case '\u0143': {
                return new char[]{'N', '\u00e2', '\u0000'};
            }
            case '\u00d1': {
                return new char[]{'N', '\u00e4', '\u0000'};
            }
            case '\u1e44': {
                return new char[]{'N', '\u00e7', '\u0000'};
            }
            case '\u0147': {
                return new char[]{'N', '\u00e9', '\u0000'};
            }
            case '\u0145': {
                return new char[]{'N', '\u00f0', '\u0000'};
            }
            case '\u1e46': {
                return new char[]{'N', '\u00f2', '\u0000'};
            }
            case '\u1ece': {
                return new char[]{'O', '\u00e0', '\u0000'};
            }
            case '\u00d2': {
                return new char[]{'O', '\u00e1', '\u0000'};
            }
            case '\u00d3': {
                return new char[]{'O', '\u00e2', '\u0000'};
            }
            case '\u00d4': {
                return new char[]{'O', '\u00e3', '\u0000'};
            }
            case '\u1ed4': {
                return new char[]{'O', '\u00e3', '\u00e0'};
            }
            case '\u1ed2': {
                return new char[]{'O', '\u00e3', '\u00e1'};
            }
            case '\u1ed0': {
                return new char[]{'O', '\u00e3', '\u00e2'};
            }
            case '\u1ed6': {
                return new char[]{'O', '\u00e3', '\u00e4'};
            }
            case '\u1ed8': {
                return new char[]{'O', '\u00e3', '\u00f2'};
            }
            case '\u00d5': {
                return new char[]{'O', '\u00e4', '\u0000'};
            }
            case '\u1e4c': {
                return new char[]{'O', '\u00e4', '\u00e2'};
            }
            case '\u022c': {
                return new char[]{'O', '\u00e4', '\u00e5'};
            }
            case '\u1e4e': {
                return new char[]{'O', '\u00e4', '\u00e8'};
            }
            case '\u014c': {
                return new char[]{'O', '\u00e5', '\u0000'};
            }
            case '\u1e50': {
                return new char[]{'O', '\u00e5', '\u00e1'};
            }
            case '\u1e52': {
                return new char[]{'O', '\u00e5', '\u00e2'};
            }
            case '\u014e': {
                return new char[]{'O', '\u00e6', '\u0000'};
            }
            case '\u022e': {
                return new char[]{'O', '\u00e7', '\u0000'};
            }
            case '\u0230': {
                return new char[]{'O', '\u00e7', '\u00e5'};
            }
            case '\u00d6': {
                return new char[]{'O', '\u00e8', '\u0000'};
            }
            case '\u022a': {
                return new char[]{'O', '\u00e8', '\u00e5'};
            }
            case '\u01d1': {
                return new char[]{'O', '\u00e9', '\u0000'};
            }
            case '\u0150': {
                return new char[]{'O', '\u00ee', '\u0000'};
            }
            case '\u01ea': {
                return new char[]{'O', '\u00f1', '\u0000'};
            }
            case '\u01ec': {
                return new char[]{'O', '\u00f1', '\u00e5'};
            }
            case '\u1ecc': {
                return new char[]{'O', '\u00f2', '\u0000'};
            }
            case '\u1e54': {
                return new char[]{'P', '\u00e2', '\u0000'};
            }
            case '\u1e56': {
                return new char[]{'P', '\u00e7', '\u0000'};
            }
            case '\u0154': {
                return new char[]{'R', '\u00e2', '\u0000'};
            }
            case '\u1e58': {
                return new char[]{'R', '\u00e7', '\u0000'};
            }
            case '\u0158': {
                return new char[]{'R', '\u00e9', '\u0000'};
            }
            case '\u0156': {
                return new char[]{'R', '\u00f0', '\u0000'};
            }
            case '\u1e5a': {
                return new char[]{'R', '\u00f2', '\u0000'};
            }
            case '\u1e5c': {
                return new char[]{'R', '\u00f2', '\u00e5'};
            }
            case '\u015a': {
                return new char[]{'S', '\u00e2', '\u0000'};
            }
            case '\u1e64': {
                return new char[]{'S', '\u00e2', '\u00e7'};
            }
            case '\u015c': {
                return new char[]{'S', '\u00e3', '\u0000'};
            }
            case '\u1e60': {
                return new char[]{'S', '\u00e7', '\u0000'};
            }
            case '\u0160': {
                return new char[]{'S', '\u00e9', '\u0000'};
            }
            case '\u1e66': {
                return new char[]{'S', '\u00e9', '\u00e7'};
            }
            case '\u015e': {
                return new char[]{'S', '\u00f0', '\u0000'};
            }
            case '\u1e62': {
                return new char[]{'S', '\u00f2', '\u0000'};
            }
            case '\u1e68': {
                return new char[]{'S', '\u00f2', '\u00e7'};
            }
            case '\u0218': {
                return new char[]{'S', '\u00f7', '\u0000'};
            }
            case '\u1e6a': {
                return new char[]{'T', '\u00e7', '\u0000'};
            }
            case '\u0164': {
                return new char[]{'T', '\u00e9', '\u0000'};
            }
            case '\u0162': {
                return new char[]{'T', '\u00f0', '\u0000'};
            }
            case '\u1e6c': {
                return new char[]{'T', '\u00f2', '\u0000'};
            }
            case '\u021a': {
                return new char[]{'T', '\u00f7', '\u0000'};
            }
            case '\u1ee6': {
                return new char[]{'U', '\u00e0', '\u0000'};
            }
            case '\u00d9': {
                return new char[]{'U', '\u00e1', '\u0000'};
            }
            case '\u00da': {
                return new char[]{'U', '\u00e2', '\u0000'};
            }
            case '\u00db': {
                return new char[]{'U', '\u00e3', '\u0000'};
            }
            case '\u0168': {
                return new char[]{'U', '\u00e4', '\u0000'};
            }
            case '\u1e78': {
                return new char[]{'U', '\u00e4', '\u00e2'};
            }
            case '\u016a': {
                return new char[]{'U', '\u00e5', '\u0000'};
            }
            case '\u1e7a': {
                return new char[]{'U', '\u00e5', '\u00e8'};
            }
            case '\u016c': {
                return new char[]{'U', '\u00e6', '\u0000'};
            }
            case '\u00dc': {
                return new char[]{'U', '\u00e8', '\u0000'};
            }
            case '\u01db': {
                return new char[]{'U', '\u00e8', '\u00e1'};
            }
            case '\u01d7': {
                return new char[]{'U', '\u00e8', '\u00e2'};
            }
            case '\u01d5': {
                return new char[]{'U', '\u00e8', '\u00e5'};
            }
            case '\u01d9': {
                return new char[]{'U', '\u00e8', '\u00e9'};
            }
            case '\u01d3': {
                return new char[]{'U', '\u00e9', '\u0000'};
            }
            case '\u016e': {
                return new char[]{'U', '\u00ea', '\u0000'};
            }
            case '\u0170': {
                return new char[]{'U', '\u00ee', '\u0000'};
            }
            case '\u0172': {
                return new char[]{'U', '\u00f1', '\u0000'};
            }
            case '\u1ee4': {
                return new char[]{'U', '\u00f2', '\u0000'};
            }
            case '\u1e72': {
                return new char[]{'U', '\u00f3', '\u0000'};
            }
            case '\u1e7c': {
                return new char[]{'V', '\u00e4', '\u0000'};
            }
            case '\u1e7e': {
                return new char[]{'V', '\u00f2', '\u0000'};
            }
            case '\u1e80': {
                return new char[]{'W', '\u00e1', '\u0000'};
            }
            case '\u1e82': {
                return new char[]{'W', '\u00e2', '\u0000'};
            }
            case '\u0174': {
                return new char[]{'W', '\u00e3', '\u0000'};
            }
            case '\u1e86': {
                return new char[]{'W', '\u00e7', '\u0000'};
            }
            case '\u1e84': {
                return new char[]{'W', '\u00e8', '\u0000'};
            }
            case '\u1e88': {
                return new char[]{'W', '\u00f2', '\u0000'};
            }
            case '\u1e8a': {
                return new char[]{'X', '\u00e7', '\u0000'};
            }
            case '\u1e8c': {
                return new char[]{'X', '\u00e8', '\u0000'};
            }
            case '\u1ef6': {
                return new char[]{'Y', '\u00e0', '\u0000'};
            }
            case '\u1ef2': {
                return new char[]{'Y', '\u00e1', '\u0000'};
            }
            case '\u00dd': {
                return new char[]{'Y', '\u00e2', '\u0000'};
            }
            case '\u0176': {
                return new char[]{'Y', '\u00e3', '\u0000'};
            }
            case '\u1ef8': {
                return new char[]{'Y', '\u00e4', '\u0000'};
            }
            case '\u0232': {
                return new char[]{'Y', '\u00e5', '\u0000'};
            }
            case '\u1e8e': {
                return new char[]{'Y', '\u00e7', '\u0000'};
            }
            case '\u0178': {
                return new char[]{'Y', '\u00e8', '\u0000'};
            }
            case '\u1ef4': {
                return new char[]{'Y', '\u00f2', '\u0000'};
            }
            case '\u0179': {
                return new char[]{'Z', '\u00e2', '\u0000'};
            }
            case '\u1e90': {
                return new char[]{'Z', '\u00e3', '\u0000'};
            }
            case '\u017b': {
                return new char[]{'Z', '\u00e7', '\u0000'};
            }
            case '\u017d': {
                return new char[]{'Z', '\u00e9', '\u0000'};
            }
            case '\u1e92': {
                return new char[]{'Z', '\u00f2', '\u0000'};
            }
            case '\u1ea3': {
                return new char[]{'a', '\u00e0', '\u0000'};
            }
            case '\u00e0': {
                return new char[]{'a', '\u00e1', '\u0000'};
            }
            case '\u00e1': {
                return new char[]{'a', '\u00e2', '\u0000'};
            }
            case '\u00e2': {
                return new char[]{'a', '\u00e3', '\u0000'};
            }
            case '\u1ea9': {
                return new char[]{'a', '\u00e3', '\u00e0'};
            }
            case '\u1ea7': {
                return new char[]{'a', '\u00e3', '\u00e1'};
            }
            case '\u1ea5': {
                return new char[]{'a', '\u00e3', '\u00e2'};
            }
            case '\u1eab': {
                return new char[]{'a', '\u00e3', '\u00e4'};
            }
            case '\u1ead': {
                return new char[]{'a', '\u00e3', '\u00f2'};
            }
            case '\u00e3': {
                return new char[]{'a', '\u00e4', '\u0000'};
            }
            case '\u0101': {
                return new char[]{'a', '\u00e5', '\u0000'};
            }
            case '\u0103': {
                return new char[]{'a', '\u00e6', '\u0000'};
            }
            case '\u1eb3': {
                return new char[]{'a', '\u00e6', '\u00e0'};
            }
            case '\u1eb1': {
                return new char[]{'a', '\u00e6', '\u00e1'};
            }
            case '\u1eaf': {
                return new char[]{'a', '\u00e6', '\u00e2'};
            }
            case '\u1eb5': {
                return new char[]{'a', '\u00e6', '\u00e4'};
            }
            case '\u1eb7': {
                return new char[]{'a', '\u00e6', '\u00f2'};
            }
            case '\u0227': {
                return new char[]{'a', '\u00e7', '\u0000'};
            }
            case '\u01e1': {
                return new char[]{'a', '\u00e7', '\u00e5'};
            }
            case '\u00e4': {
                return new char[]{'a', '\u00e8', '\u0000'};
            }
            case '\u01df': {
                return new char[]{'a', '\u00e8', '\u00e5'};
            }
            case '\u01ce': {
                return new char[]{'a', '\u00e9', '\u0000'};
            }
            case '\u00e5': {
                return new char[]{'a', '\u00ea', '\u0000'};
            }
            case '\u01fb': {
                return new char[]{'a', '\u00ea', '\u00e2'};
            }
            case '\u0105': {
                return new char[]{'a', '\u00f1', '\u0000'};
            }
            case '\u1ea1': {
                return new char[]{'a', '\u00f2', '\u0000'};
            }
            case '\u1e01': {
                return new char[]{'a', '\u00f4', '\u0000'};
            }
            case '\u1e03': {
                return new char[]{'b', '\u00e7', '\u0000'};
            }
            case '\u1e05': {
                return new char[]{'b', '\u00f2', '\u0000'};
            }
            case '\u0107': {
                return new char[]{'c', '\u00e2', '\u0000'};
            }
            case '\u0109': {
                return new char[]{'c', '\u00e3', '\u0000'};
            }
            case '\u010b': {
                return new char[]{'c', '\u00e7', '\u0000'};
            }
            case '\u010d': {
                return new char[]{'c', '\u00e9', '\u0000'};
            }
            case '\u00e7': {
                return new char[]{'c', '\u00f0', '\u0000'};
            }
            case '\u1e09': {
                return new char[]{'c', '\u00f0', '\u00e2'};
            }
            case '\u1e0b': {
                return new char[]{'d', '\u00e7', '\u0000'};
            }
            case '\u010f': {
                return new char[]{'d', '\u00e9', '\u0000'};
            }
            case '\u1e11': {
                return new char[]{'d', '\u00f0', '\u0000'};
            }
            case '\u1e0d': {
                return new char[]{'d', '\u00f2', '\u0000'};
            }
            case '\u1ebb': {
                return new char[]{'e', '\u00e0', '\u0000'};
            }
            case '\u00e8': {
                return new char[]{'e', '\u00e1', '\u0000'};
            }
            case '\u00e9': {
                return new char[]{'e', '\u00e2', '\u0000'};
            }
            case '\u00ea': {
                return new char[]{'e', '\u00e3', '\u0000'};
            }
            case '\u1ec3': {
                return new char[]{'e', '\u00e3', '\u00e0'};
            }
            case '\u1ec1': {
                return new char[]{'e', '\u00e3', '\u00e1'};
            }
            case '\u1ebf': {
                return new char[]{'e', '\u00e3', '\u00e2'};
            }
            case '\u1ec5': {
                return new char[]{'e', '\u00e3', '\u00e4'};
            }
            case '\u1ec7': {
                return new char[]{'e', '\u00e3', '\u00f2'};
            }
            case '\u1ebd': {
                return new char[]{'e', '\u00e4', '\u0000'};
            }
            case '\u0113': {
                return new char[]{'e', '\u00e5', '\u0000'};
            }
            case '\u1e15': {
                return new char[]{'e', '\u00e5', '\u00e1'};
            }
            case '\u1e17': {
                return new char[]{'e', '\u00e5', '\u00e2'};
            }
            case '\u0115': {
                return new char[]{'e', '\u00e6', '\u0000'};
            }
            case '\u0117': {
                return new char[]{'e', '\u00e7', '\u0000'};
            }
            case '\u00eb': {
                return new char[]{'e', '\u00e8', '\u0000'};
            }
            case '\u011b': {
                return new char[]{'e', '\u00e9', '\u0000'};
            }
            case '\u0229': {
                return new char[]{'e', '\u00f0', '\u0000'};
            }
            case '\u1e1d': {
                return new char[]{'e', '\u00f0', '\u00e6'};
            }
            case '\u0119': {
                return new char[]{'e', '\u00f1', '\u0000'};
            }
            case '\u1eb9': {
                return new char[]{'e', '\u00f2', '\u0000'};
            }
            case '\u1e1f': {
                return new char[]{'f', '\u00e7', '\u0000'};
            }
            case '\u01f5': {
                return new char[]{'g', '\u00e2', '\u0000'};
            }
            case '\u011d': {
                return new char[]{'g', '\u00e3', '\u0000'};
            }
            case '\u1e21': {
                return new char[]{'g', '\u00e5', '\u0000'};
            }
            case '\u011f': {
                return new char[]{'g', '\u00e6', '\u0000'};
            }
            case '\u0121': {
                return new char[]{'g', '\u00e7', '\u0000'};
            }
            case '\u01e7': {
                return new char[]{'g', '\u00e9', '\u0000'};
            }
            case '\u0123': {
                return new char[]{'g', '\u00f0', '\u0000'};
            }
            case '\u0125': {
                return new char[]{'h', '\u00e3', '\u0000'};
            }
            case '\u1e23': {
                return new char[]{'h', '\u00e7', '\u0000'};
            }
            case '\u1e27': {
                return new char[]{'h', '\u00e8', '\u0000'};
            }
            case '\u021f': {
                return new char[]{'h', '\u00e9', '\u0000'};
            }
            case '\u1e29': {
                return new char[]{'h', '\u00f0', '\u0000'};
            }
            case '\u1e25': {
                return new char[]{'h', '\u00f2', '\u0000'};
            }
            case '\u1e2b': {
                return new char[]{'h', '\u00f9', '\u0000'};
            }
            case '\u1ec9': {
                return new char[]{'i', '\u00e0', '\u0000'};
            }
            case '\u00ec': {
                return new char[]{'i', '\u00e1', '\u0000'};
            }
            case '\u00ed': {
                return new char[]{'i', '\u00e2', '\u0000'};
            }
            case '\u00ee': {
                return new char[]{'i', '\u00e3', '\u0000'};
            }
            case '\u0129': {
                return new char[]{'i', '\u00e4', '\u0000'};
            }
            case '\u012b': {
                return new char[]{'i', '\u00e5', '\u0000'};
            }
            case '\u012d': {
                return new char[]{'i', '\u00e6', '\u0000'};
            }
            case '\u00ef': {
                return new char[]{'i', '\u00e8', '\u0000'};
            }
            case '\u1e2f': {
                return new char[]{'i', '\u00e8', '\u00e2'};
            }
            case '\u01d0': {
                return new char[]{'i', '\u00e9', '\u0000'};
            }
            case '\u012f': {
                return new char[]{'i', '\u00f1', '\u0000'};
            }
            case '\u1ecb': {
                return new char[]{'i', '\u00f2', '\u0000'};
            }
            case '\u0135': {
                return new char[]{'j', '\u00e3', '\u0000'};
            }
            case '\u01f0': {
                return new char[]{'j', '\u00e9', '\u0000'};
            }
            case '\u1e31': {
                return new char[]{'k', '\u00e2', '\u0000'};
            }
            case '\u01e9': {
                return new char[]{'k', '\u00e9', '\u0000'};
            }
            case '\u0137': {
                return new char[]{'k', '\u00f0', '\u0000'};
            }
            case '\u1e33': {
                return new char[]{'k', '\u00f2', '\u0000'};
            }
            case '\u013a': {
                return new char[]{'l', '\u00e2', '\u0000'};
            }
            case '\u013e': {
                return new char[]{'l', '\u00e9', '\u0000'};
            }
            case '\u013c': {
                return new char[]{'l', '\u00f0', '\u0000'};
            }
            case '\u1e37': {
                return new char[]{'l', '\u00f2', '\u0000'};
            }
            case '\u1e39': {
                return new char[]{'l', '\u00f2', '\u00e5'};
            }
            case '\u1e3f': {
                return new char[]{'m', '\u00e2', '\u0000'};
            }
            case '\u1e41': {
                return new char[]{'m', '\u00e7', '\u0000'};
            }
            case '\u1e43': {
                return new char[]{'m', '\u00f2', '\u0000'};
            }
            case '\u01f9': {
                return new char[]{'n', '\u00e1', '\u0000'};
            }
            case '\u0144': {
                return new char[]{'n', '\u00e2', '\u0000'};
            }
            case '\u00f1': {
                return new char[]{'n', '\u00e4', '\u0000'};
            }
            case '\u1e45': {
                return new char[]{'n', '\u00e7', '\u0000'};
            }
            case '\u0148': {
                return new char[]{'n', '\u00e9', '\u0000'};
            }
            case '\u0146': {
                return new char[]{'n', '\u00f0', '\u0000'};
            }
            case '\u1e47': {
                return new char[]{'n', '\u00f2', '\u0000'};
            }
            case '\u1ecf': {
                return new char[]{'o', '\u00e0', '\u0000'};
            }
            case '\u00f2': {
                return new char[]{'o', '\u00e1', '\u0000'};
            }
            case '\u00f3': {
                return new char[]{'o', '\u00e2', '\u0000'};
            }
            case '\u00f4': {
                return new char[]{'o', '\u00e3', '\u0000'};
            }
            case '\u1ed5': {
                return new char[]{'o', '\u00e3', '\u00e0'};
            }
            case '\u1ed3': {
                return new char[]{'o', '\u00e3', '\u00e1'};
            }
            case '\u1ed1': {
                return new char[]{'o', '\u00e3', '\u00e2'};
            }
            case '\u1ed7': {
                return new char[]{'o', '\u00e3', '\u00e4'};
            }
            case '\u1ed9': {
                return new char[]{'o', '\u00e3', '\u00f2'};
            }
            case '\u00f5': {
                return new char[]{'o', '\u00e4', '\u0000'};
            }
            case '\u1e4d': {
                return new char[]{'o', '\u00e4', '\u00e2'};
            }
            case '\u022d': {
                return new char[]{'o', '\u00e4', '\u00e5'};
            }
            case '\u1e4f': {
                return new char[]{'o', '\u00e4', '\u00e8'};
            }
            case '\u014d': {
                return new char[]{'o', '\u00e5', '\u0000'};
            }
            case '\u1e51': {
                return new char[]{'o', '\u00e5', '\u00e1'};
            }
            case '\u1e53': {
                return new char[]{'o', '\u00e5', '\u00e2'};
            }
            case '\u014f': {
                return new char[]{'o', '\u00e6', '\u0000'};
            }
            case '\u022f': {
                return new char[]{'o', '\u00e7', '\u0000'};
            }
            case '\u0231': {
                return new char[]{'o', '\u00e7', '\u00e5'};
            }
            case '\u00f6': {
                return new char[]{'o', '\u00e8', '\u0000'};
            }
            case '\u022b': {
                return new char[]{'o', '\u00e8', '\u00e5'};
            }
            case '\u01d2': {
                return new char[]{'o', '\u00e9', '\u0000'};
            }
            case '\u0151': {
                return new char[]{'o', '\u00ee', '\u0000'};
            }
            case '\u01eb': {
                return new char[]{'o', '\u00f1', '\u0000'};
            }
            case '\u01ed': {
                return new char[]{'o', '\u00f1', '\u00e5'};
            }
            case '\u1ecd': {
                return new char[]{'o', '\u00f2', '\u0000'};
            }
            case '\u1e55': {
                return new char[]{'p', '\u00e2', '\u0000'};
            }
            case '\u1e57': {
                return new char[]{'p', '\u00e7', '\u0000'};
            }
            case '\u0155': {
                return new char[]{'r', '\u00e2', '\u0000'};
            }
            case '\u1e59': {
                return new char[]{'r', '\u00e7', '\u0000'};
            }
            case '\u0159': {
                return new char[]{'r', '\u00e9', '\u0000'};
            }
            case '\u0157': {
                return new char[]{'r', '\u00f0', '\u0000'};
            }
            case '\u1e5b': {
                return new char[]{'r', '\u00f2', '\u0000'};
            }
            case '\u1e5d': {
                return new char[]{'r', '\u00f2', '\u00e5'};
            }
            case '\u015b': {
                return new char[]{'s', '\u00e2', '\u0000'};
            }
            case '\u1e65': {
                return new char[]{'s', '\u00e2', '\u00e7'};
            }
            case '\u015d': {
                return new char[]{'s', '\u00e3', '\u0000'};
            }
            case '\u1e61': {
                return new char[]{'s', '\u00e7', '\u0000'};
            }
            case '\u0161': {
                return new char[]{'s', '\u00e9', '\u0000'};
            }
            case '\u1e67': {
                return new char[]{'s', '\u00e9', '\u00e7'};
            }
            case '\u015f': {
                return new char[]{'s', '\u00f0', '\u0000'};
            }
            case '\u1e63': {
                return new char[]{'s', '\u00f2', '\u0000'};
            }
            case '\u1e69': {
                return new char[]{'s', '\u00f2', '\u00e7'};
            }
            case '\u0219': {
                return new char[]{'s', '\u00f7', '\u0000'};
            }
            case '\u1e6b': {
                return new char[]{'t', '\u00e7', '\u0000'};
            }
            case '\u1e97': {
                return new char[]{'t', '\u00e8', '\u0000'};
            }
            case '\u0165': {
                return new char[]{'t', '\u00e9', '\u0000'};
            }
            case '\u0163': {
                return new char[]{'t', '\u00f0', '\u0000'};
            }
            case '\u1e6d': {
                return new char[]{'t', '\u00f2', '\u0000'};
            }
            case '\u021b': {
                return new char[]{'t', '\u00f7', '\u0000'};
            }
            case '\u1ee7': {
                return new char[]{'u', '\u00e0', '\u0000'};
            }
            case '\u00f9': {
                return new char[]{'u', '\u00e1', '\u0000'};
            }
            case '\u00fa': {
                return new char[]{'u', '\u00e2', '\u0000'};
            }
            case '\u00fb': {
                return new char[]{'u', '\u00e3', '\u0000'};
            }
            case '\u0169': {
                return new char[]{'u', '\u00e4', '\u0000'};
            }
            case '\u1e79': {
                return new char[]{'u', '\u00e4', '\u00e2'};
            }
            case '\u016b': {
                return new char[]{'u', '\u00e5', '\u0000'};
            }
            case '\u1e7b': {
                return new char[]{'u', '\u00e5', '\u00e8'};
            }
            case '\u016d': {
                return new char[]{'u', '\u00e6', '\u0000'};
            }
            case '\u00fc': {
                return new char[]{'u', '\u00e8', '\u0000'};
            }
            case '\u01dc': {
                return new char[]{'u', '\u00e8', '\u00e1'};
            }
            case '\u01d8': {
                return new char[]{'u', '\u00e8', '\u00e2'};
            }
            case '\u01d6': {
                return new char[]{'u', '\u00e8', '\u00e5'};
            }
            case '\u01da': {
                return new char[]{'u', '\u00e8', '\u00e9'};
            }
            case '\u01d4': {
                return new char[]{'u', '\u00e9', '\u0000'};
            }
            case '\u016f': {
                return new char[]{'u', '\u00ea', '\u0000'};
            }
            case '\u0171': {
                return new char[]{'u', '\u00ee', '\u0000'};
            }
            case '\u0173': {
                return new char[]{'u', '\u00f1', '\u0000'};
            }
            case '\u1ee5': {
                return new char[]{'u', '\u00f2', '\u0000'};
            }
            case '\u1e73': {
                return new char[]{'u', '\u00f3', '\u0000'};
            }
            case '\u1e7d': {
                return new char[]{'v', '\u00e4', '\u0000'};
            }
            case '\u1e7f': {
                return new char[]{'v', '\u00f2', '\u0000'};
            }
            case '\u1e81': {
                return new char[]{'w', '\u00e1', '\u0000'};
            }
            case '\u1e83': {
                return new char[]{'w', '\u00e2', '\u0000'};
            }
            case '\u0175': {
                return new char[]{'w', '\u00e3', '\u0000'};
            }
            case '\u1e87': {
                return new char[]{'w', '\u00e7', '\u0000'};
            }
            case '\u1e85': {
                return new char[]{'w', '\u00e8', '\u0000'};
            }
            case '\u1e98': {
                return new char[]{'w', '\u00ea', '\u0000'};
            }
            case '\u1e89': {
                return new char[]{'w', '\u00f2', '\u0000'};
            }
            case '\u1e8b': {
                return new char[]{'x', '\u00e7', '\u0000'};
            }
            case '\u1e8d': {
                return new char[]{'x', '\u00e8', '\u0000'};
            }
            case '\u1ef7': {
                return new char[]{'y', '\u00e0', '\u0000'};
            }
            case '\u1ef3': {
                return new char[]{'y', '\u00e1', '\u0000'};
            }
            case '\u00fd': {
                return new char[]{'y', '\u00e2', '\u0000'};
            }
            case '\u0177': {
                return new char[]{'y', '\u00e3', '\u0000'};
            }
            case '\u1ef9': {
                return new char[]{'y', '\u00e4', '\u0000'};
            }
            case '\u0233': {
                return new char[]{'y', '\u00e5', '\u0000'};
            }
            case '\u1e8f': {
                return new char[]{'y', '\u00e7', '\u0000'};
            }
            case '\u00ff': {
                return new char[]{'y', '\u00e8', '\u0000'};
            }
            case '\u1e99': {
                return new char[]{'y', '\u00ea', '\u0000'};
            }
            case '\u1ef5': {
                return new char[]{'y', '\u00f2', '\u0000'};
            }
            case '\u017a': {
                return new char[]{'z', '\u00e2', '\u0000'};
            }
            case '\u1e91': {
                return new char[]{'z', '\u00e3', '\u0000'};
            }
            case '\u017c': {
                return new char[]{'z', '\u00e7', '\u0000'};
            }
            case '\u017e': {
                return new char[]{'z', '\u00e9', '\u0000'};
            }
            case '\u1e93': {
                return new char[]{'z', '\u00f2', '\u0000'};
            }
        }
        return null;
    }

    private char getCombinedGlyph(char baseChar, char modifier1, char modifier2) {
        if (baseChar == 'A') {
            if (modifier1 == '\u00e0') {
                return '\u1ea2';
            }
            if (modifier1 == '\u00e1') {
                return '\u00c0';
            }
            if (modifier1 == '\u00e2') {
                return '\u00c1';
            }
            if (modifier1 == '\u00e3') {
                if (modifier2 == '\u00e0') {
                    return '\u1ea8';
                }
                if (modifier2 == '\u00e1') {
                    return '\u1ea6';
                }
                if (modifier2 == '\u00e2') {
                    return '\u1ea4';
                }
                if (modifier2 == '\u00e4') {
                    return '\u1eaa';
                }
                if (modifier2 == '\u00f2') {
                    return '\u1eac';
                }
                return '\u00c2';
            }
            if (modifier1 == '\u00e4') {
                return '\u00c3';
            }
            if (modifier1 == '\u00e5') {
                return '\u0100';
            }
            if (modifier1 == '\u00e6') {
                if (modifier2 == '\u00e0') {
                    return '\u1eb2';
                }
                if (modifier2 == '\u00e1') {
                    return '\u1eb0';
                }
                if (modifier2 == '\u00e2') {
                    return '\u1eae';
                }
                if (modifier2 == '\u00e4') {
                    return '\u1eb4';
                }
                if (modifier2 == '\u00f2') {
                    return '\u1eb6';
                }
                return '\u0102';
            }
            if (modifier1 == '\u00e7') {
                if (modifier2 == '\u00e5') {
                    return '\u01e0';
                }
                return '\u0226';
            }
            if (modifier1 == '\u00e8') {
                if (modifier2 == '\u00e5') {
                    return '\u01de';
                }
                return '\u00c4';
            }
            if (modifier1 == '\u00e9') {
                return '\u01cd';
            }
            if (modifier1 == '\u00ea') {
                if (modifier2 == '\u00e2') {
                    return '\u01fa';
                }
                return '\u00c5';
            }
            if (modifier1 == '\u00f1') {
                return '\u0104';
            }
            if (modifier1 == '\u00f2') {
                return '\u1ea0';
            }
            if (modifier1 == '\u00f4') {
                return '\u1e00';
            }
        }
        if (baseChar == 'B') {
            if (modifier1 == '\u00e7') {
                return '\u1e02';
            }
            if (modifier1 == '\u00f2') {
                return '\u1e04';
            }
        }
        if (baseChar == 'C') {
            if (modifier1 == '\u00e2') {
                return '\u0106';
            }
            if (modifier1 == '\u00e3') {
                return '\u0108';
            }
            if (modifier1 == '\u00e7') {
                return '\u010a';
            }
            if (modifier1 == '\u00e9') {
                return '\u010c';
            }
            if (modifier1 == '\u00f0') {
                if (modifier2 == '\u00e2') {
                    return '\u1e08';
                }
                return '\u00c7';
            }
        }
        if (baseChar == 'D') {
            if (modifier1 == '\u00e7') {
                return '\u1e0a';
            }
            if (modifier1 == '\u00e9') {
                return '\u010e';
            }
            if (modifier1 == '\u00f0') {
                return '\u1e10';
            }
            if (modifier1 == '\u00f2') {
                return '\u1e0c';
            }
        }
        if (baseChar == 'E') {
            if (modifier1 == '\u00e0') {
                return '\u1eba';
            }
            if (modifier1 == '\u00e1') {
                return '\u00c8';
            }
            if (modifier1 == '\u00e2') {
                return '\u00c9';
            }
            if (modifier1 == '\u00e3') {
                if (modifier2 == '\u00e0') {
                    return '\u1ec2';
                }
                if (modifier2 == '\u00e1') {
                    return '\u1ec0';
                }
                if (modifier2 == '\u00e2') {
                    return '\u1ebe';
                }
                if (modifier2 == '\u00e4') {
                    return '\u1ec4';
                }
                if (modifier2 == '\u00f2') {
                    return '\u1ec6';
                }
                return '\u00ca';
            }
            if (modifier1 == '\u00e4') {
                return '\u1ebc';
            }
            if (modifier1 == '\u00e5') {
                if (modifier2 == '\u00e1') {
                    return '\u1e14';
                }
                if (modifier2 == '\u00e2') {
                    return '\u1e16';
                }
                return '\u0112';
            }
            if (modifier1 == '\u00e6') {
                return '\u0114';
            }
            if (modifier1 == '\u00e7') {
                return '\u0116';
            }
            if (modifier1 == '\u00e8') {
                return '\u00cb';
            }
            if (modifier1 == '\u00e9') {
                return '\u011a';
            }
            if (modifier1 == '\u00f0') {
                if (modifier2 == '\u00e6') {
                    return '\u1e1c';
                }
                return '\u0228';
            }
            if (modifier1 == '\u00f1') {
                return '\u0118';
            }
            if (modifier1 == '\u00f2') {
                return '\u1eb8';
            }
        }
        if (baseChar == 'F' && modifier1 == '\u00e7') {
            return '\u1e1e';
        }
        if (baseChar == 'G') {
            if (modifier1 == '\u00e2') {
                return '\u01f4';
            }
            if (modifier1 == '\u00e3') {
                return '\u011c';
            }
            if (modifier1 == '\u00e5') {
                return '\u1e20';
            }
            if (modifier1 == '\u00e6') {
                return '\u011e';
            }
            if (modifier1 == '\u00e7') {
                return '\u0120';
            }
            if (modifier1 == '\u00e9') {
                return '\u01e6';
            }
            if (modifier1 == '\u00f0') {
                return '\u0122';
            }
        }
        if (baseChar == 'H') {
            if (modifier1 == '\u00e3') {
                return '\u0124';
            }
            if (modifier1 == '\u00e7') {
                return '\u1e22';
            }
            if (modifier1 == '\u00e8') {
                return '\u1e26';
            }
            if (modifier1 == '\u00e9') {
                return '\u021e';
            }
            if (modifier1 == '\u00f0') {
                return '\u1e28';
            }
            if (modifier1 == '\u00f2') {
                return '\u1e24';
            }
            if (modifier1 == '\u00f9') {
                return '\u1e2a';
            }
        }
        if (baseChar == 'I') {
            if (modifier1 == '\u00e0') {
                return '\u1ec8';
            }
            if (modifier1 == '\u00e1') {
                return '\u00cc';
            }
            if (modifier1 == '\u00e2') {
                return '\u00cd';
            }
            if (modifier1 == '\u00e3') {
                return '\u00ce';
            }
            if (modifier1 == '\u00e4') {
                return '\u0128';
            }
            if (modifier1 == '\u00e5') {
                return '\u012a';
            }
            if (modifier1 == '\u00e6') {
                return '\u012c';
            }
            if (modifier1 == '\u00e7') {
                return '\u0130';
            }
            if (modifier1 == '\u00e8') {
                if (modifier2 == '\u00e2') {
                    return '\u1e2e';
                }
                return '\u00cf';
            }
            if (modifier1 == '\u00e9') {
                return '\u01cf';
            }
            if (modifier1 == '\u00f1') {
                return '\u012e';
            }
            if (modifier1 == '\u00f2') {
                return '\u1eca';
            }
        }
        if (baseChar == 'J' && modifier1 == '\u00e3') {
            return '\u0134';
        }
        if (baseChar == 'K') {
            if (modifier1 == '\u00e2') {
                return '\u1e30';
            }
            if (modifier1 == '\u00e9') {
                return '\u01e8';
            }
            if (modifier1 == '\u00f0') {
                return '\u0136';
            }
            if (modifier1 == '\u00f2') {
                return '\u1e32';
            }
        }
        if (baseChar == 'L') {
            if (modifier1 == '\u00e2') {
                return '\u0139';
            }
            if (modifier1 == '\u00e9') {
                return '\u013d';
            }
            if (modifier1 == '\u00f0') {
                return '\u013b';
            }
            if (modifier1 == '\u00f2') {
                if (modifier2 == '\u00e5') {
                    return '\u1e38';
                }
                return '\u1e36';
            }
        }
        if (baseChar == 'M') {
            if (modifier1 == '\u00e2') {
                return '\u1e3e';
            }
            if (modifier1 == '\u00e7') {
                return '\u1e40';
            }
            if (modifier1 == '\u00f2') {
                return '\u1e42';
            }
        }
        if (baseChar == 'N') {
            if (modifier1 == '\u00e1') {
                return '\u01f8';
            }
            if (modifier1 == '\u00e2') {
                return '\u0143';
            }
            if (modifier1 == '\u00e4') {
                return '\u00d1';
            }
            if (modifier1 == '\u00e7') {
                return '\u1e44';
            }
            if (modifier1 == '\u00e9') {
                return '\u0147';
            }
            if (modifier1 == '\u00f0') {
                return '\u0145';
            }
            if (modifier1 == '\u00f2') {
                return '\u1e46';
            }
        }
        if (baseChar == 'O') {
            if (modifier1 == '\u00e0') {
                return '\u1ece';
            }
            if (modifier1 == '\u00e1') {
                return '\u00d2';
            }
            if (modifier1 == '\u00e2') {
                return '\u00d3';
            }
            if (modifier1 == '\u00e3') {
                if (modifier2 == '\u00e0') {
                    return '\u1ed4';
                }
                if (modifier2 == '\u00e1') {
                    return '\u1ed2';
                }
                if (modifier2 == '\u00e2') {
                    return '\u1ed0';
                }
                if (modifier2 == '\u00e4') {
                    return '\u1ed6';
                }
                if (modifier2 == '\u00f2') {
                    return '\u1ed8';
                }
                return '\u00d4';
            }
            if (modifier1 == '\u00e4') {
                if (modifier2 == '\u00e2') {
                    return '\u1e4c';
                }
                if (modifier2 == '\u00e5') {
                    return '\u022c';
                }
                if (modifier2 == '\u00e8') {
                    return '\u1e4e';
                }
                return '\u00d5';
            }
            if (modifier1 == '\u00e5') {
                if (modifier2 == '\u00e1') {
                    return '\u1e50';
                }
                if (modifier2 == '\u00e2') {
                    return '\u1e52';
                }
                return '\u014c';
            }
            if (modifier1 == '\u00e6') {
                return '\u014e';
            }
            if (modifier1 == '\u00e7') {
                if (modifier2 == '\u00e5') {
                    return '\u0230';
                }
                return '\u022e';
            }
            if (modifier1 == '\u00e8') {
                if (modifier2 == '\u00e5') {
                    return '\u022a';
                }
                return '\u00d6';
            }
            if (modifier1 == '\u00e9') {
                return '\u01d1';
            }
            if (modifier1 == '\u00ee') {
                return '\u0150';
            }
            if (modifier1 == '\u00f1') {
                if (modifier2 == '\u00e5') {
                    return '\u01ec';
                }
                return '\u01ea';
            }
            if (modifier1 == '\u00f2') {
                return '\u1ecc';
            }
        }
        if (baseChar == 'P') {
            if (modifier1 == '\u00e2') {
                return '\u1e54';
            }
            if (modifier1 == '\u00e7') {
                return '\u1e56';
            }
        }
        if (baseChar == 'R') {
            if (modifier1 == '\u00e2') {
                return '\u0154';
            }
            if (modifier1 == '\u00e7') {
                return '\u1e58';
            }
            if (modifier1 == '\u00e9') {
                return '\u0158';
            }
            if (modifier1 == '\u00f0') {
                return '\u0156';
            }
            if (modifier1 == '\u00f2') {
                if (modifier2 == '\u00e5') {
                    return '\u1e5c';
                }
                return '\u1e5a';
            }
        }
        if (baseChar == 'S') {
            if (modifier1 == '\u00e2') {
                if (modifier2 == '\u00e7') {
                    return '\u1e64';
                }
                return '\u015a';
            }
            if (modifier1 == '\u00e3') {
                return '\u015c';
            }
            if (modifier1 == '\u00e7') {
                return '\u1e60';
            }
            if (modifier1 == '\u00e9') {
                if (modifier2 == '\u00e7') {
                    return '\u1e66';
                }
                return '\u0160';
            }
            if (modifier1 == '\u00f0') {
                return '\u015e';
            }
            if (modifier1 == '\u00f2') {
                if (modifier2 == '\u00e7') {
                    return '\u1e68';
                }
                return '\u1e62';
            }
            if (modifier1 == '\u00f7') {
                return '\u0218';
            }
        }
        if (baseChar == 'T') {
            if (modifier1 == '\u00e7') {
                return '\u1e6a';
            }
            if (modifier1 == '\u00e9') {
                return '\u0164';
            }
            if (modifier1 == '\u00f0') {
                return '\u0162';
            }
            if (modifier1 == '\u00f2') {
                return '\u1e6c';
            }
            if (modifier1 == '\u00f7') {
                return '\u021a';
            }
        }
        if (baseChar == 'U') {
            if (modifier1 == '\u00e0') {
                return '\u1ee6';
            }
            if (modifier1 == '\u00e1') {
                return '\u00d9';
            }
            if (modifier1 == '\u00e2') {
                return '\u00da';
            }
            if (modifier1 == '\u00e3') {
                return '\u00db';
            }
            if (modifier1 == '\u00e4') {
                if (modifier2 == '\u00e2') {
                    return '\u1e78';
                }
                return '\u0168';
            }
            if (modifier1 == '\u00e5') {
                if (modifier2 == '\u00e8') {
                    return '\u1e7a';
                }
                return '\u016a';
            }
            if (modifier1 == '\u00e6') {
                return '\u016c';
            }
            if (modifier1 == '\u00e8') {
                if (modifier2 == '\u00e1') {
                    return '\u01db';
                }
                if (modifier2 == '\u00e2') {
                    return '\u01d7';
                }
                if (modifier2 == '\u00e5') {
                    return '\u01d5';
                }
                if (modifier2 == '\u00e9') {
                    return '\u01d9';
                }
                return '\u00dc';
            }
            if (modifier1 == '\u00e9') {
                return '\u01d3';
            }
            if (modifier1 == '\u00ea') {
                return '\u016e';
            }
            if (modifier1 == '\u00ee') {
                return '\u0170';
            }
            if (modifier1 == '\u00f1') {
                return '\u0172';
            }
            if (modifier1 == '\u00f2') {
                return '\u1ee4';
            }
            if (modifier1 == '\u00f3') {
                return '\u1e72';
            }
        }
        if (baseChar == 'V') {
            if (modifier1 == '\u00e4') {
                return '\u1e7c';
            }
            if (modifier1 == '\u00f2') {
                return '\u1e7e';
            }
        }
        if (baseChar == 'W') {
            if (modifier1 == '\u00e1') {
                return '\u1e80';
            }
            if (modifier1 == '\u00e2') {
                return '\u1e82';
            }
            if (modifier1 == '\u00e3') {
                return '\u0174';
            }
            if (modifier1 == '\u00e7') {
                return '\u1e86';
            }
            if (modifier1 == '\u00e8') {
                return '\u1e84';
            }
            if (modifier1 == '\u00f2') {
                return '\u1e88';
            }
        }
        if (baseChar == 'X') {
            if (modifier1 == '\u00e7') {
                return '\u1e8a';
            }
            if (modifier1 == '\u00e8') {
                return '\u1e8c';
            }
        }
        if (baseChar == 'Y') {
            if (modifier1 == '\u00e0') {
                return '\u1ef6';
            }
            if (modifier1 == '\u00e1') {
                return '\u1ef2';
            }
            if (modifier1 == '\u00e2') {
                return '\u00dd';
            }
            if (modifier1 == '\u00e3') {
                return '\u0176';
            }
            if (modifier1 == '\u00e4') {
                return '\u1ef8';
            }
            if (modifier1 == '\u00e5') {
                return '\u0232';
            }
            if (modifier1 == '\u00e7') {
                return '\u1e8e';
            }
            if (modifier1 == '\u00e8') {
                return '\u0178';
            }
            if (modifier1 == '\u00f2') {
                return '\u1ef4';
            }
        }
        if (baseChar == 'Z') {
            if (modifier1 == '\u00e2') {
                return '\u0179';
            }
            if (modifier1 == '\u00e3') {
                return '\u1e90';
            }
            if (modifier1 == '\u00e7') {
                return '\u017b';
            }
            if (modifier1 == '\u00e9') {
                return '\u017d';
            }
            if (modifier1 == '\u00f2') {
                return '\u1e92';
            }
        }
        if (baseChar == 'a') {
            if (modifier1 == '\u00e0') {
                return '\u1ea3';
            }
            if (modifier1 == '\u00e1') {
                return '\u00e0';
            }
            if (modifier1 == '\u00e2') {
                return '\u00e1';
            }
            if (modifier1 == '\u00e3') {
                if (modifier2 == '\u00e0') {
                    return '\u1ea9';
                }
                if (modifier2 == '\u00e1') {
                    return '\u1ea7';
                }
                if (modifier2 == '\u00e2') {
                    return '\u1ea5';
                }
                if (modifier2 == '\u00e4') {
                    return '\u1eab';
                }
                if (modifier2 == '\u00f2') {
                    return '\u1ead';
                }
                return '\u00e2';
            }
            if (modifier1 == '\u00e4') {
                return '\u00e3';
            }
            if (modifier1 == '\u00e5') {
                return '\u0101';
            }
            if (modifier1 == '\u00e6') {
                if (modifier2 == '\u00e0') {
                    return '\u1eb3';
                }
                if (modifier2 == '\u00e1') {
                    return '\u1eb1';
                }
                if (modifier2 == '\u00e2') {
                    return '\u1eaf';
                }
                if (modifier2 == '\u00e4') {
                    return '\u1eb5';
                }
                if (modifier2 == '\u00f2') {
                    return '\u1eb7';
                }
                return '\u0103';
            }
            if (modifier1 == '\u00e7') {
                if (modifier2 == '\u00e5') {
                    return '\u01e1';
                }
                return '\u0227';
            }
            if (modifier1 == '\u00e8') {
                if (modifier2 == '\u00e5') {
                    return '\u01df';
                }
                return '\u00e4';
            }
            if (modifier1 == '\u00e9') {
                return '\u01ce';
            }
            if (modifier1 == '\u00ea') {
                if (modifier2 == '\u00e2') {
                    return '\u01fb';
                }
                return '\u00e5';
            }
            if (modifier1 == '\u00f1') {
                return '\u0105';
            }
            if (modifier1 == '\u00f2') {
                return '\u1ea1';
            }
            if (modifier1 == '\u00f4') {
                return '\u1e01';
            }
        }
        if (baseChar == 'b') {
            if (modifier1 == '\u00e7') {
                return '\u1e03';
            }
            if (modifier1 == '\u00f2') {
                return '\u1e05';
            }
        }
        if (baseChar == 'c') {
            if (modifier1 == '\u00e2') {
                return '\u0107';
            }
            if (modifier1 == '\u00e3') {
                return '\u0109';
            }
            if (modifier1 == '\u00e7') {
                return '\u010b';
            }
            if (modifier1 == '\u00e9') {
                return '\u010d';
            }
            if (modifier1 == '\u00f0') {
                if (modifier2 == '\u00e2') {
                    return '\u1e09';
                }
                return '\u00e7';
            }
        }
        if (baseChar == 'd') {
            if (modifier1 == '\u00e7') {
                return '\u1e0b';
            }
            if (modifier1 == '\u00e9') {
                return '\u010f';
            }
            if (modifier1 == '\u00f0') {
                return '\u1e11';
            }
            if (modifier1 == '\u00f2') {
                return '\u1e0d';
            }
        }
        if (baseChar == 'e') {
            if (modifier1 == '\u00e0') {
                return '\u1ebb';
            }
            if (modifier1 == '\u00e1') {
                return '\u00e8';
            }
            if (modifier1 == '\u00e2') {
                return '\u00e9';
            }
            if (modifier1 == '\u00e3') {
                if (modifier2 == '\u00e0') {
                    return '\u1ec3';
                }
                if (modifier2 == '\u00e1') {
                    return '\u1ec1';
                }
                if (modifier2 == '\u00e2') {
                    return '\u1ebf';
                }
                if (modifier2 == '\u00e4') {
                    return '\u1ec5';
                }
                if (modifier2 == '\u00f2') {
                    return '\u1ec7';
                }
                return '\u00ea';
            }
            if (modifier1 == '\u00e4') {
                return '\u1ebd';
            }
            if (modifier1 == '\u00e5') {
                if (modifier2 == '\u00e1') {
                    return '\u1e15';
                }
                if (modifier2 == '\u00e2') {
                    return '\u1e17';
                }
                return '\u0113';
            }
            if (modifier1 == '\u00e6') {
                return '\u0115';
            }
            if (modifier1 == '\u00e7') {
                return '\u0117';
            }
            if (modifier1 == '\u00e8') {
                return '\u00eb';
            }
            if (modifier1 == '\u00e9') {
                return '\u011b';
            }
            if (modifier1 == '\u00f0') {
                if (modifier2 == '\u00e6') {
                    return '\u1e1d';
                }
                return '\u0229';
            }
            if (modifier1 == '\u00f1') {
                return '\u0119';
            }
            if (modifier1 == '\u00f2') {
                return '\u1eb9';
            }
        }
        if (baseChar == 'f' && modifier1 == '\u00e7') {
            return '\u1e1f';
        }
        if (baseChar == 'g') {
            if (modifier1 == '\u00e2') {
                return '\u01f5';
            }
            if (modifier1 == '\u00e3') {
                return '\u011d';
            }
            if (modifier1 == '\u00e5') {
                return '\u1e21';
            }
            if (modifier1 == '\u00e6') {
                return '\u011f';
            }
            if (modifier1 == '\u00e7') {
                return '\u0121';
            }
            if (modifier1 == '\u00e9') {
                return '\u01e7';
            }
            if (modifier1 == '\u00f0') {
                return '\u0123';
            }
        }
        if (baseChar == 'h') {
            if (modifier1 == '\u00e3') {
                return '\u0125';
            }
            if (modifier1 == '\u00e7') {
                return '\u1e23';
            }
            if (modifier1 == '\u00e8') {
                return '\u1e27';
            }
            if (modifier1 == '\u00e9') {
                return '\u021f';
            }
            if (modifier1 == '\u00f0') {
                return '\u1e29';
            }
            if (modifier1 == '\u00f2') {
                return '\u1e25';
            }
            if (modifier1 == '\u00f9') {
                return '\u1e2b';
            }
        }
        if (baseChar == 'i') {
            if (modifier1 == '\u00e0') {
                return '\u1ec9';
            }
            if (modifier1 == '\u00e1') {
                return '\u00ec';
            }
            if (modifier1 == '\u00e2') {
                return '\u00ed';
            }
            if (modifier1 == '\u00e3') {
                return '\u00ee';
            }
            if (modifier1 == '\u00e4') {
                return '\u0129';
            }
            if (modifier1 == '\u00e5') {
                return '\u012b';
            }
            if (modifier1 == '\u00e6') {
                return '\u012d';
            }
            if (modifier1 == '\u00e8') {
                if (modifier2 == '\u00e2') {
                    return '\u1e2f';
                }
                return '\u00ef';
            }
            if (modifier1 == '\u00e9') {
                return '\u01d0';
            }
            if (modifier1 == '\u00f1') {
                return '\u012f';
            }
            if (modifier1 == '\u00f2') {
                return '\u1ecb';
            }
        }
        if (baseChar == 'j') {
            if (modifier1 == '\u00e3') {
                return '\u0135';
            }
            if (modifier1 == '\u00e9') {
                return '\u01f0';
            }
        }
        if (baseChar == 'k') {
            if (modifier1 == '\u00e2') {
                return '\u1e31';
            }
            if (modifier1 == '\u00e9') {
                return '\u01e9';
            }
            if (modifier1 == '\u00f0') {
                return '\u0137';
            }
            if (modifier1 == '\u00f2') {
                return '\u1e33';
            }
        }
        if (baseChar == 'l') {
            if (modifier1 == '\u00e2') {
                return '\u013a';
            }
            if (modifier1 == '\u00e9') {
                return '\u013e';
            }
            if (modifier1 == '\u00f0') {
                return '\u013c';
            }
            if (modifier1 == '\u00f2') {
                if (modifier2 == '\u00e5') {
                    return '\u1e39';
                }
                return '\u1e37';
            }
        }
        if (baseChar == 'm') {
            if (modifier1 == '\u00e2') {
                return '\u1e3f';
            }
            if (modifier1 == '\u00e7') {
                return '\u1e41';
            }
            if (modifier1 == '\u00f2') {
                return '\u1e43';
            }
        }
        if (baseChar == 'n') {
            if (modifier1 == '\u00e1') {
                return '\u01f9';
            }
            if (modifier1 == '\u00e2') {
                return '\u0144';
            }
            if (modifier1 == '\u00e4') {
                return '\u00f1';
            }
            if (modifier1 == '\u00e7') {
                return '\u1e45';
            }
            if (modifier1 == '\u00e9') {
                return '\u0148';
            }
            if (modifier1 == '\u00f0') {
                return '\u0146';
            }
            if (modifier1 == '\u00f2') {
                return '\u1e47';
            }
        }
        if (baseChar == 'o') {
            if (modifier1 == '\u00e0') {
                return '\u1ecf';
            }
            if (modifier1 == '\u00e1') {
                return '\u00f2';
            }
            if (modifier1 == '\u00e2') {
                return '\u00f3';
            }
            if (modifier1 == '\u00e3') {
                if (modifier2 == '\u00e0') {
                    return '\u1ed5';
                }
                if (modifier2 == '\u00e1') {
                    return '\u1ed3';
                }
                if (modifier2 == '\u00e2') {
                    return '\u1ed1';
                }
                if (modifier2 == '\u00e4') {
                    return '\u1ed7';
                }
                if (modifier2 == '\u00f2') {
                    return '\u1ed9';
                }
                return '\u00f4';
            }
            if (modifier1 == '\u00e4') {
                if (modifier2 == '\u00e2') {
                    return '\u1e4d';
                }
                if (modifier2 == '\u00e5') {
                    return '\u022d';
                }
                if (modifier2 == '\u00e8') {
                    return '\u1e4f';
                }
                return '\u00f5';
            }
            if (modifier1 == '\u00e5') {
                if (modifier2 == '\u00e1') {
                    return '\u1e51';
                }
                if (modifier2 == '\u00e2') {
                    return '\u1e53';
                }
                return '\u014d';
            }
            if (modifier1 == '\u00e6') {
                return '\u014f';
            }
            if (modifier1 == '\u00e7') {
                if (modifier2 == '\u00e5') {
                    return '\u0231';
                }
                return '\u022f';
            }
            if (modifier1 == '\u00e8') {
                if (modifier2 == '\u00e5') {
                    return '\u022b';
                }
                return '\u00f6';
            }
            if (modifier1 == '\u00e9') {
                return '\u01d2';
            }
            if (modifier1 == '\u00ee') {
                return '\u0151';
            }
            if (modifier1 == '\u00f1') {
                if (modifier2 == '\u00e5') {
                    return '\u01ed';
                }
                return '\u01eb';
            }
            if (modifier1 == '\u00f2') {
                return '\u1ecd';
            }
        }
        if (baseChar == 'p') {
            if (modifier1 == '\u00e2') {
                return '\u1e55';
            }
            if (modifier1 == '\u00e7') {
                return '\u1e57';
            }
        }
        if (baseChar == 'r') {
            if (modifier1 == '\u00e2') {
                return '\u0155';
            }
            if (modifier1 == '\u00e7') {
                return '\u1e59';
            }
            if (modifier1 == '\u00e9') {
                return '\u0159';
            }
            if (modifier1 == '\u00f0') {
                return '\u0157';
            }
            if (modifier1 == '\u00f2') {
                if (modifier2 == '\u00e5') {
                    return '\u1e5d';
                }
                return '\u1e5b';
            }
        }
        if (baseChar == 's') {
            if (modifier1 == '\u00e2') {
                if (modifier2 == '\u00e7') {
                    return '\u1e65';
                }
                return '\u015b';
            }
            if (modifier1 == '\u00e3') {
                return '\u015d';
            }
            if (modifier1 == '\u00e7') {
                return '\u1e61';
            }
            if (modifier1 == '\u00e9') {
                if (modifier2 == '\u00e7') {
                    return '\u1e67';
                }
                return '\u0161';
            }
            if (modifier1 == '\u00f0') {
                return '\u015f';
            }
            if (modifier1 == '\u00f2') {
                if (modifier2 == '\u00e7') {
                    return '\u1e69';
                }
                return '\u1e63';
            }
            if (modifier1 == '\u00f7') {
                return '\u0219';
            }
        }
        if (baseChar == 't') {
            if (modifier1 == '\u00e7') {
                return '\u1e6b';
            }
            if (modifier1 == '\u00e8') {
                return '\u1e97';
            }
            if (modifier1 == '\u00e9') {
                return '\u0165';
            }
            if (modifier1 == '\u00f0') {
                return '\u0163';
            }
            if (modifier1 == '\u00f2') {
                return '\u1e6d';
            }
            if (modifier1 == '\u00f7') {
                return '\u021b';
            }
        }
        if (baseChar == 'u') {
            if (modifier1 == '\u00e0') {
                return '\u1ee7';
            }
            if (modifier1 == '\u00e1') {
                return '\u00f9';
            }
            if (modifier1 == '\u00e2') {
                return '\u00fa';
            }
            if (modifier1 == '\u00e3') {
                return '\u00fb';
            }
            if (modifier1 == '\u00e4') {
                if (modifier2 == '\u00e2') {
                    return '\u1e79';
                }
                return '\u0169';
            }
            if (modifier1 == '\u00e5') {
                if (modifier2 == '\u00e8') {
                    return '\u1e7b';
                }
                return '\u016b';
            }
            if (modifier1 == '\u00e6') {
                return '\u016d';
            }
            if (modifier1 == '\u00e8') {
                if (modifier2 == '\u00e1') {
                    return '\u01dc';
                }
                if (modifier2 == '\u00e2') {
                    return '\u01d8';
                }
                if (modifier2 == '\u00e5') {
                    return '\u01d6';
                }
                if (modifier2 == '\u00e9') {
                    return '\u01da';
                }
                return '\u00fc';
            }
            if (modifier1 == '\u00e9') {
                return '\u01d4';
            }
            if (modifier1 == '\u00ea') {
                return '\u016f';
            }
            if (modifier1 == '\u00ee') {
                return '\u0171';
            }
            if (modifier1 == '\u00f1') {
                return '\u0173';
            }
            if (modifier1 == '\u00f2') {
                return '\u1ee5';
            }
            if (modifier1 == '\u00f3') {
                return '\u1e73';
            }
        }
        if (baseChar == 'v') {
            if (modifier1 == '\u00e4') {
                return '\u1e7d';
            }
            if (modifier1 == '\u00f2') {
                return '\u1e7f';
            }
        }
        if (baseChar == 'w') {
            if (modifier1 == '\u00e1') {
                return '\u1e81';
            }
            if (modifier1 == '\u00e2') {
                return '\u1e83';
            }
            if (modifier1 == '\u00e3') {
                return '\u0175';
            }
            if (modifier1 == '\u00e7') {
                return '\u1e87';
            }
            if (modifier1 == '\u00e8') {
                return '\u1e85';
            }
            if (modifier1 == '\u00ea') {
                return '\u1e98';
            }
            if (modifier1 == '\u00f2') {
                return '\u1e89';
            }
        }
        if (baseChar == 'x') {
            if (modifier1 == '\u00e7') {
                return '\u1e8b';
            }
            if (modifier1 == '\u00e8') {
                return '\u1e8d';
            }
        }
        if (baseChar == 'y') {
            if (modifier1 == '\u00e0') {
                return '\u1ef7';
            }
            if (modifier1 == '\u00e1') {
                return '\u1ef3';
            }
            if (modifier1 == '\u00e2') {
                return '\u00fd';
            }
            if (modifier1 == '\u00e3') {
                return '\u0177';
            }
            if (modifier1 == '\u00e4') {
                return '\u1ef9';
            }
            if (modifier1 == '\u00e5') {
                return '\u0233';
            }
            if (modifier1 == '\u00e7') {
                return '\u1e8f';
            }
            if (modifier1 == '\u00e8') {
                return '\u00ff';
            }
            if (modifier1 == '\u00ea') {
                return '\u1e99';
            }
            if (modifier1 == '\u00f2') {
                return '\u1ef5';
            }
        }
        if (baseChar == 'z') {
            if (modifier1 == '\u00e2') {
                return '\u017a';
            }
            if (modifier1 == '\u00e3') {
                return '\u1e91';
            }
            if (modifier1 == '\u00e7') {
                return '\u017c';
            }
            if (modifier1 == '\u00e9') {
                return '\u017e';
            }
            if (modifier1 == '\u00f2') {
                return '\u1e93';
            }
        }
        return '\u0000';
    }
}

