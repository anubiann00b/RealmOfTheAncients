package me.shreyasr.ancients.util.chat;

import com.badlogic.gdx.utils.Array;

public class NewChatMessageListener implements ChatManager.ChatListener {

    private Array<ChatMessage> newMessages = new Array<ChatMessage>(false, 16);

    @Override
    public void newMessage(ChatMessage message) {
        newMessages.add(message);
    }

    public boolean hasMessages() {
        return newMessages.size > 0;
    }

    public ChatMessage[] popNewMessages() {
        ChatMessage[] newMessagesArr = newMessages.toArray(ChatMessage.class);
        newMessages.clear();
        return newMessagesArr;
    }
}
