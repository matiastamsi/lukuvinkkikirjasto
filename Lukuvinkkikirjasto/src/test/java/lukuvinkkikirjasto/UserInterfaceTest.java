package lukuvinkkikirjasto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lukuvinkkikirjasto.dao.LukuvinkkiDAO;
import lukuvinkkikirjasto.databaseconnection.ConnectionToDatabase;
import lukuvinkkikirjasto.userinterface.ConsoleIO;
import lukuvinkkikirjasto.userinterface.StubIO;
import lukuvinkkikirjasto.userinterface.UserInterface;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author henri
 */
public class UserInterfaceTest {

    UserInterface ui;
    ArrayList<String> syotteet = new ArrayList<>();
    StubIO io;
    ConnectionToDatabase connection;
    LukuvinkkiDAO dao;

    @Before
    public void setUp() throws SQLException, IOException {
        Files.deleteIfExists(Paths.get("tietokanta.db"));
        connection = new ConnectionToDatabase("jdbc:sqlite:tietokanta.db");
        dao = new LukuvinkkiDAO(connection);
        dao.createDatabase();
    }


    @Test
    public void oneTitleCanBeAdded() throws SQLException {
        ArrayList<String> str = new ArrayList<>(Arrays.asList("u", "testTitle", "e", "e", "l", "p"));
        io = new StubIO(str);
        ui = new UserInterface(io, dao);
        ui.run();
        ArrayList<String> vastaukset = io.getOutputs();
        assertTrue(vastaukset.contains("Lukuvinkin lisääminen onnistui!"));
    }

    @Test
    public void multipleTitlesCanBeAdded() throws SQLException {
        ArrayList<String> str = new ArrayList<>(Arrays.asList("u", "t1", "e", "e", "u", "t2", "e", "e", "u", "t3", "e", "e", "l", "p"));
        io = new StubIO(str);
        ui = new UserInterface(io, dao);
        ui.run();
        ArrayList<String> vastaukset = io.getOutputs();
        int i = 0;
        for (String s : vastaukset) {
            if (s.equals("Lukuvinkin lisääminen onnistui!")){
                i++;
            }
        }
        assertEquals(i, 3);
    }

    @Test
    public void tagsCanBeAdded() throws SQLException {
        ArrayList<String> str = new ArrayList<>(Arrays.asList("u", "tagTest", "k", "tag", "tagx", "", "e", "l", "p"));
        io = new StubIO(str);
        ui = new UserInterface(io, dao);
        ui.run();
        ArrayList<String> vastaukset = io.getOutputs();
        assertTrue(vastaukset.contains("Lukuvinkin lisääminen onnistui!"));
    }

    @Test
    public void deletionTest() throws SQLException {
        ArrayList<String> str = new ArrayList<>(Arrays.asList("u", "delete", "e", "e", "x", "delete", "", "p"));
        io = new StubIO(str);
        ui = new UserInterface(io, dao);
        ui.run();
        ArrayList<String> vastaukset = io.getOutputs();
        assertTrue(vastaukset.contains("Lukuvinkki poistettu!"));
    }

    @Test
    public void searchFindsATitle() throws SQLException {
        ArrayList<String> str = new ArrayList<>(Arrays.asList("u", "TestTitle", "e", "e", "e", "Test", "", "p"));
        io = new StubIO(str);
        ui = new UserInterface(io, dao);
        ui.run();
        ArrayList<String> prints = io.getOutputs();
        assertTrue(prints.contains("Löydettiin lukuvinkki!"));
    }
}
