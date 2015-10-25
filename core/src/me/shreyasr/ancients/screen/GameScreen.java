package me.shreyasr.ancients.screen;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.esotericsoftware.kryonet.Client;

import me.shreyasr.ancients.AncientsGame;
import me.shreyasr.ancients.ClientPacketListener;
import me.shreyasr.ancients.CustomUUID;
import me.shreyasr.ancients.EntityFactory;
import me.shreyasr.ancients.LinkedListQueuedListener;
import me.shreyasr.ancients.systems.network.NetworkUpdateSystem;
import me.shreyasr.ancients.systems.network.PacketHandleSystem;
import me.shreyasr.ancients.systems.network.PingUpdateSystem;
import me.shreyasr.ancients.systems.render.DebugRenderSystem;
import me.shreyasr.ancients.systems.render.MiscRenderSystem;
import me.shreyasr.ancients.systems.render.PostRenderSystem;
import me.shreyasr.ancients.systems.render.PreRenderSystem;
import me.shreyasr.ancients.systems.render.RenderSystem;
import me.shreyasr.ancients.systems.render.SquareAnimationSystem;
import me.shreyasr.ancients.systems.update.CollisionDetectionSystem;
import me.shreyasr.ancients.systems.update.InputActionSystem;
import me.shreyasr.ancients.systems.update.MyPlayerMovementSystem;
import me.shreyasr.ancients.systems.update.PositionUpdateSystem;
import me.shreyasr.ancients.systems.update.WeaponUpdateSystem;

public class GameScreen extends ScreenAdapter {

    private AncientsGame game;
    private Client client;

    public GameScreen(AncientsGame game, Client client) {
        this.game = game;
        this.client = client;
    }

    private PooledEngine engine;

    private boolean initialized = false;

    @Override
    public void show() {
        EntityFactory entityFactory = new EntityFactory(640, 480);

        // LinkedListQueuedListener queuedListener = new LagLinkedListQueuedListener(100, 0);
        LinkedListQueuedListener queuedListener = new LinkedListQueuedListener();

        engine = new PooledEngine();

        CustomUUID playerUUID = CustomUUID.randomUUID();
        System.out.println("My UUID: " + playerUUID);

        engine.addEntity(entityFactory.createPlayer(engine, playerUUID));

        // lowest first
        int priority = 0;
        engine.addSystem(new       PacketHandleSystem(++priority, queuedListener));
        engine.addSystem(new   MyPlayerMovementSystem(++priority));
        engine.addSystem(new     PositionUpdateSystem(++priority));
        engine.addSystem(new        InputActionSystem(++priority, engine, entityFactory, client));
        engine.addSystem(new    SquareAnimationSystem(++priority));
        engine.addSystem(new       WeaponUpdateSystem(++priority, engine, client));
        engine.addSystem(new CollisionDetectionSystem(++priority));
        engine.addSystem(new          PreRenderSystem(++priority, game.batch));
        engine.addSystem(new             RenderSystem(++priority, game.batch, game.assetManager));
        engine.addSystem(new         MiscRenderSystem(++priority, game, client));
        engine.addSystem(new        DebugRenderSystem(++priority, game));
        engine.addSystem(new         PostRenderSystem(++priority, game.batch));
        engine.addSystem(new      NetworkUpdateSystem(++priority, client));
        engine.addSystem(new         PingUpdateSystem(++priority, client));

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
