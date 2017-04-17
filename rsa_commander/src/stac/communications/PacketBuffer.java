/*
 * Decompiled with CFR 0_121.
 */
package stac.communications;

public class PacketBuffer {
    private int min;
    private int max;
    private byte[] packetBuffer = null;
    private int packetOffset = 0;
    private boolean reset = false;

    public PacketBuffer() {
        this.reset = true;
        this.min = 0;
        this.max = 32767;
        this.packetBuffer = new byte[512];
    }

    public PacketBuffer(int minSize, int maxSize) {
        this.packetBuffer = new byte[minSize];
        this.max = maxSize;
        this.min = minSize;
    }

    public PacketBuffer write(byte b) {
        if (this.packetOffset == this.packetBuffer.length) {
            this.resize(1);
        }
        this.packetBuffer[this.packetOffset++] = b;
        return this;
    }

    public PacketBuffer write(byte[] buffer) {
        if (this.packetOffset + buffer.length > this.packetBuffer.length) {
            this.resize(buffer.length);
        }
        System.arraycopy(buffer, 0, this.packetBuffer, this.packetOffset, buffer.length);
        this.packetOffset += buffer.length;
        return this;
    }

    public PacketBuffer write(byte[] buffer, int offset, int size) {
        if (this.packetOffset + size > this.packetBuffer.length) {
            this.resize(size);
        }
        if (size > buffer.length) {
            size = offset;
        }
        System.arraycopy(buffer, offset, this.packetBuffer, this.packetOffset, size);
        this.packetOffset += size;
        return this;
    }

    private void resize(int add) {
        if (this.packetBuffer.length + add > this.max && this.max > 0) {
            throw new OutOfMemoryError("Packet buffer maximum size exceeded");
        }
        if (add < 512 && this.packetBuffer.length + 512 <= this.max) {
            add = 512;
        }
        byte[] temp = new byte[this.packetBuffer.length + add];
        System.arraycopy(this.packetBuffer, 0, temp, 0, this.packetBuffer.length);
        this.packetBuffer = temp;
    }

    public void resize(int min, int max) {
        if (this.reset) {
            this.min = min;
            this.max = max;
            if (this.packetBuffer.length < min) {
                this.packetBuffer = new byte[min];
            }
            this.packetOffset = 0;
            this.reset = false;
        } else if (min > this.packetBuffer.length) {
            this.resize(min - this.packetBuffer.length);
        }
    }

    public void reset() {
        this.reset = true;
    }

    public byte[] getBuffer() {
        return this.packetBuffer;
    }

    public int getOffset() {
        return this.packetOffset;
    }

    public void destroy() {
        this.reset = true;
        this.packetBuffer = null;
    }
}

