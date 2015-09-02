/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

/**
 *
 * @author philipp
 * Hallo
 */public interface DB_Config {
    public static final String DB_NAME = "FDISK";
    public static final String DB_USER = "Admin";
    public static final String DB_PASSWD = "htlk";
    public static final String DB_URL = "jdbc:sqlserver://localhost:30931;databaseName=FDISK";
    //public static final String DB_URL = "jdbc:sqlserver://TestServer\\SQLEXPRESS;databaseName=FDISK";
    public static final String DB_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
}

