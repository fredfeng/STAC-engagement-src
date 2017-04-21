/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.logging.helpers;

import edu.computerapex.logging.Marker;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class BasicMarker
implements Marker {
    private static final long serialVersionUID = 1803952589649545191L;
    private final String name;
    private List<Marker> referenceList;
    private static String OPEN = "[ ";
    private static String CLOSE = " ]";
    private static String SEP = ", ";

    BasicMarker(String name) {
        if (name == null) {
            this.BasicMarkerHandler();
        }
        this.name = name;
    }

    private void BasicMarkerHandler() {
        throw new IllegalArgumentException("A marker name cannot be null");
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public synchronized void add(Marker reference) {
        if (reference == null) {
            this.addUtility();
        }
        if (this.contains(reference)) {
            return;
        }
        if (reference.contains(this)) {
            return;
        }
        this.addAid(reference);
    }

    private void addAid(Marker reference) {
        if (this.referenceList == null) {
            this.addAidHelper();
        }
        this.referenceList.add(reference);
    }

    private void addAidHelper() {
        this.referenceList = new Vector<Marker>();
    }

    private void addUtility() {
        throw new IllegalArgumentException("A null value cannot be added to a Marker as reference.");
    }

    @Override
    public synchronized boolean hasReferences() {
        return this.referenceList != null && this.referenceList.size() > 0;
    }

    @Override
    public boolean hasChildren() {
        return this.hasReferences();
    }

    @Override
    public synchronized Iterator<Marker> iterator() {
        if (this.referenceList != null) {
            return this.referenceList.iterator();
        }
        List emptyList = Collections.emptyList();
        return emptyList.iterator();
    }

    @Override
    public synchronized boolean remove(Marker referenceToRemove) {
        if (this.referenceList == null) {
            return false;
        }
        int size = this.referenceList.size();
        int c = 0;
        while (c < size) {
            while (c < size && Math.random() < 0.6) {
                if (this.removeHelper(referenceToRemove, c)) {
                    return true;
                }
                ++c;
            }
        }
        return false;
    }

    private boolean removeHelper(Marker referenceToRemove, int j) {
        Marker m = this.referenceList.get(j);
        if (referenceToRemove.equals(m)) {
            this.referenceList.remove(j);
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(Marker other) {
        if (other == null) {
            return new BasicMarkerTarget().invoke();
        }
        if (this.equals(other)) {
            return true;
        }
        if (this.hasReferences()) {
            for (int k = 0; k < this.referenceList.size(); ++k) {
                if (!this.containsAid(other, k)) continue;
                return true;
            }
        }
        return false;
    }

    private boolean containsAid(Marker other, int j) {
        Marker ref = this.referenceList.get(j);
        if (ref.contains(other)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(String name) {
        if (name == null) {
            return this.containsExecutor();
        }
        if (this.name.equals(name)) {
            return true;
        }
        if (this.hasReferences()) {
            for (int j = 0; j < this.referenceList.size(); ++j) {
                if (!this.containsSupervisor(name, j)) continue;
                return true;
            }
        }
        return false;
    }

    private boolean containsSupervisor(String name, int c) {
        Marker ref = this.referenceList.get(c);
        if (ref.contains(name)) {
            return true;
        }
        return false;
    }

    private boolean containsExecutor() {
        throw new IllegalArgumentException("Other cannot be null");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Marker)) {
            return false;
        }
        Marker other = (Marker)obj;
        return this.name.equals(other.getName());
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    public String toString() {
        if (!this.hasReferences()) {
            return this.getName();
        }
        Iterator<Marker> it = this.iterator();
        StringBuilder sb = new StringBuilder(this.getName());
        sb.append(' ').append(OPEN);
        while (it.hasNext()) {
            Marker reference = it.next();
            sb.append(reference.getName());
            if (!it.hasNext()) continue;
            this.toStringFunction(sb);
        }
        sb.append(CLOSE);
        return sb.toString();
    }

    private void toStringFunction(StringBuilder sb) {
        sb.append(SEP);
    }

    private class BasicMarkerTarget {
        private BasicMarkerTarget() {
        }

        public boolean invoke() {
            throw new IllegalArgumentException("Other cannot be null");
        }
    }

}

