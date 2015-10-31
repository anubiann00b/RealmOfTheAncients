package me.shreyasr.ancients.handler;

import com.badlogic.ashley.core.Family;
import com.esotericsoftware.kryonet.Connection;

import me.shreyasr.ancients.ServerMain;
import me.shreyasr.ancients.components.NameComponent;
import me.shreyasr.ancients.packet.PacketHandler;
import me.shreyasr.ancients.packet.server.ServerChatMessagePacket;
import me.shreyasr.ancients.util.CommandHandler;
import me.shreyasr.ancients.util.chat.ChatMessage;

public class ServerChatMessagePacketHandler extends PacketHandler<ServerChatMessagePacket> {

    private final ServerMain server;
    private final Family nameFamily = Family.all(NameComponent.class).get();

    public ServerChatMessagePacketHandler(ServerMain server) {
        this.server = server;
    }

    @Override
    public void handle(ServerChatMessagePacket packet, Connection conn) {
        if (!isCommand(packet.message)) {
            server.chatManager.addMessage(packet.message);
        } else {
            CommandHandler.process(packet.message.body, server.engine);
        }
    }

    private boolean isCommand(ChatMessage message) {
        return message.body.startsWith("/");
    }
}
