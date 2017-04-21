/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.reader.dem;

import com.graphhopper.storage.DAType;
import java.io.File;

public interface ElevationProvider {
    public static final ElevationProvider NOOP = new ElevationProvider(){

        @Override
        public double getEle(double lat, double lon) {
            return Double.NaN;
        }

        @Override
        public ElevationProvider setCacheDir(File cacheDir) {
            return this;
        }

        @Override
        public ElevationProvider setBaseURL(String baseURL) {
            return this;
        }

        @Override
        public ElevationProvider setDAType(DAType daType) {
            return this;
        }

        @Override
        public void release() {
        }

        @Override
        public void setCalcMean(boolean eleCalcMean) {
        }
    };

    public double getEle(double var1, double var3);

    public ElevationProvider setBaseURL(String var1);

    public ElevationProvider setCacheDir(File var1);

    public ElevationProvider setDAType(DAType var1);

    public void setCalcMean(boolean var1);

    public void release();

}

