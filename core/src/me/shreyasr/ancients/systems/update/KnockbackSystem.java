package me.shreyasr.ancients.systems.update;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.esotericsoftware.kryonet.Time;

import me.shreyasr.ancients.components.KnockbackComponent;
import me.shreyasr.ancients.components.VelocityComponent;

public class KnockbackSystem extends IteratingSystem {


    public KnockbackSystem(int priority) {
        super(
                Family.all(KnockbackComponent.class,
                           VelocityComponent.class)
                        .get(),
                priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        VelocityComponent vel = VelocityComponent.MAPPER.get(entity);
        KnockbackComponent knockback = KnockbackComponent.MAPPER.get(entity);

        knockback.timeSinceKnockbackStart = Time.getServerMillis() - knockback.startTime;

        if (knockback.isDone()) {
            entity.remove(KnockbackComponent.class);
        }

        vel.dx = knockback.dx * knockback.getMult();
        vel.dy = knockback.dy * knockback.getMult();
    }
}
