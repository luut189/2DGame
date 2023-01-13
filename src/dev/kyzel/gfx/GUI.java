package dev.kyzel.gfx;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

import dev.kyzel.utils.AssetManager;

public class GUI extends JFrame {

    private KeyHandler keyHandler;
    private Renderer render;

    private GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

    private static GUI window;

    private GUI(int width, int height, int FPS) {
        keyHandler = new KeyHandler(this);
        render = new Renderer(keyHandler, width, height, FPS);

        this.setTitle("Stew the Wanderer");
        this.setIconImage(AssetManager.downImage[0]);
        this.add(render);
        this.pack();
        this.addKeyListener(keyHandler);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }

    public static GUI init(int width, int height, int FPS) {
        if(window == null) {
            window = new GUI(width, height, FPS);
        }
        return window;
    }

    public void setFullscreen() {
        if(gd.getFullScreenWindow() == null) {
            this.dispose();
            this.setUndecorated(true);
            gd.setFullScreenWindow(this);
            render.setFullscreenAttribute(getWidth(), getHeight());
        } else {
            this.dispose();
            this.setUndecorated(false);
            this.setVisible(true);
            gd.setFullScreenWindow(null);
            render.setFullscreenAttribute(getWidth(), getHeight()-32);
        }
    }
    
}
