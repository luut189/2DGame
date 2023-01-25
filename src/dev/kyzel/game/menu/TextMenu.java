package dev.kyzel.game.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import dev.kyzel.gfx.Renderer;

/**
 * The text menu super class.
 */
public class TextMenu extends Menu {

    /**
     * The texts in the menu.
     */
    private final String[] text;

    /**
     * The texts' width.
     */
    private final int[] textWidth;

    /**
     * The texts' height.
     */
    private final int[] textHeight;

    /**
     * The fonts list.
     */
    private final Font[] fontList;

    /**
     * A variable to see if the menu only uses one font.
     */
    private final boolean useOneFont;
    
    /**
     * Creates a new text menu.
     * 
     * @param render the {@link Renderer} where the menu will be drawn on
     * @param text the texts in the menu
     * @param font the font for the texts
     * @param alignment the alignment of the menu
     */
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
    
    /**
     * Creates a new text menu.
     * 
     * @param render the {@link Renderer} where the menu will be drawn on
     * @param text the texts in the menu
     * @param fontList the fonts for the texts
     * @param alignment the alignment of the menu
     */
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
    
    /**
     * Creates a new text menu.
     * 
     * @param render the {@link Renderer} where the menu will be drawn on
     * @param text the texts in the menu
     * @param font the font for the texts
     */
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

    /**
     * Creates a new text menu.
     * 
     * @param render the {@link Renderer} where the menu will be drawn on
     * @param text the texts in the menu
     * @param fontList the fonts for the texts
     */
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

    /**
     * Import all the texts and fonts.
     */
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

    /**
     * Calculate the x and y coordinates based on the given information.
     */
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

    /**
     * Gets the text size in the given {@link Graphics} context.
     * 
     * @param g the given {@link Graphics} context
     * @param text the text
     * @return the size of the text, represents by a {@link Rectangle2D}
     */
    public Rectangle2D getTextSize(Graphics g, String text) {
        return g.getFontMetrics().getStringBounds(text, g);
    }

    /**
     * {@inheritDoc}
     * 
     * Draws the texts.
     */
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
