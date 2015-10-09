package me.shreyasr.ancients.packet;

import com.badlogic.ashley.core.PooledEngine;
import com.esotericsoftware.kryonet.Connection;
import me.shreyasr.ancients.PacketHandleSystem;

public interface Packet {

    void handle(PooledEngine engine, Connection conn, PacketHandleSystem packetHandleSystem);
}
