package entity;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import gfx.Renderer;
import utils.TextureLoader;

public class Player extends Entity {

    private TextureLoader loader = new TextureLoader();

    private BufferedImage[] downImage = loadPlayerImage("/player/down", 3);
    private BufferedImage[] upImage = loadPlayerImage("/player/up", 3);
    private BufferedImage[] rightImage = loadPlayerImage("/player/right", 4);
    private BufferedImage[] leftImage = loadPlayerImage("/player/left", 4);

    private int imageIndex = 0;
    private BufferedImage playerImage = downImage[imageIndex];

    private boolean isLeftLeg = false;
    private int spriteCounter = 0;

    public Player(Renderer render, int x, int y) {
        this.render = render;
        this.x = x;
        this.y = y;
        this.speed = 4;

        int offsetX = render.getUnitSize()/2;
        int offsetY = render.getUnitSize()/4;
        
        this.solidArea = new Rectangle(offsetX, offsetY, render.getUnitSize()-offsetX*2, render.getUnitSize()-offsetY);
        // System.out.println(offsetX + " " + offsetY);
        // System.out.println(solidArea.x + " " + solidArea.y + " " + solidArea.width + " " + solidArea.height);
    }

    public int increaseImageIndex(int currentIndex, int maxIndex) {
        int newIndex = currentIndex + 1;
        if(newIndex < maxIndex) {
            return newIndex;
        }
        return 0;
    }

    public BufferedImage getPlayerImage() {
        return playerImage;
    }

    public void setCurrentPlayerImage(EntityState state, Direction dir) {
        boolean isSideWay = dir == Direction.RIGHT || dir == Direction.LEFT;
        if(spriteCounter > (isSideWay ? 10 : 12)) {
            isLeftLeg = !isLeftLeg;
            if(isSideWay) {
                imageIndex = increaseImageIndex(imageIndex, 4);
            }
            spriteCounter = 0;
        }
        if(state == EntityState.STANDING) {
            imageIndex = 0;
        } else if(!isSideWay) {
            imageIndex = isLeftLeg ? 1 : 2;
        }
        switch(dir) {
            case UP:
                playerImage = upImage[imageIndex];
                break;
            case DOWN:
                playerImage = downImage[imageIndex];
                break;
            case RIGHT:
                playerImage = rightImage[imageIndex];
                break;
            case LEFT:
                playerImage = leftImage[imageIndex];
                break;
            default:
                break;
        }
        spriteCounter++;
    }

    public BufferedImage[] loadPlayerImage(String path, int numOfSprite) {
        BufferedImage[] bf = new BufferedImage[numOfSprite];
        for(int i = 0; i < numOfSprite; i++) {
            bf[i] = loader.loadImage(path + (i+1) + ".png");
        }
        return bf;
    }

    public void draw(Graphics g) {
        g.drawImage(playerImage, x, y, render.getUnitSize(), render.getUnitSize(), null);
        // g.fillRect(solidArea.x+render.getCamX(), solidArea.y+render.getCamY(), solidArea.width, solidArea.height);
    }

}