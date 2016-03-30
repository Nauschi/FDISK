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
public class Kurstaetigkeit implements Serializable
{

    private int intIdBerichte;
    private int intTeilnehmer;
    private double doKm;
    private int intInstanznummer;
    private String strInstanzname;
    private String strTaetigkeitsart;
    private String strTaetigkeitsunterart;
    private String strNummer;
    private Date dateBegin;
    private Date dateEnde;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm");
    private BL bl = new BL(); 

    public Kurstaetigkeit(int intIdBerichte, int intTeilnehmer, double doKm, int intInstanznummer, String strInstanzname, String strTaetigkeitsart, String strTaetigkeitsunterart, String strNummer, Date dateBeginn, Date dateEnde) throws ClassNotFoundException
    {
        this.intIdBerichte = intIdBerichte;
        this.intTeilnehmer = intTeilnehmer;
        this.doKm = doKm;
        this.intInstanznummer = intInstanznummer;
        this.strInstanzname = strInstanzname;
        this.strTaetigkeitsart = strTaetigkeitsart;
        this.strTaetigkeitsunterart = strTaetigkeitsunterart;
        this.strNummer = strNummer;
        this.dateBegin = dateBeginn;
        this.dateEnde = dateEnde;
    }

    public int getIntIdBerichte()
    {
        return intIdBerichte;
    }

    public void setIntIdBerichte(int intIdBerichte)
    {
        this.intIdBerichte = intIdBerichte;
    }

    public int getIntTeilnehmer()
    {
        return intTeilnehmer;
    }

    public void setIntTeilnehmer(int intTeilnehmer)
    {
        this.intTeilnehmer = intTeilnehmer;
    }



    public double getDoKm()
    {
        return doKm;
    }

    public void setDoKm(double doKm)
    {
        this.doKm = doKm;
    }

    public int getIntInstanznummer()
    {
        return intInstanznummer;
    }

    public void setIntInstanznummer(int intInstanznummer)
    {
        this.intInstanznummer = intInstanznummer;
    }

    public String getStrInstanzname()
    {
        return strInstanzname;
    }

    public void setStrInstanzname(String strInstanzname)
    {
        this.strInstanzname = strInstanzname;
    }

    public String getStrTaetigkeitsart()
    {
        return strTaetigkeitsart;
    }

    public void setStrTaetigkeitsart(String strTaetigkeitsart)
    {
        this.strTaetigkeitsart = strTaetigkeitsart;
    }

    public String getStrTaetigkeitsunterart()
    {
        return strTaetigkeitsunterart;
    }

    public void setStrTaetigkeitsunterart(String strTaetigkeitsunterart)
    {
        this.strTaetigkeitsunterart = strTaetigkeitsunterart;
    }

    public String getStrNummer()
    {
        return strNummer;
    }

    public void setStrNummer(String intNummer)
    {
        this.strNummer = intNummer;
    }

    public Date getDateBegin()
    {
        return dateBegin;
    }

    public void setDateBegin(Date dateBegin)
    {
        this.dateBegin = dateBegin;
    }

    public Date getDateEnde()
    {
        return dateEnde;
    }

    public void setDateEnde(Date dateEnde)
    {
        this.dateEnde = dateEnde;
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
        final Kurstaetigkeit other = (Kurstaetigkeit) obj;
        if (this.intIdBerichte != other.intIdBerichte)
        {
            return false;
        }
        if (this.intTeilnehmer != other.intTeilnehmer)
        {
            return false;
        }
        if (this.doKm != other.doKm)
        {
            return false;
        }
        if (this.intInstanznummer != other.intInstanznummer)
        {
            return false;
        }
        if (!Objects.equals(this.strInstanzname, other.strInstanzname))
        {
            return false;
        }
        if (!Objects.equals(this.strTaetigkeitsart, other.strTaetigkeitsart))
        {
            return false;
        }
        if (!Objects.equals(this.strTaetigkeitsunterart, other.strTaetigkeitsunterart))
        {
            return false;
        }
        if (this.strNummer != other.strNummer)
        {
            return false;
        }
        if (!Objects.equals(this.dateBegin, other.dateBegin))
        {
            return false;
        }
        if (!Objects.equals(this.dateEnde, other.dateEnde))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        if (strTaetigkeitsart == null)
        {
            strTaetigkeitsart = "";
        }
        if (strTaetigkeitsunterart == null)
        {
            strTaetigkeitsunterart = "";
        }

        strTaetigkeitsart = bl.formatiereAusgabe(strTaetigkeitsart);
        strTaetigkeitsunterart = bl.formatiereAusgabe(strTaetigkeitsunterart);

        String strHtml = "<tr><td>"
                + intTeilnehmer + "</td><td>"
                + doKm + "</td><td>"
                + strTaetigkeitsart + "</td><td>"
                + strTaetigkeitsunterart + "</td><td>"
                + sdf.format(dateBegin) + "</td><td>"
                + sdf.format(dateEnde) + "</td>"
                + "</tr>";

        return strHtml;
    }

}
