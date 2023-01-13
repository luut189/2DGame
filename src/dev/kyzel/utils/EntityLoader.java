package dev.kyzel.utils;

import java.util.ArrayList;

import dev.kyzel.game.entity.Entity;
import dev.kyzel.game.entity.animal.Slime;
import dev.kyzel.gfx.Renderer;
import dev.kyzel.game.world.tile.Tile;
import dev.kyzel.game.world.tile.TileManager;

public class EntityLoader {

    public static void loadEntity(ArrayList<Entity> entityList, Renderer render, TileManager tileManager) {
        loadSlime(10000, entityList, render, tileManager);
    }

    private static void loadSlime(int num, ArrayList<Entity> entityList, Renderer render, TileManager tileManager) {
        for(int i = 0; i < num; i++) {

            int x = (int) (Math.random() * tileManager.getMaxRow());
            int y = (int) (Math.random() * tileManager.getMaxCol());
            Tile currentTile = tileManager.getWorldTiles()[x][y];

            while(currentTile.getTileName().equals("water") ||
                  currentTile.isSolid()) {

                x = (int) (Math.random() * tileManager.getMaxRow());
                y = (int) (Math.random() * tileManager.getMaxCol());
                currentTile = tileManager.getWorldTiles()[x][y];
            }
            entityList.add(new Slime(render, x*render.getUnitSize(), y*render.getUnitSize(), 1));
        }
    }
}
