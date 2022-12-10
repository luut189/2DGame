package world.tile;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import gfx.Renderer;

public class Map {

    private Renderer render;
    private Tile[][] worldTiles;
    private int maxRow, maxCol;

    BufferedImage worldMap;

    public Map(Renderer render, TileManager tileManager) {
        this.render = render;
        this.worldTiles = tileManager.getWorldTiles();
        this.maxRow = tileManager.getMaxRow();
        this.maxCol = tileManager.getMaxCol();
    }

    public void createWorldMap() {
        int worldMapWidth = 10*render.getUnitSize();
        int worldMapHeight = 10*render.getUnitSize();
        worldMap = new BufferedImage(worldMapWidth, worldMapHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g = worldMap.createGraphics();
        int row = 0;
        int col = 0;
        while(row < maxRow && col < maxCol) {
            int x = row*render.getUnitSize();
            int y = col*render.getUnitSize();
            int translateX = render.getSceneX()-worldMapWidth/4;
            int translateY = render.getSceneY()-worldMapHeight/12;
            if(
                x < worldMapWidth - translateX &&
                y < worldMapHeight - translateY)
            {
                g.translate(translateX, translateY);
                g.drawImage(worldTiles[row][col].getTileImage(), x, y, render.getUnitSize(), render.getUnitSize(), null);
                g.translate(-translateX, -translateY);
            }
            col++;
            if(col == maxCol) {
                col = 0;
                row++;
            }
        }
        g.dispose();
    }

    public void drawMinimap(Graphics g) {
        createWorldMap();
        int width = 100;
        int height = 100;
        int x = render.getWidth() - width - 50;
        int y = 50;

        g.fillRect(x-5, y-5, width+10, height+10);

        g.drawImage(worldMap, x, y, width, height, null);
        
        double scale = (double) (render.getUnitSize() * maxRow)/width;
        int playerX = (int) ((-render.getCamX())/scale) + width/2;
        int playerY = (int) ((-render.getCamY())/scale) + height/2;
        g.translate(x, y);
        g.drawImage(render.getPlayer().getPlayerImage(), playerX+4, playerY, 10, 10, null);

    }
}