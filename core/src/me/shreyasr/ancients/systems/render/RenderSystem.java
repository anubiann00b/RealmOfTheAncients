package me.shreyasr.ancients.systems.render;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;

import me.shreyasr.ancients.AncientsGame;
import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.components.TextureComponent;
import me.shreyasr.ancients.components.TextureTransformComponent;

public class RenderSystem extends IteratingSystem {

    private final AncientsGame game;

    public RenderSystem(int priority, AncientsGame game) {
        super(
                Family.all(PositionComponent.class,
                           TextureComponent.class,
                           TextureTransformComponent.class)
                      .get(),
                priority);
        this.game = game;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pos = PositionComponent.MAPPER.get(entity);
        TextureTransformComponent ttc = TextureTransformComponent.MAPPER.get(entity);

        if (ttc.hide) return;

        Texture texture = game.assetManager.get(TextureComponent.MAPPER.get(entity).textureFile, Texture.class);

        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        game.batch.draw(texture, pos.x - ttc.originX, pos.y - ttc.originY, ttc.originX, ttc.originY,
                ttc.screenWidth, ttc.screenHeight, 1, 1, ttc.rotation,
                ttc.srcX, ttc.srcY, ttc.srcWidth, ttc.srcHeight, false, false);
    }
}
