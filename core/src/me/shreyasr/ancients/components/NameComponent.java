package me.shreyasr.ancients.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

public class NameComponent implements Component, Pool.Poolable, Comparable<NameComponent> {

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

    @Override
    public int compareTo(NameComponent o) {
        boolean nullStr = str == null;
        boolean nullOther = o == null || o.str == null;
        if (nullStr && nullOther) return 0;
        if (nullStr) return -1;
        if (nullOther) return 1;
        return str.compareTo(o.str);
    }
}
