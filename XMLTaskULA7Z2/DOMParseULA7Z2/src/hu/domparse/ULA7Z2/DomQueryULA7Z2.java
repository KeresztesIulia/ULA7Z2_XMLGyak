package hu.domparse.ULA7Z2;

import java.util.Map;
import java.util.Hashtable;

import org.w3c.dom.*;

// adatlekérdezésre vonatkozó metódusok
public class DomQueryULA7Z2 {
    public static void main(String[] args) {
        String readPath = "XMLTaskULA7Z2/DOMParseULA7Z2/src/files/XMLULA7Z2.xml";
		Document dom = DomReadULA7Z2.ReadXML(readPath);

        System.out.println("Lekérdezések:\n");
        // felhasználók platformon megjelenő neve
        // ha meg van adva megjelenített név, akkor az, ha nincs, akkor a felhasználónév
        DisplayedName(dom);
        System.out.println();

        // felhasználók által elért teljesítmények
        // olyan módon lekérdezve, hogy a felhasználó és a teljesítmény neve is látszodjon
        AchievedAchievements(dom);
        System.out.println();

        // azon támogatott nyelvek kiírása, amik esetén hang is támogatva van
        SupportedSoundLanguages(dom);
        System.out.println();

        // adott azonosítójú stúdió weboldalai
        String SID = "SMS";
        StudioWebsites(dom, SID);
        System.out.println();

        // adott azonosítójú felhasználó barátainak kiírása
        String FID = "F12";
        FriendsList(dom, FID);
	}
    
    // első lekérdezés: felhasználók platformon megjelenő neve
    // ez a megjelenített név, ha meg van adva, vagy a felhasználónév, ha nincs megadva
    private static void DisplayedName(Document dom){
        System.out.println("- felhasználók platformon megjelenö neve");
        NodeList users = dom.getElementsByTagName("felhasználó");
        for (int i = 0; i < users.getLength(); i++){
            Element user = (Element)users.item(i);
            Node displayName = user.getElementsByTagName("megjelenítettNév").item(0);
            if (displayName == null){
                displayName = user.getElementsByTagName("felhasználónév").item(0);
            }
            System.out.println(displayName.getTextContent());
        }
    }

    // második lekérdezés: felhasználók által elért teljesítmények (felhasználónév, teljesítménynév, elérés dátuma)
    private static void AchievedAchievements(Document dom){
        // felhasználók nevének kigyűjtése ID szerint
        Map<String,String> userNames = new Hashtable<>();
        NodeList users = dom.getElementsByTagName("felhasználó");
        for (int i = 0; i < users.getLength(); i++){
            Element user = (Element)users.item(i);
            String FID = user.getAttribute("FID");
            String userName = user.getElementsByTagName("felhasználónév").item(0).getTextContent();
            userNames.put(FID, userName);
        }

        // teljesítmények nevének kigyűjtése ID szerint
        Map<String,String> achievementNames = new Hashtable<>();
        NodeList achievements = dom.getElementsByTagName("teljesítmény");
        for (int i = 0; i < achievements.getLength(); i++){
            Element achievement = (Element)achievements.item(i);
            String TID = achievement.getAttribute("TID");
            String achievementName = achievement.getElementsByTagName("teljesítményNeve").item(0).getTextContent();
            achievementNames.put(TID, achievementName);
        }

        // kiíratás
        System.out.println("- elért teljesítmények olvasható kiíratása");
        System.out.println(" Felhasználó neve     Teljesítmény neve    Teljesítmény elérésének dátuma");
        System.out.println("-------------------------------------------------------------------------");
        NodeList achievedAchievements = dom.getElementsByTagName("elértTeljesítmény");
        for (int i = 0; i < achievedAchievements.getLength(); i++){
            Element achAchievement = (Element)achievedAchievements.item(i);
            String FID = achAchievement.getAttribute("teljesítő");
            String TID = achAchievement.getAttribute("teljesítettTeljesítmény");
            String achievedDate = achAchievement.getElementsByTagName("teljesítményElérésénekDátuma").item(0).getTextContent();
            System.out.println(userNames.get(FID) + "             " + achievementNames.get(TID) + "           " + achievedDate);
        }
    }

    // harmadik lekérdezés: azon támogatott nyelvek kiírása, amik esetén hang is támogatva van
    private static void SupportedSoundLanguages(Document dom){
        // játék nevének kigyűjtése ID szerint
        Map<String, String> gameNames = new Hashtable<>();
        NodeList games = dom.getElementsByTagName("játék");
        for (int i = 0; i < games.getLength(); i++){
            Element game = (Element)games.item(i);
            String JID = game.getAttribute("JID");
            String gameName = game.getElementsByTagName("játékNeve").item(0).getTextContent();
            gameNames.put(JID, gameName);
        }

        // nyelvek kigyűjtése ID szerint
        Map<String, String> languages = new Hashtable<>();
        NodeList langs = dom.getElementsByTagName("nyelv");
        for (int i = 0; i < langs.getLength(); i++){
            Element lang = (Element)langs.item(i);
            String NyID = lang.getAttribute("NyID");
            String language = lang.getTextContent();
            languages.put(NyID, language);
        }

        // hanggal támogatott nyelvek kiíratása
        System.out.print("- hanggal támogatott nyelvek kiíratása játékonként");
        String prevJID = null;
        NodeList supportedLanguages = dom.getElementsByTagName("támogatottNyelv");
        for (int i = 0; i < supportedLanguages.getLength(); i++){
            Element supportedLanguage = (Element)supportedLanguages.item(i);
            boolean supportsLanguage = supportedLanguage.getElementsByTagName("hang").item(0).getTextContent().equals("igen") ? true : false;
            if (supportsLanguage){
                String JID = supportedLanguage.getAttribute("TNY_játék");
                String NyID = supportedLanguage.getAttribute("TNY_nyelv");
                if (prevJID == null || !prevJID.equals(JID)){
                    prevJID = JID;
                    System.out.println();
                    System.out.println(gameNames.get(JID));
                    System.out.println("------------------------------");
                }
                System.out.println(languages.get(NyID));
            }
        }
    }

    // negyedik lekérdezés: adott azonosítójú stúdió weboldalai
    private static void StudioWebsites(Document dom, String SID){
        System.out.println("- " + SID + " azonosítójú stúdió weboldalainak kiírása (olvasható formátumban, nem XML struktúrálással)");
        // ellenőrizzük, hogy létezik-e adott SID-jű stúdió
        String studioName = null;
        NodeList studios = dom.getElementsByTagName("stúdió");
        for (int i = 0; i < studios.getLength(); i++){
            Element studio = (Element)studios.item(i);
            if (studio.getAttribute("SID").equals(SID)){
                studioName = studio.getElementsByTagName("stúdióNeve").item(0).getTextContent();
                break;
            }
        }
        if (studioName == null){
            System.err.println("Nincs ilyen ID-jü stúdió!");
            return;
        }

        // a stúdióhoz tartozó weboldalcsoport megkeresése
        NodeList webstiteGroups = dom.getElementsByTagName("weboldal");
        Element SIDwebsiteGroup = null;
        for (int i = 0; i < webstiteGroups.getLength(); i++){
            Element websiteGroup = (Element)webstiteGroups.item(i);
            if (websiteGroup.getAttribute("W_stúdió").equals(SID)){
                SIDwebsiteGroup = websiteGroup;
                break;
            }
        }
        if (SIDwebsiteGroup == null){
            System.out.println("A " + studioName + " stúdióhoz nem tartozik weboldalcsoport!");
            return;
        }

        // ha van hozzátartozó weboldalcsoport, azt kiírjuk
        System.out.println(studioName + " weboldalai/fiókjai");
        System.out.println("----------------------------------");
        NodeList websites = SIDwebsiteGroup.getChildNodes();
        for (int i = 0; i < websites.getLength(); i++){
            Node website = websites.item(i);
            String websiteType = website.getNodeName();
            if (!websiteType.equals("#text"))
                System.out.println(website.getNodeName() + ": " + website.getTextContent());
        }
    }

    // ötödik lekérdezés: adott ID-jű felhasználó barátlistája
    private static void FriendsList(Document dom, String FID){
        // felhasználónevek összegyűjtése FID szerint
        Map<String, String> usernames = new Hashtable<>();
        NodeList users = dom.getElementsByTagName("felhasználó");
        for (int i = 0; i < users.getLength(); i++){
            Element user = (Element)users.item(i);
            String fid = user.getAttribute("FID");
            String username = user.getElementsByTagName("felhasználónév").item(0).getTextContent();
            usernames.put(fid, username);
        }

        System.out.println("- adott ID-jü felhasználó barátlistája");
        // ellenőrizzük, hogy van-e adott ID-jű felhasználó
        if (usernames.get(FID) == null){
            System.err.println("Nincs " + FID + " azonosítójú felhasználó!");
            return;
        }

        // ha van ilyen felhasználó, kiírjuk a barátlistáját
        System.out.println(usernames.get(FID) + " barátlistája");
        System.out.println("-------------------------");
        NodeList friends = dom.getElementsByTagName("barát");
        for (int i = 0; i < friends.getLength(); i++){
            Element friend = (Element)friends.item(i);
            String user1 = friend.getAttribute("felhasználó1");
            String user2 = friend.getAttribute("felhasználó2");
            if (user1.equals(FID))
                System.out.println(usernames.get(user2));
            if (user2.equals(FID))
                System.out.println(usernames.get(user1));
        }
    }


}
