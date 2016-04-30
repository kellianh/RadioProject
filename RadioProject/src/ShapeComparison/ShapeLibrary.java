package shapecomparison;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import utilities.BasicLogger;
import utilities.FileTools;

/**
 * Created by miste on 4/28/2016.
 */
public class ShapeLibrary
{
    String libraryName;
    String libraryFilename;
    String persistentLibraryPath;
    String xmlContents;

    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
    Document shapeLibrary;
    public List<Shape> library = new ArrayList<>();



    public ShapeLibrary(String libraryName) throws ParserConfigurationException, IOException, SAXException {
        this.libraryName = libraryName;
        this.libraryFilename = libraryName + ".xml";
        this.persistentLibraryPath = "resources//"+ libraryFilename;

        CopyToPersistentPath(false);
        LoadLibrary();

    }

    public ShapeLibrary(String libraryName, boolean forceCopy) throws ParserConfigurationException, IOException, SAXException {
        this.libraryName = libraryName;
        this.libraryFilename = libraryName + ".xml";
        this.persistentLibraryPath = "resources//"+ libraryFilename;

        CopyToPersistentPath(forceCopy);
        LoadLibrary();

    }


    public void LoadLibrary() throws IOException, SAXException {

        // Uses the XML file in resources folder
        File xmlContents = new File(persistentLibraryPath);

        shapeLibrary = docBuilder.parse(xmlContents); //possibly or create new doc
        shapeLibrary.getDocumentElement().normalize();
        // Get "gesture" elements
        NodeList xmlShapeList = shapeLibrary.getElementsByTagName("shape");

        // Parse "gesture" elements and add them to library
        //for (Node xmlShapeNode : xmlShapeList) {
        for (int i = 0; i < xmlShapeList.getLength(); i++) {
            String shapeName = xmlShapeList.item(i).getAttributes().getNamedItem("name").getNodeValue();
            NodeList xmlPoints = xmlShapeList.item(i).getChildNodes();
            List<ShapePoint> shapePoints = new ArrayList<>();

            //for (Node point : xmlPoints) {
            for(int j = 0; j < xmlPoints.getLength(); j++) {
                float x = Float.parseFloat(xmlPoints.item(j).getAttributes().getNamedItem("x").getNodeValue());
                float y = Float.parseFloat(xmlPoints.item(j).getAttributes().getNamedItem("y").getNodeValue());
                int shapeID = Integer.parseInt(xmlPoints.item(j).getAttributes().getNamedItem("id").getNodeValue());
                shapePoints.add(new ShapePoint(x, y, shapeID));
            }

            Shape shape = new Shape((ShapePoint[])shapePoints.toArray(), shapeName);
            library.add(shape);
        }
    }


    public boolean AddShape(Shape shape) {

        // Create the xml node to add to the xml file
        Element rootElement = shapeLibrary.getDocumentElement();
        Element shapeNode = shapeLibrary.createElement("shape");
        shapeNode.setAttribute("name", shape.Name); //Possible append needed

        for (ShapePoint m : shape.Points) {
            Element shapePoint = shapeLibrary.createElement("point");
            shapePoint.setAttribute("x", Float.toString(m.Point.x));
            shapePoint.setAttribute("y", Float.toString(m.Point.y));
            shapePoint.setAttribute("id", Float.toString(m.StrokeID));

            shapeNode.appendChild(shapePoint);
        }

        // Append the node to xml file contents
        rootElement.appendChild(shapeNode);

        try {

            // Add the new gesture to the list of gestures
            this.library.add(shape);
            FileTools.Write(persistentLibraryPath, XmlDocToString(shapeLibrary));

            return true;
        } catch (Exception e) {
            BasicLogger.LogError(e.getMessage(), "ShapeLibrary, LoadLibrary()");
            return false;
        }

    }


    void CopyToPersistentPath(boolean forceCopy) throws IOException {

        if (!FileTools.Exists(persistentLibraryPath) || (FileTools.Exists(persistentLibraryPath) && forceCopy)) {
            FileTools.Read(libraryName);
            FileTools.Write(persistentLibraryPath, "");
        }

    }

    String XmlDocToString(Document xmlDoc) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(xmlDoc), new StreamResult(writer));
        String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
        return output;
    }

}

