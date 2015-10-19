package me.shreyasr.ancients;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import me.shreyasr.ancients.screen.LoadingScreen;

public class AncientsGame extends Game {

    public SpriteBatch batch;
    public AssetManager assetManager;
    public BitmapFont font;

    @Override
    public void create() {
        assetManager = new AssetManager();
        Assets.loadAll(assetManager);
        batch = new SpriteBatch();

        assetManager.finishLoading();

        font = new BitmapFont();
        font.setColor(Color.BLACK);

        this.setScreen(new LoadingScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }
}
