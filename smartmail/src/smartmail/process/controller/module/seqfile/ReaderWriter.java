/*
 * Decompiled with CFR 0_121.
 */
package smartmail.process.controller.module.seqfile;

import java.io.Closeable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.OutputCollector;

public interface ReaderWriter
extends OutputCollector,
Closeable {
    public void append(Writable var1, Writable var2);
}

