package com.kiloux.restopos.utils;

import java.io.*;

public class SoundGenerator {

    public static void generateAll(String dir) {
        try {
            // Vista Startup: Swell (C Major 9)
            saveWav(dir + "startup.wav", generateChord(new double[]{261.63, 329.63, 392.00, 523.25}, 3.0, true)); // C4, E4, G4, C5
            
            // Shutdown: Descending Fade
            saveWav(dir + "shutdown.wav", generateChord(new double[]{392.00, 329.63, 261.63}, 2.0, false));
            
            // Error: Low Sawtooth
            saveWav(dir + "error.wav", generateTone(110.0, 0.4, "SAW"));
            
            // Success: High Ping
            saveWav(dir + "success.wav", generateTone(880.0, 0.4, "SINE"));
            
            // Click
            saveWav(dir + "click.wav", generateTone(1200.0, 0.05, "SINE"));
            
        } catch(Exception e) { e.printStackTrace(); }
    }
    
    private static byte[] generateChord(double[] freqs, double duration, boolean fade) {
        int sampleRate = 44100;
        int numSamples = (int) (duration * sampleRate);
        byte[] buffer = new byte[numSamples * 2]; // 16-bit
        
        for (int i = 0; i < numSamples; i++) {
            double t = (double) i / sampleRate;
            double sample = 0;
            
            // Add frequencies
            for (double f : freqs) {
                sample += Math.sin(2 * Math.PI * f * t);
            }
            sample /= freqs.length; // Normalize
            
            // Envelope
            double vol = 0.8;
            if (fade) {
                // Fade in then out
                if (t < 0.5) vol *= (t / 0.5);
                else if (t > duration - 1.0) vol *= ((duration - t) / 1.0);
            } else {
                // Fade out linear
                 vol *= (1.0 - (t / duration));
            }

            short s = (short) (sample * vol * 32767);
            buffer[2*i] = (byte) (s & 0xff);
            buffer[2*i+1] = (byte) ((s >> 8) & 0xff);
        }
        return buffer;
    }
    
    private static byte[] generateTone(double freq, double duration, String type) {
        int sampleRate = 44100;
        int numSamples = (int) (duration * sampleRate);
        byte[] buffer = new byte[numSamples * 2];
        
        for (int i = 0; i < numSamples; i++) {
            double t = (double) i / sampleRate;
            double sample = 0;
            
            if ("SAW".equals(type)) {
                 sample = 2.0 * ((t * freq) % 1.0) - 1.0;
            } else {
                 sample = Math.sin(2 * Math.PI * freq * t);
            }
            
            // Decay
            double vol = 0.8 * (1.0 - (t/duration));
            
            short s = (short) (sample * vol * 32767);
            buffer[2*i] = (byte) (s & 0xff);
            buffer[2*i+1] = (byte) ((s >> 8) & 0xff);
        }
        return buffer;
    }
    
    private static void saveWav(String path, byte[] data) throws IOException {
        File f = new File(path);
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(f))) {
            // RIFF Header
            dos.writeBytes("RIFF");
            dos.writeInt(Integer.reverseBytes(36 + data.length));
            dos.writeBytes("WAVE");
            
            // fmt chunk
            dos.writeBytes("fmt ");
            dos.writeInt(Integer.reverseBytes(16)); // Chunk size
            dos.writeShort(Short.reverseBytes((short) 1)); // PCM
            dos.writeShort(Short.reverseBytes((short) 1)); // Mono
            dos.writeInt(Integer.reverseBytes(44100)); // Sample Rate
            dos.writeInt(Integer.reverseBytes(44100 * 2)); // Byte Rate
            dos.writeShort(Short.reverseBytes((short) 2)); // Block Align
            dos.writeShort(Short.reverseBytes((short) 16)); // Bits per sample
            
            // data chunk
            dos.writeBytes("data");
            dos.writeInt(Integer.reverseBytes(data.length));
            dos.write(data);
        }
    }
}
