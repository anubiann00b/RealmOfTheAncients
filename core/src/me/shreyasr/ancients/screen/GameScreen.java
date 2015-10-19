package me.shreyasr.ancients.screen;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;

import me.shreyasr.ancients.AncientsGame;
import me.shreyasr.ancients.ClientPacketListener;
import me.shreyasr.ancients.CustomUUID;
import me.shreyasr.ancients.EntityFactory;
import me.shreyasr.ancients.LinkedListQueuedListener;
import me.shreyasr.ancients.systems.MiscRenderSystem;
import me.shreyasr.ancients.systems.NetworkUpdateSystem;
import me.shreyasr.ancients.systems.PacketHandleSystem;
import me.shreyasr.ancients.systems.PingUpdateSystem;
import me.shreyasr.ancients.systems.PlayerMovementSystem;
import me.shreyasr.ancients.systems.PositionUpdateSystem;
import me.shreyasr.ancients.systems.PostRenderSystem;
import me.shreyasr.ancients.systems.PreRenderSystem;
import me.shreyasr.ancients.systems.PredictorSystem;
import me.shreyasr.ancients.systems.RenderSystem;
import me.shreyasr.ancients.systems.SquareAnimationSystem;

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

        CustomUUID playerUUID = CustomUUID.randomUUID();
        System.out.println("My UUID: " + playerUUID);

//        engine.addEntity(entityFactory.createDumbPlayer(engine));
        engine.addEntity(entityFactory.createPlayer(engine, playerUUID));

        // lowest first
        int priority = 0;
        engine.addSystem(new PacketHandleSystem(++priority, queuedListener));
        engine.addSystem(new  PlayerMovementSystem(++priority));
        engine.addSystem(new       PredictorSystem(++priority));
        engine.addSystem(new  PositionUpdateSystem(++priority));
        engine.addSystem(new SquareAnimationSystem(++priority));
        engine.addSystem(new       PreRenderSystem(++priority, game.batch));
        engine.addSystem(new          RenderSystem(++priority, game.batch, game.assetManager));
        engine.addSystem(new      MiscRenderSystem(++priority, game, client));
        engine.addSystem(new      PostRenderSystem(++priority, game.batch));
        engine.addSystem(new   NetworkUpdateSystem(++priority, client));
        engine.addSystem(new      PingUpdateSystem(++priority, client));

        queuedListener.setListener(
                new ClientPacketListener(engine, playerUUID, engine.getSystem(PacketHandleSystem.class)));

        client.addListener(queuedListener);

        initialized = true;
    }

    @Override
    public void render(float delta) {
        if (!initialized) return;
        engine.update(Gdx.graphics.getRawDeltaTime() * 1000);
    }
}
