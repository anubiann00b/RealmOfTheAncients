package me.shreyasr.ancients.handler;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.Time;

import me.shreyasr.ancients.ServerMain;
import me.shreyasr.ancients.components.StartTimeComponent;
import me.shreyasr.ancients.components.weapon.OwnerUUIDComponent;
import me.shreyasr.ancients.packet.PacketHandler;
import me.shreyasr.ancients.packet.client.ClientAttackPacket;
import me.shreyasr.ancients.packet.server.ServerAttackPacket;

public class ServerAttackPacketHandler extends PacketHandler<ServerAttackPacket> {

    private final ServerMain server;

    public ServerAttackPacketHandler(ServerMain server) {
        this.server = server;
    }

    @Override
    public void handle(ServerAttackPacket packet, Connection conn) {
        Entity newWeapon = server.engine.createEntity();
        for (Component c : packet.components) {
            if (c instanceof OwnerUUIDComponent) {
                ((OwnerUUIDComponent) c).updateEngineId(server.engine);
            }
            newWeapon.add(c);
        }
        newWeapon.add(StartTimeComponent.create(Time.getMillis() + ServerAttackPacket.ATTACK_DELAY_MS));
        server.engine.addEntity(newWeapon);

        send(newWeapon, server.server);
    }

    private void send(Entity newWeapon, Server server) {
        Component[] newComponents = newWeapon.getComponents().toArray(Component.class);
        server.sendToAllUDP(ClientAttackPacket.create(newComponents));
    }
}
