package xpathULA7Z21122;

import org.w3c.dom.*;

public class DomReaderULA7Z2 {
    public static void domReader(Document document){
        Element rootElement = document.getDocumentElement();
        printChildren((Node)rootElement, 0);

    }

    public static void printChildren(Node parent, int indentAmount){
        boolean textOnly = false;
        printNodeInfo(parent, indentAmount);
        for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.hasChildNodes()){
                printChildren(child, indentAmount+1);
            }
            else {
                if (!isRandomNode(child)){
                    System.out.print(child.getNodeValue());
                    textOnly = true;
                }
            }
        }
        printNodeEnd(parent, indentAmount, textOnly);
    }

    private static void printNodeInfo(Node node, int indentAmount){
        System.out.print("\n");
        indent(indentAmount);
        System.out.print("<" + node.getNodeName());
        printAttributes(node);
        System.out.print(">");
    }

    private static void printAttributes(Node node){
        if(node.hasAttributes()){
            NamedNodeMap attrs = node.getAttributes();
            for (int i = 0; i < attrs.getLength(); i++){
                System.out.print(" " + attrs.item(i));
            }
        }
    }

    private static void printNodeEnd(Node node, int indentAmount, boolean textOnly){
        if (!textOnly){
            System.out.println();
            indent(indentAmount);
            System.out.print("</" + node.getNodeName() + ">\n");
        }
        else{
            System.out.print("</" + node.getNodeName() + ">");
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

    private static void indent(int amount){
		for (int i = 0; i < amount; i++){
			System.out.print("    ");
		}
	}
}

