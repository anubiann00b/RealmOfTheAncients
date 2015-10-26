package me.shreyasr.ancients.components.weapon;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

import me.shreyasr.ancients.util.CustomUUID;
import me.shreyasr.ancients.components.UUIDComponent;
import me.shreyasr.ancients.components.type.TypeComponent;

public class OwnerUUIDComponent implements Component, Pool.Poolable {

    public static ComponentMapper<OwnerUUIDComponent> MAPPER
            = ComponentMapper.getFor(OwnerUUIDComponent.class);

    public static OwnerUUIDComponent create(PooledEngine engine, long engineId) {
        OwnerUUIDComponent uuid = engine.createComponent(OwnerUUIDComponent.class);
        uuid.ownerUUID = UUIDComponent.MAPPER.get(engine.getEntity(engineId)).val;
        uuid.ownerEngineID = engineId;
        return uuid;
    }

    public CustomUUID ownerUUID;
    public long ownerEngineID;

    public void updateEngineId(Engine engine) {
        for (Entity e : engine.getEntitiesFor(Family.all(TypeComponent.Player.class).get())) {
            if (ownerUUID.equals(UUIDComponent.MAPPER.get(e).val)) {
                ownerEngineID = e.getId();
                break;
            }
        }
    }

    public Entity getOwner(Engine engine) {
        return engine.getEntity(ownerEngineID);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof OwnerUUIDComponent && ((OwnerUUIDComponent) o).ownerUUID.equals(ownerUUID);
    }

    @Override
    public String toString() {
        return ownerUUID.toString();
    }

    public OwnerUUIDComponent() {
        reset();
    }

    @Override
    public void reset() {
        ownerUUID = null;
        ownerEngineID = -1;
    }
}
