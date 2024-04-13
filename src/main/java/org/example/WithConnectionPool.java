package org.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class WithConnectionPool {
    private final ConnectionPool connectionPool;

    public WithConnectionPool(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void execute() {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establishing a connection
            Connection conn = connectionPool.getConnection();

            // Creating a statement
            Statement stmt = conn.createStatement();

            // Executing the query
            ResultSet rs = stmt.executeQuery("SELECT SLEEP(0.01)");

            // Processing the result
            if (rs.next()) {
                System.out.println(Thread.currentThread().getName() + " " + "SLEEP executed successfully");
            }

            // Closing resources
            rs.close();
            stmt.close();
            connectionPool.putConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
