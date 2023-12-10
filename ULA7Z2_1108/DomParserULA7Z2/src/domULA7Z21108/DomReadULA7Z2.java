package domULA7Z21108;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class DomReadULA7Z2 {
    
    public static void main(String[] args) {
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document dom = db.parse(new File("ULA7Z2_1108/DomParserULA7Z2/ULA7Z2_kurzusfelvetel.xml"));
			dom.normalize();
			domReader(dom);
		}
		catch (ParserConfigurationException pce){
			System.out.println("Parser config error!");
			pce.printStackTrace();
			return;
		}
		catch (IOException ioe){
			System.out.println("Parsing error!");
			ioe.printStackTrace();
		}
		catch (SAXException saxe){
			System.out.println("SAX exeption!");
			saxe.printStackTrace();
		}
	}

    public static void domReader(Document document){
        Element rootElement = document.getDocumentElement();
        printChildren((Node)rootElement, 0);

    }

    static void printChildren(Node parent, int indentAmount){
        printNodeInfo(parent, indentAmount);
        for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.hasChildNodes()){
                printChildren(child, indentAmount+1);
            }
            else {
                if (!isRandomNode(child)){
                    indent(indentAmount + 1);
                    System.out.println(child.getNodeValue());
                }
            }
        }
        printNodeEnd(parent, indentAmount);
    }

    static void printNodeInfo(Node node, int indentAmount){
        indent(indentAmount);
        System.out.print(node.getNodeName());
        printAttributes(node);
        System.out.println(" start");
    }

    static void printAttributes(Node node){
        if(node.hasAttributes()){
            System.out.print(" {");
            NamedNodeMap attrs = node.getAttributes();
            for (int i = 0; i < attrs.getLength(); i++){
                Node attribute = attrs.item(i);
                if (i > 0){
                    System.out.print(", " + attribute.getNodeName() + ": " + attribute.getNodeValue());
                    continue;
                }
                System.out.print(attribute.getNodeName() + ": " + attribute.getNodeValue());
            }
            System.out.print("}");
        }
    }

    static void printNodeEnd(Node node, int indentAmount){
        indent(indentAmount);
        System.out.println(node.getNodeName() + " end");
    }

    static boolean isRandomNode(Node node){
        if (node.getNodeType() != Node.TEXT_NODE){
            return true;
        }
        String value = node.getNodeValue();
        if (value.trim().isEmpty() || value.equals("") || value.equals(" ") || value == "#text" || value == null || value.length() == 0){
            return true;
        }
        return false;
    }

    static void indent(int amount){
		for (int i = 0; i < amount; i++){
			System.out.print("    ");
		}
	}

}
