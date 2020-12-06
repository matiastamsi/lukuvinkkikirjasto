package readingtiplibrary.userinterface;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import readingtiplibrary.dao.ReadingTipDAO;
import readingtiplibrary.databaseconnection.ConnectionToDatabase;
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
        ArrayList<String> str = new ArrayList<>(Arrays.asList("u", "o", "delete", "e", "e", "e", "o", "delete", "poista", "", "p"));
        io = new StubIO(str);
        ui = new UserInterface(io, dao);
        ui.run();
        ArrayList<String> vastaukset = io.getOutputs();
        assertTrue(vastaukset.contains("Lukuvinkki poistettu!"));
    }

    @Test
    public void searchFindsATitle() throws SQLException {
        ArrayList<String> str = new ArrayList<>(Arrays.asList("u", "o", "TestTitle", "e", "e", "e", "o", "Test", "p", "", "p"));
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
                                                "e", "o", "SQLite Tutorial", "luettu", "a", "2020-12-02", "p", "", "p"));
        io = new StubIO(str);
        ui = new UserInterface(io, dao);
        ui.run();
        ArrayList<String> vastaukset = io.getOutputs();
        assertTrue(vastaukset.contains("Lukuvinkin lukupäiväksi tallennettiin: 2020-12-02"));
    }

    @Test
    public void editLink() throws SQLException {
        ArrayList<String> str = new ArrayList<>(Arrays.asList("u", "o", "editLinkTest", "e", "k",
                                                "https://github.com", "e", "o", "editLinkTest", "l", "https://tira.mooc.fi/syksy-2020/index", "p", "", "p"));
        io = new StubIO(str);
        ui = new UserInterface(io, dao);
        ui.run();
        ArrayList<String> vastaukset = io.getOutputs();
        assertTrue(vastaukset.contains("Linkki päivitetty.\n"));
    }

    @Test
    public void removeTagTest() throws SQLException {
        ArrayList<String> str = new ArrayList<>(Arrays.asList("u", "o", "removeTagTest", "k", "tag", "secondTag", "", "e", "e", "o", "removeTagTest", "t", "p", "secondTag","", "p", "", "p"));
        io = new StubIO(str);
        ui = new UserInterface(io, dao);
        ui.run();
        ArrayList<String> vastaukset = io.getOutputs();
        assertTrue(vastaukset.contains("Tagi poistettu!"));
    }
    
    
    @After
    public void tearDown() {
        dao.close();
        File db = new File("test.db");
        db.delete();
    }
}
