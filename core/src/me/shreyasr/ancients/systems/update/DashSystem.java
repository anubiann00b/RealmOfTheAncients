package me.shreyasr.ancients.systems.update;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Time;

import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.components.VelocityComponent;
import me.shreyasr.ancients.components.player.MyPlayerComponent;
import me.shreyasr.ancients.components.player.dash.DashComponent;
import me.shreyasr.ancients.util.EntityFactory;
import me.shreyasr.ancients.util.MathHelper;

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
        PositionComponent pos = PositionComponent.MAPPER.get(entity);
        VelocityComponent vel = VelocityComponent.MAPPER.get(entity);
        DashComponent dash = DashComponent.MAPPER.get(entity);

        boolean doDash = !dash.inFuture() && (dash.isActive() || dash.firstFrame);

        if (doDash || dash.isStunned()) {
            vel.dx = 0;
            vel.dy = 0;
            dash.firstFrame = false;

            long elapsed = Time.getServerMillis()-dash.startTime;
            float percentageDone = MathHelper.clamp(0f, (float)elapsed/dash.duration, 1f);
            if (dash.duration == -1) percentageDone = 1;
            if (dash.isStunned()) percentageDone = 1;

            pos.x = dash.x + (dash.dx*dash.distance)*(percentageDone);
            pos.y = dash.y + (dash.dy*dash.distance)*(percentageDone);

            if (dash.behavior != null && dash.duration != -1) {
                dash.behavior.update(entity, percentageDone, dash.dx, dash.dy);
            }
        }
    }
}
