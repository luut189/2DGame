package entity;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import gfx.KeyHandler;
import gfx.Renderer;
import utils.AssetManager;
import world.tile.TileManager;

public class Player extends Entity {

    private Direction playerDir;
    private EntityState playerState;
    
    private int imageIndex = 0;
    private BufferedImage playerImage;

    private boolean isLeftLeg = false;
    private int spriteCounter = 0;

    public Player(Renderer render, int x, int y) {
        this.render = render;
        this.x = x;
        this.y = y;
        this.speed = 4;
        initTexture(render);

        int offsetX = render.getUnitSize()/4;
        int offsetY = render.getUnitSize()/4;

        int rectWidth = render.getUnitSize()-offsetX*2;
        int rectHeight = render.getUnitSize()-offsetY;
        
        this.solidArea = new Rectangle(offsetX, offsetY, rectWidth, rectHeight);
        // System.out.println(offsetX + " " + offsetY);
        // System.out.println(solidArea.x + " " + solidArea.y + " " + solidArea.width + " " + solidArea.height);
    }

    public void initTexture(Renderer render) {
        playerState = EntityState.STANDING;
        playerDir = Direction.NONE;

        int nextTileX = (-render.getSceneX()+render.getUnitSize()/2+render.getCamX())/render.getUnitSize();
        int nextTileY = (-render.getSceneY()+render.getUnitSize()+render.getCamY())/render.getUnitSize();
        
        if(render.getTileManager().getWorldTiles()[nextTileX][nextTileY].getTileName().equals("water")) {
            playerState = EntityState.SWIMMING;
        }
        
        if(playerState == EntityState.STANDING) {
            playerImage = AssetManager.downImage[imageIndex];
        } else {
            playerImage = AssetManager.downSwimmingImage;
        }
    }

    public void update(Renderer render, KeyHandler keyHandler, TileManager tileManager) {
        playerDir = keyHandler.getPlayerDirection();
        playerState = keyHandler.getPlayerState();

        if(playerDir == Direction.NONE) {
            setCurrentPlayerImage(playerState, keyHandler.getPreviousPlayerDirection());
            return;
        }
        
        int nextTileX = (-render.getSceneX()+render.getUnitSize()/2+render.getCamX())/render.getUnitSize();
        int nextTileY = (-render.getSceneY()+render.getUnitSize()+render.getCamY())/render.getUnitSize();
        
        if(tileManager.getWorldTiles()[nextTileX][nextTileY].getTileName().equals("water")) {
            playerState = EntityState.SWIMMING;
        }

        setCurrentPlayerImage(playerState, playerDir);
        switch(playerDir) {
            case UP:
                if(-render.getSceneY()+render.getCamY() < 0 || collide(tileManager.getWorldTiles(), playerDir)) break;
                render.setSceneY(render.getSceneY() + (playerState != EntityState.SWIMMING ? getSpeed() : getSwimmingSpeed()));
                break;
            case DOWN:
                if(-render.getSceneY()+render.getCamY() >= tileManager.getMaxCol()*render.getUnitSize()-render.getUnitSize() || collide(tileManager.getWorldTiles(), playerDir)) break;
                render.setSceneY(render.getSceneY() - (playerState != EntityState.SWIMMING ? getSpeed() : getSwimmingSpeed()));
                break;
            case RIGHT:
                if(-render.getSceneX()+render.getCamX() >= tileManager.getMaxRow()*render.getUnitSize()-render.getUnitSize() || collide(tileManager.getWorldTiles(), playerDir)) break;
                render.setSceneX(render.getSceneX() - (playerState != EntityState.SWIMMING ? getSpeed() : getSwimmingSpeed()));
                break;
            case LEFT:
                if(-render.getSceneX()+render.getCamX() <= 0 || collide(tileManager.getWorldTiles(), playerDir)) break;
                render.setSceneX(render.getSceneX() + (playerState != EntityState.SWIMMING ? getSpeed() : getSwimmingSpeed()));
                break;
            default:
                break;
        }
    }

    public int increaseImageIndex(int currentIndex, int maxIndex) {
        int newIndex = currentIndex + 1;
        if(newIndex < maxIndex) {
            return newIndex;
        }
        return 0;
    }

    public BufferedImage getPlayerImage() {
        return playerImage;
    }

    public void setCurrentPlayerImage(EntityState playerState, Direction playerDir) {
        if(playerState == EntityState.WALKING) {
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
        } else if(playerState == EntityState.STANDING) {
            imageIndex = 0;
        }
    }

    public void draw(Graphics g) {
        g.drawImage(playerImage, x, y, null);
//        g.fillRect(solidArea.x+render.getCamX(), solidArea.y+render.getCamY(), solidArea.width, solidArea.height);
    }

}