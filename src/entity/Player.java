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
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    @Override
    public void initTexture() {
        state = EntityState.STANDING;
        direction = Direction.NONE;

        int nextTileX = (-render.getSceneX() + render.getUnitSize() / 2 + render.getCamX()) / render.getUnitSize();
        int nextTileY = (-render.getSceneY() + render.getUnitSize() + render.getCamY()) / render.getUnitSize();

        if (render.getTileManager().getWorldTiles()[nextTileX][nextTileY].getTileName().equals("water")) {
            state = EntityState.SWIMMING;
            isSwimming = true;
        }

        if (state == EntityState.STANDING) {
            playerImage = AssetManager.downImage[imageIndex];
        } else {
            playerImage = AssetManager.downSwimmingImage;
        }
    }

    @Override
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
        if(collideWithTile(tileManager.getWorldTiles()) || collideWithEntity(render.getEntityList())) {
            return;
        }
        switch(direction) {
            case UP:
                if(-render.getSceneY()+y <= 0) break;
                render.setSceneY(render.getSceneY() + (state != EntityState.SWIMMING ? getSpeed() : getSwimmingSpeed()));
                break;
            case DOWN:
                if(-render.getSceneY()+y >= tileManager.getMaxCol()*render.getUnitSize()-render.getUnitSize()) break;
                render.setSceneY(render.getSceneY() - (state != EntityState.SWIMMING ? getSpeed() : getSwimmingSpeed()));
                break;
            case RIGHT:
                if(-render.getSceneX()+x >= tileManager.getMaxRow()*render.getUnitSize()-render.getUnitSize()) break;
                render.setSceneX(render.getSceneX() - (state != EntityState.SWIMMING ? getSpeed() : getSwimmingSpeed()));
                break;
            case LEFT:
                if(-render.getSceneX()+x <= 0) break;
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