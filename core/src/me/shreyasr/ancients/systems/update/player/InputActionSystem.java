package me.shreyasr.ancients.systems.update.player;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Time;

import me.shreyasr.ancients.components.KnockbackComponent;
import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.components.StartTimeComponent;
import me.shreyasr.ancients.components.player.MyPlayerComponent;
import me.shreyasr.ancients.packet.server.ServerAttackPacket;
import me.shreyasr.ancients.util.EntityFactory;

public class InputActionSystem extends EntitySystem implements InputProcessor {

    private final PooledEngine engine;
    private final EntityFactory factory;
    private final Client client;
    private Entity player;

    public InputActionSystem(int priority, PooledEngine engine, EntityFactory factory, Client client) {
        super(priority);
        this.engine = engine;
        this.factory = factory;
        this.client = client;
    }

    public void addedToEngine(Engine engine) {
        player = engine.getEntitiesFor(Family.all(MyPlayerComponent.class).get()).get(0);
    }

    int cooldown = 0;
    boolean attackButtonPressed = false;
    boolean dagger = true;

    @Override
    public void update(float deltaTime) {
        PositionComponent pos = PositionComponent.MAPPER.get(player);

        cooldown += deltaTime;

        if (KnockbackComponent.MAPPER.has(player)) return;

        if (attackButtonPressed && cooldown > (dagger ? 350 : 450)) {
            cooldown = 0;
            int dir = getAttackDir(pos, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), !dagger);
            Entity newAttack = factory.createSpearStab(engine, player, pos.x, pos.y, dir);
            newAttack.add(StartTimeComponent.create(
                    Time.getServerMillis(client) + client.getReturnTripTime() / 2 + ServerAttackPacket.ATTACK_DELAY_MS));
            engine.addEntity(newAttack);

            Component[] newAttackComponents = newAttack.getComponents().toArray(Component.class);
            client.sendUDP(ServerAttackPacket.create(newAttackComponents));
        }
    }

    private int getAttackDir(PositionComponent pos, int x, int y, boolean square) {
        float dx = x - pos.x;
        float dy = y - pos.y;

        if (square) {
            if (dx >= 0 && dy >= 0) return 0;
            if (dx < 0 && dy >= 0) return 2;
            if (dx < 0 && dy < 0) return 4;
            if (dx >= 0 && dy < 0) return 6;
        } else {
            if (Math.abs(dx) >= Math.abs(dy)) {
                if (dx >= 0) return 0;
                if (dx < 0) return 4;
            } else {
                if (dy >= 0) return 2;
                if (dy < 0) return 6;
            }
        }
        return -1; // impossible
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            attackButtonPressed = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            attackButtonPressed = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
