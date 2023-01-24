package dev.kyzel.utils;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class AssetManager {

    /**
     * The list of all tiles that can be used in the game.
     */
    public static HashMap<String, BufferedImage> tileMap = new HashMap<>();

    /**
     * The list of player's sprites when moving down.
     */
    public static BufferedImage[] downImage;
    
    /**
     * The list of player's sprites when moving up.
     */
    public static BufferedImage[] upImage;
    
    /**
     * The list of player's sprites when moving to the right.
     */
    public static BufferedImage[] rightImage;
    
    /**
     * The list of player's sprites when moving to the left.
     */
    public static BufferedImage[] leftImage;

    
    /**
     * The player's sprite when swimming to down.
     */
    public static BufferedImage downSwimmingImage;
    
    /**
     * The player's sprite when swimming to up.
     */
    public static BufferedImage upSwimmingImage;
    
    /**
     * The player's sprite when swimming to the right.
     */
    public static BufferedImage rightSwimmingImage;
    
    /**
     * The player's sprite when swimming to the left.
     */
    public static BufferedImage leftSwimmingImage;


    /**
     * The list of slime's sprites.
     */
    public static BufferedImage[] slimeImage;

    /**
     * The list of ghost's sprites.
     */
    public static BufferedImage[] ghostImage;


    /**
     * The list of zombie's sprites when moving down.
     */
    public static BufferedImage[] zombieDownImage;

    /**
     * The list of zombie's sprites when moving up.
     */
    public static BufferedImage[] zombieUpImage;

    /**
     * The list of zombie's sprites when moving to the right.
     */
    public static BufferedImage[] zombieRightImage;

    /**
     * The list of zombie's sprites when moving to the left.
     */
    public static BufferedImage[] zombieLeftImage;

    
    /**
     * An empty heart image that is used for the HUD.
     */
    public static BufferedImage emptyHeartImage;

    /**
     * An full heart image that is used for the HUD.
     */
    public static BufferedImage fullHeartImage;

    /**
     * An half-empty heart image that is used for the HUD.
     */
    public static BufferedImage halfHeartImage;

    /**
     * The list of all the tiles' name
     */
    private static final String[] tilesName = {
        "grass",
        "sand",
        "tree",
        "water",
        "water1",
        "water2"
    };

    /**
     * Loads all resources for the game.
     * 
     * @param unitSize the unit size of the {@link Renderer}
     */
    public static void loadAllRes(int unitSize) {
        loadHeartImage(unitSize);

        loadPlayerImage(unitSize);

        slimeImage = loadResource("/image/animal/slime/", unitSize, 2);
        ghostImage = loadResource("/image/animal/ghost/", unitSize, 2);
        loadZombieImage(unitSize);

        loadTileImage(unitSize);
    }

    /**
     * Loads all zombie's sprites.
     * 
     * @param unitSize the unit size of the {@link Renderer}
     */
    private static void loadZombieImage(int unitSize) {
        zombieDownImage = loadResource("/image/animal/zombie/down", unitSize, 3);
        zombieUpImage = loadResource("/image/animal/zombie/up", unitSize, 3);
        zombieRightImage = loadResource("/image/animal/zombie/right", unitSize, 4);
        zombieLeftImage = loadResource("/image/animal/zombie/left", unitSize, 4);
    }

    /**
     * Loads all of the heart's images.
     * 
     * @param unitSize the unit size of the {@link Renderer}
     */
    private static void loadHeartImage(int unitSize) {
        emptyHeartImage = loadResource("/image/hud/heart/empty_heart", unitSize);
        fullHeartImage = loadResource("/image/hud/heart/full_heart", unitSize);
        halfHeartImage = loadResource("/image/hud/heart/half_heart", unitSize);
    }

    /**
     * Loads all player's sprites.
     * 
     * @param unitSize the unit size of the {@link Renderer}
     */
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

    /**
     * Loads all tiles' textures.
     * 
     * @param unitSize the unit size of the {@link Renderer}
     */
    private static void loadTileImage(int unitSize) {
        for(String name : tilesName) {
            BufferedImage image = scaleImage(TextureLoader.loadImage("/image/tiles/" + name + ".png"), unitSize);
            tileMap.put(name, image);
        }
    }

    /**
     * Loads a certain number of resources into an array.
     * 
     * @param path the path of the resources
     * @param unitSize the unit size of the {@link Renderer}
     * @param numOfSprite the number of sprites to load
     * @return the array of loaded resources
     */
    private static BufferedImage[] loadResource(String path, int unitSize, int numOfSprite) {
        BufferedImage[] bf = new BufferedImage[numOfSprite];
        for(int i = 0; i < numOfSprite; i++) {
            bf[i] = TextureLoader.loadImage(path + (i+1) + ".png");
            bf[i] = scaleImage(bf[i], unitSize);
        }
        return bf;
    }

    /**
     * Loads and scales a resource file.
     * 
     * @param path the path of the resource
     * @param unitSize the unit size of the {@link Renderer}
     * @return
     */
    private static BufferedImage loadResource(String path, int unitSize) {
        BufferedImage bf = TextureLoader.loadImage(path + ".png");
        bf = scaleImage(bf, unitSize);
        return bf;
    }

    /**
     * Scales the given image into the given unit size.
     * 
     * @param image the given image
     * @param unitSize the given unit size
     * @return the scaled image
     */
    private static BufferedImage scaleImage(BufferedImage image, int unitSize) {
        BufferedImage newImage = new BufferedImage(unitSize, unitSize, image.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(image, 0, 0, unitSize, unitSize, null);
        g.dispose();
        return newImage;
    }
}
