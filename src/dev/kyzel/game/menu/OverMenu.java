package dev.kyzel.game.menu;

import java.awt.Font;

import dev.kyzel.game.Game;
import dev.kyzel.gfx.Renderer;

public class OverMenu extends TextMenu {
    
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
            }
        );
    }
    
}
