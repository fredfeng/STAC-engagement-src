/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage;

import com.graphhopper.storage.Lock;
import com.graphhopper.storage.LockFactory;
import com.graphhopper.util.Helper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class NativeFSLockFactory
implements LockFactory {
    private File lockDir;

    public NativeFSLockFactory() {
    }

    public NativeFSLockFactory(File dir) {
        this.lockDir = dir;
    }

    @Override
    public void setLockDir(File lockDir) {
        this.lockDir = lockDir;
    }

    @Override
    public synchronized Lock create(String fileName, boolean writeAccess) {
        if (this.lockDir == null) {
            throw new RuntimeException("Set lockDir before creating " + (writeAccess ? "write" : "read") + " locks");
        }
        return new NativeLock(this.lockDir, fileName, writeAccess);
    }

    @Override
    public synchronized void forceRemove(String fileName, boolean writeAccess) {
        if (this.lockDir.exists()) {
            this.create(fileName, writeAccess).release();
            File lockFile = new File(this.lockDir, fileName);
            if (lockFile.exists() && !lockFile.delete()) {
                throw new RuntimeException("Cannot delete " + lockFile);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        File file = new File("tmp.lock");
        file.createNewFile();
        FileChannel channel = new RandomAccessFile(file, "r").getChannel();
        boolean shared = true;
        FileLock lock1 = channel.tryLock(0, Long.MAX_VALUE, shared);
        System.out.println("locked " + lock1);
        System.in.read();
        System.out.println("release " + lock1);
        lock1.release();
    }

    static class NativeLock
    implements Lock {
        private RandomAccessFile tmpRaFile;
        private FileChannel tmpChannel;
        private FileLock tmpLock;
        private final String name;
        private final File lockDir;
        private final File lockFile;
        private final boolean writeLock;
        private Exception failedReason;

        public NativeLock(File lockDir, String fileName, boolean writeLock) {
            this.name = fileName;
            this.lockDir = lockDir;
            this.lockFile = new File(lockDir, fileName);
            this.writeLock = writeLock;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public synchronized boolean tryLock() {
            if (this.lockExists()) {
                return false;
            }
            if (!this.lockDir.exists() && !this.lockDir.mkdirs()) {
                throw new RuntimeException("Directory " + this.lockDir + " does not exist and cannot created to place lock file there: " + this.lockFile);
            }
            if (!this.lockDir.isDirectory()) {
                throw new IllegalArgumentException("lockDir has to be a directory: " + this.lockDir);
            }
            try {
                this.failedReason = null;
                this.tmpRaFile = new RandomAccessFile(this.lockFile, "rw");
            }
            catch (IOException ex) {
                this.failedReason = ex;
                return false;
            }
            try {
                this.tmpChannel = this.tmpRaFile.getChannel();
                try {
                    this.tmpLock = this.tmpChannel.tryLock(0, Long.MAX_VALUE, !this.writeLock);
                }
                catch (Exception ex) {
                    this.failedReason = ex;
                }
                finally {
                    if (this.tmpLock == null) {
                        Helper.close(this.tmpChannel);
                        this.tmpChannel = null;
                    }
                }
            }
            finally {
                if (this.tmpChannel == null) {
                    Helper.close(this.tmpRaFile);
                    this.tmpRaFile = null;
                }
            }
            return this.lockExists();
        }

        private synchronized boolean lockExists() {
            return this.tmpLock != null;
        }

        @Override
        public synchronized boolean isLocked() {
            if (!this.lockFile.exists()) {
                return false;
            }
            if (this.lockExists()) {
                return true;
            }
            try {
                boolean obtained = this.tryLock();
                if (obtained) {
                    this.release();
                }
                return !obtained;
            }
            catch (Exception ex) {
                return false;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public synchronized void release() {
            if (this.lockExists()) {
                try {
                    this.failedReason = null;
                    this.tmpLock.release();
                }
                catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                finally {
                    this.tmpLock = null;
                    try {
                        this.tmpChannel.close();
                    }
                    catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    finally {
                        this.tmpChannel = null;
                        try {
                            this.tmpRaFile.close();
                        }
                        catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                        finally {
                            this.tmpRaFile = null;
                        }
                    }
                }
                this.lockFile.delete();
            }
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public Exception getObtainFailedReason() {
            return this.failedReason;
        }

        public String toString() {
            return this.lockFile.toString();
        }
    }

}

