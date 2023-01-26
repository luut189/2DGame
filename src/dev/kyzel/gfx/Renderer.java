package dev.kyzel.gfx;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import dev.kyzel.game.Game;
import dev.kyzel.utils.AssetManager;

/**
 * A class to handle drawing stuff to the screen.
 */
public class Renderer extends JPanel {

    /**
     * The width and height of this instance.
     */
    private int width, height;

    /**
     * The unit size of sprites.
     */
    private final int originalUnitSize = 16;

    /**
     * The desired scale.
     */
    private final int scale = 3;

    /**
     * The actual unit size of sprites after scaling.
     */
    private final int unitSize = originalUnitSize * scale;

    /**
     * The frame rate (FPS).
     */
    private final int FPS;

    /**
     * The game to render.
     */
    private final Game game;

    /**
     * The main {@link BufferedImage}, which the Renderer uses to draw on.
     */
    private BufferedImage gameImage;

    /**
     * Creates a new Renderer.
     *
     * @param width the width of the drawing canvas
     * @param height the height of the drawing canvas
     * @param FPS the desired frame rate (FPS)
     */
    public Renderer(int width, int height, int FPS) {
        AssetManager.loadAllRes(unitSize);

        this.width = width/unitSize*unitSize;
        this.height = height/unitSize*unitSize;

        this.FPS = FPS;

        this.setPreferredSize(new Dimension(this.width, this.height));
        this.setDoubleBuffered(true);

        gameImage = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
        game = new Game(this);
    }

    /**
     * Creates a graphics context of {@link #gameImage}.
     * @return the graphics context of {@link #gameImage}
     */
    public Graphics getGameImageGraphics() {
        return gameImage.getGraphics();
    }

    /**
     * Gets the frame rate (FPS).
     * @return the frame rate (FPS)
     */
    public int getFPS() {
        return FPS;
    }

    /**
     * Gets the scale of unit size
     * @return the scale of unit size
     */
    public int getUnitScale() {
        return scale;
    }

    /**
     * Gets the actual unit size
     * @return the actual unit size
     */
    public int getUnitSize() {
        return unitSize;
    }

    /**
     * Gets the width of this instance.
     * @return the width of this instance
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of this instance.
     * @return the height of this instance
     */
    public int getHeight() {
        return height;
    }

    /**
     * Draws the {@link #gameImage} to the window.
     */
    public void drawToScreen() {
        Graphics g = getGraphics();
        if(g == null) {
            System.err.println("No Graphics found, retrying...");
            return;
        }
        g.drawImage(gameImage, 0, 0, width, height, null);
        g.dispose();
    }

    /**
     * Set the attributes of this instance to match with the new width and height.
     *
     * @param width the new width
     * @param height the new height
     */
    public void setAttributes(int width, int height) {
        this.width = width;
        this.height = height;

        gameImage = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
        game.setAttributes(this.width, this.height);
    }
}