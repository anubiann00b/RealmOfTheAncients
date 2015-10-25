package me.shreyasr.ancients.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

public class HitboxComponent implements Component, Pool.Poolable {

    public static ComponentMapper<HitboxComponent> MAPPER
            = ComponentMapper.getFor(HitboxComponent.class);

    public static HitboxComponent create(PooledEngine engine) {
        return create(engine, 0, 0, 0, 0);
    }

    public static HitboxComponent create(PooledEngine engine, int x, int y, int w, int h) {
        HitboxComponent hitbox = engine.createComponent(HitboxComponent.class);
        hitbox.x = x;
        hitbox.y = y;
        hitbox.w = w;
        hitbox.h = h;
        return hitbox;
    }

    public float x;
    public float y;
    public float w;
    public float h;

    public boolean active;

    public boolean intersects(HitboxComponent other) {
        return x < other.x + other.w && x + w > other.x && y < other.y + other.h && y + h > other.y;
    }

    public HitboxComponent() {
        reset();
    }

    @Override
    public void reset() {
        x = 0;
        y = 0;
        w = 0;
        h = 0;
        active = true;
    }
}
