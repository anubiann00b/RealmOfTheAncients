package me.shreyasr.ancients.systems.render.util;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ShapeRenderSystem extends EntitySystem {

    private final SpriteBatch batch;
    private final ShapeRenderer shape;
    private final OrthographicCamera camera;

    public ShapeRenderSystem(int priority, SpriteBatch batch, ShapeRenderer shape, OrthographicCamera camera) {
        super(priority);
        this.batch = batch;
        this.shape = shape;
        this.camera = camera;
    }

    public void update(float deltaTime) {
        batch.end();

        shape.setProjectionMatrix(camera.combined);
        shape.begin();
    }
}
