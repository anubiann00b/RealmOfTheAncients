package me.shreyasr.ancients;

import com.badlogic.ashley.core.PooledEngine;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import me.shreyasr.ancients.packet.ServerPacket;
import me.shreyasr.ancients.systems.PacketHandleSystem;

public class ServerListener extends Listener {

    private PooledEngine engine;
    private PacketHandleSystem packetHandleSystem;

    public ServerListener(PooledEngine engine, PacketHandleSystem packetHandleSystem) {
        this.engine = engine;
        this.packetHandleSystem = packetHandleSystem;
    }

    @Override
    public void received(Connection conn, Object obj) {
        if (obj instanceof ServerPacket) {
            ((ServerPacket)obj).handle(engine, conn, packetHandleSystem);
        }
    }
}
