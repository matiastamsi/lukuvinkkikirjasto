package lukuvinkkikirjasto;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lukuvinkkikirjasto.dao.LukuvinkkiDAO;
import lukuvinkkikirjasto.databaseconnection.ConnectionToDatabase;
import lukuvinkkikirjasto.userinterface.ConsoleIO;
import lukuvinkkikirjasto.userinterface.StubIO;
import lukuvinkkikirjasto.userinterface.UserInterface;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author henri
 */
public class UserInterfaceTest {
    
    UserInterface ui;
    ArrayList<String> syotteet;
    StubIO io;
    ConnectionToDatabase connection;
    LukuvinkkiDAO dao;

    @Before
    public void setUp() {
        dao.initializeDatabase(Paths.get("tietokantaTest.db"));
        connection = new ConnectionToDatabase("jdbc:sqlite:tietokantaTest.db");
        dao = new LukuvinkkiDAO(connection);
        dao.createDatabase();
    }

    @Test
    public void oneTitleCanBeAdded() {
        ArrayList<String> str = new ArrayList<>(Arrays.asList("u", "testTitle", "e", "l", "p"));
        io = new StubIO(str);
        ui = new UserInterface(io, dao);
        ui.run();
        ArrayList<String> prints = io.getOutputs();
        assertTrue(prints.get(prints.size() - 2).equals("testTitle"));
    }

    @Test
    public void multipleTitlesCanBeAdded() {
        ArrayList<String> str = new ArrayList<>(Arrays.asList("u", "t1", "e", "u", "t2", "e", "u", "t3", "e", "l", "p"));
        io = new StubIO(str);
        ui = new UserInterface(io, dao);
        ui.run();
        ArrayList<String> prints = io.getOutputs();
        assertTrue(prints.get(prints.size() - 4).equals("t1"));
        assertTrue(prints.get(prints.size() - 3).equals("t2"));
        assertTrue(prints.get(prints.size() - 2).equals("t3"));
    }

    @Test
    public void searchFindsATitle() {
        ArrayList<String> str = new ArrayList<>(Arrays.asList("u", "TestTitle", "e", "e", "Test", "", "p"));
        io = new StubIO(str);
        ui = new UserInterface(io, dao);
        ui.run();
        ArrayList<String> prints = io.getOutputs();
        assertTrue(prints.contains("Löydettiin lukuvinkki!"));
    }
}
