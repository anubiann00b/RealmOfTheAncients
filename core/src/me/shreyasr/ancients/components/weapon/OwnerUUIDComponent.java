package me.shreyasr.ancients.components.weapon;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

import me.shreyasr.ancients.CustomUUID;
import me.shreyasr.ancients.components.UUIDComponent;

public class OwnerUUIDComponent implements Component, Pool.Poolable {

    public static ComponentMapper<OwnerUUIDComponent> MAPPER
            = ComponentMapper.getFor(OwnerUUIDComponent.class);

    public static OwnerUUIDComponent create(PooledEngine engine, long engineId) {
        OwnerUUIDComponent uuid = engine.createComponent(OwnerUUIDComponent.class);
        uuid.customUUID = UUIDComponent.MAPPER.get(engine.getEntity(engineId)).val;
        uuid.engineId = engineId;
        return uuid;
    }

    public CustomUUID customUUID;
    public long engineId;

    public void updateEngineId(Engine engine) {
        if (engineId < 0) {
            for (Entity e : engine.getEntitiesFor(Family.all(UUIDComponent.class).get())) {
                if (UUIDComponent.MAPPER.get(e).equals(customUUID)) {
                    engineId = e.getId();
                    break;
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof OwnerUUIDComponent && ((OwnerUUIDComponent) o).customUUID.equals(customUUID);
    }

    @Override
    public String toString() {
        return customUUID.toString();
    }

    public OwnerUUIDComponent() {
        reset();
    }

    @Override
    public void reset() {
        customUUID = null;
        engineId = -1;
    }
}
