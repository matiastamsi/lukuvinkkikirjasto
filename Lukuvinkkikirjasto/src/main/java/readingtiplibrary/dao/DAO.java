package readingtiplibrary.dao;

import java.nio.file.Path;
import java.util.List;
import readingtiplibrary.domain.ReadingTip;

/**
 * The interface that is responsible for data access.
 *
 * @author Lukuvinkkikirjasto-group
 */
public interface DAO {

    List<ReadingTip> getAll();

    void add(ReadingTip lukuvinkki);

    void createDatabase();

    void initializeDatabase(Path path);

    List<ReadingTip> searchByTitle(String title, boolean exact);

    List<ReadingTip> searchByTags(List<String> tagfilter);

    String getLink(ReadingTip rt);

    String markAsRead(ReadingTip lukuvinkki);

    void close();
}
