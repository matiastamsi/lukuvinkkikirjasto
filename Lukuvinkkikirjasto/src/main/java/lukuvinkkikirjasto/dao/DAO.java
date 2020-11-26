package lukuvinkkikirjasto.dao;

import java.util.List;
import lukuvinkkikirjasto.Lukuvinkki;

/**
 * The interface that is responsible for data access.
 *
 * @author Lukuvinkkikirjasto-group
 */
public interface DAO {

    List<Lukuvinkki> getAll();

    boolean delete(String title);

    boolean edit(String title);

    void add(Lukuvinkki lukuvinkki);

    void createDatabase();

    void initializeDatabase();

    List<Lukuvinkki> searchByTitle(String title, boolean exact);

    void close();
}
