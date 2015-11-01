package me.shreyasr.ancients.systems.render;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.esotericsoftware.kryonet.Client;

import java.text.DecimalFormat;

import me.shreyasr.ancients.AncientsGame;
import me.shreyasr.ancients.components.player.MyPlayerComponent;
import me.shreyasr.ancients.components.player.attack.AttackComponent;
import me.shreyasr.ancients.components.player.attack.BasicWeaponAttack;
import me.shreyasr.ancients.components.player.dash.DashComponent;

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
        game.font.setColor(Color.BLACK);
        game.font.draw(game.batch, String.valueOf((int) (1000 / deltaTime)), 16, Gdx.graphics.getHeight() - 16);
        game.font.draw(game.batch, String.valueOf(client.getReturnTripTime()), 16, Gdx.graphics.getHeight() - 32);

        AttackComponent attackComponent = AttackComponent.MAPPER.get(player);
        DashComponent dash = DashComponent.MAPPER.get(player);
        if (attackComponent != null && attackComponent.attack instanceof BasicWeaponAttack) {
            BasicWeaponAttack atk = (BasicWeaponAttack) attackComponent.attack;
            game.font.draw(game.batch, "Cooldown", 16, Gdx.graphics.getHeight() - 64);
            game.font.draw(game.batch, String.valueOf(atk.cooldownTime), 96, Gdx.graphics.getHeight() - 64);
            game.font.draw(game.batch, "Swing", 16, Gdx.graphics.getHeight() - 80);
            game.font.draw(game.batch, String.valueOf(atk.swingTime), 96, Gdx.graphics.getHeight() - 80);
            game.font.draw(game.batch, "Hold", 16, Gdx.graphics.getHeight() - 96);
            game.font.draw(game.batch, String.valueOf(atk.lastFrameHoldTime), 96, Gdx.graphics.getHeight() - 96);
            game.font.draw(game.batch, "Knockback", 16, Gdx.graphics.getHeight() - 112);
            game.font.draw(game.batch, knockbackFormat.format(atk.knockbackMultiplier), 96, Gdx.graphics.getHeight() - 112);

            game.font.draw(game.batch, "Duration", 16, Gdx.graphics.getHeight() - 144);
            game.font.draw(game.batch, String.valueOf(dash.duration), 96, Gdx.graphics.getHeight() - 144);
            game.font.draw(game.batch, "Distance", 16, Gdx.graphics.getHeight() - 160);
            game.font.draw(game.batch, String.valueOf(dash.distance), 96, Gdx.graphics.getHeight() - 160);
            game.font.draw(game.batch, "Cooldown", 16, Gdx.graphics.getHeight() - 176);
            game.font.draw(game.batch, String.valueOf(dash.cooldown), 96, Gdx.graphics.getHeight() - 176);
        }
    }

    private DecimalFormat knockbackFormat = new DecimalFormat("#0.00");
}
