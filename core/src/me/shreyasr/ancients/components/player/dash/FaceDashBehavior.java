package me.shreyasr.ancients.components.player.dash;

import com.badlogic.ashley.core.Entity;

import me.shreyasr.ancients.components.SquareDirectionComponent;

public class FaceDashBehavior extends DashBehavior {

    @Override
    public void update(Entity entity, double completion, float dx, float dy) {
        SquareDirectionComponent dir = SquareDirectionComponent.MAPPER.get(entity);
        if (dir == null) return;
        dir.dir = SquareDirectionComponent.Direction.getFromPos(dx, dy);
    }
}
