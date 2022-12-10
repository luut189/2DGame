package world.tile;

import java.awt.Graphics;

import gfx.Renderer;
import utils.AssetManager;

public class AnimatedTile extends Tile {

    private int tileIndex;
    private int spriteCounter;

    public AnimatedTile(Renderer render, int x, int y, String tileName, boolean isSolid) {
        super(render, x, y, tileName, isSolid);
        this.tileIndex = 1;
        this.spriteCounter = 0;
    }

    public void updateTileImage() {
        this.tileImage = AssetManager.tileMap.get(tileName + tileIndex);
        if(spriteCounter > 30) {
            if(tileIndex < 2) {
                tileIndex++;
            } else {
                tileIndex--;
            }
            spriteCounter = 0;
        }
        spriteCounter++;
    }

    @Override
    public void draw(Graphics g) {
        updateTileImage();
        g.drawImage(tileImage, x*render.getUnitSize(), y*render.getUnitSize(), render.getUnitSize(), render.getUnitSize(), null);
    }
    
}
