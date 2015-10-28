package me.shreyasr.ancients.systems.update.player;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import me.shreyasr.ancients.components.PositionComponent;
import me.shreyasr.ancients.components.SquareDirectionComponent;
import me.shreyasr.ancients.components.VelocityComponent;
import me.shreyasr.ancients.components.player.MyPlayerComponent;

public class MyPlayerMovementSystem extends IteratingSystem implements InputProcessor {

    public MyPlayerMovementSystem(int priority) {
        super(
                Family.all(MyPlayerComponent.class,
                           PositionComponent.class,
                           VelocityComponent.class,
                           SquareDirectionComponent.class)
                        .get(),
                priority);
    }

    boolean pressedW = false;
    boolean pressedA = false;
    boolean pressedS = false;
    boolean pressedD = false;

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        SquareDirectionComponent dir = SquareDirectionComponent.MAPPER.get(entity);
        VelocityComponent vel = VelocityComponent.MAPPER.get(entity);

        vel.dx = 0;
        vel.dy = 0;

        if (pressedD) {
            vel.dx += 1;
        }

        if (pressedW) {
            vel.dy += 1;
        }

        if (pressedA) {
            vel.dx -= 1;
        }

        if (pressedS) {
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

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.W: pressedW = true; return true;
            case Input.Keys.A: pressedA = true; return true;
            case Input.Keys.S: pressedS = true; return true;
            case Input.Keys.D: pressedD = true; return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.W: pressedW = false; return true;
            case Input.Keys.A: pressedA = false; return true;
            case Input.Keys.S: pressedS = false; return true;
            case Input.Keys.D: pressedD = false; return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}