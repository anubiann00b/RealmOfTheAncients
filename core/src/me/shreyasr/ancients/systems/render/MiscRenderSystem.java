package me.shreyasr.ancients.systems.render;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;

import me.shreyasr.ancients.AncientsGame;
import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.components.player.MyPlayerComponent;

public class MiscRenderSystem extends EntitySystem {

    private final AncientsGame game;
    private final Client client;
    private Entity player;

    public MiscRenderSystem(int priority, AncientsGame game, Client client) {
        super(priority);
        this.game = game;
        this.client = client;
    }

    public void addedToEngine(Engine engine) {
        player = engine.getEntitiesFor(Family.all(MyPlayerComponent.class).get()).get(0);
    }

    public void update(float deltaTime) {
        game.font.draw(game.batch, String.valueOf((int)(1000/deltaTime)), 16, Gdx.graphics.getHeight() - 16);
        game.font.draw(game.batch, String.valueOf(client.getReturnTripTime()), 16, Gdx.graphics.getHeight() - 32);
    }
}
