package org.example;

import org.example.connectionpool.ConnectionPool;
import org.example.connectionpool.IConnectionPool;
import org.example.util.DateUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class WithConnectionPool {
    private final IConnectionPool connectionPool;

    public WithConnectionPool(IConnectionPool connectionPool) {
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
                System.out.println(DateUtil.getDate() + " " +Thread.currentThread().getName() + " " + "SLEEP executed successfully");
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
