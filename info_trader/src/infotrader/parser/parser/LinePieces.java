/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.parser;

import infotrader.parser.exception.InfoTraderParserException;

class LinePieces {
    int level;
    String id;
    String tag;
    String remainder;
    private int currCharIdx;
    private final char[] chars;

    LinePieces(String lineToParse, int lineNum) throws InfoTraderParserException {
        this.chars = lineToParse.toCharArray();
        this.processLevel(lineNum);
        this.processXrefId();
        this.processTag();
        this.processRemainder();
    }

    private void processLevel(int lineNum) throws InfoTraderParserException {
        try {
            char c2 = this.chars[1];
            this.currCharIdx = -1;
            if (' ' == c2) {
                this.level = Character.getNumericValue(this.chars[0]);
                this.currCharIdx = 2;
            } else {
                this.level = Character.getNumericValue(this.chars[0]) * 10 + Character.getNumericValue(this.chars[1]);
                this.currCharIdx = 3;
            }
        }
        catch (NumberFormatException e) {
            throw new InfoTraderParserException("Line " + lineNum + " does not begin with a 1 or 2 digit number for the level followed by a space: " + new String(this.chars), e);
        }
        catch (IndexOutOfBoundsException e) {
            throw new InfoTraderParserException("Line " + lineNum + " does not begin with a 1 or 2 digit number for the level followed by a space: " + new String(this.chars), e);
        }
    }

    private void processRemainder() {
        if (this.currCharIdx < this.chars.length) {
            this.remainder = new String(this.chars, this.currCharIdx + 1, this.chars.length - this.currCharIdx - 1);
        }
    }

    private void processTag() {
        StringBuilder t = new StringBuilder();
        while (this.currCharIdx < this.chars.length && this.chars[this.currCharIdx] != ' ') {
            t.append(this.chars[this.currCharIdx++]);
        }
        if (t.length() > 0) {
            this.tag = t.toString().intern();
        }
    }

    private void processXrefId() {
        StringBuilder i = null;
        if ('@' == this.chars[this.currCharIdx]) {
            while (this.currCharIdx < this.chars.length && this.chars[this.currCharIdx] != ' ') {
                if (i == null) {
                    i = new StringBuilder();
                }
                i.append(this.chars[this.currCharIdx++]);
            }
            ++this.currCharIdx;
        }
        if (i != null) {
            this.id = i.toString().intern();
        }
    }
}

