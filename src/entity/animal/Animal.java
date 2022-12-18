package entity.animal;

// import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import entity.Direction;
import entity.Entity;
import entity.EntityState;

import gfx.Renderer;

import world.tile.Tile;
import world.tile.TileManager;

public abstract class Animal extends Entity {

    protected BufferedImage animalImage;
    protected int imageIndex;

    private int counter;

    public Animal(Renderer render, int x, int y, int speed) {
        super(render, x, y, speed);
        
        direction = Direction.NONE;
        state = EntityState.STANDING;

        imageIndex = 0;
        initTexture();
    }
    
    @Override
    // Credit: RyiSnow (I changed it so that it fits how my code works)
    public boolean collideWithTile(Tile[][] tileMap) {
        int entityLeftX = x + solidArea.x;
        int entityRightX = x + solidArea.x + solidArea.width;

        int entityTopY = y + solidArea.y;
        int entityBottomY = y + solidArea.y + solidArea.height;

        int entityLeftCol = entityLeftX/render.getUnitSize();
        int entityRightCol = entityRightX/render.getUnitSize();
        int entityTopRow = entityTopY/render.getUnitSize();
        int entityBottomRow = entityBottomY/render.getUnitSize();

        Tile tile1, tile2;
        switch(direction) {
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

    public void move(TileManager tileManager) {
        if(collideWithTile(tileManager.getWorldTiles()) || collideWithEntity(render.getEntityList()) || collideWithPlayer()) {
            state = EntityState.STANDING;
            return;
        }
        state = EntityState.STANDING;
        switch(direction) {
            case UP:
                if(y <= 0) break;
                state = EntityState.WALKING;
                y -= speed;
                break;
            case DOWN:
                if(y >= tileManager.getMaxCol()*render.getUnitSize()-render.getUnitSize()) break;
                state = EntityState.WALKING;
                y += speed;
                break;
            case RIGHT:
                if(x >= tileManager.getMaxRow()*render.getUnitSize()-render.getUnitSize()) break;
                state = EntityState.WALKING;
                x += speed;
                break;
            case LEFT:
                if(x <= 0) break;
                state = EntityState.WALKING;
                x -= speed;
                break;
            default:
                break;
        }
    }

    @Override
    public void update(TileManager tileManager) {
        counter++;
        if(counter == 40) {
            direction = getAnimalDirection(Math.random());
            counter = 0;
        }
        move(tileManager);
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
        // g.setColor(new Color(2, 3, 4, 50));
        // g.fillRect(solidArea.x+x, solidArea.y+y, solidArea.width, solidArea.height);
    }
    
}
