package me.shreyasr.ancients.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.utils.Pool;

public class PeerComponent implements Component, Pool.Poolable {

    public static ComponentMapper<PeerComponent> MAPPER
            = ComponentMapper.getFor(PeerComponent.class);

    public static PeerComponent create(String name) {
        PeerComponent peer = new PeerComponent();
        peer.name = name;
        return peer;
    }

    public String name;

    @Override public boolean equals(Object o) {
        return o instanceof PeerComponent && name.equals(((PeerComponent) o).name);
    }

    public PeerComponent() {
        reset();
    }

    @Override
    public void reset() {
        name = null;
    }
}
