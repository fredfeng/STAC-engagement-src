/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class Helper7 {
    public static final boolean UNMAP_SUPPORTED;

    public static String getBeanMemInfo() {
        java.lang.management.OperatingSystemMXBean mxbean = ManagementFactory.getOperatingSystemMXBean();
        OperatingSystemMXBean sunmxbean = (OperatingSystemMXBean)mxbean;
        long freeMemory = sunmxbean.getFreePhysicalMemorySize();
        long availableMemory = sunmxbean.getTotalPhysicalMemorySize();
        return "free:" + freeMemory / 0x100000 + ", available:" + availableMemory / 0x100000 + ", rfree:" + Runtime.getRuntime().freeMemory() / 0x100000;
    }

    public static void close(XMLStreamReader r) {
        try {
            if (r != null) {
                r.close();
            }
        }
        catch (XMLStreamException ex) {
            throw new RuntimeException("Couldn't close xml reader", ex);
        }
    }

    static {
        boolean v;
        try {
            Class.forName("sun.misc.Cleaner");
            Class.forName("java.nio.DirectByteBuffer").getMethod("cleaner", new Class[0]);
            v = true;
        }
        catch (Exception e) {
            v = false;
        }
        UNMAP_SUPPORTED = v;
    }
}

