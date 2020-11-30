
package lukuvinkkikirjasto;
import java.net.URL;
public class Validi {
    
    public static boolean tarkistaURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public static boolean tarkistaTag(String tag) {
        return tag.matches("^[^\\d\\s]+$");
    }
    
}
