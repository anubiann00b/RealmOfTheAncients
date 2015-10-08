package me.shreyasr.ancients.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import me.shreyasr.ancients.components.PlayerComponent;
import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.components.SquareDirectionComponent;
import me.shreyasr.ancients.components.VelocityComponent;

public class PlayerMovementSystem extends IteratingSystem {

    public PlayerMovementSystem(int priority) {
        super(
                Family.all(PlayerComponent.class,
                           PositionComponent.class,
                           VelocityComponent.class,
                           SquareDirectionComponent.class)
                        .get(),
                priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        SquareDirectionComponent dir = SquareDirectionComponent.MAPPER.get(entity);
        VelocityComponent vel = VelocityComponent.MAPPER.get(entity);

        vel.dx = 0;
        vel.dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            vel.dx += 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            vel.dy += 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            vel.dx -= 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            vel.dy -= 1;
        }

        if (vel.dx > 0) {
            dir.dir = SquareDirectionComponent.Direction.RIGHT;
        }
        if (vel.dx < 0) {
            dir.dir = SquareDirectionComponent.Direction.LEFT;
        }

        if (vel.dy > 0) {
            dir.dir = SquareDirectionComponent.Direction.UP;
        }
        if (vel.dy < 0) {
            dir.dir = SquareDirectionComponent.Direction.DOWN;
        }
    }
}