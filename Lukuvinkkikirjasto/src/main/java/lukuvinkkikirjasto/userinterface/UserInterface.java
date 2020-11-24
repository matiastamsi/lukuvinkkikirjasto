package lukuvinkkikirjasto.userinterface;

import java.util.List;
import lukuvinkkikirjasto.Lukuvinkki;
import lukuvinkkikirjasto.dao.EiPysyvaTallennusDAO;

public class UserInterface {
    private InputOutput io;
    private EiPysyvaTallennusDAO library;

    public UserInterface(final InputOutput io) {

        this.io = io;
        this.library = new EiPysyvaTallennusDAO();
    }

    public void run() {
        io.print("Alla on lueteltu ohjelman toiminnot ja niitä vastaavat "
                + "näppäimet. Valitse toiminto painamalla sitä vastaavaa "
                + "näppäintä.");
        io.print("u: Lisää uusi lukuvinkki.");
        io.print("l: Listaa lisäämiesi lukuvinkkien otsikot.");
        io.print("e: Etsi lukuvinkkiä.");
        io.print("p: Poistu ohjelmasta.");
        
        Boolean continues = true;
        
        while (continues) {
            io.print("Valitse toiminto (u, l, e tai p): ");
            String command = io.nextLine();
            switch (command) {
                case "p": 
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
                default:
                    io.print("Virheellinen näppäinvalinta. Yritä uudestaan.");
            }
        }
    }

    private void listItems() {
        this.library.getAll().stream()
            .map(l -> l.getOtsikko())
            .forEach(t -> io.print(t));
    }

    private void addToLibrary() {
        io.print("Anna lukuvinkin otsikko: ");
        String title = io.nextLine();
        if (title.equals("")) {
            io.print("Otsikossa täytyy olla vähintään yksi kirjain.");
        } else {
            Lukuvinkki newItem = new Lukuvinkki(title);
            this.library.add(newItem);
            io.print("Lukuvinkin lisääminen onnistui!");
            io.print("Haluatko lisätä lukuvinkille tagin? Valitse k/e");
            String valinta = io.nextLine();
            if (valinta.equals("k")) {
                io.print("Anna tagi: ");
                String tag = io.nextLine();
                newItem.lisaaTagi(tag);
            }
        }
    }
    
    private void searchItems() {
        while(true) {
            io.print("Anna hakusana tai poistu antamalla tyhjä merkkijono: ");
            System.out.println("jeejee");
            String title = io.nextLine();
            if (title.equals("")) {
                break;
            } else {
                List<Lukuvinkki> results = this.library.searchByTitle(title);
                switch(results.size()) {
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
    
    private void readingTipMenu(Lukuvinkki lukuvinkki) {
        // Tässä voisi sitten kysellä poistetaanko/muokataanko vinkkiä.
    }
    
}
