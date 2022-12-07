package entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import gfx.Renderer;
import utils.Direction;
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
        setRender(render);
        setX(x);
        setY(y);
        setSpeed(4);
    }

    public int increaseImageIndex(int currentIndex, int maxIndex) {
        int newIndex = currentIndex + 1;
        if(newIndex < maxIndex) {
            return newIndex;
        }
        return 0;
    }

    public void setCurrentPlayerImage(PlayerState state, Direction dir) {
        boolean isSideWay = dir == Direction.RIGHT || dir == Direction.LEFT;
        if(spriteCounter > 10) {
            isLeftLeg = !isLeftLeg;
            if(isSideWay) {
                imageIndex = increaseImageIndex(imageIndex, 4);
            }
            spriteCounter = 0;
        }
        if(state == PlayerState.STANDING) {
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
        g.drawImage(playerImage, getX(), getY(), getRender().getUnitSize(), getRender().getUnitSize(), null);
    }

}