// Credit: https://github.com/jdah/microcraft

package dev.kyzel.sfx;

import java.io.BufferedInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * A class to handle Sound.
 */
public class Sound {

    /**
     * The theme song of the game.
     */
    public static final Sound THEME = new Sound("/sound/stew_theme.wav");

    /**
     * The sound when player's score goes up.
     */
    public static final Sound SCORE_UP = new Sound("/sound/score_up.wav");

    /**
     * The sound when player's level goes up.
     */
    public static final Sound LEVEL_UP = new Sound("/sound/level_up.wav");


    /**
     * The sound when an Entity is hurt.
     */
    public static final Sound HURT = new Sound("/sound/hurt.wav");

    /**
     * The sound when player loses.
     */
    public static final Sound LOSE = new Sound("/sound/lose.wav");

    /**
     * The sound when player is hurt.
     */
    public static final Sound PLAYER_HURT = new Sound("/sound/player_hurt.wav");

    /**
     * The sound when player misses.
     */
    public static final Sound MISS = new Sound("/sound/miss.wav");

    
    /**
     * Format of the input audio file.
     */
    private final AudioFormat format;

    /**
     * The bytes of the input file.
     */
    private final byte[] bytes;

    /**
     * The clip that is currently playing.
     */
    private Clip playingClip;

    /**
     * Creates a new Sound with the given path.
     *
     * @param path the path of the sound file
     */
    public Sound(String path) {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(getClass().getResourceAsStream(path)));

            this.format = stream.getFormat();
            this.bytes = stream.readAllBytes();
        } catch (IOException | UnsupportedAudioFileException e) {
            throw new Error(e);
        }
    }

    /**
     * Plays the sound.
     */
    public void play() {
        try {
            playingClip = AudioSystem.getClip();
            playingClip.open(this.format, this.bytes, 0, this.bytes.length);
            playingClip.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops if the sound is currently playing.
     */
    public void stop() {
        if(playingClip == null) return;

        playingClip.stop();
    }

    /**
     * Plays the sound.
     *
     * @param loop the sound will be looped if true, and vice versa
     */
    public void play(boolean loop) {
        try {
            playingClip = AudioSystem.getClip();
            playingClip.open(this.format, this.bytes, 0, this.bytes.length);
            playingClip.start();
            if(loop) playingClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}