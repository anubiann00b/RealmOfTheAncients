package me.shreyasr.ancients.util;

public class MathHelper {
    
    /** My favorite method. */
    public static double clamp(double lo, double val, double hi) {
        return (lo<=val)?((val<=hi)?val:((lo<hi)?hi:lo)):((lo<=hi)?lo:((val<hi)?hi:val));
    }

    public static float clamp(float lo, float val, float hi) {
        return (float)clamp((double)lo, (double)val, (double)hi);
    }

    public static long clamp(long lo, long val, long hi) {
        return (int) clamp((double)lo, (double)val, (double)hi);
    }

    public static int clamp(int lo, int val, int hi) {
        return (int) clamp((long)lo, (long)val, (long)hi);
    }
}