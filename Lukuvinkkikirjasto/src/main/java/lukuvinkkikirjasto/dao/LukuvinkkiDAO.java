package lukuvinkkikirjasto.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lukuvinkkikirjasto.Lukuvinkki;
import lukuvinkkikirjasto.databaseconnection.ConnectionToDatabase;

/**
 * The interface that is responsible for data access when the data is stored
 * with SQL.
 *
 * @author Lukuvinkkikirjasto-group
 */
public class LukuvinkkiDAO implements DAO {

    private ConnectionToDatabase connection;

    public LukuvinkkiDAO(final ConnectionToDatabase connection) {
        this.connection = connection;
    }

    @Override
    public void createDatabase() {
        try {
            this.connection.setAutoCommit(false);
            this.connection.getStatement()
                    .execute("CREATE TABLE Lukuvinkit "
                            + "(id INTEGER PRIMARY KEY, otsikko TEXT)");

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
    public List<Lukuvinkki> getAll() {
        List<Lukuvinkki> lukuvinkit = new ArrayList<>();
        try {
            PreparedStatement prepared = this.connection
                    .getPreparedStatement("SELECT id, otsikko FROM Lukuvinkit");
            ResultSet result = prepared.executeQuery();

            while (result.next()) {
                ArrayList<String> lukuvinkinTagit = queryTagit(result
                        .getInt("id"));
                //System.out.println("lukuvinkin tagit" + lukuvinkinTagit);
                Lukuvinkki vinkki = new Lukuvinkki(
                        result.getInt("id"),
                        result.getString("otsikko"));
                vinkki.setTagit(lukuvinkinTagit);
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

        List<Lukuvinkki> lukuvinkit = searchByTitle(title, true);
        List<Integer> tagIds = new ArrayList<>();
        if (lukuvinkit.size() == 0) {
            System.out.println("Ei löytynyt lukuvinkkiä!");
            return false;
        }
        Lukuvinkki lukuvinkki = lukuvinkit.get(0);
        try {
            deleteLukuvinkki(lukuvinkki);
        } catch (SQLException e) {
            System.out.println("Lukuvinkin poisto/muokkaus epäonnistui");
            e.printStackTrace();
            return false;
        }
        try {
            tagIds = fingTagIds(lukuvinkki);
            deleteLukuvinkkiJaTagi(lukuvinkki);
        } catch (SQLException e) {
            System.out.println("Lukuvinkin ja Tagin poisto/muokkaus epäonnistui");
            e.printStackTrace();
            return false;
        }
        try {
            deleteTagit(tagIds);
        } catch (SQLException e) {
            System.out.println("Tagien poisto/muokkaus epäonnistui");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private ArrayList<Integer> fingTagIds(Lukuvinkki lukuvinkki) throws SQLException {
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

    private void deleteLukuvinkki(Lukuvinkki lukuvinkki) throws SQLException {
        PreparedStatement prepared = this.connection
                .getPreparedStatement("DELETE FROM Lukuvinkit WHERE id= ?");
        prepared.setInt(1, lukuvinkki.getId());
        prepared.executeUpdate();
        prepared.close();
    }

    private void deleteLukuvinkkiJaTagi(Lukuvinkki lukuvinkki) throws SQLException {
        PreparedStatement prepared = this.connection
                .getPreparedStatement("DELETE FROM LukuvinkitJaTagit WHERE lukuvinkki_id= ?");
        prepared.setInt(1, lukuvinkki.getId());
        prepared.executeUpdate();
        prepared.close();
    }

    private void deleteTagit(List<Integer> tagit) throws SQLException {
        for (Integer tagi : tagit) {
            PreparedStatement prepared = this.connection
                    .getPreparedStatement("DELETE FROM Tagit WHERE id= ?");
            prepared.setInt(1, tagi);
            prepared.executeUpdate();
            prepared.close();
        }
    }
    @Override
    public boolean edit(final String title) {
        //Ei vielä toteutettu. Huom. tällä hetkellä muokkaaminen tapahtuu
        //poistamalla ja lisäämällä tilalle uusi.
        return true;
    }

    @Override
    public void add(final Lukuvinkki lukuvinkki) {
        try {
            PreparedStatement prepared = this.connection
                    .getPreparedStatement("INSERT INTO Lukuvinkit "
                            + "(otsikko) VALUES (?)");
            prepared.setString(1, lukuvinkki.getOtsikko());
            prepared.executeUpdate();
            prepared.close();

            if (lukuvinkki.getTagit() != null) {

                Integer lukuvinkkiId = queryId(lukuvinkki.getOtsikko(),
                        "SELECT id FROM Lukuvinkit WHERE otsikko = ?");
                System.out.println("lukuvinkin id: " + lukuvinkkiId);

                for (int i = 0; i < lukuvinkki.getTagit().size(); i++) {
                    String tagiNimi = lukuvinkki.getTagit().get(i);
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
                        System.out.println("tagin id: " + tagiId);
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
        } catch (SQLException e) {
            System.out.println("Lukuvinkin lisääminen epäonnistui. "
                    + "Yritä uudestaan.");
            e.printStackTrace();
        }
    }

    @Override
    public void initializeDatabase() {
        System.out.println("Metodia ei ole vielä toteutettu.");
    }

    @Override
    public List<Lukuvinkki> searchByTitle(final String title, final boolean exact) {
        List<Lukuvinkki> lukuvinkit = new ArrayList<>();
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
                Lukuvinkki vinkki = new Lukuvinkki(
                        result.getInt("id"),
                        result.getString("otsikko"));
                vinkki.setTagit(lukuvinkinTagit);
                lukuvinkit.add(vinkki);
            }

            prepared.close();

        } catch (SQLException e) {
            System.out.println("Lukuvinkkien hakeminen epäonnistui.");
            e.printStackTrace();
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
    
    @Override
    public List<Lukuvinkki> searchByTitle(final String title) {
        return new ArrayList<>();
    }

}
