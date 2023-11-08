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
		hallgatoStart(qName, attributes);
		if(qName == "kurzusok"){
			indent(1);
			System.out.print("kurzusok");
		}
		kurzusStart(qName, attributes);
		System.out.print(" start");
	}
	
	public void characters(char ch[], int start, int length)
			throws SAXException{
			
		if (ch.length != 0){
			System.out.println();
			indent(indentAmount+1);
			System.out.println(new String(ch, start, length));
		}
	}
	
	public void endElement(String uri, String localName, String qName)
			throws SAXException{
		if(qName == "ULA7Z2_kurzusfelvetel"){
			System.out.print("ULA7Z2_kurzusfelvétel");
		}
		if(qName == "hallgato"){
			indent(1);
			System.out.print("hallgató");
		}
		if(qName == "hnev"){
			indent(2);
			System.out.print("név");
		}
		if(qName == "szulev"){
			indent(2);
			System.out.print("születési év");
		}
		if(qName == "szak"){
			indent(2);
			System.out.print("szak");
		}
		if(qName == "kurzusok"){
			indent(1);
			System.out.print("kurzusok");
		}
		if(qName == "kurzus"){
			indent(2);
			System.out.print("kurzus");
		}
		if(qName == "kurzusnev"){
			indent(3);
			System.out.print("kurzusnév");
		}
		if(qName == "kredit"){
			indent(3);
			System.out.print("kredit");
		}
		if(qName == "hely"){
			indent(3);
			System.out.print("hely");
		}
		if(qName == "idopont"){
			indent(3);
			System.out.print("idöpont");
		}
		if(qName == "oktató"){
			indent(3);
			System.out.print("oktató");
		}
		if(qName == "óraadó"){
			indent(3);
			System.out.print("óradadó");
		}
		
		System.out.print(" end");
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
		if(qName == "szulev"){
			indent(2);
			indentAmount = 2;
			System.out.print("születési év");
		}
		if(qName == "szak"){
			indent(2);
			indentAmount = 2;
			System.out.print("szak");
		}
		
	}

	void kurzusStart(String qName, Attributes attributes){
		if(qName == "kurzus"){
			indent(2);
			System.out.print("kurzus");
			String id = attributes.getValue("id");
			String jovahagyas = attributes.getValue("jóváhagyás");
			String attribs;
			if (jovahagyas != null){
				attribs = " {id: " + id + ", jóváhagyás: " + jovahagyas +"}";
			}
			else{
				attribs = " {id: " + id  +"}";
			}
			System.out.print(attribs);
		}
		if(qName == "kurzusnev"){
			indent(3);
			indentAmount = 3;
			System.out.print("kurzusnév");
		}
		if(qName == "kredit"){
			indent(3);
			indentAmount = 3;
			System.out.print("kredit");
		}
		if(qName == "hely"){
			indent(3);
			indentAmount = 3;
			System.out.print("hely");
		}
		if(qName == "idopont"){
			indent(3);
			indentAmount = 3;
			System.out.print("idöpont");
		}
		if(qName == "oktató"){
			indent(3);
			indentAmount = 3;
			System.out.print("oktató");
		}
		if(qName == "óraadó"){
			indent(3);
			indentAmount = 3;
			System.out.print("óradadó");
		}
	}
	

}