package entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import gfx.Renderer;

import utils.AssetManager;
import world.tile.Tile;

public class Player extends Entity {
    
    private int imageIndex = 0;
    private BufferedImage playerImage;

    private boolean isSwimming = false;
    private boolean isLeftLeg = false;

    public Player(Renderer render, int x, int y, int speed) {
        super(render, x, y, speed);
        
        setMaxHealthValue(10);
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

        Tile current = render.getTileManager().getTile(nextTileX, nextTileY);
        if(current != null && current.getTileName().equals("water")) {
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
    public void update() {
        int nextTileX = (-render.getSceneX()+render.getUnitSize()/2+render.getCamX())/render.getUnitSize();
        int nextTileY = (-render.getSceneY()+render.getUnitSize()+render.getCamY())/render.getUnitSize();
        
        Tile current = render.getTileManager().getTile(nextTileX, nextTileY);
        if(current != null && current.getTileName().equals("water")) {
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

    @Override
    public void drawHealthBar(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int i = maxHealthValue;
        int currentHeart = 0;
        
        g2d.translate(20, 20);
        
        int maxHeart = maxHealthValue / 2;
        
        int healthBarBorderWidth = maxHeart*(render.getUnitSize()+10);
        int healthBarY = render.getHeight() - render.getUnitSize()*2;

        g2d.setColor(Color.black);
        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawRect(-5, healthBarY, healthBarBorderWidth, render.getUnitSize());
        g2d.setColor(new Color(0, 0, 0, 127));
        g2d.fillRect(-5, healthBarY, healthBarBorderWidth, render.getUnitSize());

        while(i > 0) {
            g2d.drawImage(AssetManager.emptyHeartImage, currentHeart*(render.getUnitSize()+10), healthBarY, null);
            i -= 2;
            currentHeart++;
        }

        int tempCurrentHealth = currentHealthValue;
        currentHeart = 0;
        while(tempCurrentHealth > 0) {
            if(tempCurrentHealth >= 2) {
                g2d.drawImage(AssetManager.fullHeartImage, currentHeart*(render.getUnitSize()+10), healthBarY, null);
                tempCurrentHealth -= 2;
            } else {
                g2d.drawImage(AssetManager.halfHeartImage, currentHeart*(render.getUnitSize()+10), healthBarY, null);
                tempCurrentHealth--;
            }
            currentHeart++;
        }
        g2d.translate(-20, -20);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(playerImage, x, y, null);
        // g.fillRect(solidArea.x+render.getCamX(), solidArea.y+render.getCamY(), solidArea.width, solidArea.height);
    }

}