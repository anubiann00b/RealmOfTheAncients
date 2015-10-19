package me.shreyasr.ancients;

import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import me.shreyasr.ancients.components.UUIDComponent;
import me.shreyasr.ancients.packet.client.ClientPacket;

public class ClientPacketListener extends Listener {

    private PooledEngine engine;
    private EntityListener entityListener;
    private UUIDComponent uuid;

    public ClientPacketListener(PooledEngine engine, CustomUUID uuid, EntityListener entityListener) {
        this.engine = engine;
        this.entityListener = entityListener;
        this.uuid = UUIDComponent.create(uuid);
    }

    @Override
    public void received(Connection conn, Object obj) {
//        System.out.println("Recv: " + obj.getClass().getSimpleName());
        if (obj instanceof ClientPacket) {
            ((ClientPacket)obj).handle(engine, conn, uuid, entityListener);
        }
    }
}
