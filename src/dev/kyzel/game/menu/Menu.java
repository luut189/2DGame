package dev.kyzel.game.menu;

import dev.kyzel.gfx.Renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public abstract class Menu {
    
    /**
     * The constant for the left alignment.
     */
    public static final int X_LEFT = 1;     // 0b01

    /**
     * The constant for the center alignment on the x-axis.
     */
    public static final int X_CENTER = 2;   // 0b10

    /**
     * The constant for the right alignment.
     */
    public static final int X_RIGHT = 3;    // 0b11

    /**
     * The constant for the top alignment.
     */
    public static final int Y_TOP = 4;      // 0b0100

    /**
     * The constant for the center alignment on the y-axis.
     */
    public static final int Y_CENTER = 8;   // 0b1000

    /**
     * The constant for the bottom alignment.
     */
    public static final int Y_BOTTOM = 12;  // 0b1100

    /**
     * The {@link Renderer} where the menu will be drawn on.
     */
    protected Renderer render;

    /**
     * The x coordinate of the menu.
     */
    protected int x;

    /**
     * The y coordinate of the menu.
     */
    protected int y;

    /**
     * The width of the menu.
     */
    protected int width;

    /**
     * The height of the menu.
     */
    protected int height;

    /**
     * The alignment of the menu.
     */
    protected int alignment;

    /**
     * Creates a new menu.
     * 
     * @param render the {@link Renderer} where the menu will be drawn on
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width of the menu
     * @param height the height of the menu
     * @param alignment the alignment of the menu
     */
    public Menu(Renderer render, int x, int y, int width, int height, int alignment) {
        this.render = render;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.alignment = alignment;
    }

    /**
     * Creates a new menu, aligned to the center of the screen by default.
     * 
     * @param render the {@link Renderer} where the menu will be drawn on
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width of the menu
     * @param height the height of the menu
     */
    public Menu(Renderer render, int x, int y, int width, int height) {
        this(render, x, y, width, height, X_CENTER | Y_CENTER);
    }

    /**
     * Creates a menu without x, y coordinates and width, height.
     * 
     * @param render the {@link Renderer} where the menu will be drawn on
     * @param alignment the alignment of the menu
     */
    public Menu(Renderer render, int alignment) {
        this(render, 0, 0, 0, 0, alignment);
    }

    /**
     * Creates a new menu with no information, aligned to the center of the screen by default.
     * 
     * @param render the {@link Renderer} where the menu will be drawn on
     */
    public Menu(Renderer render) {
        this(render, X_CENTER | Y_CENTER);
    }

    /**
     * Gets the x-axis alignment.
     * 
     * @return the x-axis alignment
     */
    public int getXAlignment() {
        return alignment & 0b0011;
    }

    /**
     * Gets the y-axis alignment.
     * 
     * @return the y-axis alignment
     */
    public int getYAlignment() {
        return alignment & 0b1100;
    }

    /**
     * Draws the container of the menu.
     * 
     * @param g the {@link Graphics} which is used to draw
     */
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.white);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRoundRect(x, y, width, height, 10, 10);
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRoundRect(x, y, width, height, 10, 10);
    }
}
