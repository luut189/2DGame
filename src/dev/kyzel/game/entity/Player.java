package dev.kyzel.game.entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import dev.kyzel.game.Game;
import dev.kyzel.game.entity.animal.Animal;
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

    private int currentLevel = 1;

    private int score = 0;
    private int maxScoreMutiplier = 5;

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

    private void checkSwimming() {
        int nextTileX = (-game.getSceneX()+render.getUnitSize()/2+game.getCamX())/render.getUnitSize();
        int nextTileY = (-game.getSceneY()+render.getUnitSize()+game.getCamY())/render.getUnitSize();
        
        Tile current = game.getTileManager().getTile(nextTileX, nextTileY);
        if(current != null && current.getTileName().equals("water")) {
            state = EntityState.SWIMMING;
            isSwimming = true;
        } else {
            isSwimming = false;
        }
    }

    private void scoreUp(Animal target) {
        score += target.getScore();
        int remainingScore = score % (currentLevel * maxScoreMutiplier);
        Sound.SCORE_UP.play();
        if(score >= currentLevel * maxScoreMutiplier) {
            Sound.LEVEL_UP.play();
            currentLevel++;
            maxHealthValue++;
            score = remainingScore;
        }
    }

    private void healing() {
        if(healingCounter <= 0) {
            currentHealthValue++;
            healingCounter = maxHealingCounter;
        }
    }

    private void attack(int collidedEntity) {
        if(collidedEntity != -1) {
            Entity target = game.getEntityList().get(collidedEntity);
            inflictDamage(target);
            if(hitTick >= maxHitTick) Sound.HURT.play();
            if(target.getHealthValue() <= 0) {
                scoreUp((Animal) target);
                game.getEntityList().set(collidedEntity, new Ghost(target.getRender(), target.getGame(), target.getX(), target.getY(), 2));
            }
            hitTick = 0;
        } else {
            if(hitTick >= maxHitTick) Sound.MISS.play();
            hitTick = 15;
        }
    }

    @Override
    public void update() {
        if(hitTick < maxHitTick) hitTick++;
        invincibleCounter++;
        spriteCounter++;
        if(currentHealthValue < maxHealthValue) healingCounter--;

        healing();
        checkSwimming();

        direction = game.getKeyHandler().getPlayerDirection();
        state = isSwimming ? EntityState.SWIMMING : game.getKeyHandler().getPlayerState();

        Direction previousDirection = game.getKeyHandler().getPreviousPlayerDirection();

        Direction currentDirection = direction == Direction.NONE ? previousDirection : direction;
        
        boolean collideWithTile = collideWithTile(game.getTileManager().getWorldTiles());
        int collidedEntity = collideWithEntity(game.getEntityList(), currentDirection);

        setCurrentPlayerImage(currentDirection);
        if(state == EntityState.ATTACKING) {
            attack(collidedEntity);
        }

        if(collideWithTile || collidedEntity != -1) {
            return;
        }

        int currentSpeed = state != EntityState.SWIMMING ? speed : getSwimmingSpeed();
        switch(direction) {
            case UP -> {
                if(y - game.getSceneY() <= 0) break;
                game.setSceneY(game.getSceneY() + currentSpeed);
            }
            case DOWN -> {
                if(y - game.getSceneY() > game.getTileManager().getMaxCol() * render.getUnitSize() - render.getUnitSize()) break;
                game.setSceneY(game.getSceneY() - currentSpeed);
            }
            case RIGHT -> {
                if(x - game.getSceneX() > game.getTileManager().getMaxRow() * render.getUnitSize() - render.getUnitSize()) break;
                game.setSceneX(game.getSceneX() - currentSpeed);
            }
            case LEFT -> {
                if(x - game.getSceneX() <= 0) break;
                game.setSceneX(game.getSceneX() + currentSpeed);
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
        }
        
        if(state == EntityState.SWIMMING) {
            switch(playerDir) {
                case UP -> playerImage = AssetManager.upSwimmingImage;
                case DOWN -> playerImage = AssetManager.downSwimmingImage;
                case RIGHT -> playerImage = AssetManager.rightSwimmingImage;
                case LEFT -> playerImage = AssetManager.leftSwimmingImage;
                default -> {}
            }
        } else {
            switch(playerDir) {
                case UP -> playerImage = AssetManager.upImage[imageIndex];
                case DOWN -> playerImage = AssetManager.downImage[imageIndex];
                case RIGHT -> playerImage = AssetManager.rightImage[imageIndex];
                case LEFT -> playerImage = AssetManager.leftImage[imageIndex];
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
        
        int heartWidth = render.getUnitSize()+10;
        int healthBarBorderWidth = maxHeart*heartWidth;
        int healthBarY = render.getHeight() - render.getUnitSize()*2;
            
        g2d.setColor(Color.black);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(-5, healthBarY, healthBarBorderWidth, render.getUnitSize(), 10, 10);
        g2d.setColor(new Color(0, 0, 0, 127));
        g2d.fillRoundRect(-5, healthBarY, healthBarBorderWidth, render.getUnitSize(), 10, 10);
        
        while(i > 0) {
            g2d.drawImage(AssetManager.emptyHeartImage, currentHeart*heartWidth, healthBarY, null);
            i -= 2;
            currentHeart++;
        }

        int tempCurrentHealth = currentHealthValue;
        currentHeart = 0;
        while(tempCurrentHealth > 0) {
            if(tempCurrentHealth >= 2) {
                g2d.drawImage(AssetManager.fullHeartImage, currentHeart*heartWidth, healthBarY, null);
                tempCurrentHealth -= 2;
            } else {
                g2d.drawImage(AssetManager.halfHeartImage, currentHeart*heartWidth, healthBarY, null);
                tempCurrentHealth--;
            }
            currentHeart++;
        }
        g2d.translate(-20, -20);
    }

    public void drawScoreBar(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        int scoreWidth = render.getUnitSize()*5;

        double scale = (double) scoreWidth/(currentLevel*maxScoreMutiplier);
        double maxScoreValue = scale * currentLevel*maxScoreMutiplier;
        double scoreValue = scale * score;

        int scoreBarX = 15;
        int scoreBarY = render.getHeight() - render.getUnitSize()*2 - render.getUnitSize()/2;

        // background color
        g2d.setColor(new Color(0, 0, 0, 127));
        g2d.fillRoundRect(scoreBarX, scoreBarY, (int) maxScoreValue, 10, 4, 4);

        // foreground color
        g2d.setColor(new Color(0, 150, 136));
        g2d.fillRoundRect(scoreBarX, scoreBarY, (int) scoreValue, 10, 4, 4);
        
        // border
        g2d.setColor(Color.black);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(scoreBarX, scoreBarY, (int) maxScoreValue, 10, 4, 4);
    }

    public void drawHitCooldownBar(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        int hitTickBarWidth = render.getUnitSize()*5;

        double scale = (double) hitTickBarWidth/(maxHitTick);
        double maxHitTickValue = scale * maxHitTick;
        double hitTickValue = scale * hitTick;

        int hitTickBarX = 15;
        int hitTickBarY = render.getHeight() - render.getUnitSize()*2;

        // background color
        g2d.setColor(new Color(0, 0, 0, 127));
        g2d.fillRoundRect(hitTickBarX, hitTickBarY, (int) maxHitTickValue, 10, 4, 4);

        // foreground color
        g2d.setColor(new Color(255, 140, 30));
        g2d.fillRoundRect(hitTickBarX, hitTickBarY, (int) hitTickValue, 10, 4, 4);
        
        // border
        g2d.setColor(Color.black);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(hitTickBarX, hitTickBarY, (int) maxHitTickValue, 10, 4, 4);
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