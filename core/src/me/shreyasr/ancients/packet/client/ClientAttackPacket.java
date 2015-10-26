package me.shreyasr.ancients.packet.client;

import com.badlogic.ashley.core.Component;

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
import me.shreyasr.ancients.packet.Packet;
import me.shreyasr.ancients.packet.PacketHandler;

public class ClientAttackPacket extends Packet<PacketHandler<ClientAttackPacket>> {

    public static ClientAttackPacket create(Component[] components) {
        ClientAttackPacket packet = new ClientAttackPacket();
        List<Component> finalComponents = new ArrayList<Component>();
        for (Component c : components) {
            if (c instanceof HitboxComponent
                    || c instanceof StartTimeComponent
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

    public Component[] components;

    private static PacketHandler<ClientAttackPacket> handler;

    public static void setHandler(PacketHandler<ClientAttackPacket> handler) {
        ClientAttackPacket.handler = handler;
    }

    @Override
    public PacketHandler<ClientAttackPacket> getHandler() {
        return handler;
    }
}
