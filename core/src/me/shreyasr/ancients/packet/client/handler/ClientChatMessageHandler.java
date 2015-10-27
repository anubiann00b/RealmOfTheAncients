package me.shreyasr.ancients.packet.client.handler;

import com.esotericsoftware.kryonet.Connection;

import me.shreyasr.ancients.packet.PacketHandler;
import me.shreyasr.ancients.packet.client.ClientChatMessagePacket;
import me.shreyasr.ancients.screen.GameScreen;
import me.shreyasr.ancients.util.chat.ChatMessage;

public class ClientChatMessageHandler extends PacketHandler<ClientChatMessagePacket> {

    private GameScreen game;

    public ClientChatMessageHandler(GameScreen game) {
        this.game = game;
    }

    @Override
    public void handle(ClientChatMessagePacket packet, Connection conn) {
        for (ChatMessage message : packet.messages) {
            game.chatManager.addMessage(message);
        }
    }
}
