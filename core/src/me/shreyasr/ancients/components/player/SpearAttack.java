package me.shreyasr.ancients.components.player;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.util.EntityFactory;

public class SpearAttack extends TimedAttack {

    public SpearAttack() { }

    public SpearAttack(EntityFactory entityFactory, int cooldownTime) {
        super(cooldownTime, false);
    }

    @Override
    protected Entity getAttackEntity(PooledEngine engine, EntityFactory entityFactory, Entity player,
                                     PositionComponent pos, int dir) {
        return entityFactory.createSpearStab(engine, player, pos.x, pos.y, dir);
    }
}
