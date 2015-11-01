package me.shreyasr.ancients.util;

import com.badlogic.gdx.InputProcessor;

public class AccumulatingKeyboardProcessor implements InputProcessor {

    boolean[] keys = new boolean[256];

    public boolean isKeyPressed(int keycode) {
        return keys[keycode];
    }

    @Override
    public boolean keyDown(int keycode) {
        keys[keycode] = true;
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        keys[keycode] = false;
        return true;
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