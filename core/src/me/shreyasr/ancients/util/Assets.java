package me.shreyasr.ancients.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public enum Assets {

    PLAYER      ("player.png"),
    SWORD_SLASH ("sword_slash.png"),
    AXE_CLEAVE  ("axe_cleave.png");

    private final String file;

    public String get() {
        return file;
    }

    Assets(String file) {
        this.file = file;
    }

    public static void loadAll(AssetManager assetManager) {
        for (Assets asset : Assets.values()) {
            assetManager.load(asset.get(), Texture.class);
        }
    }
}
