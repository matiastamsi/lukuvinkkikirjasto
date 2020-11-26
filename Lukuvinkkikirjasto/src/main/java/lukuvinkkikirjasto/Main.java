package lukuvinkkikirjasto;

import lukuvinkkikirjasto.dao.LukuvinkkiDAO;
import lukuvinkkikirjasto.databaseconnection.ConnectionToDatabase;
import lukuvinkkikirjasto.userinterface.ConsoleIO;
import lukuvinkkikirjasto.userinterface.UserInterface;

/**
 * The Main class of the application that is a mini-project on the
 * 'Ohjelmistotuotanto'-course.
 *
 * @author Lukuvinkkikirjasto-group
 */
final class Main {

    private Main() {
        throw new AssertionError("Error");
    }

    /**
     * The main method that launches the user interface.
     *
     * @param args
     */
    public static void main(final String[] args) {
        ConnectionToDatabase connection
                = new ConnectionToDatabase("jdbc:sqlite:tietokanta.db");
        LukuvinkkiDAO lukuvinkkiDao = new LukuvinkkiDAO(connection);
        UserInterface ui = new UserInterface(new ConsoleIO(), lukuvinkkiDao);
        ui.run();
    }
}
