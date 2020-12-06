package readingtiplibrary.domain;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the ReadingTip class.
 */
public class ReadingTipTest {

    ReadingTip rt;

    @Test
    public void creatingNewReadingTipSucceed() {
        rt = new ReadingTip(1, "Title");
    }

    @Test
    public void readingTipCanBeInitializedWithDate() {
        rt = new ReadingTip(123, "Toimii");
        rt.setRead("1234-01-01");
        String date = rt.getRead();
        assertTrue("1234-01-01".equals(date));
    }

    @Test
    public void readingTipCanBeRepresentedAsString() {
        rt = new ReadingTip(3, "ABC");
        assertEquals("ABC\nEi lisättyä linkkiä\nEi luettu\nEi tageja", rt.toString());
    }

    @Test
    public void addingAndDeletingTagWorks() {
        rt = new ReadingTip(6, "Tagien lisäys on helppoa");
        rt.addTag("tag");
        assertTrue(rt.getTags().contains("tag"));
        rt.deleteTag("tag");
        assertFalse(rt.getTags().contains("tag"));
    }

    @Test
    public void addingAndDeletingLinkWorks() {
        rt = new ReadingTip(6, "Linkin lisäys on helppoa");
        rt.setLink("https://bing.com");
        assertEquals("https://bing.com", rt.getLink());
        rt.deleteLink();
        assertEquals("Ei lisättyä linkkiä", rt.getLink());
    }
}
