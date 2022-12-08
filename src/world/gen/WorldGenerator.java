package world.gen;

import java.util.Random;

public class WorldGenerator {
    

    public static void randomGenerator(int[][] worldTiles) {
        for(int i = 0; i < worldTiles.length; i++) {
            for(int j = 0; j < worldTiles[i].length; j++) {
                worldTiles[i][j] = new Random().nextInt(100);
            }
        }
    }

}
