package dev.kyzel.game.entity.animal;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import dev.kyzel.game.Game;
import dev.kyzel.game.entity.Direction;
import dev.kyzel.game.entity.Entity;
import dev.kyzel.game.entity.EntityState;
import dev.kyzel.game.world.tile.TileManager;
import dev.kyzel.gfx.Renderer;
import dev.kyzel.sfx.Sound;

public abstract class Animal extends Entity {

    protected BufferedImage animalImage;
    protected int imageIndex;

    protected double hostileProb;

    private int actionCounter;
    private final int maxActionCount = 120;

    public Animal(Renderer render, Game game, int x, int y, int speed) {
        super(render, game, x, y, speed);
        
        direction = Direction.NONE;
        state = EntityState.STANDING;

        imageIndex = 0;
        actionCounter = (int) (Math.random()*maxActionCount);
        initTexture();
    }

    public Direction getAnimalDirection(double num) {
        if(num < 0.25) return Direction.UP;
        if(num < 0.5) return Direction.DOWN;
        if(num < 0.75) return Direction.LEFT;
        if(num < 1) return Direction.RIGHT;
        return Direction.NONE;
    }

    public void move(TileManager tileManager) {
        boolean collideWithTile = collideWithTile(tileManager.getWorldTiles());
        int collidedEntity = collideWithEntity(game.getEntityList(), direction);
        boolean collideWithPlayer = collideWithPlayer();

        if(collideWithTile || collidedEntity != -1 || collideWithPlayer) {
            if(!collideWithTile) {
                state = Math.random() < hostileProb ? EntityState.ATTACKING : EntityState.STANDING;
            }
            if(state == EntityState.STANDING) return;

            if(collideWithPlayer) {
                Entity target = game.getPlayer();
                inflictDamage(target);
                if(hitTick >= maxHitTick) Sound.PLAYER_HURT.play();
                if(target.isDead()) {
                    Sound.LOSE.play();
                    int x = target.getX()-game.getSceneX();
                    int y = target.getY()-game.getSceneY();
                    game.getEntityList().set(0 ,new Ghost(target.getRender(), target.getGame(), x, y, 2));
                }
                direction = Direction.getOppositeDirection(direction);
                hitTick = 0;
            } else if(collidedEntity != -1) {
                Entity target = game.getEntityList().get(collidedEntity);
                if(target.getClass() == this.getClass()) {
                    direction = Direction.getOppositeDirection(direction);
                } else {
                    inflictDamage(target);
                    if(hitTick >= maxHitTick) Sound.HURT.play();
                    if(target.isDead()) {
                        int x = target.getX();
                        int y = target.getY();
                        game.getEntityList().set(collidedEntity ,new Ghost(target.getRender(), target.getGame(), x, y, 2));
                    }
                    direction = Direction.getOppositeDirection(direction);
                    hitTick = 0;
                }
            }
            state = EntityState.STANDING;
            return;
        }
        switch(direction) {
            case UP -> {
                if(y <= 0) break;
                state = EntityState.WALKING;
                y -= speed;
                return;
            }
            case DOWN -> {
                if(y >= tileManager.getMaxCol() * render.getUnitSize() - render.getUnitSize()) break;
                state = EntityState.WALKING;
                y += speed;
                return;
            }
            case RIGHT -> {
                if(x >= tileManager.getMaxRow() * render.getUnitSize() - render.getUnitSize()) break;
                state = EntityState.WALKING;
                x += speed;
                return;
            }
            case LEFT -> {
                if(x <= 0) break;
                state = EntityState.WALKING;
                x -= speed;
                return;
            }
            default -> {}
        }
        state = EntityState.STANDING;
    }

    @Override
    public void update() {
        super.update();
        actionCounter++;
        
        if(actionCounter >= maxActionCount) {
            direction = getAnimalDirection(Math.random());
            actionCounter = 0;
        }
        move(game.getTileManager());

        setCurrentAnimalImage();
    }

    public abstract void setCurrentAnimalImage();

    public abstract int getPoint();

    @Override
    public void drawHealthBar(Graphics g) {
        double scale = (double) render.getUnitSize()/maxHealthValue;
        double healthBarValue = scale * currentHealthValue;

        g.setColor(new Color(35, 35, 35));
        g.fillRect(x, y-render.getUnitSize()/2, render.getUnitSize(), 6);

        Color healthBarColor = isCursed ? new Color(62, 48, 86) : new Color(255, 0, 30);
        g.setColor(healthBarColor);
        g.fillRect(x, y-render.getUnitSize()/2, (int) healthBarValue, 4);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (this instanceof Ghost) ? 0.5f : 1f));
        g.drawImage(animalImage, x, y, null);
        if(invincibleCounter < maxInvincibleCounter) {
            g.setXORMode(Color.red);
            g.drawImage(animalImage, x, y, null);
            g.setPaintMode();
        }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        // g.setColor(new Color(255, 32, 43, 50));
        // g.fillRect(solidArea.x+x, solidArea.y+y, solidArea.width, solidArea.height);
    }
    
}
