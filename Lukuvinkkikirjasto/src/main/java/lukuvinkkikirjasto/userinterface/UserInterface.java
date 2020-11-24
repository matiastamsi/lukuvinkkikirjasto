package lukuvinkkikirjasto.userinterface;

import lukuvinkkikirjasto.Lukuvinkki;
import lukuvinkkikirjasto.dao.EiPysyvaTallennusDAO;
import lukuvinkkikirjasto.databaseconnection.ConnectionToDatabase;
import lukuvinkkikirjasto.dao.DAO;

public class UserInterface {
    private InputOutput io;
    private DAO library;


    public UserInterface(final InputOutput io, DAO dao) {
        this.io = io;
        this.library = dao;
    }

    public void run() {
        io.print("Alla on lueteltu ohjelman toiminnot ja niitä vastaavat "
                + "näppäimet. Valitse toiminto painamalla sitä vastaavaa "
                + "näppäintä.");
        io.print("u: Lisää uusi lukuvinkki.");
        io.print("l: Listaa lisäämiesi lukuvinkkien otsikot.");
        io.print("p: Poistu ohjelmasta.");
        
        Boolean continues = true;
        
        while (continues) {
            io.print("Valitse toiminto (u, l tai p): ");
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
}
