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

        // új kurzus hozzáadása
        addKurzus(dom, domAllNodes, "NEW_1", null, "új kurzus", 2, "XXX. előadó", "Szerda, 10-12", new String[]{"óktató1", "oktató2"}, new String[]{"óraadó1"});
        
        System.out.println("\nA módosítások után:");
        DomReaderULA7Z2.domReader(dom);
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
        DomWriterULA7Z2.domToXML(dom, path);
    }

    static void changeJóváhagyás(Map<String, List<Node>> nodeDictionary, Document dom){
        for (Node kurzus : nodeDictionary.get("kurzus")){
            String jóváhagyás = ((Element)kurzus).getAttribute("jóváhagyás");
            if (jóváhagyás.equals("nem")){
                ((Element)kurzus).setAttribute("jóváhagyás", "igen");
            }
        }
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
    }

    static void addKurzus(Document dom, Map<String, List<Node>> nodeDictionary, String idAttribute, String jóváhagyásAttribute, String kurzusnev, int kredit, String hely, String idopont, String[] oktatók, String[] óraadók){
        // kurzus szülő létrehozása
        Node kurzusNode = dom.createElement("kurzus");
        ((Element)kurzusNode).setAttribute("id", idAttribute);
        if (jóváhagyásAttribute == null){
            jóváhagyásAttribute = "";
        }
        if (jóváhagyásAttribute.equals("nem") || jóváhagyásAttribute.equals("igen") || jóváhagyásAttribute.isEmpty()){
            if (!jóváhagyásAttribute.isEmpty())
            {
                ((Element)kurzusNode).setAttribute("jóváhagyás", jóváhagyásAttribute);
            }
        }
        else{
            System.err.println("A jóváhagyás csak 'igen' vagy 'nem' lehet, vagy üres");
        }
        // ha még nincs kurzusok csomópont, azt is létrehozza
        if (nodeDictionary.get("kurzusok") == null){
            nodeDictionary.put("kurzusok", new ArrayList<>());
            Node kurzusokNode = dom.createElement("kurzusok");
            dom.getDocumentElement().appendChild(kurzusokNode);
            nodeDictionary.get("kurzusok").add(kurzusokNode);
        }
        nodeDictionary.get("kurzusok").get(0).appendChild(kurzusNode);
        
        // kurzusnév csomópont létrehozása
        Node kurzusnevNode = dom.createElement("kurzusnev");
        kurzusnevNode.setTextContent(kurzusnev);
        kurzusNode.appendChild(kurzusnevNode);
        if (nodeDictionary.get("kurzusnev") == null)
            nodeDictionary.put("kurzusnev", new ArrayList<>());
        nodeDictionary.get("kurzusnev").add(kurzusnevNode);

        // kredit csomópont létrehozása
        Node kreditNode = dom.createElement("kredit");
        String kreditString = String.valueOf(kredit);
        kreditNode.setTextContent(kreditString);
        kurzusNode.appendChild(kreditNode);
        if (nodeDictionary.get("kredit") == null)
            nodeDictionary.put("kredit", new ArrayList<>());
        nodeDictionary.get("kredit").add(kreditNode);

        // hely csomópont hozzáadása
        Node helyNode = dom.createElement("hely");
        helyNode.setTextContent(hely);
        kurzusNode.appendChild(helyNode);
        if (nodeDictionary.get("hely") == null)
            nodeDictionary.put("hely", new ArrayList<>());
        nodeDictionary.get("hely").add(helyNode);

        // időpont csomópont hozzáadása
        Node idopontNode = dom.createElement("idopont");
        idopontNode.setTextContent(idopont);
        kurzusNode.appendChild(idopontNode);
        if (nodeDictionary.get("idopont") == null)
            nodeDictionary.put("idopont", new ArrayList<>());
        nodeDictionary.get("idopont").add(idopontNode);

        // oktató csomópontok hozzáadása
        for (String oktató : oktatók){
            Node oktatóNode = dom.createElement("oktató");
            oktatóNode.setTextContent(oktató);
            kurzusNode.appendChild(oktatóNode);
            if (nodeDictionary.get("oktató") == null)
                nodeDictionary.put("oktató", new ArrayList<>());
            nodeDictionary.get("oktató").add(oktatóNode);
        }

        // óraadó csomópontok hozzáadása
        for (String óraadó : óraadók){
            Node óraadóNode = dom.createElement("óraadó");
            óraadóNode.setTextContent(óraadó);
            kurzusNode.appendChild(óraadóNode);
            if (nodeDictionary.get("óraadó") == null)
                nodeDictionary.put("óraadó", new ArrayList<>());
            nodeDictionary.get("óraadó").add(óraadóNode);
        }

        System.out.println("Az új csomópont:");
        DomReaderULA7Z2.printChildren(kurzusNode, 0);

    }


}
