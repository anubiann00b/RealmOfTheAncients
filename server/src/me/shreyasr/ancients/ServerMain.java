package me.shreyasr.ancients;

import com.badlogic.ashley.core.PooledEngine;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.Time;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;

import me.shreyasr.ancients.system.ClientUpdateSystem;
import me.shreyasr.ancients.system.CollisionDetectionSystem;
import me.shreyasr.ancients.system.RemoveOldEntitySystem;
import me.shreyasr.ancients.systems.network.PacketHandleSystem;
import me.shreyasr.ancients.systems.update.KnockbackSystem;
import me.shreyasr.ancients.systems.update.WeaponUpdateSystem;
import me.shreyasr.ancients.util.KryoRegistrar;
import me.shreyasr.ancients.util.LinkedListQueuedListener;

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

        int priority = 0;
        engine.addSystem(       new PacketHandleSystem(++priority, queuedListener));
        engine.addSystem( new CollisionDetectionSystem(++priority, server, engine));
        engine.addSystem(       new WeaponUpdateSystem(++priority, engine));
        engine.addSystem(          new KnockbackSystem(++priority));
        engine.addSystem(       new ClientUpdateSystem(++priority, 16, server));
        engine.addSystem(    new RemoveOldEntitySystem(++priority, engine, server));

        queuedListener.setListener(
                new ServerListener(engine, engine.getSystem(PacketHandleSystem.class), server));

        long lastTime = Time.getMillis();
        //noinspection InfiniteLoopStatement
        while (true) {
            long currentTime = Time.getMillis();
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
