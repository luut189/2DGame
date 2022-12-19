package world.gen;

import java.util.Random;

public class LevelGenerator {

    public static double seedValue = Math.random()*10;
    
    public static void randomGenerator(int[][] worldTiles) {
        for(int i = 0; i < worldTiles.length; i++) {
            for(int j = 0; j < worldTiles[i].length; j++) {
                worldTiles[i][j] = new Random().nextInt(100);
            }
        }
    }

    public static void noiseGenerator(double[][] worldTiles) {
        for(int i = 0; i < worldTiles.length; i++) {
            for(int j = 0; j < worldTiles[i].length; j++) {
                worldTiles[i][j] = ImprovedNoise.noise(i*0.1, j*0.1, seedValue);
            }
        }
    }

    public static void setSeedValue(double assignedSeedValue) {
        seedValue = assignedSeedValue;
    }

}
