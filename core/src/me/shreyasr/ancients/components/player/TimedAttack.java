package me.shreyasr.ancients.components.player;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.util.EntityFactory;

public abstract class TimedAttack extends Attack {

    private boolean square;
    protected int cooldownTime;
    protected transient int timeSinceLastAttack = 0;

    public void setCooldownTime(int cooldownTime) { this.cooldownTime = cooldownTime; }

    public TimedAttack() { }

    public TimedAttack(int cooldownTime, boolean square) {
        this.cooldownTime = cooldownTime;
        this.square = square;
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

    protected abstract Entity getAttackEntity(PooledEngine engine, EntityFactory entityFactory,
                                              Entity player, PositionComponent pos, int dir);
}
