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

    public static BufferedImage[] zombieDownImage;
    public static BufferedImage[] zombieUpImage;
    public static BufferedImage[] zombieRightImage;
    public static BufferedImage[] zombieLeftImage;

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

    public static void loadAllRes(int unitSize) {
        loadHeartImage(unitSize);

        loadPlayerImage(unitSize);

        slimeImage = loadResource("/image/animal/slime/", unitSize, 2);
        ghostImage = loadResource("/image/animal/ghost/", unitSize, 2);
        loadZombieImage(unitSize);

        loadTileImage(unitSize);
    }

    private static void loadZombieImage(int unitSize) {
        zombieDownImage = loadResource("/image/animal/zombie/down", unitSize, 3);
        zombieUpImage = loadResource("/image/animal/zombie/up", unitSize, 3);
        zombieRightImage = loadResource("/image/animal/zombie/right", unitSize, 4);
        zombieLeftImage = loadResource("/image/animal/zombie/left", unitSize, 4);
    }

    private static void loadHeartImage(int unitSize) {
        emptyHeartImage = loadResource("/image/hud/heart/empty_heart", unitSize);
        fullHeartImage = loadResource("/image/hud/heart/full_heart", unitSize);
        halfHeartImage = loadResource("/image/hud/heart/half_heart", unitSize);
    }

    private static void loadPlayerImage(int unitSize) {
        downImage = loadResource("/image/player/walking/down", unitSize, 3);
        upImage = loadResource("/image/player/walking/up", unitSize, 3);
        rightImage = loadResource("/image/player/walking/right", unitSize, 4);
        leftImage = loadResource("/image/player/walking/left", unitSize, 4);

        downSwimmingImage = loadResource("/image/player/swim/swim_down", unitSize);
        upSwimmingImage = loadResource("/image/player/swim/swim_up", unitSize);
        rightSwimmingImage = loadResource("/image/player/swim/swim_right", unitSize);
        leftSwimmingImage = loadResource("/image/player/swim/swim_left", unitSize);
    }

    private static void loadTileImage(int unitSize) {
        for(String name : tilesName) {
            BufferedImage image = scaleImage(TextureLoader.loadImage("/image/tiles/" + name + ".png"), unitSize);
            tileMap.put(name, image);
        }
    }

    private static BufferedImage[] loadResource(String path, int unitSize, int numOfSprite) {
        BufferedImage[] bf = new BufferedImage[numOfSprite];
        for(int i = 0; i < numOfSprite; i++) {
            bf[i] = TextureLoader.loadImage(path + (i+1) + ".png");
            bf[i] = scaleImage(bf[i], unitSize);
        }
        return bf;
    }

    private static BufferedImage loadResource(String path, int unitSize) {
        BufferedImage bf = TextureLoader.loadImage(path + ".png");
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
