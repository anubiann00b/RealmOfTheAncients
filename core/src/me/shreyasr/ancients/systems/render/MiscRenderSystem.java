package me.shreyasr.ancients.systems.render;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.esotericsoftware.kryonet.Client;

import java.text.DecimalFormat;

import me.shreyasr.ancients.AncientsGame;
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

    }

    private DecimalFormat knockbackFormat = new DecimalFormat("#0.00");
}
