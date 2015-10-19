package me.shreyasr.ancients.systems.render;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;

import me.shreyasr.ancients.AncientsGame;

public class MiscRenderSystem extends EntitySystem {

    private final AncientsGame game;
    private final Client client;

    public MiscRenderSystem(int priority, AncientsGame game, Client client) {
        super(priority);
        this.game = game;
        this.client = client;
    }

    public void update(float deltaTime) {
        game.font.draw(game.batch, String.valueOf((int)(1000/deltaTime)), 16, Gdx.graphics.getHeight() - 16);
        game.font.draw(game.batch, String.valueOf(client.getReturnTripTime()), 16, Gdx.graphics.getHeight() - 32);
    }
}
