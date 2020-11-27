package lukuvinkkikirjasto.userinterface;

import java.nio.file.Paths;
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

    public void run() {
        io.print("Alla on lueteltu ohjelman toiminnot ja niitä vastaavat "
                + "näppäimet. Valitse toiminto painamalla sitä vastaavaa "
                + "näppäintä.");
        io.print("u: Lisää uusi lukuvinkki.");
        io.print("l: Listaa lisäämiesi lukuvinkkien otsikot.");
        io.print("e: Etsi lukuvinkkiä otsikon perusteella.");
        io.print("x: Poista lukuvinkki otsikon perusteella.");
        io.print("m: Muokkaa lukuvinkkiä.");
        io.print("luot: Luo tietokanta. Toiminto luo "
                + "tietokannan, ellei sitä ole jo luotu.");
        io.print("alustat: Alusta tietokanta. Toiminto poistaa "
                + "vanhan tietokannan ja luo uuden tietokannan.");
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
                    io.print("Tietokanta alustettu. Uuden saat syöttämällä 'luot'");
                    break;
                default:
                    io.print("Virheellinen näppäinvalinta. Yritä uudestaan.");
            }
        }
    }
    /**
     * Get all 'lukuvinkki' from database and print those like:
     * "title"
     * "URL"
     */
    private void listItems() {
        List<Lukuvinkki> vinkit = this.library.getAll();
        if (vinkit == null) {
            io.print("Lukuvinkkien hakeminen epäonnistui "
                    + "tai et ole vielä lisännyt yhtään lukuvinkkiä.");
        } else {
            this.library.getAll().stream()
                    .map(l -> l.getOtsikko() + "\n" + l.getLinkki())
                    .forEach(t -> io.print(t));
        }
    }

    private void addToLibrary() {
        while (true) {
            io.print("Anna lukuvinkin otsikko: ");
            String title = io.nextLine();
            //Check wether there already exists lukuvinkki with same title.
            List<Lukuvinkki> exists = this.library.searchByTitle(title, true);
            if (!exists.isEmpty()) { //Already exists.
                io.print("Löytyy jo lukuvinkki kyseisellä otsikolla.");
                continue;
            }
            if (title.equals("")) {
                io.print("Otsikossa täytyy olla vähintään yksi kirjain.");
            } else {
                Lukuvinkki newItem = new Lukuvinkki(
                        this.library.getLukuvinkkienMaara() + 1, title);

                io.print("Haluatko lisätä lukuvinkille tagin? Valitse k/e");
                String valinta = io.nextLine();
                if (valinta.equals("k")) {
                    io.print("Anna tagi: ");
                    String tag = io.nextLine();
                    newItem.lisaaTagi(tag);
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

    private void deleteItem() {
        while (true) {
            io.print("Hae poistettavaa lukuvinkkiä otsikolla. "
                    + "\nTyhjä merkkijono lopettaa.");
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
                        io.print(results.get(0).getOtsikko());
                        readingTipMenu(results.get(0));
                        boolean success = this.library.delete(input);
                        if (success) {
                            io.print("Poistettu!");
                            break;
                        } else {
                            System.out.println("Yritä uudestaan.");
                        }
                        break;
                    default:
                        io.print("Monta tulosta:");
                        results.stream()
                                .map(l -> l.getOtsikko())
                                .forEach(t -> io.print(t));
                        io.print("Tarkenna hakua!");
                }
            }
        }
    }

    private void editItem() {
        while (true) {
            io.print("Anna muokattavan lukuvinkin otsikko."
                    + "\nVoit etsiä otsikkoa syöttämällä 'etsi'.");
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

    private void searchItems() {
        while (true) {
            io.print("Anna hakusana tai poistu antamalla tyhjä merkkijono: ");
            System.out.println("jeejee");
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
                        results.stream()
                                .map(l -> l.getOtsikko())
                                .forEach(t -> io.print(t));
                        io.print("Tarkenna hakua!");
                }
            }
        }
    }

    private void readingTipMenu(final Lukuvinkki lukuvinkki) {
        // Tässä voisi sitten kysellä poistetaanko/muokataanko vinkkiä.
    }
}
