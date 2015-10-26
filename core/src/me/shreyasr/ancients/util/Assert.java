package me.shreyasr.ancients.util;

public class Assert {

    public static void assertEq(Object a, Object b) {
        if (a == null && b == null) return;
        if (a != null && a.equals(b)) return;
        if (b != null && b.equals(a)) return;
        throw new RuntimeException("Assertion failed.");
    }
}
