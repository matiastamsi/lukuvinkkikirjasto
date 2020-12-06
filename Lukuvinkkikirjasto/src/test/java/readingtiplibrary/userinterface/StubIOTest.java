package readingtiplibrary.userinterface;

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * The class that tests StubIO class
 */
public class StubIOTest {

    StubIO s;

    @Test
    public void creatingStubIOWorks() {
        boolean works = false;
        try {
            ArrayList<String> inputs = new ArrayList<>();
            inputs.add("input");
            s = new StubIO(inputs);
            works = true;
        } catch (Exception e) {
        }
        assertTrue(works);
    }

    @Test
    public void nextLineWorksCorrectly() {
        ArrayList<String> inputs = new ArrayList<>();
        inputs.add("eka");
        inputs.add("toka");
        s = new StubIO(inputs);
        assertEquals("eka", s.nextLine());
        assertEquals("toka", s.nextLine());
    }
    
    @Test
    public void nextLineWorksCorrectlyWhenNoInputs() {
        ArrayList<String> inputs = new ArrayList<>();
        s = new StubIO(inputs);
        assertEquals("", s.nextLine());
    }

    @Test
    public void printingWorksCorrectly() {
        ArrayList<String> inputs = new ArrayList<>();
        s = new StubIO(inputs);
        assertTrue(s.getOutputs().isEmpty());
        s.print("printtaa");
        assertTrue(s.getOutputs().size() == 1);
        assertEquals("printtaa", s.getLatestPrint());
    }

}
