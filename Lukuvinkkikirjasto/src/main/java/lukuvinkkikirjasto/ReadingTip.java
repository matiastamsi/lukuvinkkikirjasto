package lukuvinkkikirjasto;

import java.time.LocalDate;
import java.util.ArrayList;

public class ReadingTip {

    private Integer id;
    private final String otsikko;
    private ArrayList<String> tagit;
    private String linkki;
    private LocalDate read;

    public ReadingTip(final Integer id, final String otsikko) {
        this.id = id;
        this.otsikko = otsikko;
        this.tagit = new ArrayList<>();
        this.linkki = "Ei lisättyä linkkiä";

    }

    public LocalDate getRead() {
        return read;
    }

    public void setRead(LocalDate read) {
        this.read = read;
    }

    public void addTag(final String tag) {
        this.tagit.add(tag);
    }

    public void deleteTag(final String tag) {
        this.tagit.remove(tag);
    }

    public ArrayList<String> getTags() {
        return this.tagit;
    }

    public void addLink(final String link) {
        this.linkki = link;
    }
    
    public void deleteLink() {
        this.linkki = "Ei lisättyä linkkiä";
    }

    public String getLink() {
        return this.linkki;
    }

    public String getTitle() {
        return this.otsikko;
    }

    public void setTags(final ArrayList<String> lukuvinkinTagit) {
        this.tagit = lukuvinkinTagit;
    }

    public int getId() {
        return this.id;
    }
    
    public String toString() {
        String vinkki = otsikko + "\n" + linkki + "\n";
        if (tagit.isEmpty()) {
            vinkki += "Ei tageja";
        } else {
            for (String t: tagit) {
                vinkki += "[" + t + "] ";
            }
        }
        return vinkki;
    }
}
