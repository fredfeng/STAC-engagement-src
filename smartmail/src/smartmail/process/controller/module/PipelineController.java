/*
 * Decompiled with CFR 0_121.
 */
package smartmail.process.controller.module;

import com.google.common.collect.Multimap;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.Reducer;
import smartmail.datamodel.EmailEvent;
import smartmail.email.manager.module.parser.email.xtra.EmailParserUtil;
import smartmail.logging.module.LogGenerator;
import smartmail.process.controller.module.EmailEventsMapper;
import smartmail.process.controller.module.EmailSessionReducer;
import smartmail.process.controller.module.Partitioner;
import smartmail.process.controller.module.StateHolder;
import smartmail.process.controller.module.seqfile.EmailParseException;
import smartmail.process.controller.module.seqfile.SequenceFileWriter;

public class PipelineController {
    public static void main(String[] args) throws IOException, EmailParseException {
        File f = new File("mail");
        List<EmailEvent> emails = EmailParserUtil.parseemails(f);
        EmailEventsMapper mapper = new EmailEventsMapper();
        EmailSessionReducer reducer = new EmailSessionReducer();
        StateHolder prepartitionstate = new StateHolder();
        SequenceFileWriter.writeEmail(prepartitionstate, emails, null);
        int partitionsize = 5;
        Partitioner epartition = new Partitioner(partitionsize, prepartitionstate);
        StateHolder masterstate = new StateHolder();
        masterstate.setMapper(mapper);
        masterstate.setReducer(reducer);
        for (int i = 0; i < epartition.sfmaplist.size(); ++i) {
            List<BytesWritable> wordspart = epartition.getPartition(i);
            SequenceFileWriter.writeWord(masterstate, wordspart, null);
            try {
                masterstate.callMapper();
                masterstate.sfclear();
                continue;
            }
            catch (IOException ex) {
                Logger.getLogger(PipelineController.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(-5);
            }
        }
        try {
            masterstate.callReducer();
        }
        catch (IOException ex) {
            Logger.getLogger(PipelineController.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-6);
        }
        LogGenerator.checkValuesandOutput(masterstate.getOutput(), emails);
    }
}

