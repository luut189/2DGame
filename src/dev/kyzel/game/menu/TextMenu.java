package dev.kyzel.game.menu;

import dev.kyzel.gfx.Renderer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

public class TextMenu extends Menu {

    private final String[] text;
    private final int[] textWidth;
    private final int[] textHeight;

    private final Font font;

    public TextMenu(Renderer render, String text, Font font) {
        super(render);
        this.font = font;
        this.text = new String[1];
        this.textWidth = new int[1];
        this.textHeight = new int[1];

        this.text[0] = text;

        Graphics g = render.getGameImageGraphics();
        g.setFont(font);
        textWidth[0] = (int) getTextSize(g, text).getWidth();
        textHeight[0] = (int) getTextSize(g, text).getHeight();

        width = textWidth[0] + textWidth[0]/2;
        height = textHeight[0] + textHeight[0]/2;

        x = render.getWidth()/2 - width/2;
        y = render.getHeight()/2 - height/2;
    }

    public TextMenu(Renderer render, String[] text, Font font) {
        super(render);
        this.font = font;
        this.text = text;
        this.textWidth = new int[text.length];
        this.textHeight = new int[text.length];

        Graphics g = render.getGameImageGraphics();
        g.setFont(font);
        for(int i = 0; i < text.length; i++) {
            String str = text[i];

            textWidth[i] = (int) getTextSize(g, str).getWidth();
            textHeight[i] = (int) getTextSize(g, str).getHeight();
            if(i == 0) textHeight[i] -= textHeight[i]/2;

            width = Math.max(width, textWidth[i]);
            height += textHeight[i];
        }
        width += width/2;
        height += height/2;

        x = render.getWidth()/2 - width/2;
        y = render.getHeight()/2 - height/2;
    }

    public Rectangle2D getTextSize(Graphics g, String text) {
        return g.getFontMetrics().getStringBounds(text, g);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        g.setFont(font);
        g.setColor(Color.white);
        for(int i = text.length-1; i >= 0; i--) {
            String str = text[i];
            int currentX = textWidth[i];
            int currentY = textHeight[i];
            if(text.length > 1) currentY *= i;

            g.drawString(str, render.getWidth()/2 - currentX/2, render.getHeight()/2 + currentY/4);
        }
    }
}
