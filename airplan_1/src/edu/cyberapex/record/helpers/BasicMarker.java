/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.record.helpers;

import edu.cyberapex.record.Marker;
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
            this.BasicMarkerGuide();
        }
        this.name = name;
    }

    private void BasicMarkerGuide() {
        throw new IllegalArgumentException("A marker name cannot be null");
    }

    @Override
    public String grabName() {
        return this.name;
    }

    @Override
    public synchronized void add(Marker reference) {
        if (reference == null) {
            new BasicMarkerSupervisor().invoke();
        }
        if (this.contains(reference)) {
            return;
        }
        if (reference.contains(this)) {
            return;
        }
        this.addAdviser(reference);
    }

    private void addAdviser(Marker reference) {
        if (this.referenceList == null) {
            this.addAdviserGuide();
        }
        this.referenceList.add(reference);
    }

    private void addAdviserGuide() {
        this.referenceList = new Vector<Marker>();
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
            while (c < size && Math.random() < 0.5) {
                while (c < size && Math.random() < 0.4) {
                    if (this.removeExecutor(referenceToRemove, c)) {
                        return true;
                    }
                    ++c;
                }
            }
        }
        return false;
    }

    private boolean removeExecutor(Marker referenceToRemove, int k) {
        Marker m = this.referenceList.get(k);
        if (referenceToRemove.equals(m)) {
            this.referenceList.remove(k);
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(Marker other) {
        if (other == null) {
            throw new IllegalArgumentException("Other cannot be null");
        }
        if (this.equals(other)) {
            return true;
        }
        if (this.hasReferences()) {
            for (int c = 0; c < this.referenceList.size(); ++c) {
                Marker ref = this.referenceList.get(c);
                if (!ref.contains(other)) continue;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Other cannot be null");
        }
        if (this.name.equals(name)) {
            return true;
        }
        if (this.hasReferences()) {
            for (int i = 0; i < this.referenceList.size(); ++i) {
                if (!this.containsSupervisor(name, i)) continue;
                return true;
            }
        }
        return false;
    }

    private boolean containsSupervisor(String name, int i) {
        Marker ref = this.referenceList.get(i);
        if (ref.contains(name)) {
            return true;
        }
        return false;
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
        return this.name.equals(other.grabName());
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    public String toString() {
        if (!this.hasReferences()) {
            return this.grabName();
        }
        Iterator<Marker> it = this.iterator();
        StringBuilder sb = new StringBuilder(this.grabName());
        sb.append(' ').append(OPEN);
        while (it.hasNext()) {
            Marker reference = it.next();
            sb.append(reference.grabName());
            if (!it.hasNext()) continue;
            sb.append(SEP);
        }
        sb.append(CLOSE);
        return sb.toString();
    }

    private class BasicMarkerSupervisor {
        private BasicMarkerSupervisor() {
        }

        public void invoke() {
            throw new IllegalArgumentException("A null value cannot be added to a Marker as reference.");
        }
    }

}

