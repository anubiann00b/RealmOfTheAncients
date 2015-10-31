package me.shreyasr.ancients.components.player;

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

    private boolean square;
    public int cooldownTime;

    public Assets asset;
    protected int totalAnimationFrames;
    protected int frameSize;
    protected int numFrames;
    public int swingTime;
    public int lastFrameHoldTime;
    protected int weapWidth;
    protected int weapLength;
    protected int stabSize;
    protected int frameDirOffset;
    protected HitboxGenerator.AttackType attackType;

    public BasicWeaponAttack() { }

    public BasicWeaponAttack(int cooldownTime, int swingTime, int lastFrameHoldTime, boolean square,
                             Assets asset, int totalAnimationFrames, int frameSize, int numFrames,
                             int weapWidth, int weapLength, int stabSize, int frameDirOffset,
                             HitboxGenerator.AttackType attackType) {
        this.cooldownTime = cooldownTime;
        this.swingTime = swingTime;
        this.lastFrameHoldTime = lastFrameHoldTime;
        this.square = square;
        this.asset = asset;
        this.totalAnimationFrames = totalAnimationFrames;
        this.frameSize = frameSize;
        this.numFrames = numFrames;
        this.weapWidth = weapWidth;
        this.weapLength = weapLength;
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
        return entityFactory.createBaseWeapon(engine, player, pos.x, pos.y)
                .add(TextureComponent.create(engine, asset.get()))
                .add(WeaponAnimationComponent.create(engine, totalAnimationFrames, frameSize, dir, numFrames,
                        swingTime, lastFrameHoldTime, weapWidth, weapLength, stabSize, frameDirOffset, attackType));
    }
}
