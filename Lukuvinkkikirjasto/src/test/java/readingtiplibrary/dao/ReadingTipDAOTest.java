/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package readingtiplibrary.dao;

import java.io.IOException;
import java.sql.SQLException;
import org.junit.Before;
import readingtiplibrary.databaseconnection.ConnectionToDatabase;

/**
 * Test the ReadinTipDAO class that is the class which is responsible for saving
 * the data to database.
 */
public class ReadingTipDAOTest {

    ConnectionToDatabase connection;
    ReadingTipDAO dao;

    @Before
    public void setUp() throws SQLException, IOException {
        connection = new ConnectionToDatabase("jdbc:sqlite:test.db");
        dao = new ReadingTipDAO(connection);
        dao.createDatabase();
    }
    
    
}
