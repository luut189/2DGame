package dev.kyzel.utils;

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

    public static BufferedImage[] slimeImage;
    public static BufferedImage[] ghostImage;

    public static BufferedImage emptyHeartImage;
    public static BufferedImage fullHeartImage;
    public static BufferedImage halfHeartImage;

    private static final String[] tilesName = {
        "grass",
        "sand",
        "tree",
        "water",
        "water1",
        "water2"
    };

    public static void loadAllRes(TextureLoader loader, int unitSize) {
        loadHeartImage(loader, unitSize);

        loadPlayerImage(loader, unitSize);

        slimeImage = loadResource(loader, "/image/animal/slime", unitSize, 2);
        ghostImage = loadResource(loader, "/image/animal/ghost", unitSize, 2);

        loadTileImage(loader, unitSize);
    }

    private static void loadHeartImage(TextureLoader loader, int unitSize) {
        emptyHeartImage = loadResource(loader, "/image/hud/heart/empty_heart", unitSize);
        fullHeartImage = loadResource(loader, "/image/hud/heart/full_heart", unitSize);
        halfHeartImage = loadResource(loader, "/image/hud/heart/half_heart", unitSize);
    }

    private static void loadPlayerImage(TextureLoader loader, int unitSize) {
        downImage = loadResource(loader, "/image/player/walking/down", unitSize, 3);
        upImage = loadResource(loader, "/image/player/walking/up", unitSize, 3);
        rightImage = loadResource(loader, "/image/player/walking/right", unitSize, 4);
        leftImage = loadResource(loader, "/image/player/walking/left", unitSize, 4);

        downSwimmingImage = loadResource(loader, "/image/player/swim/swim_down", unitSize);
        upSwimmingImage = loadResource(loader, "/image/player/swim/swim_up", unitSize);
        rightSwimmingImage = loadResource(loader, "/image/player/swim/swim_right", unitSize);
        leftSwimmingImage = loadResource(loader, "/image/player/swim/swim_left", unitSize);
    }

    private static void loadTileImage(TextureLoader loader, int unitSize) {
        for(String name : tilesName) {
            BufferedImage image = scaleImage(loader.loadImage("/image/tiles/" + name + ".png"), unitSize);
            tileMap.put(name, image);
        }
    }

    private static BufferedImage[] loadResource(TextureLoader loader, String path, int unitSize, int numOfSprite) {
        BufferedImage[] bf = new BufferedImage[numOfSprite];
        for(int i = 0; i < numOfSprite; i++) {
            bf[i] = loader.loadImage(path + (i+1) + ".png");
            bf[i] = scaleImage(bf[i], unitSize);
        }
        return bf;
    }

    private static BufferedImage loadResource(TextureLoader loader, String path, int unitSize) {
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
