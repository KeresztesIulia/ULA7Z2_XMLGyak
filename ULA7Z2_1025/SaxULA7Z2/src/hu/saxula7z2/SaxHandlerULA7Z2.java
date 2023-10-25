package hu.saxula7z2;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxHandlerULA7Z2 extends DefaultHandler {
	
	int indentAmount;
	
	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException{
		
		if(qName == "ULA7Z2_kurzusfelvetel"){
			System.out.print("ULA7Z2_kurzusfelvétel");
			String tanev = attributes.getValue("tanev");
			String egyetem = attributes.getValue("egyetemNeve");
			String attribs = " {tanév: " + tanev + ", egyetem neve: " + egyetem + "}";
			System.out.print(attribs);
		}
		System.out.print(" start\n");
			
	}
	
	public void characters(char ch[], int start, int length)
			throws SAXException{
			
			if (ch.length != 0){
				System.out.print(new String(ch, start, length) + "\n");
			}
	}
	
	public void endElement(String uri, String localName, String qName)
			throws SAXException{
		
			System.out.print(qName + " end\n");
	}
	
	void indent(int amount){
		for (int i = 0; i < amount; i++){
			System.out.print("    ");
		}
	}
	
	void hallgatoStart(String qName, Attributes attributes){
		if(qName == "hallgato"){
			indent(1);
			System.out.print("hallgató");
			String evfolyam = attributes.getValue("evf");
			String attribs = " {évfolyam: " + evfolyam + "}";
			System.out.print(attribs);
		}
		if(qName == "hnev"){
			indent(2);
			indentAmount = 2;
			System.out.print("név");
		}
		if(qName == "hnev"){
			indent(2);
			indentAmount = 2;
			System.out.print("név");
		}
		if(qName == "hnev"){
			indent(2);
			indentAmount = 2;
			System.out.print("név");
		}
		
	}
	void kurzusStart(String qName, Attributes attributes){
		
	}
	

}