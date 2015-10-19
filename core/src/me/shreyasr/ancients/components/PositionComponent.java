package me.shreyasr.ancients.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

public class PositionComponent implements Component, Pool.Poolable {

    public static ComponentMapper<PositionComponent> MAPPER
            = ComponentMapper.getFor(PositionComponent.class);

    public static PositionComponent create(PooledEngine engine, float x, float y) {
        PositionComponent p = engine.createComponent(PositionComponent.class);
        p.x = x;
        p.y = y;
        return p;
    }

    public float x;
    public float y;

    public PositionComponent() {
        reset();
    }

    @Override
    public void reset() {
        x = 0;
        y = 0;
    }
}
