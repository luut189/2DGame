package world.tile;

import java.awt.Graphics;
import java.util.Random;

import gfx.Renderer;
import utils.AssetManager;

public class AnimatedTile extends Tile {

    private int tileIndex;
    private int maxTileIndex;

    private int spriteCounter;

    public AnimatedTile(Renderer render, int x, int y, String tileName, int maxTileIndex, boolean isSolid) {
        super(render, x, y, tileName, isSolid);
        this.tileIndex = 1;
        this.maxTileIndex = maxTileIndex;
        this.spriteCounter = new Random().nextInt(60);
    }

    public int increaseTileIndex(int currentIndex, int maxIndex) {
        int newIndex = currentIndex + 1;
        if(newIndex > maxIndex) {
            return 1;
        }
        return newIndex;
    }

    public void updateTileImage() {
        this.tileImage = AssetManager.tileMap.get(tileName + tileIndex);
        if(spriteCounter > 60) {
            tileIndex = increaseTileIndex(tileIndex, maxTileIndex);
            spriteCounter = 0;
        }
        spriteCounter++;    
    }

    @Override
    public void draw(Graphics g) {
        updateTileImage();
        super.draw(g);
    }
    
}
