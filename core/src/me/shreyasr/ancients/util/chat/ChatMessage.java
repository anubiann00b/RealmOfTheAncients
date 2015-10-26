package me.shreyasr.ancients.util.chat;

public class ChatMessage implements Comparable<ChatMessage> {

    public final String message;
    public final long time;

    public ChatMessage(String message, long time) {
        this.message = message.substring(0, Math.min(message.length(), 100)).trim();
        this.time = time;
    }

    @Override
    public int compareTo(ChatMessage o) {
        return Long.compare(this.time, o.time);
    }
}
