package me.shreyasr.ancients.packet;

import com.esotericsoftware.kryonet.Connection;

public abstract class PacketHandler<T extends Packet> {

    public abstract void handle(T packet, Connection conn);
}
