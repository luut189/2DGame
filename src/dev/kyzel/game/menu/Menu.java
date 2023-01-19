package dev.kyzel.game.menu;

import dev.kyzel.gfx.Renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public abstract class Menu {

    protected Renderer render;

    protected int x, y;
    protected int width, height;

    public Menu(Renderer render, int x, int y, int width, int height) {
        this.render = render;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Menu(Renderer render) {
        this(render, 0, 0, 0, 0);
    }

    public Menu(Renderer render, int width, int height) {
        this(render, 0, 0, width, height);
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
