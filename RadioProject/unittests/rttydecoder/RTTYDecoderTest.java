package rttydecoder;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Brayden on 5/26/2016.
 */
public class RTTYDecoderTest {

    @Test
    public void testGetDecodedText() throws Exception {
        RTTYDecoderHandler rttyDecoderHandler = new RTTYDecoderHandler();
        RTTYDecoder rttyDecoder = rttyDecoderHandler.decodeRttyWavFile("resources//wavs//rtty//rtty_test_hello.wav");
        String text = rttyDecoder.getDecodedText();
        System.out.println(text);
        assertNotNull(rttyDecoder);
        assertTrue(!text.isEmpty());

    }
}