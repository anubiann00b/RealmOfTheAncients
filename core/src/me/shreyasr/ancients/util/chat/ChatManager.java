package me.shreyasr.ancients.util.chat;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;

public class ChatManager {

    private TreeSet<ChatMessage> messages = new TreeSet<ChatMessage>();
    private Queue<ChatListener> listeners = new LinkedList<ChatListener>();


    public ChatManager() {

    }

    public void addListener(ChatListener listener) {
        listeners.add(listener);
    }

    public synchronized void addMessage(ChatMessage message) {
        messages.add(message);
        notifyListeners(message);
    }

    private void notifyListeners(ChatMessage message) {
        for (ChatListener l : listeners) {
            l.newMessage(message);
        }
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
