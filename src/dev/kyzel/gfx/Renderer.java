package dev.kyzel.gfx;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import dev.kyzel.game.Game;
import dev.kyzel.utils.AssetManager;
import dev.kyzel.utils.TextureLoader;

public class Renderer extends JPanel {

    private KeyHandler keyHandler;

    private int width, height;
    
    private int screenWidth, screenHeight;

    private int originalUnitSize = 16;
    private int scale = 3;

    private int unitSize = originalUnitSize * scale;

    private int FPS;

    private Game game;
    private BufferedImage gameImage;

    public Renderer(KeyHandler keyHandler, int width, int height, int FPS) {
        AssetManager.loadAllRes(new TextureLoader(), unitSize);
        this.keyHandler = keyHandler;

        this.width = width/unitSize*unitSize;
        this.height = height/unitSize*unitSize;
        
        screenWidth = this.width;
        screenHeight = this.height;

        this.FPS = FPS;

        this.setPreferredSize(new Dimension(this.width, this.height));
        this.setDoubleBuffered(true);

        game = new Game(this);
        gameImage = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
    }

    public Graphics getGameImageGraphics() {
        return gameImage.getGraphics();
    }

    public int getFPS() {
        return FPS;
    }

    public KeyHandler getKeyHandler() {
        return keyHandler;
    }

    public int getUnitScale() {
        return scale;
    }

    public int getUnitSize() {
        return unitSize;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public void drawToScreen() {
        Graphics g = getGraphics();
        if(g == null) {
            System.err.println("No Graphics found, retrying...");
            return;
        }
        g.drawImage(gameImage, 0, 0, screenWidth, screenHeight, null);
        g.dispose();
    }

    public void setFullscreenAttribute(int width, int height) {
        this.width = width;
        this.height = height;

        screenWidth = this.width;
        screenHeight = this.height;

        gameImage = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        game.setFullscreenAttribute(this.width, this.height);
    }
}