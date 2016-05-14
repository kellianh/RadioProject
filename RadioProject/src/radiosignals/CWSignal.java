package radiosignals;

import cwdecoder.MorseProcessor;

import java.io.IOException;

/**
 * Created by Brayden on 5/13/2016.
 */
public class CWSignal
{
    MorseProcessor morseProcessor;
    boolean processed;

    public CWSignal(String filepath) throws IOException, WavFileException {
        morseProcessor = new MorseProcessor(filepath);
        processed = false;
    }

    public String GetPlainText() throws IOException, WavFileException {
        if(!processed)
            this.Process();
        return morseProcessor.resultPlainText();

    }

    public String GetMorseCode() throws IOException, WavFileException {
        if(!processed)
            this.Process();
        return morseProcessor.resultMorseCode();
    }

    public String GetWavInfo()
    {
        return morseProcessor.displayInfo();
    }

    private void Process() throws IOException, WavFileException {
        morseProcessor.process();
        processed = true;
    }




}
