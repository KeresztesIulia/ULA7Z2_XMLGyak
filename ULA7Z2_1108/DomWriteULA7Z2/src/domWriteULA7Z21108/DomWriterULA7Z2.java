package domWriteULA7Z21108;

import org.w3c.dom.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DomWriterULA7Z2 {

    private static FileWriter xml;

    public static void domToXML(Document document, String path){
        if (!path.endsWith(".xml")){
            System.err.println("Not an XML file!");
            return;
        }
        openFile(path);
        if (xml == null){
            System.err.println("Failed to open file " + path);
            return;
        }
        
        try{
            xml.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

            Element rootElement = document.getDocumentElement();
            writeChildren((Node)rootElement, 0);
            xml.close();
        }
        catch (IOException ioe){
            System.err.println("IO error!");
            ioe.printStackTrace();
            return;
        }
        
        System.out.println("DOM written to XML!");
    }

    private static void openFile(String path){
        try {
            xml = new FileWriter(path, StandardCharsets.UTF_8);            
        } catch (IOException ioe) {
            System.out.println("IO ERROR!");
            ioe.printStackTrace();
        }
    }

    private static void writeChildren(Node parent, int indentAmount) throws IOException{
        boolean textOnly = false;
        writeNodeInfo(parent, indentAmount);
        for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.hasChildNodes()){
                writeChildren(child, indentAmount+1);
            }
            else {
                if (!isRandomNode(child)){
                    xml.write(child.getNodeValue());
                    textOnly = true;
                }
            }
        }
        writeNodeEnd(parent, indentAmount, textOnly);
    }

    private static void writeNodeInfo(Node node, int indentAmount) throws IOException{
        xml.write("\n");
        writeIndent(indentAmount);
        xml.write("<" + node.getNodeName());
        writeAttributes(node);
        xml.write(">");
    }

    private static void writeAttributes(Node node) throws IOException{
        if(node.hasAttributes()){
            NamedNodeMap attrs = node.getAttributes();
            for (int i = 0; i < attrs.getLength(); i++){
                xml.write(" " + attrs.item(i));
            }
        }
    }

    private static void writeNodeEnd(Node node, int indentAmount, boolean textOnly) throws IOException{
        if (!textOnly){
            xml.write("\n");
            writeIndent(indentAmount);
            xml.write("</" + node.getNodeName() + ">\n");
        }
        else{
            xml.write("</" + node.getNodeName() + ">");
        }
    }

    private static boolean isRandomNode(Node node){
        if (node.getNodeType() != Node.TEXT_NODE){
            return true;
        }
        String value = node.getNodeValue();
        if (value.trim().isEmpty() || value == "#text" || value == null){
            return true;
        }
        return false;
    }

    private static void writeIndent(int amount) throws IOException{
		for (int i = 0; i < amount; i++){
			xml.write("    ");
		}
	}

}
