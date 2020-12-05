package readingtiplibrary;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import readingtiplibrary.dao.ReadingTipDAO;
import readingtipslibrary.databaseconnection.ConnectionToDatabase;
import readingtiplibrary.userinterface.StubIO;
import readingtiplibrary.userinterface.UserInterface;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.After;
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
    ReadingTipDAO dao;

    @Before
    public void setUp() throws SQLException, IOException {
        connection = new ConnectionToDatabase("jdbc:sqlite:test.db");
        dao = new ReadingTipDAO(connection);
        dao.createDatabase();
    }


    @Test
    public void oneTitleCanBeAdded() throws SQLException {
        ArrayList<String> str = new ArrayList<>(Arrays.asList("u", "o", "testTitle", "e", "e", "l", "p"));
        io = new StubIO(str);
        ui = new UserInterface(io, dao);
        ui.run();
        ArrayList<String> vastaukset = io.getOutputs();
        assertTrue(vastaukset.contains("Lukuvinkin lisääminen onnistui!"));
    }

    @Test
    public void multipleTitlesCanBeAdded() throws SQLException {
        ArrayList<String> str = new ArrayList<>(Arrays.asList("u", "o", "t1", "e", "e", "u", "o", "t2",  "e", "e", "u", "o", "t3", "e", "e", "l", "p"));
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
        ArrayList<String> str = new ArrayList<>(Arrays.asList("u", "o", "tagTest", "k", "tag", "tagx", "", "e", "l", "p"));
        io = new StubIO(str);
        ui = new UserInterface(io, dao);
        ui.run();
        ArrayList<String> vastaukset = io.getOutputs();
        assertTrue(vastaukset.contains("Lukuvinkin lisääminen onnistui!"));
    }

    @Test
    public void deletionTest() throws SQLException {
        ArrayList<String> str = new ArrayList<>(Arrays.asList("u", "o", "delete", "e", "e", "x", "delete", "", "p"));
        io = new StubIO(str);
        ui = new UserInterface(io, dao);
        ui.run();
        ArrayList<String> vastaukset = io.getOutputs();
        assertTrue(vastaukset.contains("Lukuvinkki poistettu!"));
    }

    @Test
    public void searchFindsATitle() throws SQLException {
        ArrayList<String> str = new ArrayList<>(Arrays.asList("u", "o", "TestTitle", "e", "e", "e", "Test", "", "p"));
        io = new StubIO(str);
        ui = new UserInterface(io, dao);
        ui.run();
        ArrayList<String> prints = io.getOutputs();
        assertTrue(prints.contains("Löydettiin lukuvinkki!"));
    }

    @Test
    public void addAsURL() throws SQLException {
        ArrayList<String> str = new ArrayList<>(Arrays.asList("u", "u", "https://ohjelmistotuotanto-hy.github.io/miniprojekti/", "e", "p"));
        io = new StubIO(str);
        ui = new UserInterface(io, dao);
        ui.run();
        ArrayList<String> vastaukset = io.getOutputs();
        assertTrue(vastaukset.contains("Lukuvinkin lisääminen onnistui!"));
    }

    @Test
    public void markAsReadTest() throws SQLException {
        ArrayList<String> str = new ArrayList<>(Arrays.asList("u", "o", "SQLite Tutorial", "e", "e",
                                                "luettu", "SQLite Tutorial", "luettu", "a", "2020-12-02", "p"));
        io = new StubIO(str);
        ui = new UserInterface(io, dao);
        ui.run();
        ArrayList<String> vastaukset = io.getOutputs();
        assertTrue(vastaukset.contains("Lukuvinkin lukupäiväksi tallennettiin: 2020-12-02"));
    }
    
    @After
    public void tearDown() {
        dao.close();
        File db = new File("test.db");
        db.delete();
    }
}
