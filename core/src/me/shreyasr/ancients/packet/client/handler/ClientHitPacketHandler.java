package me.shreyasr.ancients.packet.client.handler;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.esotericsoftware.kryonet.Connection;

import me.shreyasr.ancients.components.KnockbackComponent;
import me.shreyasr.ancients.components.UUIDComponent;
import me.shreyasr.ancients.components.player.MyPlayerComponent;
import me.shreyasr.ancients.components.player.dash.DashComponent;
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

        if (game.playerUUID.equals(recvUUID.val)) {
            updateMyPlayer(game.engine, packet.components);
            return;
        }

        ImmutableArray<Entity> otherPlayers = getOtherPlayers(game.engine);

        for (Entity otherPlayer : otherPlayers) {
            if (UUIDComponent.MAPPER.get(otherPlayer).equals(recvUUID)) {
                DashComponent.MAPPER.get(otherPlayer).cancel();
                for (Component c : packet.components) {
                    otherPlayer.add(c);
                }
                return;
            }
        }
    }

    private void updateMyPlayer(PooledEngine engine, Component[] components) {
        Entity player = engine.getEntitiesFor(Family.all(MyPlayerComponent.class).get()).first();
        DashComponent.MAPPER.get(player).cancel();
        for (Component c : components) {
            if (c instanceof KnockbackComponent) {
                player.add(c);
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
