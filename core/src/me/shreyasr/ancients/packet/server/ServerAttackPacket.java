package me.shreyasr.ancients.packet.server;

import com.badlogic.ashley.core.Component;

import java.util.ArrayList;
import java.util.List;

import me.shreyasr.ancients.components.HitboxComponent;
import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.components.TextureComponent;
import me.shreyasr.ancients.components.TextureTransformComponent;
import me.shreyasr.ancients.components.UUIDComponent;
import me.shreyasr.ancients.components.type.TypeComponent;
import me.shreyasr.ancients.components.weapon.OwnerUUIDComponent;
import me.shreyasr.ancients.components.weapon.WeaponAnimationComponent;
import me.shreyasr.ancients.packet.Packet;
import me.shreyasr.ancients.packet.PacketHandler;

public class ServerAttackPacket extends Packet<PacketHandler<ServerAttackPacket>> {

    public static final int ATTACK_DELAY_MS = 100;
    public static final int DASH_DELAY_MS = 100;
    public static final int KNOCKBACK_DELAY_MS = 0;

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

    public Component[] components;

    private static PacketHandler<ServerAttackPacket> handler;

    public static void setHandler(PacketHandler<ServerAttackPacket> handler) {
        ServerAttackPacket.handler = handler;
    }

    @Override
    public PacketHandler<ServerAttackPacket> getHandler() {
        return handler;
    }
}
