package dev.kyzel.game.entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import dev.kyzel.game.ControlHandler;
import dev.kyzel.game.Game;
import dev.kyzel.game.entity.animal.Animal;
import dev.kyzel.game.entity.animal.Ghost;
import dev.kyzel.game.world.tile.Tile;
import dev.kyzel.gfx.Renderer;
import dev.kyzel.sfx.Sound;
import dev.kyzel.utils.AssetManager;

public class Player extends Entity {
    
    private int imageIndex = 0;
    private BufferedImage playerImage;

    private Direction previousDirection = Direction.NONE;

    private boolean isSwimming = false;
    private boolean isLeftLeg = false;

    private int currentLevel = 1;

    private int score = 0;
    private final int maxScoreMultiplier = 5;

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
        score += target.getPoint();
        int remainingScore = score % (currentLevel * maxScoreMultiplier);
        Sound.SCORE_UP.play();
        if(score >= currentLevel * maxScoreMultiplier) {
            Sound.LEVEL_UP.play();
            currentLevel++;
            maxHealthValue++;
            score = remainingScore;
        }
    }

    private void attack(int collidedEntity) {
        if(collidedEntity != -1) {
            Entity target = game.getEntityList().get(collidedEntity);
            inflictDamage(target);
            if(hitTick >= maxHitTick) Sound.HURT.play();
            if(target.isDead()) {
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

        healing();
        checkSwimming();

        state = EntityState.STANDING;
        if(ControlHandler.UP.down()) {
            previousDirection = direction;
            direction = Direction.UP;
        }
        if(ControlHandler.DOWN.down()) {
            previousDirection = direction;
            direction = Direction.DOWN;
        }
        if(ControlHandler.RIGHT.down()) {
            previousDirection = direction;
            direction = Direction.RIGHT;
        }
        if(ControlHandler.LEFT.down()) {
            previousDirection = direction;
            direction = Direction.LEFT;
        }
        
        if(isSwimming) state = EntityState.SWIMMING;
        else {
            if(direction != Direction.NONE) state = EntityState.WALKING;
            if(ControlHandler.ATTACK.down()) state = EntityState.ATTACKING;
        }

        Direction currentDirection = direction == Direction.NONE ? previousDirection : direction;
        
        boolean collideWithTile = collideWithTile(game.getTileManager().getWorldTiles());
        int collidedEntity = collideWithEntity(game.getEntityList(), currentDirection);

        setCurrentPlayerImage(currentDirection);
        if(state == EntityState.ATTACKING) {
            attack(collidedEntity);
        }

        if(collideWithTile || collidedEntity != -1) {
            state = EntityState.WALKING;
            setCurrentPlayerImage(currentDirection);
            direction = Direction.NONE;
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
        previousDirection = direction;
        direction = Direction.NONE;
    }

    public BufferedImage getPlayerImage() {
        return playerImage;
    }

    public int getScore() {
        return currentLevel * maxScoreMultiplier + score;
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

    public void drawStatusBar(Graphics g, Color color, int value, int maxValue, int barIndex) {
        Graphics2D g2d = (Graphics2D) g;

        int barWidth = render.getUnitSize()*5;

        double scale = (double) barWidth/(maxValue);
        double maxBarValue = scale * maxValue;
        double currentBarValue = scale * value;

        int spacingBetweenBar = 10;
        int barHeight = 10;
        int startAt = render.getHeight() - (spacingBetweenBar+render.getUnitSize()/2);

        int barX = 15;
        int barY = startAt - (spacingBetweenBar+barHeight)*barIndex;

        // background color
        g2d.setColor(new Color(0, 0, 0, 127));
        g2d.fillRoundRect(barX, barY, (int) maxBarValue, barHeight, 4, 4);

        // foreground color
        g2d.setColor(color);
        g2d.fillRoundRect(barX, barY, (int) currentBarValue, barHeight, 4, 4);
        
        // border
        g2d.setColor(Color.black);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(barX, barY, (int) maxBarValue, barHeight, 4, 4);
    }

    @Override
    public void drawHealthBar(Graphics g) {
        Color healthColor = new Color(210, 48, 48);
        drawStatusBar(g, healthColor, currentHealthValue, maxHealthValue, 0);
    }

    public void drawScoreBar(Graphics g) {
        Color scoreColor = new Color(0, 150, 136);
        drawStatusBar(g, scoreColor, score, currentLevel*maxScoreMultiplier, 2);
    }

    public void drawHitCooldownBar(Graphics g) {
        Color hitCooldownColor = new Color(255, 140, 30);
        drawStatusBar(g, hitCooldownColor, hitTick, maxHitTick, 1);
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