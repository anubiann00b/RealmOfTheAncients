package me.shreyasr.ancients.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

import java.util.Comparator;

public class StatsComponent implements Component, Pool.Poolable {

    public static ComponentMapper<StatsComponent> MAPPER
            = ComponentMapper.getFor(StatsComponent.class);

    public static StatsComponent create(PooledEngine engine) {
        return engine.createComponent(StatsComponent.class);
    }

    public int hits;

    public StatsComponent() {
        reset();
    }

    @Override
    public void reset() {
        hits = 0;
    }

    public static class StatsComparator implements Comparator<Entity> {

        @Override
        public int compare(Entity o1, Entity o2) {
            StatsComponent s1 = StatsComponent.MAPPER.get(o1);
            StatsComponent s2 = StatsComponent.MAPPER.get(o2);
            return Integer.compare(s1.hits, s2.hits);
        }
    }
}
