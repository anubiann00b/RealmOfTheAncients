package me.shreyasr.ancients;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AncientsGame extends Game {

    public SpriteBatch batch;
    public AssetManager assetManager;

    @Override
    public void create() {
        assetManager = new AssetManager();
        Assets.loadAll(assetManager);
        batch = new SpriteBatch();

        assetManager.finishLoading();

        this.setScreen(new LoadingScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }
}
