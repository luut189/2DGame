package entity;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import gfx.KeyHandler;
import gfx.Renderer;

import utils.AssetManager;

import world.tile.Tile;
import world.tile.TileManager;

public class Player extends Entity {
    
    private int imageIndex = 0;
    private BufferedImage playerImage;

    private boolean isSwimming = false;
    private boolean isLeftLeg = false;

    public Player(Renderer render, int x, int y, int speed) {
        super(render, x, y, speed);
        initTexture();

        int offsetX = render.getUnitSize()/4;
        int offsetY = render.getUnitSize()/4;

        int rectWidth = render.getUnitSize()-offsetX*2;
        int rectHeight = render.getUnitSize()-offsetY;
        
        this.solidArea = new Rectangle(offsetX, offsetY, rectWidth, rectHeight);
    }

    @Override
    public void initTexture() {
        state = EntityState.STANDING;
        direction = Direction.NONE;

        int nextTileX = (-render.getSceneX()+render.getUnitSize()/2+render.getCamX())/render.getUnitSize();
        int nextTileY = (-render.getSceneY()+render.getUnitSize()+render.getCamY())/render.getUnitSize();
        
        if(render.getTileManager().getWorldTiles()[nextTileX][nextTileY].getTileName().equals("water")) {
            state = EntityState.SWIMMING;
            isSwimming = true;
        }
        
        if(state == EntityState.STANDING) {
            playerImage = AssetManager.downImage[imageIndex];
        } else {
            playerImage = AssetManager.downSwimmingImage;
        }
    }
    
    @Override
    // Credit: RyiSnow (I changed it so that it fits how my code works)
    public boolean collideWithTile(Tile[][] tileMap, Direction dir) {
        int entityLeftX = -render.getSceneX() + x + solidArea.x;
        int entityRightX = -render.getSceneX() + x + solidArea.x + solidArea.width;

        int entityTopY = -render.getSceneY() + y + solidArea.y;
        int entityBottomY = -render.getSceneY() + y + solidArea.y + solidArea.height;

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
                    return tile1.isSolid() || tile2.isSolid();
                }
                break;
            case DOWN:
                entityBottomRow = (entityBottomY+speed)/render.getUnitSize();
                if(isInRange(tileMap, entityLeftCol, entityBottomRow) && isInRange(tileMap, entityRightCol, entityBottomRow)) {
                    tile1 = tileMap[entityLeftCol][entityBottomRow];
                    tile2 = tileMap[entityRightCol][entityBottomRow];
                    return tile1.isSolid() || tile2.isSolid();
                }
                break;
            case RIGHT:
                entityRightCol = (entityRightX+speed)/render.getUnitSize();
                if(isInRange(tileMap, entityRightCol, entityTopRow) && isInRange(tileMap, entityRightCol, entityBottomRow)) {
                    tile1 = tileMap[entityRightCol][entityTopRow];
                    tile2 = tileMap[entityRightCol][entityBottomRow];
                    return tile1.isSolid() || tile2.isSolid();
                }
                break;
            case LEFT:
                entityLeftCol = (entityLeftX-speed)/render.getUnitSize();
                if(isInRange(tileMap, entityLeftCol, entityTopRow) && isInRange(tileMap, entityLeftCol, entityBottomRow)) {
                    tile1 = tileMap[entityLeftCol][entityTopRow];
                    tile2 = tileMap[entityLeftCol][entityBottomRow];
                    return tile1.isSolid() || tile2.isSolid();
                }
                break;
            default:
                break;
        }
        return false;
    }

    public void update(Renderer render, KeyHandler keyHandler, TileManager tileManager) {
        direction = keyHandler.getPlayerDirection();
        state = isSwimming ? EntityState.SWIMMING : keyHandler.getPlayerState();

        if(direction == Direction.NONE) {
            setCurrentPlayerImage(state, keyHandler.getPreviousPlayerDirection());
            return;
        }
        
        int nextTileX = (-render.getSceneX()+render.getUnitSize()/2+render.getCamX())/render.getUnitSize();
        int nextTileY = (-render.getSceneY()+render.getUnitSize()+render.getCamY())/render.getUnitSize();
        
        if(tileManager.getWorldTiles()[nextTileX][nextTileY < tileManager.getMaxCol() ? nextTileY : nextTileY-1].getTileName().equals("water")) {
            state = EntityState.SWIMMING;
            isSwimming = true;
        } else {
            isSwimming = false;
        }

        setCurrentPlayerImage(state, direction);
        switch(direction) {
            case UP:
                if(-render.getSceneY()+y <= 0 || collideWithTile(tileManager.getWorldTiles(), direction)) break;
                render.setSceneY(render.getSceneY() + (state != EntityState.SWIMMING ? getSpeed() : getSwimmingSpeed()));
                break;
            case DOWN:
                if(-render.getSceneY()+y >= tileManager.getMaxCol()*render.getUnitSize()-render.getUnitSize() || collideWithTile(tileManager.getWorldTiles(), direction)) break;
                render.setSceneY(render.getSceneY() - (state != EntityState.SWIMMING ? getSpeed() : getSwimmingSpeed()));
                break;
            case RIGHT:
                if(-render.getSceneX()+x >= tileManager.getMaxRow()*render.getUnitSize()-render.getUnitSize() || collideWithTile(tileManager.getWorldTiles(), direction)) break;
                render.setSceneX(render.getSceneX() - (state != EntityState.SWIMMING ? getSpeed() : getSwimmingSpeed()));
                break;
            case LEFT:
                if(-render.getSceneX()+x <= 0 || collideWithTile(tileManager.getWorldTiles(), direction)) break;
                render.setSceneX(render.getSceneX() + (state != EntityState.SWIMMING ? getSpeed() : getSwimmingSpeed()));
                break;
            default:
                break;
        }
    }

    public BufferedImage getPlayerImage() {
        return playerImage;
    }

    public void setCurrentPlayerImage(EntityState playerState, Direction playerDir) {
        if(playerState == EntityState.STANDING) {
            imageIndex = 0;
            switch(playerDir) {
                case UP:
                    playerImage = AssetManager.upImage[imageIndex];
                    break;
                case DOWN:
                    playerImage = AssetManager.downImage[imageIndex];
                    break;
                case RIGHT:
                    playerImage = AssetManager.rightImage[imageIndex];
                    break;
                case LEFT:
                    playerImage = AssetManager.leftImage[imageIndex];
                    break;
                default:
                    break;
            }
        } else if(playerState == EntityState.WALKING) {
            boolean isSideWay = playerDir == Direction.RIGHT || playerDir == Direction.LEFT;
            if(spriteCounter > (isSideWay ? 10 : 12)) {
                isLeftLeg = !isLeftLeg;
                if(isSideWay) {
                    imageIndex = increaseImageIndex(imageIndex, 4);
                }
                spriteCounter = 0;
            }
            if(!isSideWay) {
                imageIndex = isLeftLeg ? 1 : 2;
            }
            switch(playerDir) {
                case UP:
                    playerImage = AssetManager.upImage[imageIndex];
                    break;
                case DOWN:
                    playerImage = AssetManager.downImage[imageIndex];
                    break;
                case RIGHT:
                    playerImage = AssetManager.rightImage[imageIndex];
                    break;
                case LEFT:
                    playerImage = AssetManager.leftImage[imageIndex];
                    break;
                default:
                    break;
            }
            spriteCounter++;
        } else if(playerState == EntityState.SWIMMING) {
            switch(playerDir) {
                case UP:
                    playerImage = AssetManager.upSwimmingImage;
                    break;
                case DOWN:
                    playerImage = AssetManager.downSwimmingImage;
                    break;
                case RIGHT:
                    playerImage = AssetManager.rightSwimmingImage;
                    break;
                case LEFT:
                    playerImage = AssetManager.leftSwimmingImage;
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(playerImage, x, y, null);
        // g.fillRect(solidArea.x+render.getCamX(), solidArea.y+render.getCamY(), solidArea.width, solidArea.height);
    }

}