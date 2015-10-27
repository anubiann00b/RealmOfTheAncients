package me.shreyasr.ancients.systems.render;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.esotericsoftware.kryonet.Client;

import me.shreyasr.ancients.AncientsGame;
import me.shreyasr.ancients.packet.server.ServerChatMessagePacket;
import me.shreyasr.ancients.util.chat.ChatManager;
import me.shreyasr.ancients.util.chat.ChatMessage;

public class UIRenderSystem extends EntitySystem {

    private final AncientsGame game;
    private final ChatManager chatManager;
    private final Client client;

    private Skin skin;
    private Stage stage;
    private Table table;

    public UIRenderSystem(int priority, AncientsGame game, final ChatManager chatManager, final Client client) {
        super(priority);
        this.game = game;
        this.chatManager = chatManager;
        this.client = client;

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table = new Table();
//        table.setFillParent(true);
        table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - 300);
        table.setPosition(0, 0);
        table.setDebug(true);

        Label chatbox = new Label("l", skin);
        chatbox.setAlignment(Align.bottomLeft);
        chatbox.setWrap(true);
        chatbox.setHeight(Gdx.graphics.getHeight() - 300);
        TextField chatInput = new TextField("", skin);

        chatbox.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                ChatMessage[] messages = chatManager.getLastMessages(10);
                StringBuilder labelContent = new StringBuilder();
                for (int i = messages.length - 1; i >= 0; i--) {
                    ChatMessage m = messages[i];
                    labelContent.append(m.body).append("\n");
                }
                ((Label) actor).setText(labelContent.toString());
                return false;
            }
        });

        chatInput.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if (c == '\r' || c == '\n') {
                    client.sendTCP(ServerChatMessagePacket.create(textField.getText(), null));
                    textField.setText("");
                }
            }
        });

        table.add(chatbox).expand().fill().bottom().left();
        table.row();
        table.add(chatInput).expandX().fillX();

        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(0, 0, 0, 0.4f);
        pix.fill();
        table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(pix))));

        stage.addActor(table);
    }

    @Override
    public void update(float deltaTime) {
        stage.act(deltaTime);
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}
