package entity;

import java.awt.Rectangle;

import gfx.Renderer;
import utils.Direction;
import world.tile.Tile;

public class Entity {

    protected Renderer render;

    protected int x, y;
    protected int speed;

    protected Rectangle solidArea;

    // Credit: RyiSnow (I changed it so that it fits how my code works)
    public boolean collide(Tile[][] tileMap, Direction dir) {
        int entityLeftX = -render.getSceneX() + render.getCamX() + solidArea.x;
        int entityRightX = -render.getSceneX() + render.getCamX() + solidArea.x + solidArea.width;

        int entityTopY = -render.getSceneY() + render.getCamY() + solidArea.y;
        int entityBottomY = -render.getSceneY() + render.getCamY() + solidArea.y + solidArea.height;

        int entityLeftCol = entityLeftX/render.getUnitSize();
        int entityRightCol = entityRightX/render.getUnitSize();
        int entityTopRow = entityTopY/render.getUnitSize();
        int entityBottomRow = entityBottomY/render.getUnitSize();

        Tile tile1, tile2;
        switch(dir) {
            case UP:
                entityTopRow = (entityTopY-speed)/render.getUnitSize();
                if(entityBottomRow < tileMap[entityLeftCol].length && entityBottomRow < tileMap[entityRightCol].length) {
                    tile1 = tileMap[entityLeftCol][entityTopRow];
                    tile2 = tileMap[entityRightCol][entityTopRow];
                    return tile1.isSolid() || tile2.isSolid();
                }
                break;
            case DOWN:
                entityBottomRow = (entityBottomY+speed)/render.getUnitSize();
                if(entityBottomRow < tileMap[entityLeftCol].length) {
                    tile1 = tileMap[entityLeftCol][entityBottomRow];
                    tile2 = tileMap[entityRightCol][entityBottomRow];
                    return tile1.isSolid() || tile2.isSolid();
                }
                break;
            case RIGHT:
                entityRightCol = (entityRightX+speed)/render.getUnitSize();
                if(entityRightCol < tileMap.length) {
                    tile1 = entityTopRow < tileMap[entityRightCol].length ? tileMap[entityRightCol][entityTopRow] : null;
                    tile2 = entityBottomRow < tileMap[entityRightCol].length ? tileMap[entityRightCol][entityBottomRow] : null;
                    if(tile1 == null && tile2 == null) {
                        return false;
                    }
                    if(tile1 == null) return tile2.isSolid();
                    if(tile2 == null) return tile1.isSolid();

                    return tile1.isSolid() || tile2.isSolid();
                }
                break;
            case LEFT:
                entityLeftCol = (entityLeftX-speed)/render.getUnitSize();
                if(entityLeftCol < tileMap.length) {
                    tile1 = entityTopRow < tileMap[entityLeftCol].length ? tileMap[entityRightCol][entityTopRow] : null;
                    tile2 = entityBottomRow < tileMap[entityLeftCol].length ? tileMap[entityRightCol][entityBottomRow] : null;
                    if(tile1 == null && tile2 == null) {
                        return false;
                    }
                    if(tile1 == null) return tile2.isSolid();
                    if(tile2 == null) return tile1.isSolid();

                    return tile1.isSolid() || tile2.isSolid();
                }
                break;
            default:
                break;
        }
        return false;
    }
    
    public Renderer getRender() {
        return render;
    }

    public void setRender(Renderer render) {
        this.render = render;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}