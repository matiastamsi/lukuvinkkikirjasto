
package lukuvinkkikirjasto;


import java.net.URL;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


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
    
    public static String getURLTitle(String URL) {
        Document document;
        String title = "";
        try {
            document = Jsoup.connect(URL).get();
            title = document.title(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
        return title;
    }
}
