package me.shreyasr.ancients.packet.client;

import me.shreyasr.ancients.packet.Packet;
import me.shreyasr.ancients.packet.PacketHandler;
import me.shreyasr.ancients.util.chat.ChatMessage;

public class ClientChatMessagePacket extends Packet<PacketHandler<ClientChatMessagePacket>> {

    public static ClientChatMessagePacket create(ChatMessage[] message) {
        ClientChatMessagePacket packet = new ClientChatMessagePacket();
        packet.messages = message;
        return packet;
    }

    public ChatMessage[] messages;

    private static PacketHandler<ClientChatMessagePacket> handler;

    public static void setHandler(PacketHandler<ClientChatMessagePacket> handler) {
        ClientChatMessagePacket.handler = handler;
    }

    @Override
    public PacketHandler<ClientChatMessagePacket> getHandler() {
        return handler;
    }
}