package me.shreyasr.ancients;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.shreyasr.ancients.systems.*;

public class Game extends ApplicationAdapter {

    PooledEngine engine;
    SpriteBatch batch;
    AssetManager assetManager;
    EntityFactory entityFactory;

    @Override
    public void create() {
        assetManager = new AssetManager();
        Assets.loadAll(assetManager);
        entityFactory = new EntityFactory(640, 480);

        batch = new SpriteBatch();

        engine = new PooledEngine();

        engine.addEntity(entityFactory.createDumbPlayer(engine));
        engine.addEntity(entityFactory.createPlayer(engine));

        // lowest first
        engine.addSystem(new       PreRenderSystem(0, batch));
        engine.addSystem(new  PlayerMovementSystem(1));
        engine.addSystem(new  PositionUpdateSystem(2));
        engine.addSystem(new SquareAnimationSystem(3));
        engine.addSystem(new          RenderSystem(4, batch, assetManager));
        engine.addSystem(new      PostRenderSystem(5, batch));

        assetManager.finishLoading();
    }

    @Override
    public void render() {
        engine.update(Gdx.graphics.getRawDeltaTime()*1000);
    }
}
