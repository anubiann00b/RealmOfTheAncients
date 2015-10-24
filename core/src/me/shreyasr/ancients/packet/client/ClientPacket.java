package me.shreyasr.ancients.packet.client;

import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.esotericsoftware.kryonet.Connection;
import me.shreyasr.ancients.components.UUIDComponent;

/**
 * Packet consumed by the client.
 */
public interface ClientPacket {

    void handle(PooledEngine engine, Connection conn,
                UUIDComponent playerUUID, EntityListener entityListener);
}