package me.shreyasr.ancients.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.Time;
import com.esotericsoftware.minlog.Log;

import me.shreyasr.ancients.components.LastUpdateTimeComponent;
import me.shreyasr.ancients.components.UUIDComponent;
import me.shreyasr.ancients.components.type.TypeComponent;
import me.shreyasr.ancients.packet.client.ClientPlayerRemovePacket;

public class RemoveOldEntitySystem extends IteratingSystem {

    private final PooledEngine engine;
    private final Server server;

    public RemoveOldEntitySystem(int priority, PooledEngine engine, Server server) {
        super(Family.all(TypeComponent.Player.class).get(), priority);
        this.engine = engine;
        this.server = server;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (Time.getMillis()
                - LastUpdateTimeComponent.MAPPER.get(entity).lastUpdateTime > 5000) {
            UUIDComponent uuid = UUIDComponent.MAPPER.get(entity);
            Log.info("Removing old player: " + uuid);
            server.sendToAllTCP(ClientPlayerRemovePacket.create(uuid));
            engine.removeEntity(entity);
        }
    }
}
