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
import java.util.Date;
import java.util.LinkedList;
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

            liErreichbarkeiten.add(new Erreichbarkeit(intId_erreichbarkeit, strErreichbarkeitsart, strSichtbarkeit, strCode, intPersID));

        }
        sqlString = "SELECT TOP 1000 id_personen \"PersID\", standesbuchnummer \"STB\", dienstgrad \"DGR\", titel \"Titel\", vorname \"Vorname\", zuname \"Zuname\""
                + " FROM FDISK.dbo.stmkmitglieder"
                + " ORDER BY id_personen;";
        rs = stat.executeQuery(sqlString);
        LinkedList<Erreichbarkeit> liErreichbarkeitZuMitglied = new LinkedList<>();

        while (rs.next())
        {
            intPersID = rs.getInt("PersID");
            strSTB = rs.getString("STB");
            strDGR = rs.getString("DGR");
            strTitel = rs.getString("Titel");
            strVorname = rs.getString("Vorname");
            strZuname = rs.getString("Zuname");
            for (Erreichbarkeit erreichbarkeit : liErreichbarkeiten)
            {
                if (erreichbarkeit.getIntPersID() == intPersID)
                {
                    liErreichbarkeitZuMitglied.add(erreichbarkeit);
                }
            }
            liMitgliedsErreichbarkeiten.add(new MitgliedsErreichbarkeit(intPersID, strSTB, strDGR, strTitel, strVorname, strZuname, false, liErreichbarkeitZuMitglied, false));
        }
        return liMitgliedsErreichbarkeiten;
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

    public int getUserId(String strUsername, String strPasswort) throws Exception
    {
        int intUserId = -1; 
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();

        String sqlString = "SELECT IDUser \"IdUser\""
                + "  ,username \"Username\""
                + "  ,passwort \"Passwort\""
                + "  FROM FDISK.dbo.tbl_login_benutzer"
                + "  WHERE username = '"+strUsername+"' AND passwort = '"+strPasswort+"'";

        ResultSet rs = stat.executeQuery(sqlString);

        while (rs.next())
        {
            intUserId = rs.getInt("IdUser"); 
        }
        connPool.releaseConnection(conn);
      //  login(intUserId); 
        return intUserId; 
    }
    
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
                            + " WHERE UPPER(Vorname) = UPPER('"+strLoginMitgliedVorname+"')"
                            + " AND UPPER(zuname) = UPPER('"+strLoginMitgliedNachname+"')";
        ResultSet rs = stat.executeQuery(sqlString);

        while (rs.next())
        {
            intPersId = rs.getInt("PersID");
            strVorname = rs.getString("Vorname");
            strNachname = rs.getString("Zuname");
                

            System.out.println("Datensatz mit PersId: ");
            System.out.println(intPersId + " - " + strNachname  + " - " +strVorname + "\n");
        }
        connPool.releaseConnection(conn);
    }
    
    /**
     * !!!Nicht fertig!!!!
     * Sucht Vorname, Nachname und Titel zu einer bestimmten UserId
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
         * ToDo: 
         * Wenn Titel dabeistehen aka größter scheiß überhaupt
         * Alle de in stmkmitglieder stehen abfragen?
         */
        while (rs.next())
        {
            
            strVorname = rs.getString("Vorname");
            strNachname = rs.getString("Nachname");
            int intLaengeVorname = -1; 
            int intLaengeNachname = -1; 
            
            if(strVorname != null)
            {
                strVorname = strVorname.trim(); 
                intLaengeVorname = strVorname.replaceAll("[^ ]", "").length();
            }
            if(strNachname != null)
            {
                strNachname = strNachname.trim(); 
                intLaengeNachname = strNachname.replaceAll("[^ ]", "").length();
            }
          

            //Wenn nur der Nachname angegeben ist (19119)
            if(strNachname != null && strVorname == null && intLaengeNachname == 0)
            {
                loginMitglied = new LoginMitglied(intId_User, strVorname, strNachname, strTitel);
            }
            //Wenn Vorname = null ohne Titel
            else if(strNachname != null && strVorname == null && intLaengeNachname == 1)
            {
                
                String[] strTeile = strNachname.split(" ");
                String strTeil1 = strTeile[0];
                String strTeil2 = strTeile[1];

                //Wenn das erste Wort in Nachname groß geschrieben ist => Teil1 = Nachname
                if (strTeil1.toUpperCase().equals(strTeil1) && !strTeil2.toUpperCase().equals(strTeil2))
                {
                    loginMitglied = new LoginMitglied(intId_User, strTeil2, strTeil1, strTitel);
                } 
                //Wenn das zweite Wort in Nachname groß geschrieben ist => Teil2 = Nachname
                else if (!strTeil1.toUpperCase().equals(strTeil1) && strTeil2.toUpperCase().equals(strTeil2))
                {
                    loginMitglied = new LoginMitglied(intId_User, strTeil1, strTeil2, strTitel);

                } //Wenn beide Wörter klein geschrieben sind => Teil1 = Nachname
                else if (!strTeil1.toUpperCase().equals(strTeil1) && !strTeil2.toUpperCase().equals(strTeil2))
                {
                    loginMitglied = new LoginMitglied(intId_User, strTeil2, strTeil1, strTitel);
                }
            }
            
            //Bei optimalem Eintrag ohne Titel
            else if (strNachname != null && strVorname != null && intLaengeVorname == 0 && intLaengeNachname == 0)
            {
                loginMitglied = new LoginMitglied(intId_User, strVorname, strNachname, strTitel);
            } 
            //Wenn Fa. davor steht und Nachname = null z.B. Fa. Center Communication Systems GmbH (3191)
            //Familie wird als Vorname verwendet
            else if(strNachname == null &&  strVorname != null && strVorname.contains("Fa."))
            {
                String[] strTeile = strVorname.split("\\.");
                String strTeilNachname = strTeile[1].trim();
                loginMitglied = new LoginMitglied(intId_User, "Familie", strTeilNachname, strTitel);
            }
            //Wenn Nachname = null und in Vorname nur ein Wort steht (3185)
            else if(strNachname == null && intLaengeVorname == 0)
            {
                strNachname = strVorname; 
                loginMitglied = new LoginMitglied(intId_User, null, strNachname, strTitel);
            }
            //Wenn Nachname = null ohne Titel
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
            } 
            //Wenn in Vorname ein Titel dabei steht
            else if(strNachname != null && intLaengeVorname == 1)
            {
                System.out.println("Woher soll i wissen ob da Titel als erstes steht? Evtl. schauen ob ein Punkt nach dem Titel steht");
            }
            
            if(loginMitglied != null)
            {
                liLoginMitglied.add(loginMitglied);
            }
            else
            {
                System.out.println("Fehler");
            }
            
        }
        connPool.releaseConnection(conn);
        return liLoginMitglied;
    }

    public static void main(String[] args) throws Exception
    {
        try
        {
            theInstance = DB_Access.getInstance();
        } catch (ClassNotFoundException ex)
        {
            Logger.getLogger(DB_Access.class.getName()).log(Level.SEVERE, null, ex);
        }

        LinkedList<LoginMitglied> lili = new LinkedList<>();
        try
        {
            int userIdTest = theInstance.getUserId("41033002", "vo4905"); 
            lili = theInstance.login(userIdTest);
            theInstance.joinUserIdUndPersId(lili);
        } catch (Exception ex)
        {
            Logger.getLogger(DB_Access.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (LoginMitglied l : lili)
        {
            System.out.println("Datensatz mit UserId: ");
            System.out.print(l.getIntId_User() + " - ");
            System.out.print(l.getStrNachname() + " - ");
            System.out.print(l.getStrVorname() + " - ");
            System.out.print(l.getStrTitel() + "\n");
        }
    }
}
