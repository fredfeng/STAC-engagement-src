/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.io.encoding;

final class AnselMapping {
    public static char decode(int b) {
        if (b < 128) {
            return (char)b;
        }
        switch (b) {
            case 161: {
                return '\u0141';
            }
            case 162: {
                return '\u00d8';
            }
            case 163: {
                return '\u0110';
            }
            case 164: {
                return '\u00de';
            }
            case 165: {
                return '\u00c6';
            }
            case 166: {
                return '\u0152';
            }
            case 167: {
                return '\u02b9';
            }
            case 168: {
                return '\u00b7';
            }
            case 169: {
                return '\u266d';
            }
            case 170: {
                return '\u00ae';
            }
            case 171: {
                return '\u00b1';
            }
            case 172: {
                return '\u01a0';
            }
            case 173: {
                return '\u01af';
            }
            case 174: {
                return '\u02bc';
            }
            case 176: {
                return '\u02bb';
            }
            case 177: {
                return '\u0142';
            }
            case 178: {
                return '\u00f8';
            }
            case 179: {
                return '\u0111';
            }
            case 180: {
                return '\u00fe';
            }
            case 181: {
                return '\u00e6';
            }
            case 182: {
                return '\u0153';
            }
            case 183: {
                return '\u02ba';
            }
            case 184: {
                return '\u0131';
            }
            case 185: {
                return '\u00a3';
            }
            case 186: {
                return '\u00f0';
            }
            case 188: {
                return '\u01a1';
            }
            case 189: {
                return '\u01b0';
            }
            case 190: {
                return '\u25a1';
            }
            case 191: {
                return '\u25a0';
            }
            case 192: {
                return '\u00b0';
            }
            case 193: {
                return '\u2113';
            }
            case 194: {
                return '\u2117';
            }
            case 195: {
                return '\u00a9';
            }
            case 196: {
                return '\u266f';
            }
            case 197: {
                return '\u00bf';
            }
            case 198: {
                return '\u00a1';
            }
            case 200: {
                return '\u20ac';
            }
            case 205: {
                return 'e';
            }
            case 206: {
                return 'o';
            }
            case 207: {
                return '\u00df';
            }
            case 224: {
                return '\u0309';
            }
            case 225: {
                return '\u0300';
            }
            case 226: {
                return '\u0301';
            }
            case 227: {
                return '\u0302';
            }
            case 228: {
                return '\u0303';
            }
            case 229: {
                return '\u0304';
            }
            case 230: {
                return '\u0306';
            }
            case 231: {
                return '\u0307';
            }
            case 232: {
                return '\u0308';
            }
            case 233: {
                return '\u030c';
            }
            case 234: {
                return '\u030a';
            }
            case 235: {
                return '\ufe20';
            }
            case 236: {
                return '\ufe21';
            }
            case 237: {
                return '\u0315';
            }
            case 238: {
                return '\u030b';
            }
            case 239: {
                return '\u0310';
            }
            case 240: {
                return '\u0327';
            }
            case 241: {
                return '\u0328';
            }
            case 242: {
                return '\u0323';
            }
            case 243: {
                return '\u0324';
            }
            case 244: {
                return '\u0325';
            }
            case 245: {
                return '\u0333';
            }
            case 246: {
                return '\u0332';
            }
            case 247: {
                return '\u0326';
            }
            case 248: {
                return '\u031c';
            }
            case 249: {
                return '\u032e';
            }
            case 250: {
                return '\ufe22';
            }
            case 251: {
                return '\ufe23';
            }
            case 252: {
                return '\u0338';
            }
            case 254: {
                return '\u0313';
            }
        }
        return '?';
    }

    public static char encode(char c) {
        switch (c) {
            case 'e': {
                return '\u00cd';
            }
            case '\u00a1': {
                return '\u00c6';
            }
            case '\u00a3': {
                return '\u00b9';
            }
            case '\u00a9': {
                return '\u00c3';
            }
            case '\u00ae': {
                return '\u00aa';
            }
            case '\u00b0': {
                return '\u00c0';
            }
            case '\u00b1': {
                return '\u00ab';
            }
            case '\u00b7': {
                return '\u00a8';
            }
            case '\u00bf': {
                return '\u00c5';
            }
            case '\u00c6': {
                return '\u00a5';
            }
            case '\u00d8': {
                return '\u00a2';
            }
            case '\u00de': {
                return '\u00a4';
            }
            case '\u00df': {
                return '\u00cf';
            }
            case '\u00e6': {
                return '\u00b5';
            }
            case 'o': {
                return '\u00ce';
            }
            case '\u00f0': {
                return '\u00ba';
            }
            case '\u00f8': {
                return '\u00b2';
            }
            case '\u00fe': {
                return '\u00b4';
            }
            case '\u0110': {
                return '\u00a3';
            }
            case '\u01b0': {
                return '\u00bd';
            }
            case '\u0111': {
                return '\u00b3';
            }
            case '\u0131': {
                return '\u00b8';
            }
            case '\u0141': {
                return '\u00a1';
            }
            case '\u0142': {
                return '\u00b1';
            }
            case '\u0152': {
                return '\u00a6';
            }
            case '\u0153': {
                return '\u00b6';
            }
            case '\u01a0': {
                return '\u00ac';
            }
            case '\u01a1': {
                return '\u00bc';
            }
            case '\u01af': {
                return '\u00ad';
            }
            case '\u02b9': {
                return '\u00a7';
            }
            case '\u02ba': {
                return '\u00b7';
            }
            case '\u02bb': {
                return '\u00b0';
            }
            case '\u02bc': {
                return '\u00ae';
            }
            case '\u0300': {
                return '\u00e1';
            }
            case '\u0301': {
                return '\u00e2';
            }
            case '\u0302': {
                return '\u00e3';
            }
            case '\u0303': {
                return '\u00e4';
            }
            case '\u0304': {
                return '\u00e5';
            }
            case '\u0306': {
                return '\u00e6';
            }
            case '\u0307': {
                return '\u00e7';
            }
            case '\u0308': {
                return '\u00e8';
            }
            case '\u0309': {
                return '\u00e0';
            }
            case '\u030a': {
                return '\u00ea';
            }
            case '\u030b': {
                return '\u00ee';
            }
            case '\u030c': {
                return '\u00e9';
            }
            case '\u0310': {
                return '\u00ef';
            }
            case '\u0313': {
                return '\u00fe';
            }
            case '\u0315': {
                return '\u00ed';
            }
            case '\u031c': {
                return '\u00f8';
            }
            case '\u0323': {
                return '\u00f2';
            }
            case '\u0324': {
                return '\u00f3';
            }
            case '\u0325': {
                return '\u00f4';
            }
            case '\u0326': {
                return '\u00f7';
            }
            case '\u0327': {
                return '\u00f0';
            }
            case '\u0328': {
                return '\u00f1';
            }
            case '\u032e': {
                return '\u00f9';
            }
            case '\u0332': {
                return '\u00f6';
            }
            case '\u0333': {
                return '\u00f5';
            }
            case '\u0338': {
                return '\u00fc';
            }
            case '\u20ac': {
                return '\u00c8';
            }
            case '\u2113': {
                return '\u00c1';
            }
            case '\u2117': {
                return '\u00c2';
            }
            case '\u25a0': {
                return '\u00bf';
            }
            case '\u25a1': {
                return '\u00be';
            }
            case '\u266d': {
                return '\u00a9';
            }
            case '\u266f': {
                return '\u00c4';
            }
            case '\ufe20': {
                return '\u00eb';
            }
            case '\ufe21': {
                return '\u00ec';
            }
            case '\ufe22': {
                return '\u00fa';
            }
            case '\ufe23': {
                return '\u00fb';
            }
        }
        return c;
    }

    public static boolean isUnicodeCombiningDiacritic(char c) {
        return c >= '\u0300' && c <= '\u0333' || c >= '\ufe20' && c <= '\ufe23';
    }

    private AnselMapping() {
    }
}

