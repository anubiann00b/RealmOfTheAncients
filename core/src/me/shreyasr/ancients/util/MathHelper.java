package me.shreyasr.ancients.util;

public class MathHelper {
    
    /** My favorite method. */
    public static double clamp(double lo, double val, double hi) {
        return (lo<=val)?((val<=hi)?val:((lo<hi)?hi:lo)):((lo<=hi)?lo:((val<hi)?hi:val));
    }
}