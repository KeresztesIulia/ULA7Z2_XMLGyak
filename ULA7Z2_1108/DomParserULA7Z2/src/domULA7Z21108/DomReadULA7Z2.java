package domULA7Z21108;

import org.w3c.dom.*;

public class DomReadULA7Z2 {
    
    public static void domReader(Document document){
        Element rootElement = document.getDocumentElement();

        System.out.println(rootElement.getNodeName());
        Node hallgato = rootElement.getElementsByTagName("hallgato").item(0);     
        //printChildren(hallgato, 0);

        printAttributes((Node)rootElement);

    }

    static void printChildren(Node parent, int indentAmount){
        printNodeInfo(parent, indentAmount); // cuccok, start
        for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.hasChildNodes()){
                printChildren(child, indentAmount+1);
            }
            else {
                if (child.getNodeType() == Node.TEXT_NODE && !child.getNodeValue().equals("#text")){
                    System.out.println(child.getNodeValue());
                }
            }
        }
        // print end
    }

    static void printNodeInfo(Node node, int indentAmount){
        indent(indentAmount);
        System.out.print(node.getNodeName());
        printAttributes(node);
    }

    static void printAttributes(Node node){
        if(node.hasAttributes()){
            
            NamedNodeMap attrs = node.getAttributes();
            for (int i = 0; i < attrs.getLength(); i++){
                Node attribute = attrs.item(i);

            }
        }
    }

    static String nodeType(Node node){
        switch (node.getNodeType()){
            case Node.ATTRIBUTE_NODE:
                return "attribute node";

            case Node.CDATA_SECTION_NODE:
                return "cdata section node";

            case Node.COMMENT_NODE:
                return "comment node";

            case Node.DOCUMENT_FRAGMENT_NODE:
                return "doc fragment node";

            case Node.DOCUMENT_NODE:
                return "doc node";

            case Node.DOCUMENT_TYPE_NODE:
                return "doctype node";
            case Node.ELEMENT_NODE:
                return "element node";

            case Node.ENTITY_NODE:
                return "entity node";

            case Node.ENTITY_REFERENCE_NODE:
                return "entity ref node";

            case Node.NOTATION_NODE:
                return "notation node node";

            case Node.PROCESSING_INSTRUCTION_NODE:
                return "processing instruction node";

            case Node.TEXT_NODE:
                return "text node";

            default:
                return "idk";
        }
    }

    static void indent(int amount){
		for (int i = 0; i < amount; i++){
			System.out.print("    ");
		}
	}

}
