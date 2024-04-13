package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class WithoutConnectionPool {

    public void execute() {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establishing a connection
            Connection conn = ConnectionProvider.getConnection();

            // Creating a statement
            Statement stmt = conn.createStatement();

            // Executing the query
            ResultSet rs = stmt.executeQuery("SELECT SLEEP(0.01)");

            // Processing the result
            if (rs.next()) {
                System.out.println("SLEEP executed successfully");
            }

            // Closing resources
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
