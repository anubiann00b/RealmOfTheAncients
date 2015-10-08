package me.shreyasr.ancients;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.esotericsoftware.kryonet.Client;
import me.shreyasr.ancients.systems.*;

public class GameScreen extends ScreenAdapter {

    private AncientsGame game;

    public GameScreen(AncientsGame game, Client client) {
        this.game = game;
    }

    PooledEngine engine;
    EntityFactory entityFactory;
    
    boolean initialized = false;

    @Override
    public void show() {
        entityFactory = new EntityFactory(640, 480);

        engine = new PooledEngine();

        engine.addEntity(entityFactory.createDumbPlayer(engine));
        engine.addEntity(entityFactory.createPlayer(engine));

        // lowest first
        engine.addSystem(new       PreRenderSystem(0, game.batch));
        engine.addSystem(new  PlayerMovementSystem(1));
        engine.addSystem(new  PositionUpdateSystem(2));
        engine.addSystem(new SquareAnimationSystem(3));
        engine.addSystem(new          RenderSystem(4, game.batch, game.assetManager));
        engine.addSystem(new      PostRenderSystem(5, game.batch));

        initialized = true;
    }

    @Override
    public void render(float delta) {
        if (!initialized) return;
        engine.update(Gdx.graphics.getRawDeltaTime() * 1000);
    }
}
