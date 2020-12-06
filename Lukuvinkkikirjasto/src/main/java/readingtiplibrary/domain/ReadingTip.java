package readingtiplibrary.domain;

import java.util.ArrayList;

public class ReadingTip {

    private Integer id;
    private final String title;
    private ArrayList<String> tags;
    private String link;
    private String read;



    public ReadingTip(final Integer id, final String title) {
        this.id = id;
        this.title = title;
        this.tags = new ArrayList<>();
        this.link = "Ei lisättyä linkkiä";

    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public void addTag(final String tag) {
        this.tags.add(tag);
    }

    public void deleteTag(final String tag) {
        this.tags.remove(tag);
    }

    public ArrayList<String> getTags() {
        return this.tags;
    }

    public void setLink(final String link) {
        this.link = link;
    }
    
    public void deleteLink() {
        this.link = "Ei lisättyä linkkiä";
    }

    public String getLink() {
        return this.link;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTags(final ArrayList<String> readingTipsTags) {
        this.tags = readingTipsTags;
    }

    public int getId() {
        return this.id;
    }
    
    public String toString() {
        String tip = title + "\n" + this.link + "\n";
        
        if (read == null) {
            tip += "Ei luettu\n";
        } else {
            tip += "Luettu " + read + "\n";
        }
        
        if (tags.isEmpty()) {
            tip += "Ei tageja";
        } else {
            tip += "Tagit: ";
            for (String t: tags) {
                tip +=  t + " ";
            }
        }
        return tip;
    }
}
