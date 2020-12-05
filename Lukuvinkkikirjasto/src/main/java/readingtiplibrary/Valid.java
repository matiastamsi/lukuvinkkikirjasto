
package readingtiplibrary;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class Valid {
    
    public static boolean checkURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public static boolean checkTag(String tag) {
        return tag.matches("^[^\\d\\s]+$");
    }
    
    public static String getURLTitle(String URL) {
        Document document;
        String title;
        try {
            document = Jsoup.connect(URL).get();
            title = document.title(); 
        } catch (Exception e) {
            e.printStackTrace();
            title = "";
        }
        return title;
    }

	public static boolean checkDate(String date) {
        try {
            LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;

	}
}
