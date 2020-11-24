
package lukuvinkkikirjasto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lukuvinkkikirjasto.userinterface.StubIO;
import lukuvinkkikirjasto.userinterface.UserInterface;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author henri
 */
public class UserInterfaceTest {
    
    @Test
    public void oneTitleCanBeAdded() {
        ArrayList<String> str = new ArrayList<>(Arrays.asList("u", "testTitle", "e", "l", "p"));
        StubIO io = new StubIO(str);
        
        UserInterface ui = new UserInterface(io);
        ui.run();
        
        ArrayList<String> prints = io.getOutputs();
        assertTrue(prints.get(prints.size() - 2).equals("testTitle"));
    }
    
    @Test
    public void multipleTitlesCanBeAdded() {
        ArrayList<String> str = new ArrayList<>(Arrays.asList("u", "t1", "e", "u", "t2", "e", "u", "t3", "e", "l", "p"));
        StubIO io = new StubIO(str);
        
        UserInterface ui = new UserInterface(io);
        ui.run();
        
        ArrayList<String> prints = io.getOutputs();
        
        assertTrue(prints.get(prints.size() - 4).equals("t1"));
        assertTrue(prints.get(prints.size() - 3).equals("t2"));
        assertTrue(prints.get(prints.size() - 2).equals("t3"));
    }
    
    @Test
    public void searchFindsATitle() {
        ArrayList<String> str = new ArrayList<>(Arrays.asList("u", "TestTitle", "e", "e", "Test", "", "p"));
        StubIO io = new StubIO(str);
        
        UserInterface ui = new UserInterface(io);
        ui.run();
        
        ArrayList<String> prints = io.getOutputs();
        
        assertTrue(prints.contains("Löydettiin lukuvinkki!"));
    }
}
