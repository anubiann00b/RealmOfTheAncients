package me.shreyasr.ancients.packet;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.esotericsoftware.kryonet.Connection;
import me.shreyasr.ancients.components.MyPlayerComponent;
import me.shreyasr.ancients.components.UUIDComponent;

public class ClientPlayerRemovePacket implements ClientPacket {

    public static ClientPlayerRemovePacket create(UUIDComponent uuid) {
        ClientPlayerRemovePacket packet = new ClientPlayerRemovePacket();
        packet.uuid = uuid;
        return packet;
    }

    private UUIDComponent uuid;

    @Override
    public void handle(PooledEngine engine, Connection conn,
                       UUIDComponent playerUUID, EntityListener entityListener) {
        for (Entity e : getOtherPlayers(engine)) {
            if (UUIDComponent.MAPPER.get(e).equals(uuid)) {
                System.out.println("Removing player");
                engine.removeEntity(e);
                entityListener.entityRemoved(e);
                break;
            }
        }
    }

    private ImmutableArray<Entity> getOtherPlayers(PooledEngine engine) {
        return engine.getEntitiesFor(
                Family
                        .all(UUIDComponent.class)
                        .exclude(MyPlayerComponent.class)
                        .get());
    }
}
