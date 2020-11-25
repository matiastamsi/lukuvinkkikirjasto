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
    public List<Lukuvinkki> getAll() {
        List<Lukuvinkki> lukuvinkit = new ArrayList<>();
        try {
            PreparedStatement prepared = this.connection
                    .getPreparedStatement("SELECT id, otsikko FROM Lukuvinkit");
            ResultSet result = prepared.executeQuery();
            
            while (result.next()) {
                    lukuvinkit.add(new Lukuvinkki(
                        result.getInt("id"),
                        result.getString("otsikko")));
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
                    .getPreparedStatement("INSERT INTO Lukuvinkit " + "(otsikko) VALUES (?)");
            prepared.setString(1, lukuvinkki.getOtsikko());
            prepared.executeUpdate();
            prepared.close();
            
            if (lukuvinkki.getTagit()!=null) {

                Integer lukuvinkki_id = queryId(lukuvinkki.getOtsikko(), 
                    "SELECT id FROM Lukuvinkit WHERE otsikko = ?");

                for (int i = 0; i < lukuvinkki.getTagit().size(); i++) {
                    String tagiNimi = lukuvinkki.getTagit().get(i);
                    Integer tagi_id = queryId(tagiNimi, 
                        "SELECT id FROM Lukuvinkit WHERE otsikko = ?");
                    if (tagi_id!=-1) {
                        prepared = this.connection
                            .getPreparedStatement("INSERT INTO LukuvinkitJaTagit " 
                            + "(lukuvinkki_id, tagi_id) VALUES (?, ?)");
                        prepared.setInt(1, lukuvinkki_id);
                        prepared.setInt(2, tagi_id);
                        prepared.executeUpdate();
                        prepared.close();
                    }
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Lukuvinkin lisääminen epäonnistui. Yritä uudestaan.");
            e.printStackTrace();
        }

    }

    @Override
    public void edit(final Lukuvinkki lukuvinkki) {
        System.out.println("Metodia ei ole vielä toteutettu.");
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
    public void initializeDatabase() {
        System.out.println("Metodia ei ole vielä toteutettu.");

    }

    //apumetodit:
    private Integer queryId(String value, String sqlString) {
        try {
            PreparedStatement prepared = this.connection.getPreparedStatement(sqlString);
            prepared.setString(1, value);

            ResultSet result = prepared.executeQuery();

            return result.getInt("id");

        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public List<Lukuvinkki> searchByTitle(String title) {
        // TODO Auto-generated method stub
        return null;
    }

}
