package me.shreyasr.ancients;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import me.shreyasr.ancients.components.*;

public class EntityFactory {

    private final int worldWidth;
    private final int worldHeight;

    public EntityFactory(int worldWidth, int worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    public Entity createDumbPlayer(PooledEngine engine) {
        Entity e = engine.createEntity();

        e.add(PositionComponent.create((float) Math.random()*worldWidth,
                                       (float) Math.random()*worldHeight));
        e.add(VelocityComponent.create(0, 0));
        e.add(SpeedComponent.create(3));
        e.add(TextureComponent.create(Assets.PLAYER.get()));
        e.add(SquareDirectionComponent.create());
        e.add(SquareAnimationComponent.create(4, 16, 16, 166));
        e.add(TextureTransformComponent.create());

        return e;
    }

    public Entity createPlayer(PooledEngine engine) {
        Entity e = createDumbPlayer(engine);
        e.add(new PlayerComponent());
        return e;
    }
}
