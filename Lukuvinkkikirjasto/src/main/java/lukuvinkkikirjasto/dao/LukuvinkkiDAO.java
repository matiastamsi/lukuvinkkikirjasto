package lukuvinkkikirjasto.dao;

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
        return new ArrayList<>();
    }

    @Override
    public void delete(final Lukuvinkki lukuvinkki) {
        System.out.println("Metodia ei ole vielä toteutettu.");
    }

    @Override
    public void add(final Lukuvinkki lukuvinkki) {
        System.out.println("Metodia ei ole vielä toteutettu.");
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
                + "(id INTEGER PRIMARY KEY, otsikko TEXT, tagit Text)");
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

}
