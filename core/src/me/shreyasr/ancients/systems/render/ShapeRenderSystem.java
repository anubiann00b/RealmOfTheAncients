package me.shreyasr.ancients.systems.render;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ShapeRenderSystem extends EntitySystem {

    private final SpriteBatch batch;
    private final ShapeRenderer shape;

    public ShapeRenderSystem(int priority, SpriteBatch batch, ShapeRenderer shape) {
        super(priority);
        this.batch = batch;
        this.shape = shape;
    }

    public void update(float deltaTime) {
        batch.end();
        shape.begin();
    }
}
