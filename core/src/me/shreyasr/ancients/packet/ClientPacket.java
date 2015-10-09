package me.shreyasr.ancients.packet;

import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.esotericsoftware.kryonet.Connection;
import me.shreyasr.ancients.components.UUIDComponent;

public interface ClientPacket {

    void handle(PooledEngine engine, Connection conn,
                UUIDComponent uuid, EntityListener entityListener);
}