package me.shreyasr.ancients.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

public class VelocityComponent implements Component, Pool.Poolable {

    public static ComponentMapper<VelocityComponent> MAPPER
            = ComponentMapper.getFor(VelocityComponent.class);

    public static VelocityComponent create(PooledEngine engine, float dx, float dy) {
        VelocityComponent v = engine.createComponent(VelocityComponent.class);
        v.dx = dx;
        v.dy = dy;
        return v;
    }

    public float dx;
    public float dy;

    public VelocityComponent() {
        reset();
    }

    @Override
    public void reset() {
        dx = 0;
        dy = 0;
    }
}

