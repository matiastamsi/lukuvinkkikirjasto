package lukuvinkkikirjasto.dao;

import java.util.ArrayList;
import java.util.List;
import lukuvinkkikirjasto.Lukuvinkki;

/**
 * Temporary data access object that stores the data temporarily to a list. Can
 * be deleted when the class 'LukuvinkkiDAO' is taken into use.
 *
 * @author Lukuvinkkikirjasto-group
 */
public class EiPysyvaTallennusDAO implements DAO {

    private List<Lukuvinkki> list;

    public EiPysyvaTallennusDAO() {
        this.list = new ArrayList<>();
    }

    @Override
    public List<Lukuvinkki> getAll() {
        return this.list;
    }

    @Override
    public void delete(final Lukuvinkki lukuvinkki) {

    }

    @Override
    public void add(final Lukuvinkki lukuvinkki) {
        this.list.add(lukuvinkki);
    }

    @Override
    public void edit(final Lukuvinkki lukuvinkki) {

    }

}
