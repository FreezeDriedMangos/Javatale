import java.util.ArrayList;
import javax.sound.sampled.*;

public class SoundPlayer{
    //private static ArrayList<Sound> sounds = new ArrayList<Sound>();
    private static Sound track;
    private static String trackFilePath;
    //private static Track track;
    /*
    public static void play(String filePath, boolean loop){
        boolean add = true;

        for(Sound s : sounds){
            if(s.filePath.equals(filePath)){
                if(s.clip.isActive()){
                    add = false;
                }
                break;
            }
        }

        if(add){
            sounds.add(new Sound(filePath, loop));
        }
    } */
    
    public static void play(String filePath){
        new Sound(filePath).play();
    }

    public static void playTrack(String filePath){
        //track = new Sound(filePath, true);
        trackFilePath = filePath;
        track = new Sound(filePath);
        track.loop();
    }

    public static void stopTrack(){
        track.stop();
    }
    
    public static String getTrackFilePath(){
        return trackFilePath;
    }
}

/*
class Sound{
    Thread soundThread;
    String filePath;
    Clip clip;

    public Sound(String path, boolean loop){
        filePath = path;

        //code modified from http://stackoverflow.com/questions/2416935/how-to-play-wav-files-with-java
        try {
            AudioInputStream stream;
            AudioFormat format;
            DataLine.Info info;

            stream = AudioSystem.getAudioInputStream(new File(filePath));
            format = stream.getFormat();
            info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(stream);
            clip.start();
            
            if(loop){
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch (Exception e) {
            System.out.println("SoundPlayer, Sound: ERROR \nvia: " + e.getMessage());
        }
    }

    public void stop(){
        clip.stop();
    }
}*/

class Sound {
    private Clip clip;

    public Sound (String fileName) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(Sound.class.getResource(fileName));
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        try {
            if (clip != null) {
                new Thread(){
                    public void run() {
                        synchronized (clip) {
                            clip.stop();
                            clip.setFramePosition(0);
                            clip.start();
                        }
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        if(clip == null) return;
        clip.stop();
    }

    public void loop() {
        try {
            if (clip != null) {
                new Thread() {
                    public void run() {
                        synchronized (clip) {
                            clip.stop();
                            clip.setFramePosition(0);
                            clip.loop(Clip.LOOP_CONTINUOUSLY);
                        }
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isActive(){
        return clip.isActive();
    }
}