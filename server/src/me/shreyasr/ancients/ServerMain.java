package me.shreyasr.ancients;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public class ServerMain {

    public static void main(String[] args) {
        try {
            initServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initServer() throws IOException {
        Server server = new Server();
        server.start();
        server.bind(54555, 54777);

        server.addListener(new Listener() {
            public void received (Connection conn, Object obj) {

            }
        });
    }
}
