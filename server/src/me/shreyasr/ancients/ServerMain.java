package me.shreyasr.ancients;

import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.Time;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;

import me.shreyasr.ancients.handler.ServerAttackPacketHandler;
import me.shreyasr.ancients.handler.ServerPlayerUpdatePacketHandler;
import me.shreyasr.ancients.handler.ServerSendMessagePacketHandler;
import me.shreyasr.ancients.packet.server.ServerAttackPacket;
import me.shreyasr.ancients.packet.server.ServerPlayerUpdatePacket;
import me.shreyasr.ancients.packet.server.ServerSendMessagePacket;
import me.shreyasr.ancients.system.ClientUpdateSystem;
import me.shreyasr.ancients.system.CollisionDetectionSystem;
import me.shreyasr.ancients.system.RemoveOldEntitySystem;
import me.shreyasr.ancients.system.ServerMessageHandler;
import me.shreyasr.ancients.systems.network.PacketHandleSystem;
import me.shreyasr.ancients.systems.update.KnockbackSystem;
import me.shreyasr.ancients.systems.update.WeaponUpdateSystem;
import me.shreyasr.ancients.util.KryoRegistrar;
import me.shreyasr.ancients.util.LinkedListQueuedListener;
import me.shreyasr.ancients.util.PacketListener;

public class ServerMain {

    public static void main(String[] args) {
        Log.INFO();
        ServerMain server = new ServerMain();
        try {
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final Server server;
    public final PooledEngine engine;
    public final ServerMessageHandler messageHandler;
    public EntityListener entityListener;

    private final LinkedListQueuedListener queuedListener;
    private int priority = 0;

    public ServerMain() {
        server = new Server();
        engine = new PooledEngine();
        messageHandler = new ServerMessageHandler();

//        queuedListener = new LagLinkedListQueuedListener(100, 0);
        queuedListener = new LinkedListQueuedListener(new PacketListener());
    }

    public void run() throws IOException {
        KryoRegistrar.register(server.getKryo());
        server.start();
        server.bind(54555, 54777);

        server.addListener(queuedListener);

        // @formatter:off
        engine.addSystem(       new PacketHandleSystem(++priority, queuedListener));
        engine.addSystem( new CollisionDetectionSystem(++priority, server, engine));
        engine.addSystem(       new WeaponUpdateSystem(++priority, engine));
        engine.addSystem(          new KnockbackSystem(++priority));
        engine.addSystem(       new ClientUpdateSystem(++priority, 16, server));
        engine.addSystem(new RemoveOldEntitySystem(++priority, engine, server));
        // @formatter:on

        entityListener = engine.getSystem(PacketHandleSystem.class);

        // @formatter:off
              ServerAttackPacket.setHandler(new ServerAttackPacketHandler(this));
        ServerPlayerUpdatePacket.setHandler(new ServerPlayerUpdatePacketHandler(this));
         ServerSendMessagePacket.setHandler(new ServerSendMessagePacketHandler(this));
        // @formatter:on

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
