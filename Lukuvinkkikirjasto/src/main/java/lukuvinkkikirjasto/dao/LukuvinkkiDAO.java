
package lukuvinkkikirjasto.dao;

import java.util.*;
import lukuvinkkikirjasto.Lukuvinkki;
/**
 * The interface that is responsible for data access
 * when the data is stored with SQL.
 * 
 * @author Lukuvinkkikirjasto-group
 */
public class LukuvinkkiDAO implements DAO {
    
    //SQL - Anna toteuttaa tähän luokkaan.
    
    @Override
    public List<Lukuvinkki> getAll() {
        return new ArrayList<>();
    }

    @Override
    public void delete(Lukuvinkki lukuvinkki) {

    }

    @Override
    public void add(Lukuvinkki lukuvinkki) {
 
    }

    @Override
    public void edit(Lukuvinkki lukuvinkki) {
 
    }
    
}
