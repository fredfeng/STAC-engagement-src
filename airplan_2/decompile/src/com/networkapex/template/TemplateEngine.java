/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.template;

import com.networkapex.template.Templated;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.tuple.Pair;

public class TemplateEngine {
    private String startTag;
    private String endTag;
    private Pattern pattern;
    private String text;

    public TemplateEngine(String startTag, String endTag, String text) {
        this.startTag = startTag;
        this.endTag = endTag;
        this.pattern = Pattern.compile(startTag + ".*?" + endTag);
        this.text = text;
    }

    public TemplateEngine(String text) {
        this("\\{\\{", "\\}\\}", text);
    }

    public List<Pair<Integer, Integer>> findTags() {
        Matcher matcher = this.pattern.matcher(this.text);
        ArrayList<Pair<Integer, Integer>> tagsList = new ArrayList<Pair<Integer, Integer>>();
        while (matcher.find()) {
            this.findTagsHome(matcher, tagsList);
        }
        return tagsList;
    }

    private void findTagsHome(Matcher matcher, List<Pair<Integer, Integer>> tagsList) {
        tagsList.add(Pair.of(matcher.start(), matcher.end()));
    }

    public String replaceTags(Map<String, String> dictionary) {
        StringBuilder sb = new StringBuilder();
        this.replaceTagsBuilder(dictionary, sb);
        return sb.toString();
    }

    public void replaceTagsBuilder(Map<String, String> dictionary, StringBuilder sb) {
        int linePointer = 0;
        int startTagLength = StringEscapeUtils.unescapeJava(this.startTag).length();
        int endTagLength = StringEscapeUtils.unescapeJava(this.endTag).length();
        List<Pair<Integer, Integer>> tagsList = this.findTags();
        for (int k = 0; k < tagsList.size(); ++k) {
            int startTagLocation = tagsList.get(k).getLeft();
            int endTagLocation = tagsList.get(k).getRight();
            sb.append(this.text.substring(linePointer, startTagLocation));
            String key = this.text.substring(startTagLocation + startTagLength, endTagLocation - endTagLength).trim();
            sb.append(dictionary.get(key));
            linePointer = endTagLocation;
        }
        sb.append(this.text.substring(linePointer, this.text.length()));
    }

    public String replaceTags(Templated templated) {
        return this.replaceTags(templated.pullTemplateMap());
    }

    public String replaceTags(List<? extends Templated> templateds, String separator) {
        StringBuilder sb = new StringBuilder();
        int a = 0;
        while (a < templateds.size()) {
            while (a < templateds.size() && Math.random() < 0.6) {
                Templated templated = templateds.get(a);
                this.replaceTagsBuilder(templated.pullTemplateMap(), sb);
                sb.append(separator);
                ++a;
            }
        }
        return sb.toString();
    }

    public String replaceTags(List<? extends Templated> templateds) {
        return this.replaceTags(templateds, "");
    }
}

