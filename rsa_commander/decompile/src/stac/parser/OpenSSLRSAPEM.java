/*
 * Decompiled with CFR 0_121.
 */
package stac.parser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.TreeSet;
import stac.codecs.DecInputStream;
import stac.util.Hex;

public class OpenSSLRSAPEM {
    private DER_TYPE type;
    private INTEGER version;
    private INTEGER modulus;
    private INTEGER publicExponent;
    private INTEGER privateExponent;
    private INTEGER prime1;
    private INTEGER prime2;
    private INTEGER exponent1;
    private INTEGER exponent2;
    private INTEGER coefficient;

    public OpenSSLRSAPEM(INTEGER publicExponent, INTEGER modulus) {
        this.type = DER_TYPE.PUBLIC_KEY;
        this.publicExponent = publicExponent;
        this.modulus = modulus;
    }

    public OpenSSLRSAPEM(INTEGER publicExponent, INTEGER modulus, INTEGER privateExponent) {
        this.type = DER_TYPE.PRIVATE_KEY;
        this.publicExponent = publicExponent;
        this.privateExponent = privateExponent;
        this.modulus = modulus;
    }

    public OpenSSLRSAPEM(INTEGER publicExponent, INTEGER modulus, INTEGER privateExponent, INTEGER prime1, INTEGER prime2, INTEGER exponent1, INTEGER exponent2, INTEGER coefficient) {
        this.type = DER_TYPE.PRIVATE_KEY;
        this.publicExponent = publicExponent;
        this.privateExponent = privateExponent;
        this.modulus = modulus;
        this.prime1 = prime1;
        this.prime2 = prime2;
        this.exponent1 = exponent1;
        this.exponent2 = exponent2;
        this.coefficient = coefficient;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public OpenSSLRSAPEM(InputStream is) throws IOException {
        InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8.toString());
        BufferedReader reader = new BufferedReader(isr);
        String s = reader.readLine();
        if (s == null) {
            throw new IOException("Failed to read first line of input stream");
        }
        this.type = DER_TYPE.typeOf(s);
        String armoredContent = this.readUntilFooter(reader);
        DecInputStream decoded = new DecInputStream(new ByteArrayInputStream(armoredContent.getBytes(StandardCharsets.UTF_8)));
        DER.DERTLV dertlv = DER.readDER(decoded);
        if (this.type == DER_TYPE.PRIVATE_KEY) {
            if (dertlv.getTag() != 48) throw new IOException("Bad Private Key");
            ArrayList retrieve = (ArrayList)dertlv.retrieve();
            if (retrieve.size() < 9) {
                throw new IOException("Bad Private Key: Missing required params");
            }
            if (((DER.DERTLV)retrieve.get(0)).getTag() != 2) {
                throw new IOException("Bad Private Key: Invalid version");
            }
            this.version = (INTEGER)((DER.DERTLV)retrieve.get(0)).retrieve();
            if (((DER.DERTLV)retrieve.get(1)).getTag() != 2) {
                throw new IOException("Bad Private Key: Invalid modulus");
            }
            this.modulus = (INTEGER)((DER.DERTLV)retrieve.get(1)).retrieve();
            if (((DER.DERTLV)retrieve.get(2)).getTag() != 2) {
                throw new IOException("Bad Private Key: Invalid publicExponent");
            }
            this.publicExponent = (INTEGER)((DER.DERTLV)retrieve.get(2)).retrieve();
            if (((DER.DERTLV)retrieve.get(3)).getTag() != 2) {
                throw new IOException("Bad Private Key: Invalid privateExponent");
            }
            this.privateExponent = (INTEGER)((DER.DERTLV)retrieve.get(3)).retrieve();
            if (((DER.DERTLV)retrieve.get(4)).getTag() != 2) {
                throw new IOException("Bad Private Key: Invalid prime1");
            }
            this.prime1 = (INTEGER)((DER.DERTLV)retrieve.get(4)).retrieve();
            if (((DER.DERTLV)retrieve.get(5)).getTag() != 2) {
                throw new IOException("Bad Private Key: Invalid prime2");
            }
            this.prime2 = (INTEGER)((DER.DERTLV)retrieve.get(5)).retrieve();
            if (((DER.DERTLV)retrieve.get(6)).getTag() != 2) {
                throw new IOException("Bad Private Key: Invalid exponent1");
            }
            this.exponent1 = (INTEGER)((DER.DERTLV)retrieve.get(6)).retrieve();
            if (((DER.DERTLV)retrieve.get(7)).getTag() != 2) {
                throw new IOException("Bad Private Key: Invalid exponent2");
            }
            this.exponent2 = (INTEGER)((DER.DERTLV)retrieve.get(7)).retrieve();
            if (((DER.DERTLV)retrieve.get(8)).getTag() != 2) {
                throw new IOException("Bad Private Key: Invalid coefficient");
            }
            this.coefficient = (INTEGER)((DER.DERTLV)retrieve.get(8)).retrieve();
            return;
        }
        if (this.type != DER_TYPE.PUBLIC_KEY) throw new IOException("Unknown Key Type");
        if (dertlv.getTag() != 48) throw new IOException("Bad Public Key");
        ArrayList sequence = (ArrayList)dertlv.retrieve();
        if (((DER.DERTLV)sequence.get(0)).getTag() != 48) {
            throw new IOException("Bad Public Key: Invalid Public Key");
        }
        ArrayList OIDSeq = (ArrayList)((DER.DERTLV)sequence.get(0)).retrieve();
        if (((DER.DERTLV)OIDSeq.get(0)).getTag() != 6) throw new IOException("Bad Public Key: Invalid OID");
        try {
            ((DER.DERTLV)OIDSeq.get(0)).retrieve();
        }
        catch (RuntimeException e) {
            throw new IOException("Bad Public Key: Invalid OID");
        }
        if (((DER.DERTLV)sequence.get(1)).getTag() != 3) return;
        BITSTRING bs = (BITSTRING)((DER.DERTLV)sequence.get(1)).retrieve();
        ByteArrayInputStream bais = new ByteArrayInputStream(bs.toByteArray());
        DER.DERTLV publicKeyDoc = DER.readDER(bais);
        if (publicKeyDoc.getTag() != 48) {
            throw new IOException("Bad Public Key: Invalid Public Key '" + Hex.bytesToHex(publicKeyDoc.value) + "'");
        }
        ArrayList retrieve = (ArrayList)publicKeyDoc.retrieve();
        if (retrieve.size() != 2) {
            throw new IOException("Bad Public Key: Missing required params");
        }
        if (((DER.DERTLV)retrieve.get(0)).getTag() != 2) {
            throw new IOException("Bad Private Key: Invalid modulus");
        }
        this.modulus = (INTEGER)((DER.DERTLV)retrieve.get(0)).retrieve();
        if (((DER.DERTLV)retrieve.get(1)).getTag() != 2) {
            throw new IOException("Bad Private Key: Invalid publicExponent");
        }
        this.publicExponent = (INTEGER)((DER.DERTLV)retrieve.get(1)).retrieve();
    }

    public OpenSSLRSAPEM(File file) throws IOException {
        this(new FileInputStream(file));
        if (this.publicExponent.compareTo(3) < 0 || this.publicExponent.compareTo(65537) > 0 || this.modulus.getInternalBig().bitCount() < 0 || this.modulus.getInternalBig().bitCount() > 512) {
            throw new RuntimeException("Invalid OpenSSL key.");
        }
    }

    public String toString() {
        return "OpenSSLRSAPEM{type=" + (Object)((Object)this.type) + ", version=" + this.version + ", modulus=" + this.modulus + ", publicExponent=" + this.publicExponent + ", privateExponent=" + this.privateExponent + ", prime1=" + this.prime1 + ", prime2=" + this.prime2 + ", exponent1=" + this.exponent1 + ", exponent2=" + this.exponent2 + ", coefficient=" + this.coefficient + '}';
    }

    private String readUntilFooter(BufferedReader reader) throws IOException {
        String t;
        String footer = this.type != null ? this.type.getFooter() : null;
        StringBuilder b = new StringBuilder();
        while (!Objects.equals(t = reader.readLine(), footer)) {
            b.append(t);
        }
        return b.toString();
    }

    public INTEGER getModulus() {
        return this.modulus;
    }

    public INTEGER getPublicExponent() {
        return this.publicExponent;
    }

    public INTEGER getPrivateExponent() {
        return this.privateExponent;
    }

    public DER_TYPE getType() {
        return this.type;
    }

    public INTEGER getVersion() {
        return this.version;
    }

    public INTEGER getPrime1() {
        return this.prime1;
    }

    public INTEGER getPrime2() {
        return this.prime2;
    }

    public INTEGER getExponent1() {
        return this.exponent1;
    }

    public INTEGER getExponent2() {
        return this.exponent2;
    }

    public INTEGER getCoefficient() {
        return this.coefficient;
    }

    public static class INTEGER
    implements Comparable {
        public static final BigInteger INT_MAX = BigInteger.valueOf(Integer.MAX_VALUE);
        public static final BigInteger INT_MIN = BigInteger.valueOf(Integer.MIN_VALUE);
        private static final SecureRandom SECURE_PRNG = new SecureRandom();
        private Integer internal = null;
        private BigInteger internalBig = null;

        public INTEGER() {
            this.set(0);
        }

        public INTEGER(BigInteger bigInteger) {
            this.set(bigInteger.toByteArray());
        }

        public INTEGER(INTEGER other) {
            this.set(other.getBytes());
        }

        public INTEGER(Integer internal) {
            this.set(internal);
        }

        public static INTEGER valueOf(String in) {
            INTEGER i = new INTEGER();
            i.set(in);
            return i;
        }

        public static INTEGER valueOf(byte[] bytes, int offset, int length) {
            if (offset == 0 && length == bytes.length) {
                return INTEGER.valueOf(bytes);
            }
            byte[] read = new byte[length];
            System.arraycopy(bytes, offset, read, 0, length);
            INTEGER i = new INTEGER();
            i.set(read);
            return i;
        }

        public static INTEGER valueOf(byte[] bytes) {
            INTEGER integer = new INTEGER();
            integer.set(bytes);
            return integer;
        }

        public static INTEGER valueOfUnsigned(byte[] bytes) {
            INTEGER integer = new INTEGER();
            integer.setUnsigned(bytes);
            return integer;
        }

        public static INTEGER valueOfUnsigned(byte[] bytes, int off, int length) {
            INTEGER integer = new INTEGER();
            byte[] intern = new byte[length];
            System.arraycopy(bytes, off, intern, 0, length);
            integer.setUnsigned(intern);
            return integer;
        }

        public static INTEGER valueOf(long in) {
            INTEGER i = new INTEGER();
            i.set(in);
            return i;
        }

        public static INTEGER randomShort() {
            return INTEGER.valueOf(SECURE_PRNG.nextInt(32767));
        }

        public static INTEGER randomLong() {
            return INTEGER.valueOf(SECURE_PRNG.nextLong());
        }

        public static INTEGER randomInt() {
            return INTEGER.valueOf(SECURE_PRNG.nextInt());
        }

        public static INTEGER randomINTEGER(int size) {
            byte[] bytes = new byte[size];
            SECURE_PRNG.nextBytes(bytes);
            return INTEGER.valueOf(bytes);
        }

        public static INTEGER randomINTEGER() {
            return INTEGER.randomINTEGER(32);
        }

        public void set(String in) {
            boolean b16 = false;
            if (in.contains(":")) {
                b16 = true;
                in = in.replaceAll("[:\n ]", "");
            }
            if (b16) {
                if (in.length() > 4) {
                    this.internalBig = new BigInteger(in, 16);
                } else {
                    byte[] bytes = in.getBytes(StandardCharsets.UTF_8);
                    this.internal = 0;
                    for (int i = 0; i < bytes.length; ++i) {
                        this.internal = this.internal & bytes[0] << i;
                    }
                }
            } else {
                try {
                    this.internal = Integer.parseInt(in);
                }
                catch (NumberFormatException e) {
                    this.internalBig = new BigInteger(in);
                }
            }
        }

        public void setUnsigned(byte[] bytes) {
            byte[] these_bytes;
            if ((bytes[0] & 128) == 128) {
                these_bytes = new byte[bytes.length + 1];
                for (int i = 0; i < bytes.length; ++i) {
                    these_bytes[i + 1] = bytes[i];
                }
            } else {
                these_bytes = bytes;
            }
            this.set(these_bytes);
        }

        public void set(byte[] bytes) {
            BigInteger bigInteger = new BigInteger(bytes);
            if (bigInteger.compareTo(INT_MAX) > 0 || bigInteger.compareTo(INT_MIN) < 0) {
                this.internalBig = bigInteger;
                this.internal = null;
            } else {
                this.internalBig = null;
                this.internal = bigInteger.intValue();
            }
        }

        public void setUnsigned(byte b) {
            this.set(b & 255);
        }

        public void setUnsigned(int i) {
            this.set((long)i & -1);
        }

        public void set(long i) {
            this.internalBig = BigInteger.valueOf(i);
            this.internal = null;
        }

        public void set(int i) {
            this.internalBig = null;
            this.internal = i;
        }

        public void set(BigInteger i) {
            this.internalBig = i;
            this.internal = null;
        }

        public boolean isBig() {
            return this.internalBig != null;
        }

        public Integer getInternal() {
            if (!this.isBig()) {
                return this.internal;
            }
            return this.internalBig.intValue();
        }

        public BigInteger getInternalBig() {
            if (this.isBig()) {
                return this.internalBig;
            }
            this.internalBig = BigInteger.valueOf(this.internal.intValue());
            return this.internalBig;
        }

        public Object get() {
            return this.isBig() ? this.internalBig : this.internal;
        }

        public int intExactValue() {
            if (this.isBig()) {
                return this.internalBig.intValue();
            }
            return this.internal;
        }

        public INTEGER modPow(INTEGER pow, INTEGER modulus) {
            if (pow.isBig()) {
                return new INTEGER(this.getInternalBig().modPow(pow.getInternalBig(), modulus.getInternalBig()));
            }
            return new INTEGER(this.getInternalBig().pow(pow.getInternal()).mod(modulus.getInternalBig()));
        }

        public int compareTo(Object o) {
            INTEGER oo;
            if (o instanceof BigInteger) {
                oo = new INTEGER((BigInteger)o);
            } else if (o instanceof INTEGER) {
                if (this.compareTo(0) == 0) {
                    return - ((INTEGER)o).compareTo(0);
                }
                oo = new INTEGER(((INTEGER)o).getInternalBig());
            } else if (o instanceof Integer) {
                oo = INTEGER.valueOf(((Integer)o).intValue());
            } else if (o instanceof Number) {
                oo = INTEGER.valueOf(o.toString());
            } else {
                throw new RuntimeException("Something isn't implemented");
            }
            if (this.isBig() || oo.isBig()) {
                BigInteger loc = !this.isBig() ? BigInteger.valueOf(this.internal.intValue()) : this.internalBig;
                if (oo.isBig()) {
                    return loc.compareTo(oo.getInternalBig());
                }
                return loc.compareTo(BigInteger.valueOf(oo.getInternal().intValue()));
            }
            if (this.internal.longValue() > oo.internal.longValue()) {
                return 1;
            }
            if (this.internal.longValue() == oo.internal.longValue()) {
                return 0;
            }
            if (this.internal.longValue() < oo.internal.longValue()) {
                return -1;
            }
            throw new RuntimeException("Something isn't implemented");
        }

        public String toString() {
            if (this.isBig()) {
                return this.internalBig.toString();
            }
            return this.internal.toString();
        }

        public byte[] getBytes() {
            if (this.isBig()) {
                return this.internalBig.toByteArray();
            }
            return BigInteger.valueOf(this.internal.intValue()).toByteArray();
        }

        public int signum() {
            if (this.isBig()) {
                return this.getInternalBig().signum();
            }
            if (this.getInternal() == 0) {
                return 0;
            }
            if (this.getInternal() > 0) {
                return 1;
            }
            return -1;
        }

        public byte[] getBytes(int minSize) {
            if (this.signum() >= 0) {
                byte[] arr = this.isBig() ? this.internalBig.toByteArray() : BigInteger.valueOf(this.internal.intValue()).toByteArray();
                if (arr.length < minSize) {
                    byte[] bytes = new byte[minSize];
                    System.arraycopy(arr, 0, bytes, arr.length < minSize ? minSize - arr.length : 0, arr.length);
                    arr = bytes;
                }
                return arr;
            }
            byte[] arr = this.isBig() ? this.internalBig.negate().toByteArray() : BigInteger.valueOf(this.internal.intValue()).negate().toByteArray();
            if (arr.length < minSize) {
                byte[] bytes = new byte[minSize];
                System.arraycopy(arr, 0, bytes, arr.length < minSize ? minSize - arr.length : 0, arr.length);
                arr = bytes;
            }
            for (int i = 0; i < arr.length; ++i) {
                arr[i] = (byte)(~ arr[i] & 255);
            }
            byte tmp = 0;
            for (int i = arr.length - 1; i >= 0 && tmp == 0; --i) {
                arr[i] = tmp = (byte)(arr[i] + 1 & 255);
            }
            return arr;
        }

        public INTEGER add(int i) {
            BigInteger bigI = BigInteger.valueOf(i);
            if (this.isBig()) {
                this.set(this.internalBig.add(bigI));
            } else if (this.internal + i < 0) {
                this.set(this.getInternalBig().add(bigI));
            } else {
                INTEGER iNTEGER = this;
                iNTEGER.internal = iNTEGER.internal + i;
            }
            return this;
        }

        public INTEGER sub(int i) {
            BigInteger bigI = BigInteger.valueOf(i);
            if (this.isBig()) {
                this.set(this.internalBig.subtract(bigI));
            } else if (this.internal - i > 0) {
                this.set(this.getInternalBig().subtract(bigI));
            } else {
                INTEGER iNTEGER = this;
                iNTEGER.internal = iNTEGER.internal - i;
            }
            return this;
        }

        public INTEGER add(INTEGER integer) {
            if (this.isBig() || integer.isBig()) {
                this.set(this.getInternalBig().add(integer.getInternalBig()));
            } else if (this.getInternal() + integer.getInternal() < 0) {
                this.set(this.getInternalBig().add(integer.getInternalBig()));
            } else {
                INTEGER iNTEGER = this;
                iNTEGER.internal = iNTEGER.internal + integer.getInternal();
            }
            return this;
        }

        public INTEGER sub(INTEGER integer) {
            if (this.isBig() || integer.isBig()) {
                this.set(this.getInternalBig().subtract(integer.getInternalBig()));
            } else if (this.internal - integer.internal > 0) {
                this.set(this.getInternalBig().subtract(integer.getInternalBig()));
            } else {
                INTEGER iNTEGER = this;
                iNTEGER.internal = iNTEGER.internal - integer.getInternal();
            }
            return this;
        }

        public INTEGER abs() {
            if (this.isBig()) {
                this.internalBig = this.internalBig.abs();
            } else {
                this.internal = Math.abs(this.internal);
            }
            return this;
        }

        public INTEGER duplicate() {
            return new INTEGER(this);
        }
    }

    public static class NULL {
    }

    public static class BITSTRING {
        int unused;
        byte[] values;

        public BITSTRING(byte[] value) {
            this.values = new byte[value.length - 1];
            this.unused = value[0];
            for (int i = 1; i < value.length; ++i) {
                this.values[i - 1] = value[i];
            }
        }

        public static BITSTRING valueOf(byte[] value) {
            return new BITSTRING(value);
        }

        public byte[] toByteArray() {
            return this.values;
        }

        public int getUnused() {
            return this.unused;
        }
    }

    public static class BOOLEAN {
        boolean v = false;

        public BOOLEAN(byte value) {
            this.v = value != 0;
        }

        public static BOOLEAN valueOf(byte value) {
            return new BOOLEAN(value);
        }
    }

    public static class OCTETSTRING {
        byte[] string;

        public OCTETSTRING(byte[] value) {
            this.string = (byte[])value.clone();
        }

        public static OCTETSTRING valueOf(byte[] value) {
            return new OCTETSTRING(value);
        }
    }

    public static class OBJECT_IDENTIFIER {
        private final byte[] rsaPKCS1 = new byte[]{42, -122, 72, -122, -9, 13, 1, 1, 1};
        String string;

        public OBJECT_IDENTIFIER(byte[] value) {
            if (!Arrays.equals(this.rsaPKCS1, value)) {
                throw new RuntimeException("Not implemented");
            }
            this.string = "1.2.840.113549.1.1.1";
        }

        public static OBJECT_IDENTIFIER valueOf(byte[] value) {
            return new OBJECT_IDENTIFIER(value);
        }
    }

    public static class DER {
        // public static final byte BOOLEAN = 1;
        // public static final byte INTEGER = 2;
        // public static final byte BIT_STRING = 3;
        // public static final byte OCTET_STRING = 4;
        // public static final byte NULL = 5;
        // public static final byte OBJECT_IDENTIFIER = 6;
        // public static final byte UTF8String = 12;
        // public static final byte PrintableString = 19;
        // public static final byte IA5String = 22;
        // public static final byte BMPString = 30;
        // public static final byte SEQUENCE = 48;
        // public static final byte SET = 49;

        public static DERTLV readDER(InputStream is) throws EOFException {
            return new DERTLV(is);
        }

        public static class MalformedDERException
        extends RuntimeException {
            private static final long serialVersionUID = -8452231334791152338L;

            public MalformedDERException(String message, Throwable throwable) {
                super("Malformed DER: " + message, throwable);
            }

            public MalformedDERException(String message) {
                super("Malformed DER: " + message);
            }
        }

        public static class DERTLV {
            private final InputStream iis;
            private final byte tag;
            private final INTEGER len;
            private byte[] value;
            private Object retrieved;

            public DERTLV(byte[] bytes) throws EOFException {
                this(new ByteArrayInputStream(bytes));
            }

            public DERTLV(InputStream data) throws EOFException {
                block13 : {
                    this.len = new INTEGER();
                    this.retrieved = null;
                    this.iis = data;
                    try {
                        if (data.available() > 0) {
                            byte[] readTag = new byte[1];
                            if (data.read(readTag) != 1) {
                                throw new MalformedDERException("Tag read failed");
                            }
                            this.tag = readTag[0];
                            if (data.read(readTag) != 1) {
                                throw new MalformedDERException("Length read failed");
                            }
                            if ((readTag[0] & 128) == 128) {
                                int extraLenBytes = readTag[0] ^ -128;
                                byte[] readExtraLen = new byte[extraLenBytes];
                                if (data.read(readExtraLen) != extraLenBytes) {
                                    throw new MalformedDERException("Extra length read failed");
                                }
                                this.len.setUnsigned(readExtraLen);
                            } else {
                                this.len.set(readTag[0]);
                            }
                            break block13;
                        }
                        throw new EOFException("No more bytes");
                    }
                    catch (EOFException e) {
                        throw e;
                    }
                    catch (IOException e) {
                        throw new MalformedDERException("IOException occurred", e);
                    }
                }
                try {
                    if (this.len.intExactValue() > 0) {
                        this.value = new byte[this.len.intExactValue()];
                        int read = this.iis.read(this.value);
                        if (read != this.len.intExactValue()) {
                            throw new MalformedDERException("Value read failed " + read);
                        }
                    }
                }
                catch (IOException e) {
                    throw new MalformedDERException("IOException occurred", e);
                }
            }

            public byte getTag() {
                return this.tag;
            }

            public INTEGER getLen() {
                return this.len;
            }

            public byte[] getValue() {
                return this.value;
            }

            public Object retrieve() {
                if (this.retrieved == null) {
                    switch (this.tag) {
                        case 1: {
                            this.retrieved = BOOLEAN.valueOf(this.value[0]);
                            break;
                        }
                        case 2: {
                            this.retrieved = INTEGER.valueOf(this.value);
                            break;
                        }
                        case 3: {
                            this.retrieved = BITSTRING.valueOf(this.value);
                            break;
                        }
                        case 4: {
                            this.retrieved = OCTETSTRING.valueOf(this.value);
                            break;
                        }
                        case 5: {
                            this.retrieved = new NULL();
                            break;
                        }
                        case 6: {
                            this.retrieved = OBJECT_IDENTIFIER.valueOf(this.value);
                            break;
                        }
                        case 12: {
                            this.retrieved = new String(this.value, StandardCharsets.UTF_8);
                            break;
                        }
                        case 19: {
                            this.retrieved = new String(this.value, StandardCharsets.US_ASCII);
                            break;
                        }
                        case 22: {
                            this.retrieved = new String(this.value, StandardCharsets.US_ASCII);
                            break;
                        }
                        case 30: {
                            System.out.println("Warning: decoding ISO 10646-1 as UTF_16; NO LE/BE detection performed");
                            this.retrieved = new String(this.value, StandardCharsets.UTF_16);
                            break;
                        }
                        case 48: {
                            ByteArrayInputStream valueAsStream = new ByteArrayInputStream(this.value);
                            ArrayList<DERTLV> sequence = new ArrayList<DERTLV>();
                            try {
                                DERTLV local;
                                while ((local = new DERTLV(valueAsStream)) != null) {
                                    sequence.add(local);
                                }
                            }
                            catch (EOFException eOFException) {
                                // empty catch block
                            }
                            this.retrieved = sequence;
                            break;
                        }
                        case 49: {
                            ByteArrayInputStream valueAsStream = new ByteArrayInputStream(this.value);
                            TreeSet<DERTLV> set = new TreeSet<DERTLV>();
                            try {
                                DERTLV local;
                                while ((local = new DERTLV(valueAsStream)) != null) {
                                    set.add(local);
                                }
                            }
                            catch (EOFException eOFException) {
                                // empty catch block
                            }
                            this.retrieved = set;
                            break;
                        }
                        default: {
                            throw new MalformedDERException("Invalid tag type");
                        }
                    }
                }
                return this.retrieved;
            }
        }

    }

    public static enum DER_TYPE {
        PRIVATE_KEY,
        PUBLIC_KEY;
        
        private static final String privateHeader = "-----BEGIN RSA PRIVATE KEY-----";
        private static final String publicHeader = "-----BEGIN PUBLIC KEY-----";
        private static final String privateFooter = "-----END RSA PRIVATE KEY-----";
        private static final String publicFooter = "-----END PUBLIC KEY-----";

        private DER_TYPE() {
        }

        public static DER_TYPE typeOf(String s) {
            switch (s) {
                case "-----BEGIN RSA PRIVATE KEY-----": 
                case "-----END RSA PRIVATE KEY-----": {
                    return PRIVATE_KEY;
                }
                case "-----BEGIN PUBLIC KEY-----": 
                case "-----END PUBLIC KEY-----": {
                    return PUBLIC_KEY;
                }
            }
            return null;
        }

        public String getHeader() {
            if (this == PRIVATE_KEY) {
                return "-----BEGIN RSA PRIVATE KEY-----";
            }
            return "-----BEGIN PUBLIC KEY-----";
        }

        public String getFooter() {
            if (this == PRIVATE_KEY) {
                return "-----END RSA PRIVATE KEY-----";
            }
            return "-----END PUBLIC KEY-----";
        }
    }

}

