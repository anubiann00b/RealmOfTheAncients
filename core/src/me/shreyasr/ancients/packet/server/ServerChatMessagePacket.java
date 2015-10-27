package me.shreyasr.ancients.packet.server;

import com.esotericsoftware.kryonet.Time;

import me.shreyasr.ancients.packet.Packet;
import me.shreyasr.ancients.packet.PacketHandler;
import me.shreyasr.ancients.util.CustomUUID;
import me.shreyasr.ancients.util.chat.ChatMessage;

public class ServerChatMessagePacket extends Packet<PacketHandler<ServerChatMessagePacket>> {

    public static ServerChatMessagePacket create(String message, CustomUUID owner) {
        ServerChatMessagePacket packet = new ServerChatMessagePacket();
        packet.message = new ChatMessage(message, Time.getServerMillis(), owner);
        return packet;
    }

    public ChatMessage message;

    private static PacketHandler<ServerChatMessagePacket> handler;

    public static void setHandler(PacketHandler<ServerChatMessagePacket> handler) {
        ServerChatMessagePacket.handler = handler;
    }

    @Override
    public PacketHandler<ServerChatMessagePacket> getHandler() {
        return handler;
    }
}
