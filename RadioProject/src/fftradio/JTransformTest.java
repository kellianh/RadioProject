package fftradio;

import com.sun.javafx.scene.web.Debugger;
import org.jtransforms.fft.DoubleFFT_1D;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by miste on 3/15/2016.
 */
public class JTransformTest {

    public static void main (String [] args)
    {
        try{
            String audioFilePath = "C:\\Users\\miste\\Desktop\\The Beginning.wav";
            byte[] audioBytes = ReadFileToByteArray(audioFilePath);
            double[] audioDoubles = ByteToDoubleArray(audioBytes);
            DoubleFFT_1D fft = new DoubleFFT_1D(512);
            System.out.println(audioDoubles.toString());
            fft.complexForward(audioDoubles); //Input array
            System.out.println(audioDoubles.toString());
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
