package dev.kyzel.game.world.tile;

import java.awt.Graphics;

import dev.kyzel.game.Game;
import dev.kyzel.gfx.Renderer;
import dev.kyzel.game.world.gen.LevelGenerator;

import java.awt.Color;

public class TileManager {

    protected Renderer render;
    protected Game game;

    protected int maxRow, maxCol;

    private double[][] numberWorldTile;
    protected Tile[][] worldTiles;

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
                else if(value < 0.5) {
                    worldTiles[i][j] = new Tile(render, i, j, "grass", false);
                }
                else {
                    worldTiles[i][j] = new Tile(render, i, j, "tree", true);
                }
            }
        }
    }

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