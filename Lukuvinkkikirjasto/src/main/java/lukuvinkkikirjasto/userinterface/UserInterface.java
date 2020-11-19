package lukuvinkkikirjasto.userinterface;

import java.util.ArrayList;
import java.util.List;
import lukuvinkkikirjasto.Lukuvinkki;

public class UserInterface {
    private InputOutput io;
    List<Lukuvinkki> library;

    public UserInterface(InputOutput io) {
        this.io = io;
        this.library = new ArrayList<>();
    }

    public void run() {
        io.print("Alla on lueteltu ohjelman toiminnot ja toimintoja vastaavat näppäimet. Valitse toiminto painamalla sitä vastaavaa näppäintä.");
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
        this.library.stream()
            .map(l -> l.getOtsikko())
            .forEach(t -> io.print(t));
    }

    private void addToLibrary() {
        io.print("Anna lukuvinkin otsikko: ");
        String title = io.nextLine();
        if (title.equals("")) {
            io.print("Otsikossa täytyy olla vähintään yksi kirjain. Yritä uudestaan.");
        } else {
            Lukuvinkki newItem = new Lukuvinkki(title);
            this.library.add(newItem);
            io.print("Lukuvinkin lisääminen onnistui!");
        }
    }
}