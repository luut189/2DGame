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

    public void loadAllTexture() {
        for(int i = 0; i < worldTiles.length; i++) {
            for(int j = 0; j < worldTiles[i].length; j++) {
                double value = numberWorldTile[i][j];
                if(value < 0.01) {
                    worldTiles[i][j] = new AnimatedTile(render, i, j, "water", 2, false, new Color(0, 128, 255));
                }
                else if(value < 0.1) {
                    worldTiles[i][j] = new Tile(render, i, j, "sand", false);
                }
                else if(value < 0.2 || value < 0.4) {
                    worldTiles[i][j] = new Tile(render, i, j, "grass", false);
                }
                else {
                    worldTiles[i][j] = new Tile(render, i, j, "tree", true);
                }
            }
        }
    }

    public void drawAllTexture(Graphics g) {
        g.translate(render.getSceneX(), render.getSceneY());
        for(Tile[] i : worldTiles) {
            for(int j = 0; j < i.length; j++) {
                if(i[j] != null) {
                    if
                    (
                        i[j].getX()*render.getUnitSize() >= -render.getSceneX()-render.getUnitSize() &&
                        i[j].getY()*render.getUnitSize() >= -render.getSceneY()-render.getUnitSize() &&
                        i[j].getX()*render.getUnitSize() < render.getWidth() - render.getSceneX() &&
                        i[j].getY()*render.getUnitSize() < render.getHeight() - render.getSceneY())
                    {
                        i[j].draw(g);
                    }
                }
            }
        }
    }
}