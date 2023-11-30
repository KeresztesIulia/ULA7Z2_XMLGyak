package xpathULA7Z2;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;

import org.xml.sax.SAXException;


import java.io.File;
import java.io.IOException;

import org.w3c.dom.*;

public class xPathULA7Z2 {

	public static void main(String[] args) {
		String readPath = "ULA7Z2_1122/xPathULA7Z2/files/studentULA7Z2.xml";
		Document dom = readXML(readPath);
		XPath xPath = XPathFactory.newInstance().newXPath();

		// expression-ok
		String expression1 = "class/student";
		String expression2 = "//student[@id='02']";
		String expression3 = "//student";
		String expression4 = "/class/student[2]";
		String expression5 = "/class/student[last()]";
		String expression6 = "/class/student[last() - 1]";
		String expression7 = "/class/student[position() < 3]";
		String expression8 = "/class/*";
		String expression9 = "//student[@*]";
		String expression10 = "//*";
		String expression11 = "/class/student[kor>20]";
		String expression12 = "//student/keresztnev | //student/vezeteknev";

		// NodeList-ek
		NodeList classStudents = null;
		Node studentID02 = null;
		NodeList allStudents = null;
		Node secondClassStudent = null;
		Node lastClassStudent = null;
		Node penultimateClassStudent = null;
		NodeList firstTwoClassStudents = null;
		NodeList allClassChildren = null;
		NodeList studentsWithAttributes = null;
		NodeList allElements = null;
		NodeList classStudentsOlderThan20yo = null;
		NodeList studentSurnamesAndNames = null;
		
		

		// expression-ok kiértékelése
		try{
			classStudents = evaluateXPath(dom, xPath, expression1);
			studentID02 = evaluateXPath(dom, xPath, expression2).item(0);
			allStudents = evaluateXPath(dom, xPath, expression3);
			secondClassStudent = evaluateXPath(dom, xPath, expression4).item(0);
			lastClassStudent = evaluateXPath(dom, xPath, expression5).item(0);
			penultimateClassStudent = evaluateXPath(dom, xPath, expression6).item(0);
			firstTwoClassStudents = evaluateXPath(dom, xPath, expression7);
			allClassChildren = evaluateXPath(dom, xPath, expression8);
			studentsWithAttributes = evaluateXPath(dom, xPath, expression9);
			allElements = evaluateXPath(dom, xPath, expression10);
			classStudentsOlderThan20yo = evaluateXPath(dom, xPath, expression11);
			studentSurnamesAndNames = evaluateXPath(dom, xPath, expression12);
		}
		catch (XPathExpressionException xpathE){
			System.err.println("Nem megfelelő XPath kifejezés!");
		}
		
		// kiíratások
		System.out.println("1) Válassza ki az összes student elemet, amely a class gyermeke!");
		printNodeList(classStudents);

		System.out.println("\n\n2) Válassza ki azt a student elemet, amely rendelkezik 'id' attribútummal, és az értéke '02'!");
		if (studentID02 != null){
			DomReaderULA7Z2.printChildren(studentID02, 0);
		}

		System.out.println("\n\n3) Kiválasztja az összes student elemet, függetlenül attól, hogy hol vannak a dokumentumban!");
		printNodeList(allStudents);

		System.out.println("\n\n4) Válassza ki a második student elemet, amely a class root element gyermeke!");
		if (secondClassStudent != null){
			DomReaderULA7Z2.printChildren(secondClassStudent, 0);
		}

		System.out.println("\n\n5) Válassza ki az utolsó student elemet, amely a class root element gyermeke!");
		if (lastClassStudent != null){
			DomReaderULA7Z2.printChildren(lastClassStudent, 0);
		}

		System.out.println("\n\n6) Válassza ki az utolsó elötti student elemet, amely a class root element gyermeke!");
		if (penultimateClassStudent != null){
			DomReaderULA7Z2.printChildren(penultimateClassStudent, 0);
		}

		System.out.println("\n\n7) Válassza ki az első két elötti student elemet, amelyek a class root element gyermeke!");
		printNodeList(firstTwoClassStudents);

		System.out.println("\n\n8) Válassza ki a class root element összes gyermek elemét!");
		printNodeList(allClassChildren);

		System.out.println("\n\n9) Válassza ki az összes student elemet, amely rendelkezik legalább egy bármilyen attribútummal!");
		printNodeList(studentsWithAttributes);

		System.out.println("\n\n10) Válassza ki a dokumentum összes elemét!");
		printNodeList(allElements);

		System.out.println("\n\n11) Válassza ki a class root element összes student elemét, amelynél a kor>20!");
		printNodeList(classStudentsOlderThan20yo);

		System.out.println("\n\n12) Válassza ki az összes student elem összes keresztnév és vezetéknév csomópontját!");
		printNodeList(studentSurnamesAndNames);
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

}
