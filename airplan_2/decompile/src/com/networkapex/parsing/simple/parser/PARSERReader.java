/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.parsing.simple.parser;

import com.networkapex.parsing.simple.PARSERArray;
import com.networkapex.parsing.simple.PARSERObject;
import com.networkapex.parsing.simple.parser.ContainerFactory;
import com.networkapex.parsing.simple.parser.ContentManager;
import com.networkapex.parsing.simple.parser.ParseRaiser;
import com.networkapex.parsing.simple.parser.Yylex;
import com.networkapex.parsing.simple.parser.Yytoken;
import com.networkapex.parsing.simple.parser.YytokenBuilder;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PARSERReader {
    public static final int S_INIT = 0;
    public static final int S_IN_FINISHED_VALUE = 1;
    public static final int S_IN_OBJECT = 2;
    public static final int S_IN_ARRAY = 3;
    public static final int S_PASSED_PAIR_KEY = 4;
    public static final int S_IN_PAIR_VALUE = 5;
    public static final int S_END = 6;
    public static final int S_IN_ERROR = -1;
    private LinkedList managerStatusStack;
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
        this.managerStatusStack = null;
    }

    public void reset(Reader in) {
        this.lexer.yyreset(in);
        this.reset();
    }

    public int fetchPosition() {
        return this.lexer.pullPosition();
    }

    public Object parse(String s) throws ParseRaiser {
        return this.parse(s, (ContainerFactory)null);
    }

    public Object parse(String s, ContainerFactory containerFactory) throws ParseRaiser {
        StringReader in = new StringReader(s);
        try {
            return this.parse((Reader)in, containerFactory);
        }
        catch (IOException ie) {
            throw new ParseRaiser(-1, 2, ie);
        }
    }

    public Object parse(Reader in) throws IOException, ParseRaiser {
        return this.parse(in, (ContainerFactory)null);
    }

    public Object parse(Reader in, ContainerFactory containerFactory) throws IOException, ParseRaiser {
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
                    throw new ParseRaiser(this.fetchPosition(), 1, this.token);
                }
                case 2: {
                    switch (this.token.type) {
                        case 5: {
                            break block1;
                        }
                        case 0: {
                            if (this.token.value instanceof String) {
                                new PARSERReaderCoordinator(statusStack, valueStack).invoke();
                                break block1;
                            }
                            this.status = -1;
                            break block1;
                        }
                        case 2: {
                            if (valueStack.size() > 1) {
                                this.parseTarget(statusStack, valueStack);
                                break block1;
                            }
                            this.parseExecutor();
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
                                this.parseService(statusStack, valueStack);
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
                    throw new ParseRaiser(this.fetchPosition(), 1, this.token);
                }
            }
            if (this.status != -1) continue;
            throw new ParseRaiser(this.fetchPosition(), 1, this.token);
        } while (this.token.type != -1);
        throw new ParseRaiser(this.fetchPosition(), 1, this.token);
    }

    private void parseService(LinkedList statusStack, LinkedList valueStack) {
        statusStack.removeFirst();
        valueStack.removeFirst();
        this.status = this.peekStatus(statusStack);
    }

    private void parseExecutor() {
        this.status = 1;
    }

    private void parseTarget(LinkedList statusStack, LinkedList valueStack) {
        statusStack.removeFirst();
        valueStack.removeFirst();
        this.status = this.peekStatus(statusStack);
    }

    private void nextToken() throws ParseRaiser, IOException {
        this.token = this.lexer.yylex();
        if (this.token == null) {
            this.token = new YytokenBuilder().defineType(-1).setValue(null).generateYytoken();
        }
    }

    private Map generateObjectContainer(ContainerFactory containerFactory) {
        if (containerFactory == null) {
            return new PARSERObject();
        }
        Map m = containerFactory.generateObjectContainer();
        if (m == null) {
            return new PARSERObject();
        }
        return m;
    }

    private List generateArrayContainer(ContainerFactory containerFactory) {
        if (containerFactory == null) {
            return new PARSERArray();
        }
        List l = containerFactory.creatArrayContainer();
        if (l == null) {
            return new PARSERArray();
        }
        return l;
    }

    public void parse(String s, ContentManager contentManager) throws ParseRaiser {
        this.parse(s, contentManager, false);
    }

    public void parse(String s, ContentManager contentManager, boolean isResume) throws ParseRaiser {
        StringReader in = new StringReader(s);
        try {
            this.parse(in, contentManager, isResume);
        }
        catch (IOException ie) {
            throw new ParseRaiser(-1, 2, ie);
        }
    }

    public void parse(Reader in, ContentManager contentManager) throws IOException, ParseRaiser {
        this.parse(in, contentManager, false);
    }

    public void parse(Reader in, ContentManager contentManager, boolean isResume) throws IOException, ParseRaiser {
        if (!isResume) {
            this.parseHome(in);
        } else if (this.managerStatusStack == null) {
            isResume = false;
            this.reset(in);
            this.managerStatusStack = new LinkedList();
        }
        LinkedList statusStack = this.managerStatusStack;
        try {
            do {
                block1 : switch (this.status) {
                    case 0: {
                        contentManager.startPARSER();
                        this.nextToken();
                        switch (this.token.type) {
                            case 0: {
                                this.status = 1;
                                statusStack.addFirst(new Integer(this.status));
                                if (contentManager.primitive(this.token.value)) break block1;
                                return;
                            }
                            case 1: {
                                this.status = 2;
                                statusStack.addFirst(new Integer(this.status));
                                if (contentManager.startObject()) break block1;
                                return;
                            }
                            case 3: {
                                this.status = 3;
                                statusStack.addFirst(new Integer(this.status));
                                if (contentManager.startArray()) break block1;
                                return;
                            }
                        }
                        this.status = -1;
                        break;
                    }
                    case 1: {
                        this.nextToken();
                        if (this.token.type == -1) {
                            contentManager.endPARSER();
                            this.status = 6;
                            return;
                        }
                        this.parseAdviser();
                    }
                    case 2: {
                        this.nextToken();
                        switch (this.token.type) {
                            case 5: {
                                break block1;
                            }
                            case 0: {
                                if (this.token.value instanceof String) {
                                    if (!this.parseAssist(contentManager, statusStack)) break block1;
                                    return;
                                }
                                this.parseHelper();
                                break block1;
                            }
                            case 2: {
                                if (statusStack.size() > 1) {
                                    new PARSERReaderEngine(statusStack).invoke();
                                } else {
                                    this.status = 1;
                                }
                                if (contentManager.endObject()) break block1;
                                return;
                            }
                        }
                        this.status = -1;
                        break;
                    }
                    case 4: {
                        this.nextToken();
                        switch (this.token.type) {
                            case 6: {
                                break block1;
                            }
                            case 0: {
                                statusStack.removeFirst();
                                this.status = this.peekStatus(statusStack);
                                if (!contentManager.primitive(this.token.value)) {
                                    return;
                                }
                                if (contentManager.endObjectEntry()) break block1;
                                return;
                            }
                            case 3: {
                                statusStack.removeFirst();
                                statusStack.addFirst(new Integer(5));
                                this.status = 3;
                                statusStack.addFirst(new Integer(this.status));
                                if (contentManager.startArray()) break block1;
                                return;
                            }
                            case 1: {
                                statusStack.removeFirst();
                                statusStack.addFirst(new Integer(5));
                                this.status = 2;
                                statusStack.addFirst(new Integer(this.status));
                                if (contentManager.startObject()) break block1;
                                return;
                            }
                        }
                        this.status = -1;
                        break;
                    }
                    case 5: {
                        statusStack.removeFirst();
                        this.status = this.peekStatus(statusStack);
                        if (contentManager.endObjectEntry()) break;
                        return;
                    }
                    case 3: {
                        this.nextToken();
                        switch (this.token.type) {
                            case 5: {
                                break block1;
                            }
                            case 0: {
                                if (contentManager.primitive(this.token.value)) break block1;
                                return;
                            }
                            case 4: {
                                if (statusStack.size() > 1) {
                                    this.parseGuide(statusStack);
                                } else {
                                    this.status = 1;
                                }
                                if (contentManager.endArray()) break block1;
                                return;
                            }
                            case 1: {
                                this.status = 2;
                                statusStack.addFirst(new Integer(this.status));
                                if (contentManager.startObject()) break block1;
                                return;
                            }
                            case 3: {
                                this.status = 3;
                                statusStack.addFirst(new Integer(this.status));
                                if (contentManager.startArray()) break block1;
                                return;
                            }
                        }
                        this.status = -1;
                        break;
                    }
                    case 6: {
                        return;
                    }
                    case -1: {
                        throw new ParseRaiser(this.fetchPosition(), 1, this.token);
                    }
                }
                if (this.status != -1) continue;
                throw new ParseRaiser(this.fetchPosition(), 1, this.token);
            } while (this.token.type != -1);
        }
        catch (IOException ie) {
            this.status = -1;
            throw ie;
        }
        catch (ParseRaiser pe) {
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
        throw new ParseRaiser(this.fetchPosition(), 1, this.token);
    }

    private void parseGuide(LinkedList statusStack) {
        statusStack.removeFirst();
        this.status = this.peekStatus(statusStack);
    }

    private void parseHelper() {
        this.status = -1;
    }

    private boolean parseAssist(ContentManager contentManager, LinkedList statusStack) throws ParseRaiser, IOException {
        String key = (String)this.token.value;
        this.status = 4;
        statusStack.addFirst(new Integer(this.status));
        if (!contentManager.startObjectEntry(key)) {
            return true;
        }
        return false;
    }

    private void parseAdviser() throws ParseRaiser {
        this.status = -1;
        throw new ParseRaiser(this.fetchPosition(), 1, this.token);
    }

    private void parseHome(Reader in) {
        this.reset(in);
        this.managerStatusStack = new LinkedList();
    }

    static /* synthetic */ Yytoken access$000(PARSERReader x0) {
        return x0.token;
    }

    private class PARSERReaderEngine {
        private LinkedList statusStack;

        public PARSERReaderEngine(LinkedList statusStack) {
            this.statusStack = statusStack;
        }

        public void invoke() {
            this.statusStack.removeFirst();
            PARSERReader.this.status = PARSERReader.this.peekStatus(this.statusStack);
        }
    }

    private class PARSERReaderCoordinator {
        private LinkedList statusStack;
        private LinkedList valueStack;

        public PARSERReaderCoordinator(LinkedList statusStack, LinkedList valueStack) {
            this.statusStack = statusStack;
            this.valueStack = valueStack;
        }

        public void invoke() {
            String key = (String)PARSERReader.access$000((PARSERReader)PARSERReader.this).value;
            this.valueStack.addFirst(key);
            PARSERReader.this.status = 4;
            this.statusStack.addFirst(new Integer(PARSERReader.this.status));
        }
    }

}

