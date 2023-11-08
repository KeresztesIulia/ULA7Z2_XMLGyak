package domULA7Z21108;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException; 

import java.io.File;
import java.io.IOException;

import org.w3c.dom.Document;


public class DomULA7Z2 {

	public static void main(String[] args) {
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document dom = db.parse(new File("ULA7Z2_1108/DomParserULA7Z2/ULA7Z2_kurzusfelvetel.xml"));
			dom.normalize();
			DomReadULA7Z2.domReader(dom);
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

}
