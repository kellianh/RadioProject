package utilities;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Brayden on 5/26/2016.
 */
public class FileToolsTest {

    @Test
    public void testRead() throws Exception {
        String s = FileTools.Read("resources//template.xml");

        assertTrue(!s.isEmpty());
    }

    @Test
    public void testExists() throws Exception {
        boolean shouldTrue = FileTools.Exists("resources//template.xml");
        assertTrue(shouldTrue);

        boolean shouldFalse = FileTools.Exists("resources//abogus file.json");
        assertFalse(shouldFalse);
    }

    @Test
    public void testReadFileToByteArray() throws Exception {
        byte[] byteArray = FileTools.ReadFileToByteArray("resources//template.xml");

        assertNotNull(byteArray);
    }
}