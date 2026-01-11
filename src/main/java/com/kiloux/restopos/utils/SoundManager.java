package com.kiloux.restopos.utils;

import java.io.*;
import javax.sound.sampled.*;
import java.util.concurrent.CompletableFuture;

public class SoundManager {
    
    private static final String SOUND_DIR = "src/main/java/com/kiloux/restopos/assets/sound/";
    private static SoundManager instance;
    
    private SoundManager() {
        File dir = new File(SOUND_DIR);
        if (!dir.exists()) dir.mkdirs();
        
        // Check and generate if missing
        if (!new File(SOUND_DIR + "startup.wav").exists()) SoundGenerator.generateAll(SOUND_DIR);
    }
    
    public static SoundManager getInstance() {
        if (instance == null) instance = new SoundManager();
        return instance;
    }
    
    public void play(String name) {
        CompletableFuture.runAsync(() -> {
            try {
                // Priority 1: Check for MP3 (User provided)
                File mp3 = new File(SOUND_DIR + name + ".mp3");
                if (mp3.exists()) {
                    FileInputStream fs = new FileInputStream(mp3);
                    javazoom.jl.player.Player player = new javazoom.jl.player.Player(fs);
                    player.play();
                    return;
                }
                
                // Priority 2: Check for WAV (System generated)
                File wav = new File(SOUND_DIR + name + ".wav");
                if (wav.exists()) {
                    AudioInputStream audioIn = AudioSystem.getAudioInputStream(wav);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioIn);
                    clip.start();
                }
            } catch (Exception e) {
                System.err.println("Audio Error: " + e.getMessage());
            }
        });
    }
}
