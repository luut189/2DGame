package dev.kyzel.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import dev.kyzel.App;

public class TextureLoader {

    /**
     * Attempts to load an image from the given path.
     * 
     * @param path the path of the image
     * @return the loaded image if loaded successfully, null otherwise
     */
    public static BufferedImage loadImage(String path) {
        InputStream stream = App.class.getResourceAsStream(path);
        if(stream == null) return null;
        try {
            return ImageIO.read(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
