
package lukuvinkkikirjasto;

import java.util.ArrayList;

public class Lukuvinkki {
    private final String otsikko;
    private final ArrayList<String> tagit;
    
    public Lukuvinkki(final String otsikko) {
       this.otsikko = otsikko;
       this.tagit = new ArrayList<>();
    }

    public void lisaaTagi(final String tag) {
        this.tagit.add(tag);
    }

    public ArrayList<String> getTagit() {
        return this.tagit;
    }
    
    public String getOtsikko() {
        return this.otsikko;
    }
}
