package me.shreyasr.ancients.packet.client;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.esotericsoftware.kryonet.Connection;

import java.util.ArrayList;
import java.util.List;

import me.shreyasr.ancients.components.LastUpdateTimeComponent;
import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.components.TextureComponent;
import me.shreyasr.ancients.components.TextureTransformComponent;
import me.shreyasr.ancients.components.UUIDComponent;
import me.shreyasr.ancients.components.type.TypeComponent;
import me.shreyasr.ancients.components.weapon.OwnerUUIDComponent;
import me.shreyasr.ancients.components.weapon.WeaponAnimationComponent;

public class ClientAttackPacket implements ClientPacket {

    public static ClientAttackPacket create(Component[] components) {
        ClientAttackPacket packet = new ClientAttackPacket();
        List<Component> finalComponents = new ArrayList<Component>();
        for (Component c : components) {
            if (c instanceof  LastUpdateTimeComponent
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
    public void handle(PooledEngine engine, Connection conn, UUIDComponent playerUUID, EntityListener entityListener) {
        for (Entity weapon : engine.getEntitiesFor(Family.all(TypeComponent.Weapon.class).get())) {
            if (UUIDComponent.MAPPER.get(weapon).equals(getUUIDFromComponents())) {
                weapon.add(getLastUpdateFromComponents());
                return;
            }
        }
        Entity newWeapon = engine.createEntity();
        for (Component c : components) {
            newWeapon.add(c);
        }
        engine.addEntity(newWeapon);
    }

    private UUIDComponent getUUIDFromComponents() {
        for (Component c : components) {
            if (c instanceof UUIDComponent) {
                return ((UUIDComponent) c);
            }
        }
        return null;
    }

    private LastUpdateTimeComponent getLastUpdateFromComponents() {
        for (Component c : components) {
            if (c instanceof LastUpdateTimeComponent) {
                return ((LastUpdateTimeComponent) c);
            }
        }
        return LastUpdateTimeComponent.create(-1);
    }
}
