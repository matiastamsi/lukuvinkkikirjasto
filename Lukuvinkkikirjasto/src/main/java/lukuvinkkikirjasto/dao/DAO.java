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

    void delete(Lukuvinkki lukuvinkki);

    void add(Lukuvinkki lukuvinkki);

    void edit(Lukuvinkki lukuvinkki);
}
