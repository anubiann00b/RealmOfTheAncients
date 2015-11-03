package me.shreyasr.ancients.systems.render;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.components.player.MyPlayerComponent;

public class CameraUpdateSystem extends EntitySystem {

    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private Entity player;

    public CameraUpdateSystem(int priority, SpriteBatch batch, OrthographicCamera camera, Viewport viewport) {
        super(priority);
        this.batch = batch;
        this.camera = camera;
        this.viewport = viewport;
    }

    public void addedToEngine(Engine engine) {
        player = engine.getEntitiesFor(Family.all(MyPlayerComponent.class).get()).first();
    }

    @Override
    public void update(float deltaTime) {
        PositionComponent pos = PositionComponent.MAPPER.get(player);
        camera.position.set(pos.x, pos.y, 0);
        viewport.apply();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }
}
