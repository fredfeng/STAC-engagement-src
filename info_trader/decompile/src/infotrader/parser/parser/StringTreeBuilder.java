/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.parser;

import infotrader.parser.exception.InfoTraderParserException;
import infotrader.parser.model.StringTree;
import infotrader.parser.parser.InfoTraderParser;
import infotrader.parser.parser.LinePieces;
import infotrader.parser.parser.StringCanonicalizer;
import infotrader.parser.parser.Tag;
import java.util.Arrays;
import java.util.List;

class StringTreeBuilder {
    private final StringTree[] lastNodeAtLevel = new StringTree[100];
    private boolean beginsWithLevelAndSpace;
    private StringTree treeForCurrentLine;
    private final StringTree wrapperNode = new StringTree();
    private StringTree mostRecentlyAdded;
    private final InfoTraderParser parser;
    private int lineNum = 0;
    private String line;
    private final StringCanonicalizer canonizer = new StringCanonicalizer();

    static String leftTrim(String line) {
        if (line == null) {
            return null;
        }
        if (line.length() == 0) {
            return "";
        }
        if (!Character.isWhitespace(line.charAt(0))) {
            return line;
        }
        for (int i = 0; i < line.length(); ++i) {
            if (Character.isWhitespace(line.charAt(i))) continue;
            return line.substring(i);
        }
        return "";
    }

    StringTreeBuilder(InfoTraderParser parser) {
        this.parser = parser;
        this.getTree().setLevel(-1);
        this.mostRecentlyAdded = null;
        this.lineNum = parser.getLineNum();
    }

    public StringTree getTree() {
        return this.wrapperNode;
    }

    void appendLine(String l) throws InfoTraderParserException {
        this.line = l;
        ++this.lineNum;
        this.treeForCurrentLine = new StringTree();
        this.treeForCurrentLine.setLineNum(this.lineNum);
        this.checkIfNewLevelLine();
        if (this.beginsWithLevelAndSpace) {
            this.addNewNode();
            this.mostRecentlyAdded = this.treeForCurrentLine;
        } else {
            this.makeConcatenationOfPreviousNode();
        }
    }

    private void addNewNode() throws InfoTraderParserException {
        LinePieces lp = new LinePieces(this.line, this.lineNum);
        this.treeForCurrentLine.setLevel(lp.level);
        this.treeForCurrentLine.setId(lp.id);
        this.treeForCurrentLine.setTag(lp.tag.intern());
        this.treeForCurrentLine.setValue(this.canonizer.getCanonicalVersion(lp.remainder));
        lp = null;
        StringTree addTo = null;
        addTo = this.treeForCurrentLine.getLevel() == 0 ? this.getTree() : this.lastNodeAtLevel[this.treeForCurrentLine.getLevel() - 1];
        if (addTo == null) {
            this.parser.getErrors().add(this.treeForCurrentLine.getTag() + " tag at line " + this.treeForCurrentLine.getLineNum() + ": Unable to find suitable parent node at level " + (this.treeForCurrentLine.getLevel() - 1));
        } else {
            addTo.getChildren(true).add(this.treeForCurrentLine);
            this.treeForCurrentLine.setParent(addTo);
            this.lastNodeAtLevel[this.treeForCurrentLine.getLevel()] = this.treeForCurrentLine;
        }
        Arrays.fill(this.lastNodeAtLevel, this.treeForCurrentLine.getLevel() + 1, 100, null);
    }

    private void checkIfNewLevelLine() throws InfoTraderParserException {
        block2 : {
            this.beginsWithLevelAndSpace = false;
            try {
                this.beginsWithLevelAndSpace = this.startsWithLevelAndSpace();
            }
            catch (InfoTraderParserException e) {
                if (!this.parser.isStrictLineBreaks()) break block2;
                throw e;
            }
        }
    }

    private void makeConcatenationOfPreviousNode() {
        if (this.mostRecentlyAdded == null) {
            this.parser.getWarnings().add("Line " + this.lineNum + " did not begin with a level and tag, so it was discarded.");
        } else {
            this.treeForCurrentLine.setLevel(this.mostRecentlyAdded.getLevel() + 1);
            this.treeForCurrentLine.setTag(Tag.CONTINUATION.tagText);
            this.treeForCurrentLine.setValue(this.line);
            this.treeForCurrentLine.setParent(this.mostRecentlyAdded);
            this.mostRecentlyAdded.getChildren(true).add(this.treeForCurrentLine);
            this.parser.getWarnings().add("Line " + this.lineNum + " did not begin with a level and tag, so it was treated as a " + "non-standard continuation of the previous line.");
        }
    }

    private boolean startsWithLevelAndSpace() throws InfoTraderParserException {
        try {
            char c1 = this.line.charAt(0);
            char c2 = this.line.charAt(1);
            char c3 = this.line.charAt(2);
            if (Character.isDigit(c1)) {
                if (' ' == c2) {
                    return true;
                }
                if (Character.isDigit(c2) && ' ' == c3) {
                    return true;
                }
                throw new InfoTraderParserException("Line " + this.lineNum + " does not begin with a 1 or 2 digit number for the level followed by a space: " + this.line);
            }
            throw new InfoTraderParserException("Line " + this.lineNum + " does not begin with a 1 or 2 digit number for the level followed by a space: " + this.line);
        }
        catch (IndexOutOfBoundsException e) {
            throw new InfoTraderParserException("Line " + this.lineNum + " does not begin with a 1 or 2 digit number for the level followed by a space: " + this.line, e);
        }
    }
}

