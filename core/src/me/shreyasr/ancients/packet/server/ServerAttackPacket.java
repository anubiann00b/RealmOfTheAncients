package me.shreyasr.ancients.packet.server;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.Time;

import java.util.ArrayList;
import java.util.List;

import me.shreyasr.ancients.components.HitboxComponent;
import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.components.StartTimeComponent;
import me.shreyasr.ancients.components.TextureComponent;
import me.shreyasr.ancients.components.TextureTransformComponent;
import me.shreyasr.ancients.components.UUIDComponent;
import me.shreyasr.ancients.components.type.TypeComponent;
import me.shreyasr.ancients.components.weapon.OwnerUUIDComponent;
import me.shreyasr.ancients.components.weapon.WeaponAnimationComponent;
import me.shreyasr.ancients.packet.client.ClientAttackPacket;

public class ServerAttackPacket implements ServerPacket {

    public static final int ATTACK_DELAY_MS = 300;

    public static ServerAttackPacket create(Component[] components) {
        ServerAttackPacket packet = new ServerAttackPacket();
        List<Component> finalComponents = new ArrayList<Component>();
        for (Component c : components) {
            if (c instanceof HitboxComponent
                    || c instanceof PositionComponent
                    || c instanceof OwnerUUIDComponent
                    || c instanceof WeaponAnimationComponent
                    || c instanceof TypeComponent
                    || c instanceof TextureComponent
                    || c instanceof TextureTransformComponent
                    || c instanceof UUIDComponent) {
                finalComponents.add(c);
            }
        }
        packet.components = finalComponents.toArray(new Component[finalComponents.size()]);
        return packet;
    }

    private Component[] components;

    @Override
    public void handle(PooledEngine engine, Connection conn, EntityListener entityListener, Server server) {
        Entity newWeapon = engine.createEntity();
        for (Component c : components) {
            newWeapon.add(c);
        }
        newWeapon.add(StartTimeComponent.create(Time.getServerMillis(conn) + ATTACK_DELAY_MS));
        engine.addEntity(newWeapon);

        send(newWeapon, server);
    }

    private void send(Entity newWeapon, Server server) {
        Component[] newComponents = newWeapon.getComponents().toArray(Component.class);
        server.sendToAllUDP(ClientAttackPacket.create(newComponents));
    }
}
