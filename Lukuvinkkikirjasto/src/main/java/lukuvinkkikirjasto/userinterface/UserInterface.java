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
        io.print("Alla on lueteltu ohjelman toiminnot ja toimintoja vastaavat numeronäppäimet. Valitse toiminto painamalla sitä vastaavaa numeronäppäintä.");
        io.print("1: Lisää uusi lukuvinkki.");
        io.print("2: Listaa lisäämiesi lukuvinkkien otsikot.");
        io.print("0: Poistu ohjelmasta.");
        
        Boolean continues = true;
        
        while (continues) {
            io.print("Valitse toiminto (0 - 2): ");
            int command = Integer.valueOf(io.nextLine());
            switch (command) {
                case 0: 
                    continues = false;
                    break;
                case 1:
                    addToLibrary();
                    break;
                case 2:
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