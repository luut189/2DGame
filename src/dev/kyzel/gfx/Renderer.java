package dev.kyzel.gfx;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import dev.kyzel.game.Game;
import dev.kyzel.game.KeyHandler;
import dev.kyzel.utils.AssetManager;
import dev.kyzel.utils.TextureLoader;

public class Renderer extends JPanel {

    private int width, height;

    private int originalUnitSize = 16;
    private int scale = 3;

    private int unitSize = originalUnitSize * scale;

    private int FPS;

    private Game game;
    private BufferedImage gameImage;

    public Renderer(KeyHandler keyHandler, int width, int height, int FPS) {
        AssetManager.loadAllRes(new TextureLoader(), unitSize);

        this.width = width/unitSize*unitSize;
        this.height = height/unitSize*unitSize;

        this.FPS = FPS;

        this.setPreferredSize(new Dimension(this.width, this.height));
        this.setDoubleBuffered(true);

        game = new Game(this, keyHandler);
        gameImage = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
    }

    public Graphics getGameImageGraphics() {
        return gameImage.getGraphics();
    }

    public int getFPS() {
        return FPS;
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

    public void drawToScreen() {
        Graphics g = getGraphics();
        if(g == null) {
            System.err.println("No Graphics found, retrying...");
            return;
        }
        g.drawImage(gameImage, 0, 0, width, height, null);
        g.dispose();
    }

    public void setFullscreenAttribute(int width, int height) {
        this.width = width;
        this.height = height;

        gameImage = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
        game.setFullscreenAttribute(this.width, this.height);
    }
}