package lukuvinkkikirjasto.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lukuvinkkikirjasto.ReadingTip;
import lukuvinkkikirjasto.databaseconnection.ConnectionToDatabase;

/**
 * The interface that is responsible for data access when the data is stored
 * with SQL.
 *
 * @author Lukuvinkkikirjasto-group
 */
public class ReadingTipDAO implements DAO {

    private ConnectionToDatabase connection;

    public ReadingTipDAO(final ConnectionToDatabase connection) {
        this.connection = connection;
    }

    @Override
    public void createDatabase() {
        try {
            this.connection.setAutoCommit(false);
            this.connection.getStatement()
                    .execute("CREATE TABLE Lukuvinkit "
                            + "(id INTEGER PRIMARY KEY, otsikko TEXT UNIQUE, "
                            + "linkki TEXT, "
                            + "read TEXT)");

            this.connection.getStatement()
                    .execute("CREATE TABLE Tagit "
                            + "(id INTEGER PRIMARY KEY, nimi TEXT)");

            this.connection.getStatement()
                    .execute("CREATE TABLE LukuvinkitJaTagit "
                            + "(lukuvinkki_id INTEGER, tagi_id INTEGER)");
            connection.commit();
            connection.setAutoCommit(true);
            System.out.println("Tietokanta luotu.");
        } catch (SQLException e) {
            System.out.println("Tietokannan luominen "
                    + "epäonnistui, tai tietokanta on jo luotu.");
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            this.connection.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ReadingTip> getAll() {
        List<ReadingTip> lukuvinkit = new ArrayList<>();
        
        try {
            PreparedStatement prepared = this.connection
                    .getPreparedStatement(
                            "SELECT id, otsikko, linkki FROM Lukuvinkit");
            ResultSet result = prepared.executeQuery();

            while (result.next()) {
                ArrayList<String> lukuvinkinTagit = queryTagit(
                        result.getInt("id"));
                //System.out.println("lukuvinkin tagit" + lukuvinkinTagit);
                ReadingTip vinkki = new ReadingTip(
                        result.getInt("id"),
                        result.getString("otsikko"));                      
                vinkki.setTags(lukuvinkinTagit);
                String linkki = result.getString("linkki");
                vinkki.addLink(linkki);
                lukuvinkit.add(vinkki);
            }

            prepared.close();

        } catch (SQLException e) {
            System.out.println("Lukuvinkkien hakeminen epäonnistui.");
            e.printStackTrace();
        }
        return lukuvinkit;
    }

    @Override
    public boolean delete(final String title) {

        List<ReadingTip> lukuvinkit = searchByTitle(title, true);
        List<Integer> tagIds = new ArrayList<>();
        if (lukuvinkit.size() == 0) {
            System.out.println("Ei löytynyt lukuvinkkiä!");
            return false;
        }
        ReadingTip lukuvinkki = lukuvinkit.get(0);
        try {
            deleteLukuvinkki(lukuvinkki);
        } catch (SQLException e) {
            System.out.println("Lukuvinkin poisto/muokkaus epäonnistui");
            e.printStackTrace();
            return false;
        }
        try {
            tagIds = findAllTagIds(lukuvinkki);
            deleteLukuvinkkiJaTagi(lukuvinkki);
        } catch (SQLException e) {
            System.out.println("Lukuvinkin ja Tagin poisto/muokkaus epäonnistui");
            e.printStackTrace();
            return false;
        }
        try {
            deleteKaikkiTagit(tagIds);
        } catch (SQLException e) {
            System.out.println("Tagien poisto/muokkaus epäonnistui");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private ArrayList<Integer> findAllTagIds(ReadingTip lukuvinkki) throws SQLException {
        ArrayList<Integer> tags = new ArrayList<>();
        PreparedStatement prepared = this.connection
                .getPreparedStatement("SELECT tagi_id FROM LukuvinkitJaTagit WHERE lukuvinkki_id= ?");
        prepared.setInt(1, lukuvinkki.getId());
        ResultSet results = prepared.executeQuery();
        while (results.next()) {
            tags.add(results.getInt("tagi_id"));
        }
        prepared.close();
        return tags;
    }

    public boolean findValidTag(ReadingTip lukuvinkki, String taginNimi) throws SQLException {
        PreparedStatement prepared = this.connection
                    .getPreparedStatement("SELECT count(*) FROM LukuvinkitJaTagit, Tagit WHERE LukuvinkitjaTagit.lukuvinkki_id = ? AND Tagit.nimi = ?");
        prepared.setInt(1, lukuvinkki.getId());
        prepared.setString(2, taginNimi);
        ResultSet results = prepared.executeQuery();
        int tagId = results.getInt("count(*)");
        prepared.close();  
        return (tagId > 0);
    }

    public Integer findTagId(ReadingTip lukuvinkki, String taginNimi) throws SQLException {
        PreparedStatement prepared = this.connection     
                    .getPreparedStatement("SELECT tagi_id FROM LukuvinkitJaTagit, Tagit WHERE LukuvinkitJaTagit.lukuvinkki_id = ? AND Tagit.nimi= ?");
        prepared.setInt(1, lukuvinkki.getId());
        prepared.setString(2, taginNimi);
        ResultSet results = prepared.executeQuery();
        int tagId = results.getInt("tagi_id");
        prepared.close();            
        return tagId;
    }

    public void deleteLukuvinkki(ReadingTip lukuvinkki) throws SQLException {
        PreparedStatement prepared = this.connection
                .getPreparedStatement("DELETE FROM Lukuvinkit WHERE id= ?");
        prepared.setInt(1, lukuvinkki.getId());
        prepared.executeUpdate();
        prepared.close();
    }

    private void deleteLukuvinkkiJaTagi(ReadingTip lukuvinkki) throws SQLException {
        PreparedStatement prepared = this.connection
                .getPreparedStatement("DELETE FROM LukuvinkitJaTagit WHERE lukuvinkki_id= ?");
        prepared.setInt(1, lukuvinkki.getId());
        prepared.executeUpdate();
        prepared.close();
    }

    private void deleteKaikkiTagit(List<Integer> tagit) throws SQLException {
        for (Integer tagi : tagit) {
            PreparedStatement prepared = this.connection
                    .getPreparedStatement("DELETE FROM Tagit WHERE id= ?");
            prepared.setInt(1, tagi);
            prepared.executeUpdate();
            prepared.close();
        }
    }

    public void deleteTagi(Integer tagiId) throws SQLException {
        PreparedStatement prepared = this.connection
                    .getPreparedStatement("DELETE FROM Tagit WHERE id= ?");
        prepared.setInt(1, tagiId);
        prepared.executeUpdate();
        prepared.close();
    }

    @Override
    public boolean edit(final String title) {
        //Ei vielä toteutettu. Huom. tällä hetkellä muokkaaminen tapahtuu
        //poistamalla ja lisäämällä tilalle uusi.
        return true;
    }

    @Override
    public void add(final ReadingTip lukuvinkki) {
        addLinkki(lukuvinkki, lukuvinkki.getLink());
        try {
            PreparedStatement prepared = this.connection
                    .getPreparedStatement("INSERT INTO Lukuvinkit "
                            + "(otsikko, linkki) VALUES (?, ?)");
            prepared.setString(1, lukuvinkki.getTitle());
            prepared.setString(2, lukuvinkki.getLink());
            prepared.executeUpdate();

            if (lukuvinkki.getTags() != null) {

                Integer lukuvinkkiId = queryId(lukuvinkki.getTitle(),
                        "SELECT id FROM Lukuvinkit WHERE otsikko = ?");
                //System.out.println("lukuvinkin id: " + lukuvinkkiId);

                for (int i = 0; i < lukuvinkki.getTags().size(); i++) {
                    String tagiNimi = lukuvinkki.getTags().get(i);
                    Integer tagiId = queryId(tagiNimi,
                            "SELECT id FROM Tagit WHERE nimi = ?");
                    //System.out.println("tagin id: " + tagi_id);
                    //jos tagia ei ole taulussa eli tagi_id on -1, 
                    //lisätään se Tägit-tauluun  
                    //ja haetaan sen id:
                    if (tagiId == -1) {
                        prepared = this.connection
                                .getPreparedStatement("INSERT INTO Tagit "
                                        + "(nimi) VALUES (?)");
                        prepared.setString(1, tagiNimi);
                        prepared.executeUpdate();
                        prepared.close();
                        tagiId = queryId(tagiNimi,
                                "SELECT id FROM Tagit WHERE nimi = ?");
                        //System.out.println("tagin id: " + tagiId);
                    }
                    //lisätään yhteys liitostauluun:
                    prepared = this.connection.getPreparedStatement(
                            "INSERT INTO LukuvinkitJaTagit "
                            + "(lukuvinkki_id, tagi_id) VALUES (?, ?)");
                    prepared.setInt(1, lukuvinkkiId);
                    prepared.setInt(2, tagiId);
                    prepared.executeUpdate();
                    prepared.close();
                }
            }
            prepared.close();
        } catch (SQLException e) {
            System.out.println("Lukuvinkin lisääminen epäonnistui. "
                    + "Yritä uudestaan.");
            e.printStackTrace();
        }
    }

    public void addTagi(ReadingTip lukuvinkki, String tagi) throws SQLException {
        PreparedStatement prepared = this.connection.getPreparedStatement(
                    "INSERT INTO Tagit (nimi) values (?)");
                    prepared.setString(1, tagi);
                    prepared.executeUpdate();
                    prepared.close();
                    Integer tagiId = queryId(tagi,
                    "SELECT id FROM Tagit WHERE nimi = ?");
                    prepared = this.connection.getPreparedStatement(
                        "INSERT INTO LukuvinkitJaTagit "
                        + "(lukuvinkki_id, tagi_id) VALUES (?, ?)");
                prepared.setInt(1, lukuvinkki.getId());
                prepared.setInt(2, tagiId);
                prepared.executeUpdate();
                prepared.close();          
        
    }

    public void addLinkki(ReadingTip lukuvinkki, String linkki) {
        try {
            String vinkkiNimi = lukuvinkki.getTitle();
            Integer vinkkiId = queryId(vinkkiNimi, "SELECT id FROM "
                    + "Lukuvinkit WHERE otsikko = ?");
            PreparedStatement prepared = this.connection
                    .getPreparedStatement("UPDATE Lukuvinkit "
                            + "SET linkki = ? WHERE id = ?");
            prepared.setInt(2, vinkkiId);
            prepared.setString(1, linkki);
            prepared.executeUpdate();
            prepared.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void deleteLinkki(ReadingTip lukuvinkki) {
        try {
            String vinkkiNimi = lukuvinkki.getTitle();
            Integer vinkkiId = queryId(vinkkiNimi, "SELECT id FROM "
                    + "Lukuvinkit WHERE otsikko = ?");
            PreparedStatement prepared = this.connection
                    .getPreparedStatement("UPDATE Lukuvinkit "
                            + "SET linkki = ? WHERE id = ?");
            prepared.setString(1, "Ei lisättyä linkkiä");
            prepared.setInt(2, vinkkiId);
            prepared.executeUpdate();
            prepared.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initializeDatabase(Path path) {
        try {
            Files.deleteIfExists(path);
            this.connection = new ConnectionToDatabase("jdbc:sqlite:tietokanta.db");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ReadingTip> searchByTitle(final String title, final boolean exact) {
        List<ReadingTip> lukuvinkit = new ArrayList<>();
        try {
            PreparedStatement prepared = this.connection
                    .getPreparedStatement(
                            "SELECT id, otsikko "
                            + "FROM Lukuvinkit WHERE otsikko LIKE ?");
            if (exact) {
                prepared.setString(1, title);
            } else {
                prepared.setString(1, "%" + title + "%");
            }
            ResultSet result = prepared.executeQuery();

            while (result.next()) {
                ArrayList<String> lukuvinkinTagit = queryTagit(
                        result.getInt("id"));
                //System.out.println("lukuvinkin tagit" + lukuvinkinTagit);
                ReadingTip vinkki = new ReadingTip(
                        result.getInt("id"),
                        result.getString("otsikko"));
                vinkki.setTags(lukuvinkinTagit);
                lukuvinkit.add(vinkki);
            }

            prepared.close();

        } catch (SQLException e) {
            System.out.println("Lukuvinkkien hakeminen epäonnistui.");
            e.printStackTrace();
        }
        return lukuvinkit;
    }

    @Override 
    public List<ReadingTip> searchByTags(List<String> tagfilter) {
        // todo: optimointia?, haku = O(n^3)
        List<ReadingTip> lukuvinkit = new ArrayList<>();
        for (ReadingTip lukuvinkki : getAll()) {
            for (String vinkkitag : lukuvinkki.getTags()) {
                if (tagfilter.contains(vinkkitag)) {
                    lukuvinkit.add(lukuvinkki);
                }
            }
        }
        return lukuvinkit;
    }

    //apumetodit:
    private Integer queryId(final String value, final String sqlString) {
        try {
            PreparedStatement prepared = this.connection
                    .getPreparedStatement(sqlString);
            prepared.setString(1, value);
            ResultSet result = prepared.executeQuery();

            return result.getInt("id");

        } catch (SQLException e) {
            return -1;
        }
    }

    private ArrayList<String> queryTagit(final int lukuvinkkiId) {
        ArrayList<String> lukuvinkinTagit = new ArrayList<>();
        try {
            PreparedStatement prepared = connection.getPreparedStatement(
                    "SELECT Tagit.nimi "
                    + "FROM Tagit, Lukuvinkit, LukuvinkitJaTagit "
                    + "WHERE Tagit.id = LukuvinkitJaTagit.tagi_id "
                    + "AND Lukuvinkit.id = LukuvinkitJaTagit.lukuvinkki_id "
                    + "AND Lukuvinkit.id = ?");
            prepared.setInt(1, lukuvinkkiId);
            ResultSet result = prepared.executeQuery();
            while (result.next()) {
                lukuvinkinTagit.add(result.getString("nimi"));
            }

        } catch (SQLException e) {
            System.out.println("Tagien hakeminen epäonnistui.");
            e.printStackTrace();
        }

        return lukuvinkinTagit;
    }

    public int getLukuvinkkienMaara() {
        try {
            PreparedStatement p = connection.getPreparedStatement(
                    "SELECT COUNT(id) count FROM Lukuvinkit");
            ResultSet r = p.executeQuery();
            int i = r.getInt("count");
            p.close();
            return i;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

	public String markAsRead(ReadingTip lukuvinkki)  {
        try {
            PreparedStatement prepared = this.connection
                    .getPreparedStatement("UPDATE Lukuvinkit " 
                        + "SET read = ? WHERE id = ?");
            prepared.setString(1, lukuvinkki.getRead().toString());
            prepared.setInt(2, lukuvinkki.getId());
            prepared.executeUpdate();
            prepared.close();

            prepared = this.connection
                    .getPreparedStatement("SELECT read FROM Lukuvinkit " 
                        + "WHERE id = ?");
            prepared.setInt(1, lukuvinkki.getId());
            ResultSet result = prepared.executeQuery();

            String date = result.getString("read");
            return "Lukuvinkin lukupäiväksi tallennettiin: " + date;

        } catch (SQLException e) {
            e.printStackTrace();
            return "Päivämäärän lisääminen epäonnistui!";
        }
        
	}
}
