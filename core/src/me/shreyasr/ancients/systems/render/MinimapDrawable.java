package me.shreyasr.ancients.systems.render;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;

import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.components.type.TypeComponent;

public class MinimapDrawable extends BaseDrawable {

    private final Texture background;
    private final Texture playerDot;
    private final int worldWidth;
    private final int worldHeight;
    private final ImmutableArray<Entity> players;

    public MinimapDrawable(Engine engine, Texture background, int worldWidth, int worldHeight) {
        this.background = background;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        players = engine.getEntitiesFor(Family.all(TypeComponent.Player.class).get());

        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(0, 0, 1, 1);
        pix.fill();
        playerDot = new Texture(pix);
    }

    public void draw(Batch batch, float x, float y, float width, float height) {
        batch.draw(background, x, y, width, height);
        int playerDotSize = (int) (height/24);
        for (Entity player : players) {
            PositionComponent pos = PositionComponent.MAPPER.get(player);
            float scaledX = x+pos.x/worldWidth*(width-playerDotSize);
            float scaledY = y+pos.y/worldHeight*(height-playerDotSize);
            batch.draw(playerDot, scaledX, scaledY, playerDotSize, playerDotSize);
        }
    }
}
