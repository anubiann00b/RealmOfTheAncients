package me.shreyasr.ancients;

import com.badlogic.ashley.core.PooledEngine;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import me.shreyasr.ancients.packet.Packet;

public class ClientPacketListener extends Listener {

    private PooledEngine engine;
    private PacketHandleSystem packetHandleSystem;

    public ClientPacketListener(PooledEngine engine, PacketHandleSystem packetHandleSystem) {
        this.engine = engine;
        this.packetHandleSystem = packetHandleSystem;
    }

    @Override
    public void received(Connection conn, Object obj) {
        if (obj instanceof Packet) {
            ((Packet)obj).handle(engine, conn, packetHandleSystem);
        }
    }
}
