
package lukuvinkkikirjasto;

import java.util.ArrayList;

public class Lukuvinkki {
    private Integer id;
    private final String otsikko;
    private ArrayList<String> tagit;
    
    public Lukuvinkki(final String otsikko) {
       this.otsikko = otsikko;
       this.tagit = new ArrayList<>();
    }

    public Lukuvinkki(final Integer id, final String otsikko) {
        this.id = id;
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

	public void setTagit(final ArrayList<String> lukuvinkinTagit) {
        this.tagit = lukuvinkinTagit;
	}
}
