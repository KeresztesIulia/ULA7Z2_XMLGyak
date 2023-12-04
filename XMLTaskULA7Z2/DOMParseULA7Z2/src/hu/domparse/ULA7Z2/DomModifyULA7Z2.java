package hu.domparse.ULA7Z2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Hashtable;

import org.w3c.dom.*;

//adatmódosítást megvalósító metódusok
public class DomModifyULA7Z2 {
    public static void main(String[] args) {
        String readPath = "XMLTaskULA7Z2/DOMParseULA7Z2/src/files/XMLULA7Z2.xml";
		Document dom = DomReadULA7Z2.ReadXML(readPath);
		Element root = dom.getDocumentElement();

        // felhasználó megjelenített nevének beállítása FID alapján
        String FID = "F1";
        Element changedUser = AddOrChangeDisplayName(dom, FID, "New Display Name");

        // stúdióhoz weboldal hozzáadása/módosítás SID alapján
        // jelenleg ez felépíti a teljes weboldal-csoportját ennek a stúdiónak
        String SID = "BethGS";
        AddOrChangeStudioWebsite(dom, SID, "main", "BethGS mainpage");
        AddOrChangeStudioWebsite(dom, SID, "akármii", "BethGS other1");
        AddOrChangeStudioWebsite(dom, SID, "facebook", "BethGS facebook");
        AddOrChangeStudioWebsite(dom, SID, "valami cucc", "BethGS other2");
        Element changedWebsite = AddOrChangeStudioWebsite(dom, SID, "youtube", "BethGS youtube");

        // Új teljesítmény hozzáadása
        //  először egy konkrét játékhoz tartozó teljesítmény
        String achJID = "TESV";
        AddNewAchievement(dom, achJID, "Olvasó", "Olvass el 50 képességkönyvet.", "2011-11-11");
        //  majd egy általános, a platformra vonatkozó teljesítmény
        AddNewAchievement(dom, null, "Barátságos", "Barátkozz össze 10 felhasználóval a platformon.", "2023-12-04");
        NodeList achievementList = dom.getElementsByTagName("teljesítmény");

        // DLC-k nevéhez hozzáírni, hogy melyik játék DLC-je
        AddBasegameName(dom);

        // lejárt leárazás törlése
        RemoveEndedSales(dom);

        // leárazás megadása/módosítása JID alapján
        String JID = "TESV";
        AddOrChangeSale(dom, JID, 40, "2023-12-05T05:00:00", "2023-12-10T10:00:00");        


        // módosított dokumentum kiírása konzolra
        System.out.println("Módosítások:");

        System.out.print("- megjelenített név beállítása az " + FID + " azonosítójú játékos számára");
        DomReadULA7Z2.printChildren(changedUser, 0);
        System.out.println();

        System.out.print("- "+ SID + "-hez tartozó weboldal megváltoztatása");
        DomReadULA7Z2.printChildren(changedWebsite, 0);
        System.out.println();

        System.out.print("- teljesítmény hozzáadása a " + achJID + " azonosítójú játékhoz");
        DomReadULA7Z2.printChildren(achievementList.item(achievementList.getLength() - 2), 0);
        System.out.println();

        System.out.print("- platformra vonatkozó teljesítmény hozzáadása");
        DomReadULA7Z2.printChildren(achievementList.item(achievementList.getLength() - 1), 0);
        System.out.println();

        System.out.println("- DLC-k neve után zárójelben az alapjáték nevének kiíratása");
        System.out.println("- lejárt leárazások törlése");
        System.out.print("- leárazás hozzáadása vagy módosítása a " + JID + " azonosítójú játékban");
        DomReadULA7Z2.printChildren(root.getElementsByTagName("játékok").item(0), 0);
        
    }

    // első módosítás: felhasználó megjelenített nevének beállítása
    private static Element AddOrChangeDisplayName(Document dom, String FID, String displayName){
        // megadott azonosítójú felhasználó megtalálása
        NodeList users = dom.getElementsByTagName("felhasználó");
        Element FIDuser = null;
        Node displayNameNode = null;
        for (int i = 0; i < users.getLength(); i++){
            Element user = (Element)users.item(i);
            if (user.getAttribute("FID").equals(FID)){
                displayNameNode = user.getElementsByTagName("megjelenítettNév").item(0);
                FIDuser = user;
            }
        }
        if (FIDuser == null){
            System.err.println("Nincs " + FID + " azonosítójú felhasználó!");
            return FIDuser;
        }

        // ha a felhasználónak még nincs megjelenített név beállítva, létrehozzuk a csomópontot
        if (displayNameNode == null){
            // csomópont létrehozása
            displayNameNode = dom.createElement("megjelenítettNév");
            displayNameNode.setTextContent(displayName);

            // megjelenítettNév csomópont beszúrása a felhasználónév csomópont után
            Node registrationDate = FIDuser.getElementsByTagName("fiókLétrehozásánakDátuma").item(0);
            FIDuser.insertBefore(displayNameNode, registrationDate);
            
        }
        // ha már van megjelenített neve a felhasználónak
        else {
            displayNameNode.setTextContent(displayName);
        }
        return FIDuser;
    }

    // második módosítás: stúdióhoz weboldal hozzáadása SID alapján
    private static Element AddOrChangeStudioWebsite(Document dom, String SID, String websiteType, String value){
        // ellenőrizzük, hogy létezik-e adott azonosítójú stúdió
        NodeList studios = dom.getElementsByTagName("stúdió");
        boolean SIDstudio = false;
        for (int i = 0; i < studios.getLength() && SIDstudio == false; i++){
            Element studio = (Element)studios.item(i);
            if (studio.getAttribute("SID").equals(SID)){
                SIDstudio = true;
            }
        }
        if (!SIDstudio){
            System.err.println("A weboldalnak valamilyen stúdióra mutatnia kell!");
            return null;
        }
        
        // a weboldal típusát átalakítjuk olyanra, amilyen gyerekcsomópont lehetséges egy weboldal elemben
        websiteType = websiteType.toLowerCase();
        if (websiteType.equals("main") || websiteType.equals("főoldal") || websiteType.equals("mainpage")){
            websiteType = "főoldal";
        }
        else if (websiteType.equals("youtube") || websiteType.equals("yt")){
            websiteType = "Youtube";
        }
        else if (websiteType.equals("facebook") || websiteType.equals("fb")){
            websiteType = "Facebook";
        }
        else{
            websiteType = "egyéb";
        }
        
        // megadott azonosítójú felhasználó megtalálása
        NodeList websiteGroups = dom.getElementsByTagName("weboldal");
        Element SIDwebsiteGroup = null;
        Node website = null;
        for (int i = 0; i < websiteGroups.getLength(); i++){
            Element websiteGroup = (Element)websiteGroups.item(i);
            if (websiteGroup.getAttribute("W_stúdió").equals(SID)){
                website = websiteGroup.getElementsByTagName(websiteType).item(0);
                SIDwebsiteGroup = websiteGroup;
            }
        }
        if (SIDwebsiteGroup == null && !websiteType.equals("főoldal")){
            System.err.println("Nincs " + SID + "-hez tartozó weboldalcsoport! Ha létre szeretnél hozni egyet, először főoldalt kell hozzáadni!");
            return null;
        }
        
        // Ha még nem tartozik weboldalcsoport a stúdióhoz, és főoldalt akarunk módosítani/hozzáadni, akkor létrehozzuk
        // a weboldalcsoportot is
        if (SIDwebsiteGroup == null){
            // weboldalcsoport csomópont létrehozása
            Element websiteGroup = dom.createElement("weboldal");
            websiteGroup.setAttribute("WID", SID + "Web");
            websiteGroup.setAttribute("W_stúdió", SID);

            // főoldal csomópont létrehozása és hozzáadása az új weboldalcsoporthoz
            Node mainpageNode = dom.createElement("főoldal");
            mainpageNode.setTextContent(value);
            websiteGroup.appendChild(mainpageNode);

            // weboldalcsoport hozzáadása a weboldalak listájához
            // ha még nincs ilyen lista, létrehozzuk
            Node websites = dom.getElementsByTagName("weboldalak").item(0);
            if (websites == null){
                websites = dom.createElement("weboldalak");
                Node studioList = dom.getElementsByTagName("stúdiók").item(0);
                dom.insertBefore(websites, studioList.getNextSibling());
            }
            websites.appendChild(websiteGroup);
        }
        else{
            // ha már van kellő típusú csomópont, akkor csak egyszerűen lecseréljük az értékét
            if (website != null && !websiteType.equals("egyéb")){
                website.setTextContent(value);
                return SIDwebsiteGroup;
            }
            // ha nincs ilyen csomópont, akkor biztos, hogy nem főoldalról van szó, ekkor létrehozzuk a megfelelő csomópontot
            website = dom.createElement(websiteType);
            website.setTextContent(value);
            // és beszúrjuk a megfelelő helyre
            if (websiteType.equals("Youtube")){
                Node mainpage = SIDwebsiteGroup.getElementsByTagName("főoldal").item(0);
                SIDwebsiteGroup.insertBefore(website, mainpage.getNextSibling());
            }
            else if (websiteType.equals("Facebook")){
                Node otherWebsites = SIDwebsiteGroup.getElementsByTagName("egyéb").item(0);
                SIDwebsiteGroup.insertBefore(website, otherWebsites);
            }
            else{
                SIDwebsiteGroup.appendChild(website);
            }
        }
        return SIDwebsiteGroup;
    }

    // harmadik módosítás: új teljesítmény hozzáadása
    private static void AddNewAchievement(Document dom, String JID, String name, String description, String addDate){
        // a kötelező megadású mezők nem lehetnek null-ok
        if (name == null){
            System.err.println("A teljesítmény nevét kötelező megadni!");
            return;
        }
        if (addDate == null){
            System.err.println("A teljesítmény hozzáadási dátumát kötelező megadni!");
            return;
        }
        try{
            LocalDate addDateLD = LocalDate.parse(addDate);
            if (addDateLD.compareTo(LocalDate.now()) > 0){
                System.err.println("A hozzáadási dátum nem lehet későbbi a mainál!");
                return;
            }
        }catch (Exception e){
            System.err.println("A hozzáadási dátum nem megfelelő formátumú! (A formátum: ÉÉÉÉ-HH-NN, pl. 2022-04-25)");
            e.printStackTrace();
            return;
        }
        
        // ha játékhoz adunk hozzá teljesítményt, ellenőrizni kell, hogy létezik-e ilyen játék
        if (JID != null){
            boolean JIDgame = false;
            NodeList games = dom.getElementsByTagName("játék");
            for (int i = 0; i < games.getLength(); i++){
                Element game = (Element)games.item(i);
                if (game.getAttribute("JID").equals(JID)){
                    JIDgame = true;
                    break;
                }
            }
            if (!JIDgame){
                System.err.println("Nincs ilyen azonosítójú játék!");
                return;
            }
        }

        // teljesítmény csomópont létrehozása
        int achievementIndex = dom.getElementsByTagName("teljesítmény").getLength() + 1;
        Element newAchievement = dom.createElement("teljesítmény");
        newAchievement.setAttribute("TID", "ACH" + String.valueOf(achievementIndex));
        if (JID != null)
        {
            newAchievement.setAttribute("T_játék", JID);
        }

        // gyerekcsomópontok létrehozása és hozzáadása a teljesítmény csomóponthoz
        Node achievementName = dom.createElement("teljesítményNeve");
        achievementName.setTextContent(name);
        newAchievement.appendChild(achievementName);

        if (description != null){
            Node achievementDescription = dom.createElement("teljesítményLeírása");
            achievementDescription.setTextContent(description);
            newAchievement.appendChild(achievementDescription);
        }

        Node achievementAddDate = dom.createElement("teljesítményHozzáadásánakDátuma");
        achievementAddDate.setTextContent(addDate);
        newAchievement.appendChild(achievementAddDate);

        // új teljesítmény hozzáadása a teljesítménylistához
        // ellenőrizzük, hogy van-e már teljesítménylistánk
        Node achievementList = dom.getElementsByTagName("teljesítmények").item(0);
        // ha nincs, hozzuk létre, és szúrjuk be a megfelelő helyre
        if (achievementList == null)
        {
            achievementList = dom.createElement("teljesítmények");
            achievementList.appendChild(newAchievement);
            
            // a "teljesítmények" csomópontnak a "weboldalak" csomópont után kell jönnie, de nem biztos, hogy az létezik
            // azonban helyes dokumentumot feltételezünk, ami azt jelenti, hogy "stúdiók" csomópontnak viszont kötelezően léteznie
            // kell, mire teljesítményeket kezdünk hozzáadni
            Node insertAfterNode = dom.getElementsByTagName("weboldalak").item(0);
            if (insertAfterNode == null) insertAfterNode = dom.getElementsByTagName("stúdiók").item(0);
            dom.insertBefore(achievementList, insertAfterNode.getNextSibling());
        }

        achievementList.appendChild(newAchievement);
    }

    // negyedik módosítás: DLC-k neve után odaírni, hogy melyik játék DLC-i
    private static void AddBasegameName(Document dom){
        NodeList games = dom.getElementsByTagName("játék");
        
        // játék nevének gyors elérése érdekében JID-játékNév párosokat hozunk létre
        Map<String, String> gameNames = new Hashtable<>();
        for (int i = 0; i < games.getLength(); i++){
            Element game = (Element)games.item(i);
            if (!game.hasAttribute("alapjáték")){
                String gameName = game.getElementsByTagName("játékNeve").item(0).getTextContent();
                gameNames.put(game.getAttribute("JID"), gameName);
            }
        }

        // mivel kézzel bevitt dokumentumrész esetén megtörténhet, hogy valamelyik DLC azelőtt legyen felvive a file-ba, hogy
        // az alapjátékának az adatai bekerüljenek, így biztosabb kétszer végigfutni a játékokon, és csak második alkalommal
        // hozzátenni az alapjáték-neveket a DLC-khez
        // Ha biztosra vehetnénk, hogy teljesen program által lett felvive minden, akkor valószínűleg egyből az első for-ciklus
        // keretén belül el lehetett volna intézni ezt is
        for (int i = 0; i < games.getLength(); i++){
            Element game = (Element)games.item(i);
            if (game.hasAttribute("alapjáték")){
                String baseGameName = gameNames.get(game.getAttribute("alapjáték"));
                Node gameName = game.getElementsByTagName("játékNeve").item(0);
                gameName.setTextContent(gameName.getTextContent() + " (" + baseGameName + " DLC)");
            }
        }
    }

    // ötödik módosítás: lejárt leárazások törlése
    private static void RemoveEndedSales(Document dom){
        NodeList salesEndDates = dom.getElementsByTagName("vége");
        for (int i = 0; i < salesEndDates.getLength(); i++){
            Node saleEndDate = salesEndDates.item(i);
            LocalDateTime endTime = LocalDateTime.parse(saleEndDate.getTextContent());
            if (endTime.compareTo(LocalDateTime.now()) <= 0){
                Node sale = saleEndDate.getParentNode();
                sale.getParentNode().removeChild(sale);
            }
        }
    }

    // harodik módosítás: leárazás hozzáadása játékhoz vagy létező módosítása
    private static void AddOrChangeSale(Document dom, String JID, int percent, String startTime, String endTime) {
        // adatok helyességének ellenőrzése
        if (percent < 0 || percent > 100){
            System.err.println("A leárazási százaléknak 0 és 100 közötti értéknek kell lennie!");
            return;
        }
        LocalDateTime startTimeLDT;
        LocalDateTime endTimeLDT;
        try{
            startTimeLDT = LocalDateTime.parse(startTime);
        } catch(Exception e){
            System.err.println("A leárazás kezdési ideje nem megfelelö formátumban van. (A helyes formátum: ÉÉÉÉ-HH-NNTOO:PP:MM, pl 2023-03-11T11:12:12)");
            e.printStackTrace();
            return;
        }
        try{
            endTimeLDT = LocalDateTime.parse(endTime);
        } catch(Exception e){
            System.err.println("A leárazás lejárási ideje nem megfelelö formátumban van. (A helyes formátum: ÉÉÉÉ-HH-NNTOO:PP:MM, pl 2023-03-11T11:12:12)");
            e.printStackTrace();
            return;
        }
        if (startTimeLDT.compareTo(endTimeLDT) > 0 || startTimeLDT.equals(endTimeLDT)){
            System.err.println("A leárazás kezdési idejének korábbinak kell lennie, mint a lejárási idönek!");
            return;
        }
        if (endTimeLDT.compareTo(LocalDateTime.now()) < 0){
            System.err.println("Nem lehet már lejárt leárazást hozzáadni!");
        }

        // megadott ID-jű játék megtalálása
        NodeList games = dom.getElementsByTagName("játék");
        Element JIDgame = null;
        Node sale = null;
        for (int i = 0; i < games.getLength(); i++){
            Element game = (Element)games.item(i);
            if (game.getAttribute("JID").equals(JID)){
                sale = game.getElementsByTagName("leárazás").item(0);
                JIDgame = game;
                break;
            }
        }
        if (JIDgame == null){
            System.err.println("Nincs " + JID + " azonosítójú játék!");
            return;
        }

        // ha a játékhoz nem tartozik leárazás, hozzáadjuk
        if (sale == null){
            // leárazás csomópont létrehozása
            sale = dom.createElement("leárazás");
            Node salePercent = dom.createElement("százalék");
            salePercent.setTextContent(String.valueOf(percent));

            Node saleStartTime = dom.createElement("kezdés");
            saleStartTime.setTextContent(startTime);

            Node saleEndTime = dom.createElement("vége");
            saleEndTime.setTextContent(endTime);

            sale.appendChild(salePercent);
            sale.appendChild(saleStartTime);
            sale.appendChild(saleEndTime);

            // leárazás csomópont hozzáadása a játék elemhez
            JIDgame.appendChild(sale);
        }
        // ha már létezik a leárazás, módosítjuk
        else {
            Node saleData = ((Element)sale).getElementsByTagName("százalék").item(0);
            saleData.getFirstChild().setNodeValue(String.valueOf(percent));
            
            saleData = ((Element)sale).getElementsByTagName("kezdés").item(0);
            saleData.getFirstChild().setNodeValue(startTime);

            saleData = ((Element)sale).getElementsByTagName("vége").item(0);
            saleData.getFirstChild().setNodeValue(endTime);
        }
    }


}
