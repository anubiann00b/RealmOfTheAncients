package me.shreyasr.ancients.packet.client.handler;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.esotericsoftware.kryonet.Connection;

import me.shreyasr.ancients.components.UUIDComponent;
import me.shreyasr.ancients.components.type.TypeComponent;
import me.shreyasr.ancients.packet.PacketHandler;
import me.shreyasr.ancients.packet.client.ClientHitPacket;
import me.shreyasr.ancients.screen.GameScreen;

public class ClientHitPacketHandler extends PacketHandler<ClientHitPacket> {

    private GameScreen game;

    public ClientHitPacketHandler(GameScreen game) {
        this.game = game;
    }

    @Override
    public void handle(ClientHitPacket packet, Connection conn) {
        UUIDComponent recvUUID = getUUIDFromComponents(packet.components);

        ImmutableArray<Entity> otherPlayers = getOtherPlayers(game.engine);

        for (Entity otherPlayer : otherPlayers) {
            if (UUIDComponent.MAPPER.get(otherPlayer).equals(recvUUID)) {
                for (Component c : packet.components) {
                    otherPlayer.add(c);
                }
                return;
            }
        }
    }

    private ImmutableArray<Entity> getOtherPlayers(PooledEngine engine) {
        return engine.getEntitiesFor(Family.all(TypeComponent.Player.class).get());
    }

    private UUIDComponent getUUIDFromComponents(Component[] components) {
        for (Component c : components) {
            if (c instanceof UUIDComponent) {
                return ((UUIDComponent) c);
            }
        }
        return null;
    }
}
