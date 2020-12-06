package readingtiplibrary.domain;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *Test class called 'Valid'.
 * 
 * @author Lukuvinkki-group
 */
public class ValidTest {

    @Test
    public void canCreateValidi() {
        Valid v = new Valid();
    }

    @Test
    public void checkURLWorksWithRightURL() {
        assertEquals(true, Valid.checkURL("https://google.com"));
    }

    @Test
    public void checkURLWorksWithWrongURL() {
        assertEquals(false, Valid.checkURL("htps:/google.com"));
    }

    @Test
    public void checkTagWorksWithRightTag() {
        assertEquals(true, Valid.checkTag("tag"));
    }

    @Test
    public void checkTagWorksWithWrongTag() {
        assertEquals(false, Valid.checkTag(""));
    }

    @Test
    public void checkDateWorksWithRightDate() {
        assertEquals(true, Valid.checkDate("0001-01-01"));
    }

    @Test
    public void checkDateWorksWithWrongDate() {
        assertEquals(false, Valid.checkDate("006-66"));
    }

    @Test
    public void getUrlTitleWorksWithPageThatHasTitle() {
        assertEquals("Google", Valid.getURLTitle("https://google.fi"));
    }

    @Test
    public void getUrlTitleWorksCorrectlyWhenItWontFindAPage() {
        assertEquals("", Valid.getURLTitle("htdasjfiofusafid"));
    }
}
