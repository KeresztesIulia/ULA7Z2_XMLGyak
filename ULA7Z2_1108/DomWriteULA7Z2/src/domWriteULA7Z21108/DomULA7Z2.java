package domWriteULA7Z21108;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException; 

import java.io.File;
import java.io.IOException;

import org.w3c.dom.Document;


public class DomULA7Z2 {

	public static void main(String[] args) {
		String readPath = "ULA7Z2_1108/DomWriteULA7Z2/kurzusfelvetelULA7Z2.xml";
		String writePath = "ULA7Z2_1108/DomWriteULA7Z2/kurzusfelvetelULA7Z2_domToXML.xml";
		Document dom = readXML(readPath);
		if (dom == null){
			System.err.println("XML reading error!");
			return;
		}
		DomReaderULA7Z2.domReader(dom);
		DomWriterULA7Z2.domToXML(dom, writePath);
	
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

}
