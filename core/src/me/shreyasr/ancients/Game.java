package me.shreyasr.ancients;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.dermetfan.gdx.assets.AnnotationAssetManager;

public class Game extends ApplicationAdapter {

    SpriteBatch batch;
    AnnotationAssetManager assetManager;

    @Override
    public void create() {
        assetManager = new AnnotationAssetManager();
        assetManager.load(Assets.class);

        batch = new SpriteBatch();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(assetManager.get(Assets.BADLOGIC, Texture.class), 0, 0);
        batch.end();
    }
}
