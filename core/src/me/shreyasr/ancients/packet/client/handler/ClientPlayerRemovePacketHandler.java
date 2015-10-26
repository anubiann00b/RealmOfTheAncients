package me.shreyasr.ancients.packet.client.handler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.esotericsoftware.kryonet.Connection;

import me.shreyasr.ancients.components.UUIDComponent;
import me.shreyasr.ancients.components.player.MyPlayerComponent;
import me.shreyasr.ancients.packet.PacketHandler;
import me.shreyasr.ancients.packet.client.ClientPlayerRemovePacket;
import me.shreyasr.ancients.screen.GameScreen;

public class ClientPlayerRemovePacketHandler extends PacketHandler<ClientPlayerRemovePacket> {

    private GameScreen game;

    public ClientPlayerRemovePacketHandler(GameScreen game) {
        this.game = game;
    }

    @Override
    public void handle(ClientPlayerRemovePacket packet, Connection conn) {
        for (Entity e : getOtherPlayers(game.engine)) {
            if (UUIDComponent.MAPPER.get(e).equals(packet.uuid)) {
                System.out.println("Removing player");
                game.engine.removeEntity(e);
                game.entityListener.entityRemoved(e);
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
