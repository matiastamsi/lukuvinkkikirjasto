package lukuvinkkikirjasto.dao;

import java.util.ArrayList;
import java.util.List;
import lukuvinkkikirjasto.Lukuvinkki;

/**
 * The interface that is responsible for data access when the data is stored
 * with SQL.
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
    public void delete(final Lukuvinkki lukuvinkki) {

    }

    @Override
    public void add(final Lukuvinkki lukuvinkki) {

    }

    @Override
    public void edit(final Lukuvinkki lukuvinkki) {

    }
    
    @Override
    public void searchByTitle(final String title) {
        
    }

}
