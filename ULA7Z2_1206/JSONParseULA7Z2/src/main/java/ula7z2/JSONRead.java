package ula7z2;

import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.json.simple.*;
import org.json.simple.parser.*;


public class JSONRead {

	public static void main(String[] args) {
		JSONParser parser = new JSONParser();
		JSONObject all;
		try{
			all = (JSONObject)parser.parse(new FileReader("ULA7Z2_1206/JSONParseULA7Z2/src/main/resources/kurzusfelvetelULA7Z2.json", StandardCharsets.UTF_8));
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		printJSON(all, 0);
	}

	// mező: érték kiírás
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
		if (toPrint instanceof String || toPrint instanceof Long){
			System.out.print(toPrint);
		}
	}

	// JSON kiírás (zárójelekkel, indentekkel stb.)
	private static void printJSON(Object toPrint, int indentAmount){
		if (toPrint instanceof JSONObject){
			JSONObject object = (JSONObject)toPrint;
			System.out.println("{");
			indent(indentAmount + 1);
			String[] keys = Arrays.copyOf(object.keySet().toArray(), object.keySet().toArray().length, String[].class);;
			for (int i = 0; i < keys.length; i++){
				System.out.print("\"" + keys[i] + "\": ");
				printJSON(object.get(keys[i]), indentAmount + 1);
				if (i != keys.length - 1){
					System.out.println(",");
					indent(indentAmount + 1);
				}
				else{
					System.out.println();
					indent(indentAmount);
				}
			}
			System.out.print("}");
		}
		if (toPrint instanceof JSONArray){
			JSONArray array = (JSONArray)toPrint;
			System.out.println("[");
			indent(indentAmount + 1);
			for (int i = 0; i < array.size(); i++){
				printJSON(array.get(i), indentAmount + 1);
				if (i != array.size() - 1){
					System.out.println(",");
					indent(indentAmount + 1);
				}
				else{
					System.out.println();
					indent(indentAmount);
				}
			}
			System.out.println("]");	
		}
		if (toPrint instanceof String) System.out.print("\"" + toPrint + "\"");
		if (toPrint instanceof Long) System.out.print(toPrint);
	}

	private static void indent(int indentAmount){
		for (int i = 0; i < indentAmount; i++){
			System.out.print("    ");
		}
	}

}
