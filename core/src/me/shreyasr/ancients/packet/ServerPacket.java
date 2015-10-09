package me.shreyasr.ancients.packet;

import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.esotericsoftware.kryonet.Connection;

public interface ServerPacket {

    void handle(PooledEngine engine, Connection conn, EntityListener entityListener);
}
