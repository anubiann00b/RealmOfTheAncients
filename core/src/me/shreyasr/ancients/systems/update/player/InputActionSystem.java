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
import me.shreyasr.ancients.components.player.attack.Attack;
import me.shreyasr.ancients.components.player.attack.AttackComponent;
import me.shreyasr.ancients.components.player.attack.BasicWeaponAttack;
import me.shreyasr.ancients.components.player.dash.DashComponent;
import me.shreyasr.ancients.components.player.dash.FaceDashBehavior;
import me.shreyasr.ancients.components.player.dash.SpinDashBehavior;
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
    private DashComponent[] possibleDashes = new DashComponent[3];

    Attack spinToWin;
    Attack spearLunge;

    public InputActionSystem(int priority, PooledEngine engine, EntityFactory factory, Client client) {
        super(priority);
        this.engine = engine;
        this.factory = factory;
        this.client = client;
        possibleAttacks[0] = new BasicWeaponAttack(250, 30, 40, 0.25f, false, Assets.DAGGER_SLASH, 16, 48, 3,
                64, 64, 64, -1, HitboxGenerator.AttackType.STAB);
        possibleAttacks[1] = new BasicWeaponAttack(800, 50, 150, 1.25f, true, Assets.SWORD_SLASH, 8, 48, 3,
                64, 64, 64, 0, HitboxGenerator.AttackType.SLASH);
        possibleAttacks[2] = new BasicWeaponAttack(1200, 30, 100, 1.1f, false, Assets.SPEAR_STAB, 8, 80, 2,
                64, 64, 128, 0, HitboxGenerator.AttackType.STAB);

        spinToWin = new BasicWeaponAttack(500, 56, 56, 1.5f, false, Assets.SWORD_SLASH, 8, 48, 9,
                64, 64, 64, 0, HitboxGenerator.AttackType.SLASH);
        spearLunge = new BasicWeaponAttack(300, 240, 160, 0.1f, false, Assets.SPEAR_STAB, 8, 80, 2,
                64, 64, 128, 0, HitboxGenerator.AttackType.STAB);
        //                                                                           cool  dur  dist stun
        possibleDashes[0] = DashComponent.create(null, null,                         1500,  -1, 1000, 300, false);
        possibleDashes[1] = DashComponent.create(new SpinDashBehavior(), spinToWin,  2500, 400, 1200, 500, false);
        possibleDashes[2] = DashComponent.create(new FaceDashBehavior(), spearLunge, 2000, 200, 1400, 800, true);
    }

    public void addedToEngine(Engine engine) {
        player = engine.getEntitiesFor(Family.all(MyPlayerComponent.class).get()).get(0);
        setRandomWeapon(player);
    }

    boolean attackButtonPressed = false;
    boolean dashButtonPressed = false;

    @Override
    public void update(float deltaTime) {
        AttackComponent currentAttack = AttackComponent.MAPPER.get(player);
        PositionComponent pos = PositionComponent.MAPPER.get(player);
        DashComponent dash = DashComponent.MAPPER.get(player);
        float dx = Gdx.input.getX() - pos.x;
        float dy = Gdx.graphics.getHeight() - Gdx.input.getY() - pos.y;

        boolean inKnockback = KnockbackComponent.MAPPER.has(player);

        if (dashButtonPressed && dash.isReady() && !inKnockback) {
            dash.start(dx, dy, Time.getServerMillis() + client.getReturnTripTime() / 2 + ServerAttackPacket.DASH_DELAY_MS);
        }

        boolean attemptAttack = attackButtonPressed && !dash.isStunned() && !inKnockback;

        if (currentAttack != null && currentAttack.attack != null) {
            Entity newWeapon = currentAttack.attack.update(engine, factory, player, pos,
                    deltaTime, attemptAttack, dx, dy);

            if (newWeapon != null) {
                newWeapon.add(StartTimeComponent.create(
                        Time.getServerMillis() + client.getReturnTripTime() / 2 + ServerAttackPacket.ATTACK_DELAY_MS));
                engine.addEntity(newWeapon);

                Component[] newAttackComponents = newWeapon.getComponents().toArray(Component.class);
                client.sendUDP(ServerAttackPacket.create(newAttackComponents));
            }

            BasicWeaponAttack attack = (BasicWeaponAttack) currentAttack.attack;

            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT_BRACKET)) attack.cooldownTime++;
            if(Gdx.input.isKeyPressed(Input.Keys.APOSTROPHE)) attack.cooldownTime--;
            if(Gdx.input.isKeyPressed(Input.Keys.LEFT_BRACKET)) attack.swingTime++;
            if(Gdx.input.isKeyPressed(Input.Keys.SEMICOLON)) attack.swingTime--;
            if(Gdx.input.isKeyPressed(Input.Keys.P)) attack.lastFrameHoldTime++;
            if(Gdx.input.isKeyPressed(Input.Keys.L)) attack.lastFrameHoldTime--;
            if(Gdx.input.isKeyPressed(Input.Keys.O)) attack.knockbackMultiplier+=0.02;
            if(Gdx.input.isKeyPressed(Input.Keys.K)) attack.knockbackMultiplier-=0.02;
        }

        BasicWeaponAttack dashAttack = null;
        if (dash.attack instanceof BasicWeaponAttack) {
            dashAttack = (BasicWeaponAttack) dash.attack;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.I)) dash.duration+=5;
        if(Gdx.input.isKeyPressed(Input.Keys.J)) dash.duration-=5;
        if(Gdx.input.isKeyPressed(Input.Keys.U)) dash.distance+=5;
        if(Gdx.input.isKeyPressed(Input.Keys.H)) dash.distance-=5;
        if(Gdx.input.isKeyPressed(Input.Keys.Y)) dash.cooldown+=5;
        if(Gdx.input.isKeyPressed(Input.Keys.G)) dash.cooldown-=5;
        if(Gdx.input.isKeyPressed(Input.Keys.T)) dash.stunTime+=2;
        if(Gdx.input.isKeyPressed(Input.Keys.F)) dash.stunTime-=2;
        if(dashAttack!=null) {
            dashAttack.cooldownTime = dash.duration;
            if (dashAttack == spinToWin) {
                dashAttack.swingTime = dash.duration/9;
                dashAttack.lastFrameHoldTime = dash.duration/9;
            }
            if (dashAttack == spearLunge) {
                dashAttack.swingTime = dash.duration - 60;
            }
        }
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
        player.add(possibleDashes[idx]);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        switch (button) {
            case Input.Buttons.LEFT: attackButtonPressed = true; return true;
            case Input.Buttons.RIGHT: dashButtonPressed = true; return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        switch (button) {
            case Input.Buttons.LEFT: attackButtonPressed = false; return true;
            case Input.Buttons.RIGHT: dashButtonPressed = false; return true;
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.R:
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
