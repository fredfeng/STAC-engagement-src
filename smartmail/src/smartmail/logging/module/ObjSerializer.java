/*
 * Decompiled with CFR 0_121.
 */
package smartmail.logging.module;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hadoop.io.BytesWritable;

public class ObjSerializer {
    public static Object deSerializeObj(BytesWritable value) throws IOException {
        byte[] bytes = value.getBytes();
        ByteArrayInputStream bos = new ByteArrayInputStream(bytes);
        ObjectInputStream oos = new ObjectInputStream(bos);
        Object ret = null;
        try {
            ret = oos.readObject();
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(ObjSerializer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    public static <T> byte[] serializeObj(T entity) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(entity);
        oos.flush();
        oos.close();
        return bos.toByteArray();
    }
}

