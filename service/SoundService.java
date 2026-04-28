package service;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundService {

    private Clip bgmClip;

    public String getBacksound() {
        return "resources/audio/backsound.wav";
    }

    public String getBeliSound() {
        return "resources/audio/beli.wav";
    }

    // Method untuk sound efek (beli, klik, dll)
    public void playSound(String path) {
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new java.io.File(path));
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Sound gagal diputar: " + ex.getMessage());
        }
    }

    // Method untuk background music
    public void playBackgroundMusic(String path) {
        try {
            if (bgmClip != null && bgmClip.isRunning()) {
                bgmClip.stop();
            }
            AudioInputStream audio = AudioSystem.getAudioInputStream(new java.io.File(path));
            bgmClip = AudioSystem.getClip();
            bgmClip.open(audio);
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            bgmClip.start();
        } catch (Exception e) {
            System.out.println("BGM gagal diputar: " + e.getMessage());
        }
    }
}