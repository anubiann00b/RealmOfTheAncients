package me.shreyasr.ancients.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Time;

import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import me.shreyasr.ancients.AncientsGame;
import me.shreyasr.ancients.packet.server.ServerNameRegistrationPacket;
import me.shreyasr.ancients.util.KryoRegistrar;

public class LoadingScreen extends ScreenAdapter implements Input.TextInputListener {

    private AncientsGame game;
    private Client client;

    public LoadingScreen(AncientsGame game) {
        this.game = game;
    }

    private boolean connected = false;
    private boolean hasName = AncientsGame.DEBUG_MODE;
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
                    client.connect(5000, AncientsGame.DEBUG_MODE ? "127.0.0.1" : "104.131.149.236", 54555, 54777);
                    connected = true;
                } catch (IOException e) {
                    e.printStackTrace();
                    Gdx.app.exit();
                }
            }
        }).start();
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException e) {
            System.out.println("Error loading look and feel: " + e);
        } catch (InstantiationException e) {
            System.out.println("Error loading look and feel: " + e);
        } catch (IllegalAccessException e) {
            System.out.println("Error loading look and feel: " + e);
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println("Error loading look and feel: " + e);
        }

        promptName();
    }

    private void promptName() {
        if (AncientsGame.DEBUG_MODE) {
            input("test" + (int)(Math.random()*100));
        } else {
            Gdx.input.getTextInput(this, "What's your name?", "", "name");
        }
    }

    @Override
    public void render(float deltaTime) {
        if (hasName && connected) game.setScreen(new GameScreen(game, client, name));
    }

    @Override
    public void input(String text) {
        name = text;
        hasName = true;
        client.sendTCP(ServerNameRegistrationPacket.create(name));
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
