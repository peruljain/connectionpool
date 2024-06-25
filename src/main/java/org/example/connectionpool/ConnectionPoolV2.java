package org.example.connectionpool;

import org.example.BoundedBlockingQueue;
import org.example.ConnectionProvider;
import org.example.util.DateUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionPoolV2 implements IConnectionPool{

    private static final Integer MIN_CONNECTIONS = 10;
    private static final Integer MAX_CONNECTIONS = 100;
    private final BoundedBlockingQueue<Connection> queue;
    private final Integer minConnections;
    private final Integer maxConnections;

    private final AtomicInteger counter;

    public ConnectionPoolV2() throws SQLException, ClassNotFoundException {
        this.minConnections = MIN_CONNECTIONS;
        this.maxConnections = MAX_CONNECTIONS;
        final List<Connection> connections = new ArrayList<>();
        this.queue = getBoundedBlockingQueue();
        this.counter = new AtomicInteger(this.queue.size());
    }

    public ConnectionPoolV2(Integer minConnections, Integer maxConnections) throws SQLException, ClassNotFoundException {
        this.minConnections = minConnections;
        this.maxConnections = maxConnections;
        this.queue = getBoundedBlockingQueue();
        this.counter = new AtomicInteger(this.queue.size());
    }

    private BoundedBlockingQueue<Connection> getBoundedBlockingQueue() throws SQLException, ClassNotFoundException {
        final BoundedBlockingQueue<Connection> queue;
        List<Connection> connections = new ArrayList<>();
        for (int i = 0; i < this.minConnections; i++) {
            connections.add(ConnectionProvider.getConnection());
        }
        return new BoundedBlockingQueue<>(connections, this.maxConnections);
    }



    @Override
    public Connection getConnection() throws InterruptedException, SQLException, ClassNotFoundException {
        System.out.println(DateUtil.getDate() + " " + Thread.currentThread().getName() + " Get Connection....");
        putConnectionsIfRequired();
        Connection connection = queue.dequeue();
        counter.decrementAndGet();
        System.out.println(DateUtil.getDate() + " " +Thread.currentThread().getName() + " Get Connection Done ....");
        System.out.println(DateUtil.getDate() + " " +Thread.currentThread().getName() + " " + queue.size());
        return connection;
    }

    private void putConnectionsIfRequired() throws InterruptedException, SQLException, ClassNotFoundException {
        if (queue.size() == 0 && counter.get() <= this.maxConnections) {
            System.out.println(DateUtil.getDate() + " " +Thread.currentThread().getName() + " Put Connection Required ....");
            System.out.println(DateUtil.getDate() + " " +Thread.currentThread().getName() + " " + queue.size());
            synchronized (this) {
                System.out.println(DateUtil.getDate() + " " +Thread.currentThread().getName() + " Put Connection Required inside sync....");
                System.out.println(DateUtil.getDate() + " " +Thread.currentThread().getName() + " " + queue.size());
                if (queue.size() == 0 && counter.get() <= this.maxConnections) {
                    queue.enqueueWithoutWait(ConnectionProvider.getConnection());
                    counter.incrementAndGet();
                    System.out.println(DateUtil.getDate() + " " +Thread.currentThread().getName() + " Put Connection Required inside sync done ....");
                    System.out.println(DateUtil.getDate() + " " +Thread.currentThread().getName() + " " + queue.size());
                }
            }
        }
    }

    @Override
    public void putConnection(Connection connection) throws InterruptedException {
        System.out.println(DateUtil.getDate() + " " +Thread.currentThread().getName() + " Put Connection....");
        counter.incrementAndGet();
        queue.enqueueWithoutWait(connection);
        System.out.println(DateUtil.getDate() + " " +Thread.currentThread().getName() + " Put Connection Done ....");
        System.out.println(DateUtil.getDate() + " " +Thread.currentThread().getName() + " " + queue.size());
    }
}
