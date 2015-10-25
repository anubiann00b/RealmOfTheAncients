package com.esotericsoftware.kryonet;

public class Time {


    public static Connection conn;

    public static void setConn(Connection conn) {
        Time.conn = conn;
    }

    public static long getServerMillis() {
        return getMillis() + (conn != null ? conn.getTimeOffset() : 0);
    }

    public static long getServerMillis(Connection conn) {
        return getMillis() + (conn != null ? conn.getTimeOffset() : 0);
    }

    public static long getMillis() {
        return System.currentTimeMillis();
    }
}
