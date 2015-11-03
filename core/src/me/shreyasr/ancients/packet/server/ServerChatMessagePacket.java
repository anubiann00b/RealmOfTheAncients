package me.shreyasr.ancients.packet.server;

import com.badlogic.ashley.core.Entity;
import com.esotericsoftware.kryonet.Time;

import me.shreyasr.ancients.components.NameComponent;
import me.shreyasr.ancients.components.UUIDComponent;
import me.shreyasr.ancients.packet.Packet;
import me.shreyasr.ancients.packet.PacketHandler;
import me.shreyasr.ancients.util.CustomUUID;
import me.shreyasr.ancients.util.chat.ChatMessage;

public class ServerChatMessagePacket extends Packet<PacketHandler<ServerChatMessagePacket>> {

    public static ServerChatMessagePacket create(String message, Entity player) {
        return create(message, UUIDComponent.MAPPER.get(player).val, NameComponent.MAPPER.get(player).str);
    }

    public static ServerChatMessagePacket create(String message, CustomUUID owner, String playerName) {
        ServerChatMessagePacket packet = new ServerChatMessagePacket();
        packet.message = new ChatMessage(message, Time.getServerMillis(), owner, playerName);
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
