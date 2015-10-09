package me.shreyasr.ancients;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;
import me.shreyasr.ancients.systems.*;

public class GameScreen extends ScreenAdapter {

    private AncientsGame game;
    private Client client;

    public GameScreen(AncientsGame game, Client client) {
        this.game = game;
        this.client = client;
    }

    PooledEngine engine;
    EntityFactory entityFactory;

    boolean initialized = false;

    @Override
    public void show() {
        Log.INFO();
        entityFactory = new EntityFactory(640, 480);

        LinkedListQueuedListener queuedListener = new LinkedListQueuedListener();

        engine = new PooledEngine();

//        engine.addEntity(entityFactory.createDumbPlayer(engine));
        engine.addEntity(entityFactory.createPlayer(engine));

        // lowest first
        engine.addSystem(new    PacketHandleSystem(0, queuedListener));
        engine.addSystem(new       PreRenderSystem(1, game.batch));
        engine.addSystem(new  PlayerMovementSystem(2));
        engine.addSystem(new  PositionUpdateSystem(3));
        engine.addSystem(new SquareAnimationSystem(4));
        engine.addSystem(new          RenderSystem(5, game.batch, game.assetManager));
        engine.addSystem(new      PostRenderSystem(6, game.batch));
        engine.addSystem(new   NetworkUpdateSystem(7, client));

        queuedListener.setListener(
                new ClientPacketListener(engine, engine.getSystem(PacketHandleSystem.class)));

        client.addListener(queuedListener);

        initialized = true;
    }

    @Override
    public void render(float delta) {
        if (!initialized) return;
        engine.update(Gdx.graphics.getRawDeltaTime() * 1000);
    }
}
