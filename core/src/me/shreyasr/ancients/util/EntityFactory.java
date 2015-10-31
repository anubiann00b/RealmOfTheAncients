package me.shreyasr.ancients.util;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import me.shreyasr.ancients.components.HitboxComponent;
import me.shreyasr.ancients.components.NameComponent;
import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.components.SpeedComponent;
import me.shreyasr.ancients.components.SquareAnimationComponent;
import me.shreyasr.ancients.components.SquareDirectionComponent;
import me.shreyasr.ancients.components.StatsComponent;
import me.shreyasr.ancients.components.TextureComponent;
import me.shreyasr.ancients.components.TextureTransformComponent;
import me.shreyasr.ancients.components.UUIDComponent;
import me.shreyasr.ancients.components.VelocityComponent;
import me.shreyasr.ancients.components.player.AttackComponent;
import me.shreyasr.ancients.components.player.MyPlayerComponent;
import me.shreyasr.ancients.components.type.TypeComponent;
import me.shreyasr.ancients.components.weapon.OwnerUUIDComponent;

public class EntityFactory {

    private final int worldWidth;
    private final int worldHeight;

    public EntityFactory(int worldWidth, int worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    public Entity createPlayer(PooledEngine engine, CustomUUID playerUUID, String name) {
        Entity e = createDumbPlayer(engine, playerUUID);
        e.add(MyPlayerComponent.create(engine));
        e.add(StatsComponent.create(engine));
        e.add(NameComponent.create(engine, name));
        e.add(AttackComponent.create(null));
        return e;
    }

    private Entity createDumbPlayer(PooledEngine engine, CustomUUID playerUUID) {
        Entity e = engine.createEntity();

        e.add(TypeComponent.create(engine, TypeComponent.Player.class));

        e.add(HitboxComponent.create(engine));
        e.add(PositionComponent.create(engine, (float) Math.random()*worldWidth,
                (float) Math.random()*worldHeight));
        e.add(SpeedComponent.create(engine, 3));
        e.add(SquareDirectionComponent.create(engine));
        e.add(SquareAnimationComponent.create(engine, 4, 16, 16, 166));
        e.add(TextureComponent.create(engine, Assets.PLAYER.get()));
        e.add(TextureTransformComponent.create(engine));
        e.add(UUIDComponent.create(engine, playerUUID));
        e.add(VelocityComponent.create(engine, 0, 0));

        return e;
    }

    public Entity createBaseWeapon(PooledEngine engine, Entity owner, float x, float y) {
        Entity e = engine.createEntity();

        e.add(TypeComponent.create(engine, TypeComponent.Weapon.class));

        e.add(HitboxComponent.create(engine));
        e.add(PositionComponent.create(engine, x, y));
        e.add(TextureTransformComponent.create(engine));
        e.add(UUIDComponent.create(engine, CustomUUID.randomUUID()));
        e.add(OwnerUUIDComponent.create(engine, owner.getId()));

        return e;
    }
}
