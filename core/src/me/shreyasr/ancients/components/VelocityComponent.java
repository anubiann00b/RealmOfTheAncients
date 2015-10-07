package me.shreyasr.ancients.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.utils.Pool;

public class VelocityComponent implements Component, Pool.Poolable {

    public static ComponentMapper<VelocityComponent> MAPPER
            = ComponentMapper.getFor(VelocityComponent.class);

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

