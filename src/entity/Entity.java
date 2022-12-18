package entity;

import java.awt.Graphics;
import java.awt.Rectangle;

import gfx.Renderer;

import world.tile.Tile;

public abstract class Entity {

    protected Renderer render;

    protected Direction direction;
    protected EntityState state;

    protected int x, y;
    protected int speed;

    protected Rectangle solidArea;

    protected int spriteCounter = 0;

    public Entity(Renderer render, int x, int y, int speed) {
        this.render = render;
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public boolean isInRange(Tile[][] tileMap, int x, int y) {
        return (
            x >= 0 && x < tileMap.length &&
            y >= 0 && y < tileMap[x].length
        );
    }

    public abstract void initTexture();

    public abstract boolean collideWithTile(Tile[][] tileMap, Direction dir);

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

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSwimmingSpeed() {
        return speed - speed/3;
    }

    public abstract void draw(Graphics g);
}