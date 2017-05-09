/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage;

import com.graphhopper.storage.Directory;
import com.graphhopper.storage.Graph;
import com.graphhopper.storage.Storable;

public interface GraphExtension
extends Storable<GraphExtension> {
    public boolean isRequireNodeField();

    public boolean isRequireEdgeField();

    public int getDefaultNodeFieldValue();

    public int getDefaultEdgeFieldValue();

    public void init(Graph var1, Directory var2);

    public void setSegmentSize(int var1);

    public GraphExtension copyTo(GraphExtension var1);

    public static class NoOpExtension
    implements GraphExtension {
        @Override
        public boolean isRequireNodeField() {
            return false;
        }

        @Override
        public boolean isRequireEdgeField() {
            return false;
        }

        @Override
        public int getDefaultNodeFieldValue() {
            return 0;
        }

        @Override
        public int getDefaultEdgeFieldValue() {
            return 0;
        }

        @Override
        public void init(Graph grap, Directory dir) {
        }

        @Override
        public GraphExtension create(long byteCount) {
            return this;
        }

        @Override
        public boolean loadExisting() {
            return true;
        }

        @Override
        public void setSegmentSize(int bytes) {
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() {
        }

        @Override
        public long getCapacity() {
            return 0;
        }

        @Override
        public GraphExtension copyTo(GraphExtension extStorage) {
            return extStorage;
        }

        public String toString() {
            return "NoExt";
        }

        @Override
        public boolean isClosed() {
            return false;
        }
    }

}

