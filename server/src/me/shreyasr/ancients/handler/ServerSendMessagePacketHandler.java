package me.shreyasr.ancients.handler;

import com.esotericsoftware.kryonet.Connection;

import me.shreyasr.ancients.ServerMain;
import me.shreyasr.ancients.packet.PacketHandler;
import me.shreyasr.ancients.packet.server.ServerSendMessagePacket;

public class ServerSendMessagePacketHandler extends PacketHandler<ServerSendMessagePacket> {

    private final ServerMain server;

    public ServerSendMessagePacketHandler(ServerMain server) {
        this.server = server;
    }

    @Override
    public void handle(ServerSendMessagePacket packet, Connection conn) {

    }
}
