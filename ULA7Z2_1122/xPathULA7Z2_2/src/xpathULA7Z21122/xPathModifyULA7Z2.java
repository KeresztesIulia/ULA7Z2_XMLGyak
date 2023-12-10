package xpathULA7Z21122;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import org.w3c.dom.*;

public class xPathModifyULA7Z2 {

	public static void main(String[] args) {
		String readPath = "ULA7Z2_1122/xPathULA7Z2_2/files/ULA7Z2_kurzusfelvetel.xml";
		String writePath = "ULA7Z2_1122/xPathULA7Z2_2/files/kurzusfelvetelULA7Z2_1.xml";
		Document dom = readXML(readPath);
		XPath xPath = XPathFactory.newInstance().newXPath();

		// expression-ok
		String expression1 = "ULA7Z2_kurzusfelvetel/kurzusok/kurzus[descendant::kredit=5]";
		String expression2 = "ULA7Z2_kurzusfelvetel/kurzusok/kurzus/kurzusnev[following-sibling::hely[contains(text(), 'Inf')]]";
		String expression3 = "ULA7Z2_kurzusfelvetel/kurzusok/kurzus/kurzusnev[ancestor::kurzus[contains(@id, 'GEIAL')]]";
		String expression4 = "ULA7Z2_kurzusfelvetel/kurzusok/kurzus[not(@jóváhagyás) or @jóváhagyás='nem']";


		// NodeList-ek
		NodeList fiveCreditClasses = null;
		NodeList infoClasses = null;
		NodeList GEIALclasses = null;
		NodeList notApprovedClasses = null;


		// kiértékelések
		try{
			fiveCreditClasses = evaluateXPath(dom, xPath, expression1);
			infoClasses = evaluateXPath(dom, xPath, expression2);
			GEIALclasses = evaluateXPath(dom, xPath, expression3);
		}
		catch (XPathExpressionException xpathE){
			System.err.println("Nem megfelelö XPath kifejezés!\n");
		}

		// módosítások
		if (fiveCreditClasses != null) {
			approveClasses(fiveCreditClasses);
		}
		if (infoClasses != null){
			addInfoClassText(infoClasses);
		}
		if(GEIALclasses != null){
			addGEIAL(GEIALclasses);
		}


		// expression4 kiértékelése és módosítás
		try{
			notApprovedClasses = evaluateXPath(dom, xPath, expression4);
		}
		catch (XPathExpressionException xpathE){
			System.err.println("Nem megfelelö XPath kifejezés!\n");
		}
		if (notApprovedClasses != null){
			deleteNotApproved(notApprovedClasses);
		}

		// módosított kiírás
		System.out.println("Módosítások:");
		System.out.println("- 5 kredites kurzusok jóváhagyása");
		System.out.println("- GEIAL kódú tanárgyak nevéhez hozzáfűzni a kódot");
		System.out.println("- infóépületben tartott órák nevéhez a 'gyakorlat' szó hozzáírása");
		System.out.println("- csak a jóváhagyott kurzusok megtartása (módosítás utáni állapotban ellenőrizve)");
		DomReaderULA7Z2.domReader(dom);
		DomWriteULA7Z2.domToXML(dom, writePath);
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

	static void printNodeList(NodeList nodeList){
		if (nodeList != null){
			for (int i = 0; i < nodeList.getLength(); i++){
				DomReaderULA7Z2.printChildren(nodeList.item(i), 0);
			}
		}
	}

	static NodeList evaluateXPath(Document dom, XPath xPath, String expression) throws XPathExpressionException{
		return (NodeList) xPath.compile(expression).evaluate(dom, XPathConstants.NODESET);
	}

	static void approveClasses(NodeList classes){
		for (int i = 0; i < classes.getLength(); i++){
			((Element)classes.item(i)).setAttribute("jóváhagyás", "igen");
		}
	}

	static void addGEIAL(NodeList classes){
		for (int i = 0; i < classes.getLength(); i++){
			Node classs = classes.item(i);
			classs.setTextContent(classs.getTextContent() + " (" + ((Element)classs.getParentNode()).getAttribute("id") + ")");
		}
	}

	static void addInfoClassText(NodeList infoClasses){
		for (int i = 0; i < infoClasses.getLength(); i++){
			Node infoClass = infoClasses.item(i);
			infoClass.setTextContent(infoClass.getTextContent() + " gyakorlat");
		}
	}

	static void deleteNotApproved(NodeList notApprovedClasses){
		for (int i = 0; i < notApprovedClasses.getLength(); i++){
			Node notApprovedClass = notApprovedClasses.item(i);
			notApprovedClass.getParentNode().removeChild(notApprovedClass);
		}
	}
}
