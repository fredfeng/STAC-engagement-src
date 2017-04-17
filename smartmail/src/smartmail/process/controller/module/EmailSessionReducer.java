/*
 * Decompiled with CFR 0_121.
 */
package smartmail.process.controller.module;

import java.io.IOException;
import java.util.Iterator;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import smartmail.datamodel.BodyWord;
import smartmail.datamodel.EmailAddress;
import smartmail.datamodel.MessageWord;
import smartmail.datamodel.SecureEmailAddress;
import smartmail.logging.module.ObjSerializer;
import smartmail.logging.module.SecureTermMonitor;

public class EmailSessionReducer
extends MapReduceBase
implements Reducer<Text, BytesWritable, Text, String> {
    @Override
    public void reduce(Text key, Iterator<BytesWritable> itrtr, OutputCollector<Text, String> oc, Reporter rprtr) throws IOException {
        int cnt = 0;
        String term = null;
        boolean issecure = false;
        boolean isbodyword = false;
        while (itrtr.hasNext()) {
            BytesWritable next = itrtr.next();
            MessageWord deobj = (MessageWord)ObjSerializer.deSerializeObj(next);
            if (deobj instanceof EmailAddress) {
                term = key.toString();
            }
            if (deobj instanceof SecureEmailAddress) {
                SecureEmailAddress sea = (SecureEmailAddress)deobj;
                issecure = true;
            }
            if (deobj instanceof BodyWord) {
                isbodyword = true;
            }
            ++cnt;
        }
        if (term != null) {
            if (isbodyword && issecure) {
                SecureTermMonitor.doubleEntryError();
            }
            oc.collect(key, "" + cnt + ":" + issecure);
        }
    }
}

