package me.shreyasr.ancients.handler;

import com.esotericsoftware.kryonet.Connection;

import me.shreyasr.ancients.ServerMain;
import me.shreyasr.ancients.packet.PacketHandler;
import me.shreyasr.ancients.packet.server.ServerChatMessagePacket;

public class ServerChatMessagePacketHandler extends PacketHandler<ServerChatMessagePacket> {

    private final ServerMain server;

    public ServerChatMessagePacketHandler(ServerMain server) {
        this.server = server;
    }

    @Override
    public void handle(ServerChatMessagePacket packet, Connection conn) {
        server.chatManager.addMessage(packet.message);
    }
}
