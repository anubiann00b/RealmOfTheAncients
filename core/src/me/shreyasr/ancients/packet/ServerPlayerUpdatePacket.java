package me.shreyasr.ancients.packet;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.esotericsoftware.kryonet.Connection;
import me.shreyasr.ancients.NetworkUtils;
import me.shreyasr.ancients.PacketHandleSystem;
import me.shreyasr.ancients.components.PeerComponent;
import me.shreyasr.ancients.components.PlayerComponent;
import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.components.SquareDirectionComponent;

/**
 * A packet containing a client's player data, consumed by the server.
 */
public class ServerPlayerUpdatePacket implements Packet {

    public static ServerPlayerUpdatePacket create(Component[] components) {
        ServerPlayerUpdatePacket packet = new ServerPlayerUpdatePacket();
        packet.components = components;
        return packet;
    }

    private Component[] components;

    @Override
    public void handle(PooledEngine engine, Connection conn, PacketHandleSystem packetHandleSystem) {
        String peer = NetworkUtils.getNameFromConnection(conn);
        ImmutableArray<Entity> otherPlayers = getOtherPlayers(engine);

        boolean done = false;
        for (Entity otherPlayer : otherPlayers) {
            if (PeerComponent.MAPPER.get(otherPlayer).name.equals(peer)) {
                updatePlayer(otherPlayer);
                done = true;
                break;
            }
        }

        if (!done) {
            Entity newPlayer = createAndAddPlayer(engine);
            newPlayer.add(PeerComponent.create(peer));
            System.out.println("Added new player");
            packetHandleSystem.entityAdded();
        }
    }

    private Entity createAndAddPlayer(PooledEngine engine) {
        Entity e = engine.createEntity();
        for (Component c : components) {
            e.add(c);
        }
        e.remove(PlayerComponent.class);
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

    private ImmutableArray<Entity> getOtherPlayers(Engine engine) {
        return engine.getEntitiesFor(Family.all(PeerComponent.class).get());
    }
}
