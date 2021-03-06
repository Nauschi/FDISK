/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import BL.BL;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Yvonne
 */
public class Einsatzbericht implements Serializable {

    private int intId_StmkEinsatzberichte;
    private int intInstanznummer;
    private String strName;
    private String strEinsatzart;
    private String strNummer;
    private Date dateUhrzeit_Alarmierung;
    private Date dateUhrzeit_Rueckkehr;
    private String strStrasse;
    private String strNummerAdr;
    private String strStiege;
    private String strPlz;
    private String strOrt;
    private int intStandesbuchnummer;
    private String strVorname;
    private String strZuname;
    private String strMeldung;
    private String strFehlalarm;
    private int intAnzahl;
    private String strTaetigkeit;
    private BL bl = new BL();

    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm");

    public Einsatzbericht(int intId_StmkEinsatzberichte, int intInstanznummer, String strName, String strEinsatzart, String strNummer, Date dateUhrzeit_Alarmierung, Date dateUhrzeit_Rueckkehr, String strStrasse, String strNummerAdr, String strStiege, String strPlz, String strOrt, int intStandesbuchnummer, String strVorname, String strZuname, String strMeldung, String strFehlalarm, int intAnzahl, String strTaetigkeit) throws ClassNotFoundException {
        this.intId_StmkEinsatzberichte = intId_StmkEinsatzberichte;
        this.intInstanznummer = intInstanznummer;
        this.strName = strName;
        this.strEinsatzart = strEinsatzart;
        this.strNummer = strNummer;
        this.dateUhrzeit_Alarmierung = dateUhrzeit_Alarmierung;
        this.dateUhrzeit_Rueckkehr = dateUhrzeit_Rueckkehr;
        this.strStrasse = strStrasse;
        this.strNummerAdr = strNummerAdr;
        this.strStiege = strStiege;
        this.strPlz = strPlz;
        this.strOrt = strOrt;
        this.intStandesbuchnummer = intStandesbuchnummer;
        this.strVorname = strVorname;
        this.strZuname = strZuname;
        this.strMeldung = strMeldung;
        this.strFehlalarm = strFehlalarm;
        this.intAnzahl = intAnzahl;
        this.strTaetigkeit = strTaetigkeit;

    }

    public String getStrTaetigkeit() {
        return strTaetigkeit;
    }

    public void setStrTaetigkeit(String strTaetigkeit) {
        this.strTaetigkeit = strTaetigkeit;
    }

    public int getIntAnzahl() {
        return intAnzahl;
    }

    public void setIntAnzahl(int intAnzahl) {
        this.intAnzahl = intAnzahl;
    }

    public int getIntId_StmkEinsatzberichte() {
        return intId_StmkEinsatzberichte;
    }

    public void setIntId_StmkEinsatzberichte(int intId_StmkEinsatzberichte) {
        this.intId_StmkEinsatzberichte = intId_StmkEinsatzberichte;
    }

    public int getIntInstanznummer() {
        return intInstanznummer;
    }

    public void setIntInstanznummer(int intInstanznummer) {
        this.intInstanznummer = intInstanznummer;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrEinsatzart() {
        return strEinsatzart;
    }

    public void setStrEinsatzart(String strEinsatzart) {
        this.strEinsatzart = strEinsatzart;
    }

    public String getStrNummer() {
        return strNummer;
    }

    public void setStrNummer(String strNummer) {
        this.strNummer = strNummer;
    }

    public Date getDateUhrzeit_Alarmierung() {
        return dateUhrzeit_Alarmierung;
    }

    public void setDateUhrzeit_Alarmierung(Date dateUhrzeit_Alarmierung) {
        this.dateUhrzeit_Alarmierung = dateUhrzeit_Alarmierung;
    }

    public Date getDateUhrzeit_Rueckkehr() {
        return dateUhrzeit_Rueckkehr;
    }

    public void setDateUhrzeit_Rueckkehr(Date dateUhrzeit_Rueckkehr) {
        this.dateUhrzeit_Rueckkehr = dateUhrzeit_Rueckkehr;
    }

    public String getStrStrasse() {
        return strStrasse;
    }

    public void setStrStrasse(String strStrasse) {
        this.strStrasse = strStrasse;
    }

    public String getStrNummerAdr() {
        return strNummerAdr;
    }

    public void setStrNummerAdr(String strNummerAdr) {
        this.strNummerAdr = strNummerAdr;
    }

    public String getStrStiege() {
        return strStiege;
    }

    public void setStrStiege(String strStiege) {
        this.strStiege = strStiege;
    }

    public String getStrPlz() {
        return strPlz;
    }

    public void setStrPlz(String strPlz) {
        this.strPlz = strPlz;
    }

    public String getStrOrt() {
        return strOrt;
    }

    public void setStrOrt(String strOrt) {
        this.strOrt = strOrt;
    }

    public int getIntStandesbuchnummer() {
        return intStandesbuchnummer;
    }

    public void setIntStandesbuchnummer(int intStandesbuchnummer) {
        this.intStandesbuchnummer = intStandesbuchnummer;
    }

    public String getStrVorname() {
        return strVorname;
    }

    public void setStrVorname(String strVorname) {
        this.strVorname = strVorname;
    }

    public String getStrZuname() {
        return strZuname;
    }

    public void setStrZuname(String strZuname) {
        this.strZuname = strZuname;
    }

    public String getStrMeldung() {
        return strMeldung;
    }

    public void setStrMeldung(String strMeldung) {
        this.strMeldung = strMeldung;
    }

    public String getStrFehlalarm() {
        return strFehlalarm;
    }

    public void setStrFehlalarm(String strFehlalarm) {
        this.strFehlalarm = strFehlalarm;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + this.intId_StmkEinsatzberichte;
        hash = 11 * hash + this.intInstanznummer;
        hash = 11 * hash + Objects.hashCode(this.strName);
        hash = 11 * hash + Objects.hashCode(this.strEinsatzart);
        hash = 11 * hash + Objects.hashCode(this.strNummer);
        hash = 11 * hash + Objects.hashCode(this.dateUhrzeit_Alarmierung);
        hash = 11 * hash + Objects.hashCode(this.dateUhrzeit_Rueckkehr);
        hash = 11 * hash + Objects.hashCode(this.strStrasse);
        hash = 11 * hash + Objects.hashCode(this.strNummerAdr);
        hash = 11 * hash + Objects.hashCode(this.strStiege);
        hash = 11 * hash + Objects.hashCode(this.strPlz);
        hash = 11 * hash + Objects.hashCode(this.strOrt);
        hash = 11 * hash + this.intStandesbuchnummer;
        hash = 11 * hash + Objects.hashCode(this.strVorname);
        hash = 11 * hash + Objects.hashCode(this.strZuname);
        hash = 11 * hash + Objects.hashCode(this.strMeldung);
        hash = 11 * hash + Objects.hashCode(this.strFehlalarm);
        hash = 11 * hash + this.intAnzahl;
        hash = 11 * hash + Objects.hashCode(this.strTaetigkeit);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Einsatzbericht other = (Einsatzbericht) obj;
        if (this.intId_StmkEinsatzberichte != other.intId_StmkEinsatzberichte) {
            return false;
        }
        if (this.intInstanznummer != other.intInstanznummer) {
            return false;
        }
        if (!Objects.equals(this.strName, other.strName)) {
            return false;
        }
        if (!Objects.equals(this.strEinsatzart, other.strEinsatzart)) {
            return false;
        }
        if (!Objects.equals(this.strNummer, other.strNummer)) {
            return false;
        }
        if (!Objects.equals(this.dateUhrzeit_Alarmierung, other.dateUhrzeit_Alarmierung)) {
            return false;
        }
        if (!Objects.equals(this.dateUhrzeit_Rueckkehr, other.dateUhrzeit_Rueckkehr)) {
            return false;
        }
        if (!Objects.equals(this.strStrasse, other.strStrasse)) {
            return false;
        }
        if (!Objects.equals(this.strNummerAdr, other.strNummerAdr)) {
            return false;
        }
        if (!Objects.equals(this.strStiege, other.strStiege)) {
            return false;
        }
        if (!Objects.equals(this.strPlz, other.strPlz)) {
            return false;
        }
        if (!Objects.equals(this.strOrt, other.strOrt)) {
            return false;
        }
        if (this.intStandesbuchnummer != other.intStandesbuchnummer) {
            return false;
        }
        if (!Objects.equals(this.strVorname, other.strVorname)) {
            return false;
        }
        if (!Objects.equals(this.strZuname, other.strZuname)) {
            return false;
        }
        if (!Objects.equals(this.strMeldung, other.strMeldung)) {
            return false;
        }
        if (!Objects.equals(this.strFehlalarm, other.strFehlalarm)) {
            return false;
        }
        if (!Objects.equals(this.intAnzahl, other.intAnzahl)) {
            return false;
        }
        if (!Objects.equals(this.strFehlalarm, other.strFehlalarm)) {
            return false;
        }
        if (!Objects.equals(this.strTaetigkeit, other.strTaetigkeit)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {

        if (strEinsatzart == null) {
            strEinsatzart = "";
        }
        if (strNummer == null) {
            strNummer = "";
        }
        if (strStrasse == null) {
            strStrasse = "";
        }
        if (strNummerAdr == null) {
            strNummerAdr = "";
        }
        if (strStiege == null) {
            strStiege = "";
        }
        if (strPlz == null) {
            strPlz = "";
        }
        if (strOrt == null) {
            strOrt = "";
        }
        if (strVorname == null) {
            strVorname = "";
        }
        if (strZuname == null) {
            strZuname = "";
        }
        if (strFehlalarm == null) {
            strFehlalarm = "";
        }
        if (strTaetigkeit == null) {
            strTaetigkeit = "";
        }

        strEinsatzart = bl.formatiereAusgabe(strEinsatzart);
        strTaetigkeit = strTaetigkeit.replace(System.getProperty("line.separator"), "");

        String strHtml = "<tr><td>"
                + strEinsatzart + "</td><td>"
                + strNummer + "</td><td>"
                + sdf.format(dateUhrzeit_Alarmierung) + "</td><td>"
                + sdf.format(dateUhrzeit_Rueckkehr) + "</td><td>"
                + strStrasse + " " + strNummerAdr + " " + strStiege + " " + strPlz + " " + strOrt + "</td><td>"
                + intAnzahl + "</td><td>"
                + strFehlalarm + "</td><td>"
                + strTaetigkeit + "</td></tr>";

        return strHtml;
    }

}
