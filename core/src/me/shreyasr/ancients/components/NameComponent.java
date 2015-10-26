package me.shreyasr.ancients.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

public class NameComponent implements Component, Pool.Poolable {

    public static ComponentMapper<NameComponent> MAPPER
            = ComponentMapper.getFor(NameComponent.class);

    public static NameComponent create(PooledEngine engine, String name) {
        NameComponent n = engine.createComponent(NameComponent.class);
        n.str = name;
        return n;
    }

    public String str;

    public NameComponent() {
        reset();
    }

    @Override
    public void reset() {
        str = null;
    }
}
