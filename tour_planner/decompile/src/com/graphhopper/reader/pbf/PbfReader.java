/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.reader.pbf;

import com.graphhopper.reader.pbf.PbfDecoder;
import com.graphhopper.reader.pbf.PbfStreamSplitter;
import com.graphhopper.reader.pbf.Sink;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PbfReader
implements Runnable {
    private InputStream inputStream;
    private Sink sink;
    private int workers;

    public PbfReader(InputStream in, Sink sink, int workers) {
        this.inputStream = in;
        this.sink = sink;
        this.workers = workers;
    }

    @Override
    public void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(this.workers);
        try {
            PbfStreamSplitter streamSplitter = new PbfStreamSplitter(new DataInputStream(this.inputStream));
            PbfDecoder pbfDecoder = new PbfDecoder(streamSplitter, executorService, this.workers + 1, this.sink);
            pbfDecoder.run();
        }
        catch (Exception e) {
            throw new RuntimeException("Unable to read PBF file.", e);
        }
        finally {
            this.sink.complete();
            executorService.shutdownNow();
        }
    }
}

