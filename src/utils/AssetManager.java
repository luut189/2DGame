package utils;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.util.HashMap;

public class AssetManager {

    public static HashMap<String, BufferedImage> tileMap = new HashMap<>();

    public static BufferedImage[] downImage;
    public static BufferedImage[] upImage;
    public static BufferedImage[] rightImage;
    public static BufferedImage[] leftImage;

    public static BufferedImage downSwimmingImage;
    public static BufferedImage upSwimmingImage;
    public static BufferedImage rightSwimmingImage;
    public static BufferedImage leftSwimmingImage;

    private static final String[] tilesName = {
        "grass",
        "sand",
        "tree",
        "water",
        "water1",
        "water2"
    };

    public static void loadAllRes(TextureLoader loader, int unitSize) {
        downImage = loadPlayerImage(loader, "/player/walking/down", 3, unitSize);
        upImage = loadPlayerImage(loader, "/player/walking/up", 3, unitSize);
        rightImage = loadPlayerImage(loader, "/player/walking/right", 4, unitSize);
        leftImage = loadPlayerImage(loader, "/player/walking/left", 4, unitSize);

        downSwimmingImage = loadPlayerImage(loader, "/player/swim/swim_down", unitSize);
        upSwimmingImage = loadPlayerImage(loader, "/player/swim/swim_up", unitSize);
        rightSwimmingImage = loadPlayerImage(loader, "/player/swim/swim_right", unitSize);
        leftSwimmingImage = loadPlayerImage(loader, "/player/swim/swim_left", unitSize);

        loadTileImage(loader, unitSize);
    }

    private static void loadTileImage(TextureLoader loader, int unitSize) {
        for(int i = 0; i < tilesName.length; i++) {
            BufferedImage image = scaleImage(loader.loadImage("/tiles/" + tilesName[i] + ".png"), unitSize);
            tileMap.put(tilesName[i], image);
        }
    }

    private static BufferedImage[] loadPlayerImage(TextureLoader loader, String path, int numOfSprite, int unitSize) {
        BufferedImage[] bf = new BufferedImage[numOfSprite];
        for(int i = 0; i < numOfSprite; i++) {
            bf[i] = loader.loadImage(path + (i+1) + ".png");
            bf[i] = scaleImage(bf[i], unitSize);
        }
        return bf;
    }

    private static BufferedImage loadPlayerImage(TextureLoader loader, String path, int unitSize) {
        BufferedImage bf = loader.loadImage(path + ".png");
        bf = scaleImage(bf, unitSize);
        return bf;
    }

    private static BufferedImage scaleImage(BufferedImage image, int unitSize) {
        BufferedImage newImage = new BufferedImage(unitSize, unitSize, image.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(image, 0, 0, unitSize, unitSize, null);
        g.dispose();
        return newImage;
    }
}
