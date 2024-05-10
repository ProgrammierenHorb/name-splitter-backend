package com.example.namesplitter.storage;

import com.example.namesplitter.model.Gender;
import com.example.namesplitter.storage.interfaces.NameGenderService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * The SQLiteNameGenderService class implements the NameGenderService interface and provides a method to get the gender of a name.
 * It uses SQLite database to store and retrieve the gender of names.
 * Data is taken from <a href="https://archive.ics.uci.edu/dataset/591/gender+by+name">this open dataset</a>
 */
public class SQLiteNameGenderService implements NameGenderService {

    //names db holds the content of the free dataset: https://archive.ics.uci.edu/dataset/591/gender+by+name
    private static final String DB_URL = "jdbc:sqlite::resource:names.db";

    /**
     * The getGender method takes a name as input and returns its gender.
     * It connects to the SQLite database and executes a SQL query to get the gender of the input name.
     * It returns null if the name is not found in the database.
     *
     * @param name The name whose gender is to be retrieved.
     * @return The gender of the input name, or null if the name is not found.
     */
    @Override
    public Gender getGender(String name) {
        loadSQLiteJDBCDriver();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            //TODO: this is probably vulnerable to SQL injection but should be suitable for this prototype
            String query = "SELECT Gender FROM names WHERE Name = '" + name + "'";
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                return rs.getString("Gender").equals("M") ? Gender.MALE : Gender.FEMALE;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * The loadSQLiteJDBCDriver method loads the SQLite JDBC driver.
     * It is called before connecting to the SQLite database.
     */
    private void loadSQLiteJDBCDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}