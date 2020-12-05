package readingtiplibrary.userinterface;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * The class that tests the ConsoleIO class.
 */
public class ConsoleIOTest {

    ConsoleIO cio;

    @Test
    public void creatingConsoleIOWorks() {
        boolean works = false;
        try {
            cio = new ConsoleIO();
            works = true;
        } catch (Exception e) {
        }
        assertTrue(works);
    }

    @Test
    public void nextLineReturnsWhatIsReadFromConsole() {
        cio = new ConsoleIO();
        cio.setReader(new Scanner("toimii"));
        assertEquals("toimii", cio.nextLine());
    }

    @Test
    public void printingOutWorks() {
        cio = new ConsoleIO();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        cio.print("huhhuh");
        String output = new String(baos.toByteArray());
        assertTrue(output.contains("huhhuh"));
    }
}
