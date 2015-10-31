package me.shreyasr.ancients.packet.server;

import me.shreyasr.ancients.packet.Packet;
import me.shreyasr.ancients.packet.PacketHandler;

public class ServerNameRegistrationPacket extends Packet<PacketHandler<ServerNameRegistrationPacket>> {

    public static ServerNameRegistrationPacket create(String name) {
        ServerNameRegistrationPacket packet = new ServerNameRegistrationPacket();
        packet.name = name;
        return packet;
    }

    public String name;

    private static PacketHandler<ServerNameRegistrationPacket> handler;

    public static void setHandler(PacketHandler<ServerNameRegistrationPacket> handler) {
        ServerNameRegistrationPacket.handler = handler;
    }

    @Override
    public PacketHandler<ServerNameRegistrationPacket> getHandler() {
        return handler;
    }
}