package dev.kyzel.game.menu;

import java.awt.Font;

import dev.kyzel.gfx.Renderer;

public class PauseMenu extends TextMenu {
    
    /**
     * Creates a pause menu.
     * 
     * @param render the {@link Renderer} where the menu will be drawn on
     */
    public PauseMenu(Renderer render) {
        super
        (
            render,
            new String[] {
                "PAUSED"
            },
            new Font[] {
                new Font(Font.MONOSPACED, Font.PLAIN, 50),
            },
            X_CENTER | Y_CENTER
        );
    }

}