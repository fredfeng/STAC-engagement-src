/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.parser;

import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.Header;
import infotrader.parser.model.InfoTrader;
import infotrader.parser.model.InfoTraderVersion;
import infotrader.parser.model.Multimedia;
import infotrader.parser.model.Repository;
import infotrader.parser.model.Source;
import infotrader.parser.model.StringTree;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.model.Submitter;
import infotrader.parser.model.SupportedVersion;
import infotrader.parser.parser.InfoTraderParser;
import infotrader.parser.parser.Tag;
import java.util.List;
import java.util.Map;

abstract class AbstractParser<T> {
    protected final StringTree stringTree;
    protected InfoTraderParser InfoTraderParser;
    protected final T loadInto;

    AbstractParser(InfoTraderParser InfoTraderParser2, StringTree stringTree, T loadInto) {
        this.InfoTraderParser = InfoTraderParser2;
        this.stringTree = stringTree;
        this.loadInto = loadInto;
    }

    protected void addError(String string) {
        this.InfoTraderParser.getErrors().add(string);
    }

    protected void addWarning(String string) {
        this.InfoTraderParser.getWarnings().add(string);
    }

    protected final boolean g55() {
        InfoTrader g = this.InfoTraderParser.getInfoTrader();
        return g != null && g.getHeader() != null && g.getHeader().getInfoTraderVersion() != null && SupportedVersion.V5_5.equals((Object)g.getHeader().getInfoTraderVersion().getVersionNumber());
    }

    protected Multimedia getMultimedia(String xref) {
        Multimedia m = this.InfoTraderParser.getInfoTrader().getMultimedia().get(xref);
        if (m == null) {
            m = new Multimedia();
            m.setXref(xref);
            this.InfoTraderParser.getInfoTrader().getMultimedia().put(xref, m);
        }
        return m;
    }

    protected Repository getRepository(String xref) {
        Repository r = this.InfoTraderParser.getInfoTrader().getRepositories().get(xref);
        if (r == null) {
            r = new Repository();
            r.setXref(xref);
            this.InfoTraderParser.getInfoTrader().getRepositories().put(xref, r);
        }
        return r;
    }

    protected Source getSource(String xref) {
        Source src = this.InfoTraderParser.getInfoTrader().getSources().get(xref);
        if (src == null) {
            src = new Source(xref);
            this.InfoTraderParser.getInfoTrader().getSources().put(src.getXref(), src);
        }
        return src;
    }

    protected Submitter getSubmitter(String xref) {
        Submitter s = this.InfoTraderParser.getInfoTrader().getSubmitters().get(xref);
        if (s == null) {
            s = new Submitter();
            s.setName(new StringWithCustomTags("UNSPECIFIED"));
            s.setXref(xref);
            this.InfoTraderParser.getInfoTrader().getSubmitters().put(xref, s);
        }
        return s;
    }

    protected void loadMultiLinesOfText(StringTree stringTreeWithLinesOfText, List<String> listOfString, AbstractElement element) {
        if (stringTreeWithLinesOfText.getValue() != null) {
            listOfString.add(stringTreeWithLinesOfText.getValue());
        }
        if (stringTreeWithLinesOfText.getChildren() != null) {
            for (StringTree ch : stringTreeWithLinesOfText.getChildren()) {
                if (Tag.CONTINUATION.equalsText(ch.getTag())) {
                    if (ch.getValue() == null) {
                        listOfString.add("");
                        continue;
                    }
                    listOfString.add(ch.getValue());
                    continue;
                }
                if (Tag.CONCATENATION.equalsText(ch.getTag())) {
                    if (ch.getValue() == null) continue;
                    if (listOfString.isEmpty()) {
                        listOfString.add(ch.getValue());
                        continue;
                    }
                    listOfString.set(listOfString.size() - 1, listOfString.get(listOfString.size() - 1) + ch.getValue());
                    continue;
                }
                this.unknownTag(ch, element);
            }
        }
    }

    protected boolean referencesAnotherNode(StringTree st) {
        if (st.getValue() == null) {
            return false;
        }
        int r1 = st.getValue().indexOf(64);
        if (r1 == -1) {
            return false;
        }
        int r2 = st.getValue().indexOf(64, r1);
        return r2 > -1;
    }

    protected void unknownTag(StringTree node, AbstractElement element) {
        if (node.getTag().length() > 0 && node.getTag().charAt(0) == '_' || !this.InfoTraderParser.isStrictCustomTags()) {
            element.getCustomTags(true).add(node);
            return;
        }
        StringBuilder sb = new StringBuilder(64);
        sb.append("Line ").append(node.getLineNum()).append(": Cannot handle tag ");
        sb.append(node.getTag());
        StringTree st = node;
        while (st.getParent() != null) {
            sb.append(", child of ").append((st = st.getParent()).getTag() == null ? null : st.getTag());
            if (st.getId() != null) {
                sb.append(" ").append(st.getId());
            }
            sb.append(" on line ").append(st.getLineNum());
        }
        this.addError(sb.toString());
    }

    abstract void parse();
}

