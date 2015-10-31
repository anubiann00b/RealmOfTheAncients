package me.shreyasr.ancients.components.player;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.util.EntityFactory;

public abstract class Attack {

    /**
     * Updates this attack, returning a new Entity of type Weapon if necessary.
     *
     * @param engine Game engine, mostly for creating the new weapon.
     * @param entityFactory Factory to create the new weapon.
     * @param player Play entity.
     * @param pos Position of the player entity.
     * @param deltaTime Time between frames.
     * @param attack Whether or not to attack.
     * @param mouseDx X difference from the player position to the mouse position, direction of attack.
     * @param mouseDy Y difference from the player position to the mouse position, direction of attack.
     * @return An Entity representing a new attack, or null if not attacking.
     */
    public abstract Entity update(PooledEngine engine, EntityFactory entityFactory, Entity player, PositionComponent pos,
                                  float deltaTime, boolean attack, float mouseDx, float mouseDy);

    /**
     * Utility method to get a dir from relative (mouse) coordinates.
     *
     * @param dx X component of direction, usually mouseDx from update().
     * @param dy Y component of direction, usually mouseDx from update().
     * @param square Whether to use square or diagonal direction.
     * @return An int representing the direction, starting at 0 to the right
     *         and going counterclockwise in 45 degree increments.
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
