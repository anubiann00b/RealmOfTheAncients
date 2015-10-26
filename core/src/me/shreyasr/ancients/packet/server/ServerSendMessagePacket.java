package me.shreyasr.ancients.packet.server;

import com.esotericsoftware.kryonet.Time;

import me.shreyasr.ancients.packet.Packet;
import me.shreyasr.ancients.packet.PacketHandler;
import me.shreyasr.ancients.util.chat.ChatMessage;

public class ServerSendMessagePacket extends Packet<PacketHandler<ServerSendMessagePacket>> {

    public ServerSendMessagePacket create(String message) {
        ServerSendMessagePacket packet = new ServerSendMessagePacket();
        packet.message = new ChatMessage(message, Time.getServerMillis());
        return packet;
    }

    private ChatMessage message;

    private static PacketHandler<ServerSendMessagePacket> handler;

    public static void setHandler(PacketHandler<ServerSendMessagePacket> handler) {
        ServerSendMessagePacket.handler = handler;
    }

    @Override
    public PacketHandler<ServerSendMessagePacket> getHandler() {
        return handler;
    }
}
