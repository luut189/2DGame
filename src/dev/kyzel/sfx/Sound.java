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

public class Sound {

    public static final Sound THEME = new Sound("/sound/stew_theme.wav");
    public static final Sound SCORE_UP = new Sound("/sound/score_up.wav");
    public static final Sound LEVEL_UP = new Sound("/sound/level_up.wav");

    public static final Sound HURT = new Sound("/sound/hurt.wav");
    public static final Sound LOSE = new Sound("/sound/lose.wav");
    public static final Sound PLAYER_HURT = new Sound("/sound/player_hurt.wav");
    public static final Sound MISS = new Sound("/sound/miss.wav");

    private final AudioFormat format;
    private final byte[] bytes;

    private Clip playingClip;

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

    public void play() {
        try {
            playingClip = AudioSystem.getClip();
            playingClip.open(this.format, this.bytes, 0, this.bytes.length);
            playingClip.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    
    public void stop() {
        if(playingClip == null) return;

        playingClip.stop();
    }

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