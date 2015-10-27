package me.shreyasr.ancients.systems.render;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import me.shreyasr.ancients.AncientsGame;
import me.shreyasr.ancients.util.chat.ChatManager;
import me.shreyasr.ancients.util.chat.ChatMessage;

public class ChatRenderSystem extends EntitySystem {

    private final AncientsGame game;
    private final ChatManager chatManager;

    public ChatRenderSystem(int priority, AncientsGame game, ChatManager chatManager) {
        super(priority);
        this.game = game;
        this.chatManager = chatManager;
    }

    @Override
    public void update(float deltaTime) {
        ChatMessage[] messages = chatManager.getLastMessages(10);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        game.shape.setColor(0, 0, 0, 0.8f);
        game.shape.set(ShapeRenderer.ShapeType.Filled);

        int margin = 50;
        int chatboxHeight = 300;
        int messagePadding = 20;
        int rowHeight = 20;

        game.shape.rect(margin, margin, Gdx.graphics.getWidth() - margin * 2, chatboxHeight);

        game.shape.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        game.batch.begin();

        game.font.setColor(Color.WHITE);

        int rowIndex = 2;
        for(ChatMessage message : messages) {
            game.font.draw(game.batch, message.body,
                    margin + messagePadding,
                    margin + rowHeight*rowIndex);
            rowIndex++;
        }
    }
}
