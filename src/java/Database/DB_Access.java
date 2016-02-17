/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import Enum.EnFilterStatements;
import Beans.Abschnitt;
import Beans.Berechtigung;
import Beans.Bericht;
import Beans.Bezirk;
import Beans.Einsatzbericht;
import Beans.Erreichbarkeit;
import Beans.Fahrzeug;
import Beans.Feuerwehr;
import Beans.Kurs;
import Beans.Kurstaetigkeit;
import Beans.LeerberichtFahrzeug;
import Beans.LoginMitglied;
import Beans.Mitglied;
import Beans.MitgliedsAdresse;
import Beans.MitgliedsDienstzeit;
import Beans.MitgliedsErreichbarkeit;
import Beans.MitgliedsGeburtstag;
import Beans.LeerberichtMitglied;
import Beans.MitgliedsStunden;
import Beans.Taetigkeitsbericht;
import Beans.Uebungsbericht;
import Enum.EnStaticStatements;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
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

    private DB_ConnectionPool connPool;
    private static DB_Access theInstance = null;
    private HashMap<String, String> haNamesTypes = new HashMap<>();
    private boolean boAnrede = false;

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

    public String formatiereAusgabe(String strFormat)
    {
        if (strFormat == null || strFormat.equals("") || strFormat.isEmpty() || strFormat.equals(" "))
        {
            return "";
        }
        String[] strFormatTeile = strFormat.split("(?<= )|(?<=\\/)|(?<=-)|(?<=,)");
        StringBuilder strNeuesFormat = new StringBuilder("");

        if (strFormatTeile.length > 1)
        {
            for (String word : strFormatTeile)
            {

                if (word.length() > 1)
                {

                    if (word.equals("bis") || word.equals("zum") || word.equals("von")
                            || word.equals("bei") || word.equals("und")
                            || word.equals("bis") || word.equals("zum")
                            || word.equals("beim") || word.equals("bei")
                            || word.equals("und") || word.equals("an")
                            || word.equals("der"))
                    {
                        strNeuesFormat.append(word.toLowerCase());
                    } else if (word.equals("FWZS"))
                    {
                        strNeuesFormat.append(word.toUpperCase());
                    } else
                    {
                        strNeuesFormat.append(word.substring(0, 1).toUpperCase());
                        strNeuesFormat.append(word.substring(1).toLowerCase());
                    }

                } else
                {
                    strNeuesFormat.append(word);
                }

            }
        } else if (strFormatTeile.length == 1)
        {
            if (strFormat.length() > 1)
            {
                strNeuesFormat.append(strFormat.substring(0, 1).toUpperCase());
                strNeuesFormat.append(strFormat.substring(1).toLowerCase());
            } else
            {
                strNeuesFormat.append(strFormat);
            }

        }

        System.out.println(strNeuesFormat.toString());
        return strNeuesFormat.toString();
    }

    /**
     * *******************************************************************************
     */
    /*                                                                                *
     /*                       STATISCHER BERICHTGENERATOR                              *
     /*                                                                                *
     /**********************************************************************************/
    public LinkedList<Berechtigung> getBerechtigungen(int intUserID) throws Exception
    {
        System.out.println(intUserID);
        LinkedList<Berechtigung> liBerechtigungen = new LinkedList<>();
        LinkedList<LoginMitglied> liLoginBerechtigung = new LinkedList<>();
        liLoginBerechtigung = getLoginBerechtigung(intUserID);
        String fubwehr = getFubwehrForUserID(intUserID);
        String feuerwehrname = getNameFuerFubwehr(fubwehr);

        String abschnitt = getAbschnittsnameFuerFubwehr(fubwehr);
        String bereich = getBereichsnameFuerFubwehr(fubwehr);
        String strBerechtigung = "";

        if (liLoginBerechtigung.isEmpty())
        {
            strBerechtigung = "Mitglied" + " - " + feuerwehrname;
            Berechtigung berechtigung = new Berechtigung(strBerechtigung, 0, fubwehr, getAbschnittsnummerForFubwehr(fubwehr), getBereichsnummerFuerFubwehr(fubwehr), intUserID);
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

                Berechtigung berechtigung = new Berechtigung(strBerechtigung, loginMitglied.getIntIDGruppe(), fubwehr, getAbschnittsnummerForFubwehr(fubwehr), getBereichsnummerFuerFubwehr(fubwehr), intUserID);
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
        PreparedStatement prepStat = conn.prepareStatement(EnStaticStatements.getUserID.toString());
        prepStat.setString(1, strUsername);
        prepStat.setString(2, strPasswort);

        ResultSet rs = prepStat.executeQuery();

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
     * Verbindung der Methode getUserID und getLoginBerechtigung damit Frontend
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
        PreparedStatement prepStat = conn.prepareStatement(EnStaticStatements.getLoginBerechtigung.toString());
        prepStat.setInt(1, intUserID);

        ResultSet rs = prepStat.executeQuery();

        while (rs.next())
        {
            String strFubwehr = rs.getString("Fubwehr");
            int intIDGruppe = rs.getInt("IDGruppe");
            String strBezeichnung = rs.getString("Bezeichnung");
            if (intIDGruppe == 1 || intIDGruppe == 5 || intIDGruppe == 9 || intIDGruppe == 15)
            {
                LoginMitglied lm = new LoginMitglied(intUserID, strFubwehr, intIDGruppe, strBezeichnung);
                liMitglieder.add(lm);
            }
        }

        connPool.releaseConnection(conn);
        return liMitglieder;
    }

    public Bezirk getBezirk(int bereichnummer) throws Exception
    {
        LinkedList<Integer> liAbschnittnummern;
        liAbschnittnummern = getAbschnittNummernFuerBereich(bereichnummer);
        LinkedList<Abschnitt> liAbschnitte = new LinkedList<>();
        for (Integer abschnittnummer : liAbschnittnummern)
        {
            liAbschnitte.add(getAbschnitt(abschnittnummer));
        }
        Bezirk bezirk = new Bezirk(getBereichsnameFuerBereichnnummer(bereichnummer), bereichnummer, liAbschnitte);
        return bezirk;
    }

    public Abschnitt getAbschnitt(int abschnittnummer) throws Exception
    {
        LinkedList<String> liFeuerwehrnummern;
        liFeuerwehrnummern = getFubwehrNummernFuerAbschnitt(abschnittnummer);
        LinkedList<Feuerwehr> liFeuerwehren = new LinkedList<>();
        for (String feuerwehrnummer : liFeuerwehrnummern)
        {
            liFeuerwehren.add(getFeuerwehr(feuerwehrnummer));
        }
        Abschnitt abschnitt = new Abschnitt(getAbschnittsnameFuerAbschnittsnummer(abschnittnummer), abschnittnummer, liFeuerwehren);
        return abschnitt;
    }

    public Feuerwehr getFeuerwehr(String feuerwehrnummer) throws Exception
    {
        Feuerwehr feuerwehr = new Feuerwehr(getNameFuerFubwehr(feuerwehrnummer), feuerwehrnummer);
        return feuerwehr;
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
        PreparedStatement prepStat = conn.prepareStatement(EnStaticStatements.getFubwehrForUserID.toString());
        prepStat.setInt(1, intUserID);

        ResultSet rs = prepStat.executeQuery();

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
        PreparedStatement prepStat = conn.prepareStatement(EnStaticStatements.getAbschnittsnameFuerFubwehr.toString());
        prepStat.setString(1, strFubwehr);

        ResultSet rs = prepStat.executeQuery();
//        String sqlString = "SELECT DISTINCT f.Abschnitt_Instanznummer \"Nummer\" "
//                + "  FROM FDISK.dbo.stmkmitglieder s INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(s.instanznummer = f.instanznummer) "
//                + "  WHERE s.instanznummer = '" + strFubwehr + "'";
//        
      
        int intAbschnittsnummer = 0;
        while (rs.next())
        {
            intAbschnittsnummer = rs.getInt("Instanznummer");
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
        PreparedStatement prepStat = conn.prepareStatement(EnStaticStatements.getNameFuerFubwehr.toString());
        prepStat.setString(1, strFubwehr);
        ResultSet rs = prepStat.executeQuery();
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
        PreparedStatement prepStat = conn.prepareStatement(EnStaticStatements.getAbschnittsnameFuerFubwehr.toString());
        prepStat.setString(1, strFubwehr);
        ResultSet rs = prepStat.executeQuery();

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
        PreparedStatement prepStat = conn.prepareStatement(EnStaticStatements.getBereichsnameFuerFubwehr.toString());
        prepStat.setString(1, strFubwehr);
        ResultSet rs = prepStat.executeQuery();

        String strName = "";

        while (rs.next())
        {
            strName = rs.getString("Name");
        }

        connPool.releaseConnection(conn);
        return strName;
    }

    public int getBereichsnummerFuerFubwehr(String strFubwehr) throws SQLException, Exception
    {
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnStaticStatements.getBereichsnummerFuerFubwehr.toString());
        prepStat.setString(1, strFubwehr);
        ResultSet rs = prepStat.executeQuery();

        int intNummer = 0;

        while (rs.next())
        {
            intNummer = rs.getInt("Nr");
        }

        connPool.releaseConnection(conn);
        return intNummer;
    }

    public String getBereichsnameFuerBereichnnummer(int bereichnummer) throws Exception
    {
        String bereichname = " - ";
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnStaticStatements.getBereichsnameFuerBereichnnummer.toString());
        prepStat.setInt(1, bereichnummer);
        ResultSet rs = prepStat.executeQuery();
        
        while (rs.next())
        {
            bereichname = rs.getString("Bereichname");
        }
        return bereichname;
    }

    public String getAbschnittsnameFuerAbschnittsnummer(int abschnittnummer) throws Exception
    {
        String abschnittname = " - ";
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnStaticStatements.getAbschnittsnameFuerAbschnittsnummer.toString());
        prepStat.setInt(1, abschnittnummer);
        ResultSet rs = prepStat.executeQuery();
        while (rs.next())
        {
            abschnittname = rs.getString("Abschnittname");
        }
        return abschnittname;
    }

    public LinkedList<Integer> getAbschnittNummernFuerBereich(int bereichnummer) throws Exception
    {
        LinkedList<Integer> liAbschnittnummern = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnStaticStatements.getAbschnittNummernFuerBereich.toString());
        prepStat.setInt(1, bereichnummer);
        ResultSet rs = prepStat.executeQuery();
        while (rs.next())
        {
            liAbschnittnummern.add(rs.getInt("Abschnittnr"));
        }
        return liAbschnittnummern;
    }

    public LinkedList<String> getFubwehrNummernFuerAbschnitt(int abschnittnummer) throws Exception
    {
        LinkedList<String> liFubwehrnummern = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnStaticStatements.getFubwehrNummernFuerAbschnitt.toString());
        prepStat.setInt(1, abschnittnummer);
        ResultSet rs = prepStat.executeQuery();
        while (rs.next())
        {
            liFubwehrnummern.add(rs.getString("Fubwehr"));
        }
        return liFubwehrnummern;
    }

    /**
     * Gibt eine LinkedList mit allen Mitarbeitern (je nach Berechtigung eines
     * Users) zurück
     *
     * @param intBereichnr
     * @param intAbschnittnr
     * @param strFubwehr
     * @return LinkedList
     * @throws java.lang.Exception
     * @see Mitglied
     * @see LinkedList
     */
    public LinkedList<Mitglied> getEinfacheMitgliederliste(int intBereichnr, int intAbschnittnr, String strFubwehr) throws Exception
    {
        LinkedList<Mitglied> liMitglieder = new LinkedList<>();
        //String strFubwehr = getFubwehrForUserID(intUserID) + "";
        //int intAbschnittsnummer = getAbschnittsnummerForFubwehr(strFubwehr);
        //String strBereich = ("" + intAbschnittsnummer).substring(0, 2);
        //System.out.println("Bereich wenn Mitgliederliste aufgerufen wird: " + strBereich);

        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "";

        if (intAbschnittnr == -2)
        {
            sqlString = "SELECT id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\" "
                    + " FROM FDISK.dbo.stmkmitglieder s INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(s.instanznummer = f.instanznummer) "
                    + " WHERE f.Bereich_Nr = " + intBereichnr;
        } else
        {
            if (strFubwehr.equals("-2"))
            {
                sqlString = "SELECT id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\" "
                        + " FROM FDISK.dbo.stmkmitglieder s INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(s.instanznummer = f.instanznummer) "
                        + " WHERE f.abschnitt_instanznummer = " + intAbschnittnr;
            } else
            {
                sqlString = "SELECT id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\" "
                        + "FROM FDISK.dbo.stmkmitglieder "
                        + "WHERE instanznummer = '" + strFubwehr + "'";
            }
        }

//WICHTIG NICHT LÖSCHEN!
//        sqlString = "SELECT id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\" "
//                + "FROM FDISK.dbo.stmkmitglieder "
//                + "WHERE SUBSTRING(instanznummer, 0, 3) = '" + intBereichnr + "'";
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
     * @param intBereichnr
     * @param strFubwehr
     * @param intAbschnittnr
     * @return LinkedList
     * @param jahr
     * @throws java.lang.Exception
     * @see MitgliedsGeburtstag
     * @see Mitglied
     * @see LinkedList
     */
    public LinkedList<MitgliedsGeburtstag> getGeburtstagsliste(int jahr, int intBereichnr, int intAbschnittnr, String strFubwehr) throws Exception
    {
        LinkedList<MitgliedsGeburtstag> liMitgliedsGeburtstage = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();
        String sqlString = "SELECT id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\", geburtsdatum \"Geburtsdatum\""
                + " FROM FDISK.dbo.stmkmitglieder";

        if (intAbschnittnr == -2)
        {
            sqlString = "SELECT id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\", geburtsdatum \"Geburtsdatum\" "
                    + " FROM FDISK.dbo.stmkmitglieder s INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(s.instanznummer = f.instanznummer) "
                    + " WHERE f.Bereich_Nr = " + intBereichnr;
        } else
        {
            if (strFubwehr.equals("-2"))
            {
                sqlString = "SELECT id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\", geburtsdatum \"Geburtsdatum\""
                        + " FROM FDISK.dbo.stmkmitglieder s INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(s.instanznummer = f.instanznummer) "
                        + " WHERE f.abschnitt_instanznummer = " + intAbschnittnr;
            } else
            {
                sqlString = "SELECT id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\", geburtsdatum \"Geburtsdatum\""
                        + " FROM FDISK.dbo.stmkmitglieder"
                        + " WHERE instanznummer = '" + strFubwehr + "'";
            }
        }

//WICHTIG NICHT LÖSCHEN!
//         sqlString = "SELECT id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\", geburtsdatum \"Geburtsdatum\""
//                + " FROM FDISK.dbo.stmkmitglieder"
//                + " WHERE SUBSTRING(instanznummer, 0, 3) = '" + intBereichnr + "'";
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
            int intCurrentYear = jahr - 1;
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
     * @param intBereichnr
     * @param intAbschnittnr
     * @param strFubwehr
     * @return LinkedList
     * @throws java.lang.Exception
     * @see Mitglied
     * @see MitgliedsDienstzeit
     * @see LinkedList
     */
    public LinkedList<MitgliedsDienstzeit> getDienstzeitListe(int intBereichnr, int intAbschnittnr, String strFubwehr) throws Exception
    {
        LinkedList<MitgliedsDienstzeit> liMitgliedsDienstzeiten = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();
        String sqlString = "";
        if (intAbschnittnr == -2)
        {
            sqlString = "SELECT m.id_personen \"PersID\", m.standesbuchnummer \"STB\", m.dienstgrad \"DGR\", m.titel \"Titel\", m.vorname \"Vorname\", m.zuname \"Zuname\", m.geburtsdatum \"Geburtsdatum\",  m.datum_abgemeldet \"Datum_abgemeldet\", m.eintrittsdatum \"Eintrittsdatum\", m.vordienstzeit \"Vordienstzeit\", SUM(z.VD_ZEIT) \"VD_ZEIT\""
                    + " FROM FDISK.dbo.stmkmitglieder m LEFT OUTER JOIN FDISK.dbo.FDISK_MAPPING_VD_ZEIT z ON(m.id_personen = z.id_personen)"
                    + " INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(m.instanznummer = f.instanznummer)"
                    + " WHERE f.Bereich_Nr = " + intBereichnr
                    + " AND m.datum_abgemeldet IS NULL "
                    + " GROUP BY "
                    + " m.id_personen, "
                    + " m.standesbuchnummer, "
                    + " m.dienstgrad, "
                    + " m.titel, "
                    + " m.vorname, "
                    + " m.zuname, "
                    + " m.geburtsdatum, "
                    + " m.datum_abgemeldet, "
                    + " m.eintrittsdatum,"
                    + " m.vordienstzeit ";
        } else
        {
            if (strFubwehr.equals("-2"))
            {
                sqlString = "SELECT m.id_personen \"PersID\", m.standesbuchnummer \"STB\", m.dienstgrad \"DGR\", m.titel \"Titel\", m.vorname \"Vorname\", m.zuname \"Zuname\", m.geburtsdatum \"Geburtsdatum\",  m.datum_abgemeldet \"Datum_abgemeldet\", m.eintrittsdatum \"Eintrittsdatum\", m.vordienstzeit \"Vordienstzeit\", SUM(z.VD_ZEIT) \"VD_ZEIT\""
                        + " FROM FDISK.dbo.stmkmitglieder m LEFT OUTER JOIN FDISK.dbo.FDISK_MAPPING_VD_ZEIT z ON(m.id_personen = z.id_personen)"
                        + " INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(m.instanznummer = f.instanznummer)"
                        + " WHERE f.abschnitt_instanznummer = " + intAbschnittnr
                        + " AND m.datum_abgemeldet IS NULL "
                        + " GROUP BY "
                        + " m.id_personen, "
                        + " m.standesbuchnummer, "
                        + " m.dienstgrad, "
                        + " m.titel, "
                        + " m.vorname, "
                        + " m.zuname, "
                        + " m.geburtsdatum, "
                        + " m.datum_abgemeldet, "
                        + " m.eintrittsdatum,"
                        + " m.vordienstzeit ";
            } else
            {
                sqlString = "SELECT m.id_personen \"PersID\", m.standesbuchnummer \"STB\", m.dienstgrad \"DGR\", m.titel \"Titel\", m.vorname \"Vorname\", m.zuname \"Zuname\", m.geburtsdatum \"Geburtsdatum\",  m.datum_abgemeldet \"Datum_abgemeldet\", m.eintrittsdatum \"Eintrittsdatum\", m.vordienstzeit \"Vordienstzeit\", SUM(z.VD_ZEIT) \"VD_ZEIT\""
                        + " FROM FDISK.dbo.stmkmitglieder m LEFT OUTER JOIN FDISK.dbo.FDISK_MAPPING_VD_ZEIT z ON(m.id_personen = z.id_personen)"
                        + " WHERE m.instanznummer = '" + strFubwehr + "'"
                        + " AND m.datum_abgemeldet IS NULL "
                        + " GROUP BY "
                        + " m.id_personen, "
                        + " m.standesbuchnummer, "
                        + " m.dienstgrad, "
                        + " m.titel, "
                        + " m.vorname, "
                        + " m.zuname, "
                        + " m.geburtsdatum, "
                        + " m.datum_abgemeldet, "
                        + " m.eintrittsdatum,"
                        + " m.vordienstzeit ";
            }
        }

        ResultSet rs = stat.executeQuery(sqlString);

        String strSTB;
        String strDGR;
        String strTitel;
        String strVorname;
        String strZuname;
        int intPersID;
        Date dateGeburtsdatum;
        Date dateEintrittsdatum;
        double doubleVordienstzeit;

        while (rs.next())
        {
            intPersID = rs.getInt("PersID");
            strSTB = rs.getString("STB");
            strDGR = rs.getString("DGR");
            strTitel = rs.getString("Titel");
            strVorname = rs.getString("Vorname");
            strZuname = rs.getString("Zuname");
            dateGeburtsdatum = new Date(rs.getDate("Geburtsdatum").getTime());

            dateEintrittsdatum = new Date();

            Timestamp helper = rs.getTimestamp("Eintrittsdatum");
            if (helper != null)
            {
                dateEintrittsdatum = new java.util.Date(helper.getTime());
            }

            doubleVordienstzeit = rs.getDouble("VD_ZEIT");
            Calendar calEintrittsdatum = Calendar.getInstance();
            calEintrittsdatum.setTime(dateEintrittsdatum);

            double doubleDienstzeit = Calendar.getInstance().get(Calendar.YEAR) - calEintrittsdatum.get(Calendar.YEAR);

            if (Calendar.getInstance().get(Calendar.MONTH) < calEintrittsdatum.get(Calendar.MONTH))
            {
                doubleDienstzeit--;
            }

            if ((Calendar.getInstance().get(Calendar.MONTH) == calEintrittsdatum.get(Calendar.MONTH)) && Calendar.getInstance().get(Calendar.DAY_OF_MONTH) < calEintrittsdatum.get(Calendar.DAY_OF_MONTH))
            {
                doubleDienstzeit--;
            }

            doubleDienstzeit += doubleVordienstzeit;

            MitgliedsDienstzeit mitgliedsDienst = new MitgliedsDienstzeit(intPersID, strSTB, strDGR, strTitel, strVorname, strZuname, true, dateGeburtsdatum, doubleDienstzeit);
            liMitgliedsDienstzeiten.add(mitgliedsDienst);

        }
        connPool.releaseConnection(conn);
        return liMitgliedsDienstzeiten;
    }

    /**
     * Gibt eine Liste mit allen Adressen aller Mitarbeiter als LinkedList
     * zurück.
     *
     * @return LinkedList
     * @throws java.lang.Exception
     * @see Mitglied
     * @see MitgliedsAdresse
     * @see LinkedList
     */
    public LinkedList<MitgliedsAdresse> getAdressListe(int intBereichnr, int intAbschnittnr, String strFubwehr) throws Exception
    {
        LinkedList<MitgliedsAdresse> liMitgliedsAdressen = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "SELECT adressen.id_adressen \"AdressID\", adressen.strasse \"Strasse\", adressen.nummer \"Nummer\", "
                + "adressen.stiege \"Stiege\", adressen.plz \"PLZ\", adressen.ort \"Ort\", mitglied.id_personen \"PersID\", "
                + "mitglied.standesbuchnummer \"STB\", mitglied.dienstgrad \"DGR\", "
                + "mitglied.titel \"Titel\", mitglied.vorname \"Vorname\", mitglied.zuname \"Zuname\", "
                + "mitglied.geburtsdatum \"Geburtsdatum\" "
                + "FROM FDISK.dbo.stmkadressen adressen INNER JOIN FDISK.dbo.stmkmitglieder mitglied "
                + "ON (adressen.id_personen = mitglied.id_personen)";

        if (intAbschnittnr == -2)
        {
            sqlString = "SELECT adressen.id_adressen \"AdressID\", adressen.strasse \"Strasse\", adressen.nummer \"Nummer\","
                    + " adressen.stiege \"Stiege\", adressen.plz \"PLZ\", adressen.ort \"Ort\", mitglied.id_personen \"PersID\","
                    + " mitglied.standesbuchnummer \"STB\", mitglied.dienstgrad \"DGR\","
                    + " mitglied.titel \"Titel\", mitglied.vorname \"Vorname\", mitglied.zuname \"Zuname\","
                    + " mitglied.geburtsdatum \"Geburtsdatum\""
                    + " FROM FDISK.dbo.stmkadressen adressen INNER JOIN FDISK.dbo.stmkmitglieder mitglied"
                    + " ON (adressen.id_personen = mitglied.id_personen)"
                    + " INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(mitglied.instanznummer = f.instanznummer)"
                    + " WHERE f.Bereich_Nr = " + intBereichnr;
        } else
        {
            if (strFubwehr.equals("-2"))
            {
                sqlString = "SELECT adressen.id_adressen \"AdressID\", adressen.strasse \"Strasse\", adressen.nummer \"Nummer\","
                        + " adressen.stiege \"Stiege\", adressen.plz \"PLZ\", adressen.ort \"Ort\", mitglied.id_personen \"PersID\","
                        + " mitglied.standesbuchnummer \"STB\", mitglied.dienstgrad \"DGR\","
                        + " mitglied.titel \"Titel\", mitglied.vorname \"Vorname\", mitglied.zuname \"Zuname\","
                        + " mitglied.geburtsdatum \"Geburtsdatum\""
                        + " FROM FDISK.dbo.stmkadressen adressen INNER JOIN FDISK.dbo.stmkmitglieder mitglied"
                        + " ON (adressen.id_personen = mitglied.id_personen)"
                        + " INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(mitglied.instanznummer = f.instanznummer)"
                        + " WHERE f.abschnitt_instanznummer = " + intAbschnittnr;
            } else
            {
                sqlString = "SELECT adressen.id_adressen \"AdressID\", adressen.strasse \"Strasse\", adressen.nummer \"Nummer\","
                        + " adressen.stiege \"Stiege\", adressen.plz \"PLZ\", adressen.ort \"Ort\", mitglied.id_personen \"PersID\","
                        + " mitglied.standesbuchnummer \"STB\", mitglied.dienstgrad \"DGR\","
                        + " mitglied.titel \"Titel\", mitglied.vorname \"Vorname\", mitglied.zuname \"Zuname\","
                        + " mitglied.geburtsdatum \"Geburtsdatum\""
                        + " FROM FDISK.dbo.stmkadressen adressen INNER JOIN FDISK.dbo.stmkmitglieder mitglied"
                        + " ON (adressen.id_personen = mitglied.id_personen)"
                        + " WHERE mitglied.instanznummer = '" + strFubwehr + "'";
            }
        }

//WICHTIG NICHT LÖSCHEN!
//sqlString = "SELECT adressen.id_adressen \"AdressID\", adressen.strasse \"Strasse\", adressen.nummer \"Nummer\","
//                + " adressen.stiege \"Stiege\", adressen.plz \"PLZ\", adressen.ort \"Ort\", mitglied.id_personen \"PersID\","
//                + " mitglied.standesbuchnummer \"STB\", mitglied.dienstgrad \"DGR\","
//                + " mitglied.titel \"Titel\", mitglied.vorname \"Vorname\", mitglied.zuname \"Zuname\","
//                + " mitglied.geburtsdatum \"Geburtsdatum\""
//                + " FROM FDISK.dbo.stmkadressen adressen INNER JOIN FDISK.dbo.stmkmitglieder mitglied"
//                + " ON (adressen.id_personen = mitglied.id_personen)"
//                + " WHERE SUBSTRING(mitglied.instanznummer, 0, 3) = '" + intBereichnr + "'";     
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
     * Gibt spezielle Informationen zu der Tätigkeit "Kursbesuch an der FWZS"
     * als LinkedList zurück. Diese Informationen sind zum Beispiel Anzahl der
     * Mitarbeiter in einem KursAlt.
     *
     * @param intBereichnr
     * @param intAbschnittnr
     * @param strFubwehr
     * @param strVon
     * @param strBis
     * @return
     * @throws java.lang.Exception
     */
    public LinkedList<Kurstaetigkeit> getKursstatistiktaetigkeit(int intBereichnr, int intAbschnittnr, String strFubwehr, String strVon, String strBis) throws Exception
    {
        LinkedList<Kurstaetigkeit> liKursetaetigkeiten = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "SELECT COUNT(m.id_mitgliedschaften) \"Teilnahmen\""
                + " ,SUM(f.km) \"Km\""
                + " ,t.id_berichte \"BerichtId\""
                + " ,t.instanznummer \"Instanznr\""
                + " ,t.instanzname \"Instanzname\""
                + " ,t.taetigkeitsart \"Taetigkeitsart\""
                + " ,t.taetigkeitsunterart \"Taetigkeitsunterart\""
                + " ,t.nummer \"Nr\""
                + " ,t.beginn \"Beginn\""
                + " ,t.ende \"Ende\""
                + " FROM FDISK.dbo.stmktaetigkeitsberichte t"
                + " INNER JOIN FDISK.dbo.stmktaetigkeitsberichtemitglieder m ON(t.id_berichte = m.id_berichte)"
                + " INNER JOIN FDISK.dbo.stmktaetigkeitsberichtefahrzeuge f ON (t.id_berichte = f.id_berichte)"
                + " INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich fw ON(t.instanznummer = fw.instanznummer)"
                + " WHERE t.taetigkeitsart = 'Kursbesuch an der FWZS' ";

        if (intAbschnittnr == -2)
        {
            sqlString += " AND fw.Bereich_Nr = " + intBereichnr;
        } else
        {
            if (strFubwehr.equals("-2"))
            {
                sqlString += " AND fw.abschnitt_instanznummer = " + intAbschnittnr;
            } else
            {
                sqlString += " AND t.instanznummer = '" + strFubwehr + "'";
            }
        }

        sqlString += getSqlDateString(strVon, strBis, 2, false);

        sqlString += " GROUP BY t.id_berichte"
                + " ,t.instanznummer"
                + " ,t.instanzname"
                + " ,t.taetigkeitsart"
                + " ,t.taetigkeitsunterart"
                + " ,t.nummer"
                + " ,t.beginn"
                + " ,t.ende";
        ResultSet rs = stat.executeQuery(sqlString);

        double doKm;
        int intTeilnehmer;
        int intIdBerichte;
        int intInstanznummer;
        String strInstanzname;
        String strTaetigkeitsart;
        String strTaetigkeitsunterart;
        String strNummer;
        Date dateBeginn;
        Date dateEnde;

        while (rs.next())
        {

            intIdBerichte = rs.getInt("BerichtId");
            intTeilnehmer = rs.getInt("Teilnahmen");
            intInstanznummer = rs.getInt("Instanznr");
            strInstanzname = rs.getString("Instanzname");
            strTaetigkeitsart = rs.getString("Taetigkeitsart");
            strTaetigkeitsunterart = rs.getString("Taetigkeitsunterart");
            strNummer = rs.getString("Nr");
            dateBeginn = rs.getTimestamp("Beginn");
            dateEnde = rs.getTimestamp("Ende");
            doKm = rs.getDouble("Km");

            Kurstaetigkeit kurstaetigkeit = new Kurstaetigkeit(intIdBerichte, intTeilnehmer, doKm, intInstanznummer, strInstanzname, strTaetigkeitsart, strTaetigkeitsunterart, strNummer, dateBeginn, dateEnde);
            liKursetaetigkeiten.add(kurstaetigkeit);

        }
        connPool.releaseConnection(conn);
        return liKursetaetigkeiten;
    }

    public LinkedList<Kurs> getKursstatistikkurse(String strVon, String strBis) throws Exception
    {
        LinkedList<Kurs> liKurse = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "SELECT id_kurse \"KursId\""
                + " ,id_kursarten \"KursartId\""
                + " ,lehrgangsnummer \"Lehrgangsnr\""
                + " ,kursbezeichnung \"Kursbez\""
                + " ,kurskurzbezeichnung \"Kurskurzbez\""
                + " ,datum \"Datum\""
                + " ,id_instanzen_veranstalter \"Veran\""
                + " ,id_instanzen_durchfuehrend \"Durchf\""
                + " ,kursstatus \"Status\""
                + " FROM FDISK.dbo.stmkkurse ";

//        if (intAbschnittnr == -2) {
//            sqlString += " AND fw.Bereich_Nr = " + intBereichnr;
//        } else {
//            if (strFubwehr.equals("-2")) {
//                sqlString += " AND fw.abschnitt_instanznummer = " + intAbschnittnr;
//            } else {
//                sqlString += " AND t.instanznummer = '" + strFubwehr + "'";
//            }
//        }
        sqlString += getSqlDateString(strVon, strBis, 4, true);
        ResultSet rs = stat.executeQuery(sqlString);

        int intKursId;
        int intKursartId;
        int intLehrgangsnr;
        String strKursbez;
        String strKurskurzbez;
        Date dateDatum;
        int intIdVer;
        int intIdDurchf;
        String strStatus;

        while (rs.next())
        {
            intKursId = rs.getInt("KursId");
            intKursartId = rs.getInt("KursartId");
            intLehrgangsnr = rs.getInt("Lehrgangsnr");
            strKursbez = rs.getString("Kursbez");
            strKurskurzbez = rs.getString("Kurskurzbez");
            dateDatum = rs.getTimestamp("Datum");
            intIdVer = rs.getInt("Veran");
            intIdDurchf = rs.getInt("Durchf");
            strStatus = rs.getString("Status");

            Kurs kurs = new Kurs(intKursId, intKursartId, intLehrgangsnr, strKursbez, strKurskurzbez, dateDatum, intIdVer, intIdDurchf, strStatus);
            liKurse.add(kurs);
        }
        connPool.releaseConnection(conn);
        return liKurse;
    }

    public LinkedList<MitgliedsStunden> getStundenauswertungProMitgliedProInstanz(int intBereichnr, int intAbschnittnr, String strFubwehr) throws Exception
    {
        LinkedList<MitgliedsStunden> liStunden = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        int intPersID;
        String strSTB;
        String strDGR;
        String strTitel;
        String strVorname;
        String strZuname;
        double doStunden;
        String strInstanznummer;

        String sqlString = "SELECT t.[id_personen] \"PersID\""
                + " ,t.vorname \"Vorname\", t.zuname \"Zuname\", t.instanznummer \"Instanznummer\", m.standesbuchnummer \"STB\", m.dienstgrad \"DGR\", m.titel \"Titel\""
                + " , SUM(DATEDIFF(mi,einsatzzeit_von,einsatzzeit_bis)) / 60.0 \"Stunden\""
                + " FROM [FDISK].[dbo].stmktaetigkeitsberichtemitglieder t INNER JOIN FDISK.dbo.stmkmitglieder m ON(t.id_personen = m.id_personen)"
                + " INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich fw ON(fw.instanznummer = t.instanznummer)";
        if (intAbschnittnr == -2)
        {
            sqlString += " WHERE fw.Bereich_Nr = " + intBereichnr;
        } else
        {
            if (strFubwehr.equals("-2"))
            {
                sqlString += " WHERE fw.abschnitt_instanznummer = " + intAbschnittnr;
            } else
            {
                sqlString += " WHERE t.instanznummer = '" + strFubwehr + "'";
            }
        }
        sqlString += " GROUP BY t.id_personen, t.instanznummer, t.vorname, t.zuname, m.standesbuchnummer, m.dienstgrad, m.titel"
                + " UNION"
                + " SELECT u.[id_personen] \"PersID\""
                + " ,u.vorname \"Vorname\", u.zuname \"Zuname\", u.instanznummer \"Instanznummer\", m.standesbuchnummer \"STB\", m.dienstgrad \"DGR\", m.titel \"Titel\""
                + " , SUM(DATEDIFF(mi,einsatzzeit_von,einsatzzeit_bis)) / 60.0 \"Stunden\""
                + " FROM [FDISK].[dbo].stmkuebungsberichtemitglieder u INNER JOIN FDISK.dbo.stmkmitglieder m ON(u.id_personen = m.id_personen)"
                + " INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich fw ON(fw.instanznummer = u.instanznummer)";
        if (intAbschnittnr == -2)
        {
            sqlString += " WHERE fw.Bereich_Nr = " + intBereichnr;
        } else
        {
            if (strFubwehr.equals("-2"))
            {
                sqlString += " WHERE fw.abschnitt_instanznummer = " + intAbschnittnr;
            } else
            {
                sqlString += " WHERE u.instanznummer = '" + strFubwehr + "'";
            }
        }
        sqlString += " GROUP BY u.id_personen, u.instanznummer, u.vorname, u.zuname, m.standesbuchnummer, m.dienstgrad, m.titel"
                + " UNION"
                + " SELECT e.[id_personen] \"PersID\""
                + " ,e.vorname \"Vorname\", e.zuname \"Zuname\", e.instanznummer \"Instanznummer\", m.standesbuchnummer \"STB\", m.dienstgrad \"DGR\", m.titel \"Titel\""
                + " ,SUM(DATEDIFF(mi,einsatzzeit_von,einsatzzeit_bis)) / 60.0 \"Stunden\""
                + " FROM [FDISK].[dbo].stmkeinsatzberichtemitglieder e INNER JOIN FDISK.dbo.stmkmitglieder m ON(e.id_personen = m.id_personen)"
                + " INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich fw ON(fw.instanznummer = e.instanznummer)";
        if (intAbschnittnr == -2)
        {
            sqlString += " WHERE fw.Bereich_Nr = " + intBereichnr;
        } else
        {
            if (strFubwehr.equals("-2"))
            {
                sqlString += " WHERE fw.abschnitt_instanznummer = " + intAbschnittnr;
            } else
            {
                sqlString += " WHERE e.instanznummer = '" + strFubwehr + "'";
            }
        }
        sqlString += " GROUP BY e.id_personen, e.instanznummer, e.vorname, e.zuname, m.standesbuchnummer, m.dienstgrad, m.titel"
                + " order by \"PersID\";";

        ResultSet rs = stat.executeQuery(sqlString);

        boolean exists = false;

        while (rs.next())
        {
            intPersID = rs.getInt("PersID");
            strSTB = rs.getString("STB");
            strDGR = rs.getString("DGR");
            strTitel = rs.getString("Titel");
            strVorname = rs.getString("Vorname");
            strZuname = rs.getString("Zuname");

            doStunden = rs.getDouble("Stunden");
            strInstanznummer = rs.getString("Instanznummer");

            if (!liStunden.isEmpty())
            {
                for (int i = 0; i < liStunden.size(); i++)
                {
                    if (liStunden.get(i).getIntId_Personen() == intPersID && liStunden.get(i).getStrInstanznummer().equals(strInstanznummer))
                    {
                        exists = true;
                        liStunden.get(i).setDoStunden(liStunden.get(i).getDoStunden() + doStunden);
                        break;
                    }
                }
                if (!exists)
                {
                    MitgliedsStunden mitgliedsStunden = new MitgliedsStunden(intPersID, strSTB, strDGR, strTitel, strVorname, strZuname, doStunden, strInstanznummer);
                    liStunden.add(mitgliedsStunden);
                } else
                {
                    exists = false;
                }
            } else
            {
                MitgliedsStunden mitgliedsStunden = new MitgliedsStunden(intPersID, strSTB, strDGR, strTitel, strVorname, strZuname, doStunden, strInstanznummer);
                liStunden.add(mitgliedsStunden);
            }
        }

        connPool.releaseConnection(conn);
        return liStunden;
    }

    public String getDetailsFuerFahrtenbuchFahrzeug(LinkedList<Fahrzeug> liFahrzeuge)
    {
        if (liFahrzeuge == null || liFahrzeuge.size() == 0)
        {
            return "";
        }
        Fahrzeug f = liFahrzeuge.get(0);
        String htmlString = "<fieldset>"
                + "<legend><b>Fahrzeugdaten</b></legend>"
                + "<table class='ui celled table'>"
                + "<thead>"
                + "<tr>"
                + "<th>Art</th>"
                + "<th>Baujahr</th>"
                + "<th>Aufbaufirma</th>"
                + "<th>Marke</th>"
                + "<th>Leistung</th>"
                + "<th>Treibstoff</th>"
                + "</tr>"
                + "</thead>"
                + "<tbody>"
                + "<tr>"
                + "<td>" + f.getStrFahrzeugart() + "</td>"
                + "<td>" + f.getIntBaujahr() + "</td>"
                + "<td>" + f.getStrAufbaufirma() + "</td>"
                + "<td>" + f.getStrFahrzeugmarke() + "</td>"
                + "<td>" + f.getIntLeistung() + "</td>"
                + "<td>" + f.getStrTreibstoff() + "</td>"
                + "</tr>"
                + "</tbody>"
                + "</table>"
                + "</fieldset>";

        return htmlString;
    }

    /**
     * Gibt alle relevanten Daten (Fahrzeugtyp, Kennzeichen, Baujahr etc...) von
     * jedem Fahrzeug als LinkedList zurück. 
     *
     * @param intBereichnr
     * @param intAbschnittnr
     * @param strFubwehr
     * @param strVon
     * @param strBis
     * @param strEingabeKennzeichen
     * @return LinkedList
     * @throws Exception
     * @see Fahrzeug
     * @see LinkedList
     */
    public LinkedList<Fahrzeug> getFahrtenbuch(int intBereichnr, int intAbschnittnr, String strFubwehr, String strVon, String strBis, String strEingabeKennzeichen) throws Exception
    {
        LinkedList<Fahrzeug> liFahrzeuge = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        if (strEingabeKennzeichen != null && !strEingabeKennzeichen.isEmpty() && !strEingabeKennzeichen.equals(" "))
        {
            strEingabeKennzeichen = strEingabeKennzeichen.replace("/", "").replace(".", " ").replace(" ", "").replace("+", "").replace("-", "");
        }

        String sqlString = "SELECT "
                + "f.kennzeichen \"Kennzeichen\" "
                + ",f.id_fahrzeuge \"Id_Fahrzeuge\" "
                + ",f.fahrzeugtyp \"Fahrzeugtyp\" "
                + ",f.taktischebezeichnung \"Taktische Bezeichnung\" "
                + ",f.bezeichnung \"Bezeichnung\" "
                + ",f.status \"Status\" "
                + ",f.baujahr \"Baujahr\" "
                + ",f.fahrzeugmarke \"Fahrzeugmarke\" "
                + ",f.aufbaufirma \"Aufbaufirma\""
                + ",f.instanznummer \"Instanzummer\" "
                + ",f.fahrzeugart \"Fahrzeugart\" "
                + ",f.leistung \"Leistung\" "
                + ",f.eigengewicht \"Eigengewicht\" "
                + ",f.gesamtgewicht \"Gesamtgewicht\" "
                + ",f.treibstoff \"Treibstoff\" "
                + ",uf.km \"Km\" "
                + ",u.uebungsart \"Art\" "
                + ",u.beginn \"Beginn\" "
                + ",u.ende \"Ende\" "
                + " FROM"
                + " FDISK.dbo.stmkfahrzeuge f INNER JOIN"
                + " FDISK.dbo.stmkuebungsberichtefahrzeuge uf ON(f.id_fahrzeuge = uf.id_fahrzeuge)"
                + " INNER JOIN FDISK.dbo.stmkuebungsberichte u"
                + " ON(uf.id_berichte = u.id_berichte)"
                + " INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich fw ON(fw.instanznummer = f.instanznummer)"
                + " WHERE f.status = 'aktiv' ";

        sqlString += getSqlDateString(strVon, strBis, 3, false);
        if (strEingabeKennzeichen != null && !strEingabeKennzeichen.isEmpty() && !strEingabeKennzeichen.equals(" "))
        {
            sqlString += " AND UPPER(replace(replace(replace(replace(replace(f.kennzeichen,'+',''),'/',''),'.',''),' ',''),'-','')) = '" + strEingabeKennzeichen.toUpperCase() + "'";
        }

        if (intAbschnittnr == -2)
        {
            sqlString += " AND fw.Bereich_Nr = " + intBereichnr;
        } else
        {
            if (strFubwehr.equals("-2"))
            {
                sqlString += " AND fw.abschnitt_instanznummer = " + intAbschnittnr;
            } else
            {
                sqlString += " AND f.instanznummer = '" + strFubwehr + "'";
            }
        }

        sqlString += " UNION ALL SELECT "
                + "f.kennzeichen "
                + ",f.id_fahrzeuge "
                + ",f.fahrzeugtyp "
                + ",f.taktischebezeichnung "
                + ",f.bezeichnung "
                + ",f.status "
                + ",f.baujahr "
                + ",f.fahrzeugmarke "
                + ",f.aufbaufirma "
                + ",f.instanznummer "
                + ",f.fahrzeugart "
                + ",f.leistung "
                + ",f.eigengewicht "
                + ",f.gesamtgewicht "
                + ",f.treibstoff "
                + ",ef.km "
                + ",e.einsatzart "
                + ",e.uhrzeit_alarmierung "
                + ",e.uhrzeit_rueckkehr "
                + " FROM"
                + " FDISK.dbo.stmkfahrzeuge f INNER JOIN"
                + " FDISK.dbo.stmkeinsatzberichtefahrzeuge ef ON(f.id_fahrzeuge = ef.id_fahrzeuge) "
                + " INNER JOIN FDISK.dbo.stmkeinsatzberichte e ON(ef.id_berichte = e.id_berichte)"
                + " INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich fw ON(fw.instanznummer = f.instanznummer)"
                + " WHERE f.status = 'aktiv' ";

        sqlString += getSqlDateString(strVon, strBis, 1, false);

        if (strEingabeKennzeichen != null && !strEingabeKennzeichen.isEmpty() && !strEingabeKennzeichen.equals(" "))
        {
            sqlString += " AND UPPER(replace(replace(replace(replace(replace(f.kennzeichen,'+',''),'/',''),'.',''),' ',''),'-','')) = '" + strEingabeKennzeichen.toUpperCase() + "'";
        }

        if (intAbschnittnr == -2)
        {
            sqlString += " AND fw.Bereich_Nr = " + intBereichnr;
        } else
        {
            if (strFubwehr.equals("-2"))
            {
                sqlString += " AND fw.abschnitt_instanznummer = " + intAbschnittnr;
            } else
            {
                sqlString += " AND f.instanznummer = '" + strFubwehr + "'";
            }
        }

        sqlString += " UNION ALL SELECT "
                + "f.kennzeichen "
                + ",f.id_fahrzeuge "
                + ",f.fahrzeugtyp "
                + ",f.taktischebezeichnung "
                + ",f.bezeichnung "
                + ",f.status "
                + ",f.baujahr "
                + ",f.fahrzeugmarke "
                + ",f.aufbaufirma "
                + ",f.instanznummer "
                + ",f.fahrzeugart "
                + ",f.leistung "
                + ",f.eigengewicht "
                + ",f.gesamtgewicht "
                + ",f.treibstoff "
                + ",tf.km "
                + ",t.taetigkeitsart "
                + ",t.beginn "
                + ",t.ende "
                + " FROM"
                + " FDISK.dbo.stmkfahrzeuge f INNER JOIN"
                + " FDISK.dbo.stmktaetigkeitsberichtefahrzeuge tf ON(f.id_fahrzeuge = tf.id_fahrzeuge)"
                + " INNER JOIN FDISK.dbo.stmktaetigkeitsberichte t ON(tf.id_berichte = t.id_berichte)"
                + " INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich fw ON(fw.instanznummer = f.instanznummer)"
                + " WHERE f.status = 'aktiv' ";

        sqlString += getSqlDateString(strVon, strBis, 2, false);
        if (strEingabeKennzeichen != null && !strEingabeKennzeichen.isEmpty() && !strEingabeKennzeichen.equals(" "))
        {
            sqlString += " AND UPPER(replace(replace(replace(replace(replace(f.kennzeichen,'+',''),'/',''),'.',''),' ',''),'-','')) = '" + strEingabeKennzeichen.toUpperCase() + "'";
        }

        if (intAbschnittnr == -2)
        {
            sqlString += " AND fw.Bereich_Nr = " + intBereichnr;
        } else
        {
            if (strFubwehr.equals("-2"))
            {
                sqlString += " AND fw.abschnitt_instanznummer = " + intAbschnittnr;
            } else
            {
                sqlString += " AND f.instanznummer = '" + strFubwehr + "'";
            }
        }

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
        String strFahrzeugart;
        int intLeistung;
        int intEigengewicht;
        int intGesamtgewicht;
        String strTreibstoff;
        double doubleKm;
        String strArt;
        Date dateBeginn;
        Date dateEnde;

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
            strFahrzeugart = rs.getString("Fahrzeugart");
            intLeistung = rs.getInt("Leistung");
            intEigengewicht = rs.getInt("Eigengewicht");
            intGesamtgewicht = rs.getInt("Gesamtgewicht");
            strTreibstoff = rs.getString("Treibstoff");
            doubleKm = rs.getDouble("Km");
            strArt = rs.getString("Art");
            dateBeginn = rs.getTimestamp("Beginn");
            dateEnde = rs.getTimestamp("Ende");

            Fahrzeug fahrzeug = new Fahrzeug(strFahrzeugTyp, strKennzeichen,
                    intBaujahr, strAufbaufirma, strTaktischeBezeichnung,
                    intId_fahrzeuge, strBezeichnung, strFahrzeugmarke,
                    intInstanznummer, strFahrzeugart, intLeistung,
                    intEigengewicht, intGesamtgewicht, strTreibstoff,
                    doubleKm, strArt, dateBeginn, dateEnde);
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
    public LinkedList<MitgliedsErreichbarkeit> getErreichbarkeitsliste(int intBereichnr, int intAbschnittnr, String strFubwehr) throws Exception
    {
        LinkedList<MitgliedsErreichbarkeit> liMitgliedsErreichbarkeiten = new LinkedList<>();

        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "";

        if (intAbschnittnr == -2)
        {
            sqlString = "SELECT DISTINCT sm.id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\", se.erreichbarkeitsart \"Erreichbarkeitsart\", se.code \"Code\""
                    + " FROM FDISK.dbo.stmkmitglieder sm INNER JOIN FDISK.dbo.stmkerreichbarkeiten se ON(sm.id_personen = se.id_personen)"
                    + " INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(sm.instanznummer = f.instanznummer)"
                    + " WHERE f.Bereich_Nr = " + intBereichnr
                    + " ORDER BY sm.id_personen;";
        } else
        {
            if (strFubwehr.equals("-2"))
            {
                sqlString = "SELECT DISTINCT sm.id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\", se.erreichbarkeitsart \"Erreichbarkeitsart\", se.code \"Code\""
                        + " FROM FDISK.dbo.stmkmitglieder sm INNER JOIN FDISK.dbo.stmkerreichbarkeiten se ON(sm.id_personen = se.id_personen)"
                        + " INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(sm.instanznummer = f.instanznummer)"
                        + " WHERE f.abschnitt_instanznummer = " + intAbschnittnr
                        + " ORDER BY sm.id_personen;";
            } else
            {
                sqlString = "SELECT DISTINCT sm.id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\", se.erreichbarkeitsart \"Erreichbarkeitsart\", se.code \"Code\""
                        + " FROM FDISK.dbo.stmkmitglieder sm INNER JOIN FDISK.dbo.stmkerreichbarkeiten se ON(sm.id_personen = se.id_personen)"
                        + " WHERE sm.instanznummer = '" + strFubwehr + "'"
                        + " ORDER BY sm.id_personen;";
            }
        }
//WICHTIG NICHT LÖSCHEN!
//sqlString = "SELECT id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\", geburtsdatum \"Geburtsdatum\",  datum_abgemeldet \"Datum_abgemeldet\", eintrittsdatum \"Eintrittsdatum\", vordienstzeit \"Vordienstzeit\""
//                + " FROM FDISK.dbo.stmkmitglieder"
//                + " WHERE SUBSTRING(instanznummer, 0, 3) = '" + intBereichnr + "'";
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
     * Gibt alle für einen Leerbericht benötigten Mitglieder zurück
     *
     * @return
     * @throws Exception
     */
    public LinkedList<LeerberichtMitglied> getLeerberichtMitglied(int intBereichnr, int intAbschnittnr, String strFubwehr) throws Exception
    {
        LinkedList<LeerberichtMitglied> liLeerberichtMitglieder = new LinkedList<>();

        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "";

        sqlString = "SELECT id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\" "
                + "FROM FDISK.dbo.stmkmitglieder";

        if (intAbschnittnr == -2)
        {
            sqlString = "SELECT id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\" "
                    + " FROM FDISK.dbo.stmkmitglieder s INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(s.instanznummer = f.instanznummer) "
                    + " WHERE f.Bereich_Nr = " + intBereichnr;
        } else
        {
            if (strFubwehr.equals("-2"))
            {
                sqlString = "SELECT id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\" "
                        + " FROM FDISK.dbo.stmkmitglieder s INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(s.instanznummer = f.instanznummer) "
                        + " WHERE f.abschnitt_instanznummer = " + intAbschnittnr;
            } else
            {
                sqlString = "SELECT id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\" "
                        + "FROM FDISK.dbo.stmkmitglieder "
                        + "WHERE instanznummer = '" + strFubwehr + "'";
            }
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

        String sqlString = "SELECT convert(char, einsatzzeit_bis - einsatzzeit_von, 114) \"Einsatzzeit\""
                + " FROM FDISK.dbo.stmkeinsatzberichtemitglieder WHERE id_mitgliedschaften = 219782"; //WHERE id_mitgliedschaften = "+mitglied.getIntIdMitgliedschaften();

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

    /**
     * Liefert alle Fahrzeuge für einen Leerbericht zurück
     *
     * @return
     * @throws Exception
     */
    public LinkedList<LeerberichtFahrzeug> getLeerberichtFahrzeug(int intBereichnr, int intAbschnittnr, String strFubwehr) throws Exception
    {
        LinkedList<LeerberichtFahrzeug> liFahrzeuge = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString;
//        String sqlString = "SELECT "
//                + "kennzeichen \"Kennzeichen\" "
//                + ",id_fahrzeuge \"Id_Fahrzeuge\" "
//                + ",fahrzeugtyp \"Fahrzeugtyp\" "
//                + ",taktischebezeichnung \"Taktische Bezeichnung\" "
//                + ",bezeichnung \"Bezeichnung\" "
//                + ",status \"Status\" "
//                + ",baujahr \"Baujahr\" "
//                + ",fahrzeugmarke \"Fahrzeugmarke\" "
//                + ",aufbaufirma \"Aufbaufirma\""
//                + ",fzg.instanznummer \"Instanzummer\" "
//                + "FROM FDISK.dbo.stmkfahrzeuge fzg INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(fzg.instanznummer = f.instanznummer) "
//                + "WHERE status = 'aktiv'";

        if (intAbschnittnr == -2)
        {

            sqlString = "SELECT "
                    + "kennzeichen \"Kennzeichen\" "
                    + ",id_fahrzeuge \"Id_Fahrzeuge\" "
                    + ",fahrzeugtyp \"Fahrzeugtyp\" "
                    + ",taktischebezeichnung \"Taktische Bezeichnung\" "
                    + ",bezeichnung \"Bezeichnung\" "
                    + ",status \"Status\" "
                    + ",baujahr \"Baujahr\" "
                    + ",fahrzeugmarke \"Fahrzeugmarke\" "
                    + ",aufbaufirma \"Aufbaufirma\""
                    + ",fzg.instanznummer \"Instanzummer\" "
                    + "FROM FDISK.dbo.stmkfahrzeuge fzg INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(fzg.instanznummer = f.instanznummer) "
                    + "WHERE status = 'aktiv' "
                    + "AND f.Bereich_Nr = " + intBereichnr;
        } else
        {
            if (strFubwehr.equals("-2"))
            {
                sqlString = "SELECT "
                        + "kennzeichen \"Kennzeichen\" "
                        + ",id_fahrzeuge \"Id_Fahrzeuge\" "
                        + ",fahrzeugtyp \"Fahrzeugtyp\" "
                        + ",taktischebezeichnung \"Taktische Bezeichnung\" "
                        + ",bezeichnung \"Bezeichnung\" "
                        + ",status \"Status\" "
                        + ",baujahr \"Baujahr\" "
                        + ",fahrzeugmarke \"Fahrzeugmarke\" "
                        + ",aufbaufirma \"Aufbaufirma\""
                        + ",fzg.instanznummer \"Instanzummer\" "
                        + "FROM FDISK.dbo.stmkfahrzeuge fzg INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(fzg.instanznummer = f.instanznummer) "
                        + "WHERE status = 'aktiv' "
                        + "AND f.abschnitt_instanznummer = " + intAbschnittnr;
            } else
            {
                sqlString = "SELECT "
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
                        + "WHERE status = 'aktiv' "
                        + "AND instanznummer = '" + strFubwehr + "'";
            }
        }

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
     * Liefert den Teil des SQL Strings, der für die Datumsabfrage benötigt
     * wird, zurück
     *
     * intBericht = 1 => Einsatzbericht intBericht = 2 => Taetigkeitsbericht
     * intBericht = 3 => Uebungsbericht intBericht = 4 => Kursstatistik
     *
     * @param strVon
     * @param strBis
     * @param intBericht
     * @param boWhere
     * @return
     */
    public String getSqlDateString(String strVon, String strBis, int intBericht, boolean boWhere)
    {

        String dateString = "";

        if (strBis.isEmpty() && strBis.equals("") && strVon.isEmpty() && strVon.equals(""))
        {
            return "";
        }

        if (boWhere)
        {
            dateString += " WHERE";
        } else if (!boWhere)
        {
            dateString += " AND";
        }

        if (intBericht == 1)
        {
            if ((strVon.isEmpty() || strVon.equals("")) && (!strBis.isEmpty() || !strBis.equals("")))
            {
                dateString += " uhrzeit_rueckkehr < (CAST('" + strBis + " 00:00.000' AS DATETIME)+1)";

            } else if (strBis.isEmpty() || strBis.equals("") && (!strVon.isEmpty() || !strVon.equals("")))
            {
                dateString += " uhrzeit_alarmierung >= CAST('" + strVon + " 00:00.000' AS DATETIME)";

            } else if (!strBis.isEmpty() && !strBis.equals("") && !strVon.isEmpty() && !strVon.equals(""))
            {
                dateString += " uhrzeit_alarmierung >= CAST('" + strVon + " 00:00.000' AS DATETIME) AND uhrzeit_rueckkehr < (CAST('" + strBis + " 00:00.000' AS DATETIME)+1)";
            }
        } else if (intBericht == 2 || intBericht == 3)
        {
            if ((strVon.isEmpty() || strVon.equals("")) && (!strBis.isEmpty() || !strBis.equals("")))
            {
                dateString += " (ende < (CAST('" + strBis + " 00:00.000' AS DATETIME)+1))";

            } else if (strBis.isEmpty() || strBis.equals("") && (!strVon.isEmpty() || !strVon.equals("")))
            {
                dateString += " (beginn >= CAST('" + strVon + " 00:00.000' AS DATETIME))";
            } else if (!strBis.isEmpty() && !strBis.equals("") && !strVon.isEmpty() && !strVon.equals(""))
            {
                dateString += " (beginn >= CAST('" + strVon + " 00:00.000' AS DATETIME) AND ende < (CAST('" + strBis + " 00:00.000' AS DATETIME)+1))";
            }
        } else if (intBericht == 4)
        {
            if ((strVon.isEmpty() || strVon.equals("")) && (!strBis.isEmpty() || !strBis.equals("")))
            {
                dateString += " (datum < (CAST('" + strBis + " 00:00.000' AS DATETIME)+1))";

            } else if (strBis.isEmpty() || strBis.equals("") && (!strVon.isEmpty() || !strVon.equals("")))
            {
                dateString += " (datum >= CAST('" + strVon + " 00:00.000' AS DATETIME))";
            } else if (!strBis.isEmpty() && !strBis.equals("") && !strVon.isEmpty() && !strVon.equals(""))
            {
                dateString += " (datum >= CAST('" + strVon + " 00:00.000' AS DATETIME) AND datum < (CAST('" + strBis + " 00:00.000' AS DATETIME)+1))";
            }
        }

        return dateString;
    }

    /**
     * Gibt eine Liste mit allen Tätigkeitsberichten zurück
     *
     * @param strVon
     * @param strBis
     * @return
     * @throws Exception
     */
    public LinkedList<Taetigkeitsbericht> getTaetigkeitsbericht(String strVon, String strBis, int intBereichnr, int intAbschnittnr, String strFubwehr) throws Exception
    {
        LinkedList<Taetigkeitsbericht> liTaetigkeitsbericht = new LinkedList<>();

        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "";

        if (intAbschnittnr == -2)
        {
            sqlString = "SELECT DISTINCT id_berichte \"ID\""
                    + " ,tb.instanznummer \"Instanznummer\""
                    + " ,tb.instanzname \"Instanzname\""
                    + " ,taetigkeitsart \"Taetigkeitsart\""
                    + " ,taetigkeitsunterart \"Taetigkeitsunterart\""
                    + " ,nummer \"Nummer\""
                    + " ,beginn \"Beginn\""
                    + " ,ende \"Ende\""
                    + " ,strasse \"Strasse\""
                    + " ,nummeradr \"NummerAdr\""
                    + " ,stiege \"Stiege\""
                    + " ,plz \"PLZ\""
                    + " ,ort \"Ort\""
                    + " ,meldung \"Meldung\""
                    + " ,Fehlalarm \"Fehlalarm\""
                    + " FROM FDISK.dbo.stmktaetigkeitsberichte tb INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(tb.instanznummer = f.instanznummer)"
                    + " WHERE f.Bereich_Nr = " + intBereichnr;
        } else
        {
            if (strFubwehr.equals("-2"))
            {
                sqlString = "SELECT DISTINCT id_berichte \"ID\""
                        + " ,tb.instanznummer \"Instanznummer\""
                        + " ,tb.instanzname \"Instanzname\""
                        + " ,taetigkeitsart \"Taetigkeitsart\""
                        + " ,taetigkeitsunterart \"Taetigkeitsunterart\""
                        + " ,nummer \"Nummer\""
                        + " ,beginn \"Beginn\""
                        + " ,ende \"Ende\""
                        + " ,strasse \"Strasse\""
                        + " ,nummeradr \"NummerAdr\""
                        + " ,stiege \"Stiege\""
                        + " ,plz \"PLZ\""
                        + " ,ort \"Ort\""
                        + " ,meldung \"Meldung\""
                        + " ,Fehlalarm \"Fehlalarm\""
                        + " FROM FDISK.dbo.stmktaetigkeitsberichte tb INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(tb.instanznummer = f.instanznummer)"
                        + " WHERE f.abschnitt_instanznummer = " + intAbschnittnr;
            } else
            {
                sqlString = "SELECT DISTINCT id_berichte \"ID\""
                        + " ,instanznummer \"Instanznummer\""
                        + " ,instanzname \"Instanzname\""
                        + " ,taetigkeitsart \"Taetigkeitsart\""
                        + " ,taetigkeitsunterart \"Taetigkeitsunterart\""
                        + " ,nummer \"Nummer\""
                        + " ,beginn \"Beginn\""
                        + " ,ende \"Ende\""
                        + " ,strasse \"Strasse\""
                        + " ,nummeradr \"NummerAdr\""
                        + " ,stiege \"Stiege\""
                        + " ,plz \"PLZ\""
                        + " ,ort \"Ort\""
                        + " ,meldung \"Meldung\""
                        + " ,Fehlalarm \"Fehlalarm\""
                        + " FROM FDISK.dbo.stmktaetigkeitsberichte"
                        + " WHERE instanznummer = '" + strFubwehr + "'";
            }
        }

        sqlString += getSqlDateString(strVon, strBis, 2, true);

        ResultSet rs = stat.executeQuery(sqlString);

        int intIdBericht;
        int intInstanznummer;
        String strInstanzname;
        String strTaetigkeitsart;
        String strTaetigkeitsunterart;
        String strNummer;
        Date dateBeginn;
        Date dateEnde;
        String strStrasse;
        String strNummerAdr;
        String strStiege;
        String strPlz;
        String strOrt;
        String strMeldung;
        String strFehlalarm;

        while (rs.next())
        {
            intIdBericht = rs.getInt("ID");
            intInstanznummer = rs.getInt("Instanznummer");
            strInstanzname = rs.getString("Instanzname");
            strTaetigkeitsart = rs.getString("Taetigkeitsart");
            strTaetigkeitsunterart = rs.getString("Taetigkeitsunterart");
            strNummer = rs.getString("Nummer");
            dateBeginn = rs.getTimestamp("Beginn");
            dateEnde = rs.getTimestamp("Ende");
            strStrasse = rs.getString("Strasse");
            strNummerAdr = rs.getString("NummerAdr");
            strStiege = rs.getString("Stiege");
            strPlz = rs.getString("PLZ");
            strOrt = rs.getString("Ort");
            strMeldung = rs.getString("Meldung");
            strFehlalarm = rs.getString("Fehlalarm");

            if (strTaetigkeitsart.equals("Jugendübung-NICHT VERWENDEN!ALS TÄTIGKEIT ERFASSEN"))
            {
                strTaetigkeitsart = "Jugendübung";
            }
            Taetigkeitsbericht taetigkeitsbericht = new Taetigkeitsbericht(intIdBericht, intInstanznummer, strInstanzname, strTaetigkeitsart, strTaetigkeitsunterart, strNummer, dateBeginn, dateEnde, strStrasse, strNummerAdr, strStiege, strPlz, strOrt, strMeldung, strFehlalarm);
            liTaetigkeitsbericht.add(taetigkeitsbericht);
        }

        connPool.releaseConnection(conn);
        return liTaetigkeitsbericht;
    }

    /**
     * Gibt eine Liste mit allen Einsatzberichten zurück
     *
     * @param strVon
     * @param strBis
     * @return
     * @throws Exception
     */
    public LinkedList<Einsatzbericht> getEinsatzbericht(String strVon, String strBis, int intBereichnr, int intAbschnittnr, String strFubwehr) throws Exception
    {
        LinkedList<Einsatzbericht> liEinsatzbericht = new LinkedList<>();

        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "";

        if (intAbschnittnr == -2)
        {
            sqlString = "SELECT DISTINCT id_berichte \"ID\""
                    + " ,eb.nstanznummer \"Instanznummer\""
                    + " ,name \"Name\""
                    + " ,einsatzart \"Art\""
                    + " ,nummer \"Nr\""
                    + " ,uhrzeit_alarmierung \"Uhrzeit_Alarmierung\""
                    + " ,uhrzeit_rueckkehr \"Uhrzeit_Rueckkehr\""
                    + " ,strasse \"Strasse\""
                    + " ,nummeradr \"Nummeradr\""
                    + " ,stiege \"Stiege\""
                    + " ,plz \"PLZ\""
                    + " ,ort \"Ort\""
                    + " ,standesbuchnummer \"SBN\""
                    + " ,vorname \"Vorname\""
                    + " ,zuname \"Zuname\""
                    + " ,meldung \"Meldung\""
                    + " ,Fehlalarm \"Fehlalarm\""
                    + " FROM FDISK.dbo.stmkeinsatzberichte eb INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(eb.instanznummer = f.instanznummer) "
                    + " WHERE f.Bereich_Nr = " + intBereichnr;
        } else
        {
            if (strFubwehr.equals("-2"))
            {
                sqlString = "SELECT DISTINCT id_berichte \"ID\""
                        + " ,eb.nstanznummer \"Instanznummer\""
                        + " ,name \"Name\""
                        + " ,einsatzart \"Art\""
                        + " ,nummer \"Nr\""
                        + " ,uhrzeit_alarmierung \"Uhrzeit_Alarmierung\""
                        + " ,uhrzeit_rueckkehr \"Uhrzeit_Rueckkehr\""
                        + " ,strasse \"Strasse\""
                        + " ,nummeradr \"Nummeradr\""
                        + " ,stiege \"Stiege\""
                        + " ,plz \"PLZ\""
                        + " ,ort \"Ort\""
                        + " ,standesbuchnummer \"SBN\""
                        + " ,vorname \"Vorname\""
                        + " ,zuname \"Zuname\""
                        + " ,meldung \"Meldung\""
                        + " ,Fehlalarm \"Fehlalarm\""
                        + " FROM FDISK.dbo.stmkeinsatzberichte eb INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(eb.instanznummer = f.instanznummer) "
                        + " WHERE f.abschnitt_instanznummer = " + intAbschnittnr;
            } else
            {
                sqlString = "SELECT DISTINCT id_berichte \"ID\""
                        + " ,instanznummer \"Instanznummer\""
                        + " ,name \"Name\""
                        + " ,einsatzart \"Art\""
                        + " ,nummer \"Nr\""
                        + " ,uhrzeit_alarmierung \"Uhrzeit_Alarmierung\""
                        + " ,uhrzeit_rueckkehr \"Uhrzeit_Rueckkehr\""
                        + " ,strasse \"Strasse\""
                        + " ,nummeradr \"Nummeradr\""
                        + " ,stiege \"Stiege\""
                        + " ,plz \"PLZ\""
                        + " ,ort \"Ort\""
                        + " ,standesbuchnummer \"SBN\""
                        + " ,vorname \"Vorname\""
                        + " ,zuname \"Zuname\""
                        + " ,meldung \"Meldung\""
                        + " ,Fehlalarm \"Fehlalarm\""
                        + " FROM FDISK.dbo.stmkeinsatzberichte"
                        + " WHERE instanznummer = '" + strFubwehr + "'";
            }
        }

        sqlString += getSqlDateString(strVon, strBis, 1, true);

        ResultSet rs = stat.executeQuery(sqlString);

        int intIdBericht;
        int intInstanznummer;
        String strName;
        String strEinsatzart;
        String strNummer;
        Date dateUhrzeit_Alarmierung;
        Date dateUhrzeit_Rueckkehr;
        String strStrasse;
        String strNummerAdr;
        String strStiege;
        String strPlz;
        String strOrt;
        int intStandesbuchnummer;
        String strVorname;
        String strZuname;
        String strMeldung;
        String strFehlalarm;

        while (rs.next())
        {
            intIdBericht = rs.getInt("ID");
            intInstanznummer = rs.getInt("Instanznummer");
            strName = rs.getString("Name");
            strEinsatzart = rs.getString("Art");
            strNummer = rs.getString("Nr");
            dateUhrzeit_Alarmierung = rs.getTimestamp("Uhrzeit_Alarmierung");
            dateUhrzeit_Rueckkehr = rs.getTimestamp("Uhrzeit_Rueckkehr");
            strStrasse = rs.getString("Strasse");
            strNummerAdr = rs.getString("NummerAdr");
            strStiege = rs.getString("Stiege");
            strPlz = rs.getString("PLZ");
            strOrt = rs.getString("Ort");
            intStandesbuchnummer = rs.getInt("SBN");
            strVorname = rs.getString("Vorname");
            strZuname = rs.getString("Zuname");
            strMeldung = rs.getString("Meldung");
            strFehlalarm = rs.getString("Fehlalarm");

            Einsatzbericht einsatzbericht = new Einsatzbericht(intIdBericht,
                    intInstanznummer, strName, strEinsatzart, strNummer,
                    dateUhrzeit_Alarmierung, dateUhrzeit_Rueckkehr, strStrasse,
                    strNummerAdr, strStiege, strPlz, strOrt, intStandesbuchnummer,
                    strVorname, strZuname, strMeldung, strFehlalarm);
            liEinsatzbericht.add(einsatzbericht);
        }

        connPool.releaseConnection(conn);
        return liEinsatzbericht;
    }

    /**
     * Gibt eine Liste mit allen Übungsberichten zurück
     *
     * @param strVon
     * @param strBis
     * @return
     * @throws Exception
     */
    public LinkedList<Uebungsbericht> getUebungsbericht(String strVon, String strBis, int intBereichnr, int intAbschnittnr, String strFubwehr) throws Exception
    {
        LinkedList<Uebungsbericht> liUebungsbericht = new LinkedList<>();

        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "";
        if (intAbschnittnr == -2)
        {
            sqlString = "SELECT DISTINCT id_berichte \"ID\""
                    + " ,ub.instanznummer \"Instanznummer\""
                    + " ,name \"Instanzname\""
                    + " ,uebungsart \"Uebungsart\""
                    + " ,uebungsunterart \"Uebungsunterart\""
                    + " ,nummer \"Nummer\""
                    + " ,beginn \"Beginn\""
                    + " ,ende \"Ende\""
                    + " ,strasse \"Strasse\""
                    + " ,nummeradr \"NummerAdr\""
                    + " ,stiege \"Stiege\""
                    + " ,plz \"PLZ\""
                    + " ,ort \"Ort\""
                    + " ,meldung \"Meldung\""
                    + " ,Fehlalarm \"Fehlalarm\""
                    + " FROM FDISK.dbo.stmkuebungsberichte ub INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(ub.instanznummer = f.instanznummer)"
                    + " WHERE f.Bereich_Nr = " + intBereichnr;
        } else
        {
            if (strFubwehr.equals("-2"))
            {
                sqlString = "SELECT DISTINCT id_berichte \"ID\""
                        + " ,ub.instanznummer \"Instanznummer\""
                        + " ,name \"Instanzname\""
                        + " ,uebungsart \"Uebungsart\""
                        + " ,uebungsunterart \"Uebungsunterart\""
                        + " ,nummer \"Nummer\""
                        + " ,beginn \"Beginn\""
                        + " ,ende \"Ende\""
                        + " ,strasse \"Strasse\""
                        + " ,nummeradr \"NummerAdr\""
                        + " ,stiege \"Stiege\""
                        + " ,plz \"PLZ\""
                        + " ,ort \"Ort\""
                        + " ,meldung \"Meldung\""
                        + " ,Fehlalarm \"Fehlalarm\""
                        + " FROM FDISK.dbo.stmkuebungsberichte ub INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(ub.instanznummer = f.instanznummer)"
                        + " WHERE f.abschnitt_instanznummer = " + intAbschnittnr;
            } else
            {
                sqlString = "SELECT DISTINCT id_berichte \"ID\""
                        + " ,instanznummer \"Instanznummer\""
                        + " ,name \"Instanzname\""
                        + " ,uebungsart \"Uebungsart\""
                        + " ,uebungsunterart \"Uebungsunterart\""
                        + " ,nummer \"Nummer\""
                        + " ,beginn \"Beginn\""
                        + " ,ende \"Ende\""
                        + " ,strasse \"Strasse\""
                        + " ,nummeradr \"NummerAdr\""
                        + " ,stiege \"Stiege\""
                        + " ,plz \"PLZ\""
                        + " ,ort \"Ort\""
                        + " ,meldung \"Meldung\""
                        + " ,Fehlalarm \"Fehlalarm\""
                        + " FROM FDISK.dbo.stmkuebungsberichte "
                        + " WHERE instanznummer = '" + strFubwehr + "'";
            }
        }
        sqlString += getSqlDateString(strVon, strBis, 3, false);

        ResultSet rs = stat.executeQuery(sqlString);

        int intIdBericht;
        int intInstanznummer;
        String strName;
        String strUebungsart;
        String strUebungsunterart;
        String strNummer;
        Date dateBeginn;
        Date dateEnde;
        String strStrasse;
        String strNummerAdr;
        String strStiege;
        String strPlz;
        String strOrt;
        String strMeldung;
        String strFehlalarm;

        while (rs.next())
        {
            intIdBericht = rs.getInt("ID");
            intInstanznummer = rs.getInt("Instanznummer");
            strName = rs.getString("Instanzname");
            strUebungsart = rs.getString("Uebungsart");
            strUebungsunterart = rs.getString("Uebungsunterart");
            strNummer = rs.getString("Nummer");
            dateBeginn = rs.getTimestamp("Beginn");
            dateEnde = rs.getTimestamp("Ende");
            strStrasse = rs.getString("Strasse");
            strNummerAdr = rs.getString("NummerAdr");
            strStiege = rs.getString("Stiege");
            strPlz = rs.getString("PLZ");
            strOrt = rs.getString("Ort");
            strMeldung = rs.getString("Meldung");
            strFehlalarm = rs.getString("Fehlalarm");

            Uebungsbericht uebungsbericht = new Uebungsbericht(intIdBericht,
                    intInstanznummer, strName, strUebungsart, strUebungsunterart,
                    strNummer, dateBeginn, dateEnde, strStrasse, strNummerAdr,
                    strStiege, strPlz, strOrt, strMeldung, strFehlalarm);
            liUebungsbericht.add(uebungsbericht);
        }

        connPool.releaseConnection(conn);
        return liUebungsbericht;
    }

    /**
     * Gibt alle eine List mit allen Berichten zurück
     *
     * @param strVon
     * @param strBis
     * @return
     * @throws Exception
     */
    public LinkedList<Bericht> getAlleBerichte(String strVon, String strBis, int intBereichnr, int intAbschnittnr, String strFubwehr) throws Exception
    {
        LinkedList<Bericht> liBericht = new LinkedList<>();

        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        StringBuilder sqlString = new StringBuilder();

        // String sqlString = "";
        //Übungsbericht UNION Einsatzbericht UNION Tätigkeitsbericht 
        if (intAbschnittnr == -2)
        {
            sqlString.append(" SELECT DISTINCT id_berichte \"ID\""
                    + " ,ub.instanznummer \"Instanznummer\""
                    + " ,name \"Instanzname\""
                    + " ,uebungsart \"Art\""
                    + " ,nummer \"Nummer\""
                    + " ,beginn \"Beginn\""
                    + " ,ende \"Ende\""
                    + " ,strasse \"Strasse\""
                    + " ,nummeradr \"NummerAdr\""
                    + " ,stiege \"Stiege\""
                    + " ,plz \"PLZ\""
                    + " ,ort \"Ort\""
                    + " ,meldung \"Meldung\""
                    + " ,Fehlalarm \"Fehlalarm\""
                    + " FROM FDISK.dbo.stmkuebungsberichte ub INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(ub.instanznummer = f.instanznummer)"
                    + " WHERE f.Bereich_Nr = " + intBereichnr);
        } else
        {
            if (strFubwehr.equals("-2"))
            {
                sqlString.append(" SELECT DISTINCT id_berichte \"ID\""
                        + " ,ub.instanznummer \"Instanznummer\""
                        + " ,name \"Instanzname\""
                        + " ,uebungsart \"Art\""
                        + " ,nummer \"Nummer\""
                        + " ,beginn \"Beginn\""
                        + " ,ende \"Ende\""
                        + " ,strasse \"Strasse\""
                        + " ,nummeradr \"NummerAdr\""
                        + " ,stiege \"Stiege\""
                        + " ,plz \"PLZ\""
                        + " ,ort \"Ort\""
                        + " ,meldung \"Meldung\""
                        + " ,Fehlalarm \"Fehlalarm\""
                        + " FROM FDISK.dbo.stmkuebungsberichte ub INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(ub.instanznummer = f.instanznummer)"
                        + " WHERE f.abschnitt_instanznummer = " + intAbschnittnr);
            } else
            {
                sqlString.append(" SELECT DISTINCT id_berichte \"ID\""
                        + " ,instanznummer \"Instanznummer\""
                        + " ,name \"Instanzname\""
                        + " ,uebungsart \"Art\""
                        + " ,nummer \"Nummer\""
                        + " ,beginn \"Beginn\""
                        + " ,ende \"Ende\""
                        + " ,strasse \"Strasse\""
                        + " ,nummeradr \"NummerAdr\""
                        + " ,stiege \"Stiege\""
                        + " ,plz \"PLZ\""
                        + " ,ort \"Ort\""
                        + " ,meldung \"Meldung\""
                        + " ,Fehlalarm \"Fehlalarm\""
                        + " FROM FDISK.dbo.stmkuebungsberichte"
                        + " WHERE instanznummer = '" + strFubwehr + "'");
            }
        }

        sqlString.append(getSqlDateString(strVon, strBis, 3, true));

        if (intAbschnittnr == -2)
        {
            sqlString.append(" SELECT DISTINCT id_berichte \"ID\""
                    + " ,eb.nstanznummer \"Instanznummer\""
                    + " ,name \"Name\""
                    + " ,einsatzart \"Art\""
                    + " ,nummer \"Nr\""
                    + " ,uhrzeit_alarmierung \"Uhrzeit_Alarmierung\""
                    + " ,uhrzeit_rueckkehr \"Uhrzeit_Rueckkehr\""
                    + " ,strasse \"Strasse\""
                    + " ,nummeradr \"Nummeradr\""
                    + " ,stiege \"Stiege\""
                    + " ,plz \"PLZ\""
                    + " ,ort \"Ort\""
                    + " ,standesbuchnummer \"SBN\""
                    + " ,vorname \"Vorname\""
                    + " ,zuname \"Zuname\""
                    + " ,meldung \"Meldung\""
                    + " ,Fehlalarm \"Fehlalarm\""
                    + " FROM FDISK.dbo.stmkeinsatzberichte eb INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(eb.instanznummer = f.instanznummer) "
                    + " WHERE f.Bereich_Nr = " + intBereichnr);
        } else
        {
            if (strFubwehr.equals("-2"))
            {
                sqlString.append(" SELECT DISTINCT id_berichte \"ID\""
                        + " ,eb.nstanznummer \"Instanznummer\""
                        + " ,name \"Name\""
                        + " ,einsatzart \"Art\""
                        + " ,nummer \"Nr\""
                        + " ,uhrzeit_alarmierung \"Uhrzeit_Alarmierung\""
                        + " ,uhrzeit_rueckkehr \"Uhrzeit_Rueckkehr\""
                        + " ,strasse \"Strasse\""
                        + " ,nummeradr \"Nummeradr\""
                        + " ,stiege \"Stiege\""
                        + " ,plz \"PLZ\""
                        + " ,ort \"Ort\""
                        + " ,standesbuchnummer \"SBN\""
                        + " ,vorname \"Vorname\""
                        + " ,zuname \"Zuname\""
                        + " ,meldung \"Meldung\""
                        + " ,Fehlalarm \"Fehlalarm\""
                        + " FROM FDISK.dbo.stmkeinsatzberichte eb INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(eb.instanznummer = f.instanznummer) "
                        + " WHERE f.abschnitt_instanznummer = " + intAbschnittnr);
            } else
            {
                sqlString.append(" SELECT DISTINCT id_berichte \"ID\""
                        + " ,instanznummer \"Instanznummer\""
                        + " ,name \"Name\""
                        + " ,einsatzart \"Art\""
                        + " ,nummer \"Nr\""
                        + " ,uhrzeit_alarmierung \"Uhrzeit_Alarmierung\""
                        + " ,uhrzeit_rueckkehr \"Uhrzeit_Rueckkehr\""
                        + " ,strasse \"Strasse\""
                        + " ,nummeradr \"Nummeradr\""
                        + " ,stiege \"Stiege\""
                        + " ,plz \"PLZ\""
                        + " ,ort \"Ort\""
                        + " ,standesbuchnummer \"SBN\""
                        + " ,vorname \"Vorname\""
                        + " ,zuname \"Zuname\""
                        + " ,meldung \"Meldung\""
                        + " ,Fehlalarm \"Fehlalarm\""
                        + " FROM FDISK.dbo.stmkeinsatzberichte"
                        + " WHERE instanznummer = '" + strFubwehr + "'");
            }
        }

        sqlString.append(getSqlDateString(strVon, strBis, 1, true));

        //Start Tätigkeitsbericht + UNION Übungsberichte Jungend
        if (intAbschnittnr == -2)
        {
            sqlString.append(" SELECT DISTINCT id_berichte \"ID\""
                    + " ,tb.instanznummer \"Instanznummer\""
                    + " ,tb.instanzname \"Instanzname\""
                    + " ,taetigkeitsart \"Taetigkeitsart\""
                    + " ,taetigkeitsunterart \"Taetigkeitsunterart\""
                    + " ,nummer \"Nummer\""
                    + " ,beginn \"Beginn\""
                    + " ,ende \"Ende\""
                    + " ,strasse \"Strasse\""
                    + " ,nummeradr \"NummerAdr\""
                    + " ,stiege \"Stiege\""
                    + " ,plz \"PLZ\""
                    + " ,ort \"Ort\""
                    + " ,meldung \"Meldung\""
                    + " ,Fehlalarm \"Fehlalarm\""
                    + " FROM FDISK.dbo.stmktaetigkeitsberichte tb INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(tb.instanznummer = f.instanznummer)"
                    + " WHERE f.Bereich_Nr = " + intBereichnr);
        } else
        {
            if (strFubwehr.equals("-2"))
            {
                sqlString.append(" SELECT DISTINCT id_berichte \"ID\""
                        + " ,tb.instanznummer \"Instanznummer\""
                        + " ,tb.instanzname \"Instanzname\""
                        + " ,taetigkeitsart \"Taetigkeitsart\""
                        + " ,taetigkeitsunterart \"Taetigkeitsunterart\""
                        + " ,nummer \"Nummer\""
                        + " ,beginn \"Beginn\""
                        + " ,ende \"Ende\""
                        + " ,strasse \"Strasse\""
                        + " ,nummeradr \"NummerAdr\""
                        + " ,stiege \"Stiege\""
                        + " ,plz \"PLZ\""
                        + " ,ort \"Ort\""
                        + " ,meldung \"Meldung\""
                        + " ,Fehlalarm \"Fehlalarm\""
                        + " FROM FDISK.dbo.stmktaetigkeitsberichte tb INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(tb.instanznummer = f.instanznummer)"
                        + " WHERE f.abschnitt_instanznummer = " + intAbschnittnr);
            } else
            {
                sqlString.append(" SELECT DISTINCT id_berichte \"ID\""
                        + " ,instanznummer \"Instanznummer\""
                        + " ,instanzname \"Instanzname\""
                        + " ,taetigkeitsart \"Taetigkeitsart\""
                        + " ,taetigkeitsunterart \"Taetigkeitsunterart\""
                        + " ,nummer \"Nummer\""
                        + " ,beginn \"Beginn\""
                        + " ,ende \"Ende\""
                        + " ,strasse \"Strasse\""
                        + " ,nummeradr \"NummerAdr\""
                        + " ,stiege \"Stiege\""
                        + " ,plz \"PLZ\""
                        + " ,ort \"Ort\""
                        + " ,meldung \"Meldung\""
                        + " ,Fehlalarm \"Fehlalarm\""
                        + " FROM FDISK.dbo.stmktaetigkeitsberichte"
                        + " WHERE instanznummer = '" + strFubwehr + "'");
            }
        }

        sqlString.append(getSqlDateString(strVon, strBis, 2, true));

        sqlString.append(" ORDER BY beginn");

        ResultSet rs = stat.executeQuery(sqlString.toString());

        int intIdBericht;
        int intInstanznummer;
        String strName;
        String strArt;
        String strNummer;
        Date dateBeginn;
        Date dateEnde;
        String strStrasse;
        String strNummerAdr;
        String strStiege;
        String strPlz;
        String strOrt;
        String strMeldung;
        String strFehlalarm;

        while (rs.next())
        {
            intIdBericht = rs.getInt("ID");
            intInstanznummer = rs.getInt("Instanznummer");
            strName = rs.getString("Instanzname");
            strArt = rs.getString("Art");
            strNummer = rs.getString("Nummer");
            dateBeginn = rs.getTimestamp("Beginn");
            dateEnde = rs.getTimestamp("Ende");
            strStrasse = rs.getString("Strasse");
            strNummerAdr = rs.getString("NummerAdr");
            strStiege = rs.getString("Stiege");
            strPlz = rs.getString("PLZ");
            strOrt = rs.getString("Ort");
            strMeldung = rs.getString("Meldung");
            strFehlalarm = rs.getString("Fehlalarm");

            if (strArt.equals("Jugendübung-NICHT VERWENDEN!ALS TÄTIGKEIT ERFASSEN"))
            {
                strArt = "Jugendübung";
            }

            Bericht bericht = new Bericht(intIdBericht,
                    intInstanznummer, strName, strArt,
                    strNummer, dateBeginn, dateEnde, strStrasse, strNummerAdr,
                    strStiege, strPlz, strOrt, strMeldung, strFehlalarm);
            liBericht.add(bericht);
        }

        connPool.releaseConnection(conn);
        return liBericht;
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
     * @return
     * @throws Exception
     */
    public HashMap<String, LinkedList<String>> getMethodeFuerTyp() throws Exception
    {
        HashMap<String, LinkedList<String>> hmAlleTypenUndFilter = new HashMap<>();
        hmAlleTypenUndFilter.put("KURSBEZEICHNUNG", getFilterFuerKursbezeichnung());
        hmAlleTypenUndFilter.put("FUNKTIONSBEZEICHNUNG", getFilterFuerFunktionsbezeichnung());
        hmAlleTypenUndFilter.put("FUNKTIONSINSTANZ", getFilterFuerFunktionsinstanz());
        hmAlleTypenUndFilter.put("STATUS", getFilterFuerStatus());
        hmAlleTypenUndFilter.put("ERREICHBARKEITSART", getFilterFuerErreichbarkeitsart());
        hmAlleTypenUndFilter.put("STAATSBUERGERSCHAFT", getFilterFuerStaatsbuergerschaft());
        hmAlleTypenUndFilter.put("ANREDE", getFilterFuerAnrede());
        hmAlleTypenUndFilter.put("AUSZEICHNUNGSSTUFE", getFilterFuerAuszeichnungsstufe());
        hmAlleTypenUndFilter.put("AUSZEICHNUNGSART", getFilterFuerAuszeichnung());
        hmAlleTypenUndFilter.put("LEISTUNGSABZEICHENBEZEICHNUNG", getFilterFuerLeistungsabzeichenbezeichnung());
        hmAlleTypenUndFilter.put("LEISTUNGSABZEICHEN STUFE", getFilterFuerLeistungsabzeichenstufe());
        hmAlleTypenUndFilter.put("ISCO-BERUF", getFilterFuerBeruf());
        hmAlleTypenUndFilter.put("GESCHLECHT", getFilterFuerGeschlecht());
        hmAlleTypenUndFilter.put("TITEL", getFilterFuerTitel());
        hmAlleTypenUndFilter.put("AMTSTITEL", getFilterFuerAmtstitel());
        hmAlleTypenUndFilter.put("BLUTGRUPPE", getFilterFuerBlutgruppe());
        hmAlleTypenUndFilter.put("FAMILIENSTAND", getFilterFuerFamilienstand());
        hmAlleTypenUndFilter.put("DIENSTGRAD", getFilterFuerDienstgrad());
        hmAlleTypenUndFilter.put("FÜHRERSCHEINKLASSE", getFilterFuerFuehrerscheinklassen());
        hmAlleTypenUndFilter.put("UNTERSUCHUNGSART", getFilterFuerUntersuchungsart());
        return hmAlleTypenUndFilter;
    }
    
    public LinkedList<String> getFilterFuerGeschlecht() throws Exception
    {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerGeschlecht.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next())
        {
            String strFilter = rs.getString("Geschlecht");
            if (!strFilter.isEmpty() && !strFilter.equals(" "))
            {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);

        return liFilter;
    }

    public LinkedList<String> getFilterFuerStaatsbuergerschaft() throws Exception
    {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerStaatsbuergerschaft.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next())
        {
            String strFilter = rs.getString("Staatsbuergerschaft");
            if (!strFilter.isEmpty() && !strFilter.equals(" "))
            {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);

        return liFilter;
    }

    public LinkedList<String> getFilterFuerBeruf() throws Exception
    {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerBeruf.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next())
        {
            String strFilter = rs.getString("Beruf");
            if (!strFilter.isEmpty() && !strFilter.equals(" "))
            {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);

        return liFilter;
    }

    public LinkedList<String> getFilterFuerTitel() throws Exception
    {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerTitel.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next())
        {
            String strFilter = rs.getString("Titel");
            if (!strFilter.isEmpty() && !strFilter.equals(" "))
            {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);

        return liFilter;
    }

    public LinkedList<String> getFilterFuerAmtstitel() throws Exception
    {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerAmtstitel.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next())
        {
            String strFilter = rs.getString("Amtstitel");
            if (!strFilter.isEmpty() && !strFilter.equals(" "))
            {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);

        return liFilter;
    }

    public LinkedList<String> getFilterFuerBlutgruppe() throws Exception
    {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerBlutgruppe.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next())
        {
            String strFilter = rs.getString("Blutgruppe");
            if (!strFilter.isEmpty() && !strFilter.equals(" "))
            {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);

        return liFilter;
    }

    public LinkedList<String> getFilterFuerFamilienstand() throws Exception
    {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerFamilienstand.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next())
        {
            String strFilter = rs.getString("Familienstand");
            if (!strFilter.isEmpty() && !strFilter.equals(" "))
            {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);

        return liFilter;
    }

    public LinkedList<String> getFilterFuerDienstgrad() throws Exception
    {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerDienstgrade.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next())
        {
            String strFilter = rs.getString("Dienstgrad");
            if (!strFilter.isEmpty() && !strFilter.equals(" "))
            {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);

        return liFilter;
    }

    /**
     * Sucht den passenden Filter für den Typ "Anrede"
     *
     * @return
     * @throws Exception
     */
    public LinkedList<String> getFilterFuerAnrede() throws Exception
    {
        LinkedList<String> liFilter = new LinkedList<>();
        liFilter.add("Frau");
        liFilter.add("Herr");

        return liFilter;
    }

    /**
     * Sucht den passenden Filter für Kurse
     *
     * @return
     * @throws Exception
     */
    public LinkedList<String> getFilterFuerKursbezeichnung() throws Exception
    {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerKursbezeichnung.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next())
        {
            String strFilter = rs.getString("Kursbezeichnung");
            if (!strFilter.isEmpty() && !strFilter.equals(" "))
            {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);

        return liFilter;
    }

    /**
     * Sucht den passenden Filter für Funktionen
     *
     * @return
     * @throws Exception
     */
    public LinkedList<String> getFilterFuerFunktionsbezeichnung() throws Exception
    {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerFunktionsbezeichnung.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next())
        {
            String strFilter = rs.getString("Bezeichnung");
            if (!strFilter.isEmpty() && !strFilter.equals(" "))
            {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);

        return liFilter;
    }

    public LinkedList<String> getFilterFuerFunktionsinstanz() throws Exception
    {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerFunktionsinstanz.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next())
        {
            String strFilter = rs.getString("Id_Instanztypen");
            if (!strFilter.isEmpty() && !strFilter.equals(" "))
            {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);

        return liFilter;
    }

    /**
     * Gibt die passenden Filter für die Status zurück
     *
     * @return
     * @throws Exception
     */
    public LinkedList<String> getFilterFuerStatus() throws Exception
    {
        LinkedList<String> liFilter = new LinkedList<>();
        liFilter.add("Abgemeldet");
        liFilter.add("Aktiv");
        liFilter.add("Ehrenmitglied");
        liFilter.add("Jugend");
        liFilter.add("Reserve");

        return liFilter;
    }

    /**
     * Sucht den passenden Filter für Erreichbarkeiten
     *
     * @return
     * @throws Exception
     */
    public LinkedList<String> getFilterFuerErreichbarkeitsart() throws Exception
    {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerErreichbarkeitsart.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next())
        {
            String strFilter = rs.getString("Erreichbarkeitsart");
            if (!strFilter.isEmpty() && !strFilter.equals(" "))
            {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);
        return liFilter;
    }

    /**
     * Gibt die passenden Filter für Auszeichnungen zurück
     *
     * @return
     * @throws Exception
     */
    public LinkedList<String> getFilterFuerAuszeichnung() throws Exception
    {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerAuszeichnung.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next())
        {
            String strFilter = rs.getString("Auszeichnung");
            if (!strFilter.isEmpty() && !strFilter.equals(" "))
            {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);
        return liFilter;
    }

    public LinkedList<String> getFilterFuerAuszeichnungsstufe() throws Exception
    {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerAuszeichnungsstufe.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next())
        {
            String strFilter = rs.getString("Auszeichnungsstufe");
            if (!strFilter.isEmpty() && !strFilter.equals(" "))
            {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);
        return liFilter;
    }

    /**
     * Gibt die passenden Filter für Leistungsabzeichen zurück.
     *
     * @return
     * @throws Exception
     */
    public LinkedList<String> getFilterFuerLeistungsabzeichenstufe() throws Exception
    {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerLeistungsabzeichenstufe.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next())
        {
            String strFilter = rs.getString("Stufe");
            if (!strFilter.isEmpty() && !strFilter.equals(" "))
            {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);
        return liFilter;
    }

    public LinkedList<String> getFilterFuerLeistungsabzeichenbezeichnung() throws Exception
    {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerLeistungsabzeichenbezeichnung.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next())
        {
            String strFilter = rs.getString("Bezeichnung");
            if (!strFilter.isEmpty() && !strFilter.equals(" "))
            {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);
        return liFilter;
    }

    public LinkedList<String> getFilterFuerFuehrerscheinklassen() throws Exception
    {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerFuehrerscheinklassen.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next())
        {
            String strFilter = rs.getString("Fahrgenehmigungsklasse");
            if (!strFilter.isEmpty() && !strFilter.equals(" "))
            {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);
        return liFilter;
    }

    public LinkedList<String> getFilterFuerUntersuchungsart() throws Exception
    {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerUntersuchungsart.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next())
        {
            String strFilter = rs.getString("Expr1");
            if (!strFilter.isEmpty() && !strFilter.equals(" "))
            {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);
        return liFilter;
    }

    public StringBuilder getDynamischerBericht(String strEingabe[][]) throws Exception
    {
        LinkedList<String> liSpaltenUeberschriften = new LinkedList<>();
        String strSpaltenUeberschrift;
        boolean boAdresse = false;
        boolean boAuszeichnung = false;
        boolean boLeistungsabzeichen = false;
        boolean boKurse = false;
        boolean boErreichbarkeiten = false;
        boolean boFahrgenehmigungen = false;
        boolean boFunktionen = false;
        boolean boUntersuchungen = false;
        boAnrede = false;
        boolean boVordienstzeit = false;

        int intRows = strEingabe.length % 6;

        initializeDBValuesWithDBTypes();

        for (int i = 0; i < intRows; i++)
        {
            for (int j = 0; j < 6; j++)
            {
                switch (strEingabe[i][j].toUpperCase())
                {
                    case "[":
                        strEingabe[i][j] = "(";
                        break;
                    case "]":
                        strEingabe[i][j] = ")";
                        break;
                    case "{":
                        strEingabe[i][j] = "(";
                        break;
                    case "}":
                        strEingabe[i][j] = ")";
                        break;
                    case "NACHNAME":
                        strEingabe[i][j] = "Zuname";
                        break;
                    case "ANREDE":
                        strEingabe[i][j] = "Geschlecht";
                        boAnrede = true;
                        break;
                    case "AUSZEICHNUNGSDATUM":
                        strEingabe[i][j] = "Verleihungsdatum";
                        break;
                    case "LEISTUNGSABZEICHENBEZEICHNUNG":
                        strEingabe[i][j] = "Bezeichnung";
                        break;
                    case "LEISTUNGSABZEICHEN STUFE":
                        strEingabe[i][j] = "Stufe";
                        break;
                    case "LEISTUNGSABZEICHENDATUM":
                        strEingabe[i][j] = "lam.Datum";
                        break;
                    case "KURSDATUM":
                        strEingabe[i][j] = "k.Datum";
                        break;
                    case "ERREICHBARKEIT":
                        strEingabe[i][j] = "Code";
                        break;
                    case "FÜHRERSCHEINKLASSE":
                        strEingabe[i][j] = "Fahrgenehmigungsklasse";
                        break;
                    case "FÜHRERSCHEINKLASSE - GÜLTIG BIS":
                        strEingabe[i][j] = "Gueltig_bis";
                        break;
                    case "FUNKTIONSINSTANZ":
                        strEingabe[i][j] = "id_instanztypen";
                        break;
                    case "FUNKTIONSBEZEICHNUNG":
                        strEingabe[i][j] = "f.bezeichnung";
                        break;
                    case "FUNKTION VON":
                        strEingabe[i][j] = "datum_von";
                        break;
                    case "FUNKTION BIS":
                        strEingabe[i][j] = "datum_bis";
                        break;
                    case "STAATSBÜRGERSCHAFT":
                        strEingabe[i][j] = "Staatsbuergerschaft";
                        break;
                    case "ISCO-BERUF":
                        strEingabe[i][j] = "Beruf";
                        break;
                    case "VORDIENSTZEIT IN JAHREN":
                        strEingabe[i][j] = "Vordienstzeit";
                        break;
                    case "UNTERSUCHUNGSART":
                        strEingabe[i][j] = "Expr1";
                        break;
                    case "UNTERSUCHUNGSDATUM":
                        strEingabe[i][j] = "u.Datum";
                        break;
                }
            }
        }

        for (int i = 0; i < intRows; i++)
        {
            strSpaltenUeberschrift = strEingabe[i][1];

            if (strSpaltenUeberschrift.toUpperCase().equals("STATUS"))
            {
                strSpaltenUeberschrift = strEingabe[i][3];
            }

            if (!liSpaltenUeberschriften.contains(strSpaltenUeberschrift))
            {
                liSpaltenUeberschriften.add(strSpaltenUeberschrift);
            }
        }

        String sqlString = "SELECT ";

        if (liSpaltenUeberschriften.contains("Expr1") || liSpaltenUeberschriften.contains("u.Datum"))
        {
            boUntersuchungen = true;
            for (String titel : liSpaltenUeberschriften)
            {
                switch (titel)
                {
                    case "Expr1":
                        sqlString += "u." + titel.toUpperCase() + ", ";
                        break;
                    case "u.Datum":
                        sqlString += titel.toUpperCase() + " AS 'u.Datum', ";
                        break;
                }
            }
        }
        if (liSpaltenUeberschriften.contains("Bezeichnung")
                || liSpaltenUeberschriften.contains("Stufe")
                || liSpaltenUeberschriften.contains("lam.Datum"))
        {
            boLeistungsabzeichen = true;
            for (String titel : liSpaltenUeberschriften)
            {
                switch (titel)
                {
                    case "Bezeichnung":
                    case "Stufe":
                        sqlString += "la." + titel.toUpperCase() + ", ";
                        break;
                    case "lam.Datum":
                        sqlString += titel.toUpperCase() + " AS 'lam.Datum', ";
                        break;
                }
            }
        }
        if (liSpaltenUeberschriften.contains("Kursbezeichnung") || liSpaltenUeberschriften.contains("k.Datum"))
        {
            boKurse = true;
            for (String titel : liSpaltenUeberschriften)
            {
                switch (titel)
                {
                    case "Kursbezeichnung":
                        sqlString += "k." + titel.toUpperCase() + ", ";
                        break;
                    case "k.Datum":
                        sqlString += titel.toUpperCase() + " AS 'k.Datum', ";
                        break;
                }
            }
        }
        if (liSpaltenUeberschriften.contains("id_instanztypen")
                || liSpaltenUeberschriften.contains("datum_von")
                || liSpaltenUeberschriften.contains("datum_bis")
                || liSpaltenUeberschriften.contains("f.bezeichnung"))
        {
            boFunktionen = true;
            for (String titel : liSpaltenUeberschriften)
            {
                switch (titel)
                {
                    case "id_instanztypen":
                        sqlString += "f." + titel.toUpperCase() + ", ";
                        break;
                    case "f.bezeichnung":
                        sqlString += titel.toUpperCase() + " AS 'f.bezeichnung', ";
                        break;
                    case "datum_von":
                    case "datum_bis":
                        sqlString += "fm." + titel.toUpperCase() + ", ";
                        break;
                }
            }
        }
        if (liSpaltenUeberschriften.contains("Fahrgenehmigungsklasse") || liSpaltenUeberschriften.contains("Gueltig_bis"))
        {
            boFahrgenehmigungen = true;
            for (String titel : liSpaltenUeberschriften)
            {
                if (titel.equals("Fahrgenehmigungsklasse") || titel.equals("Gueltig_bis"))
                {
                    sqlString += "gf." + titel.toUpperCase() + ", ";
                }
            }
        }
        if (liSpaltenUeberschriften.contains("Auszeichnungsart")
                || liSpaltenUeberschriften.contains("Auszeichnungsstufe")
                || liSpaltenUeberschriften.contains("Verleihungsdatum"))
        {
            boAuszeichnung = true;
            for (String titel : liSpaltenUeberschriften)
            {
                switch (titel)
                {
                    case "Auszeichnungsart":
                    case "Auszeichnungsstufe":
                        sqlString += "ausz." + titel.toUpperCase() + ", ";
                        break;
                    case "Verleihungsdatum":
                        sqlString += "auszm." + titel.toUpperCase() + ", ";
                        break;
                }
            }
        }

        if (liSpaltenUeberschriften.contains("Code") || liSpaltenUeberschriften.contains("Erreichbarkeitsart"))
        {
            boErreichbarkeiten = true;
            for (String titel : liSpaltenUeberschriften)
            {
                if (titel.equals("Code") || titel.equals("Erreichbarkeitsart"))
                {
                    sqlString += "e." + titel.toUpperCase() + ", ";
                }
            }
        }
        if (liSpaltenUeberschriften.contains("Straße") || liSpaltenUeberschriften.contains("Hausnummer")
                || liSpaltenUeberschriften.contains("Stiege/Stock/Tür") || liSpaltenUeberschriften.contains("Ort"))
        {
            boAdresse = true;
            for (String titel : liSpaltenUeberschriften)
            {
                if (titel.equals("Straße") || titel.equals("Hausnummer") || titel.equals("Stiege/Stock/Tür") || titel.equals("Ort"))
                {
                    sqlString += "a." + titel.toUpperCase() + ", ";
                }
            }
        }

        if (liSpaltenUeberschriften.contains("Vordienstzeit"))
        {
            boVordienstzeit = true;
            for (String titel : liSpaltenUeberschriften)
            {
                if (titel.equals("Vordienstzeit"))
                {
                    sqlString += "z.VD_ZEIT, ";
                }
            }
        }
        if (liSpaltenUeberschriften.contains("Anrede") || liSpaltenUeberschriften.contains("Geschlecht")
                || liSpaltenUeberschriften.contains("Titel") || liSpaltenUeberschriften.contains("Amtstitel")
                || liSpaltenUeberschriften.contains("Vorname") || liSpaltenUeberschriften.contains("Zuname")
                || liSpaltenUeberschriften.contains("Namenszusatz") || liSpaltenUeberschriften.contains("Geburtsdatum")
                || liSpaltenUeberschriften.contains("Geburtsort") || liSpaltenUeberschriften.contains("Alter")
                || liSpaltenUeberschriften.contains("SVNR") || liSpaltenUeberschriften.contains("Staatsbuergerschaft")
                || liSpaltenUeberschriften.contains("Beruf") || liSpaltenUeberschriften.contains("Familienstand")
                || liSpaltenUeberschriften.contains("Blutgruppe") || liSpaltenUeberschriften.contains("Standesbuchnummer")
                || liSpaltenUeberschriften.contains("Eintrittsdatum") || liSpaltenUeberschriften.contains("Dienstalter")
                || liSpaltenUeberschriften.contains("Angelobungsdatum") || liSpaltenUeberschriften.contains("Status")
                || liSpaltenUeberschriften.contains("Dienstgrad"))
        {
            for (String titel : liSpaltenUeberschriften)
            {
                switch (titel)
                {
                    case "Anrede":
                    case "Geschlecht":
                    case "Titel":
                    case "Amtstitel":
                    case "Vorname":
                    case "Zuname":
                    case "Namenszusatz":
                    case "Geburtsdatum":
                    case "Geburtsort":
                    case "SVNR":
                    case "Staatsbuergerschaft":
                    case "Beruf":
                    case "Familienstand":
                    case "Blutgruppe":
                    case "Standesbuchnummer":
                    case "Eintrittsdatum":
                    case "Dienstalter":
                    case "Angelobungsdatum":
                    case "Status":
                    case "Vordienstzeit":
                    case "Dienstgrad":
                        sqlString += "m." + titel.toUpperCase() + ", ";
                        break;
                    case "Alter":
                        sqlString += "DATEDIFF(YY, geburtsdatum, GETDATE()) - CASE WHEN DATEADD(YY, DATEDIFF(YY,geburtsdatum, GETDATE()), geburtsdatum) > GETDATE() THEN 1 ELSE 0 END  AS Lebensalter, ";
                        break;
                }
            }
        }

        if (liSpaltenUeberschriften.contains("Jugend") || liSpaltenUeberschriften.contains("Aktiv")
                || liSpaltenUeberschriften.contains("Reserve") || liSpaltenUeberschriften.contains("Abgemeldet")
                || liSpaltenUeberschriften.contains("Ehrenmitglied"))
        {
            boErreichbarkeiten = true;
            for (String titel : liSpaltenUeberschriften)
            {
                switch (titel)
                {
                    case "Jugend":
                    case "Aktiv":
                    case "Reserve":
                    case "Abgemeldet":
                    case "Ehrenmitglied":
                        sqlString += "m." + titel.toUpperCase() + ", ";
                }
            }
        }

        sqlString = sqlString.substring(0, sqlString.lastIndexOf(",")) + " ";

        sqlString += "FROM FDISK.dbo.stmkmitglieder m ";
        if (boAdresse == true)
        {
            sqlString += " INNER JOIN FDISK.dbo.stmkadressen a ON(a.id_personen = m.id_personen) ";
        }

        if (boAuszeichnung == true)
        {
            sqlString += " INNER JOIN FDISK.dbo.stmkauszeichnungenmitglieder auszm ON(auszm.id_personen = m.id_personen) "
                    + " INNER JOIN FDISK.dbo.stmkauszeichnungen ausz ON(auszm.id_auszeichnungen = ausz.id_auszeichnungen) ";
        }

        if (boLeistungsabzeichen == true)
        {
            sqlString += " INNER JOIN FDISK.dbo.stmkleistungsabzeichenmitglieder lam ON(m.id_personen = lam.id_personen) "
                    + " INNER JOIN FDISK.dbo.stmkleistungsabzeichen la ON(la.id_leistungsabzeichen = lam.id_leistungsabzeichen) ";
        }

        if (boKurse == true)
        {
            sqlString += " INNER JOIN FDISK.dbo.stmkkursemitglieder km ON(m.id_personen = km.id_mitgliedschaften) "
                    + " INNER JOIN FDISK.dbo.stmkkurse k ON (k.id_kurse = km.id_kurse) ";
        }
        if (boErreichbarkeiten == true)
        {
            sqlString += " INNER JOIN FDISK.dbo.stmkerreichbarkeiten e ON(m.id_personen = e.id_personen) ";
        }

        if (boFahrgenehmigungen == true)
        {
            sqlString += " INNER JOIN FDISK.dbo.stmkgesetzl_fahrgenehmigungen gf ON(m.id_personen = gf.fdisk_personen_id) ";
        }

        if (boFunktionen == true)
        {
            sqlString += " INNER JOIN FDISK.dbo.stmkfunktionenmitglieder fm ON(m.id_personen = fm.id_mitgliedschaften) "
                    + " INNER JOIN FDISK.dbo.stmkfunktionen f ON(f.id_funktionen = fm.id_funktionen) ";
        }

        if (boUntersuchungen == true)
        {
            sqlString += " INNER JOIN FDISK.dbo.stmkuntersuchungenmitglieder u ON(m.id_personen = u.id_mitgliedschaften) ";
        }

        if (boVordienstzeit == true)
        {
            sqlString += " INNER JOIN FDISK.dbo.FDISK_MAPPING_VD_ZEIT z ON(m.id_personen = z.id_personen) ";
        }

        sqlString += " WHERE ";

        String strColLink = "";

        for (int i = 0; i < intRows; i++)
        {
            String strColWhere = strEingabe[i][1];
            String strColSymbol = strEingabe[i][2];
            String strColValue = strEingabe[i][3];
            strColLink = strEingabe[i][5];

            String strColWhereType = getDBTypeForValue(strColWhere);

            if (strColSymbol.equals("<>"))
            {
                strColSymbol = "!=";
            }

            switch (strColLink)
            {
                case "UND":
                    strColLink = "AND";
                    break;
                case "ODER":
                    strColLink = "OR";
                    break;
                case "UND NICHT":
                    strColLink = "AND NOT";
                    break;
                case "ODER NICHT":
                    strColLink = "OR NOT";
                    break;
            }

            if (strColWhere.equals("Geschlecht"))
            {
                switch (strColValue)
                {
                    case "Herr":
                        strColValue = "m";
                        break;
                    case "Frau":
                        strColValue = "w";
                        break;
                }
                sqlString += strColWhere + " " + strColSymbol + " '" + strColValue + "' " + strColLink + " ";
                continue;
            }

            //d.h. User gibt eine WHERE clause ein
            if (strColWhere.contains("N/A") || strColSymbol.contains("N/A") || strColValue.contains("N/A"))
            {
                System.out.println(strColWhere);
                System.out.println(strColSymbol);
            } else if (strColWhere.equals("Vordienstzeit"))
            {
                sqlString += "ROUND(VD_ZEIT, 0, 1)" + " " + strColSymbol + strColValue + " ";
            } else if (!strColWhere.equals("Alter") && !strColWhere.equals("Status"))
            {
                switch (strColWhereType)
                {
                    case "datetime":
                        sqlString += strColWhere + " " + strColSymbol + " CAST('" + strColValue + "' AS datetime) " + strColLink + " ";
                        break;
                    case "varchar":
                        sqlString += "UPPER(" + strColWhere + ") " + strColSymbol + " '" + strColValue.toUpperCase() + "' " + strColLink + " ";
                        break;
                    //default bei byte, tinyint, bigint, int
                    default:
                        sqlString += strColWhere + " " + strColSymbol + " '" + strColValue + "' " + strColLink + " ";
                        break;
                }
            } else if (strColWhere.equals("Alter"))
            {
                sqlString += "(DATEDIFF(YY, geburtsdatum, GETDATE()) - CASE WHEN DATEADD(YY, DATEDIFF(YY,geburtsdatum, GETDATE()), geburtsdatum) > GETDATE() THEN 1 ELSE 0 END )" + " " + strColSymbol + " '" + strColValue + "' " + strColLink + " ";
            } else if (strColWhere.equals("Status"))
            {
                sqlString += strColValue + " " + strColSymbol + " '1' ";
            }

        }
        int intIndex = -1;

        if (!strColLink.isEmpty())
        {
            intIndex = sqlString.lastIndexOf(strColLink);
        }

        if (intIndex != -1)
        {
            sqlString = sqlString.substring(0, intIndex) + " ";
        }

        if (sqlString.endsWith("WHERE "))
        {
            sqlString = sqlString.replace("WHERE ", " ");
        }
        if (sqlString.endsWith("AND ") || sqlString.endsWith("AND"))
        {
            sqlString = sqlString.replace("AND ", " ");
        }
        if (sqlString.endsWith("OR ") || sqlString.endsWith("OR"))
        {
            sqlString = sqlString.replace("OR", " ");
        }
        if (sqlString.endsWith("NOT ") || sqlString.endsWith("NOT"))
        {
            sqlString = sqlString.replace("NOT", " ");
        }

        StringBuilder sbHtml = createDynamicReportGeneratorOutput(sqlString, liSpaltenUeberschriften);
        return sbHtml;
    }

    public StringBuilder createDynamicReportGeneratorOutput(String sqlString, LinkedList<String> liSpaltenUeberschriften) throws Exception
    {
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();
        StringBuilder sbHtml = new StringBuilder("");

        ResultSet rs = stat.executeQuery(sqlString);

        sbHtml.append("<table class='ui sortable celled table' id='dyn_table'><thead><tr>");

        for (String str : liSpaltenUeberschriften)
        {
            sbHtml.append("<th data-content='nach ").append(str).append(" sortieren'>");
            if (boAnrede == true && str.equals("Geschlecht"))
            {
                sbHtml.append("Anrede");
            } else
            {
                sbHtml.append(str);
            }
            sbHtml.append("</th>");
        }
        sbHtml.append("</tr></thead><tbody>");

        while (rs.next())
        {
            boolean boBoolean;
            String strString;
            Date dateDate;
            Long loLong;
            Byte byByte;
            int intInt;
            BigDecimal bdBigDecimal;

            sbHtml.append("<tr>");

            for (String str : liSpaltenUeberschriften)
            {
                for (Map.Entry pair : haNamesTypes.entrySet())
                {
                    if (pair.getKey().toString().toUpperCase().equals(str.toUpperCase()))
                    {
                        String strValue = pair.getValue().toString();

                        OUTER:
                        switch (strValue)
                        {
                            case "bit":
                                boBoolean = rs.getBoolean(str);
                                sbHtml.append("<td>");

                                if (boBoolean)
                                {
                                    sbHtml.append("Ja");
                                } else
                                {
                                    sbHtml.append("Nein");
                                }

                                sbHtml.append("</td>");
                                break;
                            case "datetime":
                                dateDate = rs.getDate(str);
                                sbHtml.append("<td>");
                                if (dateDate == null)
                                {
                                    sbHtml.append("Unbekannt");
                                } else
                                {
                                    sbHtml.append(dateDate);
                                }
                                sbHtml.append("</td>");
                                break;
                            case "varchar":
                                strString = rs.getString(str);
                                if (strString.equals(""))
                                {
                                    strString = "Unbekannt";
                                }
                                if (boAnrede == true)
                                {
                                    switch (strString)
                                    {
                                        case "w":
                                            sbHtml.append("<td>");
                                            sbHtml.append("Frau");
                                            sbHtml.append("</td>");
                                            break OUTER;
                                        case "m":
                                            sbHtml.append("<td>");
                                            sbHtml.append("Herr");
                                            sbHtml.append("</td>");
                                            break OUTER;
                                    }
                                } else
                                {
                                    sbHtml.append("<td>");
                                    sbHtml.append(strString);
                                    sbHtml.append("</td>");
                                    break;
                                }
                                sbHtml.append("<td>");
                                sbHtml.append(strString);
                                sbHtml.append("</td>");
                                break;
                            case "bigint":
                                loLong = rs.getLong(str);
                                sbHtml.append("<td>");
                                sbHtml.append(loLong);
                                sbHtml.append("</td>");
                                break;
                            case "tinyint":
                                byByte = rs.getByte(str);
                                sbHtml.append("<td>");
                                sbHtml.append(byByte);
                                sbHtml.append("</td>");
                                break;
                            case "int":
                                intInt = rs.getInt(str);
                                sbHtml.append("<td>");
                                sbHtml.append(intInt);
                                sbHtml.append("</td>");
                                break;
                            case "big decimal":
                                if (str.equals("Vordienstzeit"))
                                {
                                    bdBigDecimal = rs.getBigDecimal("vd_zeit");

                                } else
                                {
                                    bdBigDecimal = rs.getBigDecimal(str);
                                }
                                sbHtml.append("<td>");
                                sbHtml.append(bdBigDecimal);
                                sbHtml.append("</td>");
                                break;
                            case "default-alter":
                                int intAlter = -1;
                                try
                                {
                                    intAlter = Integer.parseInt(rs.getString("Lebensalter"));
                                } catch (NumberFormatException e)
                                {
                                }

                                sbHtml.append("<td>");
                                if (intAlter < 0)
                                {
                                    sbHtml.append("Unbekannt");
                                } else
                                {
                                    sbHtml.append(intAlter);
                                }
                                sbHtml.append("</td>");
                                break;
                        }

                    }
                }
            }
            sbHtml.append("</tr>");
        }

        connPool.releaseConnection(conn);
        sbHtml.append("</tbody></table>");
        return sbHtml;
    }

    public void initializeDBValuesWithDBTypes()
    {
        //Tabelle stmkmitglieder
        haNamesTypes.put("vorname", "varchar");
        haNamesTypes.put("zuname", "varchar");
        haNamesTypes.put("geburtsdatum", "datetime");
        haNamesTypes.put("geschlecht", "varchar");
        haNamesTypes.put("amtstitel", "varchar");
        haNamesTypes.put("namenszusatz", "varchar");
        haNamesTypes.put("geburtsort", "varchar");
        haNamesTypes.put("svnr", "varchar");
        haNamesTypes.put("staatsbuergerschaft", "varchar");
        haNamesTypes.put("beruf", "varchar");
        haNamesTypes.put("familienstand", "varchar");
        haNamesTypes.put("blutgruppe", "varchar");
        haNamesTypes.put("standesbuchnummer", "varchar");
        haNamesTypes.put("eintrittsdatum", "datetime");
        haNamesTypes.put("angelobungsdatum", "datetime");
        haNamesTypes.put("dienstgrad", "varchar");
        haNamesTypes.put("alter", "default-alter");
        haNamesTypes.put("aktiv", "bit");
        haNamesTypes.put("jugend", "bit");
        haNamesTypes.put("reserve", "bit");
        haNamesTypes.put("abgemeldet", "bit");
        haNamesTypes.put("ehrenmitglied", "bit");
        haNamesTypes.put("vordienstzeit", "big decimal");

        //Tabelle stmkadressen
        haNamesTypes.put("ort", "varchar");

        //Tabelle stmkauszeichnungen bzw. stmkauszeichnungenmitglieder
        haNamesTypes.put("verleihungsdatum", "datetime");
        haNamesTypes.put("auszeichnungsstufe", "varchar");
        haNamesTypes.put("auszeichnungsart", "varchar");

        //Tabelle stmkleistungsabzeichen bzw. stmkleistungsabzeichenmitglieder
        haNamesTypes.put("stufe", "varchar");
        haNamesTypes.put("lam.Datum", "datetime");

        //Tabelle stmkleistungsabzeichen bzw. stmkleistungsabzeichenmitglieder
        haNamesTypes.put("k.Datum", "datetime");
        haNamesTypes.put("kursbezeichnung", "varchar");

        //Tabelle stmkerreichbarkeiten
        haNamesTypes.put("code", "varchar");
        haNamesTypes.put("erreichbarkeitsart", "varchar");

        //Tabelle stmkgesetzl_fahrgenehmigungen
        haNamesTypes.put("fahrgenehmigungsklasse", "varchar");
        haNamesTypes.put("gueltig_bis", "datetime");

        //Tabelle stmkfunktionenmitglieder bzw. stmkfunktionen
        haNamesTypes.put("datum_von", "datetime");
        haNamesTypes.put("datum_bis", "datetime");
        haNamesTypes.put("id_instanztypen", "varchar");
        haNamesTypes.put("f.bezeichnung", "varchar");

        //Tabelle stmkuntersuchungenmitglieder
        haNamesTypes.put("Expr1", "varchar");
        haNamesTypes.put("u.datum", "datetime");
    }

    public String getDBTypeForValue(String strValue)
    {
        String strType = "";

        for (Map.Entry e : haNamesTypes.entrySet())
        {
            if (e.getKey().toString().toUpperCase().equals(strValue.toUpperCase()))
            {
                strType = e.getValue().toString();
            }
        }
        return strType;
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

        LinkedList<Mitglied> liMitglied = new LinkedList<>();

        liMitglied = theInstance.getEinfacheMitgliederliste(40, 4001, "-2");
        int i = 0;
        for (Mitglied mitglied : liMitglied)
        {
            //   System.out.println(mitglied.toString());
            i++;
        }
        // System.out.println("COUNT: " + i);

//        HashMap<String, LinkedList<String>> hm = new HashMap<>();
//        LinkedList<Berechtigung> liBerechtigung = new LinkedList<>();
//        int userID = theInstance.getUserID("49001021", "feuer122");
//        System.out.println("UserID: " + userID);
//        liBerechtigung = theInstance.getBerechtigungen(3536);
//        for (Berechtigung berechtigung : liBerechtigung)
//        {
//            System.out.println("Berechtigung: " + berechtigung.getStrBerechtigung());
//            int intBereich = berechtigung.getIntBereich();
//            int intAbschnitt = berechtigung.getIntAbschnitt();
//            String strFubwehr = berechtigung.getStrFubwehr();
//            System.out.println("Bereich: " + intBereich);
//            System.out.println("Abschnitt: " + intAbschnitt);
//            System.out.println("Fubwehr: " + strFubwehr);
//            Bezirk bezirk = theInstance.getBezirk(intBereich);
//            System.out.println("Bereichnummer: " + bezirk.getIntBezirksNummer());
//            System.out.println("Bereichname: " + bezirk.getStrName());
//            LinkedList<Abschnitt> liAbschnitte = bezirk.getLiAbschnitte();
//            for (Abschnitt abschnitt : liAbschnitte)
//            {
//                System.out.println("Abschnitt: " + abschnitt.getStrName());
//            }
//        }
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

//            String[][] dynamisch
//                    =
//                    {
//                        {
//                            "", "Status", "<>", "Reserve", "", "UND"
//                        }
//
////                    };
            String[][] dynamisch
                    =
                    {
                        {
                            "(", "Vordienstzeit in Jahren", "<>", "4", ")", "UND NICHT"
                        }
                    };
//
//            StringBuilder html = theInstance.getDynamischerBericht(dynamisch);
//            System.out.println(html);
// !!!!!!!!!!!!! SUPERDUPER Tests von der allerbesten Yvonne !!!!!!!!!!!!!!!!!!!!!!

            LinkedList<Kurs> li = theInstance.getKursstatistikkurse("01.01.2056", "10.11.2058");

            for (Kurs k : li)
            {
                // System.out.println(k.toString());
            }

// !!!!!!!!!!!!! Ende SUPERDUPER Tests von der allerbesten Yvonne !!!!!!!!!!!!!!!!!!!!!!
        } catch (Exception ex)
        {
            Logger.getLogger(DB_Access.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
