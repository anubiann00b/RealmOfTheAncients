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
    private UUIDComponent playerUUID;

    public ClientPacketListener(PooledEngine engine, CustomUUID playerUUID, EntityListener entityListener) {
        this.engine = engine;
        this.entityListener = entityListener;
        this.playerUUID = UUIDComponent.create(engine, playerUUID);
    }

//    private String last = "";
//    private int count = 0;

    @Override
    public void received(Connection conn, Object obj) {
//        String newName = obj.getClass().getSimpleName();
//        if (last.equals(newName)) count++;
//        else count = 0;
//        if (count % 20 == 0) System.out.println("Recv: " + newName + " " + count + "x");
//        last = newName;

        if (obj instanceof ClientPacket) {
            ((ClientPacket)obj).handle(engine, conn, playerUUID, entityListener);
        }
    }
}
