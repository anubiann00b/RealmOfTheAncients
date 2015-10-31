package me.shreyasr.ancients.packet.server;

import com.badlogic.ashley.core.Component;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Time;

import java.util.ArrayList;
import java.util.List;

import me.shreyasr.ancients.components.HitboxComponent;
import me.shreyasr.ancients.components.LastUpdateTimeComponent;
import me.shreyasr.ancients.components.NameComponent;
import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.components.SpeedComponent;
import me.shreyasr.ancients.components.SquareAnimationComponent;
import me.shreyasr.ancients.components.SquareDirectionComponent;
import me.shreyasr.ancients.components.TextureComponent;
import me.shreyasr.ancients.components.TextureTransformComponent;
import me.shreyasr.ancients.components.UUIDComponent;
import me.shreyasr.ancients.components.VelocityComponent;
import me.shreyasr.ancients.components.player.AttackComponent;
import me.shreyasr.ancients.components.type.TypeComponent;
import me.shreyasr.ancients.packet.Packet;
import me.shreyasr.ancients.packet.PacketHandler;

/**
 * A packet containing a client's player data, consumed by the server.
 */
public class ServerPlayerUpdatePacket extends Packet<PacketHandler<ServerPlayerUpdatePacket>> {

    /**
     * Called in the client to create a packet to be sent to the server.
     */
    public static ServerPlayerUpdatePacket create(Component[] components, Connection conn) {
        ServerPlayerUpdatePacket packet = new ServerPlayerUpdatePacket();
        List<Component> finalComponents = new ArrayList<Component>();
        for (Component c : components) {
            if (c instanceof AttackComponent
                    || c instanceof HitboxComponent
                    || c instanceof NameComponent
                    || c instanceof PositionComponent
                    || c instanceof SpeedComponent
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
        finalComponents.add(LastUpdateTimeComponent.create(Time.getServerMillis(conn)));
        packet.components = finalComponents.toArray(new Component[finalComponents.size()]);
        return packet;
    }

    public Component[] components;

    private static PacketHandler<ServerPlayerUpdatePacket> handler;

    public static void setHandler(PacketHandler<ServerPlayerUpdatePacket> handler) {
        ServerPlayerUpdatePacket.handler = handler;
    }

    @Override
    public PacketHandler<ServerPlayerUpdatePacket> getHandler() {
        return handler;
    }
}
