package org.example;

import org.example.connectionpool.ConnectionPool;
import org.example.connectionpool.ConnectionPoolV2;
import org.example.connectionpool.IConnectionPool;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//        runWithoutConnectionPool();
        runWithConnectionPool();
    }

    public static void runWithoutConnectionPool() {
        long startTime = System.currentTimeMillis();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i<500; i++) {
            Thread thread = new Thread(()-> new WithoutConnectionPool().execute());
            threads.add(thread);
            thread.start();
        }
        for(Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        long timeTaken = System.currentTimeMillis() - startTime;
        System.out.println("took time " + timeTaken + " ms");
    }

    public static void runWithConnectionPool() throws SQLException, ClassNotFoundException {
        // init connections
        IConnectionPool connectionPool = new ConnectionPoolV2(10, 20);
        long startTime = System.currentTimeMillis();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i<1000; i++) {
            Thread thread = new Thread(()-> new WithConnectionPool(connectionPool).execute());
            threads.add(thread);
            thread.start();
        }
        for(Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        long timeTaken = System.currentTimeMillis() - startTime;
        System.out.println("took time " + timeTaken + " ms");
    }
}