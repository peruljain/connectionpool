package org.example.connectionpool;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnectionPool {
    Connection getConnection() throws InterruptedException, SQLException, ClassNotFoundException;
    void putConnection(Connection connection) throws InterruptedException;
}
