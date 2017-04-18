/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.parsing.simple.extractor;

import edu.cyberapex.parsing.simple.PARTArray;
import edu.cyberapex.parsing.simple.PARTObject;
import edu.cyberapex.parsing.simple.extractor.ContainerFactory;
import edu.cyberapex.parsing.simple.extractor.ContentGuide;
import edu.cyberapex.parsing.simple.extractor.ParseFailure;
import edu.cyberapex.parsing.simple.extractor.Yylex;
import edu.cyberapex.parsing.simple.extractor.Yytoken;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PARTReader {
    public static final int S_INIT = 0;
    public static final int S_IN_FINISHED_VALUE = 1;
    public static final int S_IN_OBJECT = 2;
    public static final int S_IN_ARRAY = 3;
    public static final int S_PASSED_PAIR_KEY = 4;
    public static final int S_IN_PAIR_VALUE = 5;
    public static final int S_END = 6;
    public static final int S_IN_ERROR = -1;
    private LinkedList guideStatusStack;
    private Yylex lexer = new Yylex((Reader)null);
    private Yytoken token = null;
    private int status = 0;

    private int peekStatus(LinkedList statusStack) {
        if (statusStack.size() == 0) {
            return -1;
        }
        Integer status = (Integer)statusStack.getFirst();
        return status;
    }

    public void reset() {
        this.token = null;
        this.status = 0;
        this.guideStatusStack = null;
    }

    public void reset(Reader in) {
        this.lexer.yyreset(in);
        this.reset();
    }

    public int getPosition() {
        return this.lexer.obtainPosition();
    }

    public Object parse(String s) throws ParseFailure {
        return this.parse(s, (ContainerFactory)null);
    }

    public Object parse(String s, ContainerFactory containerFactory) throws ParseFailure {
        StringReader in = new StringReader(s);
        try {
            return this.parse((Reader)in, containerFactory);
        }
        catch (IOException ie) {
            throw new ParseFailure(-1, 2, ie);
        }
    }

    public Object parse(Reader in) throws IOException, ParseFailure {
        return this.parse(in, (ContainerFactory)null);
    }

    public Object parse(Reader in, ContainerFactory containerFactory) throws IOException, ParseFailure {
        this.reset(in);
        LinkedList<Integer> statusStack = new LinkedList<Integer>();
        LinkedList<Object> valueStack = new LinkedList<Object>();
        do {
            this.nextToken();
            List newArray;
            block1 : switch (this.status) {
                case 0: {
                    switch (this.token.type) {
                        case 0: {
                            this.status = 1;
                            statusStack.addFirst(new Integer(this.status));
                            valueStack.addFirst(this.token.value);
                            break block1;
                        }
                        case 1: {
                            this.status = 2;
                            statusStack.addFirst(new Integer(this.status));
                            valueStack.addFirst(this.generateObjectContainer(containerFactory));
                            break block1;
                        }
                        case 3: {
                            this.status = 3;
                            statusStack.addFirst(new Integer(this.status));
                            valueStack.addFirst(this.generateArrayContainer(containerFactory));
                            break block1;
                        }
                    }
                    this.status = -1;
                    break;
                }
                case 1: {
                    if (this.token.type == -1) {
                        return valueStack.removeFirst();
                    }
                    throw new ParseFailure(this.getPosition(), 1, this.token);
                }
                case 2: {
                    switch (this.token.type) {
                        case 5: {
                            break block1;
                        }
                        case 0: {
                            if (this.token.value instanceof String) {
                                this.parseAdviser(statusStack, valueStack);
                                break block1;
                            }
                            this.parseAid();
                            break block1;
                        }
                        case 2: {
                            if (valueStack.size() > 1) {
                                statusStack.removeFirst();
                                valueStack.removeFirst();
                                this.status = this.peekStatus(statusStack);
                                break block1;
                            }
                            new PARTReaderAdviser().invoke();
                            break block1;
                        }
                    }
                    this.status = -1;
                    break;
                }
                case 4: {
                    Map parent;
                    String key;
                    switch (this.token.type) {
                        case 6: {
                            break block1;
                        }
                        case 0: {
                            statusStack.removeFirst();
                            key = (String)valueStack.removeFirst();
                            parent = (Map)valueStack.getFirst();
                            parent.put(key, this.token.value);
                            this.status = this.peekStatus(statusStack);
                            break block1;
                        }
                        case 3: {
                            statusStack.removeFirst();
                            key = (String)valueStack.removeFirst();
                            parent = (Map)valueStack.getFirst();
                            newArray = this.generateArrayContainer(containerFactory);
                            parent.put(key, newArray);
                            this.status = 3;
                            statusStack.addFirst(new Integer(this.status));
                            valueStack.addFirst(newArray);
                            break block1;
                        }
                        case 1: {
                            statusStack.removeFirst();
                            key = (String)valueStack.removeFirst();
                            parent = (Map)valueStack.getFirst();
                            Map newObject = this.generateObjectContainer(containerFactory);
                            parent.put(key, newObject);
                            this.status = 2;
                            statusStack.addFirst(new Integer(this.status));
                            valueStack.addFirst(newObject);
                            break block1;
                        }
                    }
                    this.status = -1;
                    break;
                }
                case 3: {
                    List val;
                    switch (this.token.type) {
                        case 5: {
                            break block1;
                        }
                        case 0: {
                            val = (List)valueStack.getFirst();
                            val.add(this.token.value);
                            break block1;
                        }
                        case 4: {
                            if (valueStack.size() > 1) {
                                statusStack.removeFirst();
                                valueStack.removeFirst();
                                this.status = this.peekStatus(statusStack);
                                break block1;
                            }
                            this.status = 1;
                            break block1;
                        }
                        case 1: {
                            val = (List)valueStack.getFirst();
                            Map newObject = this.generateObjectContainer(containerFactory);
                            val.add(newObject);
                            this.status = 2;
                            statusStack.addFirst(new Integer(this.status));
                            valueStack.addFirst(newObject);
                            break block1;
                        }
                        case 3: {
                            val = (List)valueStack.getFirst();
                            newArray = this.generateArrayContainer(containerFactory);
                            val.add(newArray);
                            this.status = 3;
                            statusStack.addFirst(new Integer(this.status));
                            valueStack.addFirst(newArray);
                            break block1;
                        }
                    }
                    this.status = -1;
                    break;
                }
                case -1: {
                    throw new ParseFailure(this.getPosition(), 1, this.token);
                }
            }
            if (this.status != -1) continue;
            return this.parseHelp();
        } while (this.token.type != -1);
        throw new ParseFailure(this.getPosition(), 1, this.token);
    }

    private Object parseHelp() throws ParseFailure {
        throw new ParseFailure(this.getPosition(), 1, this.token);
    }

    private void parseAid() {
        this.status = -1;
    }

    private void parseAdviser(LinkedList statusStack, LinkedList valueStack) {
        String key = (String)this.token.value;
        valueStack.addFirst(key);
        this.status = 4;
        statusStack.addFirst(new Integer(this.status));
    }

    private void nextToken() throws ParseFailure, IOException {
        this.token = this.lexer.yylex();
        if (this.token == null) {
            this.token = new Yytoken(-1, null);
        }
    }

    private Map generateObjectContainer(ContainerFactory containerFactory) {
        if (containerFactory == null) {
            return new PARTObject();
        }
        Map m = containerFactory.generateObjectContainer();
        if (m == null) {
            return new PARTObject();
        }
        return m;
    }

    private List generateArrayContainer(ContainerFactory containerFactory) {
        if (containerFactory == null) {
            return new PARTArray();
        }
        List l = containerFactory.creatArrayContainer();
        if (l == null) {
            return new PARTArray();
        }
        return l;
    }

    public void parse(String s, ContentGuide contentGuide) throws ParseFailure {
        this.parse(s, contentGuide, false);
    }

    public void parse(String s, ContentGuide contentGuide, boolean isResume) throws ParseFailure {
        StringReader in = new StringReader(s);
        try {
            this.parse(in, contentGuide, isResume);
        }
        catch (IOException ie) {
            throw new ParseFailure(-1, 2, ie);
        }
    }

    public void parse(Reader in, ContentGuide contentGuide) throws IOException, ParseFailure {
        this.parse(in, contentGuide, false);
    }

    public void parse(Reader in, ContentGuide contentGuide, boolean isResume) throws IOException, ParseFailure {
        if (!isResume) {
            this.reset(in);
            this.guideStatusStack = new LinkedList();
        } else if (this.guideStatusStack == null) {
            isResume = false;
            this.reset(in);
            this.guideStatusStack = new LinkedList();
        }
        LinkedList statusStack = this.guideStatusStack;
        try {
            do {
                if (!this.parseHelp(contentGuide, statusStack)) continue;
                return;
            } while (this.token.type != -1);
        }
        catch (IOException ie) {
            this.status = -1;
            throw ie;
        }
        catch (ParseFailure pe) {
            this.status = -1;
            throw pe;
        }
        catch (RuntimeException re) {
            this.status = -1;
            throw re;
        }
        catch (Error e) {
            this.status = -1;
            throw e;
        }
        this.status = -1;
        throw new ParseFailure(this.getPosition(), 1, this.token);
    }

    private boolean parseHelp(ContentGuide contentGuide, LinkedList statusStack) throws ParseFailure, IOException {
        block0 : switch (this.status) {
            case 0: {
                contentGuide.startPART();
                this.nextToken();
                switch (this.token.type) {
                    case 0: {
                        this.status = 1;
                        statusStack.addFirst(new Integer(this.status));
                        if (contentGuide.primitive(this.token.value)) break block0;
                        return true;
                    }
                    case 1: {
                        this.status = 2;
                        statusStack.addFirst(new Integer(this.status));
                        if (contentGuide.startObject()) break block0;
                        return true;
                    }
                    case 3: {
                        this.status = 3;
                        statusStack.addFirst(new Integer(this.status));
                        if (contentGuide.startArray()) break block0;
                        return true;
                    }
                }
                this.status = -1;
                break;
            }
            case 1: {
                this.nextToken();
                if (this.token.type == -1) {
                    contentGuide.endPART();
                    this.status = 6;
                    return true;
                }
                return this.parseHelpHelp();
            }
            case 2: {
                this.nextToken();
                switch (this.token.type) {
                    case 5: {
                        break block0;
                    }
                    case 0: {
                        if (this.token.value instanceof String) {
                            if (!new PARTReaderEntity(contentGuide, statusStack).invoke()) break block0;
                            return true;
                        }
                        this.status = -1;
                        break block0;
                    }
                    case 2: {
                        if (statusStack.size() > 1) {
                            statusStack.removeFirst();
                            this.status = this.peekStatus(statusStack);
                        } else {
                            new PARTReaderUtility().invoke();
                        }
                        if (contentGuide.endObject()) break block0;
                        return true;
                    }
                }
                this.status = -1;
                break;
            }
            case 4: {
                this.nextToken();
                switch (this.token.type) {
                    case 6: {
                        break block0;
                    }
                    case 0: {
                        statusStack.removeFirst();
                        this.status = this.peekStatus(statusStack);
                        if (!contentGuide.primitive(this.token.value)) {
                            return true;
                        }
                        if (contentGuide.endObjectEntry()) break block0;
                        return true;
                    }
                    case 3: {
                        statusStack.removeFirst();
                        statusStack.addFirst(new Integer(5));
                        this.status = 3;
                        statusStack.addFirst(new Integer(this.status));
                        if (contentGuide.startArray()) break block0;
                        return true;
                    }
                    case 1: {
                        statusStack.removeFirst();
                        statusStack.addFirst(new Integer(5));
                        this.status = 2;
                        statusStack.addFirst(new Integer(this.status));
                        if (contentGuide.startObject()) break block0;
                        return true;
                    }
                }
                this.status = -1;
                break;
            }
            case 5: {
                statusStack.removeFirst();
                this.status = this.peekStatus(statusStack);
                if (contentGuide.endObjectEntry()) break;
                return true;
            }
            case 3: {
                this.nextToken();
                switch (this.token.type) {
                    case 5: {
                        break block0;
                    }
                    case 0: {
                        if (contentGuide.primitive(this.token.value)) break block0;
                        return true;
                    }
                    case 4: {
                        if (statusStack.size() > 1) {
                            this.parseHelpAid(statusStack);
                        } else {
                            this.parseHelpHome();
                        }
                        if (contentGuide.endArray()) break block0;
                        return true;
                    }
                    case 1: {
                        this.status = 2;
                        statusStack.addFirst(new Integer(this.status));
                        if (contentGuide.startObject()) break block0;
                        return true;
                    }
                    case 3: {
                        this.status = 3;
                        statusStack.addFirst(new Integer(this.status));
                        if (contentGuide.startArray()) break block0;
                        return true;
                    }
                }
                this.status = -1;
                break;
            }
            case 6: {
                return true;
            }
            case -1: {
                throw new ParseFailure(this.getPosition(), 1, this.token);
            }
        }
        if (this.status == -1) {
            throw new ParseFailure(this.getPosition(), 1, this.token);
        }
        return false;
    }

    private void parseHelpHome() {
        this.status = 1;
    }

    private void parseHelpAid(LinkedList statusStack) {
        statusStack.removeFirst();
        this.status = this.peekStatus(statusStack);
    }

    private boolean parseHelpHelp() throws ParseFailure {
        this.status = -1;
        throw new ParseFailure(this.getPosition(), 1, this.token);
    }

    static /* synthetic */ Yytoken access$300(PARTReader x0) {
        return x0.token;
    }

    private class PARTReaderUtility {
        private PARTReaderUtility() {
        }

        public void invoke() {
            PARTReader.this.status = 1;
        }
    }

    private class PARTReaderEntity {
        private boolean myResult;
        private ContentGuide contentGuide;
        private LinkedList statusStack;

        public PARTReaderEntity(ContentGuide contentGuide, LinkedList statusStack) {
            this.contentGuide = contentGuide;
            this.statusStack = statusStack;
        }

        boolean is() {
            return this.myResult;
        }

        public boolean invoke() throws ParseFailure, IOException {
            String key = (String)PARTReader.access$300((PARTReader)PARTReader.this).value;
            PARTReader.this.status = 4;
            this.statusStack.addFirst(new Integer(PARTReader.this.status));
            if (!this.contentGuide.startObjectEntry(key)) {
                return true;
            }
            return false;
        }
    }

    private class PARTReaderAdviser {
        private PARTReaderAdviser() {
        }

        public void invoke() {
            PARTReader.this.status = 1;
        }
    }

}

