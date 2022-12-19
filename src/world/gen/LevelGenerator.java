package world.gen;

import java.util.Random;

public class LevelGenerator {
    
    public static void randomGenerator(int[][] worldTiles) {
        for(int i = 0; i < worldTiles.length; i++) {
            for(int j = 0; j < worldTiles[i].length; j++) {
                worldTiles[i][j] = new Random().nextInt(100);
            }
        }
    }

    public static void noiseGenerator(double[][] worldTiles) {
        double randomZ = Math.random()*10;
        for(int i = 0; i < worldTiles.length; i++) {
            for(int j = 0; j < worldTiles[i].length; j++) {
                worldTiles[i][j] = ImprovedNoise.noise(i*0.1, j*0.1, randomZ);
            }
        }
    }

}
