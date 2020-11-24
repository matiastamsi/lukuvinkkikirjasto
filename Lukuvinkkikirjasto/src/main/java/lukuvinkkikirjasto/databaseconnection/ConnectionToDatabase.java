package lukuvinkkikirjasto.databaseconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionToDatabase {
    private Connection connection;
    private Statement statement;

    public ConnectionToDatabase(final String url) {
        try {
            this.connection = DriverManager.getConnection(url);
            this.statement = connection.createStatement();
            System.out.println("Yhteyden muodostaminen onnistui: "
            + isConnected());
        } catch (SQLException e) {
            System.out.println("Yhteyden muodostaminen "
            + "tietokantaan epäonnistui: ");
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public Boolean isConnected() throws SQLException {
        if (this.connection.isClosed()) {
            return false;
        }
        return true;
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

    public void setAutoCommit(final Boolean bool) throws SQLException {
        this.connection.setAutoCommit(bool);
    }
    
}