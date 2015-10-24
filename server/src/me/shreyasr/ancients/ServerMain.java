package me.shreyasr.ancients;

import com.badlogic.ashley.core.PooledEngine;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;

import me.shreyasr.ancients.systems.network.PacketHandleSystem;

public class ServerMain {

    public static void main(String[] args) {
        Log.INFO();
        try {
            initServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initServer() throws IOException {
        final Server server = new Server();
        KryoRegistrar.register(server.getKryo());
        server.start();
        server.bind(54555, 54777);

        final PooledEngine engine = new PooledEngine();

//        LinkedListQueuedListener queuedListener = new LagLinkedListQueuedListener(100, 0);
        LinkedListQueuedListener queuedListener = new LinkedListQueuedListener();

        server.addListener(queuedListener);

        engine.addSystem(    new PacketHandleSystem(1, queuedListener));
        engine.addSystem(    new ClientUpdateSystem(2, 16, server));
        engine.addSystem( new RemoveOldEntitySystem(3, engine, server));

        queuedListener.setListener(
                new ServerListener(engine, engine.getSystem(PacketHandleSystem.class), server));

        long lastTime = System.currentTimeMillis();
        //noinspection InfiniteLoopStatement
        while (true) {
            long currentTime = System.currentTimeMillis();
            engine.update(currentTime - lastTime);
            lastTime = currentTime;
            if (server.getConnections().length == 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
