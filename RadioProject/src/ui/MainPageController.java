package ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.xml.sax.SAXException;
import shapecomparison.Shape;
import shapecomparison.ShapeLibrary;
import shapecomparison.ShapeResult;
import signalcomparison.SignalRecognizer;
import utilities.ErrorHandler;
import utilities.FileTools;
import utilities.MessageBox;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Created by Brayden on 5/1/2016.
 */
public class MainPageController
{
    public Button bLoadWav;
    public Button bAddShapeToLibrary;

    public Label lLoadedShape;
    public Label lLoadedShapeScore;

    public TextField tfAddShapeName;

    final FileChooser fileChooser = new FileChooser();
    SignalRecognizer recognizer = new SignalRecognizer();
    ShapeLibrary sl = new ShapeLibrary("test");
    Shape currentShape;

    public void handlebLoadWav_Click()
    {
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("WAV files (*.wav)", "*.wav");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show open file dialog
        File file = fileChooser.showOpenDialog(null);

        if (file != null)
        {
            try {
                byte[] audioBytes = FileTools.ReadFileToByteArray(file.getAbsolutePath());
                currentShape = recognizer.DetermineShape(audioBytes);
                ShapeResult sr = currentShape.Recognize(sl);

                lLoadedShape.setText(sr.Name);
                lLoadedShapeScore.setText(Float.toString(sr.Score));

            } catch (Exception e) {
                ErrorHandler.HandleException(e, "handlebLoadWav_Click");
            }
        }
        else
        {
            //Operation aborted
        }
    }

    public void handlebAddShapeToLibrary()
    {
        if(!tfAddShapeName.getText().equals(""))
        {
            if(!currentShape.equals(null))
            {
                currentShape.Name = tfAddShapeName.getText();
                sl.AddShape(currentShape);
                MessageBox.ShowInformation("Finished", "Added Shape", "Finished adding shape");
            }
            else
                MessageBox.ShowInformation("No shape loaded", "No shape loaded", "Please load shape first");
        }
        else
            MessageBox.ShowInformation("No title", "No title", "Please add a title to the shape you are adding");
    }
}
