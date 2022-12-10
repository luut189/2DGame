package utils;

import java.awt.image.BufferedImage;

import java.util.HashMap;

public class AssetManager {

    public static HashMap<String, BufferedImage> tileMap = new HashMap<>();

    private static final String[] tilesName = {
        "grass",
        "sand",
        "tree",
        "water1",
        "water2"
    };

    public static void loadTileImage(TextureLoader loader) {
        for(int i = 0; i < tilesName.length; i++) {
            tileMap.put(tilesName[i], loader.loadImage("/tiles/" + tilesName[i] + ".png"));
        }
    }
}
