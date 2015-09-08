/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

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

    /**
     * Gibt eine LinkedList mit allen Mitarbeitern, aller Feuerwehren in ganz
     * Steiermark, zurück.
     *
     * @return LinkedList
     * @throws IOException
     * @see Mitglied
     * @see LinkedList
     */
    public LinkedList<Mitglied> getEinfacheMitgliederliste() throws Exception
    {
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
     * Gibt alle relevanten Daten (Fahrzeugtype, Kennzeichen, Baujahr etc...)
     * von jedem Fahrzeug als LinkedList zurück.
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
     * Sucht die PersId für einen bestimmten User
     *
     * @param liLoginMitglied
     * @throws Exception
     */
    public void joinUserIdUndPersId(LinkedList<LoginMitglied> liLoginMitglied) throws Exception
    {
        String strLoginMitgliedVorname = null;
        String strLoginMitgliedNachname = null;
        String strVorname = null;
        String strNachname = null;
        int intPersId = -1;
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();
        for (LoginMitglied loginMitglied : liLoginMitglied)
        {
            strLoginMitgliedVorname = loginMitglied.getStrVorname();
            strLoginMitgliedNachname = loginMitglied.getStrNachname();
        }

        String sqlString = "SELECT id_personen \"PersID\""
                + " ,vorname \"Vorname\""
                + " ,zuname \"Zuname\""
                + " FROM FDISK.dbo.stmkmitglieder"
                + " WHERE UPPER(Vorname) = UPPER('" + strLoginMitgliedVorname + "')"
                + " AND UPPER(zuname) = UPPER('" + strLoginMitgliedNachname + "')";
        ResultSet rs = stat.executeQuery(sqlString);

        while (rs.next())
        {
            intPersId = rs.getInt("PersID");
            strVorname = rs.getString("Vorname");
            strNachname = rs.getString("Zuname");

            System.out.println("Datensatz mit PersId: ");
            System.out.println(intPersId + " - " + strNachname + " - " + strVorname + "\n");
        }
        connPool.releaseConnection(conn);
    }

    /**
     * !!!Nicht fertig!!!! Sucht Vorname, Nachname und Titel zu einer bestimmten
     * UserId
     *
     * @param intId_User
     * @return
     * @throws Exception
     */
    public LinkedList<LoginMitglied> login(int intId_User) throws Exception
    {
        LinkedList<LoginMitglied> liLoginMitglied = new LinkedList<>();
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "SELECT IDUser \"IDUser\""
                + " ,Vorname \"Vorname\""
                + " ,Nachname \"Nachname\""
                + " FROM FDISK.dbo.tbl_login_benutzerdetail"
                + " WHERE IDUser = " + intId_User;

        ResultSet rs = stat.executeQuery(sqlString);

        String strVorname;
        String strNachname;
        String strTitel = null;
        LoginMitglied loginMitglied = null;

        /**
         * ToDo: Wenn Titel dabeistehen aka größter scheiß überhaupt Alle de in
         * stmkmitglieder stehen abfragen?
         */
        while (rs.next())
        {

            strVorname = rs.getString("Vorname");
            strNachname = rs.getString("Nachname");
            int intLaengeVorname = -1;
            int intLaengeNachname = -1;

            if (strVorname != null)
            {
                strVorname = strVorname.trim();
                intLaengeVorname = strVorname.replaceAll("[^ ]", "").length();
            }
            if (strNachname != null)
            {
                strNachname = strNachname.trim();
                intLaengeNachname = strNachname.replaceAll("[^ ]", "").length();
            }

            //Wenn nur der Nachname angegeben ist (19119)
            if (strNachname != null && strVorname == null && intLaengeNachname == 0)
            {
                loginMitglied = new LoginMitglied(intId_User, strVorname, strNachname, strTitel);
            } //Wenn Vorname = null ohne Titel
            else if (strNachname != null && strVorname == null && intLaengeNachname == 1)
            {

                String[] strTeile = strNachname.split(" ");
                String strTeil1 = strTeile[0];
                String strTeil2 = strTeile[1];

                //Wenn das erste Wort in Nachname groß geschrieben ist => Teil1 = Nachname
                if (strTeil1.toUpperCase().equals(strTeil1) && !strTeil2.toUpperCase().equals(strTeil2))
                {
                    loginMitglied = new LoginMitglied(intId_User, strTeil2, strTeil1, strTitel);
                } //Wenn das zweite Wort in Nachname groß geschrieben ist => Teil2 = Nachname
                else if (!strTeil1.toUpperCase().equals(strTeil1) && strTeil2.toUpperCase().equals(strTeil2))
                {
                    loginMitglied = new LoginMitglied(intId_User, strTeil1, strTeil2, strTitel);

                } //Wenn beide Wörter klein geschrieben sind => Teil1 = Nachname
                else if (!strTeil1.toUpperCase().equals(strTeil1) && !strTeil2.toUpperCase().equals(strTeil2))
                {
                    loginMitglied = new LoginMitglied(intId_User, strTeil2, strTeil1, strTitel);
                }
            } //Bei optimalem Eintrag ohne Titel
            else if (strNachname != null && strVorname != null && intLaengeVorname == 0 && intLaengeNachname == 0)
            {
                loginMitglied = new LoginMitglied(intId_User, strVorname, strNachname, strTitel);
            } //Wenn Fa. davor steht und Nachname = null z.B. Fa. Center Communication Systems GmbH (3191)
            //Familie wird als Vorname verwendet
            else if (strNachname == null && strVorname != null && strVorname.contains("Fa."))
            {
                String[] strTeile = strVorname.split("\\.");
                String strTeilNachname = strTeile[1].trim();
                loginMitglied = new LoginMitglied(intId_User, "Familie", strTeilNachname, strTitel);
            } //Wenn Nachname = null und in Vorname nur ein Wort steht (3185)
            else if (strNachname == null && intLaengeVorname == 0)
            {
                strNachname = strVorname;
                loginMitglied = new LoginMitglied(intId_User, null, strNachname, strTitel);
            } //Wenn Nachname = null ohne Titel
            else if (strNachname == null && intLaengeVorname == 1)
            {
                String[] strTeile = strVorname.split(" ");
                String strTeil1 = strTeile[0];
                String strTeil2 = strTeile[1];

                //Wenn das erste Wort in Vorname groß geschrieben ist => Teil1 = Nachname
                if (strTeil1.toUpperCase().equals(strTeil1) && !strTeil2.toUpperCase().equals(strTeil2))
                {
                    loginMitglied = new LoginMitglied(intId_User, strTeil2, strTeil1, strTitel);
                } //Wenn das zweite Wort in Vorname groß geschrieben ist => Teil2 = Nachname
                else if (!strTeil1.toUpperCase().equals(strTeil1) && strTeil2.toUpperCase().equals(strTeil2))
                {
                    loginMitglied = new LoginMitglied(intId_User, strTeil1, strTeil2, strTitel);

                } //Wenn beide Wörter klein geschrieben sind => Teil1 = Nachname
                else if (!strTeil1.toUpperCase().equals(strTeil1) && !strTeil2.toUpperCase().equals(strTeil2))
                {
                    loginMitglied = new LoginMitglied(intId_User, strTeil2, strTeil1, strTitel);
                }
            } //Wenn in Vorname ein Titel dabei steht
            else if (strNachname != null && intLaengeVorname == 1)
            {
                System.out.println("Woher soll i wissen ob da Titel als erstes steht? Evtl. schauen ob ein Punkt nach dem Titel steht");
            }

            if (loginMitglied != null)
            {
                liLoginMitglied.add(loginMitglied);
            } else
            {
                System.out.println("Fehler");
            }

        }
        connPool.releaseConnection(conn);
        return liLoginMitglied;
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
            if (typ.toUpperCase().equals("FUNKTIONSBEZEICHNUNG"))
            {
                typ = "BEZEICHNUNG";
            } else if (typ.toUpperCase().equals("FUNKTIONSINSTANZ"))
            {
                typ = "ID_INSTANZTYPEN";
            } else if (typ.toUpperCase().equals("FUNKTION VON"))
            {
                typ = "DATUM_VON";
            } else
            {
                typ = "DATUM_BIS";
            }
            getFilterFuerFunktion(typ);
        } else if (typ.toUpperCase().equals("ALTER"))
        {

            getFilterFuerAlter(typ);
        } else if (typ.toUpperCase().equals("ERREICHBARKEITSART") || typ.toUpperCase().equals("ERREICHBARKEIT"))
        {
            if (typ.toUpperCase().equals("ERREICHBARKEIT"))
            {
                typ = "CODE";
            }
            getFilterFuerErreichbarkeit(typ);
        } else
        {
            if(typ.toUpperCase().equals("ISCO-BERUF"))
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
                strFilter = "unbekannt";
                continue;
            }
            strFilter = rs.getString("Typ");

            if (strFilter.equals("") || strFilter.equals(" "))
            {
                strFilter = "unbekannt";
            }
            if(typ.toUpperCase().equals("BERUF"))
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
                strFilter = "unbekannt";
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
                strFilter = "unbekannt";
                continue;
            }
            strFilter = rs.getString("Typ");

            if (strFilter.equals("") || strFilter.equals(" "))
            {
                strFilter = "unbekannt";
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
                strFilter = "unbekannt";
                continue;
            }
            strFilter = rs.getString("Typ");

            if (strFilter.equals("") || strFilter.equals(" "))
            {
                strFilter = "unbekannt";
            }
            liFilter.add(strFilter);
        }

        if (typ.toUpperCase().equals("BEZEICHNUNG"))
        {
            typ = "FUNKTIONSBEZEICHNUNG";
        } else if (typ.toUpperCase().equals("ID_INSTANZTYPEN"))
        {
            typ = "FUNKTIONSINSTANZ";
        } else if (typ.toUpperCase().equals("DATUM_VON"))
        {
            typ = "FUNKTION VON";
        } else if (typ.toUpperCase().equals("DATUM_BIS"))
        {
            typ = "FUNKTION BIS";
        }

        hmFilter.put(typ, liFilter);
        connPool.releaseConnection(conn);

        for (Map.Entry e : hmFilter.entrySet())
        {
            System.out.println(e.getKey() + "---" + e.getValue());
        }

        return hmFilter;
    }

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
                    strFilter = "unbekannt";
                } else
                {
                    strFilter = intAlter + "";
                }
            } else
            {
                strFilter = "unbekannt";
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

        if (liFilter.contains("unbekannt"))
        {
            liFilter.remove("unbekannt");
            liFilter.addFirst("unbekannt");
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
                strFilter = "unbekannt";
                continue;
            }
            strFilter = rs.getString("Typ");

            if (strFilter.equals("") || strFilter.equals(" ") || strFilter.equals("-") || strFilter.equals("--"))
            {
                strFilter = "unbekannt";
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
            theInstance.getMethodeFuerTyp("isco-beruf");
            // hm = theInstance.getFilterFuerGruppe("gruppe");

        } catch (Exception ex)
        {
            Logger.getLogger(DB_Access.class.getName()).log(Level.SEVERE, null, ex);
        }
//        for (Map.Entry e : hm.entrySet())
//        {
//            System.out.println(e.getKey() + "---" + e.getValue());
//
//        }
    }
}
