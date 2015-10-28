package me.shreyasr.ancients.util.chat;

import com.badlogic.ashley.core.Engine;

import me.shreyasr.ancients.components.NameComponent;
import me.shreyasr.ancients.components.weapon.OwnerUUIDComponent;
import me.shreyasr.ancients.util.CustomUUID;

public class ChatMessage implements Comparable<ChatMessage> {

    public String body;
    public long time;
    private OwnerUUIDComponent owner;
    private String name;

    private ChatMessage() { }

    public ChatMessage(String body, long time, CustomUUID owner) {
        this.body = body.substring(0, Math.min(body.length(), 200)).trim();
        this.time = time;
        this.owner = owner != null ? OwnerUUIDComponent.create(owner) : null;
    }

    public void updateName(Engine engine) {
        if (owner == null) {
            name = null;
            return;
        }
        owner.updateEngineId(engine);
        name = NameComponent.MAPPER.get(owner.getOwner(engine)).str;
    }

    @Override
    public int compareTo(ChatMessage o) {
        return Long.compare(this.time, o.time);
    }

    public String getChatString() {
        if (name != null) {
            return "[" + name + "]: " + body;
        } else {
            return body;
        }
    }
}
