package me.shreyasr.ancients.components.type;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

public abstract class TypeComponent implements Component, Pool.Poolable {

    @Override
    public void reset() { }

    public static boolean is(Entity e, Class<? extends TypeComponent> cls) {
        return ComponentMapper.getFor(cls).has(e);
    }

    public static TypeComponent create(PooledEngine engine, Class<? extends TypeComponent> cls) {
        return engine.createComponent(cls);
    }

    public static class Player extends TypeComponent { public Player() {} }
    public static class Weapon extends TypeComponent { public Weapon() {} }
}
