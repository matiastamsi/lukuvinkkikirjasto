
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
    void delete(final Lukuvinkki lukuvinkki);
    void add(final Lukuvinkki lukuvinkki);
    void edit(final Lukuvinkki lukuvinkki);
}
