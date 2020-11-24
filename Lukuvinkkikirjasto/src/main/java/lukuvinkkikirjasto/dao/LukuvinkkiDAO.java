package lukuvinkkikirjasto.dao;

import java.sql.Connection;
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

    public LukuvinkkiDAO(ConnectionToDatabase connection) {
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

}
