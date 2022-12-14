package world.tile;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import gfx.Renderer;

public class Map {

    private Renderer render;
    private Tile[][] worldTiles;
    private int maxRow, maxCol;

    BufferedImage worldMap;

    private int mapSize;

    public Map(Renderer render, TileManager tileManager, int mapSize) {
        this.render = render;
        this.worldTiles = tileManager.getWorldTiles();
        this.maxRow = tileManager.getMaxRow();
        this.maxCol = tileManager.getMaxCol();
        this.mapSize = mapSize;
    }

    public int getMapSize() {
        return mapSize;
    }

    public void setMapSize(int mapSize) {
        this.mapSize = mapSize;
    }

    public void createWorldMap() {
        int worldMapWidth = mapSize*render.getUnitSize();
        int worldMapHeight = mapSize*render.getUnitSize();
        worldMap = new BufferedImage(worldMapWidth, worldMapHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g = worldMap.createGraphics();
        int row = 0;
        int col = 0;
        while(row < maxRow && col < maxCol) {
            int x = row*render.getUnitSize();
            int y = col*render.getUnitSize();
            int translateX = -render.getCamX()+render.getSceneX()+worldMapWidth/2;
            int translateY = -render.getCamY()+render.getSceneY()+worldMapHeight/2;
            if(
                x >= -translateX-render.getUnitSize() &&
                y >= -translateY-render.getUnitSize() &&
                x < worldMapWidth-translateX &&
                y < worldMapHeight-translateY
            )
            {
                g.translate(translateX, translateY);
                if(!(worldTiles[row][col] instanceof AnimatedTile)) {
                    g.drawImage(worldTiles[row][col].getTileImage(), x, y, render.getUnitSize(), render.getUnitSize(), null);
                } else {
                    g.setColor(((AnimatedTile) worldTiles[row][col]).getTileColor());
                    g.fillRect(x, y, render.getUnitSize(), render.getUnitSize());
                }
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
        int width = 200;
        int height = 200;
        int x = render.getWidth() - width - 50;
        int y = 50;

        g.fillRect(x-5, y-5, width+10, height+10);

        g.drawImage(worldMap, x, y, width, height, null);
        
        double scale = (double) (render.getUnitSize() * maxRow)/width;
        int playerX = (int) ((-render.getCamX())/scale) + width/2;
        int playerY = (int) ((-render.getCamY())/scale) + height/2;
        int playerSize = width/mapSize;
        g.translate(x, y);
        g.drawImage(render.getPlayer().getPlayerImage(), playerX, playerY, playerSize, playerSize, null);

    }
}