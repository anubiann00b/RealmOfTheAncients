package me.shreyasr.ancients.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.utils.Pool;

public class TextureTransformComponent implements Component, Pool.Poolable {

    public static ComponentMapper<TextureTransformComponent> MAPPER
            = ComponentMapper.getFor(TextureTransformComponent.class);

    public int screenWidth;
    public int screenHeight;

    public int originX;
    public int originY;

    public int srcX;
    public int srcY;
    public int srcWidth;
    public int srcHeight;

    public float rotation;

    public TextureTransformComponent() {
        reset();
    }

    @Override
    public void reset() {
        screenWidth = 0;
        screenHeight = 0;
        originX = 0;
        originY = 0;
        srcX = 0;
        srcY = 0;
        srcWidth = 0;
        srcHeight = 0;
        rotation = 0;
    }
}

