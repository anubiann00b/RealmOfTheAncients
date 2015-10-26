package me.shreyasr.ancients.packet.server;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.Time;

import java.util.ArrayList;
import java.util.List;

import me.shreyasr.ancients.components.HitboxComponent;
import me.shreyasr.ancients.components.LastUpdateTimeComponent;
import me.shreyasr.ancients.components.NameComponent;
import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.components.SpeedComponent;
import me.shreyasr.ancients.components.SquareAnimationComponent;
import me.shreyasr.ancients.components.SquareDirectionComponent;
import me.shreyasr.ancients.components.StatsComponent;
import me.shreyasr.ancients.components.TextureComponent;
import me.shreyasr.ancients.components.TextureTransformComponent;
import me.shreyasr.ancients.components.UUIDComponent;
import me.shreyasr.ancients.components.VelocityComponent;
import me.shreyasr.ancients.components.type.TypeComponent;

/**
 * A packet containing a client's player data, consumed by the server.
 */
public class ServerPlayerUpdatePacket implements ServerPacket {

    /**
     * Called in the client to create a packet to be sent to the server.
     */
    public static ServerPlayerUpdatePacket create(Component[] components, Connection conn) {
        ServerPlayerUpdatePacket packet = new ServerPlayerUpdatePacket();
        List<Component> finalComponents = new ArrayList<Component>();
        for (Component c : components) {
            if (c instanceof HitboxComponent
                    || c instanceof NameComponent
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
        finalComponents.add(LastUpdateTimeComponent.create(Time.getServerMillis(conn)));
        packet.components = finalComponents.toArray(new Component[finalComponents.size()]);
        return packet;
    }

    private Component[] components;

    @Override
    public void handle(PooledEngine engine, Connection conn, EntityListener entityListener, Server server) {
        UUIDComponent uuid = getUUIDComponent();
        ImmutableArray<Entity> otherPlayers = getOtherPlayers(engine);

        for (Entity otherPlayer : otherPlayers) {
            if (UUIDComponent.MAPPER.get(otherPlayer).equals(uuid)) {
                LastUpdateTimeComponent thisPacketLastUpdate = getLastUpdateFromComponents();
                LastUpdateTimeComponent existingPlayerLastUpdate = LastUpdateTimeComponent.MAPPER.get(otherPlayer);

                if (thisPacketLastUpdate.lastUpdateTime > existingPlayerLastUpdate.lastUpdateTime) {
                    updatePlayer(otherPlayer);
                }
                return;
            }
        }

        Entity newPlayer = createAndAddPlayer(engine);
        System.out.println("Added new player: " + uuid);
        entityListener.entityAdded(newPlayer);
    }

    private Entity createAndAddPlayer(PooledEngine engine) {
        Entity e = engine.createEntity();
        updatePlayer(e);
        e.add(StatsComponent.create(engine));

        engine.addEntity(e);
        return e;
    }

    private void updatePlayer(Entity player) {
        for (Component c : components) {
            player.add(c);
        }
    }

    private ImmutableArray<Entity> getOtherPlayers(Engine engine) {
        return engine.getEntitiesFor(Family.all(UUIDComponent.class).get());
    }

    private UUIDComponent getUUIDComponent() {
        for (Component c : components) {
            if (c instanceof UUIDComponent) {
                return (UUIDComponent) c;
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
