package me.shreyasr.ancients.systems.update;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.esotericsoftware.kryonet.Time;

import me.shreyasr.ancients.components.KnockbackComponent;
import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.components.VelocityComponent;
import me.shreyasr.ancients.util.MathHelper;

public class KnockbackSystem extends IteratingSystem {


    public KnockbackSystem(int priority) {
        super(
                Family.all(KnockbackComponent.class,
                           PositionComponent.class)
                        .get(),
                priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pos = PositionComponent.MAPPER.get(entity);
        VelocityComponent vel = VelocityComponent.MAPPER.get(entity);
        KnockbackComponent knockback = KnockbackComponent.MAPPER.get(entity);

        long elapsed = Time.getServerMillis()-knockback.startTime;
        float percentageDone = MathHelper.clamp(0f, (float) elapsed / knockback.duration, 1f);
        if (knockback.isDone()) percentageDone = 1;
        percentageDone = interpolateKnockback(percentageDone);
        pos.x = knockback.x + (knockback.dx*knockback.getDistance())*(percentageDone);
        pos.y = knockback.y + (knockback.dy*knockback.getDistance())*(percentageDone);

        if (knockback.isDone()) {
            entity.remove(KnockbackComponent.class);
        } else {
            vel.dx = 0;
            vel.dy = 0;
        }
    }

    private float interpolateKnockback(float percent) {
        return 2*percent - percent*percent;
    }
}
