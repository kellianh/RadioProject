package fftradio;

import org.jtransforms.fft.DoubleFFT_1D;
import signalcomparison.SignalRecognizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brayden on 4/30/2016.
 */
public class BraydenTestMain
{
    public static void main (String [] args)
    {
        try{
            Double fftSize = Double.valueOf(512);
            Double sampleRate = Double.valueOf(44100);

            String audioFilePath = "C:\\Users\\miste\\Desktop\\The Beginning.wav";
            byte[] audioBytes = ReadFileToByteArray(audioFilePath);

            SignalRecognizer recognizer = new SignalRecognizer();
            recognizer.DetermineRadioSignal(audioBytes);

            System.out.println("Done");
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        //DoubleFFT_1D fft = new DoubleFFT_1D(512);
        //fft.complexForward();
    }



    public static byte[] ReadFileToByteArray(String filePath) throws IOException {
        //"Path\to\file
        Path path = Paths.get(filePath);
        byte[] data = Files.readAllBytes(path);
        return data;
    }
}
