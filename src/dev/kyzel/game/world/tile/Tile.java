package dev.kyzel.game.world.tile;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import dev.kyzel.gfx.Renderer;
import dev.kyzel.utils.AssetManager;

/**
 * The tile class.
 */
public class Tile {

    /**
     * The {@link Renderer} where the tile will be drawn on.
     */
    protected Renderer render;

    /**
     * The tile's name.
     */
    protected String tileName;
    
    /**
     * A variable to see if the tile is solid.
     */
    protected boolean isSolid;

    /**
     * The x coordinate of the tile.
     */
    protected int x;

    /**
     * The y coordinate of the tile.
     */
    protected int y;

    /**
     * The texture of the tile.
     */
    protected BufferedImage tileImage;

    /**
     * Creates a new tile.
     * 
     * @param render the {@link Renderer} where the tile will be drawn on
     * @param x the x coordinate of the tile
     * @param y the y coordinate of the tile
     * @param tileName the tile's name
     * @param isSolid is the tile solid
     */
    public Tile(Renderer render, int x, int y, String tileName, boolean isSolid) {
        this.render = render;

        this.tileName = tileName;
        this.isSolid = isSolid;

        this.x = x;
        this.y = y;

        this.tileImage = AssetManager.tileMap.get(tileName);
    }

    /**
     * Gets the x coordinate of the tile.
     * 
     * @return the x coordinate of the tile
     */
    public int getX() {
        return x;
    }

    /**
     * Set the x coordinate of the tile to the given value.
     * 
     * @param x the given value
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the y coordinate of the tile.
     * 
     * @return the y coordinate of the tile
     */
    public int getY() {
        return y;
    }

    /**
     * Set the y coordinate of the tile to the given value.
     * 
     * @param y the given value
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Draws the tile.
     * 
     * @param g the {@link Graphics} which is used to draw
     */
    public void draw(Graphics g) {
        g.drawImage(tileImage, x*render.getUnitSize(), y*render.getUnitSize(), null);
    }

    /**
     * Checks if the tile is solid.
     * 
     * @return if the tile is solid
     */
    public boolean isSolid() {
        return isSolid;
    }

    /**
     * Gets the tile's name.
     * 
     * @return the tile's name
     */
    public String getTileName() {
        return tileName;
    }

    /**
     * Set the tile's name to the given value.
     * 
     * @param tileName the given value
     */
    public void setTileName(String tileName) {
        this.tileName = tileName;
    }

    /**
     * Get the texture of the tile.
     * 
     * @return the texture of the tile
     */
    public BufferedImage getTileImage() {
        return tileImage;
    }

    /**
     * Set the texture of the tile to the given image.
     * 
     * @param tileImage the given image
     */
    public void setTileImage(BufferedImage tileImage) {
        this.tileImage = tileImage;
    }
}