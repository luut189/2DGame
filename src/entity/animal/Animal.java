package entity.animal;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import entity.Direction;
import entity.Entity;

import gfx.Renderer;

import world.tile.Tile;
import world.tile.TileManager;

public abstract class Animal extends Entity {

    protected BufferedImage animalImage;
    protected int imageIndex;

    private int counter;

    public Animal(Renderer render, int x, int y, int speed) {
        super(render, x, y, speed);

        int offsetX = 0;
        int offsetY = 0;

        int rectWidth = render.getUnitSize()-offsetX*2;
        int rectHeight = render.getUnitSize()-offsetY;
        
        this.solidArea = new Rectangle(offsetX, offsetY, rectWidth, rectHeight);
        
        direction = Direction.NONE;

        imageIndex = 0;
        initTexture();
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
                if(isInRange(tileMap, entityLeftCol, entityTopRow) && isInRange(tileMap, entityRightCol, entityTopRow)) {
                    tile1 = tileMap[entityLeftCol][entityTopRow];
                    tile2 = tileMap[entityRightCol][entityTopRow];
                    if(tile1.getTileName().equals("water")) return true;
                    if(tile2.getTileName().equals("water")) return true;
                    return tile1.isSolid() || tile2.isSolid();
                }
                break;
            case DOWN:
                entityBottomRow = (entityBottomY+speed)/render.getUnitSize();
                if(isInRange(tileMap, entityLeftCol, entityBottomRow) && isInRange(tileMap, entityRightCol, entityBottomRow)) {
                    tile1 = tileMap[entityLeftCol][entityBottomRow];
                    tile2 = tileMap[entityRightCol][entityBottomRow];
                    if(tile1.getTileName().equals("water")) return true;
                    if(tile2.getTileName().equals("water")) return true;
                    return tile1.isSolid() || tile2.isSolid();
                }
                break;
            case RIGHT:
                entityRightCol = (entityRightX+speed)/render.getUnitSize();
                if(isInRange(tileMap, entityRightCol, entityTopRow) && isInRange(tileMap, entityRightCol, entityBottomRow)) {
                    tile1 = tileMap[entityRightCol][entityTopRow];
                    tile2 = tileMap[entityRightCol][entityBottomRow];
                    if(tile1.getTileName().equals("water")) return true;
                    if(tile2.getTileName().equals("water")) return true;
                    return tile1.isSolid() || tile2.isSolid();
                }
                break;
            case LEFT:
                entityLeftCol = (entityLeftX-speed)/render.getUnitSize();
                if(isInRange(tileMap, entityLeftCol, entityTopRow) && isInRange(tileMap, entityLeftCol, entityBottomRow)) {
                    tile1 = tileMap[entityLeftCol][entityTopRow];
                    tile2 = tileMap[entityLeftCol][entityBottomRow];
                    if(tile1.getTileName().equals("water")) return true;
                    if(tile2.getTileName().equals("water")) return true;
                    return tile1.isSolid() || tile2.isSolid();
                }
                break;
            default:
                break;
        }
        return false;
    }

    public Direction getAnimalDirection(double num) {
        if(num < 0.25) return Direction.UP;
        if(num < 0.5) return Direction.DOWN;
        if(num < 0.75) return Direction.LEFT;
        if(num < 1) return Direction.RIGHT;
        return Direction.NONE;
    }

    public void move(Direction dir, TileManager tileManager) {
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
        counter++;
        if(counter == 40) {
            direction = getAnimalDirection(Math.random());
            counter = 0;
        }
        move(direction, tileManager);
    }

    @Override
    public void draw(Graphics g) {
        if(
            x >= -render.getSceneX()-render.getUnitSize() &&
            y >= -render.getSceneY()-render.getUnitSize() &&
            x < render.getWidth() - render.getSceneX() &&
            y < render.getHeight() - render.getSceneY()
        ) {
            g.drawImage(animalImage, x, y, null);
        }
        // g.fillRect(solidArea.x+x, solidArea.y+y, solidArea.width, solidArea.height);
    }
    
}
