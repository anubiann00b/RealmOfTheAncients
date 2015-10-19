package me.shreyasr.ancients.components.player;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.utils.Pool;

public class MyPlayerComponent implements Component, Pool.Poolable {

    public static ComponentMapper<MyPlayerComponent> MAPPER
            = ComponentMapper.getFor(MyPlayerComponent.class);

    public MyPlayerComponent() {
        reset();
    }

    @Override
    public void reset() {

    }
}
