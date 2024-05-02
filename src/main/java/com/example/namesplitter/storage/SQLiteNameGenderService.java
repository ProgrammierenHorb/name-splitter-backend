package com.example.namesplitter.storage;

import com.example.namesplitter.model.Gender;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteNameGenderService implements NameGenderService {

    private static final String DB_URL = "jdbc:sqlite::resource:names.db";

    @Override
    public Gender getGender(String name) {
        loadSQLiteJDBCDriver();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

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

    private void loadSQLiteJDBCDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}