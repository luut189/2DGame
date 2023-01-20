package dev.kyzel.game.menu;

import dev.kyzel.gfx.Renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public abstract class Menu {
    
    public static final int X_LEFT = 1;     // 0b01
    public static final int X_CENTER = 2;   // 0b10
    public static final int X_RIGHT = 3;    // 0b11

    public static final int Y_TOP = 4;      // 0b0100
    public static final int Y_CENTER = 8;   // 0b1000
    public static final int Y_BOTTOM = 12;  // 0b1100

    protected Renderer render;

    protected int x, y;
    protected int width, height;

    protected int alignment;

    public Menu(Renderer render, int x, int y, int width, int height, int alignment) {
        this.render = render;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.alignment = alignment;
    }

    public Menu(Renderer render, int x, int y, int width, int height) {
        this(render, x, y, width, height, X_CENTER | Y_CENTER);
    }

    public Menu(Renderer render, int alignment) {
        this(render, 0, 0, 0, 0, alignment);
    }

    public Menu(Renderer render) {
        this(render, X_CENTER | Y_CENTER);
    }

    public int getXAlignment() {
        return alignment & 0b0011;
    }

    public int getYAlignment() {
        return alignment & 0b1100;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.white);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRoundRect(x, y, width, height, 10, 10);
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRoundRect(x, y, width, height, 10, 10);
    }
}
