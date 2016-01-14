/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

/**
 *
 * @author philipp
 */public interface DB_Config {
    public static final String DB_USER = "application_fw_db";
    public static final String DB_PASSWD = "";
    public static final String DB_URL = "jdbc:sqlserver://prometheus;databaseName=FDISK";
    //public static final String DB_URL = "PROMETHEUS\\PROMETHEUS;databaseName=FDISK";
    //public static final String DB_URL = "jdbc:sqlserver://TestServer\\SQLEXPRESS;databaseName=FDISK";
    public static final String DB_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
}

