package me.shreyasr.ancients.system;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.Time;

import me.shreyasr.ancients.components.HitboxComponent;
import me.shreyasr.ancients.components.KnockbackComponent;
import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.components.StatsComponent;
import me.shreyasr.ancients.components.UUIDComponent;
import me.shreyasr.ancients.components.player.attack.AttackComponent;
import me.shreyasr.ancients.components.type.TypeComponent;
import me.shreyasr.ancients.components.weapon.OwnerUUIDComponent;
import me.shreyasr.ancients.components.weapon.WeaponAnimationComponent;
import me.shreyasr.ancients.packet.client.ClientHitPacket;
import me.shreyasr.ancients.packet.server.ServerAttackPacket;
import me.shreyasr.ancients.util.CustomUUID;

public class CollisionDetectionSystem extends EntitySystem {

    private final Server server;
    private final PooledEngine engine;

    public CollisionDetectionSystem(int priority, Server server, PooledEngine engine) {
        super(priority);
        this.server = server;
        this.engine = engine;
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
            OwnerUUIDComponent ownerComponent = OwnerUUIDComponent.MAPPER.get(w);
            CustomUUID ownerUUID = ownerComponent.ownerUUID;
            Entity owner = ownerComponent.getOwner(engine);
            if (owner == null) {
                System.out.println("Null owner :( " + ownerComponent.ownerUUID.toString());
                continue;
            }
            AttackComponent ownerAttackComponent = AttackComponent.MAPPER.get(owner);

            if (weaponAnim.isDone() || weaponAnim.timeSinceAnimStart < 0) continue;
            HitboxComponent weaponHitbox = HitboxComponent.MAPPER.get(w);
            PositionComponent weaponPos = PositionComponent.MAPPER.get(w);

            if (!weaponHitbox.active) continue;

            for (Entity e : entities) {
                CustomUUID entityUUID = UUIDComponent.MAPPER.get(e).val;
                KnockbackComponent knockback = KnockbackComponent.MAPPER.get(e);

                if (entityUUID.equals(ownerUUID)) continue;
                if (knockback != null && !knockback.isDone()) continue;

                HitboxComponent hitbox = HitboxComponent.MAPPER.get(e);
                PositionComponent pos = PositionComponent.MAPPER.get(e);
                if (!hitbox.active) continue;
                if (hitbox.intersects(weaponHitbox)) {
                    Component[] components = e.getComponents().toArray(Component.class);
                    KnockbackComponent knockbackComponent
                            = KnockbackComponent.create(engine, pos.x, pos.y,
                            pos.x - weaponPos.x, pos.y - weaponPos.y,
                            Time.getMillis() + ServerAttackPacket.KNOCKBACK_DELAY_MS);
                    if (ownerAttackComponent != null && ownerAttackComponent.attack != null) {
                        knockbackComponent.power = ownerAttackComponent.attack.getKnockbackMultiplier();
                    }
                    e.add(knockbackComponent);

                    StatsComponent.MAPPER.get(owner).hits++;

                    server.sendToAllTCP(ClientHitPacket.create(components, knockbackComponent));
                }
            }
        }
    }
}
