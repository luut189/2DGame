package dev.kyzel.game.entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import dev.kyzel.game.Game;
import dev.kyzel.game.entity.animal.Ghost;
import dev.kyzel.gfx.Renderer;
import dev.kyzel.sfx.Sound;
import dev.kyzel.utils.AssetManager;
import dev.kyzel.game.world.tile.Tile;

public class Player extends Entity {
    
    private int imageIndex = 0;
    private BufferedImage playerImage;

    private boolean isSwimming = false;
    private boolean isLeftLeg = false;

    public Player(Renderer render, Game game, int x, int y, int speed) {
        super(render, game, x, y, speed);

        maxHealthValue = 10;
        currentHealthValue = maxHealthValue;
        attackValue = 1;

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

        int nextTileX = (-game.getSceneX() + render.getUnitSize() / 2 + game.getCamX()) / render.getUnitSize();
        int nextTileY = (-game.getSceneY() + render.getUnitSize() + game.getCamY()) / render.getUnitSize();

        Tile current = game.getTileManager().getTile(nextTileX, nextTileY);
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
        if(hitTick < maxHitTick) hitTick++;
        invincibleCounter++;
        spriteCounter++;
        int nextTileX = (-game.getSceneX()+render.getUnitSize()/2+game.getCamX())/render.getUnitSize();
        int nextTileY = (-game.getSceneY()+render.getUnitSize()+game.getCamY())/render.getUnitSize();
        
        Tile current = game.getTileManager().getTile(nextTileX, nextTileY);
        if(current != null && current.getTileName().equals("water")) {
            state = EntityState.SWIMMING;
            isSwimming = true;
        } else {
            isSwimming = false;
        }
        
        direction = game.getKeyHandler().getPlayerDirection();
        state = isSwimming ? EntityState.SWIMMING : game.getKeyHandler().getPlayerState();

        Direction previousDirection = game.getKeyHandler().getPreviousPlayerDirection();
        
        boolean collideWithTile = collideWithTile(game.getTileManager().getWorldTiles());
        int collidedEntity = collideWithEntity(game.getEntityList(), direction == Direction.NONE ? previousDirection : direction);

        if(state == EntityState.ATTACKING) {
            setCurrentPlayerImage(previousDirection);
            
            if(collidedEntity != -1) {
                Entity target = game.getEntityList().get(collidedEntity);
                inflictDamage(target);
                if(hitTick >= maxHitTick) Sound.HURT.play();
                if(target.getHealthValue() <= 0) {
                    game.getEntityList().set(collidedEntity, new Ghost(target.getRender(), target.getGame(), target.getX(), target.getY(), 2));
                }
                hitTick = 0;
            } else {
                if(hitTick >= maxHitTick) Sound.MISS.play();
                hitTick = 15;
            }
        }

        if(direction == Direction.NONE) {
            setCurrentPlayerImage(previousDirection);
            return;
        }

        setCurrentPlayerImage(direction);
        if(collideWithTile || collidedEntity != -1) {
            return;
        }
        switch(direction) {
            case UP -> {
                if(-game.getSceneY() + y <= 0) break;
                game.setSceneY(game.getSceneY() + (state != EntityState.SWIMMING ? getSpeed() : getSwimmingSpeed()));
            }
            case DOWN -> {
                if(-game.getSceneY() + y > game.getTileManager().getMaxCol() * render.getUnitSize() - render.getUnitSize())
                    break;
                game.setSceneY(game.getSceneY() - (state != EntityState.SWIMMING ? getSpeed() : getSwimmingSpeed()));
            }
            case RIGHT -> {
                if(-game.getSceneX() + x > game.getTileManager().getMaxRow() * render.getUnitSize() - render.getUnitSize())
                    break;
                game.setSceneX(game.getSceneX() - (state != EntityState.SWIMMING ? getSpeed() : getSwimmingSpeed()));
            }
            case LEFT -> {
                if(-game.getSceneX() + x <= 0) break;
                game.setSceneX(game.getSceneX() + (state != EntityState.SWIMMING ? getSpeed() : getSwimmingSpeed()));
            }
            default -> {}
        }
    }

    public BufferedImage getPlayerImage() {
        return playerImage;
    }

    public void setCurrentPlayerImage(Direction playerDir) {
        if(state == EntityState.STANDING) {
            imageIndex = 0;
            switch(playerDir) {
                case UP -> playerImage = AssetManager.upImage[imageIndex];
                case DOWN -> playerImage = AssetManager.downImage[imageIndex];
                case RIGHT -> playerImage = AssetManager.rightImage[imageIndex];
                case LEFT -> playerImage = AssetManager.leftImage[imageIndex];
                default -> {}
            }
        } else if(state == EntityState.WALKING || state == EntityState.ATTACKING) {
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
                case UP -> playerImage = AssetManager.upImage[imageIndex];
                case DOWN -> playerImage = AssetManager.downImage[imageIndex];
                case RIGHT -> playerImage = AssetManager.rightImage[imageIndex];
                case LEFT -> playerImage = AssetManager.leftImage[imageIndex];
                default -> {}
            }
        } else if(state == EntityState.SWIMMING) {
            switch(playerDir) {
                case UP -> playerImage = AssetManager.upSwimmingImage;
                case DOWN -> playerImage = AssetManager.downSwimmingImage;
                case RIGHT -> playerImage = AssetManager.rightSwimmingImage;
                case LEFT -> playerImage = AssetManager.leftSwimmingImage;
                default -> {}
            }
        }
    }

    @Override
    public void drawHealthBar(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int i = maxHealthValue;
        int currentHeart = 0;
        
        g2d.translate(20, 20);
        
        int maxHeart = maxHealthValue / 2;
        maxHeart += maxHealthValue % 2 == 0 ? 0 : 1;
        
        int healthBarBorderWidth = maxHeart*(render.getUnitSize()+10);
        int healthBarY = render.getHeight() - render.getUnitSize()*2;
            
        g2d.setColor(Color.black);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(-5, healthBarY, healthBarBorderWidth, render.getUnitSize(), 10, 10);
        g2d.setColor(new Color(0, 0, 0, 127));
        g2d.fillRoundRect(-5, healthBarY, healthBarBorderWidth, render.getUnitSize(), 10, 10);
        
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

    public void drawHitDelayBar(Graphics g) {
        double scale = (double) render.getUnitSize()/maxHitTick;
        double healthBarValue = scale * hitTick;

        g.setColor(new Color(35, 35, 35));
        g.fillRect(x, y-render.getUnitSize()/2-1, render.getUnitSize()+2, 6);

        g.setColor(new Color(255, 140, 30));
        g.fillRect(x+1, y-render.getUnitSize()/2, (int) healthBarValue, 4);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(playerImage, x, y, null);
        if(invincibleCounter < maxInvincibleCounter) {
            g.setXORMode(Color.red);
            g.drawImage(playerImage, x, y, null);
            g.setPaintMode();
        }
        // g.fillRect(solidArea.x+game.getCamX(), solidArea.y+game.getCamY(), solidArea.width, solidArea.height);
    }

}