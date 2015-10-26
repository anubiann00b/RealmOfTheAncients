package me.shreyasr.ancients.packet.client.handler;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.esotericsoftware.kryonet.Connection;

import me.shreyasr.ancients.components.StartTimeComponent;
import me.shreyasr.ancients.components.UUIDComponent;
import me.shreyasr.ancients.components.type.TypeComponent;
import me.shreyasr.ancients.components.weapon.OwnerUUIDComponent;
import me.shreyasr.ancients.packet.PacketHandler;
import me.shreyasr.ancients.packet.client.ClientAttackPacket;
import me.shreyasr.ancients.screen.GameScreen;

public class ClientAttackPacketHandler extends PacketHandler<ClientAttackPacket> {

    private GameScreen game;

    public ClientAttackPacketHandler(GameScreen game) {
        this.game = game;
    }

    @Override
    public void handle(ClientAttackPacket packet, Connection conn) {
        for (Entity weapon : game.engine.getEntitiesFor(Family.all(TypeComponent.Weapon.class).get())) {
            if (UUIDComponent.MAPPER.get(weapon).equals(getUUIDFromComponents(packet.components))) {
                weapon.add(getStartTimeFromComponents(packet.components));
                return;
            }
        }
        Entity newWeapon = game.engine.createEntity();
        for (Component c : packet.components) {
            newWeapon.add(c);
        }
        OwnerUUIDComponent.MAPPER.get(newWeapon).updateEngineId(game.engine);
        game.engine.addEntity(newWeapon);
    }

    private UUIDComponent getUUIDFromComponents(Component[] components) {
        for (Component c : components) {
            if (c instanceof UUIDComponent) {
                return ((UUIDComponent) c);
            }
        }
        return null;
    }

    private StartTimeComponent getStartTimeFromComponents(Component[] components) {
        for (Component c : components) {
            if (c instanceof StartTimeComponent) {
                return ((StartTimeComponent) c);
            }
        }
        return StartTimeComponent.create(-1);
    }
}
