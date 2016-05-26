package fftradio;

import org.jtransforms.fft.DoubleFFT_1D;
import shapecomparison.Shape;
import signalcomparison.SignalRecognizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

/**
 * Created by Brayden on 4/30/2016.
 */
public class BraydenTestMain extends Application
{

    @Override public void start(Stage stage) {
        try{
        Double fftSize = Double.valueOf(512);
        Double sampleRate = Double.valueOf(44100);

        String audioFilePath = "D:\\Brayden\\Dropbox\\My Cloud Data\\Global Documents\\Biola Documents\\Spring 2016\\Software Engineering\\Sound Files\\wavs\\4radio-audio-rtty-170-45.wav";
        byte[] audioBytes = ReadFileToByteArray(audioFilePath);

        SignalRecognizer recognizer = new SignalRecognizer();
        Shape shape = recognizer.DetermineShape(audioBytes);

        stage.setTitle("Signal Graph");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        yAxis.setLabel("Freq");
        //creating the chart
        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setTitle("Signal Graph");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        //populating the series with data
        for (int i = 0; i < shape.Points.length; i++) {
            series.getData().add(new XYChart.Data(shape.Points[i].Point.x, shape.Points[i].Point.x));
        }

        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().add(series);

        stage.setScene(scene);
        stage.show();
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        //DoubleFFT_1D fft = new DoubleFFT_1D(512);
        //fft.complexForward();
    }

    public static void main (String [] args)
    {
            launch(args);
    }

    public static byte[] ReadFileToByteArray(String filePath) throws IOException {
        //"Path\to\file
        Path path = Paths.get(filePath);
        byte[] data = Files.readAllBytes(path);
        return data;
    }
}
