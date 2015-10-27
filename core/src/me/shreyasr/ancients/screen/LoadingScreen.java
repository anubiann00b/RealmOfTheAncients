package me.shreyasr.ancients.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Time;

import java.io.IOException;

import me.shreyasr.ancients.AncientsGame;
import me.shreyasr.ancients.util.KryoRegistrar;

public class LoadingScreen extends ScreenAdapter implements Input.TextInputListener {

    private AncientsGame game;
    private Client client;

    public LoadingScreen(AncientsGame game) {
        this.game = game;
    }

    private boolean connected = false;
    private boolean hasName = false;
    private String name;

    @Override
    public void show() {
        client = new Client();
        KryoRegistrar.register(client.getKryo());
        Time.setConn(client);
        client.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    client.connect(5000, "104.131.149.236", 54555, 54777);
                    client.connect(5000, "127.0.0.1", 54555, 54777);
                    connected = true;
                    moveToGameScreen();
                } catch (IOException e) {
                    e.printStackTrace();
                    Gdx.app.exit();
                }
            }
        }).start();
        Gdx.input.getTextInput(this, "What's your name?", "", "name");
    }

    @Override
    public void render(float deltaTime) { }

    private void moveToGameScreen() {
        if (hasName && connected) game.setScreen(new GameScreen(game, client, name));
    }

    @Override
    public void input(String text) {
        hasName = true;
        name = text;
        moveToGameScreen();
    }

    @Override
    public void canceled() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append((char)(Math.random()*26+'a'));
        }
        input(sb.toString());
    }
}
