package readingtiplibrary.userinterface;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import readingtiplibrary.domain.ReadingTip;
import readingtiplibrary.domain.Valid;
import readingtiplibrary.dao.ReadingTipDAO;

public class UserInterface {

    private InputOutput io;
    private ReadingTipDAO library;

    public UserInterface(final InputOutput io, final ReadingTipDAO dao) {
        this.io = io;
        System.out.println(dao);
        this.library = dao;
    }

    public void run() throws SQLException {
        io.print("Alla on lueteltu ohjelman toiminnot ja niitä vastaavat "
                + "näppäimet. Valitse toiminto painamalla sitä vastaavaa " + "näppäintä.");
        io.print("u: Lisää uusi lukuvinkki.");
        io.print("l: Listaa lisäämiesi lukuvinkkien otsikot.");
        io.print("e: Etsi lukuvinkkiä otsikon perusteella.");
        io.print("et: Etsi lukuvinkkiä tagin perusteella. ");
        io.print("x: Poista lukuvinkki otsikon perusteella.");
        io.print("m: Muokkaa lukuvinkkiä.");
        io.print("luettu: Merkitse lukuvinkki luetuksi.");
        io.print("luot: Luo tietokanta. Toiminto luo " + "tietokannan, ellei sitä ole jo luotu.");
        io.print("p: Poistu ohjelmasta.");

        Boolean continues = true;

        while (continues) {
            io.print("Valitse toiminto (u, l, e, et, x, m, luettu, luot tai p): ");
            String command = io.nextLine();
            switch (command) {
                case "p":
                    this.library.close();
                    continues = false;
                    break;
                case "u":
                    addToLibrary();
                    break;
                case "l":
                    listItems();
                    break;
                case "e":
                    searchItems();
                    break;
                case "et":
                    searchByTags();
                    break;
                case "x":
                    deleteItem();
                    break;
                case "m":
                    editItem();
                    break;
                case "luettu":
                    searchItems();
                    break;
                case "luot":
                    this.library.createDatabase();
                    break;
                default:
                    io.print("Virheellinen näppäinvalinta. Yritä uudestaan.");
            }
        }
    }

    private void markAsRead(ReadingTip lukuvinkki) throws SQLException {
        //System.out.println("markAsRead() " + lukuvinkki.getRead());
        io.print("Merkitäänkö lukuvinkki luetuksi tänään "
            + "vai aiempana päivänä? Valitse t/a. "
            + "Keskeytä painamalla mitä tahansa muuta näppäintä.");
        String option = io.nextLine();
        while (true) {
            if (option.equals("t")) {
                LocalDate date = LocalDate.now();
                lukuvinkki.setRead(date);
                String notification = this.library.markAsRead(lukuvinkki);
                io.print(notification);
            } 
            if (option.equals("a")) {
                io.print("Anna päivämäärä muodossa vvvv-kk-pp:");
                String date = io.nextLine();
                if (Valid.checkDate(date)) {
                    lukuvinkki.setRead(LocalDate.parse(date));
                    String notification = this.library.markAsRead(lukuvinkki);
                    io.print(notification);
                    break;
                } else {
                    io.print("Päivämäärä oli väärässä muodossa tai muutoin virheellinen. Sinut ohjataan "
                    + "takaisin päävalikkoon.");
                    break;
                }
            } else {
                break;
            }
        }
        
    }

    /**
     * Get all 'lukuvinkki' from database and print those like: "title" "URL"
     */
    private void listItems() {
        List<ReadingTip> vinkit = this.library.getAll();
        if (vinkit == null) {
            io.print("Lukuvinkkien hakeminen epäonnistui " + "tai et ole vielä lisännyt yhtään lukuvinkkiä.");
        } else {
            //this.library.getAll().stream().map(l -> l.getTitle() + "\n" + l.getLink()).forEach(t -> io.print(t));
            List<ReadingTip> entries = this.library.getAll();
            entries.forEach(v -> {
                io.print(v.toString());
            });
        }
    }

    private void addAsTitle() {
        while (true) {
            io.print("Anna lukuvinkin otsikko: ");
            String title = io.nextLine();
            // Check wether there already exists lukuvinkki with same title
            List<ReadingTip> exists = this.library.searchByTitle(title, true);
            if (!exists.isEmpty()) { // Already exists.
                io.print("Löytyy jo lukuvinkki kyseisellä otsikolla.");
                continue;
            }
            if (title.equals("")) {
                io.print("Otsikossa täytyy olla vähintään yksi kirjain.");
            } else {
                ReadingTip newItem = new ReadingTip(this.library.getAmountOfReadingTips() + 1, title);

                io.print("Haluatko lisätä lukuvinkille tageja? Valitse k/e");
                String valinta = io.nextLine();
                if (valinta.equals("k")) {
                    io.print("Anna tageja tai poistu antamalla tyhjä merkkijono: ");
                    while (true) {
                        String tag = io.nextLine();
                        if (tag.equals("")) {
                            break;
                        }
                        if (Valid.checkTag(tag)) {
                            newItem.addTag(tag);
                        } else {
                            io.print("Tagissa on sallittu vain kirjaimia, yritä uudelleen: ");
                        }
                    }
                }
                io.print("Haluatko lisätä lukuvinkille linkin? Valitse k/e");
                String valinta2 = io.nextLine();
                if (valinta2.equals("k")) {
                    while (true) {
                        io.print("Anna linkki tai poistu antamalla tyhjä merkkijono: ");
                        String linkki = io.nextLine();
                        if (linkki.equals("")) {
                            break;
                        } else if (Valid.checkURL(linkki)) {
                            newItem.addLink(linkki);
                            break;
                        }
                        io.print("Linkki ei ollut validi!");
                    }
                }
                this.library.add(newItem);
                io.print("Lukuvinkin lisääminen onnistui!");
                break;
            }
        }
    }

    private void addAsURL() {
        while (true) {
            io.print("Anna lukuvinkin URL: ");
            String url = io.nextLine();
            if (Valid.checkURL(url)) {
            String title = Valid.getURLTitle(url);
            List<ReadingTip> exists = this.library.searchByTitle(title, true);
            if (!exists.isEmpty()) { // Already exists.
                io.print("Löytyy jo lukuvinkki kyseisellä otsikolla: " + title + ".");
                continue;
            }
            if (title.equals("")) {
                io.print("Otsikossa täytyy olla vähintään yksi kirjain.");
            } else {
                ReadingTip newItem = new ReadingTip(this.library.getAmountOfReadingTips() + 1, title);
                newItem.addLink(url);
                io.print("Haluatko lisätä lukuvinkille tageja? Valitse k/e");
                String valinta = io.nextLine();
                if (valinta.equals("k")) {
                    io.print("Anna tageja tai poistu antamalla tyhjä merkkijono: ");
                    while (true) {
                        String tag = io.nextLine();
                        if (tag.equals("")) {
                            break;
                        }
                        if (Valid.checkTag(tag)) {
                            newItem.addTag(tag);
                        } else {
                            io.print("Tagissa on sallittu vain kirjaimia, yritä uudelleen: ");
                        }
                    }
                }
                this.library.add(newItem);
                io.print("Lukuvinkin lisääminen onnistui!");
                break;
            }
        }
        io.print("URL ei ollut validi!");
        }
    }

    private void addToLibrary() {
        io.print("Haluatko lisätä lukuvinkin otsikolla, vai URLina? Valitse 'o'/'u'");
        String input = io.nextLine();
        if (input.equals("o")) {
            addAsTitle();
        } else if (input.equals("u")) {
            addAsURL();
        }

    }   

    private void deleteItem() throws SQLException {
        while (true) {
            io.print("Hae poistettavaa lukuvinkkiä otsikolla. " + "\nTyhjä merkkijono lopettaa.");
            String input = io.nextLine();
            if (input.equals("")) {
                break;
            } else {
                List<ReadingTip> results = this.library.searchByTitle(input, false);
                switch (results.size()) {
                    case 0:
                        io.print("Tuloksia ei löytynyt.");
                        break;
                    case 1:
                        io.print("Löydettiin lukuvinkki!");
                        this.library.deleteReadingTip(results.get(0));
                        io.print("Lukuvinkki poistettu!");
                        break;
                    default:
                        io.print("Monta tulosta:");
                        results.stream().map(l -> l.getTitle()).forEach(t -> io.print(t));
                        io.print("Tarkenna hakua!");
                }
            }
        }
    }

    private void editItem() throws SQLException {
        while (true) {
            io.print("Anna muokattavan lukuvinkin otsikko." + "\nVoit etsiä otsikkoa syöttämällä 'etsi'.");
            String input = io.nextLine();
            if (input.equals("etsi")) {
                searchItems();
            } else if (input.equals("")) {
                break;
            } else {
                boolean success = this.library.delete(input);
                addToLibrary();
                if (success) {
                    io.print("Muutokset tallennettu!");
                    break;
                } else {
                    System.out.println("Yritä uudestaan.");
                }
            }

        }
    }

    private void searchByTags() {
        ArrayList<String> tagit = new ArrayList<>();
        while (true) {
            io.print("Syötä tageja tai poistu antamalla tyhjä merkkijono; ");
            String input = io.nextLine();
            if (input.isEmpty()) {
                break;
            }
            tagit.add(input);
        }
        List<ReadingTip> vinkit = this.library.searchByTags(tagit);
        io.print("Löydetyt vinkit tageilla: " + tagit);
        for (ReadingTip vinkki : vinkit) {
            io.print(vinkki.getTitle());
        }
    }

    private void searchItems() throws SQLException {
        while (true) {
            io.print("Anna hakusana tai poistu antamalla tyhjä merkkijono: ");
            String title = io.nextLine();
            if (title.equals("")) {
                break;
            } else {
                List<ReadingTip> results = this.library.searchByTitle(title, false);
                switch (results.size()) {
                    case 0:
                        io.print("Tuloksia ei löytynyt.");
                        break;
                    case 1:
                        io.print("Löydettiin lukuvinkki!");
                        io.print(results.get(0).getTitle());
                        readingTipMenu(results.get(0));
                        break;
                    default:
                        io.print("Monta tulosta:");
                        results.stream().map(l -> l.getTitle()).forEach(t -> io.print(t));
                        io.print("Tarkenna hakua!");
                }
            }
            break;
        }
    }

    private void readingTipMenu(final ReadingTip lukuvinkki) throws SQLException {
        // Tässä voisi sitten kysellä poistetaanko/muokataanko vinkkiä.
        while (true) {
            io.print("Haluatko muokata tageja, linkkiä tai otsikkoa vai merkitä vinkin luetuksi? Valitse t/l/o/luettu, tyhjä rivi poistuu");
            io.print("Otsikko: " + lukuvinkki.getTitle());
            io.print("Tagit: " + lukuvinkki.getTags());
            io.print("Linkki: " + lukuvinkki.getLink());
            String command = io.nextLine();
            switch (command) {
                case "":
                    break;
                case "t":
                    taginMuokkaus(lukuvinkki);
                case "l":
                    editLink(lukuvinkki);
                case "luettu":
                    markAsRead(lukuvinkki);
            }
            break;
        }
    }

    private void editLink(ReadingTip lukuvinkki) throws SQLException {
        while (true) {
            io.print("Haluatko muokata tai poistaa linkin? Valitse m/p, tyhjä rivi poistuu"); 
            String input = io.nextLine();
            switch (input) {
                case "":
                    break;
                case "m":
                    io.print("Anna linkki:");
                    String linkki = io.nextLine();
                    if (linkki.equals("")) {
                        break;
                    } else if (Valid.checkURL(linkki)) {
                        lukuvinkki.addLink(linkki);
                        this.library.addLink(lukuvinkki, linkki);
                        io.print("Linkin tallentaminen onnistui!");
                    } else {
                        io.print("Virheellinen URL. Anna uusi linkki!");
                    }
                    break;
                case "p":
                    lukuvinkki.deleteLink();
                    this.library.deleteLink(lukuvinkki);
                    io.print("Linkin poistaminen onnistui!");
                    break;
            }
            break;
            
        }
    }

    public void taginMuokkaus(ReadingTip lukuvinkki) throws SQLException {
        io.print("Haluatko muokata, poistaa vai lisätä tagin? Valitse m/p/l, tyhjä rivi poistuu");
        String command = io.nextLine();
        switch (command) {
            case "":
                break;
            case "p":
                    io.print("Anna poistettava tagi: ");
                    String input = io.nextLine();
                    if (this.library.findValidTag(lukuvinkki, input)) {
                            int tagiId = this.library.findTagId(lukuvinkki, input);
                            this.library.deleteTag(tagiId);
                            lukuvinkki.deleteTag(input);
                            io.print("Tagi poistettu!");
                    } else {
                        io.print("Tagia " + input + " ei löytynyt");
                    }  
                break;
            case "l":
                while (true) {
                    io.print("Anna uusi tagi: ");
                    input = io.nextLine();
                    if (Valid.checkTag(input)) {
                        lukuvinkki.addTag(input);
                        this.library.addTag(lukuvinkki, input);
                        io.print("Tagi lisätty!");
                    } else {
                        io.print("Tagissa on sallittu vain kirjaimia, yritä uudelleen ");
                    }
                    break;
                }
                break;
            case "m":
                while (true) { 
                    io.print("Anna muokattava tagi: ");
                    input = io.nextLine();
                    if (this.library.findValidTag(lukuvinkki, input)) {
                        while (true) {
                        io.print("Anna muokattu tagi: ");
                        String newTag = io.nextLine();
                        if (Valid.checkTag(newTag)) {
                            int oldTag = this.library.findTagId(lukuvinkki, input);
                            lukuvinkki.deleteTag(input);
                            lukuvinkki.addTag(newTag);
                            this.library.deleteTag(oldTag);
                            this.library.addTag(lukuvinkki, newTag);
                            io.print("Tagi muokattu!");
                            break;
                        } 
                        io.print("Tagissa on sallittu vain kirjaimia, yritä uudelleen ");
                        }
                    } else {
                        io.print("Tagia " + input + " ei löytynyt");
                    }  
                    break;
                }
                break;
            default:
                io.print("Virheellinen näppäinvalinta. Yritä uudestaan.");
            }
    }

}

