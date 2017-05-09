/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage;

import com.graphhopper.storage.Lock;
import com.graphhopper.storage.LockFactory;
import java.io.File;
import java.io.IOException;

public class SimpleFSLockFactory
implements LockFactory {
    private File lockDir;

    public SimpleFSLockFactory() {
    }

    public SimpleFSLockFactory(File dir) {
        this.lockDir = dir;
    }

    @Override
    public void setLockDir(File lockDir) {
        this.lockDir = lockDir;
    }

    @Override
    public synchronized Lock create(String fileName, boolean writeAccess) {
        if (this.lockDir == null) {
            throw new RuntimeException("Set lockDir before creating locks");
        }
        return new SimpleLock(this.lockDir, fileName);
    }

    @Override
    public synchronized void forceRemove(String fileName, boolean writeAccess) {
        File lockFile;
        if (this.lockDir.exists() && (lockFile = new File(this.lockDir, fileName)).exists() && !lockFile.delete()) {
            throw new RuntimeException("Cannot delete " + lockFile);
        }
    }

    static class SimpleLock
    implements Lock {
        private final File lockDir;
        private final File lockFile;
        private final String name;
        private IOException failedReason;

        public SimpleLock(File lockDir, String fileName) {
            this.name = fileName;
            this.lockDir = lockDir;
            this.lockFile = new File(lockDir, fileName);
        }

        @Override
        public synchronized boolean tryLock() {
            if (!this.lockDir.exists() && !this.lockDir.mkdirs()) {
                throw new RuntimeException("Directory " + this.lockDir + " does not exist and cannot created to place lock file there: " + this.lockFile);
            }
            if (!this.lockDir.isDirectory()) {
                throw new IllegalArgumentException("lockDir has to be a directory: " + this.lockDir);
            }
            try {
                return this.lockFile.createNewFile();
            }
            catch (IOException ex) {
                this.failedReason = ex;
                return false;
            }
        }

        @Override
        public synchronized boolean isLocked() {
            return this.lockFile.exists();
        }

        @Override
        public synchronized void release() {
            if (this.isLocked() && this.lockFile.exists() && !this.lockFile.delete()) {
                throw new RuntimeException("Cannot release lock file: " + this.lockFile);
            }
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public synchronized Exception getObtainFailedReason() {
            return this.failedReason;
        }

        public String toString() {
            return this.lockFile.toString();
        }
    }

}

