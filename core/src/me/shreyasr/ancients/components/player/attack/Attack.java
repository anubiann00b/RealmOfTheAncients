package me.shreyasr.ancients.components.player.attack;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.util.EntityFactory;

public abstract class Attack {

    public float getKnockbackMultiplier() { return 1; }

    public abstract void cancel();

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
            boolean pdx = dx >= 0;
            boolean pdy = dy >= 0;
            if (pdx && pdy) return 0;
            else if (!pdx && pdy) return 2;
            else if (!pdx) return 4;
            else return 6;
        } else {
            if (Math.abs(dx) >= Math.abs(dy)) {
                if (dx >= 0) return 0;
                else return 4;
            } else {
                if (dy >= 0) return 2;
                else return 6;
            }
        }
    }
}
