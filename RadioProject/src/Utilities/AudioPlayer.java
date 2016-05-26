package utilities;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Brayden on 5/26/2016.
 */
public class AudioPlayer
{
    static Clip clip;
    public static void Play(String audioFilePath) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        File soundFile = new File(audioFilePath);
        AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);
        Line.Info linfo = new Line.Info(Clip.class);
        Line line = AudioSystem.getLine(linfo);
        clip = (Clip) line;
        clip.open(ais);
        clip.start();
    }

    public static void Stop()
    {
        clip.stop();
    }
}
