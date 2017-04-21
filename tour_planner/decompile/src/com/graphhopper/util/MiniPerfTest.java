/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

import java.text.DecimalFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MiniPerfTest {
    protected Logger logger;
    private int counts;
    private double fullTime;
    private double max;
    private double min;
    private int dummySum;

    public MiniPerfTest() {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.counts = 100;
        this.fullTime = 0.0;
        this.min = Double.MAX_VALUE;
    }

    public MiniPerfTest start() {
        int warmupCount = Math.max(1, this.counts / 3);
        for (int i = 0; i < warmupCount; ++i) {
            this.dummySum += this.doCalc(true, i);
        }
        long startFull = System.nanoTime();
        for (int i = 0; i < this.counts; ++i) {
            long start = System.nanoTime();
            this.dummySum += this.doCalc(false, i);
            long time = System.nanoTime() - start;
            if ((double)time < this.min) {
                this.min = time;
            }
            if ((double)time <= this.max) continue;
            this.max = time;
        }
        this.fullTime = System.nanoTime() - startFull;
        this.logger.info("dummySum:" + this.dummySum);
        return this;
    }

    public MiniPerfTest setIterations(int counts) {
        this.counts = counts;
        return this;
    }

    public double getMin() {
        return this.min / 1000000.0;
    }

    public double getMax() {
        return this.max / 1000000.0;
    }

    public double getSum() {
        return this.fullTime / 1000000.0;
    }

    public double getMean() {
        return this.getSum() / (double)this.counts;
    }

    public String getReport() {
        return "sum:" + this.nf(this.getSum() / 1000.0) + "s, time/call:" + this.nf(this.getMean() / 1000.0) + "s";
    }

    public String nf(Number num) {
        return new DecimalFormat("#.#").format(num);
    }

    public abstract int doCalc(boolean var1, int var2);
}

