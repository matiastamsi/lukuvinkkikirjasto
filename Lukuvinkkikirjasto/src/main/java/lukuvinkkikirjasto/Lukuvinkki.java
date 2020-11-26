
package lukuvinkkikirjasto;

import java.util.ArrayList;

public class Lukuvinkki {
    private final String otsikko;
    private final ArrayList<String> tagit;
    private String linkki; 
    
    public Lukuvinkki(final String otsikko) {
       this.otsikko = otsikko;
       this.tagit = new ArrayList<>();
       this.linkki = "Ei lisättyä linkkiä";
    }

    public void lisaaTagi(final String tag) {
        this.tagit.add(tag);
    }

    public ArrayList<String> getTagit() {
        return this.tagit;
    }
    
    public void lisaaLinkki(final String link) {
        this.linkki = link;
    }
    
    public String getLinkki() {
        return this.linkki;
    }
    
    public String getOtsikko() {
        return this.otsikko;
    }
}
