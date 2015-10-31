package me.shreyasr.ancients.components.player;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.util.EntityFactory;

public abstract class Attack {

    /**
     * Updates this attack, returning a new Entity of type Weapon if necessary.
     *
     * @param engine
     * @param entityFactory
     * @param player
     * @param pos
     * @param deltaTime
     * @param attack
     * @param mouseDx
     * @param mouseDy
     * @return
     */
    public abstract Entity update(PooledEngine engine, EntityFactory entityFactory, Entity player, PositionComponent pos,
                                  float deltaTime, boolean attack, float mouseDx, float mouseDy);

    /**
     * Utility method to get a dir from relative (mouse) coordinates
     * @param dx
     * @param dy
     * @param square
     * @return
     */
    protected int getAttackDir(float dx, float dy, boolean square) {
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
}
