package entity.animal;

import java.awt.AlphaComposite;
// import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entity.Direction;
import entity.Entity;
import entity.EntityState;

import gfx.Renderer;

import world.tile.TileManager;

public abstract class Animal extends Entity {

    protected BufferedImage animalImage;
    protected int imageIndex;

    private int actionCounter;
    private int maxActionCount = 120;

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
        if(!(this instanceof Ghost)) {
            if(collideWithTile(tileManager.getWorldTiles()) || collideWithEntity(render.getEntityList()) || collideWithPlayer()) {
                state = EntityState.STANDING;
                return;
            }
        }
        state = EntityState.STANDING;
        switch(direction) {
            case UP:
                if(y <= 0) break;
                state = EntityState.WALKING;
                y -= speed;
                break;
            case DOWN:
                if(y >= tileManager.getMaxCol()*render.getUnitSize()-render.getUnitSize()) break;
                state = EntityState.WALKING;
                y += speed;
                break;
            case RIGHT:
                if(x >= tileManager.getMaxRow()*render.getUnitSize()-render.getUnitSize()) break;
                state = EntityState.WALKING;
                x += speed;
                break;
            case LEFT:
                if(x <= 0) break;
                state = EntityState.WALKING;
                x -= speed;
                break;
            default:
                break;
        }
    }

    @Override
    public void update() {
        actionCounter++;
        if(actionCounter >= maxActionCount) {
            direction = getAnimalDirection(Math.random());
            actionCounter = 0;
        }
        move(render.getTileManager());
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this instanceof Ghost ? 0.5f : 1f));
        g.drawImage(animalImage, x, y, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        // g.setColor(new Color(255, 32, 43, 50));
        // g.fillRect(solidArea.x+x, solidArea.y+y, solidArea.width, solidArea.height);
    }
    
}
