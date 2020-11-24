/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lukuvinkkikirjasto.dao;

import java.util.List;
import lukuvinkkikirjasto.Lukuvinkki;

/**
 *
 * @author tamsi
 */
public interface DAO {
    
    List<Lukuvinkki> getAll();
    void delete(Lukuvinkki lukuvinkki);
    void add(Lukuvinkki lukuvinkki);
    void edit(Lukuvinkki lukuvinkki);
}
