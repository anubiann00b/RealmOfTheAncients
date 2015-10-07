package me.shreyasr.ancients.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Pool;

public class TextureComponent implements Component, Pool.Poolable {

    public static ComponentMapper<TextureComponent> MAPPER
            = ComponentMapper.getFor(TextureComponent.class);

    public Texture texture;

    // texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    public TextureComponent() {
        reset();
    }

    @Override
    public void reset() {
        texture = null;
    }
}
