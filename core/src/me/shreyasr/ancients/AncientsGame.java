package me.shreyasr.ancients;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import me.shreyasr.ancients.screen.LoadingScreen;
import me.shreyasr.ancients.util.Assets;

public class AncientsGame extends Game {

    public static boolean DEBUG_MODE = true;

    public SpriteBatch batch;
    public ShapeRenderer shape;
    public AssetManager assetManager;
    public BitmapFont font;

    @Override
    public void create() {
        assetManager = new AssetManager();
        Assets.loadAll(assetManager);
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        shape.setAutoShapeType(true);

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
