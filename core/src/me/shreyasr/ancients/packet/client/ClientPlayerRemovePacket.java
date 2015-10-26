package me.shreyasr.ancients.packet.client;

import com.badlogic.ashley.core.Component;

import me.shreyasr.ancients.components.UUIDComponent;
import me.shreyasr.ancients.packet.Packet;
import me.shreyasr.ancients.packet.PacketHandler;

public class ClientPlayerRemovePacket extends Packet<PacketHandler<ClientPlayerRemovePacket>> {

    public static ClientPlayerRemovePacket create(UUIDComponent uuid) {
        ClientPlayerRemovePacket packet = new ClientPlayerRemovePacket();
        packet.uuid = uuid;
        return packet;
    }

    public UUIDComponent uuid;
    public Component[] components;

    private static PacketHandler<ClientPlayerRemovePacket> handler;

    public static void setHandler(PacketHandler<ClientPlayerRemovePacket> handler) {
        ClientPlayerRemovePacket.handler = handler;
    }

    @Override
    public PacketHandler<ClientPlayerRemovePacket> getHandler() {
        return handler;
    }
}
