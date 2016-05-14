package radiosignals;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Brayden on 5/13/2016.
 */
public class CWSignalTest
{
    @Test
    public void testCWSignalGetPlainText() throws Exception {
        CWSignal cw = new CWSignal("resources/wavs/cw/sos.wav");
        String message = cw.GetPlainText();
        String desiredMessage = "SOS ";
        assertEquals(desiredMessage, message);
    }

    @Test
    public void testCWSignalGetMorseCode() throws Exception {
        CWSignal cw = new CWSignal("resources/wavs/cw/sos.wav");
        String message = cw.GetMorseCode();
        String desiredMessage = "... --- ...   ";
        assertEquals(desiredMessage, message);
    }
}