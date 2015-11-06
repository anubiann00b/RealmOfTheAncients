package me.shreyasr.ancients.components.player.attack;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.components.TextureComponent;
import me.shreyasr.ancients.components.weapon.HitboxGenerator;
import me.shreyasr.ancients.components.weapon.WeaponAnimationComponent;
import me.shreyasr.ancients.util.Assets;
import me.shreyasr.ancients.util.EntityFactory;

public class BasicWeaponAttack extends Attack {

    public transient int timeSinceLastAttack = 0;
    public transient WeaponAnimationComponent weaponAnim = null;

    public int cooldownTime;
    public int swingTime;
    public int lastFrameHoldTime;
    public float knockbackMultiplier;

    private Assets asset;
    private boolean square;
    private int totalAnimationFrames;
    private int frameSize;
    private int numFrames;
    private int weaponWidth;
    private int weaponLength;
    private int stabSize;
    private int frameDirOffset;
    private HitboxGenerator.AttackType attackType;

    @Override
    public float getKnockbackMultiplier() {
        return knockbackMultiplier;
    }

    @Override
    public void cancel() {
        if (weaponAnim != null) weaponAnim.cancel();
    }

    public BasicWeaponAttack() { }

    public BasicWeaponAttack(int cooldownTime, int swingTime, int lastFrameHoldTime, float knockbackMultiplier,
                             boolean square, Assets asset, int totalAnimationFrames, int frameSize,
                             int numFrames, int weaponWidth, int weaponLength, int stabSize, int frameDirOffset,
                             HitboxGenerator.AttackType attackType) {
        this.cooldownTime = cooldownTime;
        this.swingTime = swingTime;
        this.lastFrameHoldTime = lastFrameHoldTime;
        this.knockbackMultiplier = knockbackMultiplier;

        this.square = square;
        this.asset = asset;
        this.totalAnimationFrames = totalAnimationFrames;
        this.frameSize = frameSize;
        this.numFrames = numFrames;
        this.weaponWidth = weaponWidth;
        this.weaponLength = weaponLength;
        this.stabSize = stabSize;
        this.frameDirOffset = frameDirOffset;
        this.attackType = attackType;
    }

    @Override
    public Entity update(PooledEngine engine, EntityFactory entityFactory, Entity player, PositionComponent pos,
                         float deltaTime, boolean attack, float mouseDx, float mouseDy) {
        timeSinceLastAttack += deltaTime;
        if (attack && timeSinceLastAttack >= cooldownTime) {
            timeSinceLastAttack = 0;
            int dir = getAttackDir(mouseDx, mouseDy, square);
            return getAttackEntity(engine, entityFactory, player, pos, dir);
        }
        return null;
    }

    private Entity getAttackEntity(PooledEngine engine, EntityFactory entityFactory, Entity player,
                            PositionComponent pos, int dir) {
        WeaponAnimationComponent weaponAnim
                = WeaponAnimationComponent.create(engine, totalAnimationFrames, frameSize, dir, numFrames,
                swingTime, lastFrameHoldTime, weaponWidth, weaponLength, stabSize, frameDirOffset, attackType);
        this.weaponAnim = weaponAnim;
        return entityFactory.createBaseWeapon(engine, player, pos.x, pos.y)
                .add(TextureComponent.create(engine, asset.getFile()))
                .add(weaponAnim);
    }
}
