package domULA7Z21115_2;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.w3c.dom.*;

public class DomModifyULA7Z2 {
    public static void main(String[] args) {
        String readPath = "ULA7Z2_1115/DomModifyULA7Z2/ULA7Z2_kurzusfelvetel.xml";
        String writePath = "ULA7Z2_1115/DomModifyULA7Z2/kurzusfelvetelModifyULA7Z2.xml";
        Document dom = readXML(readPath);
        if (dom == null){
            System.err.println("XML parse error!");
        }
        Element root = dom.getDocumentElement();
        Map<String, List<Node>> domAllNodes = new Hashtable<>();
        buildDomTree(domAllNodes, root);

        // a)
        addÓraadóAndSave(domAllNodes, dom, "plusz óraadó", writePath);

        // b) - nem volt nyelv megadva, úgyhogy a jóváhagyást módosítottam nem-ről igen-re
        changeJóváhagyás(domAllNodes, dom);

        // c) Napok nevének lerövidítése az időpont elemeknél (pl. Hétfő -> H)
        napRövidítések(domAllNodes, dom);
    }

    static Document readXML(String path){
        Document dom = null;
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(new File(path));
			dom.normalize();
		}
		catch (ParserConfigurationException pce){
			System.err.println("Parser config error!");
			pce.printStackTrace();	
		}
		catch (IOException ioe){
			System.out.println("Parsing error!");
			ioe.printStackTrace();
		}
		catch (SAXException saxe){
			System.out.println("SAX exeption!");
			saxe.printStackTrace();
		}
        return dom;
    }

    static void buildDomTree(Map<String, List<Node>> nodeDictionary, Node startNode){
        if (startNode.hasChildNodes()){
            for (Node child = startNode.getFirstChild(); child != null; child = child.getNextSibling()){
                String nodeName = child.getNodeName();
                if (child.hasChildNodes()){
                    //checkedNodeNames.add(nodeName);
                    if (nodeDictionary.get(nodeName) == null){
                        nodeDictionary.put(nodeName, new ArrayList<>());
                    }
                    nodeDictionary.get(nodeName).add(child);
                }
                buildDomTree(nodeDictionary, child);
            }
        } 

    }

    static void writeNodesByName(Map<String, List<Node>> nodeDictionary, String nodeName){
        for (Node node : nodeDictionary.get(nodeName)){
                DomReaderULA7Z2.printChildren(node, 0);
        }
    }

    static void addÓraadóAndSave(Map<String, List<Node>> nodeDictionary, Document dom, String óraadóName, String path){
        for (Node kurzus : nodeDictionary.get("kurzus")){
            if (((Element)kurzus).getElementsByTagName("óraadó").getLength() == 0){
                Node újÓraadó = dom.createElement("óraadó");
                újÓraadó.setTextContent(óraadóName);
                kurzus.appendChild(újÓraadó);
                if (nodeDictionary.get("óraadó") == null){
                    nodeDictionary.put("óraadó", new ArrayList<>());
                }
                nodeDictionary.get("óraadó").add(újÓraadó);
            }
        }

        DomReaderULA7Z2.domReader(dom);
        DomWriterULA7Z2.domToXML(dom, path);
    }

    static void changeJóváhagyás(Map<String, List<Node>> nodeDictionary, Document dom){
        for (Node kurzus : nodeDictionary.get("kurzus")){
            String jóváhagyás = ((Element)kurzus).getAttribute("jóváhagyás");
            if (jóváhagyás.equals("nem")){
                ((Element)kurzus).setAttribute("jóváhagyás", "igen");
            }
        }

        DomReaderULA7Z2.domReader(dom);
    }

    static void napRövidítések(Map<String, List<Node>> nodeDictionary, Document dom){
        for (Node időpont : nodeDictionary.get("idopont")){
            String időpontString = időpont.getTextContent();
            időpontString = időpontString.replace("Hétfö", "H");
            időpontString = időpontString.replace("Kedd", "K");
            időpontString = időpontString.replace("Szerda", "Sze");
            időpontString = időpontString.replace("Csütörtök", "Cs");
            időpontString = időpontString.replace("Péntek", "p");
            időpontString = időpontString.replace("Szombat", "Sz");
            időpontString = időpontString.replace("Vasárnap", "V");

            időpont.setTextContent(időpontString);
        }

        DomReaderULA7Z2.domReader(dom);
    }

}
