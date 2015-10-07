package me.shreyasr.ancients;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.shreyasr.ancients.systems.*;
import net.dermetfan.gdx.assets.AnnotationAssetManager;

public class Game extends ApplicationAdapter {

    PooledEngine engine;
    SpriteBatch batch;
    AnnotationAssetManager assetManager;
    EntityFactory entityFactory;

    @Override
    public void create() {
        assetManager = new AnnotationAssetManager();
        assetManager.load(Assets.class);
        entityFactory = new EntityFactory();

        batch = new SpriteBatch();

        engine = new PooledEngine();

        engine.addEntity(entityFactory.createDumbPlayer(engine));

        // lowest first
        engine.addSystem(new       PreRenderSystem(0, batch));
        engine.addSystem(new  PositionUpdateSystem(1));
        engine.addSystem(new SquareAnimationSystem(2));
        engine.addSystem(new          RenderSystem(3, batch, assetManager));
        engine.addSystem(new      PostRenderSystem(4, batch));

        assetManager.finishLoading();
    }

    @Override
    public void render() {
        engine.update(Gdx.graphics.getRawDeltaTime()*1000);
    }
}
