package me.shreyasr.ancients.components.player.dash;

import com.badlogic.ashley.core.Entity;

import me.shreyasr.ancients.components.SquareDirectionComponent;

public class SpinDashBehavior extends DashBehavior {

    @Override
    public void update(Entity entity, double completion, float dx, float dy) {
        completion += 0.05;
        double dir = completion * 4
                + SquareDirectionComponent.Direction.getFromPos(dx, dy).index;
        SquareDirectionComponent dirComponent = SquareDirectionComponent.MAPPER.get(entity);
        if (dirComponent == null) return;
        dirComponent.dir = SquareDirectionComponent.Direction.values()[(int)(dir)%4];
    }
}
