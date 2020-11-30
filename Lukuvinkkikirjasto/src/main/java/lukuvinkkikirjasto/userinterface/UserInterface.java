package lukuvinkkikirjasto.userinterface;

import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lukuvinkkikirjasto.Lukuvinkki;
import lukuvinkkikirjasto.Validi;
import lukuvinkkikirjasto.dao.LukuvinkkiDAO;

public class UserInterface {

    private InputOutput io;
    private LukuvinkkiDAO library;

    public UserInterface(final InputOutput io, final LukuvinkkiDAO dao) {
        this.io = io;
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
        io.print("luot: Luo tietokanta. Toiminto luo " + "tietokannan, ellei sitä ole jo luotu.");
        io.print("alustat: Alusta tietokanta. Toiminto poistaa " + "vanhan tietokannan ja luo uuden tietokannan.");
        io.print("p: Poistu ohjelmasta.");

        Boolean continues = true;

        while (continues) {
            io.print("Valitse toiminto (u, l, e, x, m, luot, alustat tai p): ");
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
                case "luot":
                    this.library.createDatabase();
                    break;
                case "alustat":
                    this.library.initializeDatabase(Paths.get("tietokanta.db"));
                    break;
                default:
                    io.print("Virheellinen näppäinvalinta. Yritä uudestaan.");
            }
        }
    }

    /**
     * Get all 'lukuvinkki' from database and print those like: "title" "URL"
     */
    private void listItems() {
        List<Lukuvinkki> vinkit = this.library.getAll();
        if (vinkit == null) {
            io.print("Lukuvinkkien hakeminen epäonnistui " + "tai et ole vielä lisännyt yhtään lukuvinkkiä.");
        } else {
            this.library.getAll().stream().map(l -> l.getOtsikko() + "\n" + l.getLinkki()).forEach(t -> io.print(t));
        }
    }

    private void addToLibrary() {
        while (true) {
            io.print("Anna lukuvinkin otsikko: ");
            String title = io.nextLine();
            // Check wether there already exists lukuvinkki with same title.
            List<Lukuvinkki> exists = this.library.searchByTitle(title, true);
            if (!exists.isEmpty()) { // Already exists.
                io.print("Löytyy jo lukuvinkki kyseisellä otsikolla.");
                continue;
            }
            if (title.equals("")) {
                io.print("Otsikossa täytyy olla vähintään yksi kirjain.");
            } else {
                Lukuvinkki newItem = new Lukuvinkki(this.library.getLukuvinkkienMaara() + 1, title);

                io.print("Haluatko lisätä lukuvinkille tageja? Valitse k/e");
                String valinta = io.nextLine();
                if (valinta.equals("k")) {
                    io.print("Anna tageja tai poistu antamalla tyhjä merkkijono: ");
                    while (true) {
                        String tag = io.nextLine();
                        if (tag.equals("")) {
                            break;
                        }
                        if (Validi.tarkistaTag(tag)) {
                            newItem.lisaaTagi(tag);
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
                        } else if (Validi.tarkistaURL(linkki)) {
                            newItem.lisaaLinkki(linkki);
                            break;
                        }
                    }
                }

                this.library.add(newItem);
                io.print("Lukuvinkin lisääminen onnistui!");
                break;
            }
        }
    }

    private void deleteItem() throws SQLException {
        while (true) {
            io.print("Hae poistettavaa lukuvinkkiä otsikolla. " + "\nTyhjä merkkijono lopettaa.");
            String input = io.nextLine();
            if (input.equals("")) {
                break;
            } else {
                List<Lukuvinkki> results = this.library.searchByTitle(input, false);
                switch (results.size()) {
                    case 0:
                        io.print("Tuloksia ei löytynyt.");
                        break;
                    case 1:
                        io.print("Löydettiin lukuvinkki!");
                        this.library.deleteLukuvinkki(results.get(0));
                        io.print("Lukuvinkki poistettu!");
                        break;
                    default:
                        io.print("Monta tulosta:");
                        results.stream().map(l -> l.getOtsikko()).forEach(t -> io.print(t));
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
        while(true) {
            io.print("Syötä tageja tai poistu antamalla tyhjä merkkijono; ");
            String input = io.nextLine();
            if(input.isEmpty()) break;
            tagit.add(input);
        }
        List<Lukuvinkki> vinkit = this.library.searchByTags(tagit);
        io.print("Löydetyt vinkit tageilla: " + tagit);
        for(Lukuvinkki vinkki : vinkit) {
            io.print(vinkki.getOtsikko());
        }
    }

    private void searchItems() throws SQLException {
        while (true) {
            io.print("Anna hakusana tai poistu antamalla tyhjä merkkijono: ");
            String title = io.nextLine();
            if (title.equals("")) {
                break;
            } else {
                List<Lukuvinkki> results = this.library.searchByTitle(title, false);
                switch (results.size()) {
                    case 0:
                        io.print("Tuloksia ei löytynyt.");
                        break;
                    case 1:
                        io.print("Löydettiin lukuvinkki!");
                        io.print(results.get(0).getOtsikko());
                        readingTipMenu(results.get(0));
                        break;
                    default:
                        io.print("Monta tulosta:");
                        results.stream().map(l -> l.getOtsikko()).forEach(t -> io.print(t));
                        io.print("Tarkenna hakua!");
                }
            }
            break;
        }
    }

    private void readingTipMenu(final Lukuvinkki lukuvinkki) throws SQLException {
        // Tässä voisi sitten kysellä poistetaanko/muokataanko vinkkiä.
        while (true) {
            io.print("Haluatko muokata tageja, linkkiä vai otsikkoa? Valitse t/l/o, tyhjä rivi poistuu");
            io.print("Otsikko: " + lukuvinkki.getOtsikko());
            io.print("Tagit: " + lukuvinkki.getTagit());
            io.print("Linkki: " + lukuvinkki.getLinkki());
            String command = io.nextLine();
            switch (command) {
                case "":
                    break;
                case "t":
                    taginMuokkaus(lukuvinkki);
                case "l":
                 editLink(lukuvinkki);
            }
            break;
        }
    }

    private void editLink(Lukuvinkki lukuvinkki) throws SQLException {
        while(true) {
            io.print("Anna linkki"); 
            String url = io.nextLine();
            if(Validi.tarkistaURL(url)) {
                lukuvinkki.lisaaLinkki(url);
                this.library.addLinkki(lukuvinkki);
                io.print("Linkin tallentaminen onnistui!");
                break;
            } else {
                io.print("Virheellinen URL. Anna uusi linkki!");
            }
        }
    }

    public void taginMuokkaus(Lukuvinkki lukuvinkki) throws SQLException {
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
                            this.library.deleteTagi(tagiId);
                            lukuvinkki.poistaTagi(input);
                            io.print("Tagi poistettu!");
                    } else {
                        io.print("Tagia " + input + " ei löytynyt");
                    }  
                break;
            case "l":
                while (true) {
                    io.print("Anna uusi tagi: ");
                    input = io.nextLine();
                    if (Validi.tarkistaTag(input)) {
                        lukuvinkki.lisaaTagi(input);
                        this.library.addTagi(lukuvinkki, input);
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
                        if (Validi.tarkistaTag(newTag)) {
                            int oldTag = this.library.findTagId(lukuvinkki, input);
                            lukuvinkki.poistaTagi(input);
                            lukuvinkki.lisaaTagi(newTag);
                            this.library.deleteTagi(oldTag);
                            this.library.addTagi(lukuvinkki, newTag);
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

