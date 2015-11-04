package me.shreyasr.ancients.util;

import com.badlogic.gdx.InputProcessor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AccumulatingKeyboardProcessor implements InputProcessor {


    private final Set<Integer> keycodes;
    private boolean[] pressed = new boolean[256];
    private boolean[] justPressed = new boolean[256];

    public boolean isKeyPressed(int keycode) {
        return pressed[keycode];
    }

    public boolean isKeyJustPressed(int keycode) {
        return justPressed[keycode];
    }

    public AccumulatingKeyboardProcessor(Integer... keys) {
        keycodes = new HashSet<Integer>(Arrays.asList(keys));
    }

    @Override
    public boolean keyDown(int keycode) {
        if (!keycodes.contains(keycode)) return false;
        justPressed[keycode] = !pressed[keycode];
        pressed[keycode] = true;
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (!keycodes.contains(keycode)) return false;
        pressed[keycode] = false;
        justPressed[keycode] = false;
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