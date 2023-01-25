package dev.kyzel.game.menu;

import java.awt.Font;

import dev.kyzel.game.Game;
import dev.kyzel.gfx.Renderer;

/**
 * A status menu class.
 */
public class StatMenu extends TextMenu {

    /**
     * Creates a status menu, which will display the current status of the player.
     * 
     * @param render the {@link Renderer} where the menu will be drawn on
     * @param game the {@link Game} where the menu will take information from
     */
    public StatMenu(Renderer render, Game game) {
        super
        (
            render,
            new String[] {
                "Level: " + game.getPlayer().getCurrentLevel(),
                "Score: " + game.getPlayer().getScore(),
                "Attack value: " + game.getPlayer().getAttackValue(),
                "Max health: " + game.getPlayer().getMaxHealthValue(),
                "Current health: " + game.getPlayer().getHealthValue(),
                "Is cursed: " + (game.getPlayer().isCursed() ? "Yes" : "No")
            },
            new Font(Font.MONOSPACED, Font.PLAIN, 20),
            X_CENTER | Y_CENTER
        );
    }
    
}