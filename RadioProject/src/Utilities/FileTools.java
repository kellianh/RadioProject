package utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Brayden on 4/30/2016.
 */
public class FileTools {

    static File resDirectory = new File("resources//");

    /// <summary>
    /// Read contents of a file
    /// </summary>
    /// <param name="path">Path of the file</param>
    /// <returns>Contents of the file</returns>
    public static String Read(String path) throws IOException {

        Path actualPath = Paths.get(path);
        String result = "";
        result = new String(Files.readAllBytes(actualPath));

        return result;
    }

    /// <summary>
    /// Write contents to a file
    /// </summary>
    /// <param name="path">Path of the file</param>
    /// <param name="contents">Contents of the file</param>
    public static void Write(String path, String contents) throws IOException {

        //Make sure directory exists
        if(!resDirectory.isDirectory())
        {
            resDirectory.mkdir();
        }

        //Make sure file exists
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
        bw.write(contents);
        bw.close();
    }

    /// <summary>
    /// Check if the file exists
    /// </summary>
    /// <param name="path">Path of the file</param>
    /// <returns>bool</returns>
    public static boolean Exists(String path) {
        Path actualPath = Paths.get(path);
        return Files.exists(actualPath);
    }

    public static byte[] ReadFileToByteArray(String filePath) throws IOException {
        //Path to file
        Path path = Paths.get(filePath);
        byte[] data = Files.readAllBytes(path);
        return data;
    }
} 
