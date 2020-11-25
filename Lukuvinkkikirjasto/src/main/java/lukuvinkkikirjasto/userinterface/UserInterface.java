package lukuvinkkikirjasto.userinterface;

import java.util.List;

import lukuvinkkikirjasto.Lukuvinkki;
import lukuvinkkikirjasto.dao.DAO;

public class UserInterface {
    private InputOutput io;
    private DAO library;


    public UserInterface(final InputOutput io, final DAO dao) {
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
        io.print("luot: Luo tietokanta. Toiminto luo "
        + "tietokannan, ellei sitä ole jo luotu.");
        io.print("alustat: Alusta tietokanta. Toiminto poistaa "
        + "vanhan tietokannan ja luo uuden tietokannan.");
        io.print("p: Poistu ohjelmasta.");
        
        Boolean continues = true;
        
        while (continues) {
            io.print("Valitse toiminto (u, l, e, luot, alustat tai p): ");
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
                case "luot":
                    this.library.createDatabase();
                    break;
                case "alustat":
                    this.library.initializeDatabase();
                    break;
                default:
                    io.print("Virheellinen näppäinvalinta. Yritä uudestaan.");
            }
        }
    }

    private void listItems() {
        List<Lukuvinkki> vinkit = this.library.getAll();
        if (vinkit == null) {
            io.print("Lukuvinkkien hakeminen epäonnistui "
            + "tai et ole vielä lisännyt yhtään lukuvinkkiä.");
        } else {
            vinkit.stream()
            .map(l -> l.getOtsikko())
            .forEach(t -> io.print(t)); 
        }
    }

    private void addToLibrary() {
        io.print("Anna lukuvinkin otsikko: ");
        String title = io.nextLine();
        if (title.equals("")) {
            io.print("Otsikossa täytyy olla vähintään yksi kirjain.");
        } else {
            Lukuvinkki newItem = new Lukuvinkki(title);
            
            io.print("Haluatko lisätä lukuvinkille tagin? Valitse k/e");
            String valinta = io.nextLine();
            if (valinta.equals("k")) {
                io.print("Anna tagi: ");
                String tag = io.nextLine();
                newItem.lisaaTagi(tag);
            }
            this.library.add(newItem);
            io.print("Lukuvinkin lisääminen onnistui!");
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
                List<Lukuvinkki> results = this.library.searchByTitle(title);
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

    private void readingTipMenu(final Lukuvinkki vinkki) {
        //toteutus kesken
    }
}
