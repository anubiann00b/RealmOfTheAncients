package me.shreyasr.ancients.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.utils.Pool;

public class TextureComponent implements Component, Pool.Poolable {

    public static ComponentMapper<TextureComponent> MAPPER
            = ComponentMapper.getFor(TextureComponent.class);

    public static TextureComponent create(String file) {
        TextureComponent anim = new TextureComponent();
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
