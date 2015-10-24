package me.shreyasr.ancients;

import com.badlogic.ashley.core.PooledEngine;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import me.shreyasr.ancients.packet.server.ServerPacket;
import me.shreyasr.ancients.systems.network.PacketHandleSystem;

public class ServerListener extends Listener {

    private PooledEngine engine;
    private PacketHandleSystem packetHandleSystem;
    private Server server;

    public ServerListener(PooledEngine engine, PacketHandleSystem packetHandleSystem, Server server) {
        this.engine = engine;
        this.packetHandleSystem = packetHandleSystem;
        this.server = server;
    }

    @Override
    public void received(Connection conn, Object obj) {
        if (obj instanceof ServerPacket) {
            ((ServerPacket)obj).handle(engine, conn, packetHandleSystem, server);
        }
    }
}
