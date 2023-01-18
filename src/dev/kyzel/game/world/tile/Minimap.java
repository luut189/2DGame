package dev.kyzel.game.world.tile;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import dev.kyzel.game.Game;
import dev.kyzel.game.KeyHandler;
import dev.kyzel.gfx.Renderer;

public class Minimap {

    private Renderer render;
    private Game game;

    private Tile[][] worldTiles;
    private int maxRow, maxCol;

    BufferedImage worldMap;

    private int mapSize;

    public Minimap(Renderer render, Game game, TileManager tileManager, int mapSize) {
        this.render = render;
        this.game = game;
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

    public void update(KeyHandler keyHandler) {
        if(keyHandler.isZooming()) {
            setMapSize(keyHandler.getZoomDist());
        }
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
            int translateX = -game.getCamX()+game.getSceneX()+worldMapWidth/2;
            int translateY = -game.getCamY()+game.getSceneY()+worldMapHeight/2;
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

    public void draw(Graphics g) {
        createWorldMap();
        int width = 200;
        int height = 200;
        int x = render.getWidth() - width - 50;
        int y = 50;

        g.setColor(Color.DARK_GRAY);
        g.fillRect(x-5, y-5, width+10, height+10);

        g.drawImage(worldMap, x, y, width, height, null);
        
        double scale = (double) (render.getUnitSize() * maxRow)/maxRow*render.getUnitSize();
        int playerX = (int) ((-game.getCamX())/scale) + width/2;
        int playerY = (int) ((-game.getCamY())/scale) + height/2;
        int playerSize = width/mapSize;
        g.translate(x, y);
        g.drawImage(game.getPlayer().getPlayerImage(), playerX, playerY, playerSize, playerSize, null);
        g.translate(-x, -y);
    }
}