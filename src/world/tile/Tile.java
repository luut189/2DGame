package world.tile;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import gfx.Renderer;

import utils.AssetManager;

public class Tile {

    protected Renderer render;

    protected String tileName;
    protected boolean isSolid;

    protected int x, y;

    protected BufferedImage tileImage;

    public Tile(Renderer render, int x, int y, String tileName, boolean isSolid) {
        this.render = render;

        this.tileName = tileName;
        this.isSolid = isSolid;

        this.x = x;
        this.y = y;

        this.tileImage = AssetManager.tileMap.get(tileName);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void draw(Graphics g) {
        g.drawImage(tileImage, x*render.getUnitSize(), y*render.getUnitSize(), null);
    }

    public boolean isSolid() {
        return isSolid;
    }

    public String getTileName() {
        return tileName;
    }

    public void setTileName(String tileName) {
        this.tileName = tileName;
    }

    public BufferedImage getTileImage() {
        return tileImage;
    }

    public void setTileImage(BufferedImage tileImage) {
        this.tileImage = tileImage;
    }
}