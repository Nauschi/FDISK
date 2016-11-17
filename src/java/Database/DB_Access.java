/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import BL.BL;
import Enum.EnFilterStatements;
import Beans.Abschnitt;
import Beans.Berechtigung;
import Beans.Bericht;
import Beans.Bezirk;
import Beans.Einsatzbericht;
import Beans.Erreichbarkeit;
import Beans.Fahrzeug;
import Beans.Feuerwehr;
import Beans.Geraetetraegermitglied;
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
import Enum.EnAdressliste;
import Enum.EnEinfacheMitgliederliste;
import Enum.EnErreichbarkeitsliste;
import Enum.EnGeburtstagsliste;
import Enum.EnLeerberichtFahrzeug;
import Enum.EnLeerberichtMitglied;
import Enum.EnStaticStatements;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @authors Corinna, Yvonne, Philipp
 */
public class DB_Access {

    private final DB_ConnectionPool connPool;
    private static DB_Access theInstance = null;
    private final HashMap<String, String> haNamesTypes = new HashMap<>();
    private boolean boAnrede = false;
    private BL bl = new BL();

    public static DB_Access getInstance() throws ClassNotFoundException {
        if (theInstance == null) {
            theInstance = new DB_Access();
        }
        return theInstance;
    }

    private DB_Access() throws ClassNotFoundException {
        connPool = DB_ConnectionPool.getInstance();
    }

    //Login
    /**
     * Gibt eine LinkedList mit allen Berechtigungen des Users mit der
     * übergebenen UserID zurück.
     *
     * @param intUserID
     * @return LinkedList
     * @throws Exception
     * @see Berechtigung
     * @see LinkedList
     */
    public LinkedList<Berechtigung> getBerechtigungen(int intUserID) throws Exception {
        LinkedList<Berechtigung> liBerechtigungen = new LinkedList<>();
        LinkedList<LoginMitglied> liLoginBerechtigung;
        liLoginBerechtigung = getLoginBerechtigung(intUserID);
        String fubwehr = getFubwehrForUserID(intUserID);
        String feuerwehrname = "";
        String abschnitt;
        String bereich;

        if (!fubwehr.endsWith("601") && fubwehr.length() > 2) {
            feuerwehrname = getNameFuerFubwehr(fubwehr);
        } else {
            int bereichnr = Integer.parseInt(fubwehr.substring(0, 2));
            if (bereichnr < 60) {
                LinkedList<Integer> liAbschnittNummern = getAbschnittNummernFuerBereich(bereichnr);
                LinkedList<String> liFubwehrNummern = getFubwehrNummernFuerAbschnitt(liAbschnittNummern.getFirst());
                fubwehr = liFubwehrNummern.getFirst();
                feuerwehrname = getNameFuerFubwehr(liFubwehrNummern.getFirst());
            } else {
                fubwehr = "-1";
            }
        }
        abschnitt = getAbschnittsnameFuerFubwehr(fubwehr);
        bereich = getBereichsnameFuerFubwehr(fubwehr);
        String strBerechtigung;

        if (liLoginBerechtigung.isEmpty()) {
            strBerechtigung = "Mitglied" + " - " + feuerwehrname;
            Berechtigung berechtigung = new Berechtigung(strBerechtigung, 0, fubwehr, getAbschnittsnummerForFubwehr(fubwehr), getBereichsnummerFuerFubwehr(fubwehr), intUserID);
            liBerechtigungen.add(berechtigung);
        } else {
            for (LoginMitglied loginMitglied : liLoginBerechtigung) {
                String bezeichnung = loginMitglied.getStrGruppe();

                switch (loginMitglied.getIntIDGruppe()) {
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
                        strBerechtigung = "Error";
                }
                Berechtigung berechtigung;
                if (!fubwehr.equals("-1")) {
                    berechtigung = new Berechtigung(strBerechtigung, loginMitglied.getIntIDGruppe(), fubwehr, getAbschnittsnummerForFubwehr(fubwehr), getBereichsnummerFuerFubwehr(fubwehr), intUserID);
                } else {
                    berechtigung = new Berechtigung(strBerechtigung, loginMitglied.getIntIDGruppe(), fubwehr, 4001, 40, intUserID);
                }
                liBerechtigungen.add(berechtigung);
            }
        }
        return liBerechtigungen;
    }

    /**
     * Gibt die UserID für den jeweiligen Username und Passwort zurück
     *
     * @param strUsername
     * @param strPasswort
     * @return
     * @throws SQLException
     * @throws Exception
     */
    public int getUserID(String strUsername, String strPasswort) throws SQLException, Exception {
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnStaticStatements.getUserID.toString());
        prepStat.setString(1, strUsername);
        prepStat.setString(2, strPasswort);
        ResultSet rs = prepStat.executeQuery();

        System.out.println("connPool size - before release: " + connPool.getSizeOfPool());
        System.out.println("num connections: " + connPool.getNum_Conn());
        if (!rs.next()) {
            connPool.releaseConnection(conn);
            return -1;
        }

        int intIDUser = rs.getInt("IDUser");

        connPool.releaseConnection(conn);
        System.out.println("connPool size - after release: " + connPool.getSizeOfPool());
        System.out.println("num connections: " + connPool.getNum_Conn());

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
    public LinkedList<LoginMitglied> getLoginMitglied(String strUsername, String strPasswort) throws SQLException, Exception {
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
    public LinkedList<LoginMitglied> getLoginBerechtigung(int intUserID) throws SQLException, Exception {
        LinkedList<LoginMitglied> liMitglieder = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnStaticStatements.getLoginBerechtigung.toString());
        prepStat.setInt(1, intUserID);

        ResultSet rs = prepStat.executeQuery();

        while (rs.next()) {
            String strFubwehr = rs.getString("Fubwehr");
            int intIDGruppe = rs.getInt("IDGruppe");
            String strBezeichnung = rs.getString("Bezeichnung");
            if (intIDGruppe == 1 || intIDGruppe == 5 || intIDGruppe == 9 || intIDGruppe == 15 || intIDGruppe == 14) {
                intIDGruppe = (intIDGruppe == 14) ? 5 : intIDGruppe;
                LoginMitglied lm = new LoginMitglied(intUserID, strFubwehr, intIDGruppe, strBezeichnung);
                liMitglieder.add(lm);
            }
        }

        connPool.releaseConnection(conn);
        return liMitglieder;
    }

    /**
     * Gibt eine LinkedList mit allen Bereichen aus der gesamten Datenbank
     * zurück.
     *
     * @return LinkedList
     * @throws Exception
     * @see Bereich
     * @see LinkedList
     */
    public LinkedList<Bezirk> getAlleBereiche() throws Exception {
        LinkedList<Bezirk> liBezirke = new LinkedList<>();

        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnStaticStatements.getAlleBereiche.toString());
        ResultSet rs = prepStat.executeQuery();
        int intBezirksnummer;
        String strBezirksname;

        LinkedList<Integer> liAbschnittnummern;

        while (rs.next()) {
            LinkedList<Abschnitt> liAbschnitte = new LinkedList<>();
            intBezirksnummer = rs.getInt("Bezirksnummer");
            strBezirksname = rs.getString("Bezirksname");

            liAbschnittnummern = getAbschnittNummernFuerBereich(intBezirksnummer);
            for (Integer abschnittnummer : liAbschnittnummern) {
                liAbschnitte.add(getAbschnitt(abschnittnummer));
            }
            liBezirke.add(new Bezirk(strBezirksname, intBezirksnummer, liAbschnitte));
        }
        connPool.releaseConnection(conn);

        return liBezirke;
    }

    /**
     * Gibt einen Bereich einer bestimmten Bereichsnummer zurück.
     *
     * @param bereichnummer
     * @return Bereich
     * @throws Exception
     * @see Bezirk
     */
    public Bezirk getBezirk(int bereichnummer) throws Exception {
        LinkedList<Integer> liAbschnittnummern;
        liAbschnittnummern = getAbschnittNummernFuerBereich(bereichnummer);
        LinkedList<Abschnitt> liAbschnitte = new LinkedList<>();
        for (Integer abschnittnummer : liAbschnittnummern) {
            liAbschnitte.add(getAbschnitt(abschnittnummer));
        }
        Bezirk bezirk = new Bezirk(getBereichsnameFuerBereichnnummer(bereichnummer), bereichnummer, liAbschnitte);
        return bezirk;
    }

    /**
     * Gibt einen Abschnitt einer bestimmten Abschnittsnummer zurück.
     *
     * @param abschnittnummer
     * @return Abschnitt
     * @throws Exception
     * @see Abschnitt
     */
    public Abschnitt getAbschnitt(int abschnittnummer) throws Exception {
        LinkedList<String> liFeuerwehrnummern;
        liFeuerwehrnummern = getFubwehrNummernFuerAbschnitt(abschnittnummer);
        LinkedList<Feuerwehr> liFeuerwehren = new LinkedList<>();
        for (String feuerwehrnummer : liFeuerwehrnummern) {
            liFeuerwehren.add(getFeuerwehr(feuerwehrnummer));
        }
        Abschnitt abschnitt = new Abschnitt(getAbschnittsnameFuerAbschnittsnummer(abschnittnummer), abschnittnummer, liFeuerwehren);
        return abschnitt;
    }

    /**
     * Gibt eine Feuerwehr einer bestimmten Feuerwehrnummer zurück.
     *
     * @param feuerwehrnummer
     * @return Feuerwehr
     * @throws Exception
     * @see Feuerwehr
     */
    public Feuerwehr getFeuerwehr(String feuerwehrnummer) throws Exception {
        Feuerwehr feuerwehr = new Feuerwehr(getNameFuerFubwehr(feuerwehrnummer), feuerwehrnummer);
        return feuerwehr;
    }

    /**
     * Gibt die zugehörige Fubwehr einer UserID aus der Tabelle
     * "benutzerdetails" zurück.
     *
     * @param intUserID
     * @return String
     * @throws SQLException
     * @throws Exception
     */
    public String getFubwehrForUserID(int intUserID) throws SQLException, Exception {
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnStaticStatements.getFubwehrForUserID.toString());
        prepStat.setInt(1, intUserID);
        ResultSet rs = prepStat.executeQuery();
        String strFubwehr = "";

        while (rs.next()) {
            strFubwehr = rs.getString("Fubwehr");
        }

        connPool.releaseConnection(conn);
        return strFubwehr;

    }

    /**
     * Gibt die zugehörige Abschnittsnummer einer Fubwehr zurück.
     *
     * @param strFubwehr
     * @return int
     * @throws SQLException
     * @throws Exception
     */
    public int getAbschnittsnummerForFubwehr(String strFubwehr) throws SQLException, Exception {
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnStaticStatements.getAbschnittsnameFuerFubwehr.toString());
        prepStat.setString(1, strFubwehr);
        ResultSet rs = prepStat.executeQuery();
        int intAbschnittsnummer = 0;
        while (rs.next()) {
            intAbschnittsnummer = rs.getInt("Instanznummer");
        }
        connPool.releaseConnection(conn);
        return intAbschnittsnummer;
    }

    /**
     * Gibt den Namen einer Fubwehr/Feuerwehrnummer zurück. (z.B. FF Wagrain)
     *
     * @param strFubwehr
     * @return String
     * @throws SQLException
     * @throws Exception
     */
    public String getNameFuerFubwehr(String strFubwehr) throws SQLException, Exception {
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnStaticStatements.getNameFuerFubwehr.toString());
        prepStat.setString(1, strFubwehr);
        ResultSet rs = prepStat.executeQuery();
        String strName = "";

        while (rs.next()) {
            strName = rs.getString("Name");
        }

        connPool.releaseConnection(conn);
        return strName;
    }

    /**
     * Gibt den zugehörigen Abschnittsnamen für eine Fubwehr/Feuerwehrnummer
     * zurück.
     *
     * @param strFubwehr
     * @return String
     * @throws SQLException
     * @throws Exception
     */
    public String getAbschnittsnameFuerFubwehr(String strFubwehr) throws SQLException, Exception {
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnStaticStatements.getAbschnittsnameFuerFubwehr.toString());
        prepStat.setString(1, strFubwehr);
        ResultSet rs = prepStat.executeQuery();

        String strName = "";
        while (rs.next()) {
            strName = rs.getString("Name");
        }

        connPool.releaseConnection(conn);
        return strName;
    }

    /**
     * Gibt den zugehörigen Bereichsnamen einer Fubwehr/Feuerwehrnummer zurück.
     *
     * @param strFubwehr
     * @return String
     * @throws SQLException
     * @throws Exception
     */
    public String getBereichsnameFuerFubwehr(String strFubwehr) throws SQLException, Exception {
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnStaticStatements.getBereichsnameFuerFubwehr.toString());
        prepStat.setString(1, strFubwehr);
        ResultSet rs = prepStat.executeQuery();
        String strName = "";
        while (rs.next()) {
            strName = rs.getString("Name");
        }

        connPool.releaseConnection(conn);
        return strName;
    }

    /**
     * Gibt die Bereichsnummer des Bereichs, in der die Feuerwehr der
     * übergegebenen Fubwehr/Feuerwehrnummer liegt, zurück.
     *
     * @param strFubwehr
     * @return int
     * @throws SQLException
     * @throws Exception
     */
    public int getBereichsnummerFuerFubwehr(String strFubwehr) throws SQLException, Exception {
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnStaticStatements.getBereichsnummerFuerFubwehr.toString());
        prepStat.setString(1, strFubwehr);
        ResultSet rs = prepStat.executeQuery();
        int intNummer = 0;

        while (rs.next()) {
            intNummer = rs.getInt("Nr");
        }

        connPool.releaseConnection(conn);
        return intNummer;
    }

    /**
     * Gibt den zugehörigen Bereichsnamen einer Bereichsnummer zurück.
     *
     * @param bereichnummer
     * @return String
     * @throws Exception
     */
    public String getBereichsnameFuerBereichnnummer(int bereichnummer) throws Exception {
        String bereichname = " - ";
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnStaticStatements.getBereichsnameFuerBereichnnummer.toString());
        prepStat.setInt(1, bereichnummer);
        ResultSet rs = prepStat.executeQuery();
        while (rs.next()) {
            bereichname = rs.getString("Bereichname");
        }
        connPool.releaseConnection(conn);
        return bereichname;
    }

    /**
     * Gibt den zugehörigen Abschnittsnamen einer Abschnittsnummer zurück.
     *
     * @param abschnittnummer
     * @return String
     * @throws Exception
     */
    public String getAbschnittsnameFuerAbschnittsnummer(int abschnittnummer) throws Exception {
        String abschnittname = " - ";
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnStaticStatements.getAbschnittsnameFuerAbschnittsnummer.toString());
        prepStat.setInt(1, abschnittnummer);
        ResultSet rs = prepStat.executeQuery();
        while (rs.next()) {
            abschnittname = rs.getString("Abschnittname");
        }
        connPool.releaseConnection(conn);
        return abschnittname;
    }

    /**
     * Gibt eine LinkedList aller Abschnittnummern eines Bereichs zurück.
     *
     * @param bereichnummer
     * @return LinkedList
     * @throws Exception
     * @see LinkedList
     */
    public LinkedList<Integer> getAbschnittNummernFuerBereich(int bereichnummer) throws Exception {
        LinkedList<Integer> liAbschnittnummern = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnStaticStatements.getAbschnittNummernFuerBereich.toString());
        prepStat.setInt(1, bereichnummer);
        ResultSet rs = prepStat.executeQuery();
        while (rs.next()) {
            liAbschnittnummern.add(rs.getInt("Abschnittnr"));
        }
        connPool.releaseConnection(conn);
        return liAbschnittnummern;
    }

    /**
     * Gibt eine LinkedList aller Fubwehrnummern eines Abschnitts zurück.
     *
     * @param abschnittnummer
     * @return LinkedList
     * @throws Exception
     * @see LinkedList
     */
    public LinkedList<String> getFubwehrNummernFuerAbschnitt(int abschnittnummer) throws Exception {
        LinkedList<String> liFubwehrnummern = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnStaticStatements.getFubwehrNummernFuerAbschnitt.toString());
        prepStat.setInt(1, abschnittnummer);
        ResultSet rs = prepStat.executeQuery();
        while (rs.next()) {
            liFubwehrnummern.add(rs.getString("Fubwehr"));
        }
        connPool.releaseConnection(conn);
        return liFubwehrnummern;
    }

    //Statische Berichte
    /**
     * Gibt eine LinkedList mit allen Mitgliedern (je nach Berechtigung eines
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
    public LinkedList<Mitglied> getEinfacheMitgliederliste(int intBereichnr, int intAbschnittnr, String strFubwehr) throws Exception {
        LinkedList<Mitglied> liMitglieder = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat;
        if (intBereichnr == -2) {
            prepStat = conn.prepareStatement(EnEinfacheMitgliederliste.getEinfacheMitgliederlisteAlle.toString());
        } else if (intAbschnittnr == -2) {
            prepStat = conn.prepareStatement(EnEinfacheMitgliederliste.getEinfacheMitgliederlisteBereich.toString());
            prepStat.setInt(1, intBereichnr);
        } else if (strFubwehr.equals("-2")) {
            prepStat = conn.prepareStatement(EnEinfacheMitgliederliste.getEinfacheMitgliederlisteAbschnitt.toString());
            prepStat.setInt(1, intAbschnittnr);
        } else {
            prepStat = conn.prepareStatement(EnEinfacheMitgliederliste.getEinfacheMitgliederlisteFubwehr.toString());
            prepStat.setString(1, strFubwehr);
        }

        ResultSet rs = prepStat.executeQuery();

        String strSTB;
        String strDGR;
        String strTitel;
        String strVorname;
        String strZuname;
        int intPersID;
        String strFubNr;

        while (rs.next()) {
            intPersID = rs.getInt("PersID");
            strSTB = rs.getString("STB");
            strDGR = rs.getString("DGR");
            strTitel = rs.getString("Titel");
            strVorname = rs.getString("Vorname");
            strZuname = rs.getString("Zuname");
            strFubNr = rs.getString("Fubwehr");

            Mitglied mitglied = new Mitglied(intPersID, strSTB, strDGR, strTitel, strVorname, strZuname, strFubNr);
            liMitglieder.add(mitglied);
        }

        connPool.releaseConnection(conn);
        return liMitglieder;
    }

    /**
     * Gibt eine Geburtstagsliste aller Mitarbeiter als LinkedList zurück.
     *
     * Gibt eine LinkedList mit allen Mitgliedern inkl. des Alters des Mitglieds
     * im angegebnen Jahr (je nach Berechtigung eines Users) zurück
     *
     * @param intBereichnr
     * @param strFubwehr
     * @param intAbschnittnr
     * @return LinkedList
     * @param intJahr
     * @throws java.lang.Exception
     * @see MitgliedsGeburtstag
     * @see Mitglied
     * @see LinkedList
     */
    public LinkedList<MitgliedsGeburtstag> getGeburtstagsliste(int intJahr, int intBereichnr, int intAbschnittnr, String strFubwehr) throws Exception {
        LinkedList<MitgliedsGeburtstag> liMitgliedsGeburtstage = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat;

        if (intBereichnr == -2) {
            prepStat = conn.prepareStatement(EnGeburtstagsliste.getGeburtstagslisteAlle.toString());
        } else if (intAbschnittnr == -2) {
            prepStat = conn.prepareStatement(EnGeburtstagsliste.getGeburtstagslisteBereich.toString());
            prepStat.setInt(1, intBereichnr);
        } else if (strFubwehr.equals("-2")) {
            prepStat = conn.prepareStatement(EnGeburtstagsliste.getGeburtstagslisteAbschnitt.toString());
            prepStat.setInt(1, intAbschnittnr);
        } else {
            prepStat = conn.prepareStatement(EnGeburtstagsliste.getGeburtstagslisteFubwehr.toString());
            prepStat.setString(1, strFubwehr);
        }

        ResultSet rs = prepStat.executeQuery();

        String strSTB;
        String strDGR;
        String strTitel;
        String strVorname;
        String strZuname;
        int intPersID;
        Date dateGeburtsdatum;
        int intZielalter;
        String strFub;

        while (rs.next()) {
            intPersID = rs.getInt("PersID");
            strSTB = rs.getString("STB");
            strDGR = rs.getString("DGR");
            strTitel = rs.getString("Titel");
            strVorname = rs.getString("Vorname");
            strZuname = rs.getString("Zuname");
            dateGeburtsdatum = new Date(rs.getDate("Geburtsdatum").getTime());
            strFub = rs.getString("Instanznummer");

            Calendar calGeburtsdatum = new GregorianCalendar();
            Calendar current = new GregorianCalendar();

            calGeburtsdatum.setTime(dateGeburtsdatum);
            //int intCurrentYear = intJahr - 1;
            int intCurrentYear = intJahr;
            intZielalter = intCurrentYear - calGeburtsdatum.get(Calendar.YEAR);

            calGeburtsdatum.set(Calendar.YEAR, intCurrentYear);

            current.get(Calendar.YEAR);

//            if (current.after(calGeburtsdatum)) {
//                intZielalter++;
//                calGeburtsdatum.add(Calendar.YEAR, 1);
//            }
            MitgliedsGeburtstag mitgliedsGeb = new MitgliedsGeburtstag(intPersID, strSTB, strDGR, strTitel, strVorname, strZuname, false, dateGeburtsdatum, intZielalter, strFub);
            liMitgliedsGeburtstage.add(mitgliedsGeb);
        }
        connPool.releaseConnection(conn);
        return liMitgliedsGeburtstage;
    }

    /**
     * Gibt eine LinkedList mit allen Mitgliedern inkl. der Dienstzeit des
     * Mitglieds im angegebnen Jahr (je nach Berechtigung eines Users) zurück
     *
     * @param intJahr
     * @param intBereichnr
     * @param intAbschnittnr
     * @param strFubwehr
     * @return LinkedList
     * @throws java.lang.Exception
     * @see Mitglied
     * @see MitgliedsDienstzeit
     * @see LinkedList
     */
    public LinkedList<MitgliedsDienstzeit> getDienstzeitListe(int intJahr, int intBereichnr, int intAbschnittnr, String strFubwehr) throws Exception {
        LinkedList<MitgliedsDienstzeit> liMitgliedsDienstzeiten = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();
        String sqlString;

        if (intBereichnr == -2) {
            sqlString = "SELECT m.id_personen 'PersID', m.standesbuchnummer 'STB', m.dienstgrad 'DGR', m.titel 'Titel', m.vorname 'Vorname', m.zuname 'Zuname', m.geburtsdatum 'Geburtsdatum',  m.datum_abgemeldet 'Datum_abgemeldet', m.eintrittsdatum 'Eintrittsdatum', z.DIENSTZEIT, m.id_instanzen 'Instanzen', m.instanznummer 'Instanznummer' "
                    + " FROM FDISK.dbo.stmkmitglieder m LEFT OUTER JOIN FDISK.dbo.FDISK_MAPPING_DZ_ZEIT z ON(m.id_personen = z.id_personen)"
                    + " WHERE (m.abgemeldet = 0) AND (NOT (LEFT(m.instanznummer, 2) = 'GA')) AND (NOT (LEFT(m.instanzname, 7) = 'FW GAST'))"
                    + " AND m.datum_abgemeldet IS NULL "
                    + " ORDER BY m.zuname, m.vorname";
        } else if (intAbschnittnr == -2) {
            sqlString = "SELECT m.id_personen 'PersID', m.standesbuchnummer 'STB', m.dienstgrad 'DGR', m.titel 'Titel', m.vorname 'Vorname', m.zuname 'Zuname', m.geburtsdatum 'Geburtsdatum',  m.datum_abgemeldet 'Datum_abgemeldet', m.eintrittsdatum 'Eintrittsdatum', z.DIENSTZEIT, m.id_instanzen 'Instanzen', m.instanznummer 'Instanznummer' "
                    + " FROM FDISK.dbo.stmkmitglieder m LEFT OUTER JOIN FDISK.dbo.FDISK_MAPPING_DZ_ZEIT z ON(m.id_personen = z.id_personen)"
                    + " INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(m.instanznummer = f.instanznummer)"
                    + " WHERE (m.abgemeldet = 0) AND (NOT (LEFT(m.instanznummer, 2) = 'GA')) AND (NOT (LEFT(m.instanzname, 7) = 'FW GAST'))"
                    + " AND f.Bereich_Nr = " + intBereichnr
                    + " AND m.datum_abgemeldet IS NULL "
                    + " ORDER BY m.zuname, m.vorname";
        } else if (strFubwehr.equals("-2")) {
            sqlString = "SELECT m.id_personen 'PersID', m.standesbuchnummer 'STB', m.dienstgrad 'DGR', m.titel 'Titel', m.vorname 'Vorname', m.zuname 'Zuname', m.geburtsdatum 'Geburtsdatum',  m.datum_abgemeldet 'Datum_abgemeldet', m.eintrittsdatum 'Eintrittsdatum', z.DIENSTZEIT, m.id_instanzen 'Instanzen', m.instanznummer 'Instanznummer' "
                    + " FROM FDISK.dbo.stmkmitglieder m LEFT OUTER JOIN FDISK.dbo.FDISK_MAPPING_DZ_ZEIT z ON(m.id_personen = z.id_personen)"
                    + " INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(m.instanznummer = f.instanznummer)"
                    + " WHERE (m.abgemeldet = 0) AND (NOT (LEFT(m.instanznummer, 2) = 'GA')) AND (NOT (LEFT(m.instanzname, 7) = 'FW GAST'))"
                    + " AND f.abschnitt_instanznummer = " + intAbschnittnr
                    + " AND m.datum_abgemeldet IS NULL "
                    + " ORDER BY m.zuname, m.vorname";
        } else {
            sqlString = "SELECT m.id_personen 'PersID', m.standesbuchnummer 'STB', m.dienstgrad 'DGR', m.titel 'Titel', m.vorname 'Vorname', m.zuname 'Zuname', m.geburtsdatum 'Geburtsdatum',  m.datum_abgemeldet 'Datum_abgemeldet', m.eintrittsdatum 'Eintrittsdatum', z.DIENSTZEIT, m.id_instanzen 'Instanzen', m.instanznummer 'Instanznummer' "
                    + " FROM FDISK.dbo.stmkmitglieder m LEFT OUTER JOIN FDISK.dbo.FDISK_MAPPING_DZ_ZEIT z ON(m.id_personen = z.id_personen)"
                    + " WHERE (m.abgemeldet = 0) AND (NOT (LEFT(m.instanznummer, 2) = 'GA')) AND (NOT (LEFT(m.instanzname, 7) = 'FW GAST'))"
                    + " AND m.instanznummer = '" + strFubwehr + "'"
                    + " AND m.datum_abgemeldet IS NULL "
                    + " ORDER BY m.zuname, m.vorname";
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
        double doDienstzeit;
        int intInstanznummer;
        int intCurrentYear = Calendar.getInstance().get(Calendar.YEAR);
        int intYearDifference = intJahr - intCurrentYear;
        String strFub;

        while (rs.next()) {
            intPersID = rs.getInt("PersID");
            strSTB = rs.getString("STB");
            strDGR = rs.getString("DGR");
            strTitel = rs.getString("Titel");
            strVorname = rs.getString("Vorname");
            strZuname = rs.getString("Zuname");
            intInstanznummer = rs.getInt("Instanzen");
            strFub = rs.getString("Instanznummer");

            dateGeburtsdatum = rs.getDate("Geburtsdatum");

            if (rs.getDate("Eintrittsdatum") != null) {
                dateEintrittsdatum = new Date(rs.getDate("Eintrittsdatum").getTime());
            } else {
                dateEintrittsdatum = null;
            }

            doDienstzeit = rs.getDouble("DIENSTZEIT");
            doDienstzeit += intYearDifference;

            MitgliedsDienstzeit mitgliedsDienst = new MitgliedsDienstzeit(intPersID, strSTB, strDGR, strTitel, strVorname, strZuname, true, dateGeburtsdatum, doDienstzeit, intInstanznummer, dateEintrittsdatum, strFub);
            liMitgliedsDienstzeiten.add(mitgliedsDienst);

        }

        connPool.releaseConnection(conn);
        return liMitgliedsDienstzeiten;
    }

    /**
     * Gibt eine Liste mit allen Adressen aller Mitarbeiter als LinkedList
     * zurück.
     *
     * Gibt eine LinkedList mit allen Mitgliedern inkl. der Adresse des
     * Mitglieds (je nach Berechtigung eines Users) zurück
     *
     * @param intBereichnr
     * @param intAbschnittnr
     * @param strFubwehr
     * @return LinkedList
     * @throws java.lang.Exception
     * @see Mitglied
     * @see MitgliedsAdresse
     * @see LinkedList
     */
    public LinkedList<MitgliedsAdresse> getAdressliste(int intBereichnr, int intAbschnittnr, String strFubwehr) throws Exception {
        LinkedList<MitgliedsAdresse> liMitgliedsAdressen = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat;

        if (intBereichnr == -2) {
            prepStat = conn.prepareStatement(EnAdressliste.getAdresslisteAlle.toString());
        } else if (intAbschnittnr == -2) {
            prepStat = conn.prepareStatement(EnAdressliste.getAdresslisteBereich.toString());
            prepStat.setInt(1, intBereichnr);
        } else if (strFubwehr.equals("-2")) {
            prepStat = conn.prepareStatement(EnAdressliste.getAdresslisteAbschnitt.toString());
            prepStat.setInt(1, intAbschnittnr);
        } else {
            prepStat = conn.prepareStatement(EnAdressliste.getAdresslisteFubwehr.toString());
            prepStat.setString(1, strFubwehr);
        }
        ResultSet rs = prepStat.executeQuery();

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
        String strFub;

        while (rs.next()) {
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
            strFub = rs.getString("Instanznummer");

            MitgliedsAdresse mitgliedsAdresse = new MitgliedsAdresse(intPersID, strSTB, strDGR, strTitel, strVorname, strZuname, true, intId_Adressen, strStrasse, strNummer, strStiege, intPLZ, strOrt, strFub, true);
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
    public LinkedList<Kurstaetigkeit> getKursstatistiktaetigkeit(int intBereichnr, int intAbschnittnr, String strFubwehr, String strVon, String strBis) throws Exception {
        LinkedList<Kurstaetigkeit> liKursetaetigkeiten = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "SELECT COUNT(m.id_mitgliedschaften) 'Teilnahmen'"
                + " ,SUM(f.km) 'Km'"
                + " ,t.id_berichte 'BerichtId'"
                + " ,t.instanznummer 'Instanznr'"
                + " ,t.instanzname 'Instanzname'"
                + " ,t.taetigkeitsart 'Taetigkeitsart'"
                + " ,t.taetigkeitsunterart 'Taetigkeitsunterart'"
                + " ,t.nummer 'Nr'"
                + " ,t.beginn 'Beginn'"
                + " ,t.ende 'Ende'"
                + " FROM FDISK.dbo.stmktaetigkeitsberichte t"
                + " INNER JOIN FDISK.dbo.stmktaetigkeitsberichtemitglieder m ON(t.id_berichte = m.id_berichte)"
                + " INNER JOIN FDISK.dbo.stmktaetigkeitsberichtefahrzeuge f ON (t.id_berichte = f.id_berichte)"
                + " INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich fw ON(t.instanznummer = fw.instanznummer)"
                + " WHERE t.taetigkeitsart = 'Kursbesuch an der FWZS' ";

        if (intBereichnr == -2) {
        } else if (intAbschnittnr == -2) {
            sqlString += " AND fw.Bereich_Nr = " + intBereichnr;
        } else if (strFubwehr.equals("-2")) {
            sqlString += " AND fw.abschnitt_instanznummer = " + intAbschnittnr;
        } else {
            sqlString += " AND t.instanznummer = '" + strFubwehr + "'";
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

        while (rs.next()) {

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

    /**
     * Liefert alle Kurse die in einem bestimmten Zeitraum sstattgefunden haben
     * als LinkedList zurück
     *
     * @param intBereichnr
     * @param intAbschnittnr
     * @param strFubwehr
     * @param strVon
     * @param strBis
     * @return
     * @throws Exception
     */
    public LinkedList<Kurs> getKursstatistikkurse(int intBereichnr, int intAbschnittnr, String strFubwehr, String strVon, String strBis) throws Exception {
        LinkedList<Kurs> liKurse = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "SELECT k.id_kursarten KursartId "
                + ",k.lehrgangsnummer Lehrgangsnr "
                + ",k.kursbezeichnung Kursbez "
                + ",k.kurskurzbezeichnung Kurskurzbez "
                + ",k.id_instanzen_veranstalter Veran "
                + ",k.id_instanzen_durchfuehrend Durchf "
                + ",k.kursstatus 'Status' "
                + ",COUNT(km.id_kurse) 'Anzahl_Teilnehmer' "
                + "FROM FDISK.dbo.stmkkurse k "
                + "INNER JOIN FDISK.dbo.stmkkursemitglieder km ON(k.id_kurse = km.id_kurse) "
                + "INNER JOIN FDISK.dbo.stmkmitglieder m ON(km.id_mitgliedschaften = m.id_mitgliedschaften) "
                + "INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich fw ON(m.instanznummer = fw.instanznummer) ";

        if (intBereichnr == -2) {
            sqlString += getSqlDateString(strVon, strBis, 4, true);
        } else {
            if (intAbschnittnr == -2) {
                sqlString += " WHERE fw.Bereich_Nr = " + intBereichnr;
            } else if (strFubwehr.equals("-2")) {
                sqlString += " WHERE fw.abschnitt_instanznummer = " + intAbschnittnr;
            } else {
                sqlString += " WHERE m.instanznummer = '" + 47030 + "'";
            }
            sqlString += getSqlDateString(strVon, strBis, 4, false);
        }

        sqlString += " GROUP BY "
                + "k.id_kursarten "
                + ",k.lehrgangsnummer "
                + ",k.kursbezeichnung "
                + ",k.kurskurzbezeichnung "
                + ",k.id_instanzen_veranstalter "
                + ",k.id_instanzen_durchfuehrend "
                + ",k.kursstatus "
                + "order by k.kursbezeichnung ";

        ResultSet rs = stat.executeQuery(sqlString);

        int intKursartId;
        int intLehrgangsnr;
        String strKursbez;
        String strKurskurzbez;
        int intIdVer;
        int intIdDurchf;
        String strStatus;
        int intAnzahlTeilnehmer;

        while (rs.next()) {
            intKursartId = rs.getInt("KursartId");
            intLehrgangsnr = rs.getInt("Lehrgangsnr");
            strKursbez = rs.getString("Kursbez");
            strKurskurzbez = rs.getString("Kurskurzbez");
            intIdVer = rs.getInt("Veran");
            intIdDurchf = rs.getInt("Durchf");
            strStatus = rs.getString("Status");
            intAnzahlTeilnehmer = rs.getInt("Anzahl_Teilnehmer");

            Kurs kurs = new Kurs(intKursartId, intLehrgangsnr, strKursbez, strKurskurzbez, intIdVer, intIdDurchf, strStatus, intAnzahlTeilnehmer);
            liKurse.add(kurs);
        }

        connPool.releaseConnection(conn);
        return liKurse;
    }

    /**
     *
     * @param strVon
     * @param strBis
     * @param intBereichnr
     * @param intAbschnittnr
     * @param strFubwehr
     * @param intIDPerson
     * @return
     * @throws Exception
     */
    public LinkedList<MitgliedsStunden> getStundenauswertungProMitgliedProInstanz(String strVon, String strBis, int intBereichnr, int intAbschnittnr, String strFubwehr, int intIDPerson) throws Exception {
        LinkedList<MitgliedsStunden> liStunden = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        int intPersID;
        String strSTB;
        String strDGR;
        String strTitel;
        String strVorname;
        String strZuname;
        int intMinuten;
        String strInstanznummer;
        int intBerichtId;
        String strInstanzname;
        int intAnzahl;

        String sqlString = "SELECT 1 'bereicht_id', PersID, vorname Vorname, zuname Zuname, instanznummer Instanznummer, STB, DGR, titel Titel ,SUM(DATEDIFF(mi,einsatzzeit_von,einsatzzeit_bis)) Minuten, instanzname Instanzname, COUNT(*) Anzahl "
                + "FROM ( "
                + "SELECT DISTINCT 1 'bereicht_id', t.id_personen PersID, t.vorname Vorname, t.zuname Zuname, t.instanznummer Instanznummer, m.standesbuchnummer STB, m.dienstgrad DGR, m.titel Titel, t.id_berichte, tb.instanzname, einsatzzeit_von, einsatzzeit_bis "
                + "FROM FDISK.dbo.stmktaetigkeitsberichtemitglieder t "
                + "INNER JOIN FDISK.dbo.stmkmitglieder m ON(t.id_mitgliedschaften = m.id_mitgliedschaften) "
                + "INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich fw ON(fw.instanznummer = t.instanznummer) "
                + "INNER JOIN FDISK.dbo.stmktaetigkeitsberichte tb ON(t.id_berichte = tb.id_berichte) ";
        if (intIDPerson == -2) {
            if (intBereichnr == -2) {
                sqlString += getSqlDateString(strVon, strBis, 2, true);
            } else {
                if (intAbschnittnr == -2) {
                    sqlString += " WHERE fw.Bereich_Nr = " + intBereichnr;
                } else if (strFubwehr.equals("-2")) {
                    sqlString += " WHERE fw.abschnitt_instanznummer = " + intAbschnittnr;
                } else {
                    sqlString += " WHERE t.instanznummer = '" + strFubwehr + "'";
                }
                sqlString += getSqlDateString(strVon, strBis, 2, false);
            }
        } else {
            sqlString += " WHERE t.id_personen = " + intIDPerson;
            sqlString += getSqlDateString(strVon, strBis, 2, false);
        }

        sqlString += " ) a GROUP BY PersID, instanznummer, vorname, zuname, stb, dgr, titel, instanzname "
                + "UNION "
                + "SELECT 2 'bereicht_id', PersID, vorname Vorname, zuname Zuname, instanznummer Instanznummer, STB, DGR, titel Titel ,SUM(DATEDIFF(mi,einsatzzeit_von,einsatzzeit_bis)) Minuten, name Instanzname, COUNT(*) Anzahl "
                + "FROM ( "
                + "SELECT DISTINCT 2 'bereicht_id', u.id_personen PersID, u.vorname Vorname, u.zuname Zuname, u.instanznummer Instanznummer, m.standesbuchnummer STB, m.dienstgrad DGR, m.titel Titel, u.id_berichte, ub.name, einsatzzeit_von, einsatzzeit_bis "
                + "FROM FDISK.dbo.stmkuebungsberichtemitglieder u "
                + "INNER JOIN FDISK.dbo.stmkmitglieder m ON(u.id_mitgliedschaften = m.id_mitgliedschaften) "
                + "INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich fw ON(fw.instanznummer = u.instanznummer) "
                + "INNER JOIN FDISK.dbo.stmkuebungsberichte ub ON(u.id_berichte = ub.id_berichte) ";

        if (intIDPerson == -2) {
            if (intBereichnr == -2) {
                sqlString += getSqlDateString(strVon, strBis, 3, true);
            } else {
                if (intAbschnittnr == -2) {
                    sqlString += " WHERE fw.Bereich_Nr = " + intBereichnr;
                } else if (strFubwehr.equals("-2")) {
                    sqlString += " WHERE fw.abschnitt_instanznummer = " + intAbschnittnr;
                } else {
                    sqlString += " WHERE u.instanznummer = '" + strFubwehr + "'";
                }
                sqlString += getSqlDateString(strVon, strBis, 3, false);
            }
        } else {
            sqlString += " WHERE u.id_personen = " + intIDPerson;
            sqlString += getSqlDateString(strVon, strBis, 3, false);
        }

        sqlString += " ) a GROUP BY PersID, instanznummer, vorname, zuname, stb, dgr, titel, name "
                + "UNION "
                + "SELECT 3 'bereicht_id', PersID, vorname Vorname, zuname Zuname, instanznummer Instanznummer, STB, DGR, titel Titel ,SUM(DATEDIFF(mi,einsatzzeit_von,einsatzzeit_bis)) Minuten, name Instanzname, COUNT(*) Anzahl "
                + "FROM ( "
                + "SELECT DISTINCT 3 'bereicht_id', e.id_personen PersID, e.vorname Vorname, e.zuname Zuname, e.instanznummer Instanznummer, m.standesbuchnummer STB, m.dienstgrad DGR, m.titel Titel, e.id_berichte, eb.name, einsatzzeit_von, einsatzzeit_bis "
                + "FROM FDISK.dbo.stmkeinsatzberichtemitglieder e "
                + "INNER JOIN FDISK.dbo.stmkmitglieder m ON(e.id_mitgliedschaften = m.id_mitgliedschaften) "
                + "INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich fw ON(fw.instanznummer = e.instanznummer) "
                + "INNER JOIN FDISK.dbo.stmkeinsatzberichte eb ON(e.id_berichte = eb.id_berichte) ";

        if (intIDPerson == -2) {
            if (intBereichnr == -2) {
                sqlString += getSqlDateString(strVon, strBis, 1, true);
            } else {
                if (intAbschnittnr == -2) {
                    sqlString += " WHERE fw.Bereich_Nr = " + intBereichnr;
                } else if (strFubwehr.equals("-2")) {
                    sqlString += " WHERE fw.abschnitt_instanznummer = " + intAbschnittnr;
                } else {
                    sqlString += " WHERE e.instanznummer = '" + strFubwehr + "'";
                }
                sqlString += getSqlDateString(strVon, strBis, 1, false);
            }
        } else {
            sqlString += " WHERE e.id_personen = " + intIDPerson;
            sqlString += getSqlDateString(strVon, strBis, 1, false);
        }

        sqlString += " ) a "
                + "GROUP BY PersID, instanznummer, vorname, zuname, stb, dgr, titel, name "
                + "ORDER BY 'PersID'";

        System.out.println("Gesamt SQL String: " + sqlString);

        ResultSet rs = stat.executeQuery(sqlString);

        boolean exists = false;

        while (rs.next()) {

            intPersID = rs.getInt("PersID");
            strSTB = rs.getString("STB");
            strDGR = rs.getString("DGR");
            strTitel = rs.getString("Titel");
            strVorname = rs.getString("Vorname");
            strZuname = rs.getString("Zuname");
            intMinuten = rs.getInt("Minuten");
            strInstanznummer = rs.getString("Instanznummer");
            intBerichtId = rs.getInt("bereicht_id");
            strInstanzname = rs.getString("Instanzname");
            intAnzahl = rs.getInt("Anzahl");

            if (!liStunden.isEmpty()) {
                for (int i = 0; i < liStunden.size(); i++) {
                    if (liStunden.get(i).getIntId_Personen() == intPersID && liStunden.get(i).getStrInstanznummer().equals(strInstanznummer)) {
                        exists = true;
                        liStunden.get(i).setIntMinuten(liStunden.get(i).getIntMinuten() + intMinuten);
                        switch (intBerichtId) {
                            case 1:
                                liStunden.get(i).setIntMinutenTb(liStunden.get(i).getIntMinutenTb() + intMinuten);
                                liStunden.get(i).setIntAnzTb(liStunden.get(i).getIntAnzTb() + intAnzahl);
                                break;
                            case 2:
                                liStunden.get(i).setIntMinutenUb(liStunden.get(i).getIntMinutenUb() + intMinuten);
                                liStunden.get(i).setIntAnzUb(liStunden.get(i).getIntAnzUb() + intAnzahl);
                                break;
                            case 3:
                                liStunden.get(i).setIntMinutenEb(liStunden.get(i).getIntMinutenEb() + intMinuten);
                                liStunden.get(i).setIntAnzEb(liStunden.get(i).getIntAnzEb() + intAnzahl);
                                break;
                        }
                        break;
                    }
                }
                if (!exists) {
                    MitgliedsStunden mitgliedsStunden = null;
                    switch (intBerichtId) {
                        case 1:
                            mitgliedsStunden = new MitgliedsStunden(intMinuten, strInstanznummer, 0, 0, intAnzahl, 0, 0, intMinuten, intPersID, strSTB, strDGR, strTitel, strVorname, strZuname, strInstanzname, strInstanznummer);
                            break;
                        case 2:
                            mitgliedsStunden = new MitgliedsStunden(intMinuten, strInstanznummer, intAnzahl, 0, 0, intMinuten, 0, 0, intPersID, strSTB, strDGR, strTitel, strVorname, strZuname, strInstanzname, strInstanznummer);
                            break;
                        case 3:
                            mitgliedsStunden = new MitgliedsStunden(intMinuten, strInstanznummer, 0, intAnzahl, 0, 0, intMinuten, 0, intPersID, strSTB, strDGR, strTitel, strVorname, strZuname, strInstanzname, strInstanznummer);
                            break;
                    }
                    liStunden.add(mitgliedsStunden);
                } else {
                    exists = false;
                }
            } else {
                MitgliedsStunden mitgliedsStunden = null;
                switch (intBerichtId) {
                    case 1:
                        mitgliedsStunden = new MitgliedsStunden(intMinuten, strInstanznummer, 0, 0, intAnzahl, 0, 0, intMinuten, intPersID, strSTB, strDGR, strTitel, strVorname, strZuname, strInstanzname, strInstanznummer);
                        break;
                    case 2:
                        mitgliedsStunden = new MitgliedsStunden(intMinuten, strInstanznummer, intAnzahl, 0, 0, intMinuten, 0, 0, intPersID, strSTB, strDGR, strTitel, strVorname, strZuname, strInstanzname, strInstanznummer);
                        break;
                    case 3:
                        mitgliedsStunden = new MitgliedsStunden(intMinuten, strInstanznummer, 0, intAnzahl, 0, 0, intMinuten, 0, intPersID, strSTB, strDGR, strTitel, strVorname, strZuname, strInstanzname, strInstanznummer);
                        break;
                }
                liStunden.add(mitgliedsStunden);
            }
        }
        liStunden.sort(Comparator.comparing(MitgliedsStunden::getStrInstanzname));
        connPool.releaseConnection(conn);
        return liStunden;
    }

    public LinkedHashMap<Integer, String> getMitgliederFuerStundenauswertung(int intBereichnr, int intAbschnittnr, String strFubwehr) throws Exception {
        LinkedHashMap<Integer, String> hmMitgliedsMap = new LinkedHashMap<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();
        String sqlString;

        sqlString = "SELECT DISTINCT id_personen 'PersID', dienstgrad 'DGR', titel 'Titel', vorname 'Vorname', zuname 'Zuname', geburtsdatum 'GebDat', abgemeldet"
                + " FROM FDISK.dbo.stmkmitglieder m INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich fw ON(m.instanznummer = fw.instanznummer)"
                + " WHERE m.abgemeldet = 0";

        if (intBereichnr == -2) {
        } else if (intAbschnittnr == -2) {
            sqlString += " AND fw.Bereich_Nr = " + intBereichnr;
        } else if (strFubwehr.equals("-2")) {
            sqlString += " AND fw.abschnitt_instanznummer = " + intAbschnittnr;
        } else {
            sqlString += " AND m.instanznummer = '" + strFubwehr + "'";
        }

        sqlString += " ORDER BY zuname";

        ResultSet rs = stat.executeQuery(sqlString);

        int intPersID;
        String strDGR;
        String strVorname;
        String strZuname;
        Date gebDat;
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        while (rs.next()) {
            intPersID = rs.getInt("PersID");
            strDGR = rs.getString("DGR");
            strVorname = bl.formatiereAusgabe(rs.getString("Vorname"));
            strZuname = bl.formatiereAusgabe(rs.getString("Zuname"));
            gebDat = new Date(rs.getDate("GebDat").getTime());

            hmMitgliedsMap.put(intPersID, strDGR + " " + strVorname + " " + strZuname + " (" + sdf.format(gebDat) + ")");
        }

        connPool.releaseConnection(conn);

        return hmMitgliedsMap;
    }

    /**
     * Setzt einen HTML String mit den Basis Informationen für ein ausgewähltes
     * Fahrzeug zusammen und gibt diesen String zurück
     *
     * @param liFahrzeuge
     * @return
     */
    public String getDetailsFuerFahrtenbuchFahrzeug(LinkedList<Fahrzeug> liFahrzeuge) {
        if (liFahrzeuge == null || liFahrzeuge.size() == 0) {
            return "";
        }
        double doKmGesamt = 0;
        for (Fahrzeug fahrzeug : liFahrzeuge) {
            doKmGesamt += fahrzeug.getDoubleKm();
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
                + "<th>Kennzeichen</th>"
                + "<th>KM Gesamt</th>"
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
                + "<td>" + f.getStrKennzeichen() + "</td>"
                + "<td>" + doKmGesamt + "</td>"
                + "</tr>"
                + "</tbody>"
                + "</table>"
                + "</fieldset>";

        return htmlString;
    }

    /**
     * Gibt alle relevanten Daten (Fahrzeugtyp, Kennzeichen, Baujahr etc...)
     * inkl. aller Fahrten in einem bestimmten Zeitraum von einem ausgewählten
     * Fahrzeug als LinkedList zurück.
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
    public LinkedList<Fahrzeug> getFahrtenbuch(int intBereichnr, int intAbschnittnr, String strFubwehr, String strVon, String strBis, String strEingabeKennzeichen) throws Exception {
        LinkedList<Fahrzeug> liFahrzeuge = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        if (strEingabeKennzeichen != null && !strEingabeKennzeichen.isEmpty() && !strEingabeKennzeichen.equals(" ")) {
            strEingabeKennzeichen = strEingabeKennzeichen.replace("/", "").replace(".", " ").replace(" ", "").replace("+", "").replace("-", "");
        }
        String sqlString = "SELECT "
                + "f.kennzeichen 'Kennzeichen' "
                + ",f.id_fahrzeuge 'Id_Fahrzeuge' "
                + ",f.fahrzeugtyp 'Fahrzeugtyp' "
                + ",f.taktischebezeichnung 'Taktische Bezeichnung' "
                + ",f.bezeichnung 'Bezeichnung' "
                + ",f.status 'Status' "
                + ",f.baujahr 'Baujahr' "
                + ",f.fahrzeugmarke 'Fahrzeugmarke' "
                + ",f.aufbaufirma 'Aufbaufirma'"
                + ",f.instanznummer 'Instanzummer' "
                + ",f.fahrzeugart 'Fahrzeugart' "
                + ",f.leistung 'Leistung' "
                + ",f.eigengewicht 'Eigengewicht' "
                + ",f.gesamtgewicht 'Gesamtgewicht' "
                + ",f.treibstoff 'Treibstoff' "
                + ",uf.km 'Km' "
                + ",u.uebungsart 'Art' "
                + ",u.beginn 'Beginn' "
                + ",u.ende 'Ende' "
                + " FROM"
                + " FDISK.dbo.stmkfahrzeuge f INNER JOIN"
                + " FDISK.dbo.stmkuebungsberichtefahrzeuge uf ON(f.id_fahrzeuge = uf.id_fahrzeuge)"
                + " INNER JOIN FDISK.dbo.stmkuebungsberichte u"
                + " ON(uf.id_berichte = u.id_berichte)"
                + " INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich fw ON(fw.instanznummer = f.instanznummer)"
                + " WHERE f.status = 'aktiv' ";

        sqlString += getSqlDateString(strVon, strBis, 3, false);
        if (strEingabeKennzeichen != null && !strEingabeKennzeichen.isEmpty() && !strEingabeKennzeichen.equals(" ")) {
            sqlString += " AND UPPER(replace(replace(replace(replace(replace(f.kennzeichen,'+',''),'/',''),'.',''),' ',''),'-','')) = '" + strEingabeKennzeichen.toUpperCase() + "'";
        }
        if (intBereichnr == -2) {
        } else if (intAbschnittnr == -2) {
            sqlString += " AND fw.Bereich_Nr = " + intBereichnr;
        } else if (strFubwehr.equals("-2")) {
            sqlString += " AND fw.abschnitt_instanznummer = " + intAbschnittnr;
        } else {
            sqlString += " AND f.instanznummer = '" + strFubwehr + "'";
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

        if (strEingabeKennzeichen != null && !strEingabeKennzeichen.isEmpty() && !strEingabeKennzeichen.equals(" ")) {
            sqlString += " AND UPPER(replace(replace(replace(replace(replace(f.kennzeichen,'+',''),'/',''),'.',''),' ',''),'-','')) = '" + strEingabeKennzeichen.toUpperCase() + "'";
        }

        if (intBereichnr == -2) {

        } else if (intAbschnittnr == -2) {
            sqlString += " AND fw.Bereich_Nr = " + intBereichnr;
        } else if (strFubwehr.equals("-2")) {
            sqlString += " AND fw.abschnitt_instanznummer = " + intAbschnittnr;
        } else {
            sqlString += " AND f.instanznummer = '" + strFubwehr + "'";
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
        if (strEingabeKennzeichen != null && !strEingabeKennzeichen.isEmpty() && !strEingabeKennzeichen.equals(" ")) {
            sqlString += " AND UPPER(replace(replace(replace(replace(replace(f.kennzeichen,'+',''),'/',''),'.',''),' ',''),'-','')) = '" + strEingabeKennzeichen.toUpperCase() + "'";
        }

        if (intBereichnr == -2) {

        } else if (intAbschnittnr == -2) {
            sqlString += " AND fw.Bereich_Nr = " + intBereichnr;
        } else if (strFubwehr.equals("-2")) {
            sqlString += " AND fw.abschnitt_instanznummer = " + intAbschnittnr;
        } else {
            sqlString += " AND f.instanznummer = '" + strFubwehr + "'";
        }

        sqlString += " ORDER BY 18";

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

        while (rs.next()) {
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

            if (strAufbaufirma == null) {
                strAufbaufirma = "-";
            }

            if (strFahrzeugmarke == null) {
                strFahrzeugmarke = "-";
            }

            if (strTreibstoff == null) {
                strTreibstoff = "-";
            }

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
     * Gibt alle Kennzeichen für die ausgewählte Instanz als LinkedList zurück
     *
     * @param intBereichnr
     * @param intAbschnittnr
     * @param strFubwehr
     * @return
     * @throws Exception
     */
    public LinkedList<String> getFahrtenbuchKennzeichen(int intBereichnr, int intAbschnittnr, String strFubwehr) throws Exception {
        LinkedList<String> liKennzeichen = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = " SELECT DISTINCT kennzeichen 'Kennzeichen'"
                + " FROM FDISK.dbo.stmkfahrzeuge sf INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich fw ON(sf.instanznummer = fw.instanznummer)";

        if (intBereichnr == -2) {
        } else if (intAbschnittnr == -2) {
            sqlString += " WHERE fw.Bereich_Nr = " + intBereichnr;
        } else if (strFubwehr.equals("-2")) {
            sqlString += " WHERE fw.abschnitt_instanznummer = " + intAbschnittnr;
        } else {
            sqlString += " WHERE sf.instanznummer = '" + strFubwehr + "'";
        }

        ResultSet rs = stat.executeQuery(sqlString);

        String strKennzeichen;

        while (rs.next()) {
            strKennzeichen = rs.getString("Kennzeichen");

            if (strKennzeichen != null && !strKennzeichen.trim().isEmpty() && !strKennzeichen.equals(" ") && !strKennzeichen.equals("")) {
                strKennzeichen = strKennzeichen.replace("/", "").replace(".", "").replace(" ", "").replace("+", "").replace("-", "");

                if (!strKennzeichen.trim().isEmpty() && !strKennzeichen.equals(" ") && !strKennzeichen.equals("")) {
                    liKennzeichen.add(strKennzeichen);
                }

            }
        }
        connPool.releaseConnection(conn);
        return liKennzeichen;
    }

    /**
     * Gibt eine Liste der Erreichbarkeiten von jedem Mitarbeiter als LinkedList
     * zurück.
     *
     * @param intBereichnr
     * @param intAbschnittnr
     * @param strFubwehr
     * @return LinkedList
     * @throws java.lang.Exception
     * @see Mitglied
     * @see MitgliedsErreichbarkeit
     * @see LinkedList
     */
    public LinkedList<MitgliedsErreichbarkeit> getErreichbarkeitsliste(int intBereichnr, int intAbschnittnr, String strFubwehr) throws Exception {
        LinkedList<MitgliedsErreichbarkeit> liMitgliedsErreichbarkeiten = new LinkedList<>();

        Connection conn = connPool.getConnection();
        PreparedStatement prepStat;

        if (intBereichnr == -2) {
            prepStat = conn.prepareStatement(EnErreichbarkeitsliste.getErreichbarkeitslisteAlle.toString());
        } else if (intAbschnittnr == -2) {
            prepStat = conn.prepareStatement(EnErreichbarkeitsliste.getErreichbarkeitslisteBereich.toString());
            prepStat.setInt(1, intBereichnr);
        } else if (strFubwehr.equals("-2")) {
            prepStat = conn.prepareStatement(EnErreichbarkeitsliste.getErreichbarkeitslisteAbschnitt.toString());
            prepStat.setInt(1, intAbschnittnr);
        } else {
            prepStat = conn.prepareStatement(EnErreichbarkeitsliste.getErreichbarkeitslisteFubwehr.toString());
            prepStat.setString(1, strFubwehr);
        }

        ResultSet rs = prepStat.executeQuery();

        String strErreichbarkeitsart;
        String strCode;

        String strSTB;
        String strDGR;
        String strTitel;
        String strVorname;
        String strZuname;
        int intPersID = 0;
        int intLetztePersID = 0;
        String strFubNr;

        LinkedList<Erreichbarkeit> liErreichbarkeiten = new LinkedList<>();
        while (rs.next()) {

            intPersID = rs.getInt("PersID");
            strErreichbarkeitsart = rs.getString("Erreichbarkeitsart");

            strCode = rs.getString("Code");

            strSTB = rs.getString("STB");
            strDGR = rs.getString("DGR");
            strTitel = rs.getString("Titel");
            strVorname = rs.getString("Vorname");
            strZuname = rs.getString("Zuname");
            strFubNr = rs.getString("Fubwehr");

            if (intPersID == intLetztePersID) {
                if (!liErreichbarkeiten.contains(new Erreichbarkeit(strErreichbarkeitsart, strCode, intPersID))) {
                    liErreichbarkeiten.add(new Erreichbarkeit(strErreichbarkeitsart, strCode, intPersID));
                }
            } else {
                if (liMitgliedsErreichbarkeiten.size() > 0) {
                    liMitgliedsErreichbarkeiten.getLast().setLiErreichbarkeiten(liErreichbarkeiten);
                    liErreichbarkeiten = new LinkedList<>();
                }
                intLetztePersID = intPersID;
                liMitgliedsErreichbarkeiten.add(new MitgliedsErreichbarkeit(false, intPersID, strSTB, strDGR, strTitel, strVorname, strZuname, strFubNr));
                liErreichbarkeiten.add(new Erreichbarkeit(strErreichbarkeitsart, strCode, intPersID));
            }

        }

        connPool.releaseConnection(conn);
        return liMitgliedsErreichbarkeiten;
    }

    /**
     * Gibt alle für einen Leerbericht benötigten Mitglieder zurück
     *
     * @param intBereichnr
     * @param intAbschnittnr
     * @param strFubwehr
     * @return
     * @throws Exception
     */
    public LinkedList<LeerberichtMitglied> getLeerberichtMitglied(int intBereichnr, int intAbschnittnr, String strFubwehr, boolean boEinsatz) throws Exception {
        LinkedList<LeerberichtMitglied> liLeerberichtMitglieder = new LinkedList<>();

        Connection conn = connPool.getConnection();
        PreparedStatement prepStat;

        if (intBereichnr == -2) {
            if (boEinsatz) {
                prepStat = conn.prepareStatement(EnLeerberichtMitglied.getLeerberichtMitgliedAlle.toString() + " AND jugend = 0");
            } else {
                prepStat = conn.prepareStatement(EnLeerberichtMitglied.getLeerberichtMitgliedAlle.toString());
            }
        } else if (intAbschnittnr == -2) {
            if (boEinsatz) {
                prepStat = conn.prepareStatement(EnLeerberichtMitglied.getLeerberichtMitgliedBereich.toString() + " AND jugend = 0");
            } else {
                prepStat = conn.prepareStatement(EnLeerberichtMitglied.getLeerberichtMitgliedBereich.toString());
            }
            prepStat.setInt(1, intBereichnr);
        } else if (strFubwehr.equals("-2")) {
            if (boEinsatz) {
                prepStat = conn.prepareStatement(EnLeerberichtMitglied.getLeerberichtMitgliedAbschnitt.toString() + " AND jugend = 0");
            } else {
                prepStat = conn.prepareStatement(EnLeerberichtMitglied.getLeerberichtMitgliedAbschnitt.toString());
            }
            prepStat.setInt(1, intAbschnittnr);
        } else {
            if (boEinsatz) {
                prepStat = conn.prepareStatement(EnLeerberichtMitglied.getLeerberichtMitgliedFubwehr.toString() + " AND jugend = 0 ORDER BY zuname");
            } else {
                prepStat = conn.prepareStatement(EnLeerberichtMitglied.getLeerberichtMitgliedFubwehr.toString() + " ORDER BY zuname");
            }
            prepStat.setString(1, strFubwehr);
        }

        ResultSet rs = prepStat.executeQuery();

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

            LeerberichtMitglied mitglied = new LeerberichtMitglied(intPersID, strSTB, strDGR, strTitel, strVorname, strZuname);
            liLeerberichtMitglieder.add(mitglied);
        }

        connPool.releaseConnection(conn);
        return liLeerberichtMitglieder;
    }

    public Date getEinsatzberichtEinsatzzeit() throws SQLException, Exception {
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "SELECT convert(char, einsatzzeit_bis - einsatzzeit_von, 114) 'Einsatzzeit'"
                + " FROM FDISK.dbo.stmkeinsatzberichtemitglieder WHERE id_mitgliedschaften = 219782"; //WHERE id_mitgliedschaften = "+mitglied.getIntIdMitgliedschaften();

        ResultSet rs = stat.executeQuery(sqlString);

        Date einsatzzeit = null;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");

        while (rs.next()) {
            einsatzzeit = sdf.parse(rs.getString("Einsatzzeit"));
        }

        connPool.releaseConnection(conn);
        return einsatzzeit;
    }

    /**
     * Liefert alle Fahrzeuge für einen Leerbericht zurück
     *
     * @param intBereichnr
     * @param intAbschnittnr
     * @param strFubwehr
     * @return
     * @throws Exception
     */
    public LinkedList<LeerberichtFahrzeug> getLeerberichtFahrzeug(int intBereichnr, int intAbschnittnr, String strFubwehr) throws Exception {
        LinkedList<LeerberichtFahrzeug> liFahrzeuge = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat;

        if (intBereichnr == -2) {
            prepStat = conn.prepareStatement(EnLeerberichtFahrzeug.getLeerberichtFahrzeugAlle.toString());
        } else if (intAbschnittnr == -2) {
            prepStat = conn.prepareStatement(EnLeerberichtFahrzeug.getLeerberichtFahrzeugBereich.toString());
            prepStat.setInt(1, intBereichnr);
        } else if (strFubwehr.equals("-2")) {
            prepStat = conn.prepareStatement(EnLeerberichtFahrzeug.getLeerberichtFahrzeugAbschnitt.toString());
            prepStat.setInt(1, intAbschnittnr);
        } else {
            prepStat = conn.prepareStatement(EnLeerberichtFahrzeug.getLeerberichtFahrzeugFubwehr.toString());
            prepStat.setString(1, strFubwehr);
        }

        ResultSet rs = prepStat.executeQuery();

        String strFahrzeugTyp;
        String strKennzeichen;
        int intBaujahr;
        String strAufbaufirma;
        String strTaktischeBezeichnung;
        int intId_fahrzeuge;
        String strBezeichnung;
        String strFahrzeugmarke;
        int intInstanznummer;

        while (rs.next()) {
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
     *
     * @param strVon
     * @param strBis
     * @param intBericht
     * @param boWhere
     * @return
     */
    public String getSqlDateString(String strVon, String strBis, int intBericht, boolean boWhere) {

        String dateString = "";

        if (strBis.isEmpty() && strBis.equals("") && strVon.isEmpty() && strVon.equals("")) {
            return "";
        }

        if (boWhere) {
            dateString += " WHERE";
        } else if (!boWhere) {
            dateString += " AND";
        }

        if (intBericht == 1) {
            if ((strVon.isEmpty() || strVon.equals("")) && (!strBis.isEmpty() || !strBis.equals(""))) {
                dateString += " uhrzeit_rueckkehr < (CAST('" + strBis + " 00:00.000' AS DATETIME)+1)";

            } else if (strBis.isEmpty() || strBis.equals("") && (!strVon.isEmpty() || !strVon.equals(""))) {
                dateString += " uhrzeit_alarmierung >= CAST('" + strVon + " 00:00.000' AS DATETIME)";

            } else if (!strBis.isEmpty() && !strBis.equals("") && !strVon.isEmpty() && !strVon.equals("")) {
                dateString += " uhrzeit_alarmierung >= CAST('" + strVon + " 00:00.000' AS DATETIME) AND uhrzeit_rueckkehr < (CAST('" + strBis + " 00:00.000' AS DATETIME)+1)";
            }
        } else if (intBericht == 2 || intBericht == 3) {
            if ((strVon.isEmpty() || strVon.equals("")) && (!strBis.isEmpty() || !strBis.equals(""))) {
                dateString += " (ende < (CAST('" + strBis + " 00:00.000' AS DATETIME)+1))";

            } else if (strBis.isEmpty() || strBis.equals("") && (!strVon.isEmpty() || !strVon.equals(""))) {
                dateString += " (beginn >= CAST('" + strVon + " 00:00.000' AS DATETIME))";
            } else if (!strBis.isEmpty() && !strBis.equals("") && !strVon.isEmpty() && !strVon.equals("")) {
                dateString += " (beginn >= CAST('" + strVon + " 00:00.000' AS DATETIME) AND ende < (CAST('" + strBis + " 00:00.000' AS DATETIME)+1))";
            }
        } else if (intBericht == 4) {
            if ((strVon.isEmpty() || strVon.equals("")) && (!strBis.isEmpty() || !strBis.equals(""))) {
                dateString += " (datum < (CAST('" + strBis + " 00:00.000' AS DATETIME)+1))";

            } else if (strBis.isEmpty() || strBis.equals("") && (!strVon.isEmpty() || !strVon.equals(""))) {
                dateString += " (datum >= CAST('" + strVon + " 00:00.000' AS DATETIME))";
            } else if (!strBis.isEmpty() && !strBis.equals("") && !strVon.isEmpty() && !strVon.equals("")) {
                dateString += " (datum >= CAST('" + strVon + " 00:00.000' AS DATETIME) AND datum < (CAST('" + strBis + " 00:00.000' AS DATETIME)+1))";
            }
        }
        return dateString;
    }

    /**
     * Gibt eine LinkedList mit allen Tätigkeitsberichten zurück
     *
     * @param strVon
     * @param strBis
     * @param intBereichnr
     * @param intAbschnittnr
     * @param strFubwehr
     * @return
     * @throws Exception
     */
    public LinkedList<Taetigkeitsbericht> getTaetigkeitsbericht(String strVon, String strBis, int intBereichnr, int intAbschnittnr, String strFubwehr) throws Exception {
        LinkedList<Taetigkeitsbericht> liTaetigkeitsbericht = new LinkedList<>();

        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();
        String sqlString;

        if (intBereichnr == -2) {
            sqlString = "SELECT DISTINCT id_berichte 'ID'"
                    + " ,instanznummer 'Instanznummer'"
                    + " ,instanzname 'Instanzname'"
                    + " ,taetigkeitsart 'Taetigkeitsart'"
                    + " ,taetigkeitsunterart 'Taetigkeitsunterart'"
                    + " ,nummer 'Nummer'"
                    + " ,beginn 'Beginn'"
                    + " ,ende 'Ende'"
                    + " ,strasse 'Strasse'"
                    + " ,nummeradr 'NummerAdr'"
                    + " ,stiege 'Stiege'"
                    + " ,plz 'PLZ'"
                    + " ,ort 'Ort'"
                    + " ,meldung 'Meldung'"
                    + " ,Fehlalarm 'Fehlalarm'"
                    + " FROM FDISK.dbo.stmktaetigkeitsberichte";
            sqlString += getSqlDateString(strVon, strBis, 2, true);
        } else {
            if (intAbschnittnr == -2) {
                sqlString = "SELECT DISTINCT id_berichte 'ID'"
                        + " ,tb.instanznummer 'Instanznummer'"
                        + " ,tb.instanzname 'Instanzname'"
                        + " ,taetigkeitsart 'Taetigkeitsart'"
                        + " ,taetigkeitsunterart 'Taetigkeitsunterart'"
                        + " ,nummer 'Nummer'"
                        + " ,beginn 'Beginn'"
                        + " ,ende 'Ende'"
                        + " ,strasse 'Strasse'"
                        + " ,nummeradr 'NummerAdr'"
                        + " ,stiege 'Stiege'"
                        + " ,plz 'PLZ'"
                        + " ,ort 'Ort'"
                        + " ,meldung 'Meldung'"
                        + " ,Fehlalarm 'Fehlalarm'"
                        + " FROM FDISK.dbo.stmktaetigkeitsberichte tb INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(tb.instanznummer = f.instanznummer)"
                        + " WHERE f.Bereich_Nr = " + intBereichnr;
            } else if (strFubwehr.equals("-2")) {
                sqlString = "SELECT DISTINCT id_berichte 'ID'"
                        + " ,tb.instanznummer 'Instanznummer'"
                        + " ,tb.instanzname 'Instanzname'"
                        + " ,taetigkeitsart 'Taetigkeitsart'"
                        + " ,taetigkeitsunterart 'Taetigkeitsunterart'"
                        + " ,nummer 'Nummer'"
                        + " ,beginn 'Beginn'"
                        + " ,ende 'Ende'"
                        + " ,strasse 'Strasse'"
                        + " ,nummeradr 'NummerAdr'"
                        + " ,stiege 'Stiege'"
                        + " ,plz 'PLZ'"
                        + " ,ort 'Ort'"
                        + " ,meldung 'Meldung'"
                        + " ,Fehlalarm 'Fehlalarm'"
                        + " FROM FDISK.dbo.stmktaetigkeitsberichte tb INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(tb.instanznummer = f.instanznummer)"
                        + " WHERE f.abschnitt_instanznummer = " + intAbschnittnr;
            } else {
                sqlString = "SELECT DISTINCT id_berichte 'ID'"
                        + " ,instanznummer 'Instanznummer'"
                        + " ,instanzname 'Instanzname'"
                        + " ,taetigkeitsart 'Taetigkeitsart'"
                        + " ,taetigkeitsunterart 'Taetigkeitsunterart'"
                        + " ,nummer 'Nummer'"
                        + " ,beginn 'Beginn'"
                        + " ,ende 'Ende'"
                        + " ,strasse 'Strasse'"
                        + " ,nummeradr 'NummerAdr'"
                        + " ,stiege 'Stiege'"
                        + " ,plz 'PLZ'"
                        + " ,ort 'Ort'"
                        + " ,meldung 'Meldung'"
                        + " ,Fehlalarm 'Fehlalarm'"
                        + " FROM FDISK.dbo.stmktaetigkeitsberichte"
                        + " WHERE instanznummer = '" + strFubwehr + "'";
            }
            sqlString += getSqlDateString(strVon, strBis, 2, false);
        }

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

        while (rs.next()) {
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

            Taetigkeitsbericht taetigkeitsbericht = new Taetigkeitsbericht(intIdBericht, intInstanznummer, strInstanzname, strTaetigkeitsart, strTaetigkeitsunterart, strNummer, dateBeginn, dateEnde, strStrasse, strNummerAdr, strStiege, strPlz, strOrt, strMeldung, strFehlalarm);
            liTaetigkeitsbericht.add(taetigkeitsbericht);
        }

        connPool.releaseConnection(conn);
        return liTaetigkeitsbericht;
    }

    /**
     * Gibt eine LinkedList mit allen Einsatzberichten zurück
     *
     * @param strVon
     * @param strBis
     * @param intBereichnr
     * @param intAbschnittnr
     * @param strFubwehr
     * @return
     * @throws Exception
     */
    public LinkedList<Einsatzbericht> getEinsatzbericht(String strVon, String strBis, int intBereichnr, int intAbschnittnr, String strFubwehr) throws Exception {
        LinkedList<Einsatzbericht> liEinsatzbericht = new LinkedList<>();

        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString;

        if (intBereichnr == -2) {
            sqlString = "SELECT DISTINCT id_berichte 'ID'"
                    + " ,instanznummer 'Instanznummer'"
                    + " ,name 'Name'"
                    + " ,einsatzart 'Art'"
                    + " ,nummer 'Nr'"
                    + " ,uhrzeit_alarmierung 'Uhrzeit_Alarmierung'"
                    + " ,uhrzeit_rueckkehr 'Uhrzeit_Rueckkehr'"
                    + " ,strasse 'Strasse'"
                    + " ,nummeradr 'Nummeradr'"
                    + " ,stiege 'Stiege'"
                    + " ,plz 'PLZ'"
                    + " ,ort 'Ort'"
                    + " ,standesbuchnummer 'SBN'"
                    + " ,vorname 'Vorname'"
                    + " ,zuname 'Zuname'"
                    + " ,meldung 'Meldung'"
                    + " ,Fehlalarm 'Fehlalarm'"
                    + " ,(SELECT Count(*)"
                    + "  FROM   fdisk.dbo.stmkeinsatzberichtemitglieder mitglied"
                    + "  WHERE  mitglied.id_berichte = eb.id_berichte) 'Anzahl'"
                    + " FROM FDISK.dbo.stmkeinsatzberichte";
            sqlString += getSqlDateString(strVon, strBis, 1, true);
        } else {
            if (intAbschnittnr == -2) {
                sqlString = "SELECT DISTINCT id_berichte 'ID'"
                        + " ,eb.instanznummer 'Instanznummer'"
                        + " ,name 'Name'"
                        + " ,einsatzart 'Art'"
                        + " ,nummer 'Nr'"
                        + " ,uhrzeit_alarmierung 'Uhrzeit_Alarmierung'"
                        + " ,uhrzeit_rueckkehr 'Uhrzeit_Rueckkehr'"
                        + " ,strasse 'Strasse'"
                        + " ,nummeradr 'Nummeradr'"
                        + " ,stiege 'Stiege'"
                        + " ,plz 'PLZ'"
                        + " ,ort 'Ort'"
                        + " ,standesbuchnummer 'SBN'"
                        + " ,vorname 'Vorname'"
                        + " ,zuname 'Zuname'"
                        + " ,meldung 'Meldung'"
                        + " ,Fehlalarm 'Fehlalarm'"
                        + " ,(SELECT Count(*)"
                        + "  FROM   fdisk.dbo.stmkeinsatzberichtemitglieder mitglied"
                        + "  WHERE  mitglied.id_berichte = eb.id_berichte) 'Anzahl'"
                        + " FROM FDISK.dbo.stmkeinsatzberichte eb INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(eb.instanznummer = f.instanznummer) "
                        + " WHERE f.Bereich_Nr = " + intBereichnr;
            } else if (strFubwehr.equals("-2")) {
                sqlString = "SELECT DISTINCT id_berichte 'ID'"
                        + " ,eb.instanznummer 'Instanznummer'"
                        + " ,name 'Name'"
                        + " ,einsatzart 'Art'"
                        + " ,nummer 'Nr'"
                        + " ,uhrzeit_alarmierung 'Uhrzeit_Alarmierung'"
                        + " ,uhrzeit_rueckkehr 'Uhrzeit_Rueckkehr'"
                        + " ,strasse 'Strasse'"
                        + " ,nummeradr 'Nummeradr'"
                        + " ,stiege 'Stiege'"
                        + " ,plz 'PLZ'"
                        + " ,ort 'Ort'"
                        + " ,standesbuchnummer 'SBN'"
                        + " ,vorname 'Vorname'"
                        + " ,zuname 'Zuname'"
                        + " ,meldung 'Meldung'"
                        + " ,Fehlalarm 'Fehlalarm'"
                        + " ,(SELECT Count(*)"
                        + "  FROM   fdisk.dbo.stmkeinsatzberichtemitglieder mitglied"
                        + "  WHERE  mitglied.id_berichte = eb.id_berichte) 'Anzahl'"
                        + " FROM FDISK.dbo.stmkeinsatzberichte eb INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(eb.instanznummer = f.instanznummer) "
                        + " WHERE f.abschnitt_instanznummer = " + intAbschnittnr;
            } else {
                sqlString = "SELECT DISTINCT id_berichte 'ID'"
                        + " ,instanznummer 'Instanznummer'"
                        + " ,name 'Name'"
                        + " ,einsatzart 'Art'"
                        + " ,nummer 'Nr'"
                        + " ,uhrzeit_alarmierung 'Uhrzeit_Alarmierung'"
                        + " ,uhrzeit_rueckkehr 'Uhrzeit_Rueckkehr'"
                        + " ,strasse 'Strasse'"
                        + " ,nummeradr 'Nummeradr'"
                        + " ,stiege 'Stiege'"
                        + " ,plz 'PLZ'"
                        + " ,ort 'Ort'"
                        + " ,standesbuchnummer 'SBN'"
                        + " ,vorname 'Vorname'"
                        + " ,zuname 'Zuname'"
                        + " ,meldung 'Meldung'"
                        + " ,Fehlalarm 'Fehlalarm'"
                        + " ,(SELECT Count(*)"
                        + "  FROM   fdisk.dbo.stmkeinsatzberichtemitglieder mitglied"
                        + "  WHERE  mitglied.id_berichte = eb.id_berichte) 'Anzahl'"
                        + " FROM FDISK.dbo.stmkeinsatzberichte"
                        + " WHERE instanznummer = '" + strFubwehr + "'";
            }
            sqlString += getSqlDateString(strVon, strBis, 1, false);
        }

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
        int intAnzahl;

        while (rs.next()) {
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
            intAnzahl = rs.getInt("Anzahl");

            Einsatzbericht einsatzbericht = new Einsatzbericht(intIdBericht,
                    intInstanznummer, strName, strEinsatzart, strNummer,
                    dateUhrzeit_Alarmierung, dateUhrzeit_Rueckkehr, strStrasse,
                    strNummerAdr, strStiege, strPlz, strOrt, intStandesbuchnummer,
                    strVorname, strZuname, strMeldung, strFehlalarm, intAnzahl);
            liEinsatzbericht.add(einsatzbericht);
        }

        connPool.releaseConnection(conn);
        return liEinsatzbericht;
    }

    /**
     * Gibt eine LinkedList mit allen Übungsberichten zurück
     *
     * @param strVon
     * @param strBis
     * @param intBereichnr
     * @param intAbschnittnr
     * @param strFubwehr
     * @return
     * @throws Exception
     */
    public LinkedList<Uebungsbericht> getUebungsbericht(String strVon, String strBis, int intBereichnr, int intAbschnittnr, String strFubwehr) throws Exception {
        LinkedList<Uebungsbericht> liUebungsbericht = new LinkedList<>();

        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "";
        if (intBereichnr == -2) {
            sqlString = "SELECT DISTINCT id_berichte 'ID'"
                    + " ,instanznummer 'Instanznummer'"
                    + " ,name 'Instanzname'"
                    + " ,uebungsart 'Uebungsart'"
                    + " ,uebungsunterart 'Uebungsunterart'"
                    + " ,nummer 'Nummer'"
                    + " ,beginn 'Beginn'"
                    + " ,ende 'Ende'"
                    + " ,strasse 'Strasse'"
                    + " ,nummeradr 'NummerAdr'"
                    + " ,stiege 'Stiege'"
                    + " ,plz 'PLZ'"
                    + " ,ort 'Ort'"
                    + " ,meldung 'Meldung'"
                    + " ,Fehlalarm 'Fehlalarm'"
                    + " FROM FDISK.dbo.stmkuebungsberichte ";
            sqlString += getSqlDateString(strVon, strBis, 3, true);
        } else {
            if (intAbschnittnr == -2) {
                sqlString = "SELECT DISTINCT id_berichte 'ID'"
                        + " ,ub.instanznummer 'Instanznummer'"
                        + " ,name 'Instanzname'"
                        + " ,uebungsart 'Uebungsart'"
                        + " ,uebungsunterart 'Uebungsunterart'"
                        + " ,nummer 'Nummer'"
                        + " ,beginn 'Beginn'"
                        + " ,ende 'Ende'"
                        + " ,strasse 'Strasse'"
                        + " ,nummeradr 'NummerAdr'"
                        + " ,stiege 'Stiege'"
                        + " ,plz 'PLZ'"
                        + " ,ort 'Ort'"
                        + " ,meldung 'Meldung'"
                        + " ,Fehlalarm 'Fehlalarm'"
                        + " FROM FDISK.dbo.stmkuebungsberichte ub INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(ub.instanznummer = f.instanznummer)"
                        + " WHERE f.Bereich_Nr = " + intBereichnr;
            } else if (strFubwehr.equals("-2")) {
                sqlString = "SELECT DISTINCT id_berichte 'ID'"
                        + " ,ub.instanznummer 'Instanznummer'"
                        + " ,name 'Instanzname'"
                        + " ,uebungsart 'Uebungsart'"
                        + " ,uebungsunterart 'Uebungsunterart'"
                        + " ,nummer 'Nummer'"
                        + " ,beginn 'Beginn'"
                        + " ,ende 'Ende'"
                        + " ,strasse 'Strasse'"
                        + " ,nummeradr 'NummerAdr'"
                        + " ,stiege 'Stiege'"
                        + " ,plz 'PLZ'"
                        + " ,ort 'Ort'"
                        + " ,meldung 'Meldung'"
                        + " ,Fehlalarm 'Fehlalarm'"
                        + " FROM FDISK.dbo.stmkuebungsberichte ub INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(ub.instanznummer = f.instanznummer)"
                        + " WHERE f.abschnitt_instanznummer = " + intAbschnittnr;
            } else {
                sqlString = "SELECT DISTINCT id_berichte 'ID'"
                        + " ,instanznummer 'Instanznummer'"
                        + " ,name 'Instanzname'"
                        + " ,uebungsart 'Uebungsart'"
                        + " ,uebungsunterart 'Uebungsunterart'"
                        + " ,nummer 'Nummer'"
                        + " ,beginn 'Beginn'"
                        + " ,ende 'Ende'"
                        + " ,strasse 'Strasse'"
                        + " ,nummeradr 'NummerAdr'"
                        + " ,stiege 'Stiege'"
                        + " ,plz 'PLZ'"
                        + " ,ort 'Ort'"
                        + " ,meldung 'Meldung'"
                        + " ,Fehlalarm 'Fehlalarm'"
                        + " FROM FDISK.dbo.stmkuebungsberichte "
                        + " WHERE instanznummer = '" + strFubwehr + "'";
            }
            sqlString += getSqlDateString(strVon, strBis, 3, false);
        }

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

        while (rs.next()) {
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
     * Gibt eine LinkedList mit allen Berichten (Einsatz + Übung + Tätigketi)
     * zurück
     *
     * @param strVon
     * @param strBis
     * @param intBereichnr
     * @param intAbschnittnr
     * @param strFubwehr
     * @return
     * @throws Exception
     */
    public LinkedList<Bericht> getAlleBerichte(String strVon, String strBis, int intBereichnr, int intAbschnittnr, String strFubwehr) throws Exception {
        LinkedList<Bericht> liBericht = new LinkedList<>();

        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        StringBuilder sqlString = new StringBuilder();

        if (intBereichnr == -2) {
            sqlString.append(" SELECT DISTINCT id_berichte 'ID'")
                    .append(" ,instanznummer 'Instanznummer'")
                    .append(" ,name 'Instanzname'")
                    .append(" ,uebungsart 'Art'")
                    .append(" ,nummer 'Nummer'")
                    .append(" ,beginn 'Beginn'")
                    .append(" ,ende 'Ende'")
                    .append(" ,strasse 'Strasse'")
                    .append(" ,nummeradr 'NummerAdr'")
                    .append(" ,stiege 'Stiege'")
                    .append(" ,plz 'PLZ'")
                    .append(" ,ort 'Ort'")
                    .append(" ,meldung 'Meldung'")
                    .append(" ,Fehlalarm 'Fehlalarm'")
                    .append(" , 'Übungsbericht' 'BArt'")
                    .append(" FROM FDISK.dbo.stmkuebungsberichte");
            sqlString.append(getSqlDateString(strVon, strBis, 3, true));
        } else {
            if (intAbschnittnr == -2) {
                sqlString.append(" SELECT DISTINCT id_berichte 'ID'")
                        .append(" ,ub.instanznummer 'Instanznummer'")
                        .append(" ,name 'Instanzname'")
                        .append(" ,uebungsart 'Art'")
                        .append(" ,nummer 'Nummer'")
                        .append(" ,beginn 'Beginn'")
                        .append(" ,ende 'Ende'")
                        .append(" ,strasse 'Strasse'")
                        .append(" ,nummeradr 'NummerAdr'")
                        .append(" ,stiege 'Stiege'")
                        .append(" ,plz 'PLZ'")
                        .append(" ,ort 'Ort'")
                        .append(" ,meldung 'Meldung'")
                        .append(" ,Fehlalarm 'Fehlalarm'")
                        .append(" , 'Übungsbericht' 'BArt'")
                        .append(" FROM FDISK.dbo.stmkuebungsberichte ub INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(ub.instanznummer = f.instanznummer)")
                        .append(" WHERE f.Bereich_Nr = ")
                        .append(intBereichnr);
            } else if (strFubwehr.equals("-2")) {
                sqlString.append(" SELECT DISTINCT id_berichte 'ID'")
                        .append(" ,ub.instanznummer 'Instanznummer'")
                        .append(" ,name 'Instanzname'")
                        .append(" ,uebungsart 'Art'")
                        .append(" ,nummer 'Nummer'")
                        .append(" ,beginn 'Beginn'")
                        .append(" ,ende 'Ende'")
                        .append(" ,strasse 'Strasse'")
                        .append(" ,nummeradr 'NummerAdr'")
                        .append(" ,stiege 'Stiege'")
                        .append(" ,plz 'PLZ'")
                        .append(" ,ort 'Ort'")
                        .append(" ,meldung 'Meldung'")
                        .append(" ,Fehlalarm 'Fehlalarm'")
                        .append(" , 'Übungsbericht' 'BArt'")
                        .append(" FROM FDISK.dbo.stmkuebungsberichte ub INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(ub.instanznummer = f.instanznummer)")
                        .append(" WHERE f.abschnitt_instanznummer = ")
                        .append(intAbschnittnr);
            } else {
                sqlString.append(" SELECT DISTINCT id_berichte 'ID'")
                        .append(" ,instanznummer 'Instanznummer'")
                        .append(" ,name 'Instanzname'")
                        .append(" ,uebungsart 'Art'")
                        .append(" ,nummer 'Nummer'")
                        .append(" ,beginn 'Beginn'")
                        .append(" ,ende 'Ende'")
                        .append(" ,strasse 'Strasse'")
                        .append(" ,nummeradr 'NummerAdr'")
                        .append(" ,stiege 'Stiege'")
                        .append(" ,plz 'PLZ'")
                        .append(" ,ort 'Ort'")
                        .append(" ,meldung 'Meldung'")
                        .append(" ,Fehlalarm 'Fehlalarm'")
                        .append(" , 'Übungsbericht' 'BArt'")
                        .append(" FROM FDISK.dbo.stmkuebungsberichte")
                        .append(" WHERE instanznummer = '")
                        .append(strFubwehr)
                        .append("'");
            }
            sqlString.append(getSqlDateString(strVon, strBis, 3, false));
        }

        if (intBereichnr == -2) {
            sqlString.append(" UNION SELECT DISTINCT id_berichte 'ID'")
                    .append(" ,instanznummer 'Instanznummer'")
                    .append(" ,name 'Name'")
                    .append(" ,einsatzart 'Art'")
                    .append(" ,nummer 'Nr'")
                    .append(" ,uhrzeit_alarmierung 'Uhrzeit_Alarmierung'")
                    .append(" ,uhrzeit_rueckkehr 'Uhrzeit_Rueckkehr'")
                    .append(" ,strasse 'Strasse'")
                    .append(" ,nummeradr 'Nummeradr'")
                    .append(" ,stiege 'Stiege'")
                    .append(" ,plz 'PLZ'")
                    .append(" ,ort 'Ort'")
                    .append(" ,meldung 'Meldung'")
                    .append(" ,Fehlalarm 'Fehlalarm'")
                    .append(" , 'Einsatzbericht' 'BArt'")
                    .append(" FROM FDISK.dbo.stmkeinsatzberichte");
            sqlString.append(getSqlDateString(strVon, strBis, 1, true));
        } else {
            if (intAbschnittnr == -2) {
                sqlString.append(" UNION SELECT DISTINCT id_berichte 'ID'")
                        .append(" ,eb.instanznummer 'Instanznummer'")
                        .append(" ,name 'Name'")
                        .append(" ,einsatzart 'Art'")
                        .append(" ,nummer 'Nr'")
                        .append(" ,uhrzeit_alarmierung 'Uhrzeit_Alarmierung'")
                        .append(" ,uhrzeit_rueckkehr 'Uhrzeit_Rueckkehr'")
                        .append(" ,strasse 'Strasse'")
                        .append(" ,nummeradr 'Nummeradr'")
                        .append(" ,stiege 'Stiege'")
                        .append(" ,plz 'PLZ'")
                        .append(" ,ort 'Ort'")
                        .append(" ,meldung 'Meldung'")
                        .append(" ,Fehlalarm 'Fehlalarm'")
                        .append(" , 'Einsatzbericht' 'BArt'")
                        .append(" FROM FDISK.dbo.stmkeinsatzberichte eb INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(eb.instanznummer = f.instanznummer) ")
                        .append(" WHERE f.Bereich_Nr = ")
                        .append(intBereichnr);
            } else if (strFubwehr.equals("-2")) {
                sqlString.append(" UNION SELECT DISTINCT id_berichte 'ID'")
                        .append(" ,eb.instanznummer 'Instanznummer'")
                        .append(" ,name 'Name'")
                        .append(" ,einsatzart 'Art'")
                        .append(" ,nummer 'Nr'")
                        .append(" ,uhrzeit_alarmierung 'Uhrzeit_Alarmierung'")
                        .append(" ,uhrzeit_rueckkehr 'Uhrzeit_Rueckkehr'")
                        .append(" ,strasse 'Strasse'")
                        .append(" ,nummeradr 'Nummeradr'")
                        .append(" ,stiege 'Stiege'")
                        .append(" ,plz 'PLZ'")
                        .append(" ,ort 'Ort'")
                        .append(" ,meldung 'Meldung'")
                        .append(" ,Fehlalarm 'Fehlalarm'")
                        .append(" , 'Einsatzbericht' 'BArt'")
                        .append(" FROM FDISK.dbo.stmkeinsatzberichte eb INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(eb.instanznummer = f.instanznummer) ")
                        .append(" WHERE f.abschnitt_instanznummer = ")
                        .append(intAbschnittnr);
            } else {
                sqlString.append(" UNION SELECT DISTINCT id_berichte 'ID'")
                        .append(" ,instanznummer 'Instanznummer'")
                        .append(" ,name 'Name'")
                        .append(" ,einsatzart 'Art'")
                        .append(" ,nummer 'Nr'")
                        .append(" ,uhrzeit_alarmierung 'Uhrzeit_Alarmierung'")
                        .append(" ,uhrzeit_rueckkehr 'Uhrzeit_Rueckkehr'")
                        .append(" ,strasse 'Strasse'")
                        .append(" ,nummeradr 'Nummeradr'")
                        .append(" ,stiege 'Stiege'")
                        .append(" ,plz 'PLZ'")
                        .append(" ,ort 'Ort'")
                        .append(" ,meldung 'Meldung'")
                        .append(" ,Fehlalarm 'Fehlalarm'")
                        .append(" , 'Einsatzbericht' 'BArt'")
                        .append(" FROM FDISK.dbo.stmkeinsatzberichte")
                        .append(" WHERE instanznummer = '")
                        .append(strFubwehr)
                        .append("'");
            }
            sqlString.append(getSqlDateString(strVon, strBis, 1, false));
        }

        if (intBereichnr == -2) {
            sqlString.append(" UNION SELECT DISTINCT id_berichte 'ID'")
                    .append(" ,instanznummer 'Instanznummer'")
                    .append(" ,instanzname 'Instanzname'")
                    .append(" ,taetigkeitsart 'Taetigkeitsart'")
                    .append(" ,nummer 'Nummer'")
                    .append(" ,beginn 'Beginn'")
                    .append(" ,ende 'Ende'")
                    .append(" ,strasse 'Strasse'")
                    .append(" ,nummeradr 'NummerAdr'")
                    .append(" ,stiege 'Stiege'")
                    .append(" ,plz 'PLZ'")
                    .append(" ,ort 'Ort'")
                    .append(" ,meldung 'Meldung'")
                    .append(" ,Fehlalarm 'Fehlalarm'")
                    .append(" , 'Tätigkeitsbericht' 'BArt'")
                    .append(" FROM FDISK.dbo.stmktaetigkeitsberichte");
            sqlString.append(getSqlDateString(strVon, strBis, 2, true));
        } else {
            if (intAbschnittnr == -2) {
                sqlString.append(" UNION SELECT DISTINCT id_berichte 'ID'")
                        .append(" ,tb.instanznummer 'Instanznummer'")
                        .append(" ,tb.instanzname 'Instanzname'")
                        .append(" ,taetigkeitsart 'Taetigkeitsart'")
                        .append(" ,nummer 'Nummer'")
                        .append(" ,beginn 'Beginn'")
                        .append(" ,ende 'Ende'")
                        .append(" ,strasse 'Strasse'")
                        .append(" ,nummeradr 'NummerAdr'")
                        .append(" ,stiege 'Stiege'")
                        .append(" ,plz 'PLZ'")
                        .append(" ,ort 'Ort'")
                        .append(" ,meldung 'Meldung'")
                        .append(" ,Fehlalarm 'Fehlalarm'")
                        .append(" , 'Tätigkeitsbericht' 'BArt'")
                        .append(" FROM FDISK.dbo.stmktaetigkeitsberichte tb INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(tb.instanznummer = f.instanznummer)")
                        .append(" WHERE f.Bereich_Nr = ")
                        .append(intBereichnr);
            } else if (strFubwehr.equals("-2")) {
                sqlString.append(" UNION SELECT DISTINCT id_berichte 'ID'")
                        .append(" ,tb.instanznummer 'Instanznummer'")
                        .append(" ,tb.instanzname 'Instanzname'")
                        .append(" ,taetigkeitsart 'Taetigkeitsart'")
                        .append(" ,nummer 'Nummer'")
                        .append(" ,beginn 'Beginn'")
                        .append(" ,ende 'Ende'")
                        .append(" ,strasse 'Strasse'")
                        .append(" ,nummeradr 'NummerAdr'")
                        .append(" ,stiege 'Stiege'")
                        .append(" ,plz 'PLZ'")
                        .append(" ,ort 'Ort'")
                        .append(" ,meldung 'Meldung'")
                        .append(" ,Fehlalarm 'Fehlalarm'")
                        .append(" , 'Tätigkeitsbericht' 'BArt'")
                        .append(" FROM FDISK.dbo.stmktaetigkeitsberichte tb INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(tb.instanznummer = f.instanznummer)")
                        .append(" WHERE f.abschnitt_instanznummer = ")
                        .append(intAbschnittnr);
            } else {
                sqlString.append(" UNION SELECT DISTINCT id_berichte 'ID'")
                        .append(" ,instanznummer 'Instanznummer'")
                        .append(" ,instanzname 'Instanzname'")
                        .append(" ,taetigkeitsart 'Taetigkeitsart'")
                        .append(" ,nummer 'Nummer'")
                        .append(" ,beginn 'Beginn'")
                        .append(" ,ende 'Ende'")
                        .append(" ,strasse 'Strasse'")
                        .append(" ,nummeradr 'NummerAdr'")
                        .append(" ,stiege 'Stiege'")
                        .append(" ,plz 'PLZ'")
                        .append(" ,ort 'Ort'")
                        .append(" ,meldung 'Meldung'")
                        .append(" ,Fehlalarm 'Fehlalarm'")
                        .append(" , 'Tätigkeitsbericht' 'BArt'")
                        .append(" FROM FDISK.dbo.stmktaetigkeitsberichte")
                        .append(" WHERE instanznummer = '")
                        .append(strFubwehr)
                        .append("'");
            }
            sqlString.append(getSqlDateString(strVon, strBis, 2, false));
        }

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
        String strBerichtart;

        while (rs.next()) {
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

            strBerichtart = rs.getString("BArt");

            Bericht bericht = new Bericht(intIdBericht,
                    intInstanznummer, strName, strArt,
                    strNummer, dateBeginn, dateEnde, strStrasse, strNummerAdr,
                    strStiege, strPlz, strOrt, strMeldung, strFehlalarm, strBerichtart);
            liBericht.add(bericht);
        }

        connPool.releaseConnection(conn);
        return liBericht;
    }

    /**
     * Gibt eine LinkedList mit allen Mitgliedern (je nach Berechtigung der
     * Users) die einsatztaugliche Geräteträger sind, zurück.
     *
     * @param intBereichnr
     * @param intAbschnittnr
     * @param strFubwehr
     * @return
     * @throws Exception
     */
    public LinkedList<Geraetetraegermitglied> getGereatetraegerMitglied(int intBereichnr, int intAbschnittnr, String strFubwehr) throws Exception {
        LinkedList<Geraetetraegermitglied> liGeraetetraeger = new LinkedList<>();

        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String strStatement = "SELECT DISTINCT p.instanznummer 'Instanznr'"
                + " ,p.standesbuchnummer 'STB'"
                + " ,p.dienstgrad 'DGR'"
                + " ,p.titel 'Titel'"
                + " ,p.vorname 'Vorname'"
                + " ,p.zuname 'Zuname'"
                + " ,p.geburtsdatum 'GebDate'"
                + " ,p.untersuchungsdatum 'UDatum'"
                + " ,p.naechste_untersuchung_am 'NaechsteUDate'"
                + " ,p.id_personen 'PersID'"
                + " ,p.id_instanzen 'IdInstanzen'"
                + " ,g.Anzahl 'Anz'"
                + " ,g.instanzname 'Instanzname'"
                + " FROM FDISK.dbo.qry_fdisk_einsatztaugliche_ats_geraetetraeger_person p"
                + " INNER JOIN FDISK.dbo.qry_fdisk_einsatztaugliche_ats_geraetetraeger g"
                + " ON(p.instanznummer = g.instanznummer)"
                + " INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich fw"
                + " ON(p.instanznummer = fw.instanznummer)";

        if (intBereichnr == -2) {
        } else if (intAbschnittnr == -2) {
            strStatement += " WHERE fw.Bereich_Nr = " + intBereichnr;
        } else if (strFubwehr.equals("-2")) {
            strStatement += " WHERE fw.abschnitt_instanznummer = " + intAbschnittnr;
        } else {
            strStatement += " WHERE p.instanznummer = '" + strFubwehr + "'";
        }

        StringBuilder sqlString = new StringBuilder();
        sqlString.append(strStatement);

        ResultSet rs = stat.executeQuery(sqlString.toString());

        int intInstanznr;
        String strSTB;
        String strDGR;
        String strTitel;
        String strVorname;
        String strZuname;
        Date dateGeb;
        Date dateUntersuchung;
        Date dateNaechsteUntersuchung;
        int intPersID;
        int intIdInstanzen;
        int intAnzahl;
        String strInstanzname;

        while (rs.next()) {
            intPersID = rs.getInt("PersID");
            strSTB = rs.getString("STB");
            strDGR = rs.getString("DGR");
            strTitel = rs.getString("Titel");
            strVorname = rs.getString("Vorname");
            strZuname = rs.getString("Zuname");
            intInstanznr = rs.getInt("Instanznr");
            dateGeb = rs.getDate("GebDate");
            dateUntersuchung = rs.getTimestamp("UDatum");
            dateNaechsteUntersuchung = rs.getTimestamp("NaechsteUDate");
            intIdInstanzen = rs.getInt("IdInstanzen");
            intAnzahl = rs.getInt("Anz");
            strInstanzname = rs.getString("Instanzname");

            Geraetetraegermitglied gm = new Geraetetraegermitglied(intInstanznr, dateGeb, dateUntersuchung, dateNaechsteUntersuchung, intIdInstanzen, intAnzahl, intPersID, strSTB, strDGR, strTitel, strVorname, strZuname, strInstanzname, "" + intInstanznr);
            liGeraetetraeger.add(gm);
        }

        connPool.releaseConnection(conn);
        return liGeraetetraeger;
    }

    //Dynamischer Berichtegenerator
    /**
     * Ruft die richtige Methode für einen Typ Filter auf
     *
     * @return
     * @throws Exception
     */
    public HashMap<String, LinkedList<String>> getMethodeFuerTyp() throws Exception {
        HashMap<String, LinkedList<String>> hmAlleTypenUndFilter = new HashMap<>();
        hmAlleTypenUndFilter.put("KURSBEZEICHNUNG", getFilterFuerKursbezeichnung());
        hmAlleTypenUndFilter.put("FUNKTIONSBEZEICHNUNG", getFilterFuerFunktionsbezeichnung());
        hmAlleTypenUndFilter.put("FUNKTIONSINSTANZ", getFilterFuerFunktionsinstanz());
        hmAlleTypenUndFilter.put("STATUS", getFilterFuerStatus());
        hmAlleTypenUndFilter.put("ERREICHBARKEITSART", getFilterFuerErreichbarkeitsart());
        hmAlleTypenUndFilter.put("STAATSBÜRGERSCHAFT", getFilterFuerStaatsbuergerschaft());
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

    /**
     * Gibt alle in der Datenbank vorkommenden Geschlechter zurück
     *
     * @return
     * @throws Exception
     */
    public LinkedList<String> getFilterFuerGeschlecht() throws Exception {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerGeschlecht.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next()) {
            String strFilter = rs.getString("Geschlecht");
            if (!strFilter.isEmpty() && !strFilter.equals(" ")) {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);

        return liFilter;
    }

    /**
     * Gibt alle in der Datenbank vorkommenden Staatsbürgerschaften zurück
     *
     * @return
     * @throws Exception
     */
    public LinkedList<String> getFilterFuerStaatsbuergerschaft() throws Exception {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerStaatsbuergerschaft.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next()) {
            String strFilter = rs.getString("Staatsbuergerschaft");
            if (!strFilter.isEmpty() && !strFilter.equals(" ")) {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);

        return liFilter;
    }

    /**
     * Gibt alle in der Datenbank vorkommenden ISCO-Berufe zurück
     *
     * @return
     * @throws Exception
     */
    public LinkedList<String> getFilterFuerBeruf() throws Exception {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerBeruf.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next()) {
            String strFilter = rs.getString("Beruf");
            if (!strFilter.isEmpty() && !strFilter.equals(" ")) {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);

        return liFilter;
    }

    /**
     * Gibt alle in der Datenbank vorkommenden Titel zurück
     *
     * @return
     * @throws Exception
     */
    public LinkedList<String> getFilterFuerTitel() throws Exception {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerTitel.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next()) {
            String strFilter = rs.getString("Titel");
            if (!strFilter.isEmpty() && !strFilter.equals(" ")) {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);

        return liFilter;
    }

    /**
     * Gibt alle in der Datenbank vorkommenden Amtstitel zurück
     *
     * @return
     * @throws Exception
     */
    public LinkedList<String> getFilterFuerAmtstitel() throws Exception {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerAmtstitel.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next()) {
            String strFilter = rs.getString("Amtstitel");
            if (!strFilter.isEmpty() && !strFilter.equals(" ")) {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);

        return liFilter;
    }

    /**
     * Gibt alle in der Datenbank vorkommenden Blutgruppen zurück
     *
     * @return
     * @throws Exception
     */
    public LinkedList<String> getFilterFuerBlutgruppe() throws Exception {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerBlutgruppe.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next()) {
            String strFilter = rs.getString("Blutgruppe");
            if (!strFilter.isEmpty() && !strFilter.equals(" ")) {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);

        return liFilter;
    }

    /**
     * Gibt alle in der Datenbank vorkommenden Familienstände zurück
     *
     * @return
     * @throws Exception
     */
    public LinkedList<String> getFilterFuerFamilienstand() throws Exception {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerFamilienstand.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next()) {
            String strFilter = rs.getString("Familienstand");
            if (!strFilter.isEmpty() && !strFilter.equals(" ")) {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);

        return liFilter;
    }

    /**
     * Gibt alle in der Datenbank vorkommenden Dienstgrade zurück
     *
     * @return
     * @throws Exception
     */
    public LinkedList<String> getFilterFuerDienstgrad() throws Exception {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerDienstgrade.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next()) {
            String strFilter = rs.getString("Dienstgrad");
            if (!strFilter.isEmpty() && !strFilter.equals(" ")) {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);

        return liFilter;
    }

    /**
     * Gibt alle verfügbaren Anreden zurück ("Herr" und "Frau")
     *
     * @return
     * @throws Exception
     */
    public LinkedList<String> getFilterFuerAnrede() throws Exception {
        LinkedList<String> liFilter = new LinkedList<>();
        liFilter.add("Frau");
        liFilter.add("Herr");

        return liFilter;
    }

    /**
     * Gibt alle in der Datenbank vorkommenden Kursbezeichnungen zurück
     *
     * @return
     * @throws Exception
     */
    public LinkedList<String> getFilterFuerKursbezeichnung() throws Exception {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerKursbezeichnung.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next()) {
            String strFilter = rs.getString("Kursbezeichnung");
            if (!strFilter.isEmpty() && !strFilter.equals(" ")) {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);

        return liFilter;
    }

    /**
     * Gibt alle in der Datenbank vorkommenden Funktionsbezeichnungen zurück
     *
     * @return
     * @throws Exception
     */
    public LinkedList<String> getFilterFuerFunktionsbezeichnung() throws Exception {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerFunktionsbezeichnung.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next()) {
            String strFilter = rs.getString("Bezeichnung");
            if (!strFilter.isEmpty() && !strFilter.equals(" ")) {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);

        return liFilter;
    }

    /**
     * Gibt alle in der Datenbank vorkommenden Funktionsinstanzen zurück
     *
     * @return
     * @throws Exception
     */
    public LinkedList<String> getFilterFuerFunktionsinstanz() throws Exception {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerFunktionsinstanz.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next()) {
            String strFilter = rs.getString("Id_Instanztypen");
            if (!strFilter.isEmpty() && !strFilter.equals(" ")) {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);

        return liFilter;
    }

    /**
     * Gibt alle in der Datenbank vorkommenden Status zurück
     *
     * @return
     * @throws Exception
     */
    public LinkedList<String> getFilterFuerStatus() throws Exception {
        LinkedList<String> liFilter = new LinkedList<>();
        liFilter.add("Abgemeldet");
        liFilter.add("Aktiv");
        liFilter.add("Ehrenmitglied");
        liFilter.add("Jugend");
        liFilter.add("Reserve");

        return liFilter;
    }

    /**
     * Gibt alle in der Datenbank vorkommenden Erreichbarkeitsarten zurück
     *
     * @return
     * @throws Exception
     */
    public LinkedList<String> getFilterFuerErreichbarkeitsart() throws Exception {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerErreichbarkeitsart.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next()) {
            String strFilter = rs.getString("Erreichbarkeitsart");
            if (!strFilter.isEmpty() && !strFilter.equals(" ")) {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);
        return liFilter;
    }

    /**
     * Gibt alle in der Datenbank vorkommenden Auszeichnungen zurück
     *
     * @return
     * @throws Exception
     */
    public LinkedList<String> getFilterFuerAuszeichnung() throws Exception {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerAuszeichnung.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next()) {
            String strFilter = rs.getString("Auszeichnung");
            if (!strFilter.isEmpty() && !strFilter.equals(" ")) {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);
        return liFilter;
    }

    /**
     * Gibt alle in der Datenbank vorkommenden Auszeichnungsstufen zurück
     *
     * @return
     * @throws Exception
     */
    public LinkedList<String> getFilterFuerAuszeichnungsstufe() throws Exception {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerAuszeichnungsstufe.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next()) {
            String strFilter = rs.getString("Auszeichnungsstufe");
            if (!strFilter.isEmpty() && !strFilter.equals(" ")) {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);
        return liFilter;
    }

    /**
     * Gibt alle in der Datenbank vorkommenden Leistungsabzeichenstufen zurück
     *
     * @return
     * @throws Exception
     */
    public LinkedList<String> getFilterFuerLeistungsabzeichenstufe() throws Exception {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerLeistungsabzeichenstufe.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next()) {
            String strFilter = rs.getString("Stufe");
            if (!strFilter.isEmpty() && !strFilter.equals(" ")) {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);
        return liFilter;
    }

    /**
     * Gibt alle in der Datenbank vorkommenden Leistungsabzeichenbezeichnungen
     * zurück
     *
     * @return
     * @throws Exception
     */
    public LinkedList<String> getFilterFuerLeistungsabzeichenbezeichnung() throws Exception {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerLeistungsabzeichenbezeichnung.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next()) {
            String strFilter = rs.getString("Bezeichnung");
            if (!strFilter.isEmpty() && !strFilter.equals(" ")) {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);
        return liFilter;
    }

    /**
     * Gibt alle in der Datenbank vorkommenden Führerscheinklassen zurück
     *
     * @return
     * @throws Exception
     */
    public LinkedList<String> getFilterFuerFuehrerscheinklassen() throws Exception {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerFuehrerscheinklassen.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next()) {
            String strFilter = rs.getString("Fahrgenehmigungsklasse");
            if (!strFilter.isEmpty() && !strFilter.equals(" ")) {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);
        return liFilter;
    }

    /**
     * Gibt alle in der Datenbank vorkommenden Untersuchungsarten zurück
     *
     * @return
     * @throws Exception
     */
    public LinkedList<String> getFilterFuerUntersuchungsart() throws Exception {
        LinkedList<String> liFilter = new LinkedList<>();
        Connection conn = connPool.getConnection();
        PreparedStatement prepStat = conn.prepareStatement(EnFilterStatements.getFilterFuerUntersuchungsart.toString());

        ResultSet rs = prepStat.executeQuery();

        while (rs.next()) {
            String strFilter = rs.getString("Expr1");
            if (!strFilter.isEmpty() && !strFilter.equals(" ")) {
                liFilter.add(strFilter);
            }
        }
        connPool.releaseConnection(conn);
        return liFilter;
    }

    /**
     * Generiert aus einem 2-dimensionalen Eingabearray und den spezifizierten
     * Ausgabespalten das Statement und gibt den daraus erhaltenen fertigen
     * Bericht als HTML-Tabelle zurück (berücksichtigt die Berechtigungen des
     * Users) zurück
     *
     * @param strEingabe
     * @param strSelectedColumns
     * @param intBereichnr
     * @param intAbschnittnr
     * @param strFubwehr
     * @return
     * @throws Exception
     */
    public StringBuilder getDynamischerBericht(String strEingabe[][], String strSelectedColumns, int intBereichnr, int intAbschnittnr, String strFubwehr) throws Exception {
        StringBuilder sbHelper = new StringBuilder(strSelectedColumns);
        sbHelper.insert(0, "Standesbuchnummer;Dienstgrad;Vorname;Zuname;");
        strSelectedColumns = sbHelper.toString();
        String[] strSelectedCols = strSelectedColumns.split(";");
        LinkedList<String> liSpaltenUeberschriften = new LinkedList<>();
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
        String strSpaltenUeberschrift;

        //LinkedLists für doppelte Spalten
        LinkedList<Integer> liDoppelteAuszeichnungsart = new LinkedList<>();
        LinkedList<Integer> liDoppelteAuszeichnungsstufe = new LinkedList<>();
        LinkedList<Integer> liDoppelteDienstgrad = new LinkedList<>();
        LinkedList<Integer> liDoppelteErreichbarkeitsart = new LinkedList<>();
        LinkedList<Integer> liDoppelteFuehrerscheinklasse = new LinkedList<>();
        LinkedList<Integer> liDoppelteFunktionsbezeichnung = new LinkedList<>();
        LinkedList<Integer> liDoppelteFunktionsinstanz = new LinkedList<>();
        LinkedList<Integer> liDoppelteKursbezeichnung = new LinkedList<>();
        LinkedList<Integer> liDoppelteLeistungsabzeichenStufe = new LinkedList<>();
        LinkedList<Integer> liDoppelteLeistungsabzeichenbezeichnung = new LinkedList<>();
        LinkedList<Integer> liDoppelteUntersuchungsart = new LinkedList<>();

        int intRows = strEingabe.length % 6;

        initializeDBValuesWithDBTypes();

        for (int i = 0; i < intRows; i++) {
            for (int j = 0; j < 6; j++) {
                switch (strEingabe[i][1].toUpperCase()) {
                    case "AUSZEICHNUNGSART":
                        if (i > 0 && (strEingabe[i - 1][5].equals("UND") || strEingabe[i - 1][5].equals("UND NICHT"))) {
                            if (!liDoppelteAuszeichnungsart.contains(i)) {
                                liDoppelteAuszeichnungsart.add(i);
                            }

                        } else if (i == 0) {
                            if (!liDoppelteAuszeichnungsart.contains(i)) {
                                liDoppelteAuszeichnungsart.add(i);
                            }
                        }
                        break;
                    case "AUSZEICHNUNGSSTUFE":
                        if (i > 0 && (strEingabe[i - 1][5].equals("UND") || strEingabe[i - 1][5].equals("UND NICHT"))) {
                            if (!liDoppelteAuszeichnungsstufe.contains(i)) {
                                liDoppelteAuszeichnungsstufe.add(i);
                            }

                        } else if (i == 0) {
                            if (!liDoppelteAuszeichnungsstufe.contains(i)) {
                                liDoppelteAuszeichnungsstufe.add(i);
                            }
                        }
                        break;
                    case "DIENSTGRAD":
                        if (i > 0 && (strEingabe[i - 1][5].equals("UND") || strEingabe[i - 1][5].equals("UND NICHT"))) {
                            if (!liDoppelteDienstgrad.contains(i)) {
                                liDoppelteDienstgrad.add(i);
                            }

                        } else if (i == 0) {
                            if (!liDoppelteDienstgrad.contains(i)) {
                                liDoppelteDienstgrad.add(i);
                            }
                        }
                        break;
                    case "ERREICHBARKEITSART":
                        if (i > 0 && (strEingabe[i - 1][5].equals("UND") || strEingabe[i - 1][5].equals("UND NICHT"))) {
                            if (!liDoppelteErreichbarkeitsart.contains(i)) {
                                liDoppelteErreichbarkeitsart.add(i);
                            }

                        } else if (i == 0) {
                            if (!liDoppelteErreichbarkeitsart.contains(i)) {
                                liDoppelteErreichbarkeitsart.add(i);
                            }
                        }
                        break;
                    case "FÜHRERSCHEINKLASSE":
                        if (i > 0 && (strEingabe[i - 1][5].equals("UND") || strEingabe[i - 1][5].equals("UND NICHT"))) {
                            if (!liDoppelteFuehrerscheinklasse.contains(i)) {
                                liDoppelteFuehrerscheinklasse.add(i);
                            }

                        } else if (i == 0) {
                            if (!liDoppelteFuehrerscheinklasse.contains(i)) {
                                liDoppelteFuehrerscheinklasse.add(i);
                            }
                        }
                        break;
                    case "FUNKTIONSBEZEICHNUNG":
                        if (i > 0 && (strEingabe[i - 1][5].equals("UND") || strEingabe[i - 1][5].equals("UND NICHT"))) {
                            if (!liDoppelteFunktionsbezeichnung.contains(i)) {
                                liDoppelteFunktionsbezeichnung.add(i);
                            }

                        } else if (i == 0) {
                            if (!liDoppelteFunktionsbezeichnung.contains(i)) {
                                liDoppelteFunktionsbezeichnung.add(i);
                            }
                        }
                        break;
                    case "FUNKTIONSINSTANZ":
                        if (i > 0 && (strEingabe[i - 1][5].equals("UND") || strEingabe[i - 1][5].equals("UND NICHT"))) {
                            if (!liDoppelteFunktionsinstanz.contains(i)) {
                                liDoppelteFunktionsinstanz.add(i);
                            }

                        } else if (i == 0) {
                            if (!liDoppelteFunktionsinstanz.contains(i)) {
                                liDoppelteFunktionsinstanz.add(i);
                            }
                        }
                        break;
                    case "KURSBEZEICHNUNG":
                        if (i > 0 && (strEingabe[i - 1][5].equals("UND") || strEingabe[i - 1][5].equals("UND NICHT"))) {
                            if (!liDoppelteKursbezeichnung.contains(i)) {
                                liDoppelteKursbezeichnung.add(i);
                            }

                        } else if (i == 0) {
                            if (!liDoppelteKursbezeichnung.contains(i)) {
                                liDoppelteKursbezeichnung.add(i);
                            }
                        }
                        break;
                    case "LEISTUNGSABZEICHEN STUFE":
                        if (i > 0 && (strEingabe[i - 1][5].equals("UND") || strEingabe[i - 1][5].equals("UND NICHT"))) {
                            if (!liDoppelteLeistungsabzeichenStufe.contains(i)) {
                                liDoppelteLeistungsabzeichenStufe.add(i);
                            }

                        } else if (i == 0) {
                            if (!liDoppelteLeistungsabzeichenStufe.contains(i)) {
                                liDoppelteLeistungsabzeichenStufe.add(i);
                            }
                        }
                        break;
                    case "LEISTUNGSABZEICHENBEZEICHNUNG":
                        if (i > 0 && (strEingabe[i - 1][5].equals("UND") || strEingabe[i - 1][5].equals("UND NICHT"))) {
                            if (!liDoppelteLeistungsabzeichenbezeichnung.contains(i)) {
                                liDoppelteLeistungsabzeichenbezeichnung.add(i);
                            }

                        } else if (i == 0) {
                            if (!liDoppelteLeistungsabzeichenbezeichnung.contains(i)) {
                                liDoppelteLeistungsabzeichenbezeichnung.add(i);
                            }
                        }
                        break;
                    case "UNTERSUCHUNGSART":
                        if (i > 0 && (strEingabe[i - 1][5].equals("UND") || strEingabe[i - 1][5].equals("UND NICHT"))) {
                            if (!liDoppelteUntersuchungsart.contains(i)) {
                                liDoppelteUntersuchungsart.add(i);
                            }

                        } else if (i == 0) {
                            if (!liDoppelteUntersuchungsart.contains(i)) {
                                liDoppelteUntersuchungsart.add(i);
                            }
                        }
                        break;
                }
                switch (strEingabe[i][j].toUpperCase()) {
                    case "N/A":
                        strEingabe[i][j] = "";
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

        //damit ich nicht zwei verschiedene listen für den gleichen Join habe
        liDoppelteAuszeichnungsart.addAll(liDoppelteAuszeichnungsstufe);
        liDoppelteFunktionsbezeichnung.addAll(liDoppelteFunktionsinstanz);
        liDoppelteLeistungsabzeichenStufe.addAll(liDoppelteLeistungsabzeichenbezeichnung);

        for (int i = 0; i < intRows; i++) {
            strSpaltenUeberschrift = strEingabe[i][1];

            if (strSpaltenUeberschrift.toUpperCase().equals("STATUS")) {
                strSpaltenUeberschrift = strEingabe[i][3];
            }

            if (!liSpaltenUeberschriften.contains(strSpaltenUeberschrift)) {
                liSpaltenUeberschriften.add(strSpaltenUeberschrift);
            }
        }

        if (liSpaltenUeberschriften.contains("Expr1") || liSpaltenUeberschriften.contains("u.Datum")) {
            boUntersuchungen = true;

        }
        if (liSpaltenUeberschriften.contains("Bezeichnung")
                || liSpaltenUeberschriften.contains("Stufe")
                || liSpaltenUeberschriften.contains("lam.Datum")) {
            boLeistungsabzeichen = true;

        }
        if (liSpaltenUeberschriften.contains("Kursbezeichnung") || liSpaltenUeberschriften.contains("k.Datum")) {
            boKurse = true;

        }
        if (liSpaltenUeberschriften.contains("id_instanztypen")
                || liSpaltenUeberschriften.contains("datum_von")
                || liSpaltenUeberschriften.contains("datum_bis")
                || liSpaltenUeberschriften.contains("f.bezeichnung")) {
            boFunktionen = true;

        }
        if (liSpaltenUeberschriften.contains("Fahrgenehmigungsklasse") || liSpaltenUeberschriften.contains("Gueltig_bis")) {
            boFahrgenehmigungen = true;

        }
        if (liSpaltenUeberschriften.contains("Auszeichnungsart")
                || liSpaltenUeberschriften.contains("Auszeichnungsstufe")
                || liSpaltenUeberschriften.contains("Verleihungsdatum")) {
            boAuszeichnung = true;

        }

        if (liSpaltenUeberschriften.contains("Code") || liSpaltenUeberschriften.contains("Erreichbarkeitsart")) {
            boErreichbarkeiten = true;

        }
        if (liSpaltenUeberschriften.contains("Straße") || liSpaltenUeberschriften.contains("Hausnummer")
                || liSpaltenUeberschriften.contains("Stiege/Stock/Tür") || liSpaltenUeberschriften.contains("Ort")) {
            boAdresse = true;

        }

        if (liSpaltenUeberschriften.contains("Vordienstzeit")) {
            boVordienstzeit = true;

        }

        if (liSpaltenUeberschriften.contains("Jugend") || liSpaltenUeberschriften.contains("Aktiv")
                || liSpaltenUeberschriften.contains("Reserve") || liSpaltenUeberschriften.contains("Abgemeldet")
                || liSpaltenUeberschriften.contains("Ehrenmitglied")) {
            boErreichbarkeiten = true;

        }

        StringBuilder sbSqlString = new StringBuilder("SELECT DISTINCT ");

        for (int i = 0; i < strSelectedCols.length; i++) {
            switch (strSelectedCols[i].toUpperCase()) {

                case "ANREDE":
                    strSelectedCols[i] = "Geschlecht";
                    boAnrede = true;
                    break;
                case "GESCHLECHT":
                    boAnrede = false;
                    break;
                case "STAATSBÜRGERSCHAFT":
                    strSelectedCols[i] = "Staatsbuergerschaft";
                    break;
                case "ISCO-BERUF":
                    strSelectedCols[i] = "Beruf";
                    break;
                case "Instanzname":
                    strSelectedCols[i] = "Instanzname";
                    break;

            }
            switch (strSelectedCols[i]) {
                case "Alter":
                    sbSqlString.append("DATEDIFF(YY, geburtsdatum, GETDATE()) - CASE WHEN DATEADD(YY, DATEDIFF(YY,geburtsdatum, GETDATE()), geburtsdatum) > GETDATE() THEN 1 ELSE 0 END 'Lebensalter', ");
                    break;
                case "Status":
                    sbSqlString.append("Jugend, Aktiv, Reserve, Abgemeldet, Ehrenmitglied, ");
                    break;
                default:
                    sbSqlString.append("m.").append(strSelectedCols[i]).append(",");
                    System.out.println(sbSqlString.toString());
                    break;
            }
            if (!liSpaltenUeberschriften.contains(strSelectedCols[i])) {
                liSpaltenUeberschriften.add(strSelectedCols[i]);
            }
        }

        sbSqlString = new StringBuilder(sbSqlString.subSequence(0, sbSqlString.lastIndexOf(",")));
        sbSqlString.append(" ");

        sbSqlString.append("FROM FDISK.dbo.stmkmitglieder m ");
        if (liDoppelteDienstgrad.size() > 0) {
            for (Integer i : liDoppelteDienstgrad) {
                sbSqlString.append(" INNER JOIN FDISK.dbo.stmkmitglieder m").append(i).append(" ON(m.id_personen = m").append(i).append(".id_personen) ");
            }
        }
        if (boAdresse == true) {
            sbSqlString.append(" INNER JOIN FDISK.dbo.stmkadressen a ON(a.id_personen = m.id_personen) ");
        }

        if (boAuszeichnung == true) {
            if (liDoppelteAuszeichnungsart.size() > 0) {
                for (Integer i : liDoppelteAuszeichnungsart) {
                    sbSqlString.append(" INNER JOIN FDISK.dbo.stmkauszeichnungenmitglieder auszm").append(i).append(" ON(auszm").append(i).append(".id_personen = m.id_personen) ").append(" INNER JOIN FDISK.dbo.stmkauszeichnungen ausz").append(i).append(" ON(auszm").append(i).append(".id_auszeichnungen = ausz").append(i)
                            .append(".id_auszeichnungen) ");
                }

            } else {
                sbSqlString.append(" INNER JOIN FDISK.dbo.stmkauszeichnungenmitglieder auszm ON(auszm.id_personen = m.id_personen) ")
                        .append(" INNER JOIN FDISK.dbo.stmkauszeichnungen ausz ON(auszm.id_auszeichnungen = ausz.id_auszeichnungen) ");
            }

        }

        if (boLeistungsabzeichen == true) {
            if (liDoppelteLeistungsabzeichenStufe.size() > 0) {
                for (Integer i : liDoppelteLeistungsabzeichenStufe) {
                    sbSqlString.append(" INNER JOIN FDISK.dbo.stmkleistungsabzeichenmitglieder lam").append(i).append(" ON(m.id_personen = lam").append(i).append(".id_personen) ").append(" INNER JOIN FDISK.dbo.stmkleistungsabzeichen la").append(i).append(" ON(la").append(i).append(".id_leistungsabzeichen = lam").append(i)
                            .append(".id_leistungsabzeichen) ");
                }

            } else {
                sbSqlString.append(" INNER JOIN FDISK.dbo.stmkleistungsabzeichenmitglieder lam ON(m.id_personen = lam.id_personen) ")
                        .append(" INNER JOIN FDISK.dbo.stmkleistungsabzeichen la ON(la.id_leistungsabzeichen = lam.id_leistungsabzeichen) ");
            }

        }

        if (boKurse == true) {
            if (liDoppelteKursbezeichnung.size() > 0) {
                for (Integer i : liDoppelteKursbezeichnung) {
                    sbSqlString.append(" INNER JOIN FDISK.dbo.stmkkursemitglieder km").append(i).append(" ON(m.id_personen = km").append(i).append(".id_mitgliedschaften) ").append(" INNER JOIN FDISK.dbo.stmkkurse k").append(i).append(" ON (k").append(i).append(".id_kurse = km").append(i)
                            .append(".id_kurse) ");
                }

            } else {
                sbSqlString.append(" INNER JOIN FDISK.dbo.stmkkursemitglieder km ON(m.id_personen = km.id_mitgliedschaften) ")
                        .append(" INNER JOIN FDISK.dbo.stmkkurse k ON (k.id_kurse = km.id_kurse) ");
            }

        }
        if (boErreichbarkeiten == true) {
            if (liDoppelteErreichbarkeitsart.size() > 0) {
                for (Integer i : liDoppelteErreichbarkeitsart) {
                    sbSqlString.append(" INNER JOIN FDISK.dbo.stmkerreichbarkeiten e").append(i).append(" ON(m.id_personen = e").append(i).append(".id_personen) ");
                }

            } else {
                sbSqlString.append(" INNER JOIN FDISK.dbo.stmkerreichbarkeiten e ON(m.id_personen = e.id_personen) ");
            }
        }

        if (boFahrgenehmigungen == true) {
            if (liDoppelteFuehrerscheinklasse.size() > 0) {
                for (Integer i : liDoppelteFuehrerscheinklasse) {
                    sbSqlString.append(" INNER JOIN FDISK.dbo.stmkgesetzl_fahrgenehmigungen gf").append(i).append(" ON(m.id_personen = gf").append(i).append(".fdisk_personen_id) ");
                }
            } else {
                sbSqlString.append(" INNER JOIN FDISK.dbo.stmkgesetzl_fahrgenehmigungen gf ON(m.id_personen = gf.fdisk_personen_id) ");
            }
        }

        if (boFunktionen == true) {
            if (liDoppelteFunktionsbezeichnung.size() > 0) {
                for (Integer i : liDoppelteFunktionsbezeichnung) {
                    sbSqlString.append(" INNER JOIN FDISK.dbo.stmkfunktionenmitglieder fm").append(i).append(" ON(m.id_personen = fm").append(i).append(".id_mitgliedschaften) ").append(" INNER JOIN FDISK.dbo.stmkfunktionen f").append(i).append(" ON(f").append(i).append(".id_funktionen = fm").append(i)
                            .append(".id_funktionen) ");
                }
            } else {
                sbSqlString.append(" INNER JOIN FDISK.dbo.stmkfunktionenmitglieder fm ON(m.id_personen = fm.id_mitgliedschaften) ")
                        .append(" INNER JOIN FDISK.dbo.stmkfunktionen f ON(f.id_funktionen = fm.id_funktionen) ");
            }
        }

        if (boUntersuchungen == true) {
            if (liDoppelteUntersuchungsart.size() > 0) {
                for (Integer i : liDoppelteUntersuchungsart) {
                    sbSqlString.append(" INNER JOIN FDISK.dbo.stmkuntersuchungenmitglieder u").append(i).append(" ON(m.id_mitgliedschaften = u").append(i).append(".id_mitgliedschaften) ");
                }
            } else {
                sbSqlString.append(" INNER JOIN FDISK.dbo.stmkuntersuchungenmitglieder u ON(m.id_mitgliedschaften = u.id_mitgliedschaften) ");
            }

        }

        if (boVordienstzeit == true) {
            sbSqlString.append(" INNER JOIN FDISK.dbo.FDISK_MAPPING_VD_ZEIT z ON(m.id_personen = z.id_personen) ");
        }

        sbSqlString.append(" INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich fw ON(m.instanznummer = fw.instanznummer) ");

        sbSqlString.append(" WHERE (m.abgemeldet = 0) AND (NOT (LEFT(m.instanznummer, 2) = 'GA')) AND (NOT (LEFT(m.instanzname, 7) = 'FW GAST')) AND (");

        String strColLink = "";
        String strBracketOpen = "";
        String strBracketClose = "";

        for (int i = 0; i < intRows; i++) {
            String strColWhere = strEingabe[i][1];
            String strColSymbol = strEingabe[i][2];
            String strColValue = strEingabe[i][3];
            strColLink = strEingabe[i][5];
            strBracketOpen = (strEingabe[i][0].equals("")) ? "" : "(";
            strBracketClose = (strEingabe[i][4].equals("")) ? "" : ")";
            String strColWhereType = getDBTypeForValue(strColWhere);

            if (strColSymbol.equals("<>")) {
                strColSymbol = "!=";
            }

            switch (strColLink) {
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

            if (strColWhere.equals("Geschlecht")) {
                switch (strColValue) {
                    case "Herr":
                        strColValue = "m";
                        break;
                    case "Frau":
                        strColValue = "w";
                        break;
                }
                sbSqlString.append(strBracketOpen).append(strColWhere).append(" ").append(strColSymbol).append(" '").append(strColValue).append("' ").append(strBracketClose).append(strColLink).append(" ");
                continue;
            }

            if (strColWhere.equals("Vordienstzeit")) {
                sbSqlString.append("ROUND(VD_ZEIT, 0, 1) ").append(strColSymbol).append(strColValue).append(" ");
            } else if (!strColWhere.equals("Alter") && !strColWhere.equals("Status")) {
                switch (strColWhereType) {
                    case "datetime":
                        sbSqlString.append(strBracketOpen).append(strColWhere).append(" ").append(strColSymbol).append(" CAST('").append(strColValue).append("' AS datetime) ").append(strBracketClose).append(strColLink).append(" ");
                        break;
                    case "varchar":
                        if (liDoppelteAuszeichnungsart.contains(i)) {
                            sbSqlString.append(strBracketOpen).append("UPPER(").append("ausz").append(i).append(".").append(strColWhere).append(") ").append(strColSymbol).append(" '").append(strColValue.toUpperCase()).append("' ").append(strBracketClose).append(strColLink).append(" ");
                            break;

                        } else if (liDoppelteDienstgrad.contains(i)) {
                            sbSqlString.append(strBracketOpen).append("UPPER(").append("m").append(i).append(".").append(strColWhere).append(") ").append(strColSymbol).append(" '").append(strColValue.toUpperCase()).append("' ").append(strBracketClose).append(strColLink).append(" ");
                            break;
                        } else if (liDoppelteErreichbarkeitsart.contains(i)) {
                            sbSqlString.append(strBracketOpen).append("UPPER(").append("e").append(i).append(".").append(strColWhere).append(") ").append(strColSymbol).append(" '").append(strColValue.toUpperCase()).append("' ").append(strBracketClose).append(strColLink).append(" ");
                            break;
                        } else if (liDoppelteFuehrerscheinklasse.contains(i)) {
                            sbSqlString.append(strBracketOpen).append("UPPER(").append("gf").append(i).append(".").append(strColWhere).append(") ").append(strColSymbol).append(" '").append(strColValue.toUpperCase()).append("' ").append(strBracketClose).append(strColLink).append(" ");
                            break;
                        } else if (liDoppelteFunktionsbezeichnung.contains(i)) {
                            if (strColWhere.contains("f.")) {
                                sbSqlString.append(strBracketOpen).append("UPPER(").append("f").append(i).append(".").append(strColWhere.substring(2)).append(") ").append(strColSymbol).append(" '").append(strColValue.toUpperCase()).append("' ").append(strBracketClose).append(strColLink).append(" ");

                            } else {
                                sbSqlString.append(strBracketOpen).append("UPPER(").append("f").append(i).append(".").append(strColWhere).append(") ").append(strColSymbol).append(" '").append(strColValue.toUpperCase()).append("' ").append(strBracketClose).append(strColLink).append(" ");

                            }
                            break;

                        } else if (liDoppelteKursbezeichnung.contains(i)) {
                            sbSqlString.append(strBracketOpen).append("UPPER(").append("k").append(i).append(".").append(strColWhere).append(") ").append(strColSymbol).append(" '").append(strColValue.toUpperCase()).append("' ").append(strBracketClose).append(strColLink).append(" ");
                            break;
                        } else if (liDoppelteLeistungsabzeichenStufe.contains(i)) {
                            sbSqlString.append(strBracketOpen).append("UPPER(").append("la").append(i).append(".").append(strColWhere).append(") ").append(strColSymbol).append(" '").append(strColValue.toUpperCase()).append("' ").append(strBracketClose).append(strColLink).append(" ");
                            break;
                        } else if (liDoppelteUntersuchungsart.contains(i)) {
                            sbSqlString.append(strBracketOpen).append("UPPER(").append("u").append(i).append(".").append(strColWhere).append(") ").append(strColSymbol).append(" '").append(strColValue.toUpperCase()).append("' ").append(strBracketClose).append(strColLink).append(" ");
                            break;
                        } else {
                            sbSqlString.append(strBracketOpen).append("UPPER(").append(strColWhere).append(") ").append(strColSymbol).append(" '").append(strColValue.toUpperCase()).append("' ").append(strBracketClose).append(strColLink).append(" ");
                            break;
                        }
                    //default bei byte, tinyint, bigint, int
                    default:
                        sbSqlString.append(strBracketOpen).append(strColWhere).append(" ").append(strColSymbol).append(" '").append(strColValue).append("' ").append(strBracketClose).append(strColLink).append(" ");
                        break;
                }
            } else if (strColWhere.equals("Alter")) {
                sbSqlString.append(strBracketOpen).append("(DATEDIFF(YY, geburtsdatum, GETDATE()) - CASE WHEN DATEADD(YY, DATEDIFF(YY,geburtsdatum, GETDATE()), geburtsdatum) > GETDATE() THEN 1 ELSE 0 END ) ").append(strColSymbol).append(" '").append(strColValue).append("' ").append(strBracketClose).append(strColLink).append(" ");
            } else if (strColWhere.equals("Status")) {
                sbSqlString.append(strBracketOpen).append(strColValue).append(" ").append(strColSymbol).append(" '1' ").append(strBracketClose).append(strColLink).append(" ");
            }
        }
        int intIndex = -1;

        if (!strColLink.isEmpty()) {
            intIndex = sbSqlString.lastIndexOf(strColLink);
        }

        if (intIndex != -1) {
            sbSqlString.substring(0, intIndex);
            sbSqlString.append(" ");
        }
        sbSqlString.append(" )");
        System.out.println("SQL String: " + sbSqlString);
        if (intBereichnr == -2) {
        } else if (intAbschnittnr == -2) {
            sbSqlString.append(" AND (fw.Bereich_Nr = ").append(intBereichnr).append(")");
        } else if (strFubwehr.equals("-2")) {
            sbSqlString.append(" AND (fw.abschnitt_instanznummer = ").append(intAbschnittnr).append(")");
        } else {
            sbSqlString.append(" AND (m.instanznummer = '").append(strFubwehr).append("')");
        }
        System.out.println("SQL String: " + sbSqlString);
        StringBuilder sbHtml = createDynamicReportGeneratorOutput(sbSqlString.toString(), strSelectedCols);
        return sbHtml;
    }

    /**
     * Generiert aus einem SQL Statement einen Report in Form einer HTML Tabelle
     *
     * @param sqlString
     * @param strSelectedCols
     * @return
     * @throws Exception
     */
    public StringBuilder createDynamicReportGeneratorOutput(String sqlString, String[] strSelectedCols) throws Exception {
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();
        StringBuilder sbHtml = new StringBuilder("");
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        ResultSet rs = stat.executeQuery(sqlString);

        sbHtml.append("<table class='sortable ui celled table' id='dyn_table'><thead><tr>");

        for (String str : strSelectedCols) {
            sbHtml.append("<th data-content='nach ").append(str).append(" sortieren'>");

            if (str.equals("Staatsbuergerschaft")) {
                sbHtml.append("Staatsbürgerschaft");
            } else if (boAnrede == true && str.equals("Geschlecht")) {
                sbHtml.append("Anrede");
            } else if (str.equals("Standesbuchnummer")) {
                sbHtml.append("STB");
            } else if (str.equals("Dienstgrad")) {
                sbHtml.append("DGR");
            } else {
                sbHtml.append(str);
            }

            sbHtml.append("</th>");
        }
        sbHtml.append("</tr></thead><tbody>");

        while (rs.next()) {
            String strString;
            Date dateDate;
            Long loLong;
            Byte byByte;
            int intInt;
            BigDecimal bdBigDecimal;

            sbHtml.append("<tr>");

            for (String str : strSelectedCols) {
                if (str.equals("Instanzname")) {
                    System.out.println("do smth");
                }
                for (Map.Entry pair : haNamesTypes.entrySet()) {
                    if (pair.getKey().toString().toUpperCase().equals(str.toUpperCase())) {
                        String strValue = pair.getValue().toString();

                        OUTER:
                        switch (strValue) {
                            case "bit":
                                boolean boJugend = rs.getBoolean("Jugend");
                                boolean boAktiv = rs.getBoolean("Aktiv");
                                boolean boReserve = rs.getBoolean("Reserve");
                                boolean boAbgemeldet = rs.getBoolean("Abgemeldet");
                                boolean boEhrenmitglied = rs.getBoolean("Ehrenmitglied");
                                sbHtml.append("<td>");

                                if (boJugend) {
                                    sbHtml.append("Jugend");
                                } else if (boAktiv) {
                                    sbHtml.append("Aktiv");
                                } else if (boReserve) {
                                    sbHtml.append("Reserve");
                                } else if (boAbgemeldet) {
                                    sbHtml.append("Abgemeldet");
                                } else if (boEhrenmitglied) {
                                    sbHtml.append("Ehrenmitglied");
                                }

                                sbHtml.append("</td>");
                                break;
                            case "datetime":
                                dateDate = rs.getDate(str);
                                sbHtml.append("<td>");
                                if (dateDate == null) {
                                    sbHtml.append("Unbekannt");
                                } else {
                                    sbHtml.append(sdf.format(dateDate));
                                }
                                sbHtml.append("</td>");
                                break;
                            case "varchar":
                                strString = rs.getString(str);
                                if (strString.equals("") && (pair.getKey().toString().equals("titel") || pair.getKey().toString().equals("amtstitel") || pair.getKey().toString().equals("beruf"))) {
                                    strString = "-";
                                } else if (strString.equals("")) {
                                    strString = "";
                                }

                                if (boAnrede == true) {
                                    switch (strString) {
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
                                } else {
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
                                if (str.equals("Vordienstzeit")) {
                                    bdBigDecimal = rs.getBigDecimal("vd_zeit");
                                } else {
                                    bdBigDecimal = rs.getBigDecimal(str);
                                }
                                sbHtml.append("<td>");
                                sbHtml.append(bdBigDecimal);
                                sbHtml.append("</td>");
                                break;
                            case "default-alter":
                                int intAlter = -1;
                                try {
                                    intAlter = Integer.parseInt(rs.getString("Lebensalter"));
                                } catch (NumberFormatException e) {

                                }
                                sbHtml.append("<td>");
                                if (intAlter < 0) {
                                    sbHtml.append("Unbekannt");
                                } else {
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

    /**
     * Definiert welche Spalten welchen Datentyp in der Datenbank haben
     */
    public void initializeDBValuesWithDBTypes() {
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
        haNamesTypes.put("status", "bit");
        haNamesTypes.put("vordienstzeit", "big decimal");
        haNamesTypes.put("titel", "varchar");
        haNamesTypes.put("instanzname", "varchar");

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
        haNamesTypes.put("bezeichnung", "varchar");

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

    public String getDBTypeForValue(String strValue) {
        String strType = "";

        for (Map.Entry e : haNamesTypes.entrySet()) {
            if (e.getKey().toString().toUpperCase().equals(strValue.toUpperCase())) {
                strType = e.getValue().toString();
            }
        }
        return strType;
    }

    /**
     * Gibt die Rangfolge zum Sortieren in Javascript aus Wird nur aufgerufen,
     * wenn sich etwas geändert hat
     *
     * @throws SQLException
     * @throws Exception
     */
    public void getRangfolge() throws SQLException, Exception {
        LinkedList<Mitglied> liMitglieder = new LinkedList<>();

        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String str = "SELECT [kurzbezeichnung] 'Bezeichnung' "
                + "      ,[langbezeichnung] "
                + "      ,[rangfolge]  "
                + "      ,[vorschlagen] "
                + "      ,[ehrendienstgrad] "
                + "      ,[id_dienstgrade] "
                + "  FROM [FDISK].[dbo].[stmkdienstgrade] "
                + "  ORDER BY rangfolge DESC";

        ResultSet rs = stat.executeQuery(str);

        String strSTB;
        int count = 0;
        while (rs.next()) {
            System.out.println(" .replace(/ " + rs.getString("Bezeichnung").toUpperCase() + " /i," + count + ")");
            count++;
        }

        connPool.releaseConnection(conn);
    }

}
