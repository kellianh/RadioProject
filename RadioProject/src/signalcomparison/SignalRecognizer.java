package signalcomparison;

import org.jtransforms.fft.DoubleFFT_1D;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brayden on 4/30/2016.
 */
public class SignalRecognizer
{
    public static void main(String[] args)
    {

    }

    public void DetermineRadioSignal(byte[] wavFile)
    {
        Double fftSize = Double.valueOf(512);
        Double sampleRate = Double.valueOf(44100);
        double[] complexCoefficients = ByteToDoubleArray(wavFile);
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
    }

    public static double[] ByteToDoubleArray(byte[] byteArr){
        double[] arr=new double[byteArr.length];
        for (int i=0;i<arr.length;i++){
            arr[i]=byteArr[i];
        }
        return arr;
    }

}
