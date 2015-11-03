package me.shreyasr.ancients.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public enum Assets {

    PLAYER       (Texture.class, "player.png"),
    SWORD_SLASH  (Texture.class, "sword_slash.png"),
    DAGGER_SLASH (Texture.class, "dagger_slash.png"),
    AXE_CLEAVE   (Texture.class, "axe_cleave.png"),
    SPEAR_STAB   (Texture.class, "spear_stab.png"),

    MAP (TiledMap.class, "maps/map.tmx");

    private final Class type;
    private final String file;

    public String get() {
        return file;
    }

    Assets(Class type, String file) {
        this.type = type;
        this.file = file;
    }

    public static void loadAll(AssetManager assetManager) {
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        for (Assets asset : Assets.values()) {
            assetManager.load(asset.get(), asset.type);
        }
    }
}
