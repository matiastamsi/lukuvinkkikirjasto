package lukuvinkkikirjasto.dao;

import java.nio.file.Path;
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

    void initializeDatabase(Path path);

    List<Lukuvinkki> searchByTitle(String title, boolean exact);

    List<Lukuvinkki> searchByTags(List<String> tagfilter);

    String markAsRead(Lukuvinkki lukuvinkki);

    void close();
}
