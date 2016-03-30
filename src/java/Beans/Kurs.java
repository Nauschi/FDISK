package Beans;

import BL.BL;
import Database.DB_Access;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Yvonne
 */
public class Kurs implements Serializable
{

    
    private int intIdKursart;
    private int intLehrgangsnummer;
    private String strKursbezeichnung;
    private String strKurskurzbezeichnung;
    private int intIdInstanzenVeranstalter;
    private int intIdInstanzenDurchfuehrend;
    private String strKursstatus;
    private int intAnzahlTeilnehmer;
    private int intAnzahlTaetigkeiten;
    
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm");
    private BL bl = new BL(); 

    public Kurs(int intIdKursart, int intLehrgangsnummer, String strKursbezeichnung, String strKurskurzbezeichnung, int intIdInstanzenVeranstalter, int intIdInstanzenDurchfuehrend, String strKursstatus, int intAnzhahlTeilnehmer) throws ClassNotFoundException
    {
        this.intIdKursart = intIdKursart;
        this.intLehrgangsnummer = intLehrgangsnummer;
        this.strKursbezeichnung = strKursbezeichnung;
        this.strKurskurzbezeichnung = strKurskurzbezeichnung;
        this.intIdInstanzenVeranstalter = intIdInstanzenVeranstalter;
        this.intIdInstanzenDurchfuehrend = intIdInstanzenDurchfuehrend;
        this.strKursstatus = strKursstatus;
        this.intAnzahlTeilnehmer = intAnzhahlTeilnehmer;
    }

    public int getIntAnzahlTaetigkeiten() {
        return intAnzahlTaetigkeiten;
    }

    public void setIntAnzahlTaetigkeiten(int intAnzahlTaetigkeiten) {
        this.intAnzahlTaetigkeiten = intAnzahlTaetigkeiten;
    }
    
    public int getIntAnzahlTeilnehmer() {
        return intAnzahlTeilnehmer;
    }

    public void setIntAnzahlTeilnehmer(int intAnzahlTeilnehmer) {
        this.intAnzahlTeilnehmer = intAnzahlTeilnehmer;
    }
    

    public int getIntIdKursart()
    {
        return intIdKursart;
    }

    public void setIntIdKursart(int intIdKursart)
    {
        this.intIdKursart = intIdKursart;
    }

    public int getIntLehrgangsnummer()
    {
        return intLehrgangsnummer;
    }

    public void setIntLehrgangsnummer(int intLehrgangsnummer)
    {
        this.intLehrgangsnummer = intLehrgangsnummer;
    }

    public String getStrKursbezeichnung()
    {
        return strKursbezeichnung;
    }

    public void setStrKursbezeichnung(String strKursbezeichnung)
    {
        this.strKursbezeichnung = strKursbezeichnung;
    }

    public String getStrKurskurzbezeichnung()
    {
        return strKurskurzbezeichnung;
    }

    public void setStrKurskurzbezeichnung(String strKurskurzbezeichnung)
    {
        this.strKurskurzbezeichnung = strKurskurzbezeichnung;
    }

    public int getIntIdInstanzenVeranstalter()
    {
        return intIdInstanzenVeranstalter;
    }

    public void setIntIdInstanzenVeranstalter(int intIdInstanzenVeranstalter)
    {
        this.intIdInstanzenVeranstalter = intIdInstanzenVeranstalter;
    }

    public int getIntIdInstanzenDurchfuehrend()
    {
        return intIdInstanzenDurchfuehrend;
    }

    public void setIntIdInstanzenDurchfuehrend(int intIdInstanzenDurchfuehrend)
    {
        this.intIdInstanzenDurchfuehrend = intIdInstanzenDurchfuehrend;
    }

    public String getStrKursstatus()
    {
        return strKursstatus;
    }

    public void setStrKursstatus(String strKursstatus)
    {
        this.strKursstatus = strKursstatus;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.intIdKursart;
        hash = 53 * hash + this.intLehrgangsnummer;
        hash = 53 * hash + Objects.hashCode(this.strKursbezeichnung);
        hash = 53 * hash + Objects.hashCode(this.strKurskurzbezeichnung);
        hash = 53 * hash + this.intIdInstanzenVeranstalter;
        hash = 53 * hash + this.intIdInstanzenDurchfuehrend;
        hash = 53 * hash + Objects.hashCode(this.strKursstatus);
        hash = 53 * hash + this.intAnzahlTeilnehmer;
        hash = 53 * hash + this.intAnzahlTaetigkeiten;
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
        final Kurs other = (Kurs) obj;
        if (this.intIdKursart != other.intIdKursart) {
            return false;
        }
        if (this.intLehrgangsnummer != other.intLehrgangsnummer) {
            return false;
        }
        if (!Objects.equals(this.strKursbezeichnung, other.strKursbezeichnung)) {
            return false;
        }
        if (!Objects.equals(this.strKurskurzbezeichnung, other.strKurskurzbezeichnung)) {
            return false;
        }
        if (this.intIdInstanzenVeranstalter != other.intIdInstanzenVeranstalter) {
            return false;
        }
        if (this.intIdInstanzenDurchfuehrend != other.intIdInstanzenDurchfuehrend) {
            return false;
        }
        if (!Objects.equals(this.strKursstatus, other.strKursstatus)) {
            return false;
        }
        if (this.intAnzahlTeilnehmer != other.intAnzahlTeilnehmer) {
            return false;
        }
        if (this.intAnzahlTaetigkeiten != other.intAnzahlTaetigkeiten) {
            return false;
        }
        return true;
    }
    

    @Override
    public String toString()
    {

        if (strKursbezeichnung == null)
        {
            strKursbezeichnung = "";
        }
        if (strKursstatus == null)
        {
            strKursstatus = "";
        }

        strKursbezeichnung = bl.formatiereAusgabe(strKursbezeichnung);
        strKursstatus = bl.formatiereAusgabe(strKursstatus);

        String strHtml = "<tr><td>"
                + strKursbezeichnung + "</td><td>"
                + intAnzahlTeilnehmer + "</td></tr>";

        return strHtml;
    }

}
