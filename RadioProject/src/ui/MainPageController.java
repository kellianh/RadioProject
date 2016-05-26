package ui;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.xml.sax.SAXException;
import radiosignals.CWSignal;
import rttydecoder.RTTYDecoder;
import rttydecoder.RTTYDecoderHandler;
import shapecomparison.Shape;
import shapecomparison.ShapeLibrary;
import shapecomparison.ShapeResult;
import signalcomparison.SignalRecognizer;
import utilities.AudioPlayer;
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
    public Label lMessage;

    public TextField tfAddShapeName;

    final FileChooser fileChooser = new FileChooser();
    SignalRecognizer recognizer = new SignalRecognizer();
    //ShapeLibrary sl = new ShapeLibrary("dictionary32");
    ShapeLibrary sl = new ShapeLibrary("dictionary128j");
    //ShapeLibrary sl = new ShapeLibrary("dictionary256");
    //ShapeLibrary sl = new ShapeLibrary("test");
    Shape currentShape;
    Thread rttyThread;


    //Main button - Load
    public void handlebLoadWav_Click()
    {
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("WAV files (*.wav)", "*.wav");
        fileChooser.getExtensionFilters().add(extFilter);
        File initialPath = new File(new java.io.File("").getAbsolutePath() + "/resources/wavs/");
        fileChooser.setInitialDirectory(initialPath);

        //Show open file dialog
        File file = fileChooser.showOpenDialog(null);

        if (file != null)
        {
            try {
                //decode and display
                byte[] audioBytes = FileTools.ReadFileToByteArray(file.getAbsolutePath());
                currentShape = recognizer.DetermineShape(audioBytes);
                ShapeResult sr = currentShape.Recognize(sl);

                lLoadedShape.setText(sr.Name);
                lLoadedShapeScore.setText(Float.toString(sr.Score));

                //Print contents for CW type
                if(sr.Name.equals("CW")) {
                    //check this.
                    CWSignal cw = new CWSignal(file.getAbsolutePath());
                    AudioPlayer.Play(file.getAbsolutePath());
                    lMessage.setText(cw.GetPlainText());
                }
                else if(sr.Name.equals("RTTY") && sr.Score < 0.1f) {
                    lMessage.setText("(processing...)");
                    AudioPlayer.Stop();
                    rttyThread = new Thread(new Runnable() {
                        public void run()
                        {
                                RTTYDecoderHandler rttyDecoderHandler = new RTTYDecoderHandler();
                                RTTYDecoder rttyDecoder = rttyDecoderHandler.decodeRttyWavFile(file.getAbsolutePath());
                                String s = rttyDecoder.getDecodedText();
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        lMessage.setText(s);
                                    }
                                });
                        }
                    });
                    rttyThread.start();
                }
                else {
                    AudioPlayer.Play(file.getAbsolutePath());
                    lMessage.setText("Wave type unable to be decoded.");
                }
            } catch (Exception e) {
                ErrorHandler.HandleException(e, "handlebLoadWav_Click");
            }
        }
        else
        {
            //Operation aborted
        }
    }

    //add to databases
    public void handlebAddShapeToLibrary()
    {
        if(!tfAddShapeName.getText().equals(""))
        {
            if(!currentShape.equals(null))
            {
                currentShape.Name = tfAddShapeName.getText().toUpperCase();
                sl.AddShape(currentShape);
                MessageBox.ShowInformation("Finished", "Added Shape", "Finished adding shape");
            }
            else
                MessageBox.ShowInformation("No shape loaded", "No shape loaded", "Please load shape first");
        }
        else
            MessageBox.ShowInformation("No title", "No title", "Please add a title to the shape you are adding");
    }

    public void handlebStopAudioProcessing()
    {
        AudioPlayer.Stop();
    }
}
