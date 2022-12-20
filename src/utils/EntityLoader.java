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
            int x = (int) (Math.random()*render.getWidth()*render.getUnitSize())-render.getUnitSize();
            int y = (int) (Math.random()*render.getHeight()*render.getUnitSize())-render.getUnitSize();
            Tile currentTile = tileManager.getWorldTiles()[(x+render.getUnitSize()/2)/render.getUnitSize()][(y+render.getUnitSize())/render.getUnitSize()];

            while(currentTile.getTileName().equals("water") ||
                  currentTile.isSolid()) {
                x = (int) (Math.random()*render.getWidth()*render.getUnitSize())-render.getUnitSize();
                y = (int) (Math.random()*render.getHeight()*render.getUnitSize())-render.getUnitSize();
                currentTile = tileManager.getWorldTiles()[(x+render.getUnitSize()/2)/render.getUnitSize()][(y+render.getUnitSize())/render.getUnitSize()];
            }
            entityList.add(new Slime(render, x, y, 1));
        }
    }
    
}
