package me.shreyasr.ancients.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PreRenderSystem extends EntitySystem {

    private final SpriteBatch batch;

    public PreRenderSystem(int priority, SpriteBatch batch) {
        super(priority);
        this.batch = batch;
    }

    public void update(float deltaTime) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
    }
}
