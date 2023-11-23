package domULA7Z21115;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.w3c.dom.*;


public class DomQueryULA7Z2 {
    public static void main(String[] args) throws Exception {
        String readPath = "ULA7Z2_1115/DomQueryULA7Z2/kurzusfelvetelULA7Z2.xml";
        String writePath = "ULA7Z2_1115/DomQueryULA7Z2/kiiratasok/hallgatoAdatok.xml";
		Document dom = readXML(readPath);
		if (dom == null){
			System.err.println("XML reading error!");
			return;
		}

        Element root = dom.getDocumentElement();
		
        // a)
        kurzusokToConsole(root);
        System.out.println();

        // b)
        hallgatoPrintAndWrite(root, writePath);
        
        // c)
        oktatokToConsole(root);

        // d) adott napon lévő kurzusok kiíratása konzolra
        adottNapKurzusaiToConsole(root, "Szerda");
    }

    static Document readXML(String path){
        try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document dom = db.parse(new File(path));
			dom.normalize();
            return dom;
		}
		catch (ParserConfigurationException pce){
			System.out.println("Parser config error!");
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
        return null;
    }

    static void kurzusokToConsole(Element root){
        Element kurzusokNode = (Element)root.getElementsByTagName("kurzusok").item(0);
        NodeList kurzus = kurzusokNode.getElementsByTagName("kurzus");
        int kurzusokLength = kurzus.getLength();
        List<String> kurzusnevek = new ArrayList<String>();

        for (int i = 0; i < kurzusokLength; i++){
            Element kurzusnev = (Element)kurzus.item(i);
            kurzusnevek.add(kurzusnev.getElementsByTagName("kurzusnev").item(0).getTextContent());
        }

        System.out.println("Kurzusnevek:");
        for (String nev : kurzusnevek){
            System.out.println(nev);
        }
    }

    static void hallgatoPrintAndWrite(Element root, String path){
        Node hallgato = root.getFirstChild();
        while (hallgato.getNodeType() == Node.TEXT_NODE){
            hallgato = hallgato.getNextSibling();
        }

        System.out.println("hallgató adatai:");
        DomReaderULA7Z2.printChildren(hallgato, 0);
        DomWriterULA7Z2.NodeToXML(path, hallgato);
    }

    static void oktatokToConsole(Element root){
        Element kurzusokNode = (Element)root.getElementsByTagName("kurzusok").item(0);
        
        NodeList kurzusok = kurzusokNode.getElementsByTagName("kurzus");
        List<List<String>> oktatok = new ArrayList<List<String>>();
        int kurzusokLength = kurzusok.getLength();

        for (int i = 0; i < kurzusokLength; i++){
            Element kurzus = (Element)kurzusok.item(i);
            List<String> kurzusOktatok = new ArrayList<String>();

            String kurzusnev = kurzus.getElementsByTagName("kurzusnev").item(0).getTextContent();
            kurzusOktatok.add(kurzusnev);
            NodeList oktatokNode = kurzus.getElementsByTagName("oktató");

            for (int j = 0; j < oktatokNode.getLength(); j++){
                kurzusOktatok.add(oktatokNode.item(j).getTextContent());
            }
            oktatokNode = kurzus.getElementsByTagName("óraadó");
            for (int j = 0; j < oktatokNode.getLength(); j++){
                kurzusOktatok.add(oktatokNode.item(j).getTextContent());
            }
            oktatok.add(kurzusOktatok);
        }

        System.out.println("Oktatok:");
        for (int i = 0; i < oktatok.size(); i++){
            for (String nev : oktatok.get(i)){
                System.err.println(nev);
            }
            System.err.println();
        }
    }

    static void adottNapKurzusaiToConsole(Element root, String day){
        Element kurzusokNode = (Element)root.getElementsByTagName("kurzusok").item(0);
        
        NodeList kurzusok = kurzusokNode.getElementsByTagName("kurzus");
        int kurzusokLength = kurzusok.getLength();
        List<Node> napiKurzusok = new ArrayList<Node>();

        for (int i = 0; i < kurzusokLength; i++){
            Node kurzus = kurzusok.item(i);

            String idopont = ((Element)kurzus).getElementsByTagName("idopont").item(0).getTextContent();
            if (idopont.contains(day)) napiKurzusok.add(kurzus);
        }

        if (napiKurzusok.size() == 0) System.out.println("Ezen a napon nincs óra!");
        else for (int i = 0; i < napiKurzusok.size(); i++){
                DomReaderULA7Z2.printChildren(napiKurzusok.get(i), 0);
            }
    }

}

