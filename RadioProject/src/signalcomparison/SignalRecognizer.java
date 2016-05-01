package signalcomparison;

import org.jtransforms.fft.DoubleFFT_1D;
import org.xml.sax.SAXException;
import shapecomparison.Shape;
import shapecomparison.ShapeLibrary;
import shapecomparison.ShapePoint;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
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

    public Shape DetermineShape(byte[] wavFile) throws IOException, SAXException, ParserConfigurationException {
        Double fftSize = Double.valueOf(512);
        Double sampleRate = Double.valueOf(44100);

        double[] basewav = {1,2,4,2,20,1,3,6,4,7,2,1,11,15,17,4,3};
        double[] complexCoefficients = ByteToDoubleArray(wavFile);
        double[] realCoefficients = complexCoefficients.clone(); //Copy into another array
        DoubleFFT_1D fft = new DoubleFFT_1D(512);
        fft.complexForward(complexCoefficients); //Input array.
        fft.realForward(realCoefficients);

        List<ShapePoint> shapePoints = new ArrayList<>();
        for(int i = 0; i<complexCoefficients.length; i++)
        {
            ShapePoint sp = new ShapePoint((float)i, (float) complexCoefficients[i], 1);
            shapePoints.add(sp);
            //System.out.println(basewav[i] + " " + sp.Point.y);
            /*
            if(complexCoefficients[i] != realCoefficients[i])
                System.out.println(complexCoefficients[i] + " vs. " + realCoefficients[i]);
            freq.add(i*sampleRate/fftSize);
            */
        }
        //n*Fs/N: n==element, Fs==Sample Rate (44.1 kHz == 44100), N == FFT Size (1024)

        //ShapeLibrary sl = new ShapeLibrary("test");
        Shape shape = new Shape(shapePoints.toArray(new ShapePoint[shapePoints.size()]), "testrtty");
        //sl.AddShape(shape);

        return shape;
    }

    public static double[] ByteToDoubleArray(byte[] byteArr){
        double[] arr=new double[byteArr.length];
        for (int i=0;i<arr.length;i++){
            arr[i]=byteArr[i];
        }
        return arr;
    }

}
