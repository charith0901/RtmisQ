package com.example.rtmisq;

import java.net.URL;
import java.sql.*;
import java.util.Objects;

public class DBConnection {

    // Method to establish a database connection
    protected Connection Connectiondb() {
        try {
            // Registering the JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Creating a connection to the SQLite database
            Connection conn = DriverManager.getConnection("jdbc:sqlite:triviaVault.db");

            // Printing success message if the connection is successful
            System.out.println("Connection success");
            return conn;
        } catch (Exception e) {
            // Printing any exceptions that occur during connection establishment
            System.out.println(e);
            return null;
        }
    }

    // Main method for testing database connection and executing a query
    public static void main(String[] args) {
        // Creating an instance of the DBConnection class
        DBConnection dbConnection = new DBConnection();

        // Establishing a connection to the database
        Connection conn = dbConnection.Connectiondb();

        // SQL query to retrieve data from the "habitats_s" table
        String GetQuesQuery = "select * from habitats_s order by qnumber;";

        try {
            // Creating a statement for executing the SQL query
            Statement stmt = conn.createStatement();

            // Executing the SQL query and retrieving the result set
            ResultSet res = stmt.executeQuery(GetQuesQuery);

            // Counter for tracking the iteration
            int i = 1;

            // Iterating through the result set
            while (res.next()) {
                // Checking if the answer matches any of the options
                if (Objects.equals(res.getString("answer"), res.getString("ans1")) ||
                        Objects.equals(res.getString("answer"), res.getString("ans2")) ||
                        Objects.equals(res.getString("answer"), res.getString("ans3")) ||
                        Objects.equals(res.getString("answer"), res.getString("ans4"))) {
                    System.out.println("okay     " + res.getString("answer"));
                } else {
                    System.out.println(i + "   is not " + res.getString("answer"));
                }
                i++;
            }

            // Printing the total number of iterations
            System.out.println(i);
        } catch (SQLException e) {
            // Printing any exceptions that occur during query execution
            System.out.println(e);
        }
    }
}
