package me.shreyasr.ancients.handler;

import com.esotericsoftware.kryonet.Connection;

import me.shreyasr.ancients.ServerMain;
import me.shreyasr.ancients.packet.PacketHandler;
import me.shreyasr.ancients.packet.server.ServerNameRegistrationPacket;

// TODO
public class ServerNameRegistrationPacketHandler extends PacketHandler<ServerNameRegistrationPacket> {

    private final ServerMain server;
//    private final Family nameFamily = Family.all(NameComponent.class).get();
//    private ArrayList<String> pendingNames = new ArrayList<String>();

    public ServerNameRegistrationPacketHandler(ServerMain server) {
        this.server = server;
    }

    @Override
    public void handle(ServerNameRegistrationPacket packet, Connection conn) {
//        ImmutableArray<Entity> namedEntities = server.engine.getEntitiesFor(nameFamily);
//        for(Entity e : namedEntities) {
//            if (NameComponent.MAPPER.get(e).str.equals(packet.name)) {
//                conn.sendTCP()
//            }
//        }
    }
}
