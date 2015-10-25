package me.shreyasr.ancients.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.utils.Pool;

/**
 * Start time of an animation or knockback.
 */
public class StartTimeComponent implements Component, Pool.Poolable {

    public static ComponentMapper<StartTimeComponent> MAPPER
            = ComponentMapper.getFor(StartTimeComponent.class);

    public static StartTimeComponent create(long lastUpdateTime) {
        StartTimeComponent c = new StartTimeComponent();
        c.val = lastUpdateTime;
        return c;
    }

    public long val;

    public StartTimeComponent() {
        reset();
    }

    @Override
    public void reset() {
        val = -1;
    }
}
