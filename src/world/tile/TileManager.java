package world.tile;

import java.awt.Graphics;
import java.awt.Color;

import gfx.Renderer;

import world.gen.LevelGenerator;

public class TileManager {

    protected Renderer render;

    protected int maxRow, maxCol;

    private double[][] numberWorldTile;
    protected Tile[][] worldTiles;

    public TileManager(Renderer render) {
        this.render = render;
        
        // maxRow = render.getWidth()/render.getUnitSize();
        // maxCol = render.getHeight()/render.getUnitSize();
        
        maxRow = render.getWidth();
        maxCol = render.getHeight();

        numberWorldTile = new double[maxRow][maxCol];
        worldTiles = new Tile[maxRow][maxCol];
        LevelGenerator.noiseGenerator(numberWorldTile);
        loadAllTexture();
    }

    public Tile[][] getWorldTiles() {
        return worldTiles;
    }
    
    public int getMaxRow() {
        return maxRow;
    }

    public int getMaxCol() {
        return maxCol;
    }

    public Tile getTile(int x, int y) {
        if(
            x >= maxRow || x < 0 ||
            y >= maxCol || y < 0) return null;
            
        return worldTiles[x][y];
    }

    public void loadAllTexture() {
        for(int i = 0; i < worldTiles.length; i++) {
            for(int j = 0; j < worldTiles[i].length; j++) {
                double value = numberWorldTile[i][j];
                if(value < 0.01) {
                    boolean isAnimated = Math.random() < 0.2;
                    worldTiles[i][j] = isAnimated ? 
                                        new AnimatedTile(render, i, j, "water", 2, false, new Color(37,142,206)) :
                                        new Tile(render, i, j, "water", false);
                }
                else if(value < 0.2) {
                    worldTiles[i][j] = new Tile(render, i, j, "sand", false);
                }
                else if(value < 0.3 || value < 0.5) {
                    worldTiles[i][j] = new Tile(render, i, j, "grass", false);
                }
                else {
                    worldTiles[i][j] = new Tile(render, i, j, "tree", true);
                }
            }
        }
    }

    public void drawAllTexture(Graphics g) {

        int xOffset = maxRow*render.getUnitSize() - render.getCamX()*2 - render.getUnitSize();
        int yOffset = maxCol*render.getUnitSize() - render.getCamY()*2 - render.getUnitSize();

        if(-render.getSceneX() >= xOffset) {
            render.setPlayerSceneX(-xOffset);
            g.translate(-xOffset, 0);
        } else if(render.getSceneX() < 0) {
            render.setPlayerSceneX(render.getSceneX());
            g.translate(render.getSceneX(), 0);
        }
        
        if(-render.getSceneY() >= yOffset) {
            render.setPlayerSceneY(-yOffset);
            g.translate(0, -yOffset);
        } else if(render.getSceneY() < 0) {
            render.setPlayerSceneY(render.getSceneY());
            g.translate(0, render.getSceneY());
        }

        for(Tile[] tiles : worldTiles) {
            for(int j = 0; j < tiles.length; j++) {
                if(tiles[j] != null) {
                    if
                    (
                        tiles[j].getX()*render.getUnitSize() >= -render.getPlayerSceneX() - render.getUnitSize() &&
                        tiles[j].getY()*render.getUnitSize() >= -render.getPlayerSceneY() - render.getUnitSize() &&
                        tiles[j].getX()*render.getUnitSize() < render.getWidth() - render.getPlayerSceneX() &&
                        tiles[j].getY()*render.getUnitSize() < render.getHeight() - render.getPlayerSceneY())
                    {
                        tiles[j].draw(g);
                    }
                }
            }
        }
    }
}