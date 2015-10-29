package me.shreyasr.ancients.systems.render;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.esotericsoftware.kryonet.Client;

import java.util.Comparator;
import java.util.Iterator;

import me.shreyasr.ancients.AncientsGame;
import me.shreyasr.ancients.components.NameComponent;
import me.shreyasr.ancients.components.StatsComponent;
import me.shreyasr.ancients.components.player.MyPlayerComponent;
import me.shreyasr.ancients.components.type.TypeComponent;
import me.shreyasr.ancients.packet.server.ServerChatMessagePacket;
import me.shreyasr.ancients.util.CustomUUID;
import me.shreyasr.ancients.util.chat.ChatManager;
import me.shreyasr.ancients.util.chat.ChatMessage;

public class UIRenderSystem extends EntitySystem implements EntityListener {

    private final AncientsGame game;
    private final ChatManager chatManager;
    private final Client client;
    private final CustomUUID playerUUID;
    private final String playerName;

    private Array<Entity> sortedPlayers;

    private Comparator<Entity> comparator = new StatsComponent.StatsComparator().reversed();

    private Skin skin;
    private Stage stage;
    private Table chatTable;
    private Table scoreboardTable;

    public UIRenderSystem(int priority, AncientsGame game, Engine engine, ChatManager chatManager,
                          Client client, CustomUUID playerUUID, String playerName) {
        super(priority);
        this.game = game;
        this.chatManager = chatManager;
        this.client = client;
        this.playerUUID = playerUUID;
        this.playerName = playerName;

        sortedPlayers = new Array<Entity>(false, 16);
        Family family = Family.all(TypeComponent.Player.class).get();
        ImmutableArray<Entity> newPlayers  = engine.getEntitiesFor(family);
        sortedPlayers.clear();
        for (Entity player : newPlayers) {
            sortedPlayers.add(player);
        }
        sortedPlayers.sort(comparator);
        engine.addEntityListener(family, this);
        init();
    }

    public InputProcessor getStageInputProcessor() {
        return stage;
    }

    private void init() {
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        chatTable = new Table();
        chatTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - 265);
        chatTable.setPosition(0, 0);
        chatTable.setDebug(false);

        Label chatbox = new Label("l", skin);
        chatbox.setAlignment(Align.bottomLeft);
        chatbox.setWrap(true);
        chatbox.setHeight(Gdx.graphics.getHeight() - 265);
        final TextField chatInput = new TextField("", skin);

        chatManager.addListener(new ChatManager.ChatListener() {
            @Override
            public void newMessage(ChatMessage message) {
                chatTable.setVisible(true);
            }
        });

        chatbox.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                ChatMessage[] messages = chatManager.getLastMessages(8);
                StringBuilder labelContent = new StringBuilder();
                for (int i = messages.length - 1; i >= 0; i--) {
                    ChatMessage m = messages[i];
                    labelContent.append(m.getChatString()).append("\n");
                }
                ((Label) actor).setText(labelContent.toString());
                return false;
            }
        });

        chatInput.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if ((c == '\r' || c == '\n') && !textField.getText().trim().isEmpty()) {
                    client.sendTCP(ServerChatMessagePacket.create(textField.getText(), playerUUID, playerName));
                    textField.setText("");
                }
            }
        });

        stage.addListener(new InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                if (event.getKeyCode() == Input.Keys.T && stage.getKeyboardFocus() == null) {
                    chatTable.setVisible(true);
                    stage.setKeyboardFocus(chatInput);
                    event.cancel();
                    return true;
                }
                return false;
            }

            boolean firstFrameEscape = false;
            boolean escapeDown = false;

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (event.getKeyCode() == Input.Keys.ESCAPE) {
                    firstFrameEscape = !escapeDown;
                    escapeDown = true;
                    stage.unfocusAll();

                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && firstFrameEscape) {
                        chatTable.setVisible(false);
                    }
                    return true;
                }
                return false;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (event.getKeyCode() == Input.Keys.ESCAPE) {
                    escapeDown = false;
                    firstFrameEscape = false;
                    return true;
                }
                return false;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (stage.hit(x, y, true) == null) {
                    stage.unfocusAll();
                }
                return false;
            }
        });

        chatTable.add(chatbox).expand().fill().bottom().left();
        chatTable.row();
        chatTable.add(chatInput).expandX().fillX();

        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(0, 0, 0, 0.4f);
        pix.fill();
        chatTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(pix))));

        int margin = 50;

        scoreboardTable = new Table();
        scoreboardTable.setSize(
                Gdx.graphics.getWidth() - margin * 2,
                Gdx.graphics.getHeight() - margin * 2);
        scoreboardTable.setPosition(margin, margin);
        scoreboardTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(pix))));
        scoreboardTable.setDebug(false);

        Label nameHeadingLabel = new Label(null, skin);
        nameHeadingLabel.setText("Player");
        scoreboardTable.add(nameHeadingLabel).align(Align.left).expand();

        Label hitsHeadingLabel = new Label(null, skin);
        hitsHeadingLabel.setText("Hits");
        scoreboardTable.add(hitsHeadingLabel).align(Align.left).expand();

        scoreboardTable.row();

        final int numScoreboardRows = 9;
        final Label[] scoreNameLabels = new Label[numScoreboardRows];
        final Label[] scoreHitLabels = new Label[numScoreboardRows];
        for (int i = 0; i < numScoreboardRows; i++) {
            scoreNameLabels[i] = new Label(null, skin);
            scoreNameLabels[i].setAlignment(Align.left);
            scoreboardTable.add(scoreNameLabels[i]).align(Align.left).expand();
            scoreHitLabels[i] = new Label(null, skin);
            scoreHitLabels[i].setAlignment(Align.left);
            scoreboardTable.add(scoreHitLabels[i]).align(Align.left).expand();
            scoreboardTable.row();
        }

        scoreboardTable.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                scoreboardTable.setVisible(Gdx.input.isKeyPressed(Input.Keys.TAB));
                Iterator<Entity> playerIterator = sortedPlayers.iterator();
                for (int i = 0; i < numScoreboardRows; i++) {
                    if (!playerIterator.hasNext()) {
                        scoreNameLabels[i].setText(null);
                        scoreHitLabels[i].setText(null);
                    } else {
                        Entity player = playerIterator.next();

                        StatsComponent stats = StatsComponent.MAPPER.get(player);
                        NameComponent name = NameComponent.MAPPER.get(player);

                        Color c = MyPlayerComponent.MAPPER.has(player) ? Color.SKY : Color.WHITE;
                        scoreNameLabels[i].setColor(c);
                        scoreHitLabels[i].setColor(c);

                        scoreNameLabels[i].setText(name.str);
                        scoreHitLabels[i].setText(String.valueOf(stats.hits));
                    }
                }
                return false;
            }
        });

        stage.addActor(chatTable);
        stage.addActor(scoreboardTable);
    }

    @Override
    public void update(float deltaTime) {
        stage.act(deltaTime);
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void entityAdded(Entity entity) {
        sortedPlayers.add(entity);
        sortedPlayers.sort(comparator);
    }

    @Override
    public void entityRemoved(Entity entity) {
        sortedPlayers.removeValue(entity, true);
        sortedPlayers.sort(comparator);
    }
}
