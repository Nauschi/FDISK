package Beans;

import Database.DB_Access;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Yvonne
 */
public class Kurs
{

    private int intIdKurse;
    private int intIdKursart;
    private int intLehrgangsnummer;
    private String strKursbezeichnung;
    private String strKurskurzbezeichnung;
    private Date dateDatum;
    private int intIdInstanzenVeranstalter;
    private int intIdInstanzenDurchfuehrend;
    private String strKursstatus;
    
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm");
    private DB_Access theInstance;

    public Kurs(int intIdKurse, int intIdKursart, int intLehrgangsnummer, String strKursbezeichnung, String strKurskurzbezeichnung, Date dateDatum, int intIdInstanzenVeranstalter, int intIdInstanzenDurchfuehrend, String strKursstatus) throws ClassNotFoundException
    {
        this.intIdKurse = intIdKurse;
        this.intIdKursart = intIdKursart;
        this.intLehrgangsnummer = intLehrgangsnummer;
        this.strKursbezeichnung = strKursbezeichnung;
        this.strKurskurzbezeichnung = strKurskurzbezeichnung;
        this.dateDatum = dateDatum;
        this.intIdInstanzenVeranstalter = intIdInstanzenVeranstalter;
        this.intIdInstanzenDurchfuehrend = intIdInstanzenDurchfuehrend;
        this.strKursstatus = strKursstatus;
        theInstance = DB_Access.getInstance();
    }

    public int getIntIdKurse()
    {
        return intIdKurse;
    }

    public void setIntIdKurse(int intIdKurse)
    {
        this.intIdKurse = intIdKurse;
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

    public Date getDateDatum()
    {
        return dateDatum;
    }

    public void setDateDatum(Date dateDatum)
    {
        this.dateDatum = dateDatum;
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
    public int hashCode()
    {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Kurs other = (Kurs) obj;
        if (this.intIdKurse != other.intIdKurse)
        {
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

        strKursbezeichnung = theInstance.formatiereAusgabe(strKursbezeichnung);
        strKursstatus = theInstance.formatiereAusgabe(strKursstatus);

        String strHtml = "<tr><td>"
                + strKursbezeichnung + "</td><td>"
                + strKursstatus + "</td><td>"
                + sdf.format(dateDatum) + "</td><td>"
                + "</td></tr>";

        return strHtml;
    }

}
