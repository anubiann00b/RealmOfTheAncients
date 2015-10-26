package me.shreyasr.ancients.packet.client;

import com.badlogic.ashley.core.Component;

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
import me.shreyasr.ancients.packet.Packet;
import me.shreyasr.ancients.packet.PacketHandler;

public class ClientHitPacket extends Packet<PacketHandler<ClientHitPacket>> {

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

    public Component[] components;

    private static PacketHandler<ClientHitPacket> handler;

    public static void setHandler(PacketHandler<ClientHitPacket> handler) {
        ClientHitPacket.handler = handler;
    }

    @Override
    public PacketHandler<ClientHitPacket> getHandler() {
        return handler;
    }
}
