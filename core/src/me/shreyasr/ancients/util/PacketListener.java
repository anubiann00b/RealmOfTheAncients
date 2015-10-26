package me.shreyasr.ancients.util;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import me.shreyasr.ancients.packet.Packet;

public class PacketListener extends Listener {

//    private String last = "";
//    private int count = 0;

    @Override
    public void received(Connection conn, Object obj) {
//        String newName = obj.getClass().getSimpleName();
//        if (last.equals(newName)) count++;
//        else count = 0;
//        if (count % 20 == 0) System.out.println("Recv: " + newName + " " + count + "x");
//        last = newName;

        if (obj instanceof Packet) {
            ((Packet)obj).handle(conn);
        }
    }
}
