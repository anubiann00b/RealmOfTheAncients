package me.shreyasr.ancients.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

import java.util.Comparator;

import me.shreyasr.ancients.components.player.MyPlayerComponent;

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

    public static class ReversedStatsComparator implements Comparator<Entity> {

        private StatsComparator comparator = new StatsComparator();

        @Override
        public int compare(Entity o1, Entity o2) {
            return comparator.compare(o2, o1);
        }
    }

    /**
     * Lowest to highest.
     */
    public static class StatsComparator implements Comparator<Entity> {

        @Override
        public int compare(Entity o1, Entity o2) {
            StatsComponent s1 = StatsComponent.MAPPER.get(o1);
            StatsComponent s2 = StatsComponent.MAPPER.get(o2);

            if (s1.hits != s2.hits) {
                return Integer.compare(s1.hits, s2.hits);
            }

            if (MyPlayerComponent.MAPPER.has(o1)) return 1;
            if (MyPlayerComponent.MAPPER.has(o2)) return -1;

            NameComponent n1 = NameComponent.MAPPER.get(o1);
            NameComponent n2 = NameComponent.MAPPER.get(o2);

            if (n1 != null) return n1.compareTo(n2);
            if (n2 != null) return n2.compareTo(n1);

            return 0;
        }
    }
}
