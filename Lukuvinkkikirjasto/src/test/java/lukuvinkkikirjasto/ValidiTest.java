package lukuvinkkikirjasto;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *Test class called 'Validi'.
 * 
 * @author Lukuvinkki-group
 */
public class ValidiTest {

    @Test
    public void canCreateValidi() {
        Validi v = new Validi();
    }

    @Test
    public void checkURLWorksWithRightURL() {
        assertEquals(true, Validi.checkURL("https://google.com"));
    }

    @Test
    public void checkURLWorksWithWrongURL() {
        assertEquals(false, Validi.checkURL("htps:/google.com"));
    }

    @Test
    public void checkTagWorksWithRightTag() {
        assertEquals(true, Validi.checkTag("tag"));
    }

    @Test
    public void checkTagWorksWithWrongTag() {
        assertEquals(false, Validi.checkTag(""));
    }

    @Test
    public void checkDateWorksWithRightDate() {
        assertEquals(true, Validi.checkDate("0001-01-01"));
    }

    @Test
    public void checkDateWorksWithWrongDate() {
        assertEquals(false, Validi.checkDate("006-66"));
    }

    @Test
    public void getUrlTitleWorksWithPageThatHasTitle() {
        assertEquals("Google", Validi.getURLTitle("https://google.fi"));
    }

    @Test
    public void getUrlTitleWorksCorrectlyWhenItWontFindAPage() {
        assertEquals("", Validi.getURLTitle("htdasjfiofusafid"));
    }
}
