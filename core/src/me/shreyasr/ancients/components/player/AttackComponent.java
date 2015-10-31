package me.shreyasr.ancients.components.player;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.utils.Pool;

public class AttackComponent implements Component, Pool.Poolable {

    public static ComponentMapper<AttackComponent> MAPPER
            = ComponentMapper.getFor(AttackComponent.class);

    public static AttackComponent create(Attack attack) {
        AttackComponent atk = new AttackComponent();
        atk.attack = attack;
        return atk;
    }

    public Attack attack;

    public AttackComponent() {
        reset();
    }

    @Override
    public void reset() {
        attack = null;
    }
}
