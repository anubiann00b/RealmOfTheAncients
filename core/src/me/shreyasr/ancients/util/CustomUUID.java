package me.shreyasr.ancients.util;

import java.security.SecureRandom;

public class CustomUUID {

    private static final SecureRandom numberGenerator = new SecureRandom();

    public static CustomUUID randomUUID() {
        byte[] randomBytes = new byte[16];
        numberGenerator.nextBytes(randomBytes);
        randomBytes[6]  &= 0x0f;  /* clear version        */
        randomBytes[6]  |= 0x40;  /* set to version 4     */
        randomBytes[8]  &= 0x3f;  /* clear variant        */
        randomBytes[8]  |= 0x80;  /* set to IETF variant  */
        return new CustomUUID(randomBytes);
    }

    private long mostSigBits;
    private long leastSigBits;

    private CustomUUID() { }

    private CustomUUID(byte[] data) {
        long msb = 0;
        long lsb = 0;
        for (int i=0; i<8; i++)
            msb = (msb << 8) | (data[i] & 0xff);
        for (int i=8; i<16; i++)
            lsb = (lsb << 8) | (data[i] & 0xff);
        this.mostSigBits = msb;
        this.leastSigBits = lsb;
    }

    @Override
    public int hashCode() {
        long hilo = mostSigBits ^ leastSigBits;
        return ((int)(hilo >> 32)) ^ (int) hilo;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CustomUUID
                && mostSigBits == ((CustomUUID)o).mostSigBits
                && leastSigBits == ((CustomUUID)o).leastSigBits;
    }

    public String toString() {
        return (digits(mostSigBits >> 32, 8) + "-" +
                digits(mostSigBits >> 16, 4) + "-" +
                digits(mostSigBits, 4) + "-" +
                digits(leastSigBits >> 48, 4) + "-" +
                digits(leastSigBits, 12));
    }

    public String toNameString() {
        return digits(mostSigBits >> 32, 8);
    }

    private static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }
}
