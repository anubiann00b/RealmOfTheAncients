package me.shreyasr.ancients.packet.server;

import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

/**
 * Packet consumed by the server.
 */
public interface ServerPacket {

    void handle(PooledEngine engine, Connection conn, EntityListener entityListener, Server server);
}
