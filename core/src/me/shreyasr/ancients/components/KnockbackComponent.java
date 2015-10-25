package me.shreyasr.ancients.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

public class KnockbackComponent implements Component, Pool.Poolable {

    public static ComponentMapper<KnockbackComponent> MAPPER
            = ComponentMapper.getFor(KnockbackComponent.class);

    public static KnockbackComponent create(PooledEngine engine, float dx, float dy, long startTime) {
        KnockbackComponent k = engine.createComponent(KnockbackComponent.class);
        float len = (float) Math.sqrt(dx*dx + dy*dy);
        k.dx = dx / len;
        k.dy = dy / len;
        k.startTime = startTime;
        return k;
    }

    public float dx;
    public float dy;

    public float weight;
    public float power;

    public long startTime;
    public long timeSinceKnockbackStart;
    public int duration;

    public boolean isStarted() {
        return timeSinceKnockbackStart >= 0;
    }

    public boolean isDone() {
        return timeSinceKnockbackStart >= duration;
    }

    public boolean isActive() {
        return isStarted() && isDone();
    }

    public float getMult() {
        return (duration-timeSinceKnockbackStart)*power/weight/200;
    }

    public KnockbackComponent() {
        reset();
    }

    @Override
    public void reset() {
        dx = 0;
        dy = 0;
        weight = 1;
        power = 1;
        startTime = -1;
        timeSinceKnockbackStart = -1;
        duration = 400;
    }
}