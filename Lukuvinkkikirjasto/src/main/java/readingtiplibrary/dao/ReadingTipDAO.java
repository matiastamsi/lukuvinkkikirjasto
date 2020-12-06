package readingtiplibrary.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import readingtiplibrary.domain.ReadingTip;
import readingtiplibrary.databaseconnection.ConnectionToDatabase;

/**
 * The interface that is responsible for data access when the data is stored
 * with SQL.
 *
 * @author Group 1.
 */
public class ReadingTipDAO implements DAO {

    private ConnectionToDatabase connection;

    public ReadingTipDAO(final ConnectionToDatabase connection) {
        this.connection = connection;
    }

    @Override
    public void createDatabase() {
        try {
            this.connection.setAutoCommit(false);
            this.connection.getStatement()
                    .execute("CREATE TABLE Lukuvinkit "
                            + "(id INTEGER PRIMARY KEY, otsikko TEXT UNIQUE, "
                            + "linkki TEXT, "
                            + "read TEXT)");

            this.connection.getStatement()
                    .execute("CREATE TABLE Tagit "
                            + "(id INTEGER PRIMARY KEY, nimi TEXT)");

            this.connection.getStatement()
                    .execute("CREATE TABLE LukuvinkitJaTagit "
                            + "(lukuvinkki_id INTEGER, tagi_id INTEGER)");
            connection.commit();
            connection.setAutoCommit(true);
            System.out.println("Tietokanta luotu.");
        } catch (SQLException e) {
            System.out.println("Tietokannan luominen "
                    + "epäonnistui, tai tietokanta on jo luotu.");
        }
    }

    @Override
    public void close() {
        try {
            this.connection.closeConnection();
        } catch (SQLException e) {
            System.out.println("Tietokannan sulkeminen epäonnistui!");
        }
    }

    @Override
    public List<ReadingTip> getAll() {
        List<ReadingTip> readingTips = new ArrayList<>();
        try {
            PreparedStatement prepared = this.connection
                    .getPreparedStatement(
                            "SELECT id, otsikko, linkki, read FROM Lukuvinkit");
            ResultSet result = prepared.executeQuery();

            while (result.next()) {
                ArrayList<String> tags = queryTags(
                        result.getInt("id"));
                //System.out.println("lukuvinkin tagit" + lukuvinkinTagit);
                ReadingTip rt = new ReadingTip(
                        result.getInt("id"),
                        result.getString("otsikko"));
                rt.setTags(tags);
                rt.setRead(result.getString("read"));
                String link = result.getString("linkki");
                rt.setLink(link);
                readingTips.add(rt);
            }
            prepared.close();

        } catch (SQLException e) {
            System.out.println("Lukuvinkkien hakeminen epäonnistui.");
        }
        return readingTips;
    }


    public boolean findValidTag(ReadingTip rt, String tagName) throws SQLException {
        PreparedStatement prepared = this.connection
                .getPreparedStatement("SELECT count(*) FROM LukuvinkitJaTagit, Tagit WHERE LukuvinkitjaTagit.lukuvinkki_id = ? AND Tagit.nimi = ?");
        prepared.setInt(1, rt.getId());
        prepared.setString(2, tagName);
        ResultSet results = prepared.executeQuery();
        int tagId = results.getInt("count(*)");
        prepared.close();
        return (tagId > 0);
    }

    public Integer findTagId(ReadingTip rt, String tagName) throws SQLException {
        PreparedStatement prepared = this.connection
                .getPreparedStatement("SELECT tagi_id FROM LukuvinkitJaTagit, Tagit WHERE LukuvinkitJaTagit.lukuvinkki_id = ? AND Tagit.nimi= ?");
        prepared.setInt(1, rt.getId());
        prepared.setString(2, tagName);
        ResultSet results = prepared.executeQuery();
        int tagId = results.getInt("tagi_id");
        prepared.close();
        return tagId;
    }

    public void deleteReadingTip(ReadingTip rt) throws SQLException {
        PreparedStatement prepared = this.connection
                .getPreparedStatement("DELETE FROM Lukuvinkit WHERE id= ?");
        prepared.setInt(1, rt.getId());
        prepared.executeUpdate();
        prepared.close();
    }



    public void deleteTag(Integer tagId) throws SQLException {
        PreparedStatement prepared = this.connection
                .getPreparedStatement("DELETE FROM Tagit WHERE id= ?");
        prepared.setInt(1, tagId);
        prepared.executeUpdate();
        prepared.close();
    }

    @Override
    public void add(final ReadingTip rt) {
        try {
            PreparedStatement prepared = this.connection
                    .getPreparedStatement("INSERT INTO Lukuvinkit "
                            + "(otsikko, linkki) VALUES (?, ?)");
            prepared.setString(1, rt.getTitle());
            prepared.setString(2, rt.getLink());
            prepared.executeUpdate();

            if (rt.getTags() != null) {

                Integer rtId = queryId(rt.getTitle(),
                        "SELECT id FROM Lukuvinkit WHERE otsikko = ?");

                for (int i = 0; i < rt.getTags().size(); i++) {
                    String tagName = rt.getTags().get(i);
                    Integer tagId = queryId(tagName,
                            "SELECT id FROM Tagit WHERE nimi = ?");

                    if (tagId == -1) {
                        prepared = this.connection
                                .getPreparedStatement("INSERT INTO Tagit "
                                        + "(nimi) VALUES (?)");
                        prepared.setString(1, tagName);
                        prepared.executeUpdate();
                        prepared.close();
                        tagId = queryId(tagName,
                                "SELECT id FROM Tagit WHERE nimi = ?");
                    }
                    prepared = this.connection.getPreparedStatement(
                            "INSERT INTO LukuvinkitJaTagit "
                            + "(lukuvinkki_id, tagi_id) VALUES (?, ?)");
                    prepared.setInt(1, rtId);
                    prepared.setInt(2, tagId);
                    prepared.executeUpdate();
                    prepared.close();
                }
            }
            prepared.close();
        } catch (SQLException e) {
            System.out.println("Lukuvinkin lisääminen epäonnistui. "
                    + "Yritä uudestaan.");
        }
    }

    public void addTag(ReadingTip rt, String tag) throws SQLException {
        PreparedStatement prepared = this.connection.getPreparedStatement(
                "INSERT INTO Tagit (nimi) values (?)");
        prepared.setString(1, tag);
        prepared.executeUpdate();
        prepared.close();
        Integer tagId = queryId(tag,
                "SELECT id FROM Tagit WHERE nimi = ?");
        prepared = this.connection.getPreparedStatement(
                "INSERT INTO LukuvinkitJaTagit "
                + "(lukuvinkki_id, tagi_id) VALUES (?, ?)");
        prepared.setInt(1, rt.getId());
        prepared.setInt(2, tagId);
        prepared.executeUpdate();
        prepared.close();

    }

    public void addLink(ReadingTip rt, String link) {
        try {
            String name = rt.getTitle();
            Integer vinkkiId = queryId(name, "SELECT id FROM "
                    + "Lukuvinkit WHERE otsikko = ?");
            PreparedStatement prepared = this.connection
                    .getPreparedStatement("UPDATE Lukuvinkit "
                            + "SET linkki = ? WHERE id = ?");
            prepared.setInt(2, vinkkiId);
            prepared.setString(1, link);
            prepared.executeUpdate();
            prepared.close();

        } catch (SQLException e) {
            System.out.println("Linkin lisääminen epäonnistui!");
        }
    }


    @Override
    public void initializeDatabase(Path path) {
        try {
            Files.deleteIfExists(path);
            this.connection = new ConnectionToDatabase("jdbc:sqlite:tietokanta.db");
        } catch (IOException e) {
            System.out.println("Tietokannan alustaminen epäonnistui!");
        }
    }

    @Override
    public List<ReadingTip> searchByTitle(final String title, final boolean exact) {
        List<ReadingTip> readingTips = new ArrayList<>();
        try {
            PreparedStatement prepared = this.connection
                    .getPreparedStatement(
                            "SELECT id, otsikko, linkki, read "
                            + "FROM Lukuvinkit WHERE otsikko LIKE ?");
            if (exact) {
                prepared.setString(1, title);
            } else {
                prepared.setString(1, "%" + title + "%");
            }
            ResultSet result = prepared.executeQuery();

            while (result.next()) {
                ArrayList<String> tags = queryTags(
                        result.getInt("id"));
                ReadingTip rt = new ReadingTip(
                        result.getInt("id"),
                        result.getString("otsikko"));
                String link = result.getString("linkki");
                String read = result.getString("read");
                if (read != null) {
                    rt.setRead(read);
                }
                rt.setTags(tags);
                rt.setLink(link);
                readingTips.add(rt);
            }

            prepared.close();

        } catch (SQLException e) {
            System.out.println("Lukuvinkkien hakeminen epäonnistui.");
        }
        return readingTips;
    }

    @Override
    public List<ReadingTip> searchByTags(List<String> tagfilter) {

        List<ReadingTip> readingTips = new ArrayList<>();
        for (ReadingTip rt : getAll()) {
            for (String tag : rt.getTags()) {
                if (tagfilter.contains(tag)) {
                    readingTips.add(rt);
                }
            }
        }
        return readingTips;
    }

    private Integer queryId(final String value, final String sqlString) {
        try {
            PreparedStatement prepared = this.connection
                    .getPreparedStatement(sqlString);
            prepared.setString(1, value);
            ResultSet result = prepared.executeQuery();

            return result.getInt("id");

        } catch (SQLException e) {
            return -1;
        }
    }

    private ArrayList<String> queryTags(final int rtId) {
        ArrayList<String> lukuvinkinTagit = new ArrayList<>();
        try {
            PreparedStatement prepared = connection.getPreparedStatement(
                    "SELECT Tagit.nimi "
                    + "FROM Tagit, Lukuvinkit, LukuvinkitJaTagit "
                    + "WHERE Tagit.id = LukuvinkitJaTagit.tagi_id "
                    + "AND Lukuvinkit.id = LukuvinkitJaTagit.lukuvinkki_id "
                    + "AND Lukuvinkit.id = ?");
            prepared.setInt(1, rtId);
            ResultSet result = prepared.executeQuery();
            while (result.next()) {
                lukuvinkinTagit.add(result.getString("nimi"));
            }

        } catch (SQLException e) {
            System.out.println("Tagien hakeminen epäonnistui.");
        }

        return lukuvinkinTagit;
    }

    public int getAmountOfReadingTips() {
        try {
            PreparedStatement p = connection.getPreparedStatement(
                    "SELECT COUNT(id) count FROM Lukuvinkit");
            ResultSet r = p.executeQuery();
            int i = r.getInt("count");
            p.close();
            return i;
        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public String getLink(ReadingTip rt) {
        try {
            PreparedStatement prepared = this.connection
                    .getPreparedStatement("SELECT linkki FROM Lukuvinkit WHERE id = ?");
                    prepared.setInt(1, rt.getId());
            ResultSet r = prepared.executeQuery();
            String url = r.getString("linkki");
            prepared.close();
            return url;
        } catch (SQLException e) {
            return "Linkkiä ei löydetty";
        }
    }

    @Override
    public String markAsRead(ReadingTip rt) {
        try {
            PreparedStatement prepared = this.connection
                    .getPreparedStatement("UPDATE Lukuvinkit "
                            + "SET read = ? WHERE id = ?");
            prepared.setString(1, rt.getRead());
            prepared.setInt(2, rt.getId());
            prepared.executeUpdate();
            prepared.close();

            prepared = this.connection
                    .getPreparedStatement("SELECT read FROM Lukuvinkit "
                            + "WHERE id = ?");
            prepared.setInt(1, rt.getId());
            ResultSet result = prepared.executeQuery();

            String date = result.getString("read");
            return "Lukuvinkin lukupäiväksi tallennettiin: " + date;

        } catch (SQLException e) {
            return "Päivämäärän lisääminen epäonnistui!";
        }

    }
}
