package me.shreyasr.ancients.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import me.shreyasr.ancients.components.*;

public class PredictorSystem extends IteratingSystem {

    public PredictorSystem(int priority) {
        super(
                Family.exclude(MyPlayerComponent.class).get(),
                priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        VelocityComponent vel = VelocityComponent.MAPPER.get(entity);
        SquareDirectionComponent dir = SquareDirectionComponent.MAPPER.get(entity);

        vel.dx = dir.dir.getX();
        vel.dy = dir.dir.getY();
    }
}
