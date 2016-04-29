package fftradio;

import com.sun.javafx.scene.web.Debugger;
import org.jtransforms.fft.DoubleFFT_1D;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by miste on 3/15/2016.
 */
public class JTransformTest {

    public static void main (String [] args)
    {
        try{
            Double fftSize = Double.valueOf(512);
            Double sampleRate = Double.valueOf(44100);

            String audioFilePath = "C:\\Users\\miste\\Desktop\\The Beginning.wav";
            byte[] audioBytes = ReadFileToByteArray(audioFilePath);
            double[] complexCoefficients = ByteToDoubleArray(audioBytes);
            double[] realCoefficients = complexCoefficients.clone(); //Copy into another array
            DoubleFFT_1D fft = new DoubleFFT_1D(512);
            fft.complexForward(complexCoefficients); //Input array.
            fft.realForward(realCoefficients);
            List<Double> freq = new ArrayList<>();
            for(int i = 0; i<complexCoefficients.length; i++)
            {
                if(complexCoefficients[i] != realCoefficients[i])
                    System.out.println(complexCoefficients[i] + " vs. " + realCoefficients[i]);
                freq.add(i*sampleRate/fftSize);
            }
            //n*Fs/N: n==element, Fs==Sample Rate (44.1 kHz == 44100), N == FFT Size (1024)


            System.out.println("Done");
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        //DoubleFFT_1D fft = new DoubleFFT_1D(512);
        //fft.complexForward();
    }

    public static double[] ByteToDoubleArray(byte[] byteArr){
        double[] arr=new double[byteArr.length];
        for (int i=0;i<arr.length;i++){
            arr[i]=byteArr[i];
        }
        return arr;
    }

    public static byte[] ReadFileToByteArray(String filePath) throws IOException {
        //"Path\to\file
        Path path = Paths.get(filePath);
        byte[] data = Files.readAllBytes(path);
        return data;
    }



}
