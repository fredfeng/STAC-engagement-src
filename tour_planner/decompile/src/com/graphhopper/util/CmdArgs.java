/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

import com.graphhopper.util.Helper;
import com.graphhopper.util.PMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class CmdArgs
extends PMap {
    public CmdArgs() {
    }

    public CmdArgs(Map<String, String> map) {
        super(map);
    }

    @Override
    public CmdArgs put(String key, Object str) {
        super.put(key, str);
        return this;
    }

    public static CmdArgs readFromConfig(String fileStr, String systemProperty) throws IOException {
        String configLocation;
        if (systemProperty.startsWith("-D")) {
            systemProperty = systemProperty.substring(2);
        }
        if (Helper.isEmpty(configLocation = System.getProperty(systemProperty))) {
            configLocation = fileStr;
        }
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        Helper.loadProperties(map, new InputStreamReader((InputStream)new FileInputStream(new File(configLocation).getAbsoluteFile()), Helper.UTF_CS));
        CmdArgs args = new CmdArgs();
        args.merge(map);
        Properties props = System.getProperties();
        for (Map.Entry e : props.entrySet()) {
            String k = (String)e.getKey();
            String v = (String)e.getValue();
            if (!k.startsWith("graphhopper.")) continue;
            k = k.substring("graphhopper.".length());
            args.put(k, v);
        }
        return args;
    }

    public static CmdArgs read(String[] args) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        for (String arg : args) {
            int index = arg.indexOf("=");
            if (index <= 0) continue;
            String key = arg.substring(0, index);
            if (key.startsWith("-")) {
                key = key.substring(1);
            }
            if (key.startsWith("-")) {
                key = key.substring(1);
            }
            String value = arg.substring(index + 1);
            map.put(key.toLowerCase(), value);
        }
        return new CmdArgs(map);
    }

    public static CmdArgs readFromConfigAndMerge(CmdArgs args, String configKey, String configSysAttr) {
        String configVal = args.get(configKey, "");
        if (!Helper.isEmpty(configVal)) {
            try {
                CmdArgs tmp = CmdArgs.readFromConfig(configVal, configSysAttr);
                tmp.merge(args);
                return tmp;
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return args;
    }
}

