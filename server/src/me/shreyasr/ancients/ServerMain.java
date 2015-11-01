package me.shreyasr.ancients;

import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.Time;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;

import me.shreyasr.ancients.handler.ServerAttackPacketHandler;
import me.shreyasr.ancients.handler.ServerChatMessagePacketHandler;
import me.shreyasr.ancients.handler.ServerNameRegistrationPacketHandler;
import me.shreyasr.ancients.handler.ServerPlayerUpdatePacketHandler;
import me.shreyasr.ancients.packet.server.ServerAttackPacket;
import me.shreyasr.ancients.packet.server.ServerChatMessagePacket;
import me.shreyasr.ancients.packet.server.ServerNameRegistrationPacket;
import me.shreyasr.ancients.packet.server.ServerPlayerUpdatePacket;
import me.shreyasr.ancients.system.ChatUpdateSystem;
import me.shreyasr.ancients.system.ClientUpdateSystem;
import me.shreyasr.ancients.system.CollisionDetectionSystem;
import me.shreyasr.ancients.system.RemoveOldEntitySystem;
import me.shreyasr.ancients.systems.network.PacketHandleSystem;
import me.shreyasr.ancients.systems.update.KnockbackSystem;
import me.shreyasr.ancients.systems.update.WeaponUpdateSystem;
import me.shreyasr.ancients.util.KryoRegistrar;
import me.shreyasr.ancients.util.LinkedListQueuedListener;
import me.shreyasr.ancients.util.PacketListener;
import me.shreyasr.ancients.util.chat.ChatManager;
import me.shreyasr.ancients.util.chat.ChatMessage;

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
    public EntityListener entityListener;

    public final ChatManager chatManager;

    private final LinkedListQueuedListener queuedListener;

    public ServerMain() {
        server = new Server();
        engine = new PooledEngine();

//        queuedListener = new LagLinkedListQueuedListener(100, 0);
        queuedListener = new LinkedListQueuedListener(new PacketListener());
        chatManager = new ChatManager();

        chatManager.addListener(new ChatManager.ChatListener() {
            @Override
            public void newMessage(ChatMessage message) {
                Log.info(message.getLogString());
            }
        });
    }

    public void run() throws IOException {
        KryoRegistrar.register(server.getKryo());
        server.start();
        server.bind(54555, 54777);

        server.addListener(queuedListener);

        int priority = 0;
        // @formatter:off
        engine.addSystem(       new PacketHandleSystem(++priority, queuedListener));
        engine.addSystem(       new WeaponUpdateSystem(++priority, engine));
        engine.addSystem( new CollisionDetectionSystem(++priority, server, engine));
//        engine.addSystem(               new DashSystem(++priority, engine, en));
        engine.addSystem(          new KnockbackSystem(++priority));
        engine.addSystem(       new ClientUpdateSystem(++priority, 16, server));
        engine.addSystem(    new RemoveOldEntitySystem(++priority, engine, server));
        engine.addSystem(         new ChatUpdateSystem(++priority, chatManager, server));
        // @formatter:on

        entityListener = engine.getSystem(PacketHandleSystem.class);

        // @formatter:off
                  ServerAttackPacket.setHandler(new ServerAttackPacketHandler(this));
            ServerPlayerUpdatePacket.setHandler(new ServerPlayerUpdatePacketHandler(this));
             ServerChatMessagePacket.setHandler(new ServerChatMessagePacketHandler(this));
        ServerNameRegistrationPacket.setHandler(new ServerNameRegistrationPacketHandler(this));
        // @formatter:on

        priority = 0;
        LinkedListQueuedListener.QueueElementWrapper.putClassPriority(ServerNameRegistrationPacket.class, ++priority);
        LinkedListQueuedListener.QueueElementWrapper.putClassPriority(ServerAttackPacket.class, ++priority);
        LinkedListQueuedListener.QueueElementWrapper.putClassPriority(ServerPlayerUpdatePacket.class, ++priority);
        LinkedListQueuedListener.QueueElementWrapper.putClassPriority(ServerChatMessagePacket.class, ++priority);

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
