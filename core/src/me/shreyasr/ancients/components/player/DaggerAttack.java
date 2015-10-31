package me.shreyasr.ancients.components.player;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.util.EntityFactory;

public class DaggerAttack extends TimedAttack {

    public DaggerAttack() { }

    public DaggerAttack(EntityFactory entityFactory, int cooldownTime) {
        super(cooldownTime, false);
    }

    @Override
    protected Entity getAttackEntity(PooledEngine engine, EntityFactory entityFactory, Entity player,
                                     PositionComponent pos, int dir) {
        return entityFactory.createDaggerSlash(engine, player, pos.x, pos.y, dir);
    }
}
