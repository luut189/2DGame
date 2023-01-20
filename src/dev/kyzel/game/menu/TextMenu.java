package dev.kyzel.game.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import dev.kyzel.gfx.Renderer;

public class TextMenu extends Menu {

    private final String[] text;
    private final int[] textWidth;
    private final int[] textHeight;

    private final Font[] fontList;
    private final boolean useOneFont;
    
    // constructors with alignment
    public TextMenu(Renderer render, String[] text, Font font, int alignment) {
        super(render, alignment);

        this.fontList = new Font[1];
        this.fontList[0] = font;
        useOneFont = true;

        this.text = text;
        this.textWidth = new int[text.length];
        this.textHeight = new int[text.length];
        
        importTextAndFont();
        calculateXY();
    }

    public TextMenu(Renderer render, String[] text, Font[] fontList, int alignment) {
        super(render, alignment);

        this.fontList = fontList;
        useOneFont = false;

        this.text = text;
        this.textWidth = new int[text.length];
        this.textHeight = new int[text.length];

        importTextAndFont();
        calculateXY();
    }
    
    // constructors without alignment
    public TextMenu(Renderer render, String[] text, Font font) {
        super(render);

        this.fontList = new Font[1];
        this.fontList[0] = font;
        useOneFont = true;

        this.text = text;
        this.textWidth = new int[text.length];
        this.textHeight = new int[text.length];
        
        importTextAndFont();
        calculateXY();
    }

    public TextMenu(Renderer render, String[] text, Font[] fontList) {
        super(render);

        this.fontList = fontList;
        useOneFont = false;

        this.text = text;
        this.textWidth = new int[text.length];
        this.textHeight = new int[text.length];

        importTextAndFont();
        calculateXY();
    }

    public void importTextAndFont() {
        Graphics g = render.getGameImageGraphics();
        for(int i = 0; i < text.length; i++) {
            String str = text[i];
            g.setFont(useOneFont ? fontList[0] : fontList[i]);
            textWidth[i] = (int) getTextSize(g, str).getWidth();
            textHeight[i] = (int) getTextSize(g, str).getHeight();
            
            width = Math.max(width, textWidth[i]);
            height += textHeight[i];
        }
        width += width/2;
        height += textHeight[text.length-1]/2;

    }

    public void calculateXY() {
        int xAlignment = getXAlignment();
        int yAlignment = getYAlignment();
        switch (xAlignment) {
            case X_LEFT -> x = width/2;
            case X_CENTER -> x = render.getWidth()/2 - width/2;
            case X_RIGHT -> x = render.getWidth() - width/2 - width;
            default -> {}
        }
        switch (yAlignment) {
            case Y_TOP -> y = height/2;
            case Y_CENTER -> y = render.getHeight()/2 - height/2;
            case Y_BOTTOM -> y = render.getHeight() - height/2 - height;
            default -> {}
        }
    }

    public Rectangle2D getTextSize(Graphics g, String text) {
        return g.getFontMetrics().getStringBounds(text, g);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        g.setColor(Color.white);
        int previousHeight = 0;
        for(int i = 0; i < text.length; i++) {
            String str = text[i];
            g.setFont(useOneFont ? fontList[0] : fontList[i]);
            int currentX = x + width/2 - textWidth[i]/2;
            int currentY = y + textHeight[i] + previousHeight;
            previousHeight += textHeight[i];

            g.drawString(str, currentX, currentY);
        }
    }
}
