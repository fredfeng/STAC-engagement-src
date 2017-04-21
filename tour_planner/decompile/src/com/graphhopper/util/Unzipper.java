/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

import com.graphhopper.util.Helper;
import com.graphhopper.util.ProgressListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Unzipper {
    public void unzip(String from, boolean remove) throws IOException {
        String to = Helper.pruneFileEnd(from);
        this.unzip(from, to, remove);
    }

    public boolean unzip(String fromStr, String toStr, boolean remove) throws IOException {
        File from = new File(fromStr);
        if (!from.exists() || fromStr.equals(toStr)) {
            return false;
        }
        this.unzip(new FileInputStream(from), new File(toStr), null);
        if (remove) {
            Helper.removeDir(from);
        }
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void unzip(InputStream fromIs, File toFolder, ProgressListener progressListener) throws IOException {
        if (!toFolder.exists()) {
            toFolder.mkdirs();
        }
        long sumBytes = 0;
        ZipInputStream zis = new ZipInputStream(fromIs);
        try {
            ZipEntry ze = zis.getNextEntry();
            byte[] buffer = new byte[8192];
            while (ze != null) {
                if (ze.isDirectory()) {
                    new File(toFolder, ze.getName()).mkdir();
                } else {
                    double factor = 1.0;
                    if (ze.getCompressedSize() > 0 && ze.getSize() > 0) {
                        factor = (double)ze.getCompressedSize() / (double)ze.getSize();
                    }
                    File newFile = new File(toFolder, ze.getName());
                    FileOutputStream fos = new FileOutputStream(newFile);
                    try {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                            sumBytes = (long)((double)sumBytes + (double)len * factor);
                            if (progressListener == null) continue;
                            progressListener.update(sumBytes);
                        }
                    }
                    finally {
                        fos.close();
                    }
                }
                ze = zis.getNextEntry();
            }
            zis.closeEntry();
        }
        finally {
            zis.close();
        }
    }
}

