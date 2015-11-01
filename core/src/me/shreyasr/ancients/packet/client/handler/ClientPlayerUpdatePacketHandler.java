package me.shreyasr.ancients.packet.client.handler;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.esotericsoftware.kryonet.Connection;

import me.shreyasr.ancients.components.KnockbackComponent;
import me.shreyasr.ancients.components.LastUpdateTimeComponent;
import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.components.StatsComponent;
import me.shreyasr.ancients.components.UUIDComponent;
import me.shreyasr.ancients.components.player.MyPlayerComponent;
import me.shreyasr.ancients.components.player.dash.DashComponent;
import me.shreyasr.ancients.components.type.TypeComponent;
import me.shreyasr.ancients.packet.PacketHandler;
import me.shreyasr.ancients.packet.client.ClientPlayerUpdatePacket;
import me.shreyasr.ancients.screen.GameScreen;

public class ClientPlayerUpdatePacketHandler extends PacketHandler<ClientPlayerUpdatePacket> {

    private GameScreen game;

    public ClientPlayerUpdatePacketHandler(GameScreen game) {
        this.game = game;
    }

    @Override
    public void handle(ClientPlayerUpdatePacket packet, Connection conn) {
        UUIDComponent recvUUID = getUUIDFromComponents(packet.components);

        if (game.playerUUID.equals(recvUUID.val)) {
            updateMyPlayer(game.engine, packet.components);
            return;
        }

        ImmutableArray<Entity> otherPlayers = getOtherPlayers(game.engine);

        for (Entity otherPlayer : otherPlayers) {
            if (UUIDComponent.MAPPER.get(otherPlayer).equals(recvUUID)) {
                LastUpdateTimeComponent thisPacketLastUpdate = getLastUpdateFromComponents(packet.components);
                LastUpdateTimeComponent existingPlayerLastUpdate = LastUpdateTimeComponent.MAPPER.get(otherPlayer);

                if (thisPacketLastUpdate.lastUpdateTime > existingPlayerLastUpdate.lastUpdateTime) {
                    updatePlayer(otherPlayer, packet.components);
                }
                return;
            }
        }

        Entity e = createAndAddPlayer(game.engine, packet.components);
        System.out.println("Added new player: " + recvUUID.toString());
        game.entityListener.entityAdded(e);
    }

    private void updateMyPlayer(Engine engine, Component[] components) {
        Entity player = engine.getEntitiesFor(Family.all(MyPlayerComponent.class).get()).first();
        for (Component c : components) {
            if (c instanceof StatsComponent) {
                player.add(c);
            }
        }
    }

    private Entity createAndAddPlayer(PooledEngine engine, Component[] components) {
        Entity e = engine.createEntity();
        updatePlayer(e, components);
        engine.addEntity(e);
        return e;
    }

    private void updatePlayer(Entity player, Component[] components) {
        boolean hasDashOrKnockback = false;
        for (Component c : components) {
            if (c instanceof DashComponent) {
                if (((DashComponent) c).isScheduledOrStunned()) {
                    hasDashOrKnockback = true;
                    break;
                }
            }
            if (c instanceof KnockbackComponent) {
                if (((KnockbackComponent) c).isScheduled()) {
                    hasDashOrKnockback = true;
                    break;
                }
            }
        }
        for (Component c : components) {
            if ((c instanceof PositionComponent) && hasDashOrKnockback) {
                continue; // don't add position if we have active dashes or knockbacks
            } else {
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

    private LastUpdateTimeComponent getLastUpdateFromComponents(Component[] components) {
        for (Component c : components) {
            if (c instanceof LastUpdateTimeComponent) {
                return ((LastUpdateTimeComponent) c);
            }
        }
        return LastUpdateTimeComponent.create(-1);
    }
}
