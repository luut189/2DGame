package dev.kyzel.game.world.tile;

import java.awt.Color;
import java.awt.Graphics;

import dev.kyzel.gfx.Renderer;
import dev.kyzel.utils.AssetManager;

/**
 * The Animated Tile class.
 */
public class AnimatedTile extends Tile {

    /**
     * The current index to use as a tile's texture.
     */
    private int tileIndex;

    /**
     * The max index that can be used as a tile's texture.
     */
    private int maxTileIndex;

    /**
     * The sprite counter, which is used for animation.
     */
    private int spriteCounter;

    /**
     * The max value of sprite counter, which will invoke a texture change if the {@link #spriteCounter} reaches this value.
     */
    private int maxSpriteCounter = 60;

    /**
     * The color to used on the {@link Minimap}
     */
    private Color tileColor;

    /**
     * Creates a new animated tile.
     * 
     * @param render the {@link Renderer} where the tile will be drawn on
     * @param x the x coordinate of the tile
     * @param y the y coordinate of the tile
     * @param tileName the tile's name
     * @param maxTileIndex the max index of the tile
     * @param isSolid is the tile solid
     * @param tileColor the color of the tile
     */
    public AnimatedTile(Renderer render, int x, int y, String tileName, int maxTileIndex, boolean isSolid, Color tileColor) {
        super(render, x, y, tileName, isSolid);
        this.tileIndex = 1;
        this.maxTileIndex = maxTileIndex;
        this.spriteCounter = (int) (Math.random() * maxSpriteCounter);
        this.tileColor = tileColor;
    }

    /**
     * Increases the tile index so that it will always be in range.
     * 
     * @param currentIndex the current index
     * @param maxIndex the max index
     * @return the new index
     */
    public int increaseTileIndex(int currentIndex, int maxIndex) {
        int newIndex = currentIndex + 1;
        if(newIndex > maxIndex) {
            return 1;
        }
        return newIndex;
    }

    /**
     * Updates the tile image if the requirement is met.
     */
    public void updateTileImage() {
        this.tileImage = AssetManager.tileMap.get(tileName + tileIndex);
        if(spriteCounter > maxSpriteCounter) {
            tileIndex = increaseTileIndex(tileIndex, maxTileIndex);
            spriteCounter = 0;
        }
        spriteCounter++;    
    }

    /**
     * Updates the tile.
     * {@inheritDoc}
     */
    @Override
    public void draw(Graphics g) {
        updateTileImage();
        super.draw(g);
    }

    /**
     * Gets the tile's color.
     * 
     * @return the tile's color
     */
    public Color getTileColor() {
        return tileColor;
    }
    
}
