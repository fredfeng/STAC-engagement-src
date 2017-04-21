/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.coll;

import com.graphhopper.util.Helper;
import java.io.PrintStream;

public class SparseIntIntArray {
    private static final int DELETED = Integer.MIN_VALUE;
    private boolean mGarbage = false;
    private int[] mKeys;
    private int[] mValues;
    private int mSize;

    public SparseIntIntArray() {
        this(10);
    }

    public SparseIntIntArray(int cap) {
        try {
            cap = Helper.idealIntArraySize(cap);
            this.mKeys = new int[cap];
            this.mValues = new int[cap];
            this.mSize = 0;
        }
        catch (OutOfMemoryError err) {
            System.err.println("requested capacity " + cap);
            throw err;
        }
    }

    private int[] getKeys() {
        int length = this.mKeys.length;
        int[] result = new int[length];
        System.arraycopy(this.mKeys, 0, result, 0, length);
        return result;
    }

    private void setValues(int[] keys, int uniqueValue) {
        int length = keys.length;
        for (int i = 0; i < length; ++i) {
            this.put(keys[i], uniqueValue);
        }
    }

    public int get(int key) {
        return this.get(key, -1);
    }

    private int get(int key, int valueIfKeyNotFound) {
        int i = SparseIntIntArray.binarySearch(this.mKeys, 0, this.mSize, key);
        if (i < 0 || this.mValues[i] == Integer.MIN_VALUE) {
            return valueIfKeyNotFound;
        }
        return this.mValues[i];
    }

    public void remove(int key) {
        int i = SparseIntIntArray.binarySearch(this.mKeys, 0, this.mSize, key);
        if (i >= 0 && this.mValues[i] != Integer.MIN_VALUE) {
            this.mValues[i] = Integer.MIN_VALUE;
            this.mGarbage = true;
        }
    }

    private void gc() {
        int n = this.mSize;
        int o = 0;
        int[] keys = this.mKeys;
        int[] values = this.mValues;
        for (int i = 0; i < n; ++i) {
            int val = values[i];
            if (val == Integer.MIN_VALUE) continue;
            if (i != o) {
                keys[o] = keys[i];
                values[o] = val;
            }
            ++o;
        }
        this.mGarbage = false;
        this.mSize = o;
    }

    public int put(int key, int value) {
        int i = SparseIntIntArray.binarySearch(this.mKeys, 0, this.mSize, key);
        if (i >= 0) {
            this.mValues[i] = value;
        } else {
            if ((i ^= -1) < this.mSize && this.mValues[i] == Integer.MIN_VALUE) {
                this.mKeys[i] = key;
                this.mValues[i] = value;
                return i;
            }
            if (this.mGarbage && this.mSize >= this.mKeys.length) {
                this.gc();
                i = ~ SparseIntIntArray.binarySearch(this.mKeys, 0, this.mSize, key);
            }
            if (this.mSize >= this.mKeys.length) {
                int n = Helper.idealIntArraySize(this.mSize + 1);
                int[] nkeys = new int[n];
                int[] nvalues = new int[n];
                System.arraycopy(this.mKeys, 0, nkeys, 0, this.mKeys.length);
                System.arraycopy(this.mValues, 0, nvalues, 0, this.mValues.length);
                this.mKeys = nkeys;
                this.mValues = nvalues;
            }
            if (this.mSize - i != 0) {
                System.arraycopy(this.mKeys, i, this.mKeys, i + 1, this.mSize - i);
                System.arraycopy(this.mValues, i, this.mValues, i + 1, this.mSize - i);
            }
            this.mKeys[i] = key;
            this.mValues[i] = value;
            ++this.mSize;
        }
        return i;
    }

    public int getSize() {
        if (this.mGarbage) {
            this.gc();
        }
        return this.mSize;
    }

    public int keyAt(int index) {
        if (this.mGarbage) {
            this.gc();
        }
        return this.mKeys[index];
    }

    public void setKeyAt(int index, int key) {
        if (this.mGarbage) {
            this.gc();
        }
        this.mKeys[index] = key;
    }

    public int valueAt(int index) {
        if (this.mGarbage) {
            this.gc();
        }
        return this.mValues[index];
    }

    public void setValueAt(int index, int value) {
        if (this.mGarbage) {
            this.gc();
        }
        this.mValues[index] = value;
    }

    private int indexOfKey(int key) {
        if (this.mGarbage) {
            this.gc();
        }
        return SparseIntIntArray.binarySearch(this.mKeys, 0, this.mSize, key);
    }

    private int indexOfValue(int value) {
        if (this.mGarbage) {
            this.gc();
        }
        for (int i = 0; i < this.mSize; ++i) {
            if (this.mValues[i] != value) continue;
            return i;
        }
        return -1;
    }

    public void clear() {
        int n = this.mSize;
        int[] values = this.mValues;
        for (int i = 0; i < n; ++i) {
            values[i] = -1;
        }
        this.mSize = 0;
        this.mGarbage = false;
    }

    public int append(int key, int value) {
        int pos;
        if (this.mSize != 0 && key <= this.mKeys[this.mSize - 1]) {
            return this.put(key, value);
        }
        if (this.mGarbage && this.mSize >= this.mKeys.length) {
            this.gc();
        }
        if ((pos = this.mSize) >= this.mKeys.length) {
            int n = Helper.idealIntArraySize(pos + 1);
            int[] nkeys = new int[n];
            int[] nvalues = new int[n];
            System.arraycopy(this.mKeys, 0, nkeys, 0, this.mKeys.length);
            System.arraycopy(this.mValues, 0, nvalues, 0, this.mValues.length);
            this.mKeys = nkeys;
            this.mValues = nvalues;
        }
        this.mKeys[pos] = key;
        this.mValues[pos] = value;
        this.mSize = pos + 1;
        return pos;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.getSize(); ++i) {
            int k = this.mKeys[i];
            int v = this.mValues[i];
            if (i > 0) {
                sb.append(",");
            }
            sb.append(k);
            sb.append(":");
            sb.append(v);
        }
        return sb.toString();
    }

    public int binarySearch(int key) {
        return SparseIntIntArray.binarySearch(this.mKeys, 0, this.mSize, key);
    }

    static int binarySearch(int[] a, int start, int len, int key) {
        int high = start + len;
        int low = start - 1;
        while (high - low > 1) {
            int guess = high + low >>> 1;
            if (a[guess] < key) {
                low = guess;
                continue;
            }
            high = guess;
        }
        if (high == start + len) {
            return ~ (start + len);
        }
        if (a[high] == key) {
            return high;
        }
        return ~ high;
    }

    private void checkIntegrity() {
        for (int i = 1; i < this.mSize; ++i) {
            if (this.mKeys[i] > this.mKeys[i - 1]) continue;
            for (int j = 0; j < this.mSize; ++j) {
                System.err.println("FAIL " + j + ": " + this.mKeys[j] + " -> " + this.mValues[j]);
            }
            throw new RuntimeException();
        }
    }
}

