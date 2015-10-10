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

    public Entity createPlayer(PooledEngine engine, CustomUUID playerUUID) {
        Entity e = createDumbPlayer(engine, playerUUID);
        e.add(new MyPlayerComponent());
        return e;
    }

    public Entity createDumbPlayer(PooledEngine engine) {
        return createDumbPlayer(engine, CustomUUID.randomUUID());
    }

    private Entity createDumbPlayer(PooledEngine engine, CustomUUID playerUUID) {
        Entity e = engine.createEntity();

        e.add(LastUpdateTimeComponent.create(System.currentTimeMillis()));
        e.add(PositionComponent.create((float) Math.random()*worldWidth,
                (float) Math.random()*worldHeight));
        e.add(SpeedComponent.create(3));
        e.add(SquareDirectionComponent.create());
        e.add(SquareAnimationComponent.create(4, 16, 16, 166));
        e.add(TextureComponent.create(Assets.PLAYER.get()));
        e.add(TextureTransformComponent.create());
        e.add(UUIDComponent.create(playerUUID));
        e.add(VelocityComponent.create(0, 0));

        return e;
    }
}
