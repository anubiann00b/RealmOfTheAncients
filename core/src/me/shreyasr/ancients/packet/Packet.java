package me.shreyasr.ancients.packet;

import com.esotericsoftware.kryonet.Connection;

public abstract class Packet<T extends PacketHandler> {

    public void handle(Connection conn) {
        T handler = getHandler();
        if (handler == null) throw new NullPointerException("Handler not initialized: " + getClass());
        getHandler().handle(this, conn);
    }

    public abstract T getHandler();
}
