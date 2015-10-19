package me.shreyasr.ancients.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

public class SpeedComponent implements Component, Pool.Poolable {

    public static ComponentMapper<SpeedComponent> MAPPER
            = ComponentMapper.getFor(SpeedComponent.class);

    public static SpeedComponent create(PooledEngine engine, float speed) {
        SpeedComponent spd = engine.createComponent(SpeedComponent.class);
        spd.speed = speed;
        return spd;
    }

    public double speed;

    public SpeedComponent() {
        reset();
    }

    @Override
    public void reset() {
        speed = 0;
    }
}
