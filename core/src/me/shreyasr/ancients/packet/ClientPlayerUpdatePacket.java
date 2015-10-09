package me.shreyasr.ancients.packet;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.esotericsoftware.kryonet.Connection;
import me.shreyasr.ancients.PacketHandleSystem;
import me.shreyasr.ancients.components.PeerComponent;
import me.shreyasr.ancients.components.PlayerComponent;
import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.components.SquareDirectionComponent;

public class ClientPlayerUpdatePacket implements Packet {

    public static ClientPlayerUpdatePacket create(Component[] components) {
        ClientPlayerUpdatePacket packet = new ClientPlayerUpdatePacket();
        packet.components = components;
        return packet;
    }

    private Component[] components;

    @Override
    public void handle(PooledEngine engine, Connection conn, PacketHandleSystem packetHandleSystem) {
        PeerComponent peer = getPeerFromComponents();
        ImmutableArray<Entity> otherPlayers = getOtherPlayers(engine);

        boolean done = false;
        for (Entity otherPlayer : otherPlayers) {
            if (PeerComponent.MAPPER.get(otherPlayer).equals(peer)) {
                updatePlayer(otherPlayer);
//                System.out.println("Edited player");
                done = true;
                break;
            }
        }

        if (!done) {
            createAndAddPlayer(engine);
            System.out.println("Added new player");
            packetHandleSystem.entityAdded();
        }
    }

    private Entity createAndAddPlayer(PooledEngine engine) {
        Entity e = engine.createEntity();
        for (Component c : components) {
            e.add(c);
        }
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
                        .all(PeerComponent.class)
                        .exclude(PlayerComponent.class)
                        .get());
    }

    private PeerComponent getPeerFromComponents() {
        for (Component c : components) {
            if (c instanceof PeerComponent) {
                return ((PeerComponent) c);
            }
        }
        return null;
    }
}
