package dev.kyzel.game.menu;

import java.awt.Font;

import dev.kyzel.game.Game;
import dev.kyzel.gfx.Renderer;

/**
 * The game-over menu class.
 */
public class OverMenu extends TextMenu {
    
    /**
     * Creates a game-over menu, which will display the score of the player.
     * 
     * @param render the {@link Renderer} where the menu will be drawn on
     * @param game the {@link Game} where the menu will take information from
     */
    public OverMenu(Renderer render, Game game) {
        super
        (
            render,
            new String[] {
                "GAME OVER",
                "Score: " + game.getPlayer().getScore()
            },
            new Font[] {
                new Font(Font.MONOSPACED, Font.BOLD, 50),
                new Font(Font.MONOSPACED, Font.PLAIN, 25)
            },
            X_CENTER | Y_CENTER
        );
    }
    
}
