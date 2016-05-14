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

        double[] complexCoefficients = ByteToDoubleArray(wavFile);
        double[] realCoefficients = complexCoefficients.clone(); //Copy into another array
        DoubleFFT_1D fft = new DoubleFFT_1D(512);
        fft.complexForward(complexCoefficients); //Input array.
        fft.realForward(realCoefficients);

        List<ShapePoint> shapePoints = new ArrayList<>();
        for(int i = 0; i<complexCoefficients.length; i++)
        {
            ShapePoint sp = new ShapePoint((float)i, (float) complexCoefficients[i], 0);
            shapePoints.add(sp);

        }
        Shape shape = new Shape(shapePoints.toArray(new ShapePoint[shapePoints.size()]));

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
