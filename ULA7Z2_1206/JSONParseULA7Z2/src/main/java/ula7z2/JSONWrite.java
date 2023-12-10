package ula7z2;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSONWrite {

	static FileWriter jsonWriter;
	public static void main(String[] args) {
		String writePath = "ULA7Z2_1206/JSONParseULA7Z2/src/main/resources/kurzusfelvetelULA7Z2_1.json";

		// tartalom létrehozása
		// HashMappel a type safety warning miatt, később át van konverálva JSONObjectté
		HashMap<String, JSONObject> allHash = new HashMap<>();
		HashMap<String, JSONObject> rootHash = new HashMap<>();

		// hallgató létrehozása
		JSONObject student = createStudent("Keresztes Iulia", 2002, "programtervezö informatikus");
		rootHash.put("hallgato", student);

		// kurzusok létrehozása
		String[] courseNames = {"Idegennyelv I.", "Mesterséges intelligencia", "Adatkezelés XML környezetben", "Algoritmusok és vizsgálatuk", "Adatkezelés XML környezetben", "Vállalati információs rendszerek fejlesztése", "Vállalati információs rendszerek fejlesztése", "Mesterséges intelligencia", "Versenyrobotok programozása", "Játékprototípusok"};
		int[] courseCredits = {0, 5, 5, 5, 5, 5, 5, 5, 3, 5};
		String[] courseLocations = {"A5/202", "XXXII. elöadó", "XXXII. elöadó", "A1/320", "Inf/101", "Inf/101", "Inf/101", "III. elöadó", "Tréningstúdió", "Inf/15"};
		String[] courseTimes = {"Hétfö, 12-14", "Kedd, 10-12", "Kedd, 12-14", "Kedd, 14-18", "Szerda, 12-14", "Szerda, 14-16", "Szerda, 18-20", "Csütörtök, 10-12", "Csütörtök, 14-16", "Csütörtök, 16-20"};
		String[][] courseLecturers = {{"Pásztor Krisztina"}, {"Kunné Tamás Judit"}, {"Bednarik László"}, {"Házy Attila"}, null, {"Sasvári Péter"}, null, null, null, null};
		String[][] courseTeachers = {null, {"Fazekas Levente"}, null, null, {"Bednarik László"}, null, {"Sasvári Péter"}, {"Fazekas Levente"}, {"Lengyelné Szilágyi Szivia", "Körei Attila"}, {"Kis Áron"}};
		HashMap<String, Object> coursesHash = new HashMap<>();
		if (courseNames.length == 1){
			JSONObject courseObject = createCourse(courseNames[0], courseCredits[0], courseLocations[0], courseTimes[0], courseLecturers[0], courseTeachers[0]);
			coursesHash.put("kurzus", courseObject);
		}
		else{
			JSONArray coursesArray = new JSONArray();
			for (int i = 0; i < courseNames.length; i++){
				coursesArray.add(createCourse(courseNames[i], courseCredits[i], courseLocations[i], courseTimes[i], courseLecturers[i], courseTeachers[i]));
			}
			coursesHash.put("kurzus", coursesArray);
		}
		JSONObject courses = new JSONObject(coursesHash);
		rootHash.put("kurzusok", courses);

		JSONObject root = new JSONObject(rootHash);
		allHash.put("ULA7Z2_kurzusfelvetel", root);

		JSONObject all = new JSONObject(allHash);
		printNormal(all, 0);

		// kiírás file-ba
		try{
			jsonWriter = new FileWriter(writePath, StandardCharsets.UTF_8);
			writeJSON(all, 0);
			jsonWriter.close();
			System.out.println("File írás sikeres!");
		} catch(IOException ioe){
			System.err.println("File írás sikertelen!");
			ioe.printStackTrace();
		}
	}

	private static JSONObject createStudent(String name, int birthyear, String major){
		HashMap<String, Object> student = new HashMap<>();
		student.put("hnev", name);
		student.put("szulev", birthyear);
		student.put("szak", major);
		return new JSONObject(student);
	}

	private static JSONObject createCourse(String name, int credit, String location, String time, String[] lecturers, String[] teachers){
		if (lecturers == null && teachers == null){
			System.err.println("Legalább egy oktatónak vagy óraadónak lennie kell!");
			return null;
		}
		HashMap<String, Object> course = new HashMap<>();
		course.put("kurzusnev", name);
		course.put("kredit", credit);
		course.put("hely", location);
		course.put("idopont", time);

		if (lecturers != null && lecturers.length != 0){
			if (lecturers.length == 1){
				course.put("oktató", lecturers[0]);
			}
			else{
				JSONArray lecturerArray = new JSONArray();
				for (int i = 0; i < lecturers.length; lecturerArray.add(lecturers[i++]));
				course.put("oktató", lecturerArray);
			}
		}
		if (teachers != null && teachers.length != 0){
			if (teachers.length == 1){
				course.put("óraadó", teachers[0]);
			}
			else{
				JSONArray teacherArray = new JSONArray();
				for (int i = 0; i < teachers.length; teacherArray.add(teachers[i++]));
				course.put("óraadó", teacherArray);
			}
		}
		return new JSONObject(course);
	}

	private static void printNormal(Object toPrint, int indentAmount){
		if (toPrint instanceof JSONObject){
			JSONObject object = (JSONObject)toPrint;
			String[] keys = Arrays.copyOf(object.keySet().toArray(), object.keySet().toArray().length, String[].class);;
			System.out.println();
			for (int i = 0; i < keys.length; i++){
				System.out.print(keys[i] + ": ");
				printNormal(object.get(keys[i]), indentAmount);
				System.out.println();
			}
		}
		if (toPrint instanceof JSONArray){
			JSONArray array = (JSONArray)toPrint;
			for (int i = 0; i < array.size(); i++){
				System.out.println();
				indent(indentAmount);
				System.out.print(i+1 + ":");
				printNormal(array.get(i), indentAmount + 1);
			}
		}
		if (toPrint instanceof String || toPrint instanceof Integer){
			System.out.print(toPrint);
		}
	}

	private static void indent(int indentAmount){
		for (int i = 0; i < indentAmount; i++){
			System.out.print("    ");
		}
	}

	private static void writeJSON(Object toPrint, int indentAmount) throws IOException{
		if (toPrint instanceof JSONObject){
			JSONObject object = (JSONObject)toPrint;
			jsonWriter.write("{\n");
			writeIndent(indentAmount + 1);
			String[] keys = Arrays.copyOf(object.keySet().toArray(), object.keySet().toArray().length, String[].class);;
			for (int i = 0; i < keys.length; i++){
				jsonWriter.write("\"" + keys[i] + "\": ");
				writeJSON(object.get(keys[i]), indentAmount + 1);
				if (i != keys.length - 1){
					jsonWriter.write(",\n");
					writeIndent(indentAmount + 1);
				}
				else{
					jsonWriter.write("\n");
					writeIndent(indentAmount);
				}
			}
			jsonWriter.write("}");
		}
		if (toPrint instanceof JSONArray){
			JSONArray array = (JSONArray)toPrint;
			jsonWriter.write("[\n");
			writeIndent(indentAmount + 1);
			for (int i = 0; i < array.size(); i++){
				writeJSON(array.get(i), indentAmount + 1);
				if (i != array.size() - 1){
					jsonWriter.write(",\n");
					writeIndent(indentAmount + 1);
				}
				else{
					jsonWriter.write("\n");
					writeIndent(indentAmount);
				}
			}
			jsonWriter.write("]");	
		}
		if (toPrint instanceof String) jsonWriter.write("\"" + toPrint + "\"");
		if (toPrint instanceof Integer) jsonWriter.write(String.valueOf(toPrint));
	}
	
	private static void writeIndent(int indentAmount) throws IOException{
		for (int i = 0; i < indentAmount; i++){
			jsonWriter.write("    ");
		}
	}

}
