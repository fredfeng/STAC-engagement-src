/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.json.simple.parser;

import edu.computerapex.json.simple.JSONArray;
import edu.computerapex.json.simple.JSONObject;
import edu.computerapex.json.simple.parser.ContainerFactory;
import edu.computerapex.json.simple.parser.ContentHandler;
import edu.computerapex.json.simple.parser.ParseDeviation;
import edu.computerapex.json.simple.parser.Yylex;
import edu.computerapex.json.simple.parser.Yytoken;
import edu.computerapex.json.simple.parser.YytokenBuilder;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JSONRetriever {
    public static final int S_INIT = 0;
    public static final int S_IN_FINISHED_VALUE = 1;
    public static final int S_IN_OBJECT = 2;
    public static final int S_IN_ARRAY = 3;
    public static final int S_PASSED_PAIR_KEY = 4;
    public static final int S_IN_PAIR_VALUE = 5;
    public static final int S_END = 6;
    public static final int S_IN_ERROR = -1;
    private LinkedList handlerStatusStack;
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
        this.handlerStatusStack = null;
    }

    public void reset(Reader in) {
        this.lexer.yyreset(in);
        this.reset();
    }

    public int takePosition() {
        return this.lexer.fetchPosition();
    }

    public Object parse(String s) throws ParseDeviation {
        return this.parse(s, (ContainerFactory)null);
    }

    public Object parse(String s, ContainerFactory containerFactory) throws ParseDeviation {
        StringReader in = new StringReader(s);
        try {
            return this.parse((Reader)in, containerFactory);
        }
        catch (IOException ie) {
            throw new ParseDeviation(-1, 2, ie);
        }
    }

    public Object parse(Reader in) throws IOException, ParseDeviation {
        return this.parse(in, (ContainerFactory)null);
    }

    public Object parse(Reader in, ContainerFactory containerFactory) throws IOException, ParseDeviation {
        this.reset(in);
        LinkedList<Integer> statusStack = new LinkedList<Integer>();
        LinkedList<Object> valueStack = new LinkedList<Object>();
        do {
            this.nextToken();
            String key;
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
                    throw new ParseDeviation(this.takePosition(), 1, this.token);
                }
                case 2: {
                    switch (this.token.type) {
                        case 5: {
                            break block1;
                        }
                        case 0: {
                            if (this.token.value instanceof String) {
                                key = (String)this.token.value;
                                valueStack.addFirst(key);
                                this.status = 4;
                                statusStack.addFirst(new Integer(this.status));
                                break block1;
                            }
                            new JSONRetrieverWorker().invoke();
                            break block1;
                        }
                        case 2: {
                            if (valueStack.size() > 1) {
                                statusStack.removeFirst();
                                valueStack.removeFirst();
                                this.status = this.peekStatus(statusStack);
                                break block1;
                            }
                            this.parseHelp();
                            break block1;
                        }
                    }
                    this.status = -1;
                    break;
                }
                case 4: {
                    Map parent;
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
                    throw new ParseDeviation(this.takePosition(), 1, this.token);
                }
            }
            if (this.status != -1) continue;
            throw new ParseDeviation(this.takePosition(), 1, this.token);
        } while (this.token.type != -1);
        throw new ParseDeviation(this.takePosition(), 1, this.token);
    }

    private void parseHelp() {
        this.status = 1;
    }

    private void nextToken() throws ParseDeviation, IOException {
        this.token = this.lexer.yylex();
        if (this.token == null) {
            this.token = new YytokenBuilder().fixType(-1).assignValue(null).generateYytoken();
        }
    }

    private Map generateObjectContainer(ContainerFactory containerFactory) {
        if (containerFactory == null) {
            return new JSONObject();
        }
        Map m = containerFactory.createObjectContainer();
        if (m == null) {
            return new JSONObject();
        }
        return m;
    }

    private List generateArrayContainer(ContainerFactory containerFactory) {
        if (containerFactory == null) {
            return new JSONArray();
        }
        List l = containerFactory.creatArrayContainer();
        if (l == null) {
            return new JSONArray();
        }
        return l;
    }

    public void parse(String s, ContentHandler contentHandler) throws ParseDeviation {
        this.parse(s, contentHandler, false);
    }

    public void parse(String s, ContentHandler contentHandler, boolean isResume) throws ParseDeviation {
        StringReader in = new StringReader(s);
        try {
            this.parse(in, contentHandler, isResume);
        }
        catch (IOException ie) {
            throw new ParseDeviation(-1, 2, ie);
        }
    }

    public void parse(Reader in, ContentHandler contentHandler) throws IOException, ParseDeviation {
        this.parse(in, contentHandler, false);
    }

    public void parse(Reader in, ContentHandler contentHandler, boolean isResume) throws IOException, ParseDeviation {
        if (!isResume) {
            this.reset(in);
            this.handlerStatusStack = new LinkedList();
        } else if (this.handlerStatusStack == null) {
            isResume = false;
            this.reset(in);
            this.handlerStatusStack = new LinkedList();
        }
        LinkedList statusStack = this.handlerStatusStack;
        try {
            do {
                if (!this.parseEntity(contentHandler, statusStack)) continue;
                return;
            } while (this.token.type != -1);
        }
        catch (IOException ie) {
            this.status = -1;
            throw ie;
        }
        catch (ParseDeviation pe) {
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
        throw new ParseDeviation(this.takePosition(), 1, this.token);
    }

    private boolean parseEntity(ContentHandler contentHandler, LinkedList statusStack) throws ParseDeviation, IOException {
        block0 : switch (this.status) {
            case 0: {
                contentHandler.startJSON();
                this.nextToken();
                switch (this.token.type) {
                    case 0: {
                        this.status = 1;
                        statusStack.addFirst(new Integer(this.status));
                        if (contentHandler.primitive(this.token.value)) break block0;
                        return true;
                    }
                    case 1: {
                        this.status = 2;
                        statusStack.addFirst(new Integer(this.status));
                        if (contentHandler.startObject()) break block0;
                        return true;
                    }
                    case 3: {
                        this.status = 3;
                        statusStack.addFirst(new Integer(this.status));
                        if (contentHandler.startArray()) break block0;
                        return true;
                    }
                }
                this.status = -1;
                break;
            }
            case 1: {
                this.nextToken();
                if (this.token.type == -1) {
                    return this.parseEntityAid(contentHandler);
                }
                return this.parseEntityHerder();
            }
            case 2: {
                this.nextToken();
                switch (this.token.type) {
                    case 5: {
                        break block0;
                    }
                    case 0: {
                        if (this.token.value instanceof String) {
                            String key = (String)this.token.value;
                            this.status = 4;
                            statusStack.addFirst(new Integer(this.status));
                            if (contentHandler.startObjectEntry(key)) break block0;
                            return true;
                        }
                        this.parseEntityHome();
                        break block0;
                    }
                    case 2: {
                        if (statusStack.size() > 1) {
                            statusStack.removeFirst();
                            this.status = this.peekStatus(statusStack);
                        } else {
                            this.status = 1;
                        }
                        if (contentHandler.endObject()) break block0;
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
                        if (!contentHandler.primitive(this.token.value)) {
                            return true;
                        }
                        if (contentHandler.endObjectEntry()) break block0;
                        return true;
                    }
                    case 3: {
                        statusStack.removeFirst();
                        statusStack.addFirst(new Integer(5));
                        this.status = 3;
                        statusStack.addFirst(new Integer(this.status));
                        if (contentHandler.startArray()) break block0;
                        return true;
                    }
                    case 1: {
                        statusStack.removeFirst();
                        statusStack.addFirst(new Integer(5));
                        this.status = 2;
                        statusStack.addFirst(new Integer(this.status));
                        if (contentHandler.startObject()) break block0;
                        return true;
                    }
                }
                this.status = -1;
                break;
            }
            case 5: {
                statusStack.removeFirst();
                this.status = this.peekStatus(statusStack);
                if (contentHandler.endObjectEntry()) break;
                return true;
            }
            case 3: {
                this.nextToken();
                switch (this.token.type) {
                    case 5: {
                        break block0;
                    }
                    case 0: {
                        if (contentHandler.primitive(this.token.value)) break block0;
                        return true;
                    }
                    case 4: {
                        if (statusStack.size() > 1) {
                            this.parseEntityService(statusStack);
                        } else {
                            this.parseEntityHandler();
                        }
                        if (contentHandler.endArray()) break block0;
                        return true;
                    }
                    case 1: {
                        this.status = 2;
                        statusStack.addFirst(new Integer(this.status));
                        if (contentHandler.startObject()) break block0;
                        return true;
                    }
                    case 3: {
                        this.status = 3;
                        statusStack.addFirst(new Integer(this.status));
                        if (contentHandler.startArray()) break block0;
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
                throw new ParseDeviation(this.takePosition(), 1, this.token);
            }
        }
        if (this.status == -1) {
            return this.parseEntityCoordinator();
        }
        return false;
    }

    private boolean parseEntityCoordinator() throws ParseDeviation {
        throw new ParseDeviation(this.takePosition(), 1, this.token);
    }

    private void parseEntityHandler() {
        this.status = 1;
    }

    private void parseEntityService(LinkedList statusStack) {
        statusStack.removeFirst();
        this.status = this.peekStatus(statusStack);
    }

    private void parseEntityHome() {
        this.status = -1;
    }

    private boolean parseEntityHerder() throws ParseDeviation {
        this.status = -1;
        throw new ParseDeviation(this.takePosition(), 1, this.token);
    }

    private boolean parseEntityAid(ContentHandler contentHandler) throws ParseDeviation, IOException {
        contentHandler.endJSON();
        this.status = 6;
        return true;
    }

    private class JSONRetrieverWorker {
        private JSONRetrieverWorker() {
        }

        public void invoke() {
            JSONRetriever.this.status = -1;
        }
    }

}

