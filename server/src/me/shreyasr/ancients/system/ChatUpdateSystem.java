package me.shreyasr.ancients.system;

import com.badlogic.ashley.core.EntitySystem;
import com.esotericsoftware.kryonet.Server;

import me.shreyasr.ancients.packet.client.ClientChatMessagePacket;
import me.shreyasr.ancients.util.chat.ChatManager;
import me.shreyasr.ancients.util.chat.NewChatMessageListener;

public class ChatUpdateSystem extends EntitySystem {

    private final ChatManager chatManager;
    private final Server server;
    private final NewChatMessageListener newChatMessageListener;

    public ChatUpdateSystem(int priority, ChatManager chatManager, Server server) {
        super(priority);
        this.chatManager = chatManager;
        this.server = server;
        newChatMessageListener = new NewChatMessageListener();
        chatManager.setListener(newChatMessageListener);
    }

    @Override
    public void update(float deltaTime) {
        if (newChatMessageListener.hasMessages()) {
            server.sendToAllTCP(ClientChatMessagePacket.create(newChatMessageListener.popNewMessages()));
        }
    }
}
