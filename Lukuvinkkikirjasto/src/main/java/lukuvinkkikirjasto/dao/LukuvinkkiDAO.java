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
    public void delete(final Lukuvinkki lukuvinkki) {
        System.out.println("Metodia ei ole vielä toteutettu.");
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
    public void edit(final Lukuvinkki lukuvinkki) {
        System.out.println("Metodia ei ole vielä toteutettu.");
    }

    @Override
    public void initializeDatabase() {
        System.out.println("Metodia ei ole vielä toteutettu.");

    }

    @Override
    public List<Lukuvinkki> searchByTitle(final String title) {
        List<Lukuvinkki> lukuvinkit = new ArrayList<>();
        try {
            PreparedStatement prepared = this.connection
                    .getPreparedStatement(
                        "SELECT id, otsikko "
                        + "FROM Lukuvinkit WHERE otsikko LIKE ?");
            prepared.setString(1, "%"+title+"%");
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



}
