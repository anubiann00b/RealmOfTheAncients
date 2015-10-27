package me.shreyasr.ancients.systems.render;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;

import me.shreyasr.ancients.AncientsGame;
import me.shreyasr.ancients.components.NameComponent;
import me.shreyasr.ancients.components.StatsComponent;
import me.shreyasr.ancients.components.player.MyPlayerComponent;
import me.shreyasr.ancients.components.type.TypeComponent;

public class ScoreboardRenderSystem extends EntitySystem implements EntityListener {

    /*

    n = name
    s = score

    +--------------+
    | +----------+ |
    | | n s      | | \ rowHeight
    | | n s      | | /
    | | n s      | |
    | +----------+ |
    +--------------+
    \ / = margin
      \ / = padding
        V = nameWidth
    */

    private final AncientsGame game;
    private Array<Entity> sortedPlayers;

    private Comparator<Entity> comparator = new StatsComponent.StatsComparator().reversed();

    public ScoreboardRenderSystem(int priority, AncientsGame game) {
        super(priority);
        this.game = game;
        sortedPlayers = new Array<Entity>(false, 16);
    }

    public void addedToEngine(Engine engine) {
        Family family = Family.all(TypeComponent.Player.class).get();
        ImmutableArray<Entity> newPlayers  = engine.getEntitiesFor(family);
        sortedPlayers.clear();
        for (Entity player : newPlayers) {
            sortedPlayers.add(player);
        }
        engine.addEntityListener(family, this);
    }

    @Override
    public void entityAdded(Entity entity) {
        sortedPlayers.add(entity);
    }

    @Override
    public void entityRemoved(Entity entity) {
        sortedPlayers.removeValue(entity, true);
    }

    @Override
    public void update(float deltaTime) {
        if (!Gdx.input.isKeyPressed(Input.Keys.TAB)) return;

        sortedPlayers.sort(comparator);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        game.shape.setColor(0, 0, 0, 0.8f);
        game.shape.set(ShapeRenderer.ShapeType.Filled);

        int margin = 50;
        int padding = 20;
        int rowHeight = 20;
        int nameWidth = 80;

        game.shape.rect(margin, margin, Gdx.graphics.getWidth() - margin * 2, Gdx.graphics.getHeight() - margin * 2);

        game.shape.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        game.batch.begin();

        game.font.setColor(Color.WHITE);

        game.font.draw(game.batch, "Player",
                margin + padding,
                Gdx.graphics.getHeight() - (margin + rowHeight));
        game.font.draw(game.batch, "Hits",
                margin + padding + nameWidth,
                Gdx.graphics.getHeight() - (margin + rowHeight));
        int rowIndex = 2;
        for (Entity player : sortedPlayers) {
            StatsComponent stats = StatsComponent.MAPPER.get(player);
            NameComponent name = NameComponent.MAPPER.get(player);

            if (MyPlayerComponent.MAPPER.has(player))
                game.font.setColor(Color.SKY);
            else
                game.font.setColor(Color.WHITE);

            game.font.draw(game.batch, name.str,
                    margin + padding,
                    Gdx.graphics.getHeight() - (margin + rowHeight*rowIndex));

            game.font.draw(game.batch, String.valueOf(stats.hits),
                    margin + padding + nameWidth,
                    Gdx.graphics.getHeight() - (margin + rowHeight*rowIndex));
            rowIndex++;
        }

        game.batch.end();
        game.shape.begin();
    }
}
