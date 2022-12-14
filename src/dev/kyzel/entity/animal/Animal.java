package dev.kyzel.entity.animal;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import dev.kyzel.entity.Direction;
import dev.kyzel.entity.Entity;
import dev.kyzel.entity.EntityState;
import dev.kyzel.gfx.Renderer;
import dev.kyzel.world.tile.TileManager;

public abstract class Animal extends Entity {

    protected BufferedImage animalImage;
    protected int imageIndex;

    protected double hostileProb;

    private int actionCounter;
    private final int maxActionCount = 120;

    public Animal(Renderer render, int x, int y, int speed) {
        super(render, x, y, speed);
        
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
        if(collideWithTile(tileManager.getWorldTiles()) || collideWithEntity(render.getEntityList()) || collideWithPlayer()) {
            if(collideWithPlayer()) {
                Entity target = render.getPlayer();
                if(state == EntityState.STANDING) return;
                int x = target.getX()-render.getSceneX();
                int y = target.getY()-render.getSceneY();
                inflictAttack(target);
                if(target.getHealthValue() <= 0) {
                    render.getEntityList().set(0 ,new Ghost(target.getRender(), x, y, 2));
                }
                return;
            }
            state = EntityState.STANDING;
            return;
        }
        switch(direction) {
            case UP -> {
                if(y <= 0) break;
                state = EntityState.WALKING;
                y -= speed;
            }
            case DOWN -> {
                if(y >= tileManager.getMaxCol() * render.getUnitSize() - render.getUnitSize()) break;
                state = EntityState.WALKING;
                y += speed;
            }
            case RIGHT -> {
                if(x >= tileManager.getMaxRow() * render.getUnitSize() - render.getUnitSize()) break;
                state = EntityState.WALKING;
                x += speed;
            }
            case LEFT -> {
                if(x <= 0) break;
                state = EntityState.WALKING;
                x -= speed;
            }
            default -> {}
        }
    }

    @Override
    public void update() {
        actionCounter++;
        invincibleCounter++;
        if(actionCounter >= maxActionCount) {
            direction = getAnimalDirection(Math.random());
            state = Math.random() < hostileProb ? EntityState.ATTACKING : EntityState.STANDING;
            actionCounter = 0;
        }
        move(render.getTileManager());
    }

    @Override
    public void drawHealthBar(Graphics g) {
        double scale = (double) render.getUnitSize()/maxHealthValue;
        double healthBarValue = scale * currentHealthValue;

        g.setColor(new Color(35, 35, 35));
        g.fillRect(x, y-render.getUnitSize()/2, render.getUnitSize(), 6);

        g.setColor(new Color(255, 0, 30));
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
