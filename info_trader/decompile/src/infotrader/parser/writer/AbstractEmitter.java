/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.writer;

import infotrader.parser.exception.InfoTraderWriterException;
import infotrader.parser.exception.WriterCancelledException;
import infotrader.parser.model.Header;
import infotrader.parser.model.InfoTrader;
import infotrader.parser.model.InfoTraderVersion;
import infotrader.parser.model.StringTree;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.model.SupportedVersion;
import infotrader.parser.writer.InfoTraderWriter;
import java.util.ArrayList;
import java.util.List;

abstract class AbstractEmitter<T> {
    private static final int MAX_LINE_LENGTH = 128;
    protected int startLevel;
    protected final T writeFrom;
    protected InfoTraderWriter baseWriter;

    protected AbstractEmitter(InfoTraderWriter baseWriter, int startLevel, T writeFrom) throws WriterCancelledException {
        this.baseWriter = baseWriter;
        this.startLevel = startLevel;
        this.writeFrom = writeFrom;
        if (baseWriter != null) {
            baseWriter.notifyConstructObserversIfNeeded();
            if (baseWriter.isCancelled()) {
                throw new WriterCancelledException("Construction of InfoTrader data cancelled");
            }
        }
    }

    protected abstract void emit() throws InfoTraderWriterException;

    protected void emitLinesOfText(int level, String startingTag, List<String> linesOfText) {
        this.emitLinesOfText(level, null, startingTag, linesOfText);
    }

    protected void emitLinesOfText(int level, String xref, String startingTag, List<String> linesOfText) {
        List<String> splitLinesOfText = this.splitLinesOnBreakingCharacters(linesOfText);
        int lineNum = 0;
        for (String l : splitLinesOfText) {
            StringBuilder line = new StringBuilder();
            if (lineNum == 0) {
                line.append(level).append(" ");
                if (xref != null && xref.length() > 0) {
                    line.append(xref).append(" ");
                }
                line.append(startingTag).append(" ").append(l);
            } else {
                line.append(level + 1).append(" ");
                if (xref != null && xref.length() > 0) {
                    line.append(xref).append(" ");
                }
                line.append("CONT ").append(l);
            }
            ++lineNum;
            this.emitAndSplit(level, line.toString());
        }
    }

    protected void emitStringsWithCustomTags(int level, List<StringWithCustomTags> strings, String tagValue) throws InfoTraderWriterException {
        if (strings != null) {
            for (StringWithCustomTags f : strings) {
                this.emitTagWithRequiredValue(level, tagValue, f);
            }
        }
    }

    protected void emitTag(int level, String tag) {
        this.baseWriter.lines.add("" + level + " " + tag);
    }

    protected void emitTag(int level, String xref, String tag) {
        StringBuilder line = new StringBuilder(Integer.toString(level));
        if (xref != null && xref.length() > 0) {
            line.append(" ").append(xref);
        }
        line.append(" ").append(tag);
        this.baseWriter.lines.add(line.toString());
    }

    protected void emitTagIfValueNotNull(int level, String tag, Object value) {
        this.emitTagIfValueNotNull(level, null, tag, value);
    }

    protected void emitTagWithOptionalValue(int level, String tag, String value) throws InfoTraderWriterException {
        if (value == null) {
            StringBuilder line = new StringBuilder(Integer.toString(level));
            line.append(" ").append(tag);
            this.baseWriter.lines.add(line.toString());
        } else {
            ArrayList<String> temp = new ArrayList<String>();
            temp.add(value);
            List<String> valueLines = this.splitLinesOnBreakingCharacters(temp);
            boolean first = true;
            for (String v : valueLines) {
                StringBuilder line = new StringBuilder();
                if (first) {
                    line.append(level);
                    line.append(" ").append(tag).append(" ").append(v);
                    this.emitAndSplit(level, line.toString());
                } else {
                    line.append(level + 1);
                    line.append(" CONT ").append(v);
                    this.emitAndSplit(level + 1, line.toString());
                }
                first = false;
            }
        }
    }

    protected void emitTagWithOptionalValueAndCustomSubtags(int level, String tag, StringWithCustomTags valueToRightOfTag) throws InfoTraderWriterException {
        if (valueToRightOfTag == null || valueToRightOfTag.getValue() == null) {
            StringBuilder line = new StringBuilder(Integer.toString(level));
            line.append(" ").append(tag);
            this.baseWriter.lines.add(line.toString());
            if (valueToRightOfTag != null) {
                this.emitCustomTags(level + 1, valueToRightOfTag.getCustomTags());
            }
            return;
        }
        ArrayList<String> temp = new ArrayList<String>();
        temp.add(valueToRightOfTag.getValue());
        List<String> valueLines = this.splitLinesOnBreakingCharacters(temp);
        boolean first = true;
        for (String v : valueLines) {
            StringBuilder line = new StringBuilder();
            if (first) {
                line.append(level);
                line.append(" ").append(tag).append(" ").append(v);
                this.emitAndSplit(level, line.toString());
            } else {
                line.append(level + 1);
                line.append(" CONT ").append(v);
                this.emitAndSplit(level + 1, line.toString());
            }
            first = false;
        }
        this.emitCustomTags(level + 1, valueToRightOfTag.getCustomTags());
    }

    protected void emitTagWithRequiredValue(int level, String tag, String value) throws InfoTraderWriterException {
        this.emitTagWithRequiredValue(level, null, tag, new StringWithCustomTags(value));
    }

    protected void emitTagWithRequiredValue(int level, String tag, StringWithCustomTags value) throws InfoTraderWriterException {
        this.emitTagWithRequiredValue(level, null, tag, value);
    }

    protected boolean g55() {
        return this.baseWriter != null && ((InfoTrader)this.baseWriter.writeFrom).getHeader() != null && ((InfoTrader)this.baseWriter.writeFrom).getHeader().getInfoTraderVersion() != null && SupportedVersion.V5_5.equals((Object)((Object)((InfoTrader)this.baseWriter.writeFrom).getHeader().getInfoTraderVersion().getVersionNumber()));
    }

    void emitCustomTags(int level, List<StringTree> customTags) {
        if (customTags != null) {
            for (StringTree st : customTags) {
                StringBuilder line = new StringBuilder(Integer.toString(level));
                line.append(" ");
                if (st.getId() != null && st.getId().trim().length() > 0) {
                    line.append(st.getId()).append(" ");
                }
                line.append(st.getTag());
                if (st.getValue() != null && st.getValue().trim().length() > 0) {
                    line.append(" ").append(st.getValue());
                }
                this.baseWriter.lines.add(line.toString());
                this.emitCustomTags(level + 1, st.getChildren());
            }
        }
    }

    List<String> splitLinesOnBreakingCharacters(List<String> linesOfText) {
        ArrayList<String> result = new ArrayList<String>();
        if (linesOfText != null) {
            for (String s : linesOfText) {
                String[] pieces;
                for (String piece : pieces = s.split("(\r\n|\n\r|\r|\n)")) {
                    result.add(piece);
                }
            }
        }
        return result;
    }

    private void emitAndSplit(int level, String line) {
        if (line.length() <= 128) {
            this.baseWriter.lines.add(line);
        } else {
            this.baseWriter.lines.add(line.substring(0, 128));
            String remainder = line.substring(128);
            while (remainder.length() > 0) {
                if (remainder.length() > 128) {
                    this.baseWriter.lines.add("" + (level + 1) + " CONC " + remainder.substring(0, 128));
                    remainder = remainder.substring(128);
                    continue;
                }
                this.baseWriter.lines.add("" + (level + 1) + " CONC " + remainder);
                remainder = "";
            }
        }
    }

    private void emitTagIfValueNotNull(int level, String xref, String tag, Object value) {
        if (value != null) {
            ArrayList<String> temp = new ArrayList<String>();
            temp.add(value.toString());
            List<String> valueLines = this.splitLinesOnBreakingCharacters(temp);
            boolean first = true;
            for (String v : valueLines) {
                StringBuilder line = new StringBuilder();
                if (first) {
                    line.append(level);
                    if (xref != null && xref.length() > 0) {
                        line.append(" ").append(xref);
                    }
                    line.append(" ").append(tag).append(" ").append(v);
                    this.emitAndSplit(level, line.toString());
                } else {
                    line.append(level + 1);
                    line.append(" CONT ").append(v);
                    this.emitAndSplit(level + 1, line.toString());
                }
                first = false;
            }
        }
    }

    private void emitTagWithRequiredValue(int level, String xref, String tag, StringWithCustomTags e) throws InfoTraderWriterException {
        if (e == null || e.getValue() == null || e.getValue().trim().length() == 0) {
            throw new InfoTraderWriterException("Required value for tag " + tag + " at level " + level + " was null or blank");
        }
        ArrayList<String> temp = new ArrayList<String>();
        temp.add(e.getValue());
        List<String> valueLines = this.splitLinesOnBreakingCharacters(temp);
        boolean first = true;
        for (String v : valueLines) {
            StringBuilder line = new StringBuilder();
            if (first) {
                line.append(level);
                if (xref != null && xref.length() > 0) {
                    line.append(" ").append(xref);
                }
                line.append(" ").append(tag).append(" ").append(v);
                this.emitAndSplit(level, line.toString());
            } else {
                line.append(level + 1);
                line.append(" CONT ").append(v);
                this.emitAndSplit(level + 1, line.toString());
            }
            first = false;
        }
        this.emitCustomTags(level + 1, e.getCustomTags());
    }
}

