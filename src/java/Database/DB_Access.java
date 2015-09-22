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
import Beans.LoginMitglied;
import Beans.Mitglied;
import Beans.MitgliedsAdresse;
import Beans.MitgliedsDienstzeit;
import Beans.MitgliedsErreichbarkeit;
import Beans.MitgliedsGeburtstag;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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

    public LinkedList<Berechtigung> getBerechtigungen(int intUserID) throws Exception{
        LinkedList<Berechtigung> liBerechtigungen = new LinkedList<>();
        LinkedList<LoginMitglied> liLoginBerechtigung = new LinkedList<>();
        liLoginBerechtigung = getLoginBerechtigung(intUserID);
        String fubwehr = getFubwehrForUserID(intUserID);
        String feuerwehrname = getNameFuerFubwehr(fubwehr);
        
        for (LoginMitglied loginMitglied : liLoginBerechtigung) {
            String bezeichnung = loginMitglied.getStrGruppe();
            
            String strBerechtigung = bezeichnung + " - " + feuerwehrname;
            Berechtigung berechtigung = new Berechtigung(strBerechtigung, loginMitglied.getIntIDGruppe());
            liBerechtigungen.add(berechtigung);
        }
        return liBerechtigungen;
    }
    
    /**
     * Gibt die UserID für den jeweiligen Username zurück
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
     * Gibt eine LinkedList mit allen Berechtigungen eines Mitglieds zurück.
     * Enthalten sind die UserID, die Fubwehr, die GruppenID und die Gruppenbezeichnung
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
     * @param strFubwehr
     * @return
     * @throws SQLException
     * @throws Exception 
     */
    public int getAbschnittsnummerForFubwehr(String strFubwehr) throws SQLException, Exception
    {
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();
        String sqlString = "  SELECT DISTINCT f.Abschnitt_Instanznummer \"Nummer\" "
                + "  FROM FDISK.dbo.stmkmitglieder s INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(s.instanznummer = f.instanznummer) "
                + "  WHERE s.instanznummer = '" + strFubwehr + "'";
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
     * @param strFubwehr
     * @return
     * @throws SQLException
     * @throws Exception 
     */
    public String getAbschnittsnameFuerFubwehr(String strFubwehr) throws SQLException, Exception
    {
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();
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
     * Gibt eine LinkedList mit allen Mitarbeitern (je nach Berechtigung eines Users)
     * zurück
     * @return LinkedList
     * @throws IOException
     * @see Mitglied
     * @see LinkedList
     */
    public LinkedList<Mitglied> getEinfacheMitgliederliste(int intUserID, int intGruppe) throws Exception
    {
        LinkedList<Mitglied> liMitglieder = new LinkedList<>();
        String strFubwehr = getFubwehrForUserID(intUserID) + "";
        String strBereich = strFubwehr.substring(0, 2);
        int intAbschnittsnummer = getAbschnittsnummerForFubwehr(strFubwehr);

        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "";

        switch (intGruppe)
        {
            case 1:
                sqlString = "SELECT TOP 1000 id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\" "
                        + "FROM FDISK.dbo.stmkmitglieder";
                break;
            case 5:
                sqlString = "SELECT TOP 1000 id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\" "
                        + "FROM FDISK.dbo.stmkmitglieder "
                        + "WHERE SUBSTRING(instanznummer, 0, 3) = '" + strBereich + "'";
                break;
            case 9:
                sqlString = "SELECT TOP 1000 id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\" "
                        + "FROM FDISK.dbo.stmkmitglieder "
                        + "WHERE instanznummer = '" + strFubwehr + "'";
                break;
            case 15:
                sqlString = "SELECT TOP 1000 id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\" "
                        + " FROM FDISK.dbo.stmkmitglieder s INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(s.instanznummer = f.instanznummer) "
                        + " WHERE f.abschnitt_instanznummer = " + intAbschnittsnummer;
                System.out.println(intAbschnittsnummer);
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
     * Gibt spezielle Informationen zu der Tätigkeit "Kursbesuch an der FWZS"
     * als LinkedList zurück. Diese Informationen sind zum Beispiel Anzahl der
     * Mitarbeiter in einem Kurs.
     *
     * @return LinkedList
     * @throws IOException
     * @see Kurs
     * @see LinkedList
     */
    public LinkedList<Kurs> getKursstatistik() throws Exception
    {
        LinkedList<Kurs> liKurse = new LinkedList<>();
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

            Kurs kurs = new Kurs(intId_kurse, intId_Kursarten, intLehrgangsnummer, strKursbezeichnung, dateDatum, intId_instanzen_veranstalter, intId_instanzen_durchfuehrend, strKursstatus, intAnzahlBesucher);
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
                + ",aufbaufirma \"Aufbaufirma\" "
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

            Fahrzeug fahrzeug = new Fahrzeug(strFahrzeugTyp, strKennzeichen, intBaujahr, strAufbaufirma, strTaktischeBezeichnung, intId_fahrzeuge, strBezeichnung, strFahrzeugmarke);
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

        String sqlString = "SELECT TOP 1000 sm.id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\","
                + " vorname \"Vorname\", zuname \"Zuname\", se.erreichbarkeitsart \"Erreichbarkeitsart\", se.code \"Code\","
                + " se.sichtbarkeit \"Sichtbarkeit\", se.id_erreichbarkeiten \"ID_erreichbarkeit\""
                + " FROM FDISK.dbo.stmkmitglieder sm INNER JOIN FDISK.dbo.stmkerreichbarkeiten se ON(sm.id_personen = se.id_personen)"
                + " ORDER BY se.id_personen;";
        ResultSet rs = stat.executeQuery(sqlString);
        int intId_erreichbarkeit;
        String strErreichbarkeitsart;
        String strSichtbarkeit;
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
            intId_erreichbarkeit = rs.getInt("ID_erreichbarkeit");
            strErreichbarkeitsart = rs.getString("Erreichbarkeitsart");
            strSichtbarkeit = rs.getString("Sichtbarkeit");
            strCode = rs.getString("Code");

            strSTB = rs.getString("STB");
            strDGR = rs.getString("DGR");
            strTitel = rs.getString("Titel");
            strVorname = rs.getString("Vorname");
            strZuname = rs.getString("Zuname");

            if (intPersID == intLetztePersID)
            {
                if (!liErreichbarkeiten.contains(new Erreichbarkeit(intId_erreichbarkeit, strErreichbarkeitsart, strSichtbarkeit, strCode, intPersID)))
                {
                    liErreichbarkeiten.add(new Erreichbarkeit(intId_erreichbarkeit, strErreichbarkeitsart, strSichtbarkeit, strCode, intPersID));
                }
            } else
            {
                if (liMitgliedsErreichbarkeiten.size() > 0)
                {
                    liMitgliedsErreichbarkeiten.getLast().setLiErreichbarkeiten(liErreichbarkeiten);
                    liErreichbarkeiten = new LinkedList<Erreichbarkeit>();
                }
                intLetztePersID = intPersID;
                liMitgliedsErreichbarkeiten.add(new MitgliedsErreichbarkeit(false, intPersID, strSTB, strTitel, strTitel, strVorname, strZuname));
                liErreichbarkeiten.add(new Erreichbarkeit(intId_erreichbarkeit, strErreichbarkeitsart, strSichtbarkeit, strCode, intPersID));
            }

        }
        return liMitgliedsErreichbarkeiten;
    }

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
            LinkedList<Berechtigung> lili = new LinkedList<>();
            lili = theInstance.getBerechtigungen(3494);
            for (Berechtigung berech : lili)
            {
                System.out.println(berech.getStrBerechtigung());
            }
//            LinkedList<Mitglied> li = theInstance.getEinfacheMitgliederliste(3566, 15);
//            for (Mitglied li1 : li)
//            {
//                System.out.println(li1.getStrVorname() + "-" + li1.getStrZuname());
//            }

        } catch (Exception ex)
        {
            Logger.getLogger(DB_Access.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
