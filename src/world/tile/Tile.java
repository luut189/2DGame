package world.tile;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import gfx.Renderer;
import utils.AssetManager;

public class Tile {

    private Renderer render;

    private String tileName;
    private boolean isSolid;

    private int x, y;

    private BufferedImage tileImage;

    public Tile(Renderer render, int x, int y, String tileName, boolean isSolid) {
        this.render = render;

        this.tileName = tileName;
        this.isSolid = isSolid;

        this.x = x;
        this.y = y;

        loadImage();
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
        g.drawImage(tileImage, x*render.getUnitSize(), y*render.getUnitSize(), render.getUnitSize(), render.getUnitSize(), null);
    }

    public void loadImage() {
        tileImage = AssetManager.tileMap.get(tileName);
    }

    public boolean isSolid() {
        return isSolid;
    }

    public void setSolid(boolean isSolid) {
        this.isSolid = isSolid;
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