package entity;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import gfx.Renderer;

import utils.AssetManager;

public class Player extends Entity implements IAttackable {
    
    private int imageIndex = 0;
    private BufferedImage playerImage;

    private boolean isSwimming = false;
    private boolean isLeftLeg = false;

    public Player(Renderer render, int x, int y, int speed) {
        super(render, x, y, speed);
        
        maxHealthValue = 10;
        setHealthValue(3);
        setAttackValue(1);

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
    public void setAttackValue(int value) {
        attackValue = value;
    }

    @Override
    public int getAttackValue() {
        return attackValue;
    }

    @Override
    public void setHealthValue(int value) {
        if(value > maxHealthValue) {
            System.err.println("Invaid value");
            currentHealthValue = 0;
            return;
        }

        currentHealthValue = value;
    }

    @Override
    public int getHealthValue() {
        return currentHealthValue;
    }

    @Override
    public void update() {
        int nextTileX = (-render.getSceneX()+render.getUnitSize()/2+render.getCamX())/render.getUnitSize();
        int nextTileY = (-render.getSceneY()+render.getUnitSize()+render.getCamY())/render.getUnitSize();

        nextTileX -= nextTileX < render.getTileManager().getMaxRow() ? 0 : 1;
        nextTileY -= nextTileY < render.getTileManager().getMaxCol() ? 0 : 1;
        
        if(render.getTileManager().getWorldTiles()[nextTileX][nextTileY].getTileName().equals("water")) {
            state = EntityState.SWIMMING;
            isSwimming = true;
        } else {
            isSwimming = false;
        }
        
        direction = render.getKeyHandler().getPlayerDirection();
        state = isSwimming ? EntityState.SWIMMING : render.getKeyHandler().getPlayerState();

        if(state == EntityState.ATTACKING) {
            setCurrentPlayerImage(render.getKeyHandler().getPreviousPlayerDirection());
            // TODO - What will happen when player attack?
        }

        if(direction == Direction.NONE) {
            setCurrentPlayerImage(render.getKeyHandler().getPreviousPlayerDirection());
            return;
        }

        setCurrentPlayerImage(direction);
        if(collideWithTile(render.getTileManager().getWorldTiles()) || collideWithEntity(render.getEntityList())) {
            return;
        }
        switch(direction) {
            case UP:
                if(-render.getSceneY()+y <= 0) break;
                render.setSceneY(render.getSceneY() + (state != EntityState.SWIMMING ? getSpeed() : getSwimmingSpeed()));
                break;
            case DOWN:
                if(-render.getSceneY()+y > render.getTileManager().getMaxCol()*render.getUnitSize()-render.getUnitSize()) break;
                render.setSceneY(render.getSceneY() - (state != EntityState.SWIMMING ? getSpeed() : getSwimmingSpeed()));
                break;
            case RIGHT:
                if(-render.getSceneX()+x > render.getTileManager().getMaxRow()*render.getUnitSize()-render.getUnitSize()) break;
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

    public void setCurrentPlayerImage(Direction playerDir) {
        if(state == EntityState.STANDING) {
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
        } else if(state == EntityState.WALKING) {
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
        } else if(state == EntityState.SWIMMING) {
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
        } else if(state == EntityState.ATTACKING) {
            // TODO - Will be used if I added attacking sprite for the player

            // TESTING!!
            setHealthValue(getHealthValue()+1);
        }
    }

    public void drawHealthBar(Graphics g) {
        int i = maxHealthValue;
        int currentHeart = 0;
        g.translate(20, 0);
        while(i > 0) {
            g.drawImage(AssetManager.emptyHeartImage, currentHeart*(render.getUnitSize()+10), render.getUnitSize(), null);
            i -= 2;
            currentHeart++;
        }

        int tempCurrentHealth = currentHealthValue;
        currentHeart = 0;
        while(tempCurrentHealth > 0) {
            if(tempCurrentHealth >= 2) {
                g.drawImage(AssetManager.fullHeartImage, currentHeart*(render.getUnitSize()+10), render.getUnitSize(), null);
                tempCurrentHealth -= 2;
            } else {
                g.drawImage(AssetManager.halfHeartImage, currentHeart*(render.getUnitSize()+10), render.getUnitSize(), null);
                tempCurrentHealth--;
            }
            currentHeart++;
        }
        g.translate(-20, 0);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(playerImage, x, y, null);
        // g.fillRect(solidArea.x+render.getCamX(), solidArea.y+render.getCamY(), solidArea.width, solidArea.height);
    }

}