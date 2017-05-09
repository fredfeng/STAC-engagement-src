/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage;

public class DAType {
    public static final DAType RAM = new DAType(MemRef.HEAP, false, false, true, false);
    public static final DAType RAM_INT = new DAType(MemRef.HEAP, false, true, true, false);
    public static final DAType RAM_STORE = new DAType(MemRef.HEAP, true, false, true, false);
    public static final DAType RAM_INT_STORE = new DAType(MemRef.HEAP, true, true, true, false);
    public static final DAType MMAP = new DAType(MemRef.MMAP, true, false, true, false);
    public static final DAType MMAP_RO = new DAType(MemRef.MMAP, true, false, false, false);
    public static final DAType UNSAFE_STORE = new DAType(MemRef.UNSAFE, true, false, true, false);
    private final MemRef memRef;
    private final boolean storing;
    private final boolean integ;
    private final boolean synched;
    private final boolean allowWrites;

    public DAType(DAType type, boolean synched) {
        this(type.getMemRef(), type.isStoring(), type.isInteg(), type.isAllowWrites(), synched);
        if (!synched) {
            throw new IllegalStateException("constructor can only be used with synched=true");
        }
        if (type.isSynched()) {
            throw new IllegalStateException("something went wrong as DataAccess object is already synched!?");
        }
    }

    public DAType(MemRef memRef, boolean storing, boolean integ, boolean allowWrites, boolean synched) {
        this.memRef = memRef;
        this.storing = storing;
        this.integ = integ;
        this.allowWrites = allowWrites;
        this.synched = synched;
    }

    MemRef getMemRef() {
        return this.memRef;
    }

    public boolean isAllowWrites() {
        return this.allowWrites;
    }

    public boolean isInMemory() {
        return this.memRef == MemRef.HEAP;
    }

    public boolean isMMap() {
        return this.memRef == MemRef.MMAP;
    }

    public boolean isStoring() {
        return this.storing;
    }

    public boolean isInteg() {
        return this.integ;
    }

    public boolean isSynched() {
        return this.synched;
    }

    public String toString() {
        String str = this.getMemRef() == MemRef.MMAP ? "MMAP" : (this.getMemRef() == MemRef.HEAP ? "RAM" : "UNSAFE");
        if (this.isInteg()) {
            str = str + "_INT";
        }
        if (this.isStoring()) {
            str = str + "_STORE";
        }
        if (this.isSynched()) {
            str = str + "_SYNC";
        }
        return str;
    }

    public static DAType fromString(String dataAccess) {
        DAType type = (dataAccess = dataAccess.toUpperCase()).contains("MMAP") ? MMAP : (dataAccess.contains("UNSAFE") ? UNSAFE_STORE : (dataAccess.contains("RAM_STORE") ? RAM_STORE : RAM));
        if (dataAccess.contains("SYNC")) {
            type = new DAType(type, true);
        }
        return type;
    }

    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + 37 * this.memRef.hashCode();
        hash = 59 * hash + (this.storing ? 1 : 0);
        hash = 59 * hash + (this.integ ? 1 : 0);
        hash = 59 * hash + (this.synched ? 1 : 0);
        return hash;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        DAType other = (DAType)obj;
        if (this.memRef != other.memRef) {
            return false;
        }
        if (this.storing != other.storing) {
            return false;
        }
        if (this.integ != other.integ) {
            return false;
        }
        if (this.synched != other.synched) {
            return false;
        }
        return true;
    }

    public static enum MemRef {
        HEAP,
        MMAP,
        UNSAFE;
        

        private MemRef() {
        }
    }

}

