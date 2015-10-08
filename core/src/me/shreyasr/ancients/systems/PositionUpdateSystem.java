package me.shreyasr.ancients.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.components.VelocityComponent;

public class PositionUpdateSystem extends IteratingSystem {

    public PositionUpdateSystem(int priority) {
        super(
                Family.all(PositionComponent.class,
                           VelocityComponent.class)
                      .get(),
                priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pos = PositionComponent.MAPPER.get(entity);
        VelocityComponent vel = VelocityComponent.MAPPER.get(entity);

        double speedMult = deltaTime * 60 / 1000;

        pos.x += vel.dx * speedMult;
        pos.y += vel.dy * speedMult;
    }
}
