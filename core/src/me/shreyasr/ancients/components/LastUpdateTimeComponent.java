package me.shreyasr.ancients.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.utils.Pool;

public class LastUpdateTimeComponent implements Component, Pool.Poolable {

    public static ComponentMapper<LastUpdateTimeComponent> MAPPER
            = ComponentMapper.getFor(LastUpdateTimeComponent.class);

    public static LastUpdateTimeComponent create(long lastUpdateTime) {
        LastUpdateTimeComponent c = new LastUpdateTimeComponent();
        c.lastUpdateTime = lastUpdateTime;
        return c;
    }

    public long lastUpdateTime;

    public LastUpdateTimeComponent() {
        reset();
    }

    @Override
    public void reset() {
        lastUpdateTime = -1;
    }
}
