package me.shreyasr.ancients.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.utils.Pool;
import me.shreyasr.ancients.CustomUUID;

public class UUIDComponent implements Component, Pool.Poolable {

    public static ComponentMapper<UUIDComponent> MAPPER
            = ComponentMapper.getFor(UUIDComponent.class);

    public static UUIDComponent create() {
        UUIDComponent uuid = new UUIDComponent();
        uuid.val = CustomUUID.randomUUID();
        return uuid;
    }

    public static UUIDComponent create(CustomUUID uuid) {
        UUIDComponent uuidComponent = new UUIDComponent();
        uuidComponent.val = uuid;
        return uuidComponent;
    }

    public CustomUUID val;

    @Override
    public boolean equals(Object o) {
        return o instanceof UUIDComponent && ((UUIDComponent)o).val.equals(val);
    }

    @Override
    public String toString() {
        return val.toString();
    }

    public UUIDComponent() {
        reset();
    }

    @Override
    public void reset() {
        val = null;
    }
}
