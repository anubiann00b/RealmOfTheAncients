package me.shreyasr.ancients.packet.client;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.esotericsoftware.kryonet.Connection;

import java.util.ArrayList;
import java.util.List;

import me.shreyasr.ancients.components.LastUpdateTimeComponent;
import me.shreyasr.ancients.components.player.MyPlayerComponent;
import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.components.SpeedComponent;
import me.shreyasr.ancients.components.SquareAnimationComponent;
import me.shreyasr.ancients.components.SquareDirectionComponent;
import me.shreyasr.ancients.components.TextureComponent;
import me.shreyasr.ancients.components.TextureTransformComponent;
import me.shreyasr.ancients.components.UUIDComponent;
import me.shreyasr.ancients.components.VelocityComponent;
import me.shreyasr.ancients.components.type.TypeComponent;

public class ClientPlayerUpdatePacket implements ClientPacket {

    public static ClientPlayerUpdatePacket create(Component[] components) {
        ClientPlayerUpdatePacket packet = new ClientPlayerUpdatePacket();
        List<Component> finalComponents = new ArrayList<Component>();
        for (Component c : components) {
            if (c instanceof LastUpdateTimeComponent
                    || c instanceof PositionComponent
                    || c instanceof SpeedComponent
                    || c instanceof SquareAnimationComponent
                    || c instanceof SquareDirectionComponent
                    || c instanceof TextureComponent
                    || c instanceof TextureTransformComponent
                    || c instanceof TypeComponent
                    || c instanceof UUIDComponent
                    || c instanceof VelocityComponent) {
                finalComponents.add(c);
            }
        }
        packet.components = finalComponents.toArray(new Component[finalComponents.size()]);
        return packet;
    }

    private Component[] components;

    @Override
    public void handle(PooledEngine engine, Connection conn,
                       UUIDComponent playerUUID, EntityListener entityListener) {
        UUIDComponent recvUUID = getUUIDFromComponents();

        if (playerUUID.equals(recvUUID)) {
            return;
        }

        ImmutableArray<Entity> otherPlayers = getOtherPlayers(engine);

        for (Entity otherPlayer : otherPlayers) {
            if (UUIDComponent.MAPPER.get(otherPlayer).equals(recvUUID)) {
                LastUpdateTimeComponent thisPacketLastUpdate = getLastUpdateFromComponents();
                LastUpdateTimeComponent existingPlayerLastUpdate = LastUpdateTimeComponent.MAPPER.get(otherPlayer);

                if (thisPacketLastUpdate.lastUpdateTime > existingPlayerLastUpdate.lastUpdateTime) {
                    updatePlayer(otherPlayer);
                }
                return;
            }
        }

        Entity e = createAndAddPlayer(engine);
        System.out.println("Added new player");
        entityListener.entityAdded(e);
    }

    private Entity createAndAddPlayer(PooledEngine engine) {
        Entity e = engine.createEntity();
        updatePlayer(e);
        engine.addEntity(e);
        return e;
    }

    private void updatePlayer(Entity otherPlayer) {
        for (Component c : components) {
            otherPlayer.add(c);
        }
    }

    private ImmutableArray<Entity> getOtherPlayers(PooledEngine engine) {
        return engine.getEntitiesFor(
                Family
                        .all(UUIDComponent.class)
                        .exclude(MyPlayerComponent.class)
                        .get());
    }

    private UUIDComponent getUUIDFromComponents() {
        for (Component c : components) {
            if (c instanceof UUIDComponent) {
                return ((UUIDComponent) c);
            }
        }
        return null;
    }

    private LastUpdateTimeComponent getLastUpdateFromComponents() {
        for (Component c : components) {
            if (c instanceof LastUpdateTimeComponent) {
                return ((LastUpdateTimeComponent) c);
            }
        }
        return LastUpdateTimeComponent.create(-1);
    }
}
