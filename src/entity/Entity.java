package entity;

import java.awt.Graphics;
import java.awt.Rectangle;

import gfx.Renderer;

import world.tile.Tile;

public abstract class Entity {

    protected Renderer render;

    protected int x, y;
    protected int speed;

    protected Rectangle solidArea;

    public Entity(Renderer render, int x, int y, int speed) {
        this.render = render;
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public abstract boolean collide(Tile[][] tileMap, Direction dir);
    
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