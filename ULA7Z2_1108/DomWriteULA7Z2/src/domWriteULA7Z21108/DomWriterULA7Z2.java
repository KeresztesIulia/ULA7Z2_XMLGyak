package domWriteULA7Z21108;

import org.w3c.dom.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class DomWriterULA7Z2 {

    private static FileWriter xml;
    private static Document dom;

    private static List<String> IDs = new ArrayList<>();

    public static void main(String[] args) {
		String writePath = "ULA7Z2_1108/DomWriteULA7Z2/kurzusfelvetelULA7Z2_domToXML.xml";

		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.newDocument();
		}
		catch (ParserConfigurationException pce){
			System.err.println("Parser config error!");
			pce.printStackTrace();
			return;
		}

        // gyökércsomópont létrehozása
        String neptunID = "ULA7Z2";
        String academicYear = "2023/2024 I.";
        String university = "ME";
        Element root = dom.createElement(neptunID + "_kurzusfelvetel");
        root.setAttribute("tanev", academicYear);
        root.setAttribute("egyetem", university);

        // hallgató csomópont létrehozása
        Node studentNode = createStudentNode(neptunID, "Keresztes Iulia", 2002, "programtervezö informatikus", 3);
        root.appendChild(studentNode);

        // kurzus csomópontok létrehozása
        Node coursesNode = dom.createElement("kurzusok");
        String[] courseIDs = {"MIGEBANNY1_02", "GEIAK130-B_00", "GEIAL332-B_00", "GEMAK234-B", "GEIAL332-B_02", "GEIAL315-B_00", "GEIAL315-B_01", "GEIAK130-B_01", "GEMAKVRP2-B2_02", "GEIAK310-B"};
        boolean[][] courseApprovals = {{true, true}, {true, false}, {false, false}, {true, false}, {true, true}, {false, false}, {true, true}, {false, false}, {false, false}, {false, false}};
        String[] courseNames = {"Idegennyelv I.", "Mesterséges intelligencia", "Adatkezelés XML környezetben", "Algoritmusok és vizsgálatuk", "Adatkezelés XML környezetben", "Vállalati információs rendszerek fejlesztése", "Vállalati információs rendszerek fejlesztése", "Mesterséges intelligencia", "Versenyrobotok programozása", "Játékprototípusok"};
		int[] courseCredits = {0, 5, 5, 5, 5, 5, 5, 5, 3, 5};
		String[] courseLocations = {"A5/202", "XXXII. elöadó", "XXXII. elöadó", "A1/320", "Inf/101", "Inf/101", "Inf/101", "III. elöadó", "Tréningstúdió", "Inf/15"};
        String[] courseDays = {"Hétfő", "Kedd", "Kedd", "Kedd", "Szerda", "Szerda", "Szerda", "Csütörtök", "Csütörtök", "Csütörtök"};
        int[][] courseTimes = {{12, 14}, {10, 12}, {12, 14}, {14, 18}, {12, 14}, {14, 16}, {18, 20}, {10, 12}, {14, 16}, {16, 20}};
		String[][] courseLecturers = {{"Pásztor Krisztina"}, {"Kunné Tamás Judit"}, {"Bednarik László"}, {"Házy Attila"}, null, {"Sasvári Péter"}, null, null, null, null};
		String[][] courseTeachers = {null, {"Fazekas Levente"}, null, null, {"Bednarik László"}, null, {"Sasvári Péter"}, {"Fazekas Levente"}, {"Lengyelné Szilágyi Szivia", "Körei Attila"}, {"Kis Áron"}};
        for (int i = 0; i < courseNames.length; i++){
            coursesNode.appendChild(createCourseNode(courseIDs[i], courseApprovals[i][0], courseApprovals[i][1], courseNames[i], courseCredits[i], courseLocations[i], courseDays[i], courseTimes[i][0], courseTimes[i][1], courseLecturers[i], courseTeachers[i]));
        }

        root.appendChild(coursesNode);
        dom.appendChild(root);

		DomReaderULA7Z2.domReader(dom);
		domToXML(dom, writePath);
	
	}

    // különböző típusú csomópontok létrehozása adattípus ellenőrzésének spórlása érdekében
    private static Node createTextNode(String nodeName, String nodeContent){
        if (nodeContent == null || nodeContent.isEmpty()){
            System.err.println("(" + nodeName + ") A csomópontok tartalma nem lehet üres!");
            return null;
        }
        Node node = dom.createElement(nodeName);
        node.setTextContent(nodeContent);
        return node;
    }

    private static Node createIntNode(String nodeName, int nodeContent){
        return createTextNode(nodeName, String.valueOf(nodeContent));
    }

    private static Node createTimeNode(String dayOfWeek, int startTime, int endTime){
        // ellenőrzések
        switch (dayOfWeek.toLowerCase()) {
            case "hétfő":
            case "kedd":
            case "szerda":
            case "csütörtök":
            case "péntek":
            case "szombat":
            case "vasárnap":
                break;
            default:
                System.err.println("A hét valamely napját kell megadni!");
                return null;
        }
        if (startTime >= endTime){
            System.err.println("A kezdési idő nem lehet későbbi vagy egyenlő a befejezési időnél!");
            return null;
        }
        if (startTime < 0){
            System.err.println("Az időpont nem lehet negatív!");
            return null;
        }

        return createTextNode("idopont", dayOfWeek + ", " + startTime + "-" + endTime);
    }

    // tényleges csomópontok létrehozásának függvényei
    private static Node createStudentNode(String ID, String name, int birthyear, String department, int year){
        // ellenőrzések
        if (ID == null || name == null || department == null){
            System.err.println("A hallgatónál minden adat kötelező megadású!");
            return null;
        }
        if (!(birthyear > 1900 && birthyear < LocalDate.now().getYear())){
            System.err.println("A megadott születési év nem valid!");
            return null;
        }
        if (year < 0){
            System.err.println("Az évfolyam nem lehet negatív!");
            return null;
        }
        if (IDs.contains(ID)){
            System.err.println("(" + ID + ") Már van ilyen azonosítójú elem!");
            return null;
        }

        // csomópont létrehozása
        Element studentNode = dom.createElement("hallgato");
        studentNode.setAttribute("id", ID);

        Node studentNameNode = createTextNode("hnev", name);
        Node birthyearNode = createIntNode("szulev", birthyear);
        Element departmentNode = (Element)createTextNode("szak", department);
        departmentNode.setAttribute("evf", String.valueOf(year));

        try{
            studentNode.appendChild(studentNameNode);
            studentNode.appendChild(birthyearNode);
            studentNode.appendChild(departmentNode);
        } catch(NullPointerException npe){
            return null;
        }

        IDs.add(ID);
        return studentNode;
    }

    // kurzus csomópont létrehozása
    // nem kötelező megadásúak: lecturers, teachers (de egyszerre nem lehet mindkettő null)
    private static Node createCourseNode(String ID, boolean hasApproval, boolean approved, String name, int credit, String location, String dayOfWeek, int startTime, int endTime, String[] lecturers, String[] teachers){
        // ellenőrzések
        if (ID == null || name == null || location == null || dayOfWeek == null || (lecturers == null && teachers == null))
        {
            System.err.println("(" + ID + ") Csak az oktatók és óraadók tömbje nem kötelező megadású, de legalább az egyiket meg kell adni!");
            return null;
        }
        if (IDs.contains(ID)){
            System.err.println("(" + ID + ") Már létezik ilyen azonosítójú elem!");
            return null;
        }
        if (credit < 0){
            System.err.println("(" + ID + ") A kreditérték nem lehet negatív!");
            return null;
        }

        // csomópont létrehozása
        Element courseNode = dom.createElement("kurzus");
        courseNode.setAttribute("id", ID);
        if (hasApproval)
            if (approved) courseNode.setAttribute("jóváhagyás", "igen"); 
            else courseNode.setAttribute("jóváhagyás", "nem");
        Node nameNode = createTextNode("kurzusnev", name),
            creditNode = createIntNode("kredit", credit),
            locationNode = createTextNode("hely", location),
            timeNode = createTimeNode(dayOfWeek, startTime, endTime);
        try{
            courseNode.appendChild(nameNode);
            courseNode.appendChild(creditNode);
            courseNode.appendChild(locationNode);
            courseNode.appendChild(timeNode);
        } catch (NullPointerException npe){
            System.out.println(ID);
            return null;
        }

        if (lecturers != null){
            for (int i = 0; i < lecturers.length; i++){
                try{
                    courseNode.appendChild(createTextNode("oktató", lecturers[i]));
                }
                catch (NullPointerException npe){
                    continue;
                }
            }
        }
        if (teachers != null){
            for (int i = 0; i < teachers.length; i++){
                try{
                    courseNode.appendChild(createTextNode("óraadó", teachers[i]));
                }
                catch (NullPointerException npe){
                    continue;
                }
            }
        }

        IDs.add(ID);
        return courseNode;
    }

//#region file-ba írás
    public static void domToXML(Document dom, String path){
        if (!path.endsWith(".xml")){
            System.err.println("Not an XML file!");
            return;
        }
        openFile(path);
        if (xml == null){
            System.err.println("Failed to open file " + path);
            return;
        }
        
        try{
            xml.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

            Element rootElement = dom.getDocumentElement();
            writeChildren((Node)rootElement, 0);
            xml.close();
        }
        catch (IOException ioe){
            System.err.println("IO error!");
            ioe.printStackTrace();
            return;
        }
        
        System.out.println("DOM written to XML!");
    }

    private static void openFile(String path){
        try {
            xml = new FileWriter(path, StandardCharsets.UTF_8);            
        } catch (IOException ioe) {
            System.out.println("IO ERROR!");
            ioe.printStackTrace();
        }
    }

    private static void writeChildren(Node parent, int indentAmount) throws IOException{
        boolean textOnly = false;
        writeNodeInfo(parent, indentAmount);
        for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.hasChildNodes()){
                writeChildren(child, indentAmount+1);
            }
            else {
                if (!isRandomNode(child)){
                    xml.write(child.getNodeValue());
                    textOnly = true;
                }
            }
        }
        writeNodeEnd(parent, indentAmount, textOnly);
    }

    private static void writeNodeInfo(Node node, int indentAmount) throws IOException{
        xml.write("\n");
        writeIndent(indentAmount);
        xml.write("<" + node.getNodeName());
        writeAttributes(node);
        xml.write(">");
    }

    private static void writeAttributes(Node node) throws IOException{
        if(node.hasAttributes()){
            NamedNodeMap attrs = node.getAttributes();
            for (int i = 0; i < attrs.getLength(); i++){
                xml.write(" " + attrs.item(i));
            }
        }
    }

    private static void writeNodeEnd(Node node, int indentAmount, boolean textOnly) throws IOException{
        if (!textOnly){
            xml.write("\n");
            writeIndent(indentAmount);
            xml.write("</" + node.getNodeName() + ">\n");
        }
        else{
            xml.write("</" + node.getNodeName() + ">");
        }
    }

    private static boolean isRandomNode(Node node){
        if (node.getNodeType() != Node.TEXT_NODE){
            return true;
        }
        String value = node.getNodeValue();
        if (value.trim().isEmpty() || value == "#text" || value == null){
            return true;
        }
        return false;
    }

    private static void writeIndent(int amount) throws IOException{
		for (int i = 0; i < amount; i++){
			xml.write("    ");
		}
	}
//#endregion

}
