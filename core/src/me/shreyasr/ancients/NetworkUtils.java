package me.shreyasr.ancients;

import com.badlogic.ashley.core.Component;
import com.esotericsoftware.kryonet.Connection;

import java.net.InetSocketAddress;

public class NetworkUtils {

    public static String getNameFromConnection(Connection conn) {
        InetSocketAddress sock = conn.getRemoteAddressTCP();
        return sock.getAddress() + ":" + sock.getPort();
    }

    public static Component[] getComponentArrayFromObject(Object obj) {
        if (obj instanceof Object[]) {
            Object[] arr = ((Object[]) obj);
            if (arr.length > 0 && arr[0] instanceof Component) {
                Component[] components = new Component[arr.length];
                for (int i = 0; i < arr.length; i++) {
                    components[i] = (Component) arr[i];
                }
                return components;
            }
        }
        return null;
    }
}
