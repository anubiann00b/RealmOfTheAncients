package me.shreyasr.ancients.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;
import com.esotericsoftware.kryonet.Time;

public class KnockbackComponent implements Component, Pool.Poolable {

    public static ComponentMapper<KnockbackComponent> MAPPER
            = ComponentMapper.getFor(KnockbackComponent.class);

    public static KnockbackComponent create(PooledEngine engine, float x, float y, float dx, float dy, long startTime) {
        KnockbackComponent k = engine.createComponent(KnockbackComponent.class);
        k.x = x;
        k.y = y;
        float len = (float) Math.sqrt(dx*dx + dy*dy);
        k.dx = dx / len;
        k.dy = dy / len;
        k.startTime = startTime;
        return k;
    }

    public float x;
    public float y;

    public float dx;
    public float dy;

    public float weight;
    public float power;

    public long startTime;
    public int duration;

    public boolean isStarted() {
        return Time.getServerMillis() >= startTime;
    }

    public boolean isDone() {
        return Time.getServerMillis() >= startTime + duration;
    }

    public boolean isActive() {
        return isStarted() && !isDone();
    }

    public boolean isScheduled() {
        return !isDone();
    }

    public float getDistance() {
        return power/weight;
    }

    public KnockbackComponent() {
        reset();
    }

    @Override
    public void reset() {
        x = 0;
        y = 0;
        dx = 0;
        dy = 0;
        weight = 1;
        power = 1;
        startTime = -1;
        duration = 400;
    }
}