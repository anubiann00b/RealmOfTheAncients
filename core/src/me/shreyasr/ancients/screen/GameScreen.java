package me.shreyasr.ancients.screen;

import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;

import me.shreyasr.ancients.AncientsGame;
import me.shreyasr.ancients.packet.client.ClientAttackPacket;
import me.shreyasr.ancients.packet.client.ClientChatMessagePacket;
import me.shreyasr.ancients.packet.client.ClientHitPacket;
import me.shreyasr.ancients.packet.client.ClientPlayerRemovePacket;
import me.shreyasr.ancients.packet.client.ClientPlayerUpdatePacket;
import me.shreyasr.ancients.packet.client.handler.ClientAttackPacketHandler;
import me.shreyasr.ancients.packet.client.handler.ClientChatMessageHandler;
import me.shreyasr.ancients.packet.client.handler.ClientHitPacketHandler;
import me.shreyasr.ancients.packet.client.handler.ClientPlayerRemovePacketHandler;
import me.shreyasr.ancients.packet.client.handler.ClientPlayerUpdatePacketHandler;
import me.shreyasr.ancients.systems.network.NetworkUpdateSystem;
import me.shreyasr.ancients.systems.network.PacketHandleSystem;
import me.shreyasr.ancients.systems.network.PingUpdateSystem;
import me.shreyasr.ancients.systems.render.CameraUpdateSystem;
import me.shreyasr.ancients.systems.render.MainRenderSystem;
import me.shreyasr.ancients.systems.render.MiscRenderSystem;
import me.shreyasr.ancients.systems.render.NameRenderSystem;
import me.shreyasr.ancients.systems.render.SquareAnimationSystem;
import me.shreyasr.ancients.systems.render.UIRenderSystem;
import me.shreyasr.ancients.systems.render.util.PostRenderSystem;
import me.shreyasr.ancients.systems.render.util.PreBatchRenderSystem;
import me.shreyasr.ancients.systems.render.util.ShapeRenderSystem;
import me.shreyasr.ancients.systems.update.DashSystem;
import me.shreyasr.ancients.systems.update.KnockbackSystem;
import me.shreyasr.ancients.systems.update.PositionUpdateSystem;
import me.shreyasr.ancients.systems.update.WeaponUpdateSystem;
import me.shreyasr.ancients.systems.update.player.InputActionSystem;
import me.shreyasr.ancients.systems.update.player.MyPlayerMovementSystem;
import me.shreyasr.ancients.util.CustomUUID;
import me.shreyasr.ancients.util.EntityFactory;
import me.shreyasr.ancients.util.LinkedListQueuedListener;
import me.shreyasr.ancients.util.PacketListener;
import me.shreyasr.ancients.util.chat.ChatManager;

public class GameScreen extends ScreenAdapter {

    private AncientsGame game;

    public Client client;
    public PooledEngine engine;
    public CustomUUID playerUUID;
    public EntityListener entityListener;
    public ChatManager chatManager;

    private InputMultiplexer inputMultiplexer;
    private OrthographicCamera camera;
    ExtendViewport viewport;

    private String name;

    public GameScreen(AncientsGame game, Client client, String name) {
        this.game = game;
        this.client = client;
        this.name = name;
    }

    private boolean initialized = false;

    @Override
    public void show() {
        engine = new PooledEngine();
        EntityFactory entityFactory = new EntityFactory(640, 480);

        chatManager = new ChatManager();

         LinkedListQueuedListener queuedListener = new LinkedListQueuedListener(new PacketListener());
        client.addListener(queuedListener);

        camera = new OrthographicCamera(640, 480);
        viewport = new ExtendViewport(800, 600, 1280, 720, camera);

        playerUUID = CustomUUID.randomUUID();
        Log.info("My UUID: " + playerUUID);
        engine.addEntity(entityFactory.createPlayer(engine, playerUUID, name));

        int priority = 0;
        // @formatter:off
        engine.addSystem(new     PacketHandleSystem(++priority, queuedListener));
        engine.addSystem(new MyPlayerMovementSystem(++priority));
        engine.addSystem(new             DashSystem(++priority, engine, entityFactory, client));
        engine.addSystem(new        KnockbackSystem(++priority));
        engine.addSystem(new   PositionUpdateSystem(++priority));
        engine.addSystem(new      InputActionSystem(++priority, engine, entityFactory, client, viewport));
        engine.addSystem(new  SquareAnimationSystem(++priority));
        engine.addSystem(new     WeaponUpdateSystem(++priority, engine));

        engine.addSystem(new CameraUpdateSystem   (++priority, game.batch, camera, viewport));
        engine.addSystem(new TileRenderSystem     (++priority, game, camera));
        engine.addSystem(new PreBatchRenderSystem (++priority, game.batch));
        engine.addSystem(new    MainRenderSystem  (++priority, game));
        engine.addSystem(new    MiscRenderSystem  (++priority, game, client));
        engine.addSystem(new    NameRenderSystem  (++priority, game));
        engine.addSystem(new ShapeRenderSystem    (++priority, game.batch, game.shape, camera));
//        engine.addSystem(new    DebugRenderSystem (++priority, game));
        engine.addSystem(new PostRenderSystem     (++priority, game));
        engine.addSystem(new    UIRenderSystem    (++priority, game, engine, chatManager, client));

        engine.addSystem(new NetworkUpdateSystem(++priority, client));
        engine.addSystem(new    PingUpdateSystem(++priority, client));
        // @formatter:on

        entityListener = engine.getSystem(PacketHandleSystem.class);
        engine.getSystem(MyPlayerMovementSystem.class).setJoystickSource(engine.getSystem(UIRenderSystem.class));

        // @formatter:off
              ClientAttackPacket.setHandler(new ClientAttackPacketHandler(this));
                 ClientHitPacket.setHandler(new ClientHitPacketHandler(this));
        ClientPlayerRemovePacket.setHandler(new ClientPlayerRemovePacketHandler(this));
        ClientPlayerUpdatePacket.setHandler(new ClientPlayerUpdatePacketHandler(this));
         ClientChatMessagePacket.setHandler(new ClientChatMessageHandler(this));
        // @formatter:on

        InputActionSystem inputActionSystem = engine.getSystem(InputActionSystem.class);

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(engine.getSystem(UIRenderSystem.class).getStageInputProcessor());
        inputMultiplexer.addProcessor(engine.getSystem(MyPlayerMovementSystem.class));
        inputMultiplexer.addProcessor(inputActionSystem);
        if (!AncientsGame.TOUCH_CONTROLS) {
            inputMultiplexer.addProcessor(inputActionSystem.getTuningInputProcessor());
        }
        Gdx.input.setInputProcessor(inputMultiplexer);

        initialized = true;
    }

    @Override
    public void render(float delta) {
        if (!initialized) return;
        engine.update(Gdx.graphics.getRawDeltaTime() * 1000);
    }

    @Override
    public void resize(int width, int height) {
        engine.getSystem(UIRenderSystem.class).resize(width, height);
        viewport.update(width, height);
    }
}
