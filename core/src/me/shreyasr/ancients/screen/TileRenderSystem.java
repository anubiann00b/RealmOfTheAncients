package me.shreyasr.ancients.screen;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import me.shreyasr.ancients.AncientsGame;
import me.shreyasr.ancients.util.Assets;

public class TileRenderSystem extends EntitySystem {

    private final AncientsGame game;
    TiledMap map;
    OrthogonalTiledMapRenderer renderer;
    OrthographicCamera camera;

    public TileRenderSystem(int priority, AncientsGame game, OrthographicCamera camera) {
        super(priority);
        this.game = game;
        this.camera = camera;
        map = game.assetManager.get(Assets.MAP.getFile());
        renderer = new OrthogonalTiledMapRenderer(map, 4f, game.batch);
    }

    @Override
    public void update(float deltaTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.setView(camera);
        renderer.render();
    }
}
