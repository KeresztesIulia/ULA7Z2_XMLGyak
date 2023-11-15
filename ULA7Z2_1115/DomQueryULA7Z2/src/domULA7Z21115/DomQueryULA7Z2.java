package domULA7Z21115;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException; 

import java.io.File;
import java.io.IOException;

import org.w3c.dom.*;


public class DomQueryULA7Z2 {
    public static void main(String[] args) throws Exception {
        String readPath = "ULA7Z2_1115/DomQueryULA7Z2/kurzusfelvetelULA7Z2.xml";
		Document dom = DomReaderULA7Z2.readXML(readPath);
		if (dom == null){
			System.err.println("XML reading error!");
			return;
		}
		
        Element root = dom.getDocumentElement();
        Node kurzusokNode = root.getElementsByTagName("kurzusok").item(0);
        NodeList kurzusok = kurzusokNode.getChildNodes();
        List<String> kurzusnevek = new List();
        for (Node kurzus : Array(kurzusok)){
            kurzusnevek.add(kurzus.getTextContent());
        }

        for (String nev : kurzusnevek){
            System.out.println(nev);
        }

    }
}

