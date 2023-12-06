package hu.domparse.ULA7Z2;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Hashtable;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;

public class DomWriteULA7Z2 {
    // néhány gyakran szükséges változó
    private static FileWriter xml;
    private static Document dom;

    // ID-Csomópont párosok mentése gyors IDREF-ellenőrzés érdekében
    private static Map<String, Node> studios = new Hashtable<>();
    private static Map<String, Node> websiteGroups = new Hashtable<>(); // itt a W_stúdió szerint tároljuk
    private static Map<String, Node> games = new Hashtable<>();
    private static Map<String, Node> achievements = new Hashtable<>();
    private static Map<String, Node> languages = new Hashtable<>();
    private static Map<String, Node> users = new Hashtable<>();
	private static List<String> friends = new ArrayList<>(); // a két felhasználó FID-jét fűzzük össze egy stringként, mindkét sorrendben

    // minden ID mentése gyors ismétlődésellenőrzés érdekében
    private static List<String> IDs = new ArrayList<>();

    // műfajok és állapotok nevei
    private static String[] genres = {"fantasy", "horror", "akció", "kaland", "rejtély", "romantikus", "RPG", "shooter", "multiplayer", "singleplayer", "FPS", "belső-nézetes", "külső-nézetes", "indie"};


    public static void main(String[] args) {
        String writePath = "XMLTaskULA7Z2/DOMParseULA7Z2/src/files/XMLULA7Z2_2d.xml";   

		// dom létrehozása
        try{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.newDocument();
        } catch (Exception e){
            e.printStackTrace();
            return;
        }

		Element root = (Element)dom.appendChild(dom.createElement("XMLTaskULA7Z2"));
        boolean error = false;

		// az egyes csomópontok létrehozásához szükséges adatok
		// (mivel az XML-be rengeteg példányt létrehoztam, itt nem fogom az egészet újra beírni, csak egy részét)
		//		- stúdiók
		String[] SIDs = {"BT34", "BethGS", "BethSW", "RG"};
		String[] studioNames = {"34BigThings srl", "Bethesda Game Studios", "Bethesda Softworks", "Raiser Games"};
		String[] introTexts = {null, "Bethesda Game Studios bemutatkozó szövege...", "Bethesda Softworks bemutató szöveg valami", null};
		int[] studioFoundationYears = {2013, 2001, 1986, 2016};

		Node studiosNode = dom.createElement("stúdiók");
		root.appendChild(studiosNode);
		for (int i = 0; i < SIDs.length; i++){
			System.out.println(i + ". stúdió");
			Node studio = CreateStudioNode(SIDs[i], studioNames[i], introTexts[i], studioFoundationYears[i]);
			if (studio == null){
				error = true;
				continue;
			}
			studiosNode.appendChild(studio);
		}

		//		- weboldalak
		String[] wSIDs = SIDs;
		String[] mainpages = {"34bigthings.com", "bethesdagamestudios.com", "bethesda.net", "www.raisergames.com"};
		String[] youtubePages = {"34BigThings_games", null, "bethesda", "RaiserGames"};
		String[] facebookPages = {"34bigthings", null, "bethesda.anz", "RaiserGamesPublisher"};
		String[][] others = {null, {"twitter.com/bethesdastudios", "discord.com/invite/bethesdastudios"}, {"twitter.com/bethesda"}, {"instagram.com/raisergames"}};

		if (wSIDs != null && wSIDs.length > 0){
			Node websitesNode = dom.createElement("weboldalak");
			root.appendChild(websitesNode);
			for (int i = 0; i < wSIDs.length; i++){
				System.out.println(i + ". weboldalcsoport");
				Node websiteGroup = CreateWebsiteGroupNode(null, wSIDs[i], mainpages[i], youtubePages[i], facebookPages[i], others[i]);
				if (websiteGroup == null){
					error = true;
					continue;
				}
				websitesNode.appendChild(websiteGroup);
			}
		}

		//		- játékok
		String[] JIDs = {"TESV", "TESVDB", "TESVHF", "GoD"};
		String[] bjIDs = {null, "TESV", "TESV", null};
		String[] devSIDs = {"BethGS", "BethGS", "BethGS", "BT34"};
		String[] pSIDs = {"BethSW", "BethSW", "BethSW", "RG"};
		String[] gameNames = {"The Elder Scrolls V: Skyrim", "Dragonborn", "Hearthfire", "Goat of Duty"};
		float[] prices = {20, 20, 5, 10};
		String[] publishedDates = {"2011-11-11", "2012-12-04", "2012-09-04", "2019-07-10"};
		int[][] gameGenres = {{2, 9, 0, 3, 6}, {2, 9, 0, 3, 6}, {2, 9, 0, 3, 6}, {2, 10, 8}};
		String[][] saleDatas = {null, null, {"5", "2023-11-25T00:00:00", "2023-12-06T23:59:59"}, null};
		
		Node gamesNode = dom.createElement("játékok");
		root.appendChild(gamesNode);
		for (int i = 0; i < JIDs.length; i++){
			System.out.println(i + ". játék");
			Node game = CreateGameNode(JIDs[i], bjIDs[i], devSIDs[i], pSIDs[i], gameNames[i], prices[i], publishedDates[i], gameGenres[i], saleDatas[i]);
			if (game == null){
				error = true;
				continue;
			}
			gamesNode.appendChild(game);
		}

		//		- teljesítmények
		String[] TIDs = {"ACH1", "ACH2", "ACH3", "ACH4", "ACH6", "ACH12"};
		String[] tJIDs = {null, "TESV", "TESV", "TESVHF", "TESVDB", "GoD"};
		String[] aNames = {"Gyűjtemény", "Legendás", "Thu'um mester", "Polgár", "Miraak Temploma", "Cuki és halálos"};
		String[] descriptions = {"Szerezz meg 10 játékot.", "Érj el 100-as szintet valamely képességeddel.", "Tanulj meg 20 kiáltást.", "Vásárolj egy házat.", null, "Ölj meg 10 ellenfelet anélkül, hogy meghalj."};
		String[] aAddDates = {"2010-05-25", "2011-11-11", "2011-11-11", "2012-09-04", "2014-12-01", "2019-08-10"};

		if (TIDs != null && TIDs.length > 0){
			Node achievementsNode = dom.createElement("teljesítmények");
			root.appendChild(achievementsNode);
			for (int i = 0; i < TIDs.length; i++){
				System.out.println(i + ". teljesítmény");
				Node achievement = CreateAchievementNode(TIDs[i], tJIDs[i], aNames[i], descriptions[i], aAddDates[i]);
				if (achievement == null){
					error = true;
					continue;
				}
				achievementsNode.appendChild(achievement);

			}
		}

		//		- nyelvek
		String[] NyIDs = {"EN", "DE", "IT", "RU", "JP", "HU", "TR"};
		String[] langs = {"angol", "német", "olasz", "orosz", "japán", "magyar", "török"};

		if (NyIDs != null && NyIDs.length > 0){
			Node languagesNode = dom.createElement("nyelvek");
			root.appendChild(languagesNode);
			for (int i = 0; i < NyIDs.length; i++){
				System.out.println(i + ". nyelv");
				Node language = CreateLanguageNode(NyIDs[i], langs[i]);
				if (language == null){
					error = true;
					continue;
				}
				languagesNode.appendChild(language);
			}
		}

		//		- támogatott nyelvek
		String[] slJIDs = {"TESV", "TESV", "TESV", "TESV", "TESV", "TESVDB", "TESVDB", "TESVDB", "TESVDB", "TESVDB", "TESVHF", "TESVHF", "TESVHF", "TESVHF", "TESVHF", "GoD", "GoD", "GoD", "GoD", "GoD", "GoD"};
		String[] slNyIDs = {"EN", "JP", "DE", "IT", "RU", "EN", "JP", "DE", "IT", "RU", "EN", "JP", "DE", "IT", "RU", "EN", "JP", "DE", "IT", "RU", "TR"};
		boolean[] subtitleSupport = {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true};
		boolean[] soundSupport = {true, true, true, true, true, true, false, true, true, true, true, false, true, true, true, false, false, false, false, false, false};

		if (slJIDs != null && slJIDs.length > 0){
			Node supportedLanguagesNode = dom.createElement("támogatottNyelvek");
			root.appendChild(supportedLanguagesNode);
			for (int i = 0; i < slJIDs.length; i++){
				System.out.println(i + ". támogatott nyelv");
				Node supportedLanguage = CreateSupportedLanguageNode(slJIDs[i], slNyIDs[i], subtitleSupport[i], soundSupport[i]);
				if (supportedLanguage == null){
					error = true;
					continue;
				}
				supportedLanguagesNode.appendChild(supportedLanguage);
			}
		}

		//		- felhasználók
		String[] FIDs = {"F1", "F5", "F7", "F11", "F13", "F14"};
		String[] usernames = {"toothed2010", "snowwingedhunter123", "Dand3lion", "hooty3", "corgiii89", "julienmoveit"};
		String[] displayNames = {null, "Snow-winged hunter", "Jaskier the bard", "Hootsifer", null, null};
		String[] registrationDates = {"2010-06-11", "2017-01-15", "2019-10-20", "2022-08-31", "2022-11-05", "2023-12-01"};
		String[] birthdates = {"1982-12-30", "2002-10-04", "2000-09-23", "2000-05-18", "1989-03-10", "2007-02-16"};

		if (FIDs != null && FIDs.length > 0){
			Node usersNode = dom.createElement("felhasználók");
			root.appendChild(usersNode);
			for (int i = 0; i < FIDs.length; i++){
				System.out.println(i + ". felhasználó");
				Node user = CreateUserNode(FIDs[i], usernames[i], displayNames[i], registrationDates[i], birthdates[i]);
				if (user == null){
					error = true;
					continue;
				}
				usersNode.appendChild(user);
			}
		}

		//		- barátok
		String[] FID1 = {"F11", "F11", "F11", "F11", "F11", "F1", "F13", "F7", "F13"};
		String[] FID2 = {"F1", "F5", "F7", "F13", "F14", "F7", "F5", "F5", "F14"};

		if (FID1 != null && FID1.length > 0){
			Node friendsNode = dom.createElement("barátok");
			root.appendChild(friendsNode);
			for (int i = 0; i < FID1.length; i++){
				System.out.println(i + ". barát");
				Node friend = CreateFriendNode(FID1[i], FID2[i]);
				if (friend == null){
					error = true;
					continue;
				}
				friendsNode.appendChild(friend);				
			}
		}

		//		- hozzáadott játékok
		String[] agJIDs = {"TESV", "TESVHF", "TESV", "TESVDB", "TESVHF", "GoD", "TESVHF"};
		String[] agFIDs = {"F1", "F1", "F5", "F5", "F5", "F5", "F11"};
		String[] agAddDates = {"2010-06-19", "2018-05-23", "2021-11-11", "2021-11-11", "2021-11-11", "2017-09-09", "2022-10-01"};
		String[] states = {"kosárban", "kívánságlistán", "megvéve", "megvéve", "megvéve", "megvéve", "megvéve"};
		int[] playtimes = {0, 0, 174, 9, 31, 13, 0};

		if (agJIDs != null && agJIDs.length > 0){
			Node addedGamesNode = dom.createElement("hozzáadottJátékok");
			root.appendChild(addedGamesNode);
			for (int i = 0; i < agJIDs.length; i++){
				System.out.println(i + ". hozzáadott játék");
				Node addedGame = CreateAddedGameNode(agJIDs[i], agFIDs[i], agAddDates[i], states[i], playtimes[i]);
				if (addedGame == null){
					error = true;
					continue;
				}
				addedGamesNode.appendChild(addedGame);
			}
		}

		//		- elért teljesítmények
		String[] aaFIDs = {"F5", "F5", "F5"};
		String[] aaTIDs = {"ACH2", "ACH3", "ACH6"};
		String[] achieveDates = {"2021-12-02", "2021-11-15", "2022-04-13"};

		if (aaFIDs != null && aaFIDs.length > 0){
			Node achievedAchievementsNode = dom.createElement("elértTeljesítmények");
			root.appendChild(achievedAchievementsNode);
			for (int i = 0; i < aaFIDs.length; i++) {
				System.out.println(i + ". elért teljesítmény");
				Node achievedAchievement = CreateAchievedAchievementsNode(aaFIDs[i], aaTIDs[i], achieveDates[i]);
				if (achievedAchievement == null){
					error = true;
					continue;
				}
				achievedAchievementsNode.appendChild(achievedAchievement);
			}
		}

		if (!error){
			DomReadULA7Z2.printChildren(root, 0);
			DOMToXML(dom, writePath);
		}
	}



//#region file-ba való kiíratás dolgok
    // teljes Document strukturált kiíratása file-ba
    static void DOMToXML(Document document, String path){
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

            Element rootElement = document.getDocumentElement();
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

    // csomópont strukturált kiíratása file-ba
    static void NodeToXML(String path, Node startNode){
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
            writeChildren(startNode, 0);
            xml.close();
        }
        catch (IOException ioe){
            System.err.println("IO error!");
            ioe.printStackTrace();
            return;
        }
        
        System.out.println("Node info written to XML!");
    }

    // írandó file megnyitása/létrehozása
    private static void openFile(String path){
        try {
            xml = new FileWriter(path, StandardCharsets.UTF_8);            
        } catch (IOException ioe) {
            System.err.println("IO ERROR!");
            ioe.printStackTrace();
        }
    }

    // csomópontok kiírására szolgáló függvények
    private static void writeChildren(Node parent, int indentAmount) throws IOException{
        boolean textOnly = false;
        writeNodeInfo(parent, indentAmount);
        for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.hasChildNodes()){
                writeChildren(child, indentAmount+1);
            }
            else {
                if (!isRandomNode(child)){
					if (child.hasAttributes()){
						xml.write("\n");
						writeIndent(indentAmount + 1);
                        xml.write("<" + child.getNodeName());
                        writeAttributes(child);
                        xml.write(" />");
                    }
                    else{
						xml.write(child.getNodeValue());
						textOnly = true;
					}
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

	// csak szöveges vagy üres csomópontokat irasson ki
    // ez a fölösleges üres sorok elkerülésének érdekében van
    private static boolean isRandomNode(Node node){
		if (node.hasAttributes()) return false;
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

    // különböző típusú csomópontok létrehozása
    // megspórolja annak az ellenőrzését, hogy megfelelő adattípust adunk-e meg egy adott csomóponthoz
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

    private static Node createFloatNode(String nodeName, float nodeContent){
        return createTextNode(nodeName, String.valueOf(nodeContent));
    }

    private static Node createDateNode(String nodeName, String nodeContent, String dateType){
        // dateType lehet date vagy datetime
        if (dateType.equals("date")){
            try{
                LocalDate.parse(nodeContent);
                return createTextNode(nodeName, nodeContent);
            }catch(Exception e){
                System.err.println("(" + nodeName + ") Dátum formátuma nem megfelelő! Helyes formátum: ÉÉÉÉ-HH-NN, pl. 2023-10-12");
                return null;
            }
        }
        else if (dateType.equals("datetime")){
            try{
                LocalDateTime.parse(nodeContent);
                return createTextNode(nodeName, nodeContent);
            }catch(Exception e){
                System.err.println("(" + nodeName + ") Dátum formátuma nem megfelelő! Helyes formátum: ÉÉÉÉ-HH-NNTOO:PP:MM, pl. 2023-10-12T10:00:00");
                return null;
            }
        }
        else{
            System.err.println("(" + nodeName + ") Két dátumtípus adható meg: date, datetime!");
            return null;
        }
    }

    private static Node createGenreNode(int genreIndex){
        if (genreIndex >= genres.length){
            System.err.println("Az index túl nagy! " + genres.length + " műfaj van!");
            return null;
        }
        return createTextNode("műfaj", genres[genreIndex]);
    }

    private static Node createBoolNode(String nodeName, boolean nodeContent){
        if (nodeContent){
            return createTextNode(nodeName, "igen");
        }
        else{
            return createTextNode(nodeName, "nem");
        }
    }

    private static Node createStateNode(String state){
        // állapot lehet: "kívánságlistán", "kosárban", "megvéve"
        if (!(state.equals("kívánságlistán") || state.equals("kosárban") || state.equals("megvéve"))){
            System.err.println("Nem valid állapot! Állapot típusok: kívánságlistán, kosárban, megvéve");
            return null;
        }
        return createTextNode("állapot", state);
    }

    // stúdió csomópont létrehozása
    // nem kötelező megadású: introText
    private static Node CreateStudioNode(String SID, String name, String introText, int foundedIn){
        // ellenőrzések
        if (SID == null || SID.isEmpty() || name == null){
            System.err.println("Csak a bemutatószöveg maradhat üresen!");
            return null;
        }
        if (foundedIn < 1900 && foundedIn > LocalDate.now().getYear())
        {
            System.err.println("(" + SID + ") Nem megfelelő évszám!");
            return null;
        }
        if (IDs.contains(SID)){
            System.err.println("(" + SID + ") Már van ilyen ID-jü stúdió!");
            return null;
        }

        // csomópontok létrehozása
        Element studioNode = dom.createElement("stúdió");
        Node nameNode = createTextNode("stúdióNeve", name),
            foundedNode = createIntNode("alapításÉve", foundedIn);
        
        try{
            studioNode.setAttribute("SID", SID);
            studioNode.appendChild(nameNode);
            if (introText != null){
                Node introTextNode = createTextNode("bemutatóSzöveg", introText);
                studioNode.appendChild(introTextNode);
            }
            studioNode.appendChild(foundedNode);
        } catch (NullPointerException npe){
            return null;
        }

        // csak akkor adjuk hozzá az ID-ket, ha sikerült rendesen létrehozni
        IDs.add(SID);
        studios.put(SID, studioNode);
        
        return studioNode;
    }
    
    // weboldalcsoport csomópont létrehozása
    // nem kötelező megadásúak: WID, youtube, facebook, others
    private static Node CreateWebsiteGroupNode(String WID, String SID, String mainpage, String youtube, String facebook, String[] others){
        // ellenőrzések
        if (SID == null || mainpage == null){
            System.err.println("Az SID és a föoldallink nem maradhatnak üresen!");
            return null;
        }
        if (studios.get(SID) == null){
            System.err.println("(" + SID + ") Először létre kell hozni az adott ID-jü stúdiót!");
            return null;
        }
        if (WID == null || WID.isEmpty()){
            WID = SID + "Web";
            if (IDs.contains(WID)){
                System.err.println("Meg kell adni egy WID-et, mert már létezik " + WID + " azonosítójú elem! Nem ajánlott SID+'Web' nevű azonosítót használni a WID-en kívül!");
                return null;
            }
        }
        if (IDs.contains(WID)){
            System.err.println("(" + SID + ") Már létezik ilyen azonosítójú elem!");
            return null;
        }
        if (websiteGroups.get(SID) != null){
            System.err.println("(" + SID + ") Egy stúdióhoz csak egy weboldalcsoport tartozhat!");
            return null;
        }

        // csomópontok létrehozása
        Element websiteGroupNode = dom.createElement("weboldal");
        Node mainpageNode = createTextNode("főoldal", mainpage);

        try{
            websiteGroupNode.setAttribute("WID", WID);
            websiteGroupNode.setAttribute("W_stúdió", SID);
            websiteGroupNode.appendChild(mainpageNode);
        } catch (NullPointerException npe){
            return null;
        }

        if (youtube != null){
            Node youtubeNode = createTextNode("Youtube", youtube);
            if (youtubeNode == null) return null;
            websiteGroupNode.appendChild(youtubeNode);
        }
        if (facebook != null){
            Node facebookNode = createTextNode("Facebook", facebook);
            if (facebookNode == null) return null;
            websiteGroupNode.appendChild(facebookNode);
        }

        if (others != null){
            for (String other : others){
                Node otherNode = createTextNode("egyéb", other);
                if (otherNode != null) websiteGroupNode.appendChild(otherNode);
            }
        }

        // csak akkor adjuk hozzá az ID- és weboldalcsoport-listához, ha valóban sikerült létrehozni
        IDs.add(WID);
        websiteGroups.put(SID, websiteGroupNode);

        return websiteGroupNode;
    }

    // játék csomópont létrehozása
    // nem kötelező megadásúak: baseGame, gameGenres, saleData
    private static Node CreateGameNode(String JID, String baseGame, String developer, String publisher, String name, float price, String publishedDate, int[] genreIndices, String[] saleData){
        // kötelező megadású elemek ellenőrzése
        if (JID == null || developer == null || publisher == null || JID.isEmpty()){
            System.err.println("Csak az alapjáték attribútum lehet üres!");
            return null;
        }
        if (name == null || publishedDate == null){
            System.err.println("Csak a játékműfajok és a leárazásinformációk maradhatnak üresen!");
            return null;
        }

        // attribútumok ellenőrzése
        // (JID-nél egyediség, alapjátéknál, hogy van-e olyan JID-ű játék, fejlesztő és kiadó stúdiónál, hogy van-e ilyen stúdió)
        if (IDs.contains(JID)){
            System.err.println("(" + JID + ") Már van ilyen ID-jü játék!");
            return null;
        }
        if (baseGame != null && games.get(baseGame) == null){
            System.err.println("(" + JID + ") Alapjátéknak csak már bevitt játék feltethető meg!");
        }
        if (studios.get(developer) == null || studios.get(publisher) == null){
            System.err.println("(" + JID + ") A fejlesztőnek és kiadónak már létező stúdióknak kell lenniük!");
            return null;
        }

        // elemtartalmak ellenőrzése
        // (a leárazások elemei közül egyik sem lehet üres, ha már meg van adva; a százalék 0 és 100 között legyen; az ár ne legyen negatív)
        if (price < 0){
            System.err.println("Az ár nem lehet negatív!");
            return null;
        }
        if (saleData != null){
            if (saleData.length != 3){
                System.err.println("Meg kell adni az összes leárazásadatot!");
                return null;
            }
            for (String saleElement : saleData){
                if (saleElement == null){
                    System.err.println("Ha már meg van adva valamilyen leárazásadat, meg kell adni az egészet!");
                    return null;
                }
            }

            int percent = Integer.parseInt(saleData[0]);
            if (percent <= 0 || percent > 100){
                System.err.println("A leárazási százaléknak 0 és 100 közötti értéknek kell lennie!");
                return null;
            }
        }

        // csomópontok tényleges létrehozása
        Element gameNode = dom.createElement("játék");
        Node nameNode = createTextNode("játékNeve", name),
            priceNode = createFloatNode("ár", price),
            publishedDateNode = createDateNode("kiadásDátuma", publishedDate, "date");

        try{
            gameNode.setAttribute("JID", JID);
            if (baseGame != null) gameNode.setAttribute("alapjáték", baseGame);
            gameNode.setAttribute("fejlesztő", developer);
            gameNode.setAttribute("kiadó", publisher);
            gameNode.appendChild(nameNode);
            gameNode.appendChild(priceNode);
            gameNode.appendChild(publishedDateNode);
        } catch (NullPointerException npe){
            return null;
        }

        // műfajok csomópont összeállítása
        if (genreIndices != null){
            Node genresNode = dom.createElement("műfajok");
            for (int genreIndex : genreIndices){
                Node genreNode = createGenreNode(genreIndex);
                if (genreNode == null) return null;
                genresNode.appendChild(genreNode);
            }
            gameNode.appendChild(genresNode);
        }
        // leárazás csomópont összeállítása
        // ami nincs ellenőrizve:
        //      - a leárazás ne legyen máris lejárva (ezek később törölhetőek)
        //      - a leárazás kezdési ideje ne legyen később, mint a befejezési idő
        if (saleData != null){
            Node saleDataNode = dom.createElement("leárazás");
            Node salePercent = createIntNode("százalék", Integer.parseInt(saleData[0]));
            if (salePercent == null){
                System.err.println("Leárazási százalék nem megfelelően volt megadva!");
                return null;
            }
            Node saleStart = createDateNode("kezdés", saleData[1], "datetime");
            if (saleStart == null){
                System.err.println("Leárazás kezdési időpontja nem megfelelően volt megadva!");
                return null;
            }
            Node saleEnd = createDateNode("vége", saleData[2], "datetime");
            if (saleEnd == null){
                System.err.println("Leárazás végződési időpontja nem megfelelően volt megadva!");
                return null;
            }
            saleDataNode.appendChild(salePercent);
            saleDataNode.appendChild(saleStart);
            saleDataNode.appendChild(saleEnd);
            gameNode.appendChild(saleDataNode);
        }

        // csak akkor adjuk hozzá az ID- és játéklistához, ha valóban sikerült létrehozni
        IDs.add(JID);
        games.put(JID, gameNode);

        return gameNode;
    }

    // teljesítmény csomópont létrehozása
    // nem kötelező megadású: JID, description
    private static Node CreateAchievementNode(String TID, String JID, String name, String description, String addDate){
        // ellenőrzések
        if (TID == null || TID.isEmpty() || name == null || addDate == null){
            System.err.println("Csak a JID és a teljesítmény leírása maradhat üresen!");
            return null;
        }
        if (IDs.contains(TID)){
            System.err.println("(" + TID + ") Már van ilyen ID-jü teljesítmény!");
            return null;
        }
        if (JID != null && games.get(JID) == null){
            System.err.println("(" + TID + ") A teljesítmény előtt a játékot kell létrehozni!");
            return null;
        }

        // csomópontok létrehozása
        Element achievementNode = dom.createElement("teljesítmény");
        Node nameNode = createTextNode("teljesítményNeve", name),
            addDateNode = createDateNode("teljesítményHozzáadásánakDátuma", addDate, "date");

        try{
            achievementNode.setAttribute("TID", TID);
			if (JID != null) achievementNode.setAttribute("T_játék", JID);
            achievementNode.appendChild(nameNode);
            if (description != null){
                Node descriptionNode = createTextNode("teljesítményLeírása", description);
                achievementNode.appendChild(descriptionNode);
            }
            achievementNode.appendChild(addDateNode);
        } catch (NullPointerException npe){
            return null;
        }

        // csak akkor adjuk hozzá az ID- és teljesítménylistához, ha valóban sikerült létrehozni
        IDs.add(TID);
        achievements.put(TID, achievementNode);

        return achievementNode;
    }

	// nyelv csomópont létrehozása
	// minden kötelező megadású
    private static Node CreateLanguageNode(String NyID, String language){
        // ellenőrzések
		if (NyID == null || NyID.isEmpty() || language == null || language.isEmpty()){
			System.err.println("A nyelv csomópontnak kötelező megadni az ID-jét és a nevét is!");
			return null;
		}
		if (IDs.contains(NyID)){
			System.err.println("(" + NyID + ") Már van ilyen azonosítójú nyelv!");
			return null;
		}

		// csomópont létrehozása
		Element languageNode = dom.createElement("nyelv");
		languageNode.setAttribute("NyID", NyID);
		languageNode.setTextContent(language);

		IDs.add(NyID);
		languages.put(NyID, languageNode);
		return languageNode;
    }

	// támogatottNyelv csomópont létrehozása
	// minden kötelező megadású
	private static Node CreateSupportedLanguageNode(String JID, String NyID, boolean subtitles, boolean sound){
		// ellenőrzések
		if (JID == null || NyID == null){
			System.err.println("A támogatott nyelveknél mindent meg kell adni!");
			return null;
		}
		if (!(subtitles || sound)){
			System.err.println("(" + NyID + ") Ha nem támogat sem feliratot, sem hangot egy nyelv esetén, akkor a játék nem támogatja a nyelvet!");
			return null;
		}
		if (games.get(JID) == null){
			System.err.println("(" + JID + ", " + NyID +") Elöször létre kell hozni a játékot!");
			return null;
		}
		if (languages.get(NyID) == null){
			System.err.println("(" + JID + ", " + NyID +") Elöször létre kell hozni a nyelvet!");
			return null;
		}

		// csomópontok létrehozása
		Element supportedLanguageNode = dom.createElement("támogatottNyelv");
		Node subtitlesNode = createBoolNode("felirat", subtitles),
			 soundNode = createBoolNode("hang", sound);
		
		try{
			supportedLanguageNode.setAttribute("TNY_játék", JID);
			supportedLanguageNode.setAttribute("TNY_nyelv", NyID);
			supportedLanguageNode.appendChild(subtitlesNode);
			supportedLanguageNode.appendChild(soundNode);
		} catch (NullPointerException npe){
			return null;
		}

		return supportedLanguageNode;
	}

	// felhasználó csomópont létrehozása
	// nem kötelező megadású: displayName
	private static Node CreateUserNode(String FID, String username, String displayName, String registrationDate, String birthdate){
		// ellenőrzések
		if (FID == null || FID.isEmpty() || username == null || registrationDate == null || birthdate == null){
			System.err.println("Csak a megjelenített név maradhat üresen!");
			return null;
		}
		if (IDs.contains(FID)){
			System.err.println("(" + FID + ") Már van ilyen ID-jü felhasználó!");
		}

		// csomópontok létrehozása
		Element userNode = dom.createElement("felhasználó");
		Node usernameNode = createTextNode("felhasználónév", username),
			 registrationDateNode = createDateNode("fiókLétrehozásánakDátuma", registrationDate, "date"),
			 birthdateNode = createDateNode("születésiDátum", birthdate, "date");
		
		try{
			userNode.setAttribute("FID", FID);
			userNode.appendChild(usernameNode);
			if (displayName != null){
				Node displayNameNode = createTextNode("megjelenítettNév", displayName);
				userNode.appendChild(displayNameNode);
			}
			userNode.appendChild(registrationDateNode);
			userNode.appendChild(birthdateNode);
		} catch (NullPointerException npe){
			return null;
		}

		// ha sikerült létrehozni, hozzáadjuk az ID- és felhasználólistához
		IDs.add(FID);
		users.put(FID, userNode);

		return userNode;
	}

	// barát csomópont létrehozása
	// minden kötelező megadású
	private static Node CreateFriendNode(String FID1, String FID2){
		// ellenőrzések
		if (FID1 == null || FID1.isEmpty() || FID2 == null || FID2.isEmpty()){
			System.err.println("A barát csomópontnál mindkét felhasználó ID-jét meg kell adni!");
			return null;
		}
		if (users.get(FID1) == null || users.get(FID2) == null){
			System.err.println("(" + FID1 + ", " + FID2 + ") Először a felhasználókat kell létrehozni!");
			return null;
		}
		if (friends.contains(FID1 + FID2) || friends.contains(FID2 + FID1)){
			System.err.println("(" + FID1 + ", " + FID2 + ") A felhasználók barátsága már be lett jegyezve!");
			return null;
		}

		// csomópont létrehozása
		Element friendNode = dom.createElement("barát");
		friendNode.setAttribute("felhasználó1", FID1);
		friendNode.setAttribute("felhasználó2", FID2);

		friends.add(FID1 + FID2);
		friends.add(FID2 + FID1);
		return friendNode;
	}

	// hozzáadottJáték csomópont létrehozása
	// minden kötelező megadású
	private static Node CreateAddedGameNode(String JID, String FID, String addDate, String state, int playtime){
		// ellenőrzések
		if (JID == null || FID == null || addDate == null || state == null)
		{
			System.err.println("Hozzáadott játék esetén minden adatot meg kell adni!");
			return null;
		}
		if (state.equals("megvéve") && playtime < 0){
			System.err.println("A játszási idő nem lehet negatív");
			return null;
		}
		if (games.get(JID) == null){
			System.err.println("(" + JID + ", " + FID + ") Először létre kell hozni a játékot!");
			return null;
		}
		if (users.get(FID) == null){
			System.err.println("(" + JID + ", " + FID + ") Először létre kell hozni a felhasználót!");
			return null;
		}

		// csomópontok létrehozása
		Element addedGameNode = dom.createElement("hozzáadottJáték");
		Node addDateNode = createDateNode("játékHozzáadásánakDátuma", addDate, "date"),
			 stateNode = createStateNode(state);

		try{
			addedGameNode.setAttribute("HJ_játék", JID);
			addedGameNode.setAttribute("HJ_felhasználó", FID);
			addedGameNode.appendChild(addDateNode);
			addedGameNode.appendChild(stateNode);
		} catch (NullPointerException npe){
			return null;
		}

		if (state.equals("megvéve")){
			Node playtimeNode = createIntNode("játszásIdőtartama", playtime);
			if (playtimeNode == null) return null;
			addedGameNode.appendChild(playtimeNode);
		}

		return addedGameNode;
	}

	// elértTeljesítmény csomópont létrehozása
	// minden kötelező megadású
	private static Node CreateAchievedAchievementsNode(String FID, String TID, String achieveDate){
		// ellenőrzések
		if (FID == null || TID == null || achieveDate == null){
			System.err.println("Az elértTeljesítmény csomópontnál semmi sem maradhat üresen!");
			return null;
		}
		if (users.get(FID) == null){
			System.err.println("(" + FID + ", " + TID + ") Először létre kell hozni a felhasználót!");
			return null;
		}
		if (achievements.get(TID) == null){
			System.err.println("(" + FID + ", " + TID + ") Először létre kell hozni a teljesítményt!");
			return null;
		}

		// csomópontok létrehozása
		Element achievedAchievementNode = dom.createElement("elértTeljesítmény");
		Node achieveDateNode = createDateNode("teljesítményElérésénekDátuma", achieveDate, "date");

		try{
			achievedAchievementNode.setAttribute("teljesítő", FID);
			achievedAchievementNode.setAttribute("teljesítettTeljesítmény", TID);
			achievedAchievementNode.appendChild(achieveDateNode);
		} catch (NullPointerException npe){
			return null;
		}

		return achievedAchievementNode;
	}

}