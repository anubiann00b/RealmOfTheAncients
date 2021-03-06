package me.shreyasr.ancients.systems.network;

import com.badlogic.ashley.core.*;
import com.esotericsoftware.kryonet.Client;
import me.shreyasr.ancients.components.player.MyPlayerComponent;
import me.shreyasr.ancients.packet.server.ServerPlayerUpdatePacket;

public class NetworkUpdateSystem extends EntitySystem {

    private final Client client;
    private Entity player;

    public NetworkUpdateSystem(int priority, Client client) {
        super(priority);
        this.client = client;
    }

    public void addedToEngine(Engine engine) {
        player = engine.getEntitiesFor(Family.all(MyPlayerComponent.class).get()).get(0);
    }

    public void update(float deltaTime) {
        Component[] componentsArr = player.getComponents().toArray(Component.class);
        client.sendUDP(ServerPlayerUpdatePacket.create(componentsArr));
    }
}
