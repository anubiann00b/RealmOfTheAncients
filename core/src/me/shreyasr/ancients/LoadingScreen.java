package me.shreyasr.ancients;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.esotericsoftware.kryonet.Client;

import java.io.IOException;

public class LoadingScreen extends ScreenAdapter {

    private AncientsGame game;

    public LoadingScreen(AncientsGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        final Client client = new Client();
        KryoRegistrar.register(client.getKryo());
        client.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client.connect(5000, "127.0.0.1", 54555, 54777);
                    game.setScreen(new GameScreen(game, client));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    Gdx.app.exit();
                }
            }
        }).start();
    }

    @Override
    public void render(float deltaTime) {

    }
}
