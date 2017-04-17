/*
 * Decompiled with CFR 0_121.
 */
package smartmail.process.controller.module;

import java.io.IOException;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import smartmail.datamodel.MessageWord;
import smartmail.logging.module.ObjSerializer;

public class EmailEventsMapper
extends MapReduceBase
implements Mapper<Writable, BytesWritable, Text, BytesWritable> {
    public EmailEventsMapper getnewInstance() {
        return new EmailEventsMapper();
    }

    @Override
    public void map(Writable key, BytesWritable value, OutputCollector<Text, BytesWritable> output, Reporter reporter) throws IOException {
        MessageWord deobj = (MessageWord)ObjSerializer.deSerializeObj(value);
        output.collect(new Text(deobj.getValue()), value);
    }
}

