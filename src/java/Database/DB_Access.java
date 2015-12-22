/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import Beans.Berechtigung;
import Beans.Erreichbarkeit;
import Beans.Fahrzeug;
import Beans.Kurs;
import Beans.KursAlt;
import Beans.LeerberichtFahrzeug;
import Beans.LoginMitglied;
import Beans.Mitglied;
import Beans.MitgliedsAdresse;
import Beans.MitgliedsDienstzeit;
import Beans.MitgliedsErreichbarkeit;
import Beans.MitgliedsGeburtstag;
import Beans.LeerberichtMitglied;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author philipp
 */
public class DB_Access
{

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private DB_ConnectionPool connPool;
    private static DB_Access theInstance = null;

    public static DB_Access getInstance() throws ClassNotFoundException
    {
        if (theInstance == null)
        {
            theInstance = new DB_Access();
        }
        return theInstance;
    }

    private DB_Access() throws ClassNotFoundException
    {
        connPool = DB_ConnectionPool.getInstance();
    }

    /**
     * *******************************************************************************
     */
    /*                                                                                *
     /*                       STATISCHER BERICHTGENERATOR                              *
     /*                                                                                *
     /**********************************************************************************/
    public LinkedList<String> getFubwehrnummernFuerAbschnitt(int intAbschnittsnummer)
    {
        LinkedList<String> liFilterdFubwehr = new LinkedList<>();

        return liFilterdFubwehr;
    }

    public LinkedList<Berechtigung> getBerechtigungen(int intUserID) throws Exception
    {
        System.out.println(intUserID);
        LinkedList<Berechtigung> liBerechtigungen = new LinkedList<>();
        LinkedList<LoginMitglied> liLoginBerechtigung = new LinkedList<>();
        liLoginBerechtigung = getLoginBerechtigung(intUserID);
        String fubwehr = getFubwehrForUserID(intUserID);
        System.out.println("DIE RICHTIGE FUBWERH: " + fubwehr);
        String feuerwehrname = getNameFuerFubwehr(fubwehr);
        System.out.println("Fubwehrname: " + feuerwehrname);
        String abschnitt = getAbschnittsnameFuerFubwehr(fubwehr);
        System.out.println("Abschnittsname: " + abschnitt);
        String bereich = getBereichsnameFuerFubwehr(fubwehr);
        System.out.println("Bereichsname: " + bereich);
        String strBerechtigung = "";

        if (liLoginBerechtigung.isEmpty())
        {
            strBerechtigung = "Mitglied" + " - " + feuerwehrname;
            Berechtigung berechtigung = new Berechtigung(strBerechtigung, 0);
            liBerechtigungen.add(berechtigung);
        } else
        {
            for (LoginMitglied loginMitglied : liLoginBerechtigung)
            {
                String bezeichnung = loginMitglied.getStrGruppe();

                switch (loginMitglied.getIntIDGruppe())
                {
                    case 1:
                        strBerechtigung = bezeichnung + " - Alle Feuerwehren";
                        break;
                    case 5:
                        strBerechtigung = bezeichnung + " - " + bereich;
                        break;
                    case 9:
                        strBerechtigung = bezeichnung + " - " + feuerwehrname;
                        break;
                    case 15:
                        strBerechtigung = bezeichnung + " - " + abschnitt;
                        break;
                    default:
                        strBerechtigung = "nice! A fail";
                }

                Berechtigung berechtigung = new Berechtigung(strBerechtigung, loginMitglied.getIntIDGruppe());
                liBerechtigungen.add(berechtigung);
            }
        }
        return liBerechtigungen;
    }

    /**
     * Gibt die UserID für den jeweiligen Username zurück
     *
     * @param strUsername
     * @param strPasswort
     * @return
     * @throws SQLException
     * @throws Exception
     */
    public int getUserID(String strUsername, String strPasswort) throws SQLException, Exception
    {
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();
        String sqlString = "SELECT IDUser \"IDUser\", username \"username\", passwort \"passwort\" "
                + " FROM FDISK.dbo.tbl_login_benutzer "
                + " WHERE username = '" + strUsername
                + "' AND passwort = '" + strPasswort + "'";

        /**
         * int intId_User; String strFubwehr; int intIDGruppe; String strGruppe;
         */
        ResultSet rs = stat.executeQuery(sqlString);

        if (!rs.next())
        {
            connPool.releaseConnection(conn);
            return -1;
        }

        int intIDUser = rs.getInt("IDUser");
        connPool.releaseConnection(conn);

        return intIDUser;
    }

    /**
     * Verbindung der Methode getUserID und getLoginBerechtigung damit Marcel
     * nur eine Methode aufrufen muss
     *
     * @param strUsername
     * @param strPasswort
     * @return
     * @throws SQLException
     * @throws Exception
     */
    public LinkedList<LoginMitglied> getLoginMitglied(String strUsername, String strPasswort) throws SQLException, Exception
    {
        int intIDUser = getUserID(strUsername, strPasswort);
        LinkedList<LoginMitglied> liLoginMitglied = getLoginBerechtigung(intIDUser);
        return liLoginMitglied;
    }

    /**
     * Gibt eine LinkedList mit allen Berechtigungen eines Mitglieds zurück.
     * Enthalten sind die UserID, die Fubwehr, die GruppenID und die
     * Gruppenbezeichnung
     *
     * @param intUserID
     * @return
     * @throws SQLException
     * @throws Exception
     */
    public LinkedList<LoginMitglied> getLoginBerechtigung(int intUserID) throws SQLException, Exception
    {
        LinkedList<LoginMitglied> liMitglieder = new LinkedList<>();

        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();
        String sqlString = "SELECT fubwehr \"Fubwehr\", gruppe.IDGruppe \"IDGruppe\", Bezeichnung \"Bezeichnung\" "
                + "FROM FDISK.dbo.tbl_login_benutzerdetail benutzerdetail INNER JOIN FDISK.dbo.tbl_login_gruppenbenutzer gruppenbenutzer ON(benutzerdetail.IDUser = gruppenbenutzer.IDUser) "
                + "INNER JOIN FDISK.dbo.tbl_login_gruppe gruppe ON(gruppe.IDGruppe = gruppenbenutzer.IDGruppe) "
                + "WHERE benutzerdetail.IDUser = " + intUserID;
        ResultSet rs = stat.executeQuery(sqlString);

        while (rs.next())
        {
            String strFubwehr = rs.getString("Fubwehr");
            int intIDGruppe = rs.getInt("IDGruppe");
            String strBezeichnung = rs.getString("Bezeichnung");
            System.out.println("IDGruppe: " + intIDGruppe);
            System.out.println("Bezeichnung: " + strBezeichnung);
            System.out.println("Fubwehr: " + strFubwehr);
            if (intIDGruppe == 1 || intIDGruppe == 5 || intIDGruppe == 9 || intIDGruppe == 15)
            {
                LoginMitglied lm = new LoginMitglied(intUserID, strFubwehr, intIDGruppe, strBezeichnung);
                liMitglieder.add(lm);
            }

        }

        connPool.releaseConnection(conn);
        return liMitglieder;
    }

    /**
     * Gibt die zugehörige Fubwehr einer UserID zurück.
     *
     * @param intUserID
     * @return
     * @throws SQLException
     * @throws Exception
     */
    public String getFubwehrForUserID(int intUserID) throws SQLException, Exception
    {
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();
        String sqlString = "SELECT fubwehr \"Fubwehr\" "
                + "FROM FDISK.dbo.tbl_login_benutzerdetail "
                + "WHERE IDUser = " + intUserID;
        ResultSet rs = stat.executeQuery(sqlString);

        String strFubwehr = "";

        while (rs.next())
        {
            strFubwehr = rs.getString("Fubwehr");
        }

        connPool.releaseConnection(conn);
        return strFubwehr;

    }

    /**
     * Gibt die zugehörige Abschnittsnummer einer Fubwehr zurück.
     *
     * @param strFubwehr
     * @return
     * @throws SQLException
     * @throws Exception
     */
    public int getAbschnittsnummerForFubwehr(String strFubwehr) throws SQLException, Exception
    {
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();
//        String sqlString = "SELECT DISTINCT f.Abschnitt_Instanznummer \"Nummer\" "
//                + "  FROM FDISK.dbo.stmkmitglieder s INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(s.instanznummer = f.instanznummer) "
//                + "  WHERE s.instanznummer = '" + strFubwehr + "'";
//        
        String sqlString = "SELECT Abschnitt_Instanznummer \"nummer\""
                + " FROM [FDISK].[dbo].[qry_alle_feuerwehren_mit_Abschnitt_und_Bereich]"
                + " WHERE instanznummer = " + strFubwehr;
        ResultSet rs = stat.executeQuery(sqlString);

        int intAbschnittsnummer = 0;

        while (rs.next())
        {
            intAbschnittsnummer = rs.getInt("Nummer");
        }

        connPool.releaseConnection(conn);
        return intAbschnittsnummer;

    }

    /**
     * Gibt den Namen einer Fubwehr zurück. (z.B. FF Wagrain)
     *
     * @param strFubwehr
     * @return
     * @throws SQLException
     * @throws Exception
     */
    public String getNameFuerFubwehr(String strFubwehr) throws SQLException, Exception
    {
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();
        String sqlString = "SELECT TOP 1000 CONCAT(instanzart, ' ' , instanzname) \"Name\", instanznummer "
                + "FROM FDISK.dbo.qry_alle_feuerwehren "
                + "WHERE instanznummer = '" + strFubwehr + "'";
        ResultSet rs = stat.executeQuery(sqlString);

        String strName = "";

        while (rs.next())
        {
            strName = rs.getString("Name");
        }

        connPool.releaseConnection(conn);
        return strName;

    }

    /**
     * Gibt den zugehörigen Abschnittsnamen für die Fubwehr zurück
     *
     * @param strFubwehr
     * @return
     * @throws SQLException
     * @throws Exception
     */
    public String getAbschnittsnameFuerFubwehr(String strFubwehr) throws SQLException, Exception
    {
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();
        System.out.println("strFubwerh: " + strFubwehr);
        String sqlString = "SELECT Abschnitt_Instanznummer \"Instanznummer\", Abschnittsname \"Name\" "
                + "FROM FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich "
                + "WHERE instanznummer = '" + strFubwehr + "'";
        ResultSet rs = stat.executeQuery(sqlString);

        String strName = "";

        while (rs.next())
        {
            strName = rs.getString("Name");
        }

        connPool.releaseConnection(conn);
        return strName;
    }

    /**
     * Gibt den zugehörigen Bereichsnamen einer Fubwehr zurück
     *
     * @param strFubwehr
     * @return
     * @throws SQLException
     * @throws Exception
     */
    public String getBereichsnameFuerFubwehr(String strFubwehr) throws SQLException, Exception
    {
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();
        String sqlString = "SELECT uebergeordneteInstanz \"Name\", Bereich_Nr \"Nr\" "
                + "  FROM FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich "
                + "  WHERE instanznummer = '" + strFubwehr + "'";
        ResultSet rs = stat.executeQuery(sqlString);

        String strName = "";

        while (rs.next())
        {
            strName = rs.getString("Name");
        }

        connPool.releaseConnection(conn);
        return strName;
    }

    /**
     * Gibt eine LinkedList mit allen Mitarbeitern (je nach Berechtigung eines
     * Users) zurück
     *
     * @return LinkedList
     * @throws IOException
     * @see Mitglied
     * @see LinkedList
     */
    public LinkedList<Mitglied> getEinfacheMitgliederliste(int intUserID, int intGruppe) throws Exception
    {
        LinkedList<Mitglied> liMitglieder = new LinkedList<>();
        String strFubwehr = getFubwehrForUserID(intUserID) + "";
        int intAbschnittsnummer = getAbschnittsnummerForFubwehr(strFubwehr);
        String strBereich = ("" + intAbschnittsnummer).substring(0, 2);
        System.out.println("Bereich wenn Mitgliederliste aufgerufen wird: " + strBereich);

        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "";

        switch (intGruppe)
        {
            case 0:
                sqlString = "SELECT id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\" "
                        + "FROM FDISK.dbo.stmkmitglieder "
                        + "WHERE instanznummer = '" + strFubwehr + "'";
                break;
            case 1:
                sqlString = "SELECT id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\" "
                        + "FROM FDISK.dbo.stmkmitglieder";
                break;
            case 5:
                sqlString = "SELECT id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\" "
                        + "FROM FDISK.dbo.stmkmitglieder "
                        + "WHERE SUBSTRING(instanznummer, 0, 3) = '" + strBereich + "'";
//              ODER!!!!!  
//              String sqlString2 = "SELECT id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\", f.instanzname, f.instanznummer, f.Abschnitt_Instanznummer, SUBSTRING(f.instanznummer, 0, 3) \"Bereich\""
//                        + " FROM FDISK.dbo.stmkmitglieder s INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(s.instanznummer = f.instanznummer)"
//                        + " WHERE f.Bereich_Nr = '" + strBereich + "'";
                break;
            case 9:
                sqlString = "SELECT id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\" "
                        + "FROM FDISK.dbo.stmkmitglieder "
                        + "WHERE instanznummer = '" + strFubwehr + "'";
                break;
            case 15:
                sqlString = "SELECT id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\" "
                        + " FROM FDISK.dbo.stmkmitglieder s INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(s.instanznummer = f.instanznummer) "
                        + " WHERE f.abschnitt_instanznummer = " + intAbschnittsnummer;
                System.out.println("Abschnitt wenn Mitgliederliste aufgerufen wird: " + intAbschnittsnummer);
                break;
            default:
                return null;

        }

        ResultSet rs = stat.executeQuery(sqlString);

        String strSTB;
        String strDGR;
        String strTitel;
        String strVorname;
        String strZuname;
        int intPersID;

        while (rs.next())
        {
            intPersID = rs.getInt("PersID");
            strSTB = rs.getString("STB");
            strDGR = rs.getString("DGR");
            strTitel = rs.getString("Titel");
            strVorname = rs.getString("Vorname");
            strZuname = rs.getString("Zuname");

            Mitglied mitglied = new Mitglied(intPersID, strSTB, strDGR, strTitel, strVorname, strZuname);
            liMitglieder.add(mitglied);
        }

        connPool.releaseConnection(conn);
        return liMitglieder;
    }

    /**
     * Gibt eine Geburtstagsliste aller Mitarbeiter als LinkedList zurück.
     *
     * @return LinkedList
     * @param jahr
     * @throws IOException
     * @see MitgliedsGeburtstag
     * @see Mitglied
     * @see LinkedList
     */
    public LinkedList<MitgliedsGeburtstag> getGeburtstagsliste(int jahr) throws Exception
    {
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

        while (rs.next())
        {
            intPersID = rs.getInt("PersID");
            strSTB = rs.getString("STB");
            strDGR = rs.getString("DGR");
            strTitel = rs.getString("Titel");
            strVorname = rs.getString("Vorname");
            strZuname = rs.getString("Zuname");
            dateGeburtsdatum = new Date(rs.getDate("Geburtsdatum").getTime());

            Calendar calGeburtsdatum = new GregorianCalendar();
            Calendar current = new GregorianCalendar();

            calGeburtsdatum.setTime(dateGeburtsdatum);
            int intCurrentYear = current.get(Calendar.YEAR);
            intZielalter = intCurrentYear - calGeburtsdatum.get(Calendar.YEAR);

            calGeburtsdatum.set(Calendar.YEAR, intCurrentYear);

            if (current.after(calGeburtsdatum))
            {
                intZielalter++;
                calGeburtsdatum.add(Calendar.YEAR, 1);
            }

            MitgliedsGeburtstag mitgliedsGeb = new MitgliedsGeburtstag(intPersID, strSTB, strDGR, strTitel, strVorname, strZuname, false, dateGeburtsdatum, intZielalter);
            liMitgliedsGeburtstage.add(mitgliedsGeb);
        }
        connPool.releaseConnection(conn);
        return liMitgliedsGeburtstage;
    }

    /**
     * Gibt eine Dienstzeitliste aller Mitarbeiter als LinkedList zurück.
     *
     * @return LinkedList
     * @throws IOException
     * @see Mitglied
     * @see MitgliedsDienstzeit
     * @see LinkedList
     */
    public LinkedList<MitgliedsDienstzeit> getDienstzeitListe() throws Exception
    {
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

        while (rs.next())
        {
            if (rs.getDate("Datum_abgemeldet") == null)
            {
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

                if (Calendar.getInstance().get(Calendar.MONTH) < calEintrittsdatum.get(Calendar.MONTH))
                {
                    intDienstzeit--;
                }

                if ((Calendar.getInstance().get(Calendar.MONTH) == calEintrittsdatum.get(Calendar.MONTH)) && Calendar.getInstance().get(Calendar.DAY_OF_MONTH) < calEintrittsdatum.get(Calendar.DAY_OF_MONTH))
                {
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
     * Gibt eine Liste mit allen Adressen aller Mitarbeiter als LinkedList
     * zurück.
     *
     * @return LinkedList
     * @throws IOException
     * @see Mitglied
     * @see MitgliedsAdresse
     * @see LinkedList
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

    /**
     * !!!Alte Version!!! Gibt spezielle Informationen zu der Tätigkeit
     * "Kursbesuch an der FWZS" als LinkedList zurück. Diese Informationen sind
     * zum Beispiel Anzahl der Mitarbeiter in einem KursAlt.
     *
     * @return LinkedList
     * @throws IOException
     * @see KursAlt
     * @see LinkedList
     */
    public LinkedList<KursAlt> getKursstatistikAlt() throws Exception
    {
        LinkedList<KursAlt> liKurse = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "SELECT TOP 1000 stmkkurse.id_kurse \"KursID\""
                + ",stmkkurse.id_kursarten \"Kursarten\""
                + ",stmkkurse.lehrgangsnummer \"LGN\""
                + ",stmkkurse.kursbezeichnung \"Kursbezeichnung\""
                + ",stmkkurse.datum \"Datum\""
                + ",stmkkurse.id_instanzen_veranstalter \"InstanzVeranstalter\""
                + ",stmkkurse.id_instanzen_durchfuehrend \"InstanzDurchfuehrend\""
                + ",stmkkurse.kursstatus \"Kursstatus\""
                + ", COUNT(stmkkursmitglieder.id_kurse) \"Teilnehmer\""
                + "FROM FDISK.dbo.stmkkurse stmkkurse INNER JOIN FDISK.dbo.stmkkursemitglieder stmkkursmitglieder "
                + "ON (stmkkursmitglieder.id_kurse = stmkkurse.id_kurse) "
                + "WHERE stmkkurse.id_instanzen_veranstalter = 29885 "
                + "GROUP BY stmkkurse.id_kurse, stmkkurse.id_kursarten "
                + ",stmkkurse.lehrgangsnummer "
                + ",stmkkurse.kursbezeichnung "
                + ",stmkkurse.datum "
                + ",stmkkurse.id_instanzen_veranstalter "
                + ",stmkkurse.id_instanzen_durchfuehrend "
                + ",stmkkurse.kursstatus ";

        ResultSet rs = stat.executeQuery(sqlString);

        int intId_kurse;
        int intId_Kursarten;
        int intLehrgangsnummer;
        String strKursbezeichnung;
        Date dateDatum;
        int intId_instanzen_veranstalter;
        int intId_instanzen_durchfuehrend;
        String strKursstatus;
        int intAnzahlBesucher;

        while (rs.next())
        {

            intId_kurse = rs.getInt("KursID");
            intId_Kursarten = rs.getInt("Kursarten");
            intLehrgangsnummer = rs.getInt("LGN");
            strKursbezeichnung = rs.getString("Kursbezeichnung");
            dateDatum = new Date(rs.getDate("Datum").getTime());
            intId_instanzen_veranstalter = rs.getInt("InstanzVeranstalter");
            intId_instanzen_durchfuehrend = rs.getInt("InstanzDurchfuehrend");
            strKursstatus = rs.getString("Kursstatus");
            intAnzahlBesucher = rs.getInt("Teilnehmer");

            KursAlt kurs = new KursAlt(intId_kurse, intId_Kursarten, intLehrgangsnummer, strKursbezeichnung, dateDatum, intId_instanzen_veranstalter, intId_instanzen_durchfuehrend, strKursstatus, intAnzahlBesucher);
            liKurse.add(kurs);

        }
        connPool.releaseConnection(conn);
        return liKurse;
    }

    /**
     * Gibt spezielle Informationen zu der Tätigkeit "Kursbesuch an der FWZS"
     * als LinkedList zurück. Diese Informationen sind zum Beispiel Anzahl der
     * Mitarbeiter in einem KursAlt.
     *
     * Achtung: Im Moment werden Daten aller Kurse zurückgegeben (nicht nur
     * Kursbesuch an der FWZS)
     */
    public LinkedList<Kurs> getKursstatistik() throws Exception
    {
        LinkedList<Kurs> liKurse = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "SELECT t.id_stmktaetigkeitsberichte \"IdBerichte\","
                + "COUNT(m.zuname) \"Teilnehmer\","
                + "f.bezeichnung \"Bezeichnung\","
                + "f.km \"KM\""
                + ",t.instanznummer \"Instanznr\""
                + ",t.instanzname \"Instanzname\""
                + ",t.taetigkeitsart \"TArt\""
                + ",t.taetigkeitsunterart \"TUnterArt\""
                + ",t.nummer \"Nummer\""
                + ",t.beginn \"Beginn\""
                + ",t.ende \"Ende\""
                + " FROM FDISK.dbo.stmktaetigkeitsberichte t INNER JOIN FDISK.dbo.stmktaetigkeitsberichtemitglieder m"
                + " ON(t.id_stmktaetigkeitsberichte = m.id_berichte) INNER JOIN FDISK.dbo.stmktaetigkeitsberichtefahrzeuge f"
                + " ON(t.id_stmktaetigkeitsberichte = f.id_berichte)"
                // + " WHERE taetigkeitsart = 'Kursbesuch an der FWZS'"
                + " GROUP BY t.id_stmktaetigkeitsberichte,"
                + " f.bezeichnung,"
                + "f.km"
                + ",t.instanznummer"
                + ",t.instanzname"
                + ",t.taetigkeitsart"
                + ",t.taetigkeitsunterart"
                + ",t.nummer"
                + ",t.beginn"
                + ",t.ende";

        ResultSet rs = stat.executeQuery(sqlString);

        int intIdBerichte;
        int intTeilnehmer;
        String strBezeichnung;
        int intKm;
        int intInstanznummer;
        String strInstanzname;
        String strTaetigkeitsart;
        String strTaetigkeitsunterart;
        String strNummer;
        Date dateBeginn;
        Date dateEnde;

        while (rs.next())
        {

            intIdBerichte = rs.getInt("IdBerichte");
            intTeilnehmer = rs.getInt("Teilnehmer");
            strBezeichnung = rs.getString("Bezeichnung");
            intKm = rs.getInt("KM");
            intInstanznummer = rs.getInt("Instanznr");
            strInstanzname = rs.getString("Instanzname");
            strTaetigkeitsart = rs.getString("TArt");
            strTaetigkeitsunterart = rs.getString("TUnterArt");
            strNummer = rs.getString("Nummer");
            dateBeginn = rs.getDate("Beginn");
            dateEnde = rs.getDate("Ende");

            Kurs kurs = new Kurs(intIdBerichte, intTeilnehmer, strBezeichnung, intKm, intInstanznummer, strInstanzname, strTaetigkeitsart, strTaetigkeitsunterart, strNummer, dateBeginn, dateEnde);
            liKurse.add(kurs);

        }
        connPool.releaseConnection(conn);
        return liKurse;
    }

    /**
     * Gibt alle relevanten Daten (Fahrzeugtyp, Kennzeichen, Baujahr etc...) von
     * jedem Fahrzeug als LinkedList zurück.
     *
     * @return LinkedList
     * @throws Exception
     * @see Fahrzeug
     * @see LinkedList
     */
    public LinkedList<Fahrzeug> getFahrtenbuch() throws Exception
    {
        LinkedList<Fahrzeug> liFahrzeuge = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "SELECT TOP 1000 "
                + "kennzeichen \"Kennzeichen\" "
                + ",id_fahrzeuge \"Id_Fahrzeuge\" "
                + ",fahrzeugtyp \"Fahrzeugtyp\" "
                + ",taktischebezeichnung \"Taktische Bezeichnung\" "
                + ",bezeichnung \"Bezeichnung\" "
                + ",status \"Status\" "
                + ",baujahr \"Baujahr\" "
                + ",fahrzeugmarke \"Fahrzeugmarke\" "
                + ",aufbaufirma \"Aufbaufirma\""
                + ",instanznummer \"Instanzummer\" "
                + "FROM FDISK.dbo.stmkfahrzeuge "
                + "WHERE status = 'aktiv'";

        ResultSet rs = stat.executeQuery(sqlString);

        String strFahrzeugTyp;
        String strKennzeichen;
        int intBaujahr;
        String strAufbaufirma;
        String strTaktischeBezeichnung;
        int intId_fahrzeuge;
        String strBezeichnung;
        String strFahrzeugmarke;
        int intInstanznummer;

        while (rs.next())
        {
            strFahrzeugTyp = rs.getString("Fahrzeugtyp");
            strKennzeichen = rs.getString("Kennzeichen");
            intBaujahr = rs.getInt("Baujahr");
            strAufbaufirma = rs.getString("Aufbaufirma");
            strTaktischeBezeichnung = rs.getString("Taktische Bezeichnung");
            intId_fahrzeuge = rs.getInt("Id_Fahrzeuge");
            strBezeichnung = rs.getString("Bezeichnung");
            strFahrzeugmarke = rs.getString("Fahrzeugmarke");
            intInstanznummer = rs.getInt("Instanzummer");

            Fahrzeug fahrzeug = new Fahrzeug(strFahrzeugTyp, strKennzeichen, intBaujahr, strAufbaufirma, strTaktischeBezeichnung, intId_fahrzeuge, strBezeichnung, strFahrzeugmarke, intInstanznummer);
            liFahrzeuge.add(fahrzeug);
        }
        connPool.releaseConnection(conn);
        return liFahrzeuge;
    }

    /**
     * Gibt eine Liste der Erreichbarkeiten von jedem Mitarbeiter als LinkedList
     * zurück.
     *
     * @return LinkedList
     * @throws IOException
     * @see Mitglied
     * @see MitgliedsErreichbarkeit
     * @see LinkedList
     */
    public LinkedList<MitgliedsErreichbarkeit> getErreichbarkeitsliste() throws Exception
    {
        LinkedList<MitgliedsErreichbarkeit> liMitgliedsErreichbarkeiten = new LinkedList<>();

        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "SELECT DISTINCT TOP 1000 sm.id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\", se.erreichbarkeitsart \"Erreichbarkeitsart\", se.code \"Code\""
                + " FROM FDISK.dbo.stmkmitglieder sm INNER JOIN FDISK.dbo.stmkerreichbarkeiten se ON(sm.id_personen = se.id_personen)"
                + " ORDER BY sm.id_personen;";
        ResultSet rs = stat.executeQuery(sqlString);
        String strErreichbarkeitsart;
        String strCode;

        String strSTB;
        String strDGR;
        String strTitel;
        String strVorname;
        String strZuname;
        int intPersID = 0;
        int intLetztePersID = 0;

        LinkedList<Erreichbarkeit> liErreichbarkeiten = new LinkedList<>();
        while (rs.next())
        {

            intPersID = rs.getInt("PersID");
            strErreichbarkeitsart = rs.getString("Erreichbarkeitsart");

            strCode = rs.getString("Code");

            strSTB = rs.getString("STB");
            strDGR = rs.getString("DGR");
            strTitel = rs.getString("Titel");
            strVorname = rs.getString("Vorname");
            strZuname = rs.getString("Zuname");

            if (intPersID == intLetztePersID)
            {
                if (!liErreichbarkeiten.contains(new Erreichbarkeit(strErreichbarkeitsart, strCode, intPersID)))
                {
                    liErreichbarkeiten.add(new Erreichbarkeit(strErreichbarkeitsart, strCode, intPersID));
                }
            } else
            {
                if (liMitgliedsErreichbarkeiten.size() > 0)
                {
                    liMitgliedsErreichbarkeiten.getLast().setLiErreichbarkeiten(liErreichbarkeiten);
                    liErreichbarkeiten = new LinkedList<Erreichbarkeit>();
                }
                intLetztePersID = intPersID;
                liMitgliedsErreichbarkeiten.add(new MitgliedsErreichbarkeit(false, intPersID, strSTB, strDGR, strTitel, strVorname, strZuname));
                liErreichbarkeiten.add(new Erreichbarkeit(strErreichbarkeitsart, strCode, intPersID));
            }

        }

        connPool.releaseConnection(conn);
        return liMitgliedsErreichbarkeiten;
    }

    /**
     * Gibt alle Mitglieder für Übungsberichte als LinkedList zurück
     *
     * @return
     * @throws Exception
     */
    /**
     * Gibt alle Mitglieder für Tätigkeitsberichte als LinkedList zurück
     *
     * @return
     * @throws Exception
     */
    public LinkedList<LeerberichtMitglied> getLeerberichtMitglied() throws Exception
    {
        LinkedList<LeerberichtMitglied> liLeerberichtMitglieder = new LinkedList<>();

        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "";

        sqlString = "SELECT DISTINCT TOP 1000 id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\" "
                + "FROM FDISK.dbo.stmkmitglieder";

        ResultSet rs = stat.executeQuery(sqlString);

        String strSTB;
        String strDGR;
        String strTitel;
        String strVorname;
        String strZuname;
        int intPersID;

        while (rs.next())
        {
            intPersID = rs.getInt("PersID");
            strSTB = rs.getString("STB");
            strDGR = rs.getString("DGR");
            strTitel = rs.getString("Titel");
            strVorname = rs.getString("Vorname");
            strZuname = rs.getString("Zuname");

            LeerberichtMitglied mitglied = new LeerberichtMitglied(intPersID, strSTB, strDGR, strTitel, strVorname, strZuname);
            liLeerberichtMitglieder.add(mitglied);
        }

        connPool.releaseConnection(conn);
        return liLeerberichtMitglieder;
    }

    public Date getEinsatzberichtEinsatzzeit() throws SQLException, Exception
    {
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "SELECT TOP 1000 convert(char, einsatzzeit_bis - einsatzzeit_von, 114) \"Einsatzzeit\""
                + " FROM [FDISK].[dbo].[stmkeinsatzberichtemitglieder] WHERE id_mitgliedschaften = 219782"; //WHERE id_mitgliedschaften = "+mitglied.getIntIdMitgliedschaften();

        ResultSet rs = stat.executeQuery(sqlString);

        Date einsatzzeit = null;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");

        while (rs.next())
        {
            einsatzzeit = sdf.parse(rs.getString("Einsatzzeit"));
        }

        connPool.releaseConnection(conn);
        return einsatzzeit;
    }

    public LinkedList<LeerberichtFahrzeug> getLeerberichtFahrzeug() throws Exception
    {
        LinkedList<LeerberichtFahrzeug> liFahrzeuge = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "SELECT TOP 1000 "
                + "kennzeichen \"Kennzeichen\" "
                + ",id_fahrzeuge \"Id_Fahrzeuge\" "
                + ",fahrzeugtyp \"Fahrzeugtyp\" "
                + ",taktischebezeichnung \"Taktische Bezeichnung\" "
                + ",bezeichnung \"Bezeichnung\" "
                + ",status \"Status\" "
                + ",baujahr \"Baujahr\" "
                + ",fahrzeugmarke \"Fahrzeugmarke\" "
                + ",aufbaufirma \"Aufbaufirma\""
                + ",instanznummer \"Instanzummer\" "
                + "FROM FDISK.dbo.stmkfahrzeuge "
                + "WHERE status = 'aktiv'";

        ResultSet rs = stat.executeQuery(sqlString);

        String strFahrzeugTyp;
        String strKennzeichen;
        int intBaujahr;
        String strAufbaufirma;
        String strTaktischeBezeichnung;
        int intId_fahrzeuge;
        String strBezeichnung;
        String strFahrzeugmarke;
        int intInstanznummer;

        while (rs.next())
        {
            strFahrzeugTyp = rs.getString("Fahrzeugtyp");
            strKennzeichen = rs.getString("Kennzeichen");
            intBaujahr = rs.getInt("Baujahr");
            strAufbaufirma = rs.getString("Aufbaufirma");
            strTaktischeBezeichnung = rs.getString("Taktische Bezeichnung");
            intId_fahrzeuge = rs.getInt("Id_Fahrzeuge");
            strBezeichnung = rs.getString("Bezeichnung");
            strFahrzeugmarke = rs.getString("Fahrzeugmarke");
            intInstanznummer = rs.getInt("Instanzummer");

            LeerberichtFahrzeug fahrzeug = new LeerberichtFahrzeug(strFahrzeugTyp, strKennzeichen, intBaujahr, strAufbaufirma, strTaktischeBezeichnung, intId_fahrzeuge, strBezeichnung, strFahrzeugmarke, intInstanznummer);
            liFahrzeuge.add(fahrzeug);
        }
        connPool.releaseConnection(conn);
        return liFahrzeuge;
    }

    /**
     * *******************************************************************************
     */
    /*                                                                                *
     /*                       DYNAMISCHER BERICHTGENERATOR                             *
     /*                                                                                *
     /**********************************************************************************/
    /**
     * Ruft die richtige Methode für einen Typ auf
     *
     * @param typ
     * @throws Exception
     */
    public void getMethodeFuerTyp(String typ) throws Exception
    {
        if (typ.toUpperCase().equals("GRUPPE"))
        {
            getFilterFuerGruppe(typ);
        } else if (typ.toUpperCase().equals("KURSBEZEICHNUNG") || typ.toUpperCase().equals("KURSDATUM"))
        {
            if (typ.toUpperCase().equals("KURSDATUM"))
            {
                typ = "DATUM";
            }
            getFilterFuerKurs(typ);
        } else if (typ.toUpperCase().equals("FUNKTIONSINSTANZ") || typ.toUpperCase().equals("FUNKTIONSBEZEICHNUNG") || typ.toUpperCase().equals("FUNKTION VON") || typ.toUpperCase().equals("FUNKTION BIS"))
        {
            switch (typ.toUpperCase())
            {
                case "FUNKTIONSBEZEICHNUNG":
                    typ = "BEZEICHNUNG";
                    break;
                case "FUNKTIONSINSTANZ":
                    typ = "ID_INSTANZTYPEN";
                    break;
                case "FUNKTION VON":
                    typ = "DATUM_VON";
                    break;
                default:
                    typ = "DATUM_BIS";
                    break;
            }
            getFilterFuerFunktion(typ);
        } else if (typ.toUpperCase().equals("ALTER"))
        {
            getFilterFuerAlter(typ);
        } else if (typ.toUpperCase().equals("STATUS"))
        {
            getFilterFuerStatus(typ);
        } else if (typ.toUpperCase().equals("ERREICHBARKEITSART") || typ.toUpperCase().equals("ERREICHBARKEIT"))
        {
            if (typ.toUpperCase().equals("ERREICHBARKEIT"))
            {
                typ = "CODE";
            }
            getFilterFuerErreichbarkeit(typ);
        } else if (typ.toUpperCase().equals("ANREDE"))
        {
            getFilterFuerAnrede(typ);
        } else if (typ.toUpperCase().equals("AUSZEICHNUNGSART") || typ.toUpperCase().equals("AUSZEICHNUNGSSTUFE") || typ.toUpperCase().equals("AUSZEICHNUNGSDATUM"))
        {
            if (typ.toUpperCase().equals("AUSZEICHNUNGSDATUM"))
            {
                typ = "VERLEIHUNGSDATUM";
            }
            getFilterFuerAuszeichnung(typ);
        } else if (typ.toUpperCase().equals("LEISTUNGSABZEICHENBEZEICHNUNG") || typ.toUpperCase().equals("LEISTUNGSABZEICHENSTUFE") || typ.toUpperCase().equals("LEISTUNGSABZEICHENDATUM"))
        {
            switch (typ.toUpperCase())
            {
                case "LEISTUNGSABZEICHENBEZEICHNUNG":
                    typ = "BEZEICHNUNG";
                    break;
                case "LEISTUNGSABZEICHENSTUFE":
                    typ = "STUFE";
                    break;
                case "LEISTUNGSABZEICHENDATUM":
                    typ = "DATUM";
                    break;
            }
            getFilterFuerLeistungsabzeichen(typ);
        } else
        {
            if (typ.toUpperCase().equals("ISCO-BERUF"))
            {
                typ = "BERUF";
            }

            getFilterFuerTyp(typ);
        }
    }

    /**
     * Sucht den passenden Filter für einen Typ
     *
     * @param typ
     * @return
     * @throws Exception
     */
    public HashMap<String, LinkedList<String>> getFilterFuerTyp(String typ) throws Exception
    {
        HashMap<String, LinkedList<String>> hmFilter = new HashMap<>();
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "SELECT DISTINCT " + typ + "\"Typ\""
                + " FROM FDISK.dbo.stmkmitglieder";
        ResultSet rs = stat.executeQuery(sqlString);

        while (rs.next())
        {
            String strFilter;
            if (rs.getString("Typ") == null)
            {
                strFilter = "Unbekannt";
                continue;
            }
            strFilter = rs.getString("Typ");

            if (strFilter.equals("") || strFilter.equals(" "))
            {
                strFilter = "Unbekannt";
            }
            if (typ.toUpperCase().equals("BERUF"))
            {
                typ = "ISCO-BERUF";
            }
            liFilter.add(strFilter);
        }

        hmFilter.put(typ, liFilter);
        connPool.releaseConnection(conn);

        for (Map.Entry e : hmFilter.entrySet())
        {
            System.out.println(e.getKey() + "---" + e.getValue());
        }

        return hmFilter;
    }

    /**
     * Sucht den passenden Filter für den Typ "Anrede"
     *
     * @param typ
     * @return
     * @throws Exception
     */
    public HashMap<String, LinkedList<String>> getFilterFuerAnrede(String typ) throws Exception
    {
        HashMap<String, LinkedList<String>> hmFilter = new HashMap<>();
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "SELECT DISTINCT Geschlecht"
                + " FROM FDISK.dbo.stmkmitglieder";

        ResultSet rs = stat.executeQuery(sqlString);

        while (rs.next())
        {
            String strFilter = rs.getString("Geschlecht");

            switch (strFilter)
            {
                case "":
                case " ":
                    strFilter = "Unbekannt";
                    break;
                case "m":
                    strFilter = "Herr";
                    break;
                case "w":
                    strFilter = "Frau";
                    break;
            }

            liFilter.add(strFilter);
        }

        hmFilter.put(typ, liFilter);
        connPool.releaseConnection(conn);

        for (Map.Entry e : hmFilter.entrySet())
        {
            System.out.println(e.getKey() + "---" + e.getValue());
        }

        return hmFilter;
    }

    /**
     * Sucht den passenden Filter für den Typ "Gruppe"
     *
     * @param typ
     * @return
     * @throws Exception
     */
    public HashMap<String, LinkedList<String>> getFilterFuerGruppe(String typ) throws Exception
    {
        HashMap<String, LinkedList<String>> hmFilter = new HashMap<>();
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "SELECT DISTINCT Bezeichnung"
                + " FROM FDISK.dbo.tbl_login_gruppe";

        ResultSet rs = stat.executeQuery(sqlString);

        while (rs.next())
        {
            String strFilter = rs.getString("Bezeichnung");

            if (strFilter.equals("") || strFilter.equals(" "))
            {
                strFilter = "Unbekannt";
            }

            liFilter.add(strFilter);
        }

        hmFilter.put(typ, liFilter);
        connPool.releaseConnection(conn);

        for (Map.Entry e : hmFilter.entrySet())
        {
            System.out.println(e.getKey() + "---" + e.getValue());
        }

        return hmFilter;
    }

    /**
     * Sucht den passenden Filter für Kurse
     *
     * @param typ
     * @return
     * @throws Exception
     */
    public HashMap<String, LinkedList<String>> getFilterFuerKurs(String typ) throws Exception
    {
        HashMap<String, LinkedList<String>> hmFilter = new HashMap<>();
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "SELECT DISTINCT " + typ + "\"Typ\""
                + " FROM FDISK.dbo.stmkkurse";
        ResultSet rs = stat.executeQuery(sqlString);

        while (rs.next())
        {
            String strFilter;
            if (rs.getString("Typ") == null)
            {
                strFilter = "Unbekannt";
                continue;
            }
            strFilter = rs.getString("Typ");

            if (strFilter.equals("") || strFilter.equals(" "))
            {
                strFilter = "Unbekannt";
            }
            liFilter.add(strFilter);
        }

        if (typ.toUpperCase().equals("DATUM"))
        {
            typ = "KURSDATUM";
        }
        hmFilter.put(typ, liFilter);
        connPool.releaseConnection(conn);

        for (Map.Entry e : hmFilter.entrySet())
        {
            System.out.println(e.getKey() + "---" + e.getValue());
        }

        return hmFilter;
    }

    /**
     * Sucht den passenden Filter für Kurse
     *
     * @param typ
     * @return
     * @throws Exception
     */
    public HashMap<String, LinkedList<String>> getFilterFuerFunktion(String typ) throws Exception
    {
        HashMap<String, LinkedList<String>> hmFilter = new HashMap<>();
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "SELECT DISTINCT " + typ + "\"Typ\""
                + " FROM FDISK.dbo.stmkfunktionen funktionen INNER JOIN"
                + " FDISK.dbo.stmkfunktionenmitglieder mitglieder"
                + " ON(mitglieder.id_funktionen = funktionen.id_funktionen)";
        ResultSet rs = stat.executeQuery(sqlString);

        while (rs.next())
        {
            String strFilter;
            if (rs.getString("Typ") == null)
            {
                strFilter = "Unbekannt";
                continue;
            }
            strFilter = rs.getString("Typ");

            if (strFilter.equals("") || strFilter.equals(" "))
            {
                strFilter = "Unbekannt";
            }
            liFilter.add(strFilter);
        }

        switch (typ.toUpperCase())
        {
            case "BEZEICHNUNG":
                typ = "FUNKTIONSBEZEICHNUNG";
                break;
            case "ID_INSTANZTYPEN":
                typ = "FUNKTIONSINSTANZ";
                break;
            case "DATUM_VON":
                typ = "FUNKTION VON";
                break;
            case "DATUM_BIS":
                typ = "FUNKTION BIS";
                break;
        }

        hmFilter.put(typ, liFilter);
        connPool.releaseConnection(conn);

        for (Map.Entry e : hmFilter.entrySet())
        {
            System.out.println(e.getKey() + "---" + e.getValue());
        }

        return hmFilter;
    }

    /**
     * Gibt alle verfügbaren Filter für Alter zurück (Also alle Alterszahlen,
     * die zurzeit aktuell sind.)
     *
     * @param typ
     * @return
     * @throws Exception
     */
    public HashMap<String, LinkedList<String>> getFilterFuerAlter(String typ) throws Exception
    {
        HashMap<String, LinkedList<String>> hmFilter = new HashMap<>();
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "SELECT DISTINCT geburtsdatum \"Typ\""
                + " FROM FDISK.dbo.stmkmitglieder";
        ResultSet rs = stat.executeQuery(sqlString);

        while (rs.next())
        {
            String strFilter;
            if (rs.getDate("Typ") != null)
            {
                Date dateGeburtsdatum = new Date(rs.getDate("Typ").getTime());

                Calendar calGeburtsdatum = Calendar.getInstance();
                Calendar calHeute = Calendar.getInstance();
                calGeburtsdatum.setTime(dateGeburtsdatum);
                int intAlter = Calendar.getInstance().get(Calendar.YEAR) - calGeburtsdatum.get(Calendar.YEAR);

                if ((calGeburtsdatum.get(Calendar.DAY_OF_YEAR) - calHeute.get(Calendar.DAY_OF_YEAR) > 3)
                        || (calGeburtsdatum.get(Calendar.MONTH) > calHeute.get(Calendar.MONTH)))
                {
                    intAlter--;

                } else if ((calGeburtsdatum.get(Calendar.MONTH) == calHeute.get(Calendar.MONTH))
                        && (calGeburtsdatum.get(Calendar.DAY_OF_MONTH) > calHeute.get(Calendar.DAY_OF_MONTH)))
                {
                    intAlter--;
                }

                if (intAlter <= 0)
                {
                    strFilter = "Unbekannt";
                } else
                {
                    strFilter = intAlter + "";
                }
            } else
            {
                strFilter = "Unbekannt";
            }

            if (!liFilter.contains(strFilter))
            {
                liFilter.add(strFilter);
            }
        }

        Collections.sort(liFilter, new Comparator<String>()
        {

            @Override
            public int compare(String o1, String o2)
            {
                try
                {
                    return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
                } catch (Exception e)
                {
                    return -1;
                }

            }
        });

        if (liFilter.contains("Unbekannt"))
        {
            liFilter.remove("Unbekannt");
            liFilter.addFirst("Unbekannt");
        }

        hmFilter.put(typ, liFilter);
        connPool.releaseConnection(conn);
        return hmFilter;
    }

    /**
     * Gibt die passenden Filter für die Status zurück
     *
     * @param typ
     * @return
     * @throws Exception
     */
    public HashMap<String, LinkedList<String>> getFilterFuerStatus(String typ) throws Exception
    {
        HashMap<String, LinkedList<String>> hmFilter = new HashMap<>();
        LinkedList<String> liFilter = new LinkedList<>();
        liFilter.add("Jugend");
        liFilter.add("Aktiv");
        liFilter.add("Reserve");
        liFilter.add("Ehrenmitglied");
        liFilter.add("Unbekannt");

        hmFilter.put(typ, liFilter);
        return hmFilter;
    }

    /**
     * Sucht den passenden Filter für Erreichbarkeiten
     *
     * @param typ
     * @return
     * @throws Exception
     */
    public HashMap<String, LinkedList<String>> getFilterFuerErreichbarkeit(String typ) throws Exception
    {
        HashMap<String, LinkedList<String>> hmFilter = new HashMap<>();
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "SELECT DISTINCT " + typ + "\"Typ\""
                + " FROM FDISK.dbo.stmkerreichbarkeiten";
        ResultSet rs = stat.executeQuery(sqlString);

        while (rs.next())
        {
            String strFilter;
            if (rs.getString("Typ") == null)
            {
                strFilter = "Unbekannt";
                continue;
            }
            strFilter = rs.getString("Typ");

            if (strFilter.equals("") || strFilter.equals(" ") || strFilter.equals("-") || strFilter.equals("--"))
            {
                strFilter = "Unbekannt";
            }

            liFilter.add(strFilter.trim());
        }

        if (typ.toUpperCase().equals("ERREICHBARKEIT"))
        {
            typ = "CODE";
        }

        hmFilter.put(typ, liFilter);
        connPool.releaseConnection(conn);

        for (Map.Entry e : hmFilter.entrySet())
        {
            System.out.println(e.getKey() + "---" + e.getValue());
        }

        return hmFilter;
    }

    /**
     * Gibt die passenden Filter für Auszeichnungen zurück
     *
     * @param typ
     * @return
     * @throws Exception
     */
    public HashMap<String, LinkedList<String>> getFilterFuerAuszeichnung(String typ) throws Exception
    {
        HashMap<String, LinkedList<String>> hmFilter = new HashMap<>();
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "SELECT DISTINCT " + typ + "\"Typ\""
                + " FROM FDISK.dbo.stmkauszeichnungenmitglieder mitglieder INNER JOIN"
                + " FDISK.dbo.stmkauszeichnungen auszeichnungen"
                + " ON(mitglieder.id_auszeichnungen = auszeichnungen.id_auszeichnungen)";
        ResultSet rs = stat.executeQuery(sqlString);

        while (rs.next())
        {
            String strFilter;
            if (rs.getString("Typ") == null)
            {
                strFilter = "Unbekannt";
                continue;
            }
            strFilter = rs.getString("Typ");

            if (strFilter.equals("") || strFilter.equals(" "))
            {
                strFilter = "Unbekannt";
            }
            liFilter.add(strFilter);
        }

        if (typ.toUpperCase().equals("VERLEIHUNGSDATUM"))
        {
            typ = "DATUM";
        }

        hmFilter.put(typ, liFilter);
        connPool.releaseConnection(conn);

        for (Map.Entry e : hmFilter.entrySet())
        {
            System.out.println(e.getKey() + "---" + e.getValue());
        }

        return hmFilter;
    }

    /**
     * Gibt die passenden Filter für Leistungsabzeichen zurück.
     *
     * @param typ
     * @return
     * @throws Exception
     */
    public HashMap<String, LinkedList<String>> getFilterFuerLeistungsabzeichen(String typ) throws Exception
    {
        HashMap<String, LinkedList<String>> hmFilter = new HashMap<>();
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "SELECT DISTINCT " + typ + "\"Typ\""
                + " FROM FDISK.dbo.stmkleistungsabzeichen leistungsabzeichen INNER JOIN"
                + " FDISK.dbo.stmkleistungsabzeichenmitglieder mitglieder"
                + " ON(mitglieder.id_leistungsabzeichenstufe = leistungsabzeichen.id_leistungsabzeichenstufe)";
        ResultSet rs = stat.executeQuery(sqlString);

        while (rs.next())
        {
            String strFilter;
            if (rs.getString("Typ") == null)
            {
                strFilter = "Unbekannt";
                continue;
            }
            strFilter = rs.getString("Typ");

            if (strFilter.equals("") || strFilter.equals(" "))
            {
                strFilter = "Unbekannt";
            }
            liFilter.add(strFilter);
        }

        if (typ.toUpperCase().equals("DATUM"))
        {
            typ = "LEISTUNGSABZEICHENDATUM";
        }

        hmFilter.put(typ, liFilter);
        connPool.releaseConnection(conn);

        for (Map.Entry e : hmFilter.entrySet())
        {
            System.out.println(e.getKey() + "---" + e.getValue());
        }

        return hmFilter;
    }

    public String getDynamischenBerichtMitUnd(String strEingabe[]) throws Exception
    {
        String strHtml = "";
        LinkedList<String> liSpaltenUeberschriften = new LinkedList<>();
        String strSpaltenUeberschrift;

        for (int i = 0; i < strEingabe.length; i += 6)
        {
            strSpaltenUeberschrift = strEingabe[1 + i];
            if (strSpaltenUeberschrift.toUpperCase().equals("ANREDE"))
            {
                strSpaltenUeberschrift = "geschlecht";
            } else if (strSpaltenUeberschrift.toUpperCase().equals("ALTER"))
            {
                strSpaltenUeberschrift = "geburtsdatum";
            } else if (strSpaltenUeberschrift.toUpperCase().equals("STAATSBÜRGERSCHAFT"))
            {
                strSpaltenUeberschrift = "staatsbuergerschaft";
            } else if (strSpaltenUeberschrift.toUpperCase().equals("ISCO-BERUF"))
            {
                strSpaltenUeberschrift = "beruf";
            } else if (strSpaltenUeberschrift.toUpperCase().equals("STATUS"))
            {
                strSpaltenUeberschrift = strEingabe[3 + i];
            } else if (strSpaltenUeberschrift.toUpperCase().equals("VORDIENSTZEIT IN JAHREN"))
            {
                strSpaltenUeberschrift = "vordienstzeit";
            }

            liSpaltenUeberschriften.add(strSpaltenUeberschrift);
        }

        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();
        String sqlString = "SELECT ";

        //vorläufig, noch nicht final
        if (liSpaltenUeberschriften.contains("Untersuchungen"))
        {

        } else if (liSpaltenUeberschriften.contains("Leistungsabzeichen"))
        {

        } else if (liSpaltenUeberschriften.contains("Kurse"))
        {

        } else if (liSpaltenUeberschriften.contains("Funktionen"))
        {

        } else if (liSpaltenUeberschriften.contains("Gesetzliche Fahrgenehmigungen"))
        {

        } else if (liSpaltenUeberschriften.contains("Auszeichnungen"))
        {

        } else if (liSpaltenUeberschriften.contains("Erreichbarkeiten"))
        {

        } else if (liSpaltenUeberschriften.contains("Adresse"))
        {

        } else
        {
            for (String titel : liSpaltenUeberschriften)
            {
                sqlString += titel.toUpperCase() + ",";
            }

            //damit der letzte Beistrich gelöscht wird (sonst Exception im SQL)
            sqlString = sqlString.substring(0, sqlString.lastIndexOf(",")) + " ";
            sqlString += "FROM FDISK.dbo.stmkmitglieder";
            System.out.println(sqlString);
        }

        //Um die ColumnNames und ColumnTypes aus dem spezifischen Statement auszulesen
        //dynamisch, damit man nicht 27000 ifs hat
        ResultSet rs = stat.executeQuery(sqlString);
        HashMap<String, String> haNamesTypes = new HashMap<>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int cols = rsmd.getColumnCount();

        for (int i = 1; i <= cols; i++)
        {
            String strColName = rsmd.getColumnName(i);
            String strColType = rsmd.getColumnTypeName(i);
            
            haNamesTypes.put(strColName, strColType);
        }

        //Ausgabe zusammenbasteln
        strHtml += "<html><table><tr>";
        for (String str : liSpaltenUeberschriften)
        {
            strHtml += "<td>" + str + "</td>";
        }
       
       
        

        //Ende Ausgabe zusammenbasteln
        
        /*
         In der HashMap steht jetzt folgendes: (zum Beispiel)
         GEBURTSDATUM = datetime
         JUGEND = bit
         VORDIENSTZEIT = bit
         */
        
        //Irgendwas passt bei der Reihenfolge net bei der Ausgabe (Ausgabe geht grundsätzlich)
        Iterator it = haNamesTypes.entrySet().iterator();

        while (rs.next())
        {
            String strName;
            String strType;
            boolean boBoolean;
            String strString;
            Date dateDate;
            Long loLong;

            while (it.hasNext())
            {
                strHtml += "<tr>";
                Map.Entry pair = (Map.Entry) it.next();
                strName = pair.getKey().toString();
                System.out.println("strName: "+strName);
                strType = pair.getValue().toString();
                System.out.println("strType: "+strType);

                if (strType.equals("bit"))
                {
                    boBoolean = rs.getBoolean(strName);
                    strHtml += "<td>" + boBoolean + "</td>";
                } else if (strType.equals("datetime"))
                {
                    dateDate = rs.getDate(strName);
                    strHtml += "<td>" + dateDate + "</td>";

                } else if (strType.equals("varchar"))
                {
                    strString = rs.getString(strName);
                    strHtml += "<td>" + strString + "</td>";
                }
                else if (strType.equals("bigint"))
                {
                    loLong = rs.getLong(strName);
                    strHtml += "<td>" + loLong + "</td>";
                }
                strHtml += "</tr>";
            }
            
        }

        connPool.releaseConnection(conn);

        strHtml += "</table></html>";
        System.out.println("htmlString: " + strHtml);

        return strHtml;
    }

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        try
        {
            theInstance = DB_Access.getInstance();
        } catch (ClassNotFoundException ex)
        {
            Logger.getLogger(DB_Access.class.getName()).log(Level.SEVERE, null, ex);
        }

        HashMap<String, LinkedList<String>> hm = new HashMap<>();
        try
        {
//            LinkedList<Berechtigung> lili = theInstance.getBerechtigungen(3566);
//            System.out.println("zweite Berechtigung: " + lili.get(1).getIntIDGruppe());
//            System.out.println("erste Berechtigung: " + lili.get(0).getIntIDGruppe());
//            for (int i = 0; i < lili.size(); i++)
//            {
//                System.out.println("Berechtigung: " + lili.get(i).getStrBerechtigung());
//            }
//            System.out.println("Abschnittsnummer: " + theInstance.getAbschnittsnummerForFubwehr("50012"));
//            System.out.println("\n\n\n\n");
//            System.out.println("****************LISTE****************");
//            LinkedList<Mitglied> liliMitglieder = theInstance.getEinfacheMitgliederliste(3566, lili.get(1).getIntIDGruppe());
//            int counter = 0;
//            for (Mitglied mitglied : liliMitglieder)
//            {
//                System.out.println(mitglied.toString());
//                counter++;
//            }
//            System.out.println("Counter: " + counter);
//            for (int i = 0; i < liliMitglieder.size(); i++) {
//                System.out.println(liliMitglieder.get(i).toString());
//            }
//            lili = theInstance.getLeerberichtFahrzeug();
//            for (LeerberichtFahrzeug k : lili)
//            {
//                System.out.println(k.toString());
//            }
//            LinkedList<Mitglied> li = theInstance.getEinfacheMitgliederliste(3566, 15);
//            for (Mitglied li1 : li)
//            {
//                System.out.println(li1.getStrVorname() + "-" + li1.getStrZuname());
//            }

            String[] dynamisch =
            {
                "(", "id_personen", "=", "15", "", "", "(", "Vordienstzeit in Jahren", "", "", "", "", "(", "Status", "", "Jugend", "", ""
            };
            String html = theInstance.getDynamischenBerichtMitUnd(dynamisch);

        } catch (Exception ex)
        {
            Logger.getLogger(DB_Access.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
