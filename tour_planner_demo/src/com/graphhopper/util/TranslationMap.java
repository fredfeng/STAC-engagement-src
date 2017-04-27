/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

import com.graphhopper.util.Helper;
import com.graphhopper.util.Translation;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class TranslationMap {
    private static final List<String> LOCALES = Arrays.asList("ar", "ast", "bg", "ca", "cs_CZ", "da_DK", "de_DE", "el", "en_US", "es", "fa", "fil", "fi", "fr", "gl", "he", "hsb", "hu_HU", "it", "ja", "lt_LT", "ne", "nl", "pl_PL", "pt_BR", "pt_PT", "ro", "ru", "si", "sk", "sv_SE", "tr", "uk", "vi_VI", "zh_CN");
    private final Map<String, Translation> translations = new HashMap<String, Translation>();

    public TranslationMap doImport(File folder) {
        try {
            for (String locale : LOCALES) {
                TranslationHashMap trMap = new TranslationHashMap(Helper.getLocale(locale));
                trMap.doImport(new FileInputStream(new File(folder, locale + ".txt")));
                this.add(trMap);
            }
            this.postImportHook();
            return this;
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public TranslationMap doImport() {
        try {
            for (String locale : LOCALES) {
                TranslationHashMap trMap = new TranslationHashMap(Helper.getLocale(locale));
                trMap.doImport(TranslationMap.class.getResourceAsStream(locale + ".txt"));
                this.add(trMap);
            }
            this.postImportHook();
            return this;
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void add(Translation tr) {
        Locale locale = tr.getLocale();
        this.translations.put(locale.toString(), tr);
        if (!locale.getCountry().isEmpty() && !this.translations.containsKey(tr.getLanguage())) {
            this.translations.put(tr.getLanguage(), tr);
        }
        if ("iw".equals(locale.getLanguage())) {
            this.translations.put("he", tr);
        }
        if ("in".equals(locale.getLanguage())) {
            this.translations.put("id", tr);
        }
    }

    public Translation getWithFallBack(Locale locale) {
        Translation tr = this.get(locale.toString());
        if (tr == null && (tr = this.get(locale.getLanguage())) == null) {
            tr = this.get("en");
        }
        return tr;
    }

    public Translation get(String locale) {
        locale = locale.replace("-", "_");
        Translation tr = this.translations.get(locale);
        if (locale.contains("_") && tr == null) {
            tr = this.translations.get(locale.substring(0, 2));
        }
        return tr;
    }

    public static int countOccurence(String phrase, String splitter) {
        if (Helper.isEmpty(phrase)) {
            return 0;
        }
        return phrase.trim().split(splitter).length;
    }

    private void postImportHook() {
        Map<String, String> enMap = this.get("en").asMap();
        StringBuilder sb = new StringBuilder();
        for (Translation tr : this.translations.values()) {
            Map<String, String> trMap = tr.asMap();
            for (Map.Entry<String, String> enEntry : enMap.entrySet()) {
                String value = trMap.get(enEntry.getKey());
                if (Helper.isEmpty(value)) {
                    trMap.put(enEntry.getKey(), enEntry.getValue());
                    continue;
                }
                int expectedCount = TranslationMap.countOccurence(enEntry.getValue(), "\\%");
                if (expectedCount != TranslationMap.countOccurence(value, "\\%")) {
                    sb.append(tr.getLocale()).append(" - error in ").append(enEntry.getKey()).append("->").append(value).append("\n");
                    continue;
                }
                Object[] strs = new String[expectedCount];
                Arrays.fill(strs, "tmp");
                try {
                    String.format(value, strs);
                }
                catch (Exception ex) {
                    sb.append(tr.getLocale()).append(" - error ").append(ex.getMessage()).append("in ").append(enEntry.getKey()).append("->").append(value).append("\n");
                }
            }
        }
        if (sb.length() > 0) {
            System.out.println(sb);
            throw new IllegalStateException(sb.toString());
        }
    }

    public String toString() {
        return this.translations.toString();
    }

    public static class TranslationHashMap
    implements Translation {
        private final Map<String, String> map = new HashMap<String, String>();
        final Locale locale;

        public TranslationHashMap(Locale locale) {
            this.locale = locale;
        }

        public void clear() {
            this.map.clear();
        }

        @Override
        public Locale getLocale() {
            return this.locale;
        }

        @Override
        public String getLanguage() {
            return this.locale.getLanguage();
        }

        @Override
        public /* varargs */ String tr(String key, Object ... params) {
            String val = this.map.get(key.toLowerCase());
            if (Helper.isEmpty(val)) {
                return key;
            }
            return String.format(val, params);
        }

        public TranslationHashMap put(String key, String val) {
            String existing = this.map.put(key.toLowerCase(), val);
            if (existing != null) {
                throw new IllegalStateException("Cannot overwrite key " + key + " with " + val + ", was: " + existing);
            }
            return this;
        }

        public String toString() {
            return this.map.toString();
        }

        @Override
        public Map<String, String> asMap() {
            return this.map;
        }

        public TranslationHashMap doImport(InputStream is) {
            if (is == null) {
                throw new IllegalStateException("No input stream found in class path!?");
            }
            try {
                for (String line : Helper.readFile(new InputStreamReader(is, Helper.UTF_CS))) {
                    int index;
                    if (line.isEmpty() || line.startsWith("//") || line.startsWith("#") || (index = line.indexOf(61)) < 0) continue;
                    String key = line.substring(0, index);
                    if (key.isEmpty()) {
                        throw new IllegalStateException("No key provided:" + line);
                    }
                    String value = line.substring(index + 1);
                    if (value.isEmpty()) continue;
                    this.put(key, value);
                }
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            return this;
        }
    }

}

