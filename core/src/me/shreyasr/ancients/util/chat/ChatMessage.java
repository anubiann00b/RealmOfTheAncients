package me.shreyasr.ancients.util.chat;

import me.shreyasr.ancients.components.weapon.OwnerUUIDComponent;
import me.shreyasr.ancients.util.CustomUUID;

public class ChatMessage implements Comparable<ChatMessage> {

    public String body;
    public long time;
    private OwnerUUIDComponent owner;

    private ChatMessage() { }

    public ChatMessage(String body, long time, CustomUUID owner) {
        this.body = body.substring(0, Math.min(body.length(), 100)).trim();
        this.time = time;
        this.owner = owner != null ? OwnerUUIDComponent.create(owner) : null;
    }

    @Override
    public int compareTo(ChatMessage o) {
        return Long.compare(this.time, o.time);
    }
}
