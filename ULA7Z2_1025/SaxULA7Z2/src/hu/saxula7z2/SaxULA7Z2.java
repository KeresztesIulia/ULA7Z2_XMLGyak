package hu.saxula7z2;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SaxULA7Z2 {
	   public static void main(String[] args){

	      try {	
	         File xml = new File("ULA7Z2_1025\\SaxULA7Z2\\ULA7Z2_kurzusfelvetel.xml");
	         SAXParserFactory spFactory = SAXParserFactory.newInstance();
	         SAXParser parser = spFactory.newSAXParser();
	         SaxHandlerULA7Z2 userhandler = new SaxHandlerULA7Z2();
	         parser.parse(xml, userhandler);     
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	   }   
	}