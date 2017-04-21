/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.Header;
import infotrader.parser.model.Multimedia;
import infotrader.parser.model.Note;
import infotrader.parser.model.Repository;
import infotrader.parser.model.Source;
import infotrader.parser.model.Submission;
import infotrader.parser.model.Submitter;
import infotrader.parser.model.Trailer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class InfoTrader
extends AbstractElement {
    private static final long serialVersionUID = -2972879346299316334L;
    private Header header = new Header();
    private final Map<String, Multimedia> multimedia = new HashMap<String, Multimedia>(0);
    private final Map<String, Note> notes = new HashMap<String, Note>(0);
    private final Map<String, Repository> repositories = new HashMap<String, Repository>(0);
    private final Map<String, Source> sources = new HashMap<String, Source>(0);
    private Submission submission = new Submission("@SUBMISSION@");
    public final Map<String, Submitter> submitters = new HashMap<String, Submitter>(0);
    private Trailer trailer = new Trailer();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        InfoTrader other = (InfoTrader)obj;
        if (this.header == null ? other.header != null : !this.header.equals(other.header)) {
            return false;
        }
        if (this.multimedia == null ? other.multimedia != null : !this.multimedia.equals(other.multimedia)) {
            return false;
        }
        if (this.getNotes() == null ? other.getNotes() != null : !this.getNotes().equals(other.getNotes())) {
            return false;
        }
        if (this.repositories == null ? other.repositories != null : !this.repositories.equals(other.repositories)) {
            return false;
        }
        if (this.sources == null ? other.sources != null : !this.sources.equals(other.sources)) {
            return false;
        }
        if (this.submission == null ? other.submission != null : !this.submission.equals(other.submission)) {
            return false;
        }
        if (this.submitters == null ? other.submitters != null : !this.submitters.equals(other.submitters)) {
            return false;
        }
        if (this.trailer == null ? other.trailer != null : !this.trailer.equals(other.trailer)) {
            return false;
        }
        return true;
    }

    public Header getHeader() {
        return this.header;
    }

    public Map<String, Multimedia> getMultimedia() {
        return this.multimedia;
    }

    public Map<String, Note> getNotes() {
        return this.notes;
    }

    public Map<String, Repository> getRepositories() {
        return this.repositories;
    }

    public Map<String, Source> getSources() {
        return this.sources;
    }

    public Submission getSubmission() {
        return this.submission;
    }

    public Map<String, Submitter> getSubmitters() {
        return this.submitters;
    }

    public Trailer getTrailer() {
        return this.trailer;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = super.hashCode();
        result = 31 * result + (this.header == null ? 0 : this.header.hashCode());
        result = 31 * result + (this.multimedia == null ? 0 : this.multimedia.hashCode());
        result = 31 * result + (this.getNotes() == null ? 0 : this.getNotes().hashCode());
        result = 31 * result + (this.repositories == null ? 0 : this.repositories.hashCode());
        result = 31 * result + (this.sources == null ? 0 : this.sources.hashCode());
        result = 31 * result + (this.submission == null ? 0 : this.submission.hashCode());
        result = 31 * result + (this.submitters == null ? 0 : this.submitters.hashCode());
        result = 31 * result + (this.trailer == null ? 0 : this.trailer.hashCode());
        return result;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    public void setTrailer(Trailer trailer) {
        this.trailer = trailer;
    }

    @Override
    public String toString() {
        int maxLen = 3;
        StringBuilder builder = new StringBuilder();
        builder.append("InfoTrader [");
        if (this.header != null) {
            builder.append("header=");
            builder.append(this.header);
            builder.append(", ");
        }
        if (this.multimedia != null) {
            builder.append("multimedia=");
            builder.append(this.toStringLimitCollection(this.multimedia.entrySet(), 3));
            builder.append(", ");
        }
        if (this.notes != null) {
            builder.append("notes=");
            builder.append(this.toStringLimitCollection(this.notes.entrySet(), 3));
            builder.append(", ");
        }
        if (this.repositories != null) {
            builder.append("repositories=");
            builder.append(this.toStringLimitCollection(this.repositories.entrySet(), 3));
            builder.append(", ");
        }
        if (this.sources != null) {
            builder.append("sources=");
            builder.append(this.toStringLimitCollection(this.sources.entrySet(), 3));
            builder.append(", ");
        }
        if (this.submission != null) {
            builder.append("submission=");
            builder.append(this.submission);
            builder.append(", ");
        }
        if (this.submitters != null) {
            builder.append("submitters=");
            builder.append(this.toStringLimitCollection(this.submitters.entrySet(), 3));
            builder.append(", ");
        }
        if (this.trailer != null) {
            builder.append("trailer=");
            builder.append(this.trailer);
        }
        builder.append("]");
        return builder.toString();
    }

    private String toStringLimitCollection(Collection<?> collection, int maxLen) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        Iterator iterator = collection.iterator();
        for (int i = 0; iterator.hasNext() && i < maxLen; ++i) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(iterator.next());
        }
        builder.append("]");
        return builder.toString();
    }
}

