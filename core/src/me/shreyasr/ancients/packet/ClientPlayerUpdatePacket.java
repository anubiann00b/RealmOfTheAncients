package me.shreyasr.ancients.packet;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.esotericsoftware.kryonet.Connection;
import me.shreyasr.ancients.components.*;

public class ClientPlayerUpdatePacket implements ClientPacket {

    public static ClientPlayerUpdatePacket create(Component[] components) {
        ClientPlayerUpdatePacket packet = new ClientPlayerUpdatePacket();
        packet.components = components;
        return packet;
    }

    private Component[] components;

    @Override
    public void handle(PooledEngine engine, Connection conn,
                       UUIDComponent myUUID, EntityListener entityListener) {
        UUIDComponent recvUUID = getUUIDFromComponents();

        if (myUUID.equals(recvUUID)) {
            return;
        }

        ImmutableArray<Entity> otherPlayers = getOtherPlayers(engine);

        boolean done = false;
        for (Entity otherPlayer : otherPlayers) {
            if (UUIDComponent.MAPPER.get(otherPlayer).equals(recvUUID)) {
                updatePlayer(otherPlayer);
                done = true;
                break;
            }
        }

        if (!done) {
            Entity e = createAndAddPlayer(engine);
            System.out.println("Added new player");
            entityListener.entityAdded(e);
        }
    }

    private Entity createAndAddPlayer(PooledEngine engine) {
        Entity e = engine.createEntity();
        for (Component c : components) {
            e.add(c);
        }
        e.remove(LastUpdateTimeComponent.class);
        engine.addEntity(e);
        return e;
    }

    private void updatePlayer(Entity otherPlayer) {
        for (Component c : components) {
            if (c instanceof PositionComponent
                    || c instanceof SquareDirectionComponent) {
                    otherPlayer.add(c);
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

    private UUIDComponent getUUIDFromComponents() {
        for (Component c : components) {
            if (c instanceof UUIDComponent) {
                return ((UUIDComponent) c);
            }
        }
        return null;
    }
}
