package dev.kyzel.game.world.gen;

import java.util.Random;

/**
 * A class used to generate the level/world for the game.
 */
public class LevelGenerator {

    /**
     * The seed value which would later be used to generate the map for the game.
     */
    public static double seedValue = (Math.random()*100)*0.1;
    
    /**
     * Generates a random noise map.
     * 
     * @param worldTiles the array of number, which contains the output
     */
    public static void randomGenerator(int[][] worldTiles) {
        for(int i = 0; i < worldTiles.length; i++) {
            for(int j = 0; j < worldTiles[i].length; j++) {
                worldTiles[i][j] = new Random().nextInt(100);
            }
        }
    }

    /**
     * Generates an improved noise map, which used the Improved Noise algorithm.
     * 
     * @param worldTiles the array of number, which contains the output
     */
    public static void noiseGenerator(double[][] worldTiles) {
        for(int i = 0; i < worldTiles.length; i++) {
            for(int j = 0; j < worldTiles[i].length; j++) {
                worldTiles[i][j] = ImprovedNoise.noise(i*0.1, j*0.1, seedValue);
            }
        }
    }

    /**
     * Set the seed value to the assigned value.
     * 
     * @param assignedSeedValue the assigned seed value
     */
    public static void setSeedValue(double assignedSeedValue) {
        seedValue = assignedSeedValue;
    }

}
