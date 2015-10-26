package me.shreyasr.ancients.util.chat;

import com.badlogic.ashley.core.EntitySystem;

import java.util.TreeSet;

public abstract class MessageHandler extends EntitySystem {

    TreeSet<ChatMessage> messages = new TreeSet<ChatMessage>();

    public void addMessage(ChatMessage message) {
        messages.add(message);
    }
}
