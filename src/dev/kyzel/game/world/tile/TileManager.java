package dev.kyzel.game.world.tile;

import java.awt.Color;
import java.awt.Graphics;

import dev.kyzel.game.Game;
import dev.kyzel.game.world.gen.LevelGenerator;
import dev.kyzel.gfx.Renderer;

/**
 * A class to manage the tiles in the game.
 */
public class TileManager {

    /**
     * The {@link Renderer} where the all the tiles will be drawn on.
     */
    protected Renderer render;
    
    /**
     * The {@link Game} where the all the tiles' information will write to.
     */
    protected Game game;

    /**
     * The max number of rows of the tiles list.
     */
    protected int maxRow;

    /**
     * The max number of columns of the tiles list.
     */
    protected int maxCol;

    /**
     * The list of number which is used to generate the map.
     */
    private double[][] numberWorldTile;

    /**
     * The list of all tiles.
     */
    protected Tile[][] worldTiles;

    /**
     * Creates a new TileManager.
     * 
     * @param render the {@link Renderer} where the all the tiles will be drawn on
     * @param game the {@link Game} where the all the tiles' information will write to
     */
    public TileManager(Renderer render, Game game) {
        this.render = render;
        this.game = game;
        
        // maxRow = render.getWidth()/render.getUnitSize();
        // maxCol = render.getHeight()/render.getUnitSize();
        
        maxRow = render.getWidth();
        maxCol = render.getHeight();

        numberWorldTile = new double[maxRow][maxCol];
        worldTiles = new Tile[maxRow][maxCol];
        LevelGenerator.noiseGenerator(numberWorldTile);
        loadAllTexture();
    }

    /**
     * Gets a list of all tiles.
     * 
     * @return a list of all tiles
     */
    public Tile[][] getWorldTiles() {
        return worldTiles;
    }
    
    /**
     * Gets the max number of rows of the tiles list.
     * 
     * @return the max number of rows of the tiles list.
     */
    public int getMaxRow() {
        return maxRow;
    }

    /**
     * Gets the max number of columns of the tiles list.
     * 
     * @return the max number of columns of the tiles list.
     */
    public int getMaxCol() {
        return maxCol;
    }

    /**
     * Attempts to get a tile in the tiles list.
     * 
     * @param x the x coordinate of the tile
     * @param y the y coordinate of the tile
     * @return the tile if the given values are in range, null otherwise
     */
    public Tile getTile(int x, int y) {
        if(
            x >= maxRow || x < 0 ||
            y >= maxCol || y < 0) return null;
            
        return worldTiles[x][y];
    }

    /**
     * Loads all tiles into the game.
     */
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
                else if(value < 0.5) {
                    worldTiles[i][j] = new Tile(render, i, j, "grass", false);
                }
                else {
                    worldTiles[i][j] = new Tile(render, i, j, "tree", true);
                }
            }
        }
    }

    /**
     * Draws all the visible tiles.
     * 
     * @param g the {@link Graphics} which is used to draw
     */
    public void drawAllTexture(Graphics g) {

        int xOffset = maxRow*render.getUnitSize() - game.getCamX()*2 - render.getUnitSize();
        int yOffset = maxCol*render.getUnitSize() - game.getCamY()*2 - render.getUnitSize();

        if(-game.getSceneX() >= xOffset) {
            game.setPlayerSceneX(-xOffset);
            g.translate(-xOffset, 0);
        } else if(game.getSceneX() < 0) {
            game.setPlayerSceneX(game.getSceneX());
            g.translate(game.getSceneX(), 0);
        }
        
        if(-game.getSceneY() >= yOffset) {
            game.setPlayerSceneY(-yOffset);
            g.translate(0, -yOffset);
        } else if(game.getSceneY() < 0) {
            game.setPlayerSceneY(game.getSceneY());
            g.translate(0, game.getSceneY());
        }

        for(Tile[] tilesList : worldTiles) {
            for(Tile tile : tilesList) {
                if(tile != null) {
                    if
                    (
                        tile.getX() * render.getUnitSize() >= -game.getPlayerSceneX() - render.getUnitSize() &&
                        tile.getY() * render.getUnitSize() >= -game.getPlayerSceneY() - render.getUnitSize() &&
                        tile.getX() * render.getUnitSize() < render.getWidth() - game.getPlayerSceneX() &&
                        tile.getY() * render.getUnitSize() < render.getHeight() - game.getPlayerSceneY()
                    ) {
                        tile.draw(g);
                    }
                }
            }
        }
    }
}