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
import me.shreyasr.ancients.components.player.Attack;
import me.shreyasr.ancients.components.player.AttackComponent;
import me.shreyasr.ancients.components.player.BasicWeaponAttack;
import me.shreyasr.ancients.components.player.MyPlayerComponent;
import me.shreyasr.ancients.components.weapon.HitboxGenerator;
import me.shreyasr.ancients.packet.server.ServerAttackPacket;
import me.shreyasr.ancients.util.Assets;
import me.shreyasr.ancients.util.EntityFactory;

public class InputActionSystem extends EntitySystem implements InputProcessor {

    private final PooledEngine engine;
    private final EntityFactory factory;
    private final Client client;
    private Entity player;
    private Attack[] possibleAttacks = new Attack[3];

    public InputActionSystem(int priority, PooledEngine engine, EntityFactory factory, Client client) {
        super(priority);
        this.engine = engine;
        this.factory = factory;
        this.client = client;
        possibleAttacks[0] = new BasicWeaponAttack(250, 30, 40, false, Assets.DAGGER_SLASH, 16, 48, 3,
                64, 64, 64, -1, HitboxGenerator.AttackType.STAB);
        possibleAttacks[1] = new BasicWeaponAttack(450, 50, 150, true, Assets.SWORD_SLASH, 8, 48, 3,
                64, 64, 64, 0, HitboxGenerator.AttackType.SLASH);
        possibleAttacks[2] = new BasicWeaponAttack(1000, 30, 200, false, Assets.SPEAR_STAB, 8, 80, 2,
                64, 64, 128, 0, HitboxGenerator.AttackType.STAB);
    }

    public void addedToEngine(Engine engine) {
        player = engine.getEntitiesFor(Family.all(MyPlayerComponent.class).get()).get(0);
        setRandomWeapon(player);
    }

    boolean attackButtonPressed = false;

    @Override
    public void update(float deltaTime) {
        PositionComponent pos = PositionComponent.MAPPER.get(player);

        boolean attemptAttack = attackButtonPressed && !KnockbackComponent.MAPPER.has(player);

        AttackComponent currentAttack = AttackComponent.MAPPER.get(player);

        if (currentAttack != null && currentAttack.attack != null) {
            Entity newWeapon = currentAttack.attack.update(engine, factory, player, pos, deltaTime, attemptAttack,
                    Gdx.input.getX() - pos.x, Gdx.graphics.getHeight() - Gdx.input.getY() - pos.y);

            if (newWeapon != null) {
                newWeapon.add(StartTimeComponent.create(
                        Time.getServerMillis(client) + client.getReturnTripTime() / 2 + ServerAttackPacket.ATTACK_DELAY_MS));
                engine.addEntity(newWeapon);

                Component[] newAttackComponents = newWeapon.getComponents().toArray(Component.class);
                client.sendUDP(ServerAttackPacket.create(newAttackComponents));
            }
        }

        BasicWeaponAttack attack = (BasicWeaponAttack) AttackComponent.MAPPER.get(player).attack;

        if(Gdx.input.isKeyPressed(Input.Keys.P)) attack.cooldownTime++;
        if(Gdx.input.isKeyPressed(Input.Keys.L)) attack.cooldownTime--;
        if(Gdx.input.isKeyPressed(Input.Keys.O)) attack.swingTime++;
        if(Gdx.input.isKeyPressed(Input.Keys.K)) attack.swingTime--;
        if(Gdx.input.isKeyPressed(Input.Keys.I)) attack.lastFrameHoldTime++;
        if(Gdx.input.isKeyPressed(Input.Keys.J)) attack.lastFrameHoldTime--;
    }

    private void setRandomWeapon(Entity player) {
        setWeapon(player, (int) (Math.random()*possibleAttacks.length));
    }

    private void setNextWeapon(Entity player) {
        Attack currentAttack = AttackComponent.MAPPER.get(player).attack;
        for (int i = 0; i < possibleAttacks.length; i++) {
            if (currentAttack == possibleAttacks[i]) {
                setWeapon(player, i+1);
            }
        }
    }

    private void setWeapon(Entity player, int idx) {
        idx = idx%possibleAttacks.length;
        AttackComponent.MAPPER.get(player).attack = possibleAttacks[idx];
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
        switch (keycode) {
            case Input.Keys.F:
                setNextWeapon(player);
                return true;
        }
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
