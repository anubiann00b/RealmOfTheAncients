package me.shreyasr.ancients.systems.render;

import com.badlogic.ashley.core.EntitySystem;

import me.shreyasr.ancients.AncientsGame;

public class PostRenderSystem extends EntitySystem {

    private final AncientsGame game;

    public PostRenderSystem(int priority, AncientsGame game) {
        super(priority);
        this.game = game;
    }

    public void update(float deltaTime) {
        if (game.shape.isDrawing()) game.shape.end();
        if (game.batch.isDrawing()) game.batch.end();
    }
}
