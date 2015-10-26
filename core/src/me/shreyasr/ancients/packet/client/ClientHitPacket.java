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

import me.shreyasr.ancients.components.HitboxComponent;
import me.shreyasr.ancients.components.KnockbackComponent;
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

public class ClientHitPacket implements ClientPacket {

    public static ClientHitPacket create(Component[] components, KnockbackComponent knockback) {
        ClientHitPacket packet = new ClientHitPacket();
        List<Component> finalComponents = new ArrayList<Component>();
        for (Component c : components) {
            if (c instanceof HitboxComponent
                    || c instanceof PositionComponent
                    || c instanceof SpeedComponent
                    || c instanceof StatsComponent
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
        finalComponents.add(knockback);
        packet.components = finalComponents.toArray(new Component[finalComponents.size()]);
        return packet;
    }

    private Component[] components;

    @Override
    public void handle(PooledEngine engine, Connection conn,
                       UUIDComponent playerUUID, EntityListener entityListener) {
        UUIDComponent recvUUID = getUUIDFromComponents();

        ImmutableArray<Entity> otherPlayers = getOtherPlayers(engine);

        for (Entity otherPlayer : otherPlayers) {
            if (UUIDComponent.MAPPER.get(otherPlayer).equals(recvUUID)) {
                for (Component c : components) {
                    otherPlayer.add(c);
                }
                return;
            }
        }
    }

    private ImmutableArray<Entity> getOtherPlayers(PooledEngine engine) {
        return engine.getEntitiesFor(Family.all(TypeComponent.Player.class).get());
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
