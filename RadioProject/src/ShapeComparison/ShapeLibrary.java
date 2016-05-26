package shapecomparison;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import utilities.BasicLogger;
import utilities.ErrorHandler;
import utilities.FileTools;

/**
 * Created by miste on 4/28/2016.
 */
public class ShapeLibrary
{
    String libraryName;
    String libraryFilename;
    String persistentLibraryPath;
    public List<Shape> library = new ArrayList<>();
    Gson gson = new Gson();


    public ShapeLibrary(String libraryName) {
        try
        {
            this.libraryName = libraryName;
            this.libraryFilename = libraryName + ".json";
            this.persistentLibraryPath = "resources//"+ libraryFilename;

            LoadLibrary();
        }
        catch (Exception e)
        {
            ErrorHandler.HandleException(e,"ShapeLibrary");
        }
    }


    public void LoadLibrary() throws IOException, SAXException {
        if(FileTools.Exists(this.persistentLibraryPath))
        {
            Type listType = new TypeToken<ArrayList<Shape>>() {
            }.getType();
            this.library = gson.fromJson(FileTools.Read(this.persistentLibraryPath), listType);
        }

    }


    public boolean AddShape(Shape shape) {
        try {
            // Add the new gesture to the list of gestures
            this.library.add(shape);
            FileTools.Write(persistentLibraryPath, gson.toJson(this.library));

            return true;
        } catch (Exception e) {
            ErrorHandler.HandleException(e, "ShapeLibrary, AddShape()");
            return false;
        }

    }

}

