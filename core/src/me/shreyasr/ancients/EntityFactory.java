package me.shreyasr.ancients;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import me.shreyasr.ancients.components.*;

public class EntityFactory {

    public Entity createDumbPlayer(PooledEngine engine) {
        Entity e = engine.createEntity();

        e.add(PositionComponent.create(100, 100));
//        e.add(PositionComponent.create((float) Math.random()*1000, (float) Math.random()*1000));
        e.add(VelocityComponent.create(0, 0));
        e.add(TextureComponent.create(Assets.PLAYER));
        e.add(SquareDirectionComponent.create());
        e.add(SquareAnimationComponent.create(4, 16, 16, 166));
        e.add(TextureTransformComponent.create());

        return e;
    }
}
