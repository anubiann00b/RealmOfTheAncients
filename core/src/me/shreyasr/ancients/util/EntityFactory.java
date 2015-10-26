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
import me.shreyasr.ancients.components.player.MyPlayerComponent;
import me.shreyasr.ancients.components.type.TypeComponent;
import me.shreyasr.ancients.components.weapon.OwnerUUIDComponent;
import me.shreyasr.ancients.components.weapon.WeaponAnimationComponent;
import me.shreyasr.ancients.util.Assets;
import me.shreyasr.ancients.util.CustomUUID;

public class EntityFactory {

    private final int worldWidth;
    private final int worldHeight;

    public EntityFactory(int worldWidth, int worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    public Entity createPlayer(PooledEngine engine, CustomUUID playerUUID) {
        Entity e = createDumbPlayer(engine, playerUUID);
        e.add(MyPlayerComponent.create(engine));
        e.add(StatsComponent.create(engine));
        e.add(NameComponent.create(engine, playerUUID.toNameString()));
        return e;
    }

    public Entity createSwordSlash(PooledEngine engine, Entity owner, float x, float y, int startFrame) {
        Entity e = engine.createEntity();

        e.add(TypeComponent.create(engine, TypeComponent.Weapon.class));

        e.add(HitboxComponent.create(engine));
        e.add(PositionComponent.create(engine, x, y));
        e.add(WeaponAnimationComponent.create(engine, 8, 48, startFrame, 3, 50, 150));
        e.add(TextureComponent.create(engine, Assets.SWORD_SLASH.get()));
        e.add(TextureTransformComponent.create(engine));
        e.add(UUIDComponent.create(engine, CustomUUID.randomUUID()));

        e.add(OwnerUUIDComponent.create(engine, owner.getId()));

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
}
