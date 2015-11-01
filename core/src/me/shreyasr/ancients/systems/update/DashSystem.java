package me.shreyasr.ancients.systems.update;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Time;

import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.components.StartTimeComponent;
import me.shreyasr.ancients.components.VelocityComponent;
import me.shreyasr.ancients.components.player.MyPlayerComponent;
import me.shreyasr.ancients.components.player.dash.DashComponent;
import me.shreyasr.ancients.packet.server.ServerAttackPacket;
import me.shreyasr.ancients.util.EntityFactory;

public class DashSystem extends IteratingSystem {


    private final PooledEngine engine;
    private final EntityFactory factory;
    private final Client client;
    private Entity player;

    public DashSystem(int priority, PooledEngine engine, EntityFactory factory, Client client) {
        super(
                Family.all(DashComponent.class,
                           VelocityComponent.class)
                        .get(),
                priority);
        this.engine = engine;
        this.factory = factory;
        this.client = client;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        player = engine.getEntitiesFor(Family.all(MyPlayerComponent.class).get()).get(0);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        VelocityComponent vel = VelocityComponent.MAPPER.get(entity);
        DashComponent dash = DashComponent.MAPPER.get(entity);

        boolean doDash = !dash.inFuture() && (dash.isActive() || dash.firstFrame);

        if (doDash) {
            dash.firstFrame = false;

            vel.dx = dash.dx * dash.distance / Math.max(dash.duration, 16);
            vel.dy = dash.dy * dash.distance / Math.max(dash.duration, 16);

            if (dash.behavior != null && dash.duration != -1) {
                dash.behavior.update(entity,
                        ((double) Time.getServerMillis() - (double) dash.startTime) / (double) dash.duration,
                        dash.dx, dash.dy);
            }
        } else if (dash.isStunned()) {
            vel.dx = 0;
            vel.dy = 0;
        }

        if (dash.attack != null) {
            PositionComponent pos = PositionComponent.MAPPER.get(player);
            Entity newWeapon = dash.attack.update(engine, factory, player, pos,
                    deltaTime, doDash, dash.dx, dash.dy);

            if (newWeapon != null) {
                newWeapon.add(StartTimeComponent.create(
                        Time.getServerMillis() + client.getReturnTripTime() / 2 + ServerAttackPacket.ATTACK_DELAY_MS));
                engine.addEntity(newWeapon);

                Component[] newAttackComponents = newWeapon.getComponents().toArray(Component.class);
                client.sendUDP(ServerAttackPacket.create(newAttackComponents));
            }
        }
    }
}
