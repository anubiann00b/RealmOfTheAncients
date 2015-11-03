package me.shreyasr.ancients.systems.render;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;

import me.shreyasr.ancients.AncientsGame;
import me.shreyasr.ancients.components.NameComponent;
import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.components.type.TypeComponent;

public class NameRenderSystem extends IteratingSystem {

    private final AncientsGame game;

    public NameRenderSystem(int priority, AncientsGame game) {
        super(
                Family.all(PositionComponent.class,
                           NameComponent.class,
                           TypeComponent.Player.class)
                        .get(),
                priority);
        this.game = game;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent playerPos = PositionComponent.MAPPER.get(entity);
        String name = NameComponent.MAPPER.get(entity).str;
        game.font.setColor(Color.WHITE);
        game.font.draw(game.batch, name, playerPos.x - 32, playerPos.y - 36, 0, name.length(), 64, Align.center, false);
    }
}
