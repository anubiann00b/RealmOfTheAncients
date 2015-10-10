package me.shreyasr.ancients.packet;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.esotericsoftware.kryonet.Connection;
import me.shreyasr.ancients.components.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A packet containing a client's player data, consumed by the server.
 */
public class ServerPlayerUpdatePacket implements ServerPacket {

    public static ServerPlayerUpdatePacket create(Component[] components) {
        ServerPlayerUpdatePacket packet = new ServerPlayerUpdatePacket();
        List<Component> finalComponents = new ArrayList<Component>();
        for (Component c : components) {
            if (!(c instanceof LastUpdateTimeComponent)) {
                finalComponents.add(c);
            }
        }

        packet.components = finalComponents.toArray(new Component[finalComponents.size()]);
        return packet;
    }

    private Component[] components;

    @Override
    public void handle(PooledEngine engine, Connection conn, EntityListener entityListener) {
        UUIDComponent uuid = getUUIDComponent();
        ImmutableArray<Entity> otherPlayers = getOtherPlayers(engine);

        boolean done = false;
        for (Entity otherPlayer : otherPlayers) {
            if (UUIDComponent.MAPPER.get(otherPlayer).equals(uuid)) {
                updatePlayer(otherPlayer);
                done = true;
                break;
            }
        }

        if (!done) {
            Entity newPlayer = createAndAddPlayer(engine);
            System.out.println("Added new player: " + uuid);
            entityListener.entityAdded(newPlayer);
        }
    }

    private Entity createAndAddPlayer(PooledEngine engine) {
        Entity e = engine.createEntity();
        for (Component c : components) {
            e.add(c);
        }
        e.remove(MyPlayerComponent.class);
        e.add(LastUpdateTimeComponent.create(System.currentTimeMillis()));
        engine.addEntity(e);
        return e;
    }

    private void updatePlayer(Entity player) {
        for (Component c : components) {
            if (c instanceof PositionComponent
                    || c instanceof SquareDirectionComponent) {
                player.add(c);
            }
        }
        player.add(LastUpdateTimeComponent.create(System.currentTimeMillis()));
    }

    private UUIDComponent getUUIDComponent() {
        for (Component c : components) {
            if (c instanceof UUIDComponent) {
                return (UUIDComponent) c;
            }
        }
        return null;
    }

    private ImmutableArray<Entity> getOtherPlayers(Engine engine) {
        return engine.getEntitiesFor(Family.all(UUIDComponent.class).get());
    }
}
