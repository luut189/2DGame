package utils;

import java.util.ArrayList;

import entity.Entity;
import entity.animal.Slime;

import gfx.Renderer;

import world.tile.Tile;
import world.tile.TileManager;

public class EntityLoader {

    public static void loadEntity(ArrayList<Entity> entityList, Renderer render, TileManager tileManager) {
        loadSlime(10000, entityList, render, tileManager);
    }

    private static void loadSlime(int num, ArrayList<Entity> entityList, Renderer render, TileManager tileManager) {
        for(int i = 0; i < num; i++) {

            int x = (int) (Math.random() * tileManager.getMaxRow() * render.getUnitSize());
            int y = (int) (Math.random() * tileManager.getMaxCol() * render.getUnitSize());
            Tile currentTile = tileManager.getWorldTiles()[x/render.getUnitSize()][y/render.getUnitSize()];

            while(currentTile.getTileName().equals("water") ||
                  currentTile.isSolid()) {

                x = (int) (Math.random() * tileManager.getMaxRow() * render.getUnitSize());
                y = (int) (Math.random() * tileManager.getMaxCol() * render.getUnitSize());
                currentTile = tileManager.getWorldTiles()[x/render.getUnitSize()][y/render.getUnitSize()];
            }
            entityList.add(new Slime(render, x, y, 1));
        }
    }
}
