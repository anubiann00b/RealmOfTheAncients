package me.shreyasr.ancients.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

public class TextureComponent implements Component, Pool.Poolable {

    public static ComponentMapper<TextureComponent> MAPPER
            = ComponentMapper.getFor(TextureComponent.class);

    public static TextureComponent create(PooledEngine engine, String file) {
        TextureComponent anim = engine.createComponent(TextureComponent.class);
        anim.textureFile = file;
        return anim;
    }


    public String textureFile;

    public TextureComponent() {
        reset();
    }

    @Override
    public void reset() {
        textureFile = null;
    }
}
