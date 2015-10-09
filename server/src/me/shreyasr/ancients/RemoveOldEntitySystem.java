package me.shreyasr.ancients;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.esotericsoftware.kryonet.Server;
import me.shreyasr.ancients.components.LastUpdateTimeComponent;
import me.shreyasr.ancients.components.UUIDComponent;
import me.shreyasr.ancients.packet.ClientPlayerRemovePacket;

public class RemoveOldEntitySystem extends IteratingSystem {

    private final PooledEngine engine;
    private final Server server;

    public RemoveOldEntitySystem(int priority, PooledEngine engine, Server server) {
        super(Family.all(LastUpdateTimeComponent.class).get(), priority);
        this.engine = engine;
        this.server = server;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (System.currentTimeMillis()
                - LastUpdateTimeComponent.MAPPER.get(entity).lastUpdateTime > 5000) {
            System.out.println("Removing old player");
            server.sendToAllTCP(ClientPlayerRemovePacket.create(UUIDComponent.MAPPER.get(entity)));
            engine.removeEntity(entity);
        }
    }
}
