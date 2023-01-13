package dev.kyzel.game.world.tile;

import java.awt.Graphics;

import dev.kyzel.gfx.Renderer;
import dev.kyzel.utils.AssetManager;

import java.awt.Color;

public class AnimatedTile extends Tile {

    private int tileIndex;
    private int maxTileIndex;

    private int spriteCounter;
    private int maxSpriteCounter = 60;

    private Color tileColor;

    public AnimatedTile(Renderer render, int x, int y, String tileName, int maxTileIndex, boolean isSolid, Color tileColor) {
        super(render, x, y, tileName, isSolid);
        this.tileIndex = 1;
        this.maxTileIndex = maxTileIndex;
        this.spriteCounter = (int) (Math.random() * maxSpriteCounter);
        this.tileColor = tileColor;
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
        if(spriteCounter > maxSpriteCounter) {
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

    public Color getTileColor() {
        return tileColor;
    }
    
}
