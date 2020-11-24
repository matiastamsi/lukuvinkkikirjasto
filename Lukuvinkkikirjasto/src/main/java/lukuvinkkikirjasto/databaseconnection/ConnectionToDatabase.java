package lukuvinkkikirjasto.databaseconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionToDatabase {
    private Connection connection;
    private Statement statement;

    public ConnectionToDatabase(String url) {
        try {
            this.connection = DriverManager.getConnection(url);
            this.statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Yhteyden muodostaminen tietokantaan epäonnistui: ");
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public Boolean isConnected() throws SQLException {
        return this.connection.isClosed();
    }

    public Statement getStatement() {
        return this.statement;
    }

    public void closeConnection() throws SQLException {
        this.connection.close();
    }

    public void commit() throws SQLException {
        this.connection.commit();
    }

    public void createDatabase()  {
        try {
            this.connection.setAutoCommit(false);
            this.statement.execute("CREATE TABLE Lukuvinkit (id INTEGER PRIMARY KEY, otsikko TEXT, tagit Text)");
            connection.commit();
            connection.setAutoCommit(true);
            System.out.println("Tietokanta luotu.");
        } catch (SQLException e) {
            System.out.println("Tietokannan luominen epäonnistui, tai tietokanta on jo luotu.");
            e.printStackTrace();
        }
        
    }
    
}