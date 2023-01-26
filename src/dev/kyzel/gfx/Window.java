package dev.kyzel.gfx;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

import dev.kyzel.utils.AssetManager;
import dev.kyzel.utils.Keyboard;

/**
 * A class to handle creating and managing a Window.
 */
public class Window extends JFrame {

    /**
     * The renderer of this Window.
     */
    private final Renderer render;

    /**
     * The GraphicsDevice where the code runs on.
     */
    private final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

    /**
     * The only Window that should be used.
     */
    public static Window window;

    /**
     * Creates a new Window.
     * Construction should only be done with {@link #init(int, int, int)} or {@link #init()}.
     *
     * @param width the width of the Window
     * @param height the height of the Window
     * @param FPS the desired frame rate (FPS)
     */
    private Window(int width, int height, int FPS) {
        render = new Renderer(width, height, FPS);

        this.setTitle("Stew the Wanderer");
        this.setIconImage(AssetManager.downImage[0]);
        this.add(render);
        this.pack();
        this.addKeyListener(Keyboard.getListener());
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }

    /**
     * Creates a new Window if there is none available.
     *
     * @param width the width of the Window
     * @param height the height of the Window
     * @param FPS the desired frame rate (FPS)
     * @return the available or newly created Window
     */
    public static Window init(int width, int height, int FPS) {
        if(window == null) {
            window = new Window(width, height, FPS);
        }
        return window;
    }

    /**
     * Creates a new Window with a dimension of 800x600 and 60 FPS, if there is none available.
     *
     * @return the available or newly created Window
     */
    public static Window init() {
        return init(800, 600, 60);
    }

    /**
     * If the window is not in fullscreen mode, set it to fullscreen mode.
     * If the window is in fullscreen mode, set it back to windowed mode.
     */
    public void setFullscreen() {
        if(gd.getFullScreenWindow() == null) {
            this.dispose();
            this.setUndecorated(true);
            gd.setFullScreenWindow(this);
            render.setAttributes(getWidth(), getHeight());
        } else {
            this.dispose();
            this.setUndecorated(false);
            this.setVisible(true);
            gd.setFullScreenWindow(null);
            render.setAttributes(getWidth(), getHeight()-32);
        }
    }
}