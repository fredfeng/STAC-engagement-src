/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.reader.pbf;

import com.graphhopper.reader.OSMElement;
import com.graphhopper.reader.pbf.PbfBlobDecoder;
import com.graphhopper.reader.pbf.PbfBlobDecoderListener;
import com.graphhopper.reader.pbf.PbfBlobResult;
import com.graphhopper.reader.pbf.PbfRawBlob;
import com.graphhopper.reader.pbf.PbfStreamSplitter;
import com.graphhopper.reader.pbf.Sink;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PbfDecoder
implements Runnable {
    private final PbfStreamSplitter streamSplitter;
    private final ExecutorService executorService;
    private final int maxPendingBlobs;
    private final Sink sink;
    private final Lock lock;
    private final Condition dataWaitCondition;
    private final Queue<PbfBlobResult> blobResults;

    public PbfDecoder(PbfStreamSplitter streamSplitter, ExecutorService executorService, int maxPendingBlobs, Sink sink) {
        this.streamSplitter = streamSplitter;
        this.executorService = executorService;
        this.maxPendingBlobs = maxPendingBlobs;
        this.sink = sink;
        this.lock = new ReentrantLock();
        this.dataWaitCondition = this.lock.newCondition();
        this.blobResults = new LinkedList<PbfBlobResult>();
    }

    private void waitForUpdate() {
        try {
            this.dataWaitCondition.await();
        }
        catch (InterruptedException e) {
            throw new RuntimeException("Thread was interrupted.", e);
        }
    }

    private void signalUpdate() {
        this.dataWaitCondition.signal();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void sendResultsToSink(int targetQueueSize) {
        while (this.blobResults.size() > targetQueueSize) {
            PbfBlobResult blobResult = this.blobResults.remove();
            while (!blobResult.isComplete()) {
                this.waitForUpdate();
            }
            if (!blobResult.isSuccess()) {
                throw new RuntimeException("A PBF decoding worker thread failed, aborting.", blobResult.getException());
            }
            this.lock.unlock();
            try {
                for (OSMElement entity : blobResult.getEntities()) {
                    this.sink.process(entity);
                }
            }
            finally {
                this.lock.lock();
            }
        }
    }

    private void processBlobs() {
        while (this.streamSplitter.hasNext()) {
            PbfRawBlob rawBlob = this.streamSplitter.next();
            final PbfBlobResult blobResult = new PbfBlobResult();
            this.blobResults.add(blobResult);
            PbfBlobDecoderListener decoderListener = new PbfBlobDecoderListener(){

                /*
                 * WARNING - Removed try catching itself - possible behaviour change.
                 */
                @Override
                public void error(Exception ex) {
                    PbfDecoder.this.lock.lock();
                    try {
                        blobResult.storeFailureResult(ex);
                        PbfDecoder.this.signalUpdate();
                    }
                    finally {
                        PbfDecoder.this.lock.unlock();
                    }
                }

                /*
                 * WARNING - Removed try catching itself - possible behaviour change.
                 */
                @Override
                public void complete(List<OSMElement> decodedEntities) {
                    PbfDecoder.this.lock.lock();
                    try {
                        blobResult.storeSuccessResult(decodedEntities);
                        PbfDecoder.this.signalUpdate();
                    }
                    finally {
                        PbfDecoder.this.lock.unlock();
                    }
                }
            };
            PbfBlobDecoder blobDecoder = new PbfBlobDecoder(rawBlob.getType(), rawBlob.getData(), decoderListener);
            this.executorService.execute(blobDecoder);
            this.sendResultsToSink(this.maxPendingBlobs - 1);
        }
        this.sendResultsToSink(0);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void run() {
        this.lock.lock();
        try {
            this.processBlobs();
        }
        finally {
            this.lock.unlock();
        }
    }

}

