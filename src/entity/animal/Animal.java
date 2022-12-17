package entity.animal;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import entity.Direction;
import entity.Entity;

import gfx.Renderer;

import utils.AssetManager;
import world.tile.TileManager;

public class Animal extends Entity {

    private BufferedImage animalImage;
    private int counter;

    public Animal(Renderer render, int x, int y, int speed) {
        super(render, x, y, speed);

        int offsetX = render.getUnitSize()/4;
        int offsetY = render.getUnitSize()/4;

        int rectWidth = render.getUnitSize()-offsetX*2;
        int rectHeight = render.getUnitSize()-offsetY;
        
        this.solidArea = new Rectangle(offsetX, offsetY, rectWidth, rectHeight);

        initTexture(render);
    }

    public void initTexture(Renderer render) {
        animalImage = AssetManager.downImage[0];
    }

    public Direction getAnimalDirection(double num) {
        if(num < 0.25) return Direction.UP;
        if(num < 0.5) return Direction.DOWN;
        if(num < 0.75) return Direction.LEFT;
        if(num < 1) return Direction.RIGHT;
        return Direction.NONE;
    }

    public void move(Direction dir, TileManager tileManager) {
        System.out.println(x + " " + y);
        switch(dir) {
            case UP:
                if(-render.getSceneY()+y < 0 || collide(tileManager.getWorldTiles(), dir)) break;
                y -= speed;
                break;
            case DOWN:
                if(-render.getSceneY()+y >= tileManager.getMaxCol()*render.getUnitSize()-render.getUnitSize() || collide(tileManager.getWorldTiles(), dir)) break;
                y += speed;
                break;
            case RIGHT:
                if(-render.getSceneX()+x >= tileManager.getMaxRow()*render.getUnitSize()-render.getUnitSize() || collide(tileManager.getWorldTiles(), dir)) break;
                x += speed;
                break;
            case LEFT:
                if(-render.getSceneX()+x <= 0 || collide(tileManager.getWorldTiles(), dir)) break;
                x -= speed;
                break;
            default:
                break;
        }
    }

    public void update(TileManager tileManager) {
        Direction animalDirection = getAnimalDirection(Math.random());
        if(counter == 10) {
            animalDirection = getAnimalDirection(Math.random());
            move(animalDirection, tileManager);
            counter = 0;
        }
        counter++;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(animalImage, x, y, null);
        // g.fillRect(solidArea.x+x, solidArea.y+y, solidArea.width, solidArea.height);
    }
    
}
