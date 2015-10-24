package me.shreyasr.ancients;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalIteratingSystem;
import com.esotericsoftware.kryonet.Server;

import me.shreyasr.ancients.components.type.TypeComponent;
import me.shreyasr.ancients.packet.client.ClientPlayerUpdatePacket;

public class ClientUpdateSystem extends IntervalIteratingSystem {

    private final Server server;

    public ClientUpdateSystem(int priority, float interval, Server server) {
        super(Family.all(TypeComponent.Player.class).get(), interval, priority);
        this.server = server;
    }

    @Override
    protected void processEntity(Entity entity) {
        Component[] componentsArr = entity.getComponents().toArray(Component.class);
        server.sendToAllUDP(ClientPlayerUpdatePacket.create(componentsArr));
    }
}
