package me.shreyasr.ancients;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.shreyasr.ancients.components.PlayerComponent;
import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.systems.SquareAnimationSystem;
import me.shreyasr.ancients.systems.PositionUpdateSystem;
import me.shreyasr.ancients.systems.RenderSystem;
import net.dermetfan.gdx.assets.AnnotationAssetManager;

public class Game extends ApplicationAdapter {

    PooledEngine engine;
    SpriteBatch batch;
    AnnotationAssetManager assetManager;

    @Override
    public void create() {
        assetManager = new AnnotationAssetManager();
        assetManager.load(Assets.class);

        batch = new SpriteBatch();

        engine = new PooledEngine();

        Entity player = engine.createEntity();
        player.add(new PositionComponent());
        player.add(new PlayerComponent());
        engine.addEntity(player);

        // lowest first
        engine.addSystem(new  PositionUpdateSystem(1));
        engine.addSystem(new SquareAnimationSystem(2));
        engine.addSystem(new          RenderSystem(3, batch));
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
