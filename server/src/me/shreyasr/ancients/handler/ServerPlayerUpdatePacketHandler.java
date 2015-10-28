package me.shreyasr.ancients.handler;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Time;
import com.esotericsoftware.minlog.Log;

import me.shreyasr.ancients.ServerMain;
import me.shreyasr.ancients.components.LastUpdateTimeComponent;
import me.shreyasr.ancients.components.NameComponent;
import me.shreyasr.ancients.components.StatsComponent;
import me.shreyasr.ancients.components.UUIDComponent;
import me.shreyasr.ancients.packet.PacketHandler;
import me.shreyasr.ancients.packet.server.ServerPlayerUpdatePacket;
import me.shreyasr.ancients.util.chat.ChatMessage;

public class ServerPlayerUpdatePacketHandler extends PacketHandler<ServerPlayerUpdatePacket> {

    private final ServerMain server;

    public ServerPlayerUpdatePacketHandler(ServerMain server) {
        this.server = server;
    }

    @Override
    public void handle(ServerPlayerUpdatePacket packet, Connection conn) {
        UUIDComponent uuid = getUUIDComponent(packet.components);
        ImmutableArray<Entity> otherPlayers = getOtherPlayers(server.engine);

        for (Entity otherPlayer : otherPlayers) {
            if (UUIDComponent.MAPPER.get(otherPlayer).equals(uuid)) {
                LastUpdateTimeComponent thisPacketLastUpdate = getLastUpdateFromComponents(packet.components);
                LastUpdateTimeComponent existingPlayerLastUpdate = LastUpdateTimeComponent.MAPPER.get(otherPlayer);

                if (thisPacketLastUpdate.lastUpdateTime > existingPlayerLastUpdate.lastUpdateTime) {
                    updatePlayer(otherPlayer, packet.components);
                }
                return;
            }
        }

        Entity newPlayer = createAndAddPlayer(server.engine, packet.components);
        Log.info("Added new player: " + uuid);
        server.chatManager.addMessage(new ChatMessage(
                NameComponent.MAPPER.get(newPlayer).str + " joined!",
                Time.getServerMillis(), null, null));
        server.entityListener.entityAdded(newPlayer);
    }

    private Entity createAndAddPlayer(PooledEngine engine, Component[] components) {
        Entity e = engine.createEntity();
        updatePlayer(e, components);
        e.add(StatsComponent.create(engine));

        engine.addEntity(e);
        return e;
    }

    private void updatePlayer(Entity player, Component[] components) {
        for (Component c : components) {
            player.add(c);
        }
    }

    private ImmutableArray<Entity> getOtherPlayers(Engine engine) {
        return engine.getEntitiesFor(Family.all(UUIDComponent.class).get());
    }

    private UUIDComponent getUUIDComponent(Component[] components) {
        for (Component c : components) {
            if (c instanceof UUIDComponent) {
                return (UUIDComponent) c;
            }
        }
        return null;
    }

    private LastUpdateTimeComponent getLastUpdateFromComponents(Component[] components) {
        for (Component c : components) {
            if (c instanceof LastUpdateTimeComponent) {
                return ((LastUpdateTimeComponent) c);
            }
        }
        return LastUpdateTimeComponent.create(-1);
    }
}
