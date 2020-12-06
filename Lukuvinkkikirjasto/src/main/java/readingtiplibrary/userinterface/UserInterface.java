
package readingtiplibrary.userinterface;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import readingtiplibrary.dao.ReadingTipDAO;
import readingtiplibrary.domain.ReadingTip;
import readingtiplibrary.domain.Valid;


public class UserInterface {
    
    private InputOutput io;
    private ReadingTipDAO dao;
    
    public UserInterface(final InputOutput io, final ReadingTipDAO dao) {
        this.io = io;
        this.dao = dao;
    }
    
    public void run() throws SQLException {
        io.print("Lukuvinkkisovellus \n---------------");
        io.print("Komennot:\nu: lisää uusi lukuvinkki\nl: listaa lukuvinkit"
                + "\ne: hallinnoi lukuvinkkejä\nluot: luo tietokanta"
                + "\np: poistu sovelluksesta\n");
        
        boolean continues = true;
        while (continues) {
            io.print("Päävalikko\n---------------");
            io.print("Anna komento: (u, l, e, luot, p)");
            String command = io.nextLine();
            switch (command) {
                case "u":
                    addReadingTip();
                    break;
                case "l":
                    listTips();
                    break;
                case "e":
                    search();
                    break;
                case "luot":
                    dao.createDatabase();
                    break;
                case "p":
                    continues = false;
                    break;
                default:
                    io.print("Virheellinen näppäinvalinta. Yritä uudestaan.");
            }
        }
    }
    
    private void addReadingTip() {
        io.print("\nHaluatko lisätä lukuvinkin otsikolla, vai URLina? Valitse o/u");
        String input = io.nextLine();
        switch (input) {
            case "o":
                addWithTitle();
                break;
            case "u":
                addWithUrl();
                break;
            default:
                io.print("Virheellinen komento, palataan päävalikkoon.");
                break;
        }
    }
    
    private void addWithTitle() {
        while (true) {
            io.print("");
            io.print("Anna lukuvinkin otsikko: ");
            String title = io.nextLine();
            
            if (!dao.searchByTitle(title, true).isEmpty()) {
                io.print("Löytyy jo lukuvinkki kyseisellä otsikolla.");
                continue;
            } else if (title.equals("")) {
                io.print("Otsikossa täytyy olla vähintään yksi kirjain.");
                continue;
            }
            
            ReadingTip newTip = new ReadingTip(dao.getAmountOfReadingTips() + 1, title);
            io.print("\nHaluatko lisätä lukuvinkille tageja? Valitse k/e");
            String command = io.nextLine();
            if (command.equals("k")) {
                addTags(newTip);
            }
            
            io.print("\nHaluatko lisätä lukuvinkille linkin? Valitse k/e");
            command = io.nextLine();
            if (command.equals("k")) {
                addLink(newTip);
            }
            
            dao.add(newTip);
            io.print("Lukuvinkin lisääminen onnistui!");
            io.print("---------------");
            break;
        }
    }
    
    private void addWithUrl() {
        while (true) {
            io.print("Anna lukuvinkin URL");
            String url = io.nextLine();
            if (!Valid.checkURL(url)) {
                io.print("URL ei ollut validi, yritä uudelleen.");
                continue;
            }
            
            String title = Valid.getURLTitle(url);
            if (!dao.searchByTitle(title, true).isEmpty()) {
                io.print("Löytyy jo lukuvinkki kyseisellä otsikolla." + title + ".");
                continue;
            } else if (title.equals("")) {
                io.print("URLin otsikko on tyhjä, lisää otsikko ja linkki manuaalisesti: ");
                addWithTitle();
                break;
            }
            
            io.print("Löydettiin otsikko: " + title);
            
            ReadingTip newTip = new ReadingTip(dao.getAmountOfReadingTips() + 1, title);
            newTip.setLink(url);
            
            io.print("\nHaluatko lisätä lukuvinkille tageja? Valitse k/e");
            String command = io.nextLine();
            if (command.equals("k")) {
                addTags(newTip);
            }
            
            dao.add(newTip);
            io.print("Lukuvinkin lisääminen onnistui!");
            io.print("---------------");
            break;
            
        }
    }
    
    private void addTags(ReadingTip newTip) {
        io.print("Anna tageja tai poistu antamalla tyhjä merkkijono: ");
        while (true) {
            String tag = io.nextLine();
            if (tag.equals("")) {
                break;
            }
            if (Valid.checkTag(tag)) {
                newTip.addTag(tag);
            } else {
                io.print("Tagi saa sisältää vain kirjaimia, yritä uudelleen.");
            }
        }
    }
    
    private void addLink(ReadingTip newTip) {
        while (true) {
            io.print("Anna linkki: ");
            String link = io.nextLine();
            if (Valid.checkURL(link)) {
                newTip.setLink(link);
                break;
            }
            io.print("Linkki ei ollut validi, yritä uudestaan:");
        }
    }
    
    private void listTips() {
        List<ReadingTip> tipList = dao.getAll();
        if (tipList == null) {
            io.print("Lukuvinkkien hakeminen epäonnistui tai et ole lisännyt yhtään lukuvinkkiä.");
        } else {
            io.print("");
            tipList.forEach(tip -> {
                io.print(tip.toString() + "\n");
            });
        }
        io.print("---------------");
    }
    
    private void search() {
        io.print("Lukuvinkkihaku\n---------------");
        io.print("Haetaanko otsikon vai tagin perusteella? o/t");
        String command = io.nextLine();
        switch (command) {
            case "o":
                searchWithTitle();
                break;
            case "t":
                searchWithTag();
                break;
            default:
                io.print("Virheellinen komento, palataan päävalikkoon.");
                break;
        }
    }
    
    private void searchWithTitle() {
        io.print("Anna hakusana tai poistu antamalla tyhjä merkkijono: ");
        while (true) {
            String title = io.nextLine();
            if (title.equals("")) {
                break;
            }
            List<ReadingTip> results = dao.searchByTitle(title, false);
            switch (results.size()) {
                case 0:
                    io.print("Tuloksia ei löytynyt.");
                    break;
                case 1:
                    io.print("Löydettiin lukuvinkki!");
                    readingTipMenu(results.get(0));
                    break;
                default:
                    io.print("Monta tulosta:\n");
                    results.stream().map(l -> l.getTitle()).forEach(t -> io.print(t));
                    io.print("Tarkenna hakua!");
                    break;
            }
        }
    }
    
    private void searchWithTag() {
        ArrayList<String> tags = new ArrayList<>();
        List<ReadingTip> results = dao.searchByTags(tags);
        io.print("Syötä tageja, poistu tyhjällä merkkijonolla tai valitse vinkki numerolla.");
        while (true) {
            io.print("Anna tagi tai vinkin numero: ");
            String input = io.nextLine();
            if (input.isEmpty()) {
                break;
            } else if (input.matches("[0-9]+")) {
                try {
                    readingTipMenu(results.get(Integer.valueOf(input)));
                    break;
                } catch (Exception e) {
                    io.print("Virheellinen valinta!");
                }
            } else if (input.matches("\\D+")) {
                tags.add(input);
                results = dao.searchByTags(tags);
                io.print("Löydetyt vinkit:\n");
                for (int i = 0; i < results.size(); i++) {
                    io.print(i + ":");
                    io.print(results.get(i).getTitle());
                    io.print("");
                }
            } else {
                io.print("Virheellinen syöte.");
            }
        }
    }
    
    private void readingTipMenu(ReadingTip tip) {
        io.print("\nLukuvinkin muokkaus\n---------------");
        io.print(tip.toString() + "\n");
        io.print("Komennot:\nl: muokkaa linkkiä\nluettu: merkkaa luetuksi"
                + "\nt: muokkaa tageja\npoista: poista lukuvinkki\np: poistu");
        boolean continues = true;
        while (continues) {
            io.print("Anna komento: (l, luettu, t, poista, p)");
            String command = io.nextLine();
            switch (command) {
                case "l":
                    editLink(tip);
                    break;
                case "t":
                    editTags(tip);
                    break;
                case "poista":
                    removeTip(tip);
                    continues = false;
                    break;
                case "p":
                    continues = false;
                    break;
                case "luettu":
                    markAsRead(tip);
                    break;
                default:
                    io.print("Virheellinen näppäinvalinta. Yritä uudestaan.");
                    break;
            }
        }
       
        
    }
    
    private void editLink(ReadingTip tip) {
        io.print("Nykyinen linkki: " + dao.getLink(tip));
        io.print("Anna uusi linkki: (tyhjä merkkijono poistaa linkin)");
        String newLink = io.nextLine();
        
        if (newLink.equals("")) {
            tip.setLink("Ei lisättyä linkkiä");
            dao.addLink(tip, "Ei lisättyä linkkiä");
            io.print("Linkki poistettu.\n");
            return;
        } else if (Valid.checkURL(newLink)) {
            tip.setLink(newLink);
            dao.addLink(tip, newLink);
            io.print("Linkki päivitetty.\n");
            return;
        }
        io.print("Virheellinen syöte, linkkiä ei muokattu.");
    }
    
    private void editTags(ReadingTip tip) {
        io.print("Nykyiset tagit: " + tip.getTags());
        boolean continues = true;
        while (continues) {
            io.print("Poistetaanko vai lisätäänkö tagi? (p/l) Tyhjä merkkijono poistuu.");
            String command = io.nextLine();
            switch (command) {
                case "p":
                    removeTag(tip);
                    break;
                case "l":
                    addTag(tip);
                    break;
                default:
                    continues = false;
            }
        }
    }
    
    private void removeTag(ReadingTip tip) {
        io.print("Anna poistettava tagi: ");
        String tag = io.nextLine();
        try {
            if (dao.findValidTag(tip, tag)) {
                int tagId = dao.findTagId(tip, tag);
                dao.deleteTag(tagId);
                tip.deleteTag(tag);
                io.print("Tagi poistettu!");
            } else {
                io.print("Tagia " + tag + " ei löytynyt");
            }
        } catch (Exception e) {
            io.print("Virhe " + tag);
        }
    }
    
    private void addTag(ReadingTip tip) {
        io.print("Anna uusi tagi: ");
        String tag = io.nextLine();
        try {
            if (Valid.checkTag(tag)) {
                tip.addTag(tag);
                dao.addTag(tip, tag);
                io.print("Tagi lisätty!");
            } else {
                io.print("Tagissa on sallittu vain kirjaimia, yritä uudelleen ");
            }
        } catch (Exception e) {
            io.print("Virhe " + tag);
        }
    }
    
    private void removeTip(ReadingTip tip) {
        try {
            dao.deleteReadingTip(tip);
            io.print("Lukuvinkki poistettu!");
        } catch (Exception e) {
            io.print("Virhe: " + e);
        }
    }
    
    private void markAsRead(ReadingTip tip) {
        io.print("Merkitäänkö lukuvinkki luetuksi tänään "
            + "vai aiempana päivänä? Valitse t/a. "
            + "Keskeytä painamalla mitä tahansa muuta näppäintä.");
        String command = io.nextLine();
        
        switch (command) {
            case "t":
                LocalDate date = LocalDate.now();
                tip.setRead(date.toString());
                io.print(dao.markAsRead(tip));
                break;
            case "a":
                io.print("Anna päivämäärä muodossa vvvv-kk-pp:");
                String customDate = io.nextLine();
                if (Valid.checkDate(customDate)) {
                    tip.setRead(customDate);
                    io.print(dao.markAsRead(tip));
                } else {
                    io.print("Merkkaus epäonnistui, päivämäärä oli virheellinen.");
                }
                break;
            default:
                break;
        }
    }
}
