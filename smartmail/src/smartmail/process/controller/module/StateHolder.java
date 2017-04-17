/*
 * Decompiled with CFR 0_121.
 */
package smartmail.process.controller.module;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Multiset;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import smartmail.process.controller.module.seqfile.ReaderWriter;

public class StateHolder
implements ReaderWriter {
    public Multimap sfmap = Multimaps.synchronizedMultimap(ArrayListMultimap.create());
    Multimap mmap = Multimaps.synchronizedMultimap(ArrayListMultimap.create());
    Multimap rmap = Multimaps.synchronizedMultimap(ArrayListMultimap.create());
    Multimap tomap;
    Multimap frommap;
    Mapper mapper;
    Reducer reducer;
    public static final int partitionsize = 4;
    int pcurr = 0;
    int vadded = 0;

    public void sfclear() {
        this.sfmap.clear();
    }

    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    public void setReducer(Reducer reducer) {
        this.reducer = reducer;
    }

    public void callMapper() throws IOException {
        System.out.println("Call Mapper");
        this.callDumper("Mapper");
        Multiset keys = this.mmap.keys();
    }

    public void callReducer() throws IOException {
        this.callDumper("Reducer");
    }

    private void callDumper(String stage) throws IOException {
        long startTime = System.currentTimeMillis();
        if (stage.equals("Mapper")) {
            this.frommap = this.sfmap;
            this.tomap = this.mmap;
        }
        if (stage.equals("Reducer")) {
            this.frommap = this.mmap;
            this.tomap = this.rmap;
        }
        Set keys = this.frommap.keySet();
        for (Object nextkey : keys) {
            Collection nextcoll = this.frommap.get(nextkey);
            Iterator itv = nextcoll.iterator();
            if (stage.equals("Mapper")) {
                while (itv.hasNext()) {
                    Object next = itv.next();
                    this.mapper.map(nextkey, next, this, Reporter.NULL);
                }
                continue;
            }
            if (!stage.equals("Reducer")) continue;
            this.reducer.reduce(nextkey, itv, this, Reporter.NULL);
        }
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
    }

    public void collect(Object k, Object v) throws IOException {
        this.tomap.put(k, v);
    }

    @Override
    public void append(Writable key, Writable val) {
        this.sfmap.put(key, val);
    }

    @Override
    public void close() throws IOException {
    }

    public Multimap getOutput() {
        return this.tomap;
    }
}

