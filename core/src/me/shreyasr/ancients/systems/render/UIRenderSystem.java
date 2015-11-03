package me.shreyasr.ancients.systems.render;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.esotericsoftware.kryonet.Client;

import java.util.Iterator;
import java.util.TreeSet;

import me.shreyasr.ancients.components.NameComponent;
import me.shreyasr.ancients.components.StatsComponent;
import me.shreyasr.ancients.components.player.MyPlayerComponent;
import me.shreyasr.ancients.components.player.attack.AttackComponent;
import me.shreyasr.ancients.components.player.attack.BasicWeaponAttack;
import me.shreyasr.ancients.components.player.dash.DashComponent;
import me.shreyasr.ancients.components.type.TypeComponent;
import me.shreyasr.ancients.packet.server.ServerChatMessagePacket;
import me.shreyasr.ancients.util.chat.ChatManager;
import me.shreyasr.ancients.util.chat.ChatMessage;

public class UIRenderSystem extends EntitySystem {

    private final Engine engine;
    private final ChatManager chatManager;
    private final Client client;
    private Entity player;

    private TreeSet<Entity> sortedPlayers;
    private ImmutableArray<Entity> players;

    private Skin skin;
    private Stage stage;
    private Table chatTable;
    private Table scoreboardTable;
    private Table infoTable;
    private Image minimap;

    private ExtendViewport viewport;

    int scoreboardMargin = 50;

    public UIRenderSystem(int priority, Engine engine, ChatManager chatManager, Client client) {
        super(priority);
        this.engine = engine;
        this.chatManager = chatManager;
        this.client = client;

        players = engine.getEntitiesFor(Family.all(TypeComponent.Player.class).get());
        sortedPlayers = new TreeSet<Entity>(new StatsComponent.ReversedStatsComparator());

        init();
    }

    @Override
    public void addedToEngine(Engine engine) {
        player = engine.getEntitiesFor(Family.all(MyPlayerComponent.class).get()).first();
        addPlayerInfo(infoTable);
    }

    public InputProcessor getStageInputProcessor() {
        return stage;
    }

    private void init() {
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        viewport = new ExtendViewport(800, 600, 1280, 720);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        chatTable = new Table();
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
                    client.sendTCP(ServerChatMessagePacket.create(textField.getText(), player));
                    textField.setText("");
                }
            }
        });

        stage.addListener(new InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                if (event.getKeyCode() == Input.Keys.ENTER && stage.getKeyboardFocus() == null) {
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

        scoreboardTable = new Table();
        scoreboardTable.setPosition(scoreboardMargin, scoreboardMargin);
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
                sortedPlayers.clear();
                for (Entity e : players) {
                    sortedPlayers.add(e);
                }
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

                        if (name != null) scoreNameLabels[i].setText(name.str);
                        if (stats != null) scoreHitLabels[i].setText(String.valueOf(stats.hits));
                    }
                }
                return false;
            }
        });

        infoTable = new Table(skin);
        infoTable.align(Align.topLeft);

        createLabelPair(infoTable, "FPS", new ValueUpdateAction() {
            @Override
            public void updateValue(Label label, float deltaTime) {
                label.setText(String.valueOf((int) (1000 / deltaTime)));
            }
        });
        createLabelPair(infoTable, "PING", new ValueUpdateAction() {
            @Override
            public void updateValue(Label label, float deltaTime) {
                label.setText(String.valueOf(client.getReturnTripTime()));
            }
        });
        infoTable.row();

        minimap = new Image(new MinimapDrawable(engine, new Texture(pix), 3840, 3840));

        stage.addActor(chatTable);
        stage.addActor(infoTable);
        stage.addActor(minimap);
        stage.addActor(scoreboardTable);

        chatTable.setVisible(false);
    }

    @Override
    public void update(float deltaTime) {
        stage.act(deltaTime);
        viewport.getCamera().position.set(stage.getWidth()/2,stage.getHeight()/2,0);
        stage.getViewport().apply();
        viewport.getCamera().update();
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
        scoreboardTable.setSize(
                stage.getWidth() - scoreboardMargin * 2,
                stage.getHeight() - scoreboardMargin * 2);
        chatTable.setSize(stage.getWidth(), stage.getHeight()*2/5f);
        infoTable.setPosition(0, stage.getHeight());

        int minimapSize = (int) (stage.getHeight()/3);
        minimap.setPosition(stage.getWidth()-minimapSize, stage.getHeight()-minimapSize);
        minimap.setSize(minimapSize, minimapSize);
    }

    private Label createLabelPair(Table table, String label, final ValueUpdateAction action) {
        Label name = new Label(label, skin);
        final Label value = new Label(null, skin);
        name.setColor(Color.WHITE);
        value.setColor(Color.WHITE);
        table.add(name).align(Align.left);
        table.add(value).align(Align.left).row();
        value.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                action.updateValue(value, delta);
                return false;
            }
        });
        return value;
    }

    private void addPlayerInfo(Table infoTable) {
        createLabelPair(infoTable, "Cooldown", new PlayerInfoValueUpdateAction() {
            @Override
            public String getValue(BasicWeaponAttack atk, DashComponent dash) {
                return String.valueOf(atk.cooldownTime);
            }
        });
        createLabelPair(infoTable, "Swing", new PlayerInfoValueUpdateAction() {
            @Override
            public String getValue(BasicWeaponAttack atk, DashComponent dash) {
                return String.valueOf(atk.swingTime);
            }
        });
        createLabelPair(infoTable, "Hold", new PlayerInfoValueUpdateAction() {
            @Override
            public String getValue(BasicWeaponAttack atk, DashComponent dash) {
                return String.valueOf(atk.lastFrameHoldTime);
            }
        });
        createLabelPair(infoTable, "Knockback", new PlayerInfoValueUpdateAction() {
            @Override
            public String getValue(BasicWeaponAttack atk, DashComponent dash) {
                return String.valueOf(atk.knockbackMultiplier);
            }
        });

        createLabelPair(infoTable, "Duration", new PlayerInfoValueUpdateAction() {
            @Override
            public String getValue(BasicWeaponAttack atk, DashComponent dash) {
                return String.valueOf(dash.duration);
            }
        });
        createLabelPair(infoTable, "Distance", new PlayerInfoValueUpdateAction() {
            @Override
            public String getValue(BasicWeaponAttack atk, DashComponent dash) {
                return String.valueOf(dash.distance);
            }
        });
        createLabelPair(infoTable, "Cooldown", new PlayerInfoValueUpdateAction() {
            @Override
            public String getValue(BasicWeaponAttack atk, DashComponent dash) {
                return String.valueOf(dash.cooldown);
            }
        });
        createLabelPair(infoTable, "Stun", new PlayerInfoValueUpdateAction() {
            @Override
            public String getValue(BasicWeaponAttack atk, DashComponent dash) {
                return String.valueOf(dash.stunTime);
            }
        });
    }

    private interface ValueUpdateAction {
        void updateValue(Label label, float deltaTime);
    }

    private abstract class PlayerInfoValueUpdateAction implements ValueUpdateAction {
        @Override
        public void updateValue(Label label, float deltaTime) {
            AttackComponent attackComponent = AttackComponent.MAPPER.get(player);
            final DashComponent dash = DashComponent.MAPPER.get(player);
            final BasicWeaponAttack atk = (BasicWeaponAttack) attackComponent.attack;
            label.setText(getValue(atk, dash));
        }

        public abstract String getValue(BasicWeaponAttack atk, DashComponent dash);
    }
}
