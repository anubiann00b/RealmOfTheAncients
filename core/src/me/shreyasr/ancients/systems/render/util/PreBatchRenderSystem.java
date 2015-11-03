package me.shreyasr.ancients.systems.render.util;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PreBatchRenderSystem extends EntitySystem {

    private final SpriteBatch batch;
    private OrthographicCamera camera;

    public PreBatchRenderSystem(int priority, SpriteBatch batch) {
        super(priority);
        this.batch = batch;
    }

    public void update(float deltaTime) {
        batch.begin();
    }
}
