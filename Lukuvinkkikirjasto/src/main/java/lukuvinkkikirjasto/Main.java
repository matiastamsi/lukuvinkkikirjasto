package lukuvinkkikirjasto;

import java.sql.SQLException;

import lukuvinkkikirjasto.dao.ReadingTipDAO;
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
     * @throws SQLException
     */
    public static void main(final String[] args) throws SQLException {
        ConnectionToDatabase connection
                = new ConnectionToDatabase("jdbc:sqlite:tietokanta.db");
        ReadingTipDAO lukuvinkkiDao = new ReadingTipDAO(connection);
        UserInterface ui = new UserInterface(new ConsoleIO(), lukuvinkkiDao);
        ui.run();
    }
}
