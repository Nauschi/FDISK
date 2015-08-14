/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import Beans.Mitglied;
import Beans.MitgliedsAdresse;
import Beans.MitgliedsDienstzeit;
import Beans.MitgliedsGeburtstag;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author philipp
 */
public class DB_Access {

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
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

    /**
     * 
     * @return
     * @throws Exception 
     */
    public LinkedList<Mitglied> getEinfacheMitgliederliste() throws Exception {
        LinkedList<Mitglied> liMitglieder = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();
        String sqlString = "SELECT TOP 1000 id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\" "
                + "FROM FDISK.dbo.stmkmitglieder";
        ResultSet rs = stat.executeQuery(sqlString);

        String strSTB;
        String strDGR;
        String strTitel;
        String strVorname;
        String strZuname;
        int intPersID;

        while (rs.next()) {
            intPersID = rs.getInt("PersID");
            strSTB = rs.getString("STB");
            strDGR = rs.getString("DGR");
            strTitel = rs.getString("Titel");
            strVorname = rs.getString("Vorname");
            strZuname = rs.getString("Zuname");

            Mitglied mitglied = new Mitglied(intPersID, strSTB, strDGR, strTitel, strVorname, strZuname, true);
            liMitglieder.add(mitglied);
        }
        connPool.releaseConnection(conn);
        return liMitglieder;
    }

    /**
     * 
     * @param jahr
     * @return
     * @throws Exception 
     */
    public LinkedList<MitgliedsGeburtstag> getGeburtstagsliste(int jahr) throws Exception {
        LinkedList<MitgliedsGeburtstag> liMitgliedsGeburtstage = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();
        String sqlString = "SELECT TOP 1000 id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\", geburtsdatum \"Geburtsdatum\" \n"
                + "FROM FDISK.dbo.stmkmitglieder";
        ResultSet rs = stat.executeQuery(sqlString);

        String strSTB;
        String strDGR;
        String strTitel;
        String strVorname;
        String strZuname;
        int intPersID;
        Date dateGeburtsdatum;
        int intZielalter;

        while (rs.next()) {
            intPersID = rs.getInt("PersID");
            strSTB = rs.getString("STB");
            strDGR = rs.getString("DGR");
            strTitel = rs.getString("Titel");
            strVorname = rs.getString("Vorname");
            strZuname = rs.getString("Zuname");
            dateGeburtsdatum = new Date(rs.getDate("Geburtsdatum").getTime());

            //Zielalter berechnen
            Calendar calGeburtsdatum = Calendar.getInstance();
            calGeburtsdatum.setTime(dateGeburtsdatum);
            intZielalter = jahr - calGeburtsdatum.get(Calendar.YEAR);

            MitgliedsGeburtstag mitgliedsGeb = new MitgliedsGeburtstag(intPersID, strSTB, strDGR, strTitel, strVorname, strZuname, false, dateGeburtsdatum, intZielalter);
            liMitgliedsGeburtstage.add(mitgliedsGeb);
        }
        connPool.releaseConnection(conn);
        return liMitgliedsGeburtstage;
    }

    /**
     *
     * @return
     * @throws Exception 
     */
    public LinkedList<MitgliedsDienstzeit> getDienstzeitListe() throws Exception {
        LinkedList<MitgliedsDienstzeit> liMitgliedsDienstzeiten = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();
        String sqlString = "SELECT TOP 1000 id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\", geburtsdatum \"Geburtsdatum\",  datum_abgemeldet \"Datum_abgemeldet\", eintrittsdatum \"Eintrittsdatum\", vordienstzeit \"Vordienstzeit\""
                + "FROM FDISK.dbo.stmkmitglieder";
        ResultSet rs = stat.executeQuery(sqlString);

        String strSTB;
        String strDGR;
        String strTitel;
        String strVorname;
        String strZuname;
        int intPersID;
        Date dateGeburtsdatum;
        Date dateEintrittsdatum;
        int intVordienstzeit;

        while (rs.next()) {
            if (rs.getDate("Datum_abgemeldet") == null) {
                intPersID = rs.getInt("PersID");
                strSTB = rs.getString("STB");
                strDGR = rs.getString("DGR");
                strTitel = rs.getString("Titel");
                strVorname = rs.getString("Vorname");
                strZuname = rs.getString("Zuname");
                dateGeburtsdatum = new Date(rs.getDate("Geburtsdatum").getTime());
                dateEintrittsdatum = new Date(rs.getDate("Eintrittsdatum").getTime());
                intVordienstzeit = rs.getInt("Vordienstzeit");
                Calendar calEintrittsdatum = Calendar.getInstance();
                calEintrittsdatum.setTime(dateEintrittsdatum);

                int intDienstzeit = Calendar.getInstance().get(Calendar.YEAR) - calEintrittsdatum.get(Calendar.YEAR);

                if (Calendar.getInstance().get(Calendar.MONTH) < calEintrittsdatum.get(Calendar.MONTH)) {
                    intDienstzeit--;
                }

                if ((Calendar.getInstance().get(Calendar.MONTH) == calEintrittsdatum.get(Calendar.MONTH)) && Calendar.getInstance().get(Calendar.DAY_OF_MONTH) < calEintrittsdatum.get(Calendar.DAY_OF_MONTH)) {
                    intDienstzeit--;
                }

                intDienstzeit += intVordienstzeit;

                MitgliedsDienstzeit mitgliedsDienst = new MitgliedsDienstzeit(intPersID, strSTB, strDGR, strTitel, strVorname, strZuname, true, dateGeburtsdatum, intDienstzeit);
                liMitgliedsDienstzeiten.add(mitgliedsDienst);
            }

        }
        connPool.releaseConnection(conn);
        return liMitgliedsDienstzeiten;
    }
    
    /**
     * 
     * @return
     * @throws Exception 
     */
    public LinkedList<MitgliedsAdresse> getAdressListe() throws Exception
    {
        LinkedList<MitgliedsAdresse> liMitgliedsAdressen = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "SELECT TOP 1000 adressen.id_adressen \"AdressID\", adressen.strasse \"Strasse\", adressen.nummer \"Nummer\", "
                + "adressen.stiege \"Stiege\", adressen.plz \"PLZ\", adressen.ort \"Ort\", mitglied.id_personen \"PersID\", "
                + "mitglied.standesbuchnummer \"STB\", mitglied.dienstgrad \"DGR\", "
                + "mitglied.titel \"Titel\", mitglied.vorname \"Vorname\", mitglied.zuname \"Zuname\", "
                + "mitglied.geburtsdatum \"Geburtsdatum\" "
                + "FROM FDISK.dbo.stmkadressen adressen INNER JOIN FDISK.dbo.stmkmitglieder mitglied "
                + "ON (adressen.id_personen = mitglied.id_personen)";

        ResultSet rs = stat.executeQuery(sqlString);

        String strSTB;
        String strDGR;
        String strTitel;
        String strVorname;
        String strZuname;
        int intPersID;
        int intId_Adressen;
        String strStrasse;
        String strNummer;
        String strStiege;
        int intPLZ;
        String strOrt;

        while (rs.next())
        {
            intPersID = rs.getInt("PersID");
            strSTB = rs.getString("STB");
            strDGR = rs.getString("DGR");
            strTitel = rs.getString("Titel");
            strVorname = rs.getString("Vorname");
            strZuname = rs.getString("Zuname");

            intId_Adressen = rs.getInt("AdressID");
            strStrasse = rs.getString("Strasse");
            strNummer = rs.getString("Nummer");
            strStiege = rs.getString("Stiege");
            intPLZ = rs.getInt("PLZ");
            strOrt = rs.getString("Ort");

            MitgliedsAdresse mitgliedsAdresse = new MitgliedsAdresse(intPersID, strSTB, strDGR, strTitel, strVorname, strZuname, true, intId_Adressen, strStrasse, strNummer, strStiege, intPLZ, strOrt, true);
            liMitgliedsAdressen.add(mitgliedsAdresse);
        }
        connPool.releaseConnection(conn);
        return liMitgliedsAdressen;
    }

    public static void main(String[] args) {
        try {
            theInstance = DB_Access.getInstance();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DB_Access.class.getName()).log(Level.SEVERE, null, ex);
        }
        LinkedList<MitgliedsAdresse> lili = new LinkedList<>();
        try {
            lili = theInstance.getAdressListe();
        } catch (Exception ex) {
            Logger.getLogger(DB_Access.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (MitgliedsAdresse ma : lili) {
            System.out.print(ma.getIntId_Adressen() + " - ");
            System.out.print(ma.getIntNummer() + " - ");
            System.out.print(ma.getIntPLZ() + " - ");
            System.out.print(ma.getStrOrt() + " - ");
            System.out.print(ma.getStrStiege() + " - ");
            System.out.print(ma.getStrStrasse() + " - ");
            System.out.print(ma.getIntId_Personen() + " - ");
            System.out.print(ma.getIntStammblattnummer() + " - ");
            System.out.print(ma.getStrDienstgrad() + " - ");
            System.out.print(ma.getStrVorname() + " - ");
            System.out.print(ma.getStrZuname() + " - ");
            System.out.print(ma.getStrTitel() + " - ");
            System.out.println("\n");
        }
    }

}
