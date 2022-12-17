package entity.animal;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import entity.Direction;
import entity.Entity;

import gfx.Renderer;

import utils.AssetManager;

import world.tile.Tile;
import world.tile.TileManager;

public class Animal extends Entity {

    private BufferedImage animalImage;
    private int counter;

    public Animal(Renderer render, int x, int y, int speed) {
        super(render, x, y, speed);

        int offsetX = render.getUnitSize()/4;
        int offsetY = render.getUnitSize()/4;

        int rectWidth = render.getUnitSize()-offsetX*2;
        int rectHeight = render.getUnitSize()-offsetY;
        
        this.solidArea = new Rectangle(offsetX, offsetY, rectWidth, rectHeight);

        initTexture(render);
    }

    public void initTexture(Renderer render) {
        animalImage = AssetManager.downImage[0];
    }

    public Direction getAnimalDirection(double num) {
        if(num < 0.25) return Direction.UP;
        if(num < 0.5) return Direction.DOWN;
        if(num < 0.75) return Direction.LEFT;
        if(num < 1) return Direction.RIGHT;
        return Direction.NONE;
    }
    
    @Override
    // Credit: RyiSnow (I changed it so that it fits how my code works)
    public boolean collide(Tile[][] tileMap, Direction dir) {
        int entityLeftX = x + solidArea.x;
        int entityRightX = x + solidArea.x + solidArea.width;

        int entityTopY = y + solidArea.y;
        int entityBottomY = y + solidArea.y + solidArea.height;

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
                    tile1 = entityTopRow < tileMap[entityLeftCol].length ? tileMap[entityLeftCol][entityTopRow] : null;
                    tile2 = entityBottomRow < tileMap[entityLeftCol].length ? tileMap[entityLeftCol][entityBottomRow] : null;
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

    public void move(Direction dir, TileManager tileManager) {
        System.out.println(x + " " + y);
        switch(dir) {
            case UP:
                if(y <= 0 || collide(tileManager.getWorldTiles(), dir)) break;
                y -= speed;
                break;
            case DOWN:
                if(y >= tileManager.getMaxCol()*render.getUnitSize()-render.getUnitSize() || collide(tileManager.getWorldTiles(), dir)) break;
                y += speed;
                break;
            case RIGHT:
                if(x >= tileManager.getMaxRow()*render.getUnitSize()-render.getUnitSize() || collide(tileManager.getWorldTiles(), dir)) break;
                x += speed;
                break;
            case LEFT:
                if(x <= 0 || collide(tileManager.getWorldTiles(), dir)) break;
                x -= speed;
                break;
            default:
                break;
        }
    }

    public void update(TileManager tileManager) {
        Direction animalDirection = getAnimalDirection(Math.random());
        if(counter == 10) {
            animalDirection = getAnimalDirection(Math.random());
            move(animalDirection, tileManager);
            counter = 0;
        }
        counter++;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(animalImage, x, y, null);
        // g.fillRect(solidArea.x+x, solidArea.y+y, solidArea.width, solidArea.height);
    }
    
}
