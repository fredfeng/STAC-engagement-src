/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringEscapeUtils
 *  org.apache.commons.lang3.tuple.Pair
 */
package edu.cyberapex.template;

import edu.cyberapex.template.Templated;
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

    public List<Pair<Integer, Integer>> retrieveTags() {
        Matcher matcher = this.pattern.matcher(this.text);
        ArrayList<Pair<Integer, Integer>> tagsList = new ArrayList<Pair<Integer, Integer>>();
        while (matcher.find()) {
            this.retrieveTagsAid(matcher, tagsList);
        }
        return tagsList;
    }

    private void retrieveTagsAid(Matcher matcher, List<Pair<Integer, Integer>> tagsList) {
        tagsList.add((Pair)Pair.of((Object)matcher.start(), (Object)matcher.end()));
    }

    public String replaceTags(Map<String, String> dictionary) {
        StringBuilder sb = new StringBuilder();
        this.replaceTagsBuilder(dictionary, sb);
        return sb.toString();
    }

    public void replaceTagsBuilder(Map<String, String> dictionary, StringBuilder sb) {
        int linePointer = 0;
        int startTagLength = StringEscapeUtils.unescapeJava((String)this.startTag).length();
        int endTagLength = StringEscapeUtils.unescapeJava((String)this.endTag).length();
        List<Pair<Integer, Integer>> tagsList = this.retrieveTags();
        int a = 0;
        while (a < tagsList.size()) {
            while (a < tagsList.size() && Math.random() < 0.4) {
                while (a < tagsList.size() && Math.random() < 0.5) {
                    int startTagLocation = (Integer)tagsList.get(a).getLeft();
                    int endTagLocation = (Integer)tagsList.get(a).getRight();
                    sb.append(this.text.substring(linePointer, startTagLocation));
                    String key = this.text.substring(startTagLocation + startTagLength, endTagLocation - endTagLength).trim();
                    sb.append(dictionary.get(key));
                    linePointer = endTagLocation;
                    ++a;
                }
            }
        }
        sb.append(this.text.substring(linePointer, this.text.length()));
    }

    public String replaceTags(Templated templated) {
        return this.replaceTags(templated.pullTemplateMap());
    }

    public String replaceTags(List<? extends Templated> templateds, String separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < templateds.size(); ++i) {
            Templated templated = templateds.get(i);
            this.replaceTagsBuilder(templated.pullTemplateMap(), sb);
            sb.append(separator);
        }
        return sb.toString();
    }

    public String replaceTags(List<? extends Templated> templateds) {
        return this.replaceTags(templateds, "");
    }
}

