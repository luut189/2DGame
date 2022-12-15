package entity;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import gfx.Renderer;
import utils.AssetManager;

public class Player extends Entity {

    private int imageIndex = 0;
    private BufferedImage playerImage = AssetManager.downImage[imageIndex];

    private boolean isLeftLeg = false;
    private int spriteCounter = 0;

    public Player(Renderer render, int x, int y) {
        this.render = render;
        this.x = x;
        this.y = y;
        this.speed = 4;

        int offsetX = render.getUnitSize()/4;
        int offsetY = render.getUnitSize()/4;

        int rectWidth = render.getUnitSize()-offsetX*2;
        int rectHeight = render.getUnitSize()-offsetY;
        
        this.solidArea = new Rectangle(offsetX, offsetY, rectWidth, rectHeight);
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
        if(state == EntityState.WALKING) {
            boolean isSideWay = dir == Direction.RIGHT || dir == Direction.LEFT;
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
            switch(dir) {
                case UP:
                    playerImage = AssetManager.upImage[imageIndex];
                    break;
                case DOWN:
                    playerImage = AssetManager.downImage[imageIndex];
                    break;
                case RIGHT:
                    playerImage = AssetManager.rightImage[imageIndex];
                    break;
                case LEFT:
                    playerImage = AssetManager.leftImage[imageIndex];
                    break;
                default:
                    break;
            }
            spriteCounter++;
        } else if(state == EntityState.SWIMMING) {
            switch(dir) {
                case UP:
                    playerImage = AssetManager.upSwimmingImage;
                    break;
                case DOWN:
                    playerImage = AssetManager.downSwimmingImage;
                    break;
                case RIGHT:
                    playerImage = AssetManager.rightSwimmingImage;
                    break;
                case LEFT:
                    playerImage = AssetManager.leftSwimmingImage;
                    break;
                default:
                    break;
            }
        } else if(state == EntityState.STANDING) {
            imageIndex = 0;
        }
    }

    public void draw(Graphics g) {
        g.drawImage(playerImage, x, y, null);
//        g.fillRect(solidArea.x+render.getCamX(), solidArea.y+render.getCamY(), solidArea.width, solidArea.height);
    }

}