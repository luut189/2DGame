package dev.kyzel.game.world.tile;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import dev.kyzel.game.ControlHandler;
import dev.kyzel.game.Game;
import dev.kyzel.gfx.Renderer;

/**
 * A class to handle minimap.
 */
public class Minimap {

    /**
     * The {@link Renderer} where the minimap will be drawn on.
     */
    private Renderer render;

    /**
     * The {@link Game} where the minimap will take information from.
     */
    private Game game;

    /**
     * The list of all tiles in the game.
     */
    private Tile[][] worldTiles;

    /**
     * The max number of rows of the tiles list.
     */
    private int maxRow;
    
    /**
     * The max number of columns of tiles list.
     */
    private int maxCol;

    /**
     * The output minimap.
     */
    BufferedImage worldMap;

    /**
     * The minimap's size.
     */
    private int mapSize;

    /**
     * Creates a new minimap.
     * 
     * @param render the {@link Renderer} where the minimap will be drawn on
     * @param game {@link Game} where the minimap will take information from
     * @param mapSize the minimap's size
     */
    public Minimap(Renderer render, Game game, int mapSize) {
        this.render = render;
        this.game = game;
        this.worldTiles = game.getTileManager().getWorldTiles();
        this.maxRow = game.getTileManager().getMaxRow();
        this.maxCol = game.getTileManager().getMaxCol();
        this.mapSize = mapSize;
    }

    /**
     * Gets the minimap's size.
     * 
     * @return the minimap's size
     */
    public int getMapSize() {
        return mapSize;
    }

    /**
     * Set the minimap's size to the given value.
     * 
     * @param mapSize the given value
     */
    public void setMapSize(int mapSize) {
        this.mapSize = mapSize;
    }

    /**
     * Updates the minimap.
     */
    public void update() {
        if(ControlHandler.ZOOM_OUT_MINIMAP.down() && mapSize < 30) {
            mapSize++;
        } else if(ControlHandler.ZOOM_IN_MINIMAP.down() && mapSize > 10) {
            mapSize--;
        }
    }

    /**
     * Creates the minimap.
     */
    public void createMinimap() {
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

    /**
     * Draws the minimap.
     * 
     * @param g the {@link Graphics} which is used to draw
     */
    public void draw(Graphics g) {
        createMinimap();
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