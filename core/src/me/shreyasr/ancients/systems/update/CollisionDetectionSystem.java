package me.shreyasr.ancients.systems.update;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import me.shreyasr.ancients.CustomUUID;
import me.shreyasr.ancients.components.HitboxComponent;
import me.shreyasr.ancients.components.UUIDComponent;
import me.shreyasr.ancients.components.type.TypeComponent;
import me.shreyasr.ancients.components.weapon.OwnerUUIDComponent;
import me.shreyasr.ancients.components.weapon.WeaponAnimationComponent;

public class CollisionDetectionSystem extends EntitySystem {

    public CollisionDetectionSystem(int priority) {
        super(priority);
    }

    private ImmutableArray<Entity> weapons;
    private ImmutableArray<Entity> entities;

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(HitboxComponent.class).exclude(TypeComponent.Weapon.class).get());
        weapons = engine.getEntitiesFor(Family.all(TypeComponent.Weapon.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity w : weapons) {
            WeaponAnimationComponent weaponAnim = WeaponAnimationComponent.MAPPER.get(w);
            CustomUUID ownerUUID = OwnerUUIDComponent.MAPPER.get(w).ownerUUID;

            if (weaponAnim.isDone() || weaponAnim.timeSinceAnimStart < 0) continue;
            HitboxComponent weaponHitbox = HitboxComponent.MAPPER.get(w);

            if (!weaponHitbox.active) continue;

            for (Entity e : entities) {
                if (UUIDComponent.MAPPER.get(e).val.equals(ownerUUID)) continue;

                HitboxComponent hitbox = HitboxComponent.MAPPER.get(e);
                if (!hitbox.active) continue;
                if (hitbox.intersects(weaponHitbox)) {
                    System.out.println("collision");
                }
            }
        }
    }
}
