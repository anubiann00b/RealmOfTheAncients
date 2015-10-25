package me.shreyasr.ancients.systems.render;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;

import me.shreyasr.ancients.AncientsGame;
import me.shreyasr.ancients.components.HitboxComponent;

public class DebugRenderSystem extends IteratingSystem {

    private final AncientsGame game;

    public DebugRenderSystem(int priority, AncientsGame game) {
        super(Family.all(HitboxComponent.class).get(), priority);
        this.game = game;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        HitboxComponent hitbox = HitboxComponent.MAPPER.get(entity);
        game.batch.end();
        game.shape.begin();
        game.shape.setColor(Color.RED);

        game.shape.rect(hitbox.x, hitbox.y, hitbox.w, hitbox.h);

        game.shape.end();
        game.batch.begin();
    }
}
