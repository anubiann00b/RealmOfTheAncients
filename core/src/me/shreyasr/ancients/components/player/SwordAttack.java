package me.shreyasr.ancients.components.player;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.util.EntityFactory;

public class SwordAttack extends TimedAttack {

    public SwordAttack() {

    }

    public SwordAttack(EntityFactory entityFactory, int cooldownTime) {
        super(cooldownTime, true);
    }

    @Override
    protected Entity getAttackEntity(PooledEngine engine, EntityFactory entityFactory, Entity player,
                                     PositionComponent pos, int dir) {
        return entityFactory.createSwordSlash(engine, player, pos.x, pos.y, dir);
    }
}
