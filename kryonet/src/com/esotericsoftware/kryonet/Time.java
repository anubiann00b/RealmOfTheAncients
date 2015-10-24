package com.esotericsoftware.kryonet;

public class Time {

    public static long getMillis() {
        return System.currentTimeMillis();
    }

    public static long getServerMillis(Connection conn) {
        return getMillis() + conn.getTimeOffset();
    }
}
