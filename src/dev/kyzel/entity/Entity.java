package dev.kyzel.entity;

import java.awt.Graphics;
import java.awt.Rectangle;

import java.util.ArrayList;

import dev.kyzel.entity.animal.Animal;
import dev.kyzel.entity.animal.Ghost;
import dev.kyzel.gfx.Renderer;
import dev.kyzel.world.tile.Tile;

public abstract class Entity implements IAttackable {

    protected Renderer render;

    protected Direction direction;
    protected EntityState state;

    protected int x, y;

    protected int maxHealthValue;
    protected int currentHealthValue;
    
    protected int attackValue;
    
    protected int speed;

    protected Rectangle solidArea;
    protected int solidAreaDefaultX, solidAreaDefaultY;

    protected int spriteCounter = 0;

    protected final int maxInvincibleCounter = 10;
    protected int invincibleCounter = maxInvincibleCounter;

    public Entity(Renderer render, int x, int y, int speed) {
        this.render = render;
        this.x = x;
        this.y = y;
        this.speed = speed;

        solidArea = new Rectangle(0, 0, render.getUnitSize(), render.getUnitSize());
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    public boolean isInRange(Tile[][] tileMap, int x, int y) {
        return (
            x >= 0 && x < tileMap.length &&
            y >= 0 && y < tileMap[x].length
        );
    }

    public abstract void update();

    public abstract void initTexture();

    public void inflictAttack(Entity target) {
        if(invincibleCounter < maxInvincibleCounter) return;
        target.setHealthValue(target.getHealthValue() - attackValue);
        target.setInvincibleCounter(0);
    }

    public boolean isAlive() {
        return currentHealthValue > 0;
    }

    public boolean collideWithTile(Tile[][] tileMap) {
        if(this instanceof Ghost) return false;
        
        int entityLeftX = x + solidArea.x;
        int entityRightX = x + solidArea.x + solidArea.width;

        int entityTopY = y + solidArea.y;
        int entityBottomY = y + solidArea.y + solidArea.height;

        if(this.equals(render.getPlayer())) {
            entityLeftX -= render.getSceneX();
            entityRightX -= render.getSceneX();

            entityTopY -= render.getSceneY();
            entityBottomY -= render.getSceneY();
        }

        int entityLeftCol = entityLeftX/render.getUnitSize();
        int entityRightCol = entityRightX/render.getUnitSize();
        int entityTopRow = entityTopY/render.getUnitSize();
        int entityBottomRow = entityBottomY/render.getUnitSize();

        Tile tile1;
        Tile tile2;
        switch(direction) {
            case UP -> {
                entityTopRow = (entityTopY - speed) / render.getUnitSize();
                if(isInRange(tileMap, entityLeftCol, entityTopRow) && isInRange(tileMap, entityRightCol, entityTopRow)) {
                    tile1 = tileMap[entityLeftCol][entityTopRow];
                    tile2 = tileMap[entityRightCol][entityTopRow];
                } else {
                    return false;
                }
            }
            case DOWN -> {
                entityBottomRow = (entityBottomY + speed) / render.getUnitSize();
                if(isInRange(tileMap, entityLeftCol, entityBottomRow) && isInRange(tileMap, entityRightCol, entityBottomRow)) {
                    tile1 = tileMap[entityLeftCol][entityBottomRow];
                    tile2 = tileMap[entityRightCol][entityBottomRow];
                } else {
                    return false;
                }
            }
            case RIGHT -> {
                entityRightCol = (entityRightX + speed) / render.getUnitSize();
                if(isInRange(tileMap, entityRightCol, entityTopRow) && isInRange(tileMap, entityRightCol, entityBottomRow)) {
                    tile1 = tileMap[entityRightCol][entityTopRow];
                    tile2 = tileMap[entityRightCol][entityBottomRow];
                } else {
                    return false;
                }
            }
            case LEFT -> {
                entityLeftCol = (entityLeftX - speed) / render.getUnitSize();
                if(isInRange(tileMap, entityLeftCol, entityTopRow) && isInRange(tileMap, entityLeftCol, entityBottomRow)) {
                    tile1 = tileMap[entityLeftCol][entityTopRow];
                    tile2 = tileMap[entityLeftCol][entityBottomRow];
                } else {
                    return false;
                }
            }
            default -> {
                return false;
            }
        }
        
        if(this instanceof Animal) {
            if(tile1.getTileName().equals("water")) return true;
            if(tile2.getTileName().equals("water")) return true;
        }
        return tile1.isSolid() || tile2.isSolid();
    }

    public boolean collideWithEntity(ArrayList<Entity> targetList) {
        for(Entity entity : targetList) {
            if(!entity.isAlive()) continue;
            if(entity != null && !entity.equals(this) && !entity.equals(render.getPlayer())) {
                if(this.equals(render.getPlayer())) {
                    solidArea.x += -render.getSceneX() + x;
                    solidArea.y += -render.getSceneY() + y;
                } else {
                    solidArea.x += x;
                    solidArea.y += y;
                }
                entity.getSolidArea().x += entity.getX();
                entity.getSolidArea().y += entity.getY();

                switch(direction) {
                    case UP -> solidArea.y -= speed;
                    case DOWN -> solidArea.y += speed;
                    case RIGHT -> solidArea.x += speed;
                    case LEFT -> solidArea.x -= speed;
                    default -> {}
                }
                if(solidArea.intersects(entity.getSolidArea())) {
                    solidArea.x = solidAreaDefaultX;
                    solidArea.y = solidAreaDefaultY;
                    entity.getSolidArea().x = entity.getSolidAreaDefaultX();
                    entity.getSolidArea().y = entity.getSolidAreaDefaultY();
                    return true;
                }
                solidArea.x = solidAreaDefaultX;
                solidArea.y = solidAreaDefaultY;
                entity.getSolidArea().x = entity.getSolidAreaDefaultX();
                entity.getSolidArea().y = entity.getSolidAreaDefaultY();
            }
        }
        return false;
    }

    public boolean collideWithPlayer() {
        if(!render.getPlayer().isAlive()) return false;
        solidArea.x += x;
        solidArea.y += y;
        render.getPlayer().getSolidArea().x += -render.getSceneX() + render.getPlayer().getX();
        render.getPlayer().getSolidArea().y += -render.getSceneY() + render.getPlayer().getY();

        switch(direction) {
            case UP -> solidArea.y -= speed;
            case DOWN -> solidArea.y += speed;
            case RIGHT -> solidArea.x += speed;
            case LEFT -> solidArea.x -= speed;
            default -> {}
        }
        if(solidArea.intersects(render.getPlayer().getSolidArea())) {
            solidArea.x = solidAreaDefaultX;
            solidArea.y = solidAreaDefaultY;
            render.getPlayer().getSolidArea().x = render.getPlayer().getSolidAreaDefaultX();
            render.getPlayer().getSolidArea().y = render.getPlayer().getSolidAreaDefaultY();
            return true;
        }
        solidArea.x = solidAreaDefaultX;
        solidArea.y = solidAreaDefaultY;
        render.getPlayer().getSolidArea().x = render.getPlayer().getSolidAreaDefaultX();
        render.getPlayer().getSolidArea().y = render.getPlayer().getSolidAreaDefaultY();
        return false;
    }

    public int getAttackedEntity(ArrayList<Entity> targetList) {
        for(int i = 0; i < targetList.size(); i++) {
            Entity entity = targetList.get(i);
            if(entity != null && !entity.equals(this) && !entity.equals(render.getPlayer())) {
                if(this.equals(render.getPlayer())) {
                    solidArea.x += -render.getSceneX() + x;
                    solidArea.y += -render.getSceneY() + y;
                } else {
                    solidArea.x += x;
                    solidArea.y += y;
                }
                entity.getSolidArea().x += entity.getX();
                entity.getSolidArea().y += entity.getY();

                switch(direction) {
                    case UP -> solidArea.y -= speed;
                    case DOWN -> solidArea.y += speed;
                    case RIGHT -> solidArea.x += speed;
                    case LEFT -> solidArea.x -= speed;
                    default -> {}
                }
                if(solidArea.intersects(entity.getSolidArea())) {
                    solidArea.x = solidAreaDefaultX;
                    solidArea.y = solidAreaDefaultY;
                    entity.getSolidArea().x = entity.getSolidAreaDefaultX();
                    entity.getSolidArea().y = entity.getSolidAreaDefaultY();
                    return i;
                }
                solidArea.x = solidAreaDefaultX;
                solidArea.y = solidAreaDefaultY;
                entity.getSolidArea().x = entity.getSolidAreaDefaultX();
                entity.getSolidArea().y = entity.getSolidAreaDefaultY();
            }
        }
        return -1;
    }

    public int increaseImageIndex(int currentIndex, int maxIndex) {
        int newIndex = currentIndex + 1;
        if(newIndex < maxIndex) {
            return newIndex;
        }
        return 0;
    }
    
    public Renderer getRender() {
        return render;
    }

    public void setRender(Renderer render) {
        this.render = render;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Rectangle getSolidArea() {
        return solidArea;
    }

    public int getSolidAreaDefaultX() {
        return solidAreaDefaultX;
    }

    public int getSolidAreaDefaultY() {
        return solidAreaDefaultY;
    }

    public Direction getDirection() {
        return direction;
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
            System.err.println("Invalid value");
            currentHealthValue = 0;
            return;
        }
        if(invincibleCounter < maxInvincibleCounter) {
            return;
        }

        invincibleCounter = 0;
        currentHealthValue = value;
    }

    @Override
    public int getHealthValue() {
        return currentHealthValue;
    }

    @Override
    public void setMaxHealthValue(int value) {
        maxHealthValue = value;
    }

    @Override
    public int getMaxHealthValue() {
        return maxHealthValue;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSwimmingSpeed() {
        return speed - speed/3;
    }

    public int getInvincibleCounter() {
        return invincibleCounter;
    }

    public void setInvincibleCounter(int counter) {
        invincibleCounter = counter;
    }

    public abstract void drawHealthBar(Graphics g);

    public abstract void draw(Graphics g);
}