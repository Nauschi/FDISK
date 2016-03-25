/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import static Database.DB_Config.DB_DRIVER;
import static Database.DB_Config.DB_PASSWD;
import static Database.DB_Config.DB_URL;
import static Database.DB_Config.DB_USER;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedList;

/**
 *
 * @author philipp
 */
public class DB_ConnectionPool {
    private LinkedList<Connection> connections = new LinkedList<>();
    private static final int MAX_CONN = 1000;
    private int num_conn = 0;

    private static DB_ConnectionPool theInstance = null; 

    public static DB_ConnectionPool getInstance() throws ClassNotFoundException {
        if (theInstance == null) {
            theInstance = new DB_ConnectionPool();
        }
        return theInstance;
    }

    private DB_ConnectionPool() throws ClassNotFoundException {
        Class.forName(DB_DRIVER);
    }

    public synchronized Connection getConnection() throws Exception {
        if (connections.isEmpty()) {
            if (num_conn == MAX_CONN) {
                throw new Exception("Max number of connection reached");
            }
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);
            num_conn++;
            return conn;
        } else {
            return connections.poll();
        }
    }

    public synchronized void releaseConnection(Connection conn) {
        connections.offer(conn);
    }
}
