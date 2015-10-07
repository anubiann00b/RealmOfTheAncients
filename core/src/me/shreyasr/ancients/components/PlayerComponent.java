package me.shreyasr.ancients.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.utils.Pool;

public class PlayerComponent implements Component, Pool.Poolable {

    public static ComponentMapper<PlayerComponent> MAPPER
            = ComponentMapper.getFor(PlayerComponent.class);

    public PlayerComponent() {
        reset();
    }

    @Override
    public void reset() {

    }
}
