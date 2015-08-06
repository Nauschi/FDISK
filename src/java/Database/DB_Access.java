/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author philipp
 */
public class DB_Access {

    private DB_ConnectionPool connPool;
    private static DB_Access theInstance = null;

    public static DB_Access getInstance() throws ClassNotFoundException {
        if (theInstance == null) {
            theInstance = new DB_Access();
        }
        return theInstance;
    }

    private DB_Access() throws ClassNotFoundException {
        connPool = DB_ConnectionPool.getInstance();
    }

//    public LinkedList<Movie> getMovies() throws SQLException, Exception {
//        Connection conn = connPool.getConnection();
//        Statement stat = conn.createStatement();
//        String sqlString = "SELECT title, description, category, length, actors"
//                + " FROM film_list;";
//        ResultSet rs = stat.executeQuery(sqlString);
//
//        String titel;
//        String beschreibung;
//        String schauspieler;
//        String genre;
//        int laenge;
//
//        while (rs.next()) {
//            titel = rs.getString("title");
//            beschreibung = rs.getString("description");
//            schauspieler = rs.getString("actors");
//            genre = rs.getString("category");
//            laenge = rs.getInt("length");
//            Movie movie = new Movie(titel, beschreibung, genre, schauspieler, laenge);
//            movies.add(movie);
//        }
//
//        connPool.releaseConnection(conn);
//        return movies;
//    }
    public LinkedList<String> getInstanznamen() throws Exception {
        LinkedList<String> liInstanznamen = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();
        String sqlString = "SELECT TOP 1000 instanzname"
                + "  FROM FDISK.dbo.qry_alle_feuerwehren";
        ResultSet rs = stat.executeQuery(sqlString);
        while (rs.next()) {
            liInstanznamen.add(rs.getString("instanzname"));
        }
        connPool.releaseConnection(conn);
        return liInstanznamen;
    }
    
    public static void main(String[] args) {
        try {
            theInstance = DB_Access.getInstance();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DB_Access.class.getName()).log(Level.SEVERE, null, ex);
        }
        LinkedList<String> lili = new LinkedList<>();
        try {
            lili = theInstance.getInstanznamen();
        } catch (Exception ex) {
            Logger.getLogger(DB_Access.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (String instanzname : lili) {
            System.out.println("Instanzname: "+instanzname);
        }
    }

}
