package me.shreyasr.ancients.components.player.dash;

import com.badlogic.ashley.core.Entity;

public abstract class DashBehavior {

    public abstract void update(Entity entity, double completion, float dx, float dy);
}
