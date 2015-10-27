package me.shreyasr.ancients.util.chat;

import java.util.Iterator;
import java.util.TreeSet;

public class ChatManager {

    TreeSet<ChatMessage> messages = new TreeSet<ChatMessage>();
    ChatListener listener;

    public ChatManager() {

    }

    public void setListener(ChatListener listener) {
        this.listener = listener;
    }

    public synchronized void addMessage(ChatMessage message) {
        messages.add(message);
        if (listener != null) listener.newMessage(message);
    }

    public synchronized ChatMessage[] getLastMessages(int num) {
        num = Math.min(num, messages.size());
        Iterator<ChatMessage> chatMessageIterator = messages.descendingIterator();
        ChatMessage[] lastMessages = new ChatMessage[num];
        for (int i = 0; i < num; i++) {
            lastMessages[i] = chatMessageIterator.next();
        }
        return lastMessages;
    }

    public interface ChatListener {

        void newMessage(ChatMessage message);
    }
}
