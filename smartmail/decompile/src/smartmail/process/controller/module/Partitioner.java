/*
 * Decompiled with CFR 0_121.
 */
package smartmail.process.controller.module;

import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.hadoop.io.BytesWritable;
import smartmail.process.controller.module.StateHolder;

public class Partitioner {
    List<List<BytesWritable>> sfmaplist;
    private final int partitionsize;
    StateHolder sfile;

    public Partitioner(int psize, StateHolder sfile) {
        this.partitionsize = psize;
        this.sfile = sfile;
        this.initseqfiles();
    }

    private void initseqfiles() {
        Collection values = this.sfile.sfmap.values();
        ArrayList<BytesWritable> valuesl = new ArrayList<BytesWritable>(values);
        this.sfmaplist = Partitioner.chopped(valuesl, this.partitionsize);
    }

    static List<List<BytesWritable>> chopped(List<BytesWritable> list, int L) {
        ArrayList<List<BytesWritable>> parts = new ArrayList<List<BytesWritable>>();
        int N = list.size();
        for (int i = 0; i < N; i += L) {
            parts.add(new ArrayList<BytesWritable>(list.subList(i, Math.min(N, i + L))));
        }
        return parts;
    }

    List<BytesWritable> getPartition(int i) {
        return this.sfmaplist.get(i);
    }
}

