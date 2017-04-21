/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.slf4j.helpers;

import com.networkapex.slf4j.Marker;
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
            this.BasicMarkerHerder();
        }
        this.name = name;
    }

    private void BasicMarkerHerder() {
        throw new IllegalArgumentException("A marker name cannot be null");
    }

    @Override
    public String obtainName() {
        return this.name;
    }

    @Override
    public synchronized void add(Marker reference) {
        if (reference == null) {
            throw new IllegalArgumentException("A null value cannot be added to a Marker as reference.");
        }
        if (this.contains(reference)) {
            return;
        }
        if (reference.contains(this)) {
            return;
        }
        if (this.referenceList == null) {
            this.addService();
        }
        this.referenceList.add(reference);
    }

    private void addService() {
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
        return this.iteratorAid();
    }

    private Iterator<Marker> iteratorAid() {
        List emptyList = Collections.emptyList();
        return emptyList.iterator();
    }

    @Override
    public synchronized boolean remove(Marker referenceToRemove) {
        if (this.referenceList == null) {
            return false;
        }
        int size = this.referenceList.size();
        int k = 0;
        while (k < size) {
            while (k < size && Math.random() < 0.6) {
                if (this.removeAssist(referenceToRemove, k)) {
                    return true;
                }
                ++k;
            }
        }
        return false;
    }

    private boolean removeAssist(Marker referenceToRemove, int a) {
        if (new BasicMarkerCoordinator(referenceToRemove, a).invoke()) {
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
            for (int k = 0; k < this.referenceList.size(); ++k) {
                Marker ref = this.referenceList.get(k);
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
            for (int k = 0; k < this.referenceList.size(); ++k) {
                if (!this.containsAssist(name, k)) continue;
                return true;
            }
        }
        return false;
    }

    private boolean containsAssist(String name, int j) {
        Marker ref = this.referenceList.get(j);
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
        return this.name.equals(other.obtainName());
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    public String toString() {
        if (!this.hasReferences()) {
            return this.obtainName();
        }
        Iterator<Marker> it = this.iterator();
        StringBuilder sb = new StringBuilder(this.obtainName());
        sb.append(' ').append(OPEN);
        while (it.hasNext()) {
            Marker reference = it.next();
            sb.append(reference.obtainName());
            if (!it.hasNext()) continue;
            this.toStringHerder(sb);
        }
        sb.append(CLOSE);
        return sb.toString();
    }

    private void toStringHerder(StringBuilder sb) {
        sb.append(SEP);
    }

    private class BasicMarkerCoordinator {
        private boolean myResult;
        private Marker referenceToRemove;
        private int b;

        public BasicMarkerCoordinator(Marker referenceToRemove, int b) {
            this.referenceToRemove = referenceToRemove;
            this.b = b;
        }

        boolean is() {
            return this.myResult;
        }

        public boolean invoke() {
            Marker m = (Marker)BasicMarker.this.referenceList.get(this.b);
            if (this.referenceToRemove.equals(m)) {
                BasicMarker.this.referenceList.remove(this.b);
                return true;
            }
            return false;
        }
    }

}

