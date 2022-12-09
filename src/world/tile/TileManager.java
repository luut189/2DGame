package world.tile;

import java.awt.Graphics;

import gfx.Renderer;
import world.gen.WorldGenerator;

public class TileManager {

    private Renderer render;

    private int maxRow, maxCol;

    private int[][] numberWorldTile;
    private Tile[][] worldTiles;

    public TileManager(Renderer render) {
        this.render = render;
        
        // maxRow = render.getWidth()/render.getUnitSize();
        // maxCol = render.getHeight()/render.getUnitSize();
        
        maxRow = 1000;
        maxCol = 1000;

        numberWorldTile = new int[maxRow][maxCol];
        worldTiles = new Tile[maxRow][maxCol];
        WorldGenerator.randomGenerator(numberWorldTile);

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
                if(numberWorldTile[i][j] < 10) {
                    worldTiles[i][j] = new Tile(render, i, j, "tree", true);
                } else {
                    worldTiles[i][j] = numberWorldTile[i][j] < 50 ? new Tile(render, i, j, "grass", false) : new Tile(render, i, j, "sand", false);
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
