package me.shreyasr.ancients.screen;

import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.esotericsoftware.kryonet.Client;

import me.shreyasr.ancients.AncientsGame;
import me.shreyasr.ancients.packet.client.ClientAttackPacket;
import me.shreyasr.ancients.packet.client.ClientHitPacket;
import me.shreyasr.ancients.packet.client.ClientPlayerRemovePacket;
import me.shreyasr.ancients.packet.client.ClientPlayerUpdatePacket;
import me.shreyasr.ancients.packet.client.handler.ClientAttackPacketHandler;
import me.shreyasr.ancients.packet.client.handler.ClientHitPacketHandler;
import me.shreyasr.ancients.packet.client.handler.ClientPlayerRemovePacketHandler;
import me.shreyasr.ancients.packet.client.handler.ClientPlayerUpdatePacketHandler;
import me.shreyasr.ancients.systems.network.NetworkUpdateSystem;
import me.shreyasr.ancients.systems.network.PacketHandleSystem;
import me.shreyasr.ancients.systems.network.PingUpdateSystem;
import me.shreyasr.ancients.systems.render.DebugRenderSystem;
import me.shreyasr.ancients.systems.render.MainRenderSystem;
import me.shreyasr.ancients.systems.render.MiscRenderSystem;
import me.shreyasr.ancients.systems.render.NameRenderSystem;
import me.shreyasr.ancients.systems.render.ScoreboardRenderSystem;
import me.shreyasr.ancients.systems.render.SquareAnimationSystem;
import me.shreyasr.ancients.systems.render.util.PostRenderSystem;
import me.shreyasr.ancients.systems.render.util.PreBatchRenderSystem;
import me.shreyasr.ancients.systems.render.util.ShapeRenderSystem;
import me.shreyasr.ancients.systems.update.KnockbackSystem;
import me.shreyasr.ancients.systems.update.PositionUpdateSystem;
import me.shreyasr.ancients.systems.update.WeaponUpdateSystem;
import me.shreyasr.ancients.systems.update.player.InputActionSystem;
import me.shreyasr.ancients.systems.update.player.MyPlayerMovementSystem;
import me.shreyasr.ancients.util.CustomUUID;
import me.shreyasr.ancients.util.EntityFactory;
import me.shreyasr.ancients.util.LinkedListQueuedListener;
import me.shreyasr.ancients.util.PacketListener;

public class GameScreen extends ScreenAdapter {

    private AncientsGame game;

    public Client client;
    public PooledEngine engine;
    public CustomUUID playerUUID;
    public EntityListener entityListener;

    public GameScreen(AncientsGame game, Client client) {
        this.game = game;
        this.client = client;
    }


    private boolean initialized = false;

    @Override
    public void show() {
        EntityFactory entityFactory = new EntityFactory(640, 480);

        // LinkedListQueuedListener queuedListener = new LagLinkedListQueuedListener(100, 0);
        LinkedListQueuedListener queuedListener = new LinkedListQueuedListener(new PacketListener());
        client.addListener(queuedListener);

        engine = new PooledEngine();

        playerUUID = CustomUUID.randomUUID();
        System.out.println("My UUID: " + playerUUID);

        engine.addEntity(entityFactory.createPlayer(engine, playerUUID));

        int priority = 0;
        // @formatter:off
        engine.addSystem(new       PacketHandleSystem(++priority, queuedListener));
        engine.addSystem(new   MyPlayerMovementSystem(++priority));
        engine.addSystem(new          KnockbackSystem(++priority));
        engine.addSystem(new     PositionUpdateSystem(++priority));
        engine.addSystem(new        InputActionSystem(++priority, engine, entityFactory, client));
        engine.addSystem(new    SquareAnimationSystem(++priority));
        engine.addSystem(new       WeaponUpdateSystem(++priority, engine));

        engine.addSystem(new PreBatchRenderSystem       (++priority, game.batch));
        engine.addSystem(new MainRenderSystem(++priority, game));
        engine.addSystem(new MiscRenderSystem       (++priority, game, client));
        engine.addSystem(new NameRenderSystem       (++priority, game));
        engine.addSystem(new ShapeRenderSystem          (++priority, game.batch, game.shape));
        engine.addSystem(new DebugRenderSystem      (++priority, game));
        engine.addSystem(new ScoreboardRenderSystem (++priority, game));
        engine.addSystem(new PostRenderSystem           (++priority, game));

        engine.addSystem(new      NetworkUpdateSystem(++priority, client));
        engine.addSystem(new         PingUpdateSystem(++priority, client));
        // @formatter:on

        entityListener = engine.getSystem(PacketHandleSystem.class);

        // @formatter:off
              ClientAttackPacket.setHandler(new ClientAttackPacketHandler(this));
                 ClientHitPacket.setHandler(new ClientHitPacketHandler(this));
        ClientPlayerRemovePacket.setHandler(new ClientPlayerRemovePacketHandler(this));
        ClientPlayerUpdatePacket.setHandler(new ClientPlayerUpdatePacketHandler(this));
        // @formatter:on

        initialized = true;
    }

    @Override
    public void render(float delta) {
        if (!initialized) return;
        engine.update(Gdx.graphics.getRawDeltaTime() * 1000);
    }
}
