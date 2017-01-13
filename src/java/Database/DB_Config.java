/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

/**
 *
 * @author philipp
 */
public interface DB_Config {

    public static final String DB_NAME = "FDISK";
    public static final String DB_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    
    //Local Config (HTL)
    //public static final String DB_USER = "Admin";
    //public static final String DB_PASSWD = "htlk";
    //public static final String DB_URL = "jdbc:sqlserver://localhost:30931;databaseName=FDISK";
    
    //Prometheus Config
    public static final String DB_USER = "sa";
    public static final String DB_PASSWD = "53411@lfv";
    public static final String DB_URL = "jdbc:sqlserver://172.16.1.18;instanceName=prometheus;databaseName=FDISK";
    
    //hp370 Config - not working yet
    //public static final String DB_USER = "application_fw_db";
    //public static final String DB_PASSWD = "";
    //public static final String DB_URL = "jdbc:sqlserver://172.16.1.11;instanceName=hp370;databaseName=FW_DB_LIGHT";
    
  
    
    
}
