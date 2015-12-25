package Beans;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Yvonne
 */
public class Kurs
{

    private int intIdBerichte;
    private int intTeilnehmer;
    private String strBezeichnung;
    private int intKm;
    private int intInstanznummer;
    private String strInstanzname;
    private String strTaetigkeitsart;
    private String strTaetigkeitsunterart;
    private String strNummer;
    private Date dateBegin;
    private Date dateEnde;

    public Kurs(int intIdBerichte, int intTeilnehmer, String strBezeichnung, int intKm, int intInstanznummer, String strInstanzname, String strTaetigkeitsart, String strTaetigkeitsunterart, String strNummer, Date dateBeginn, Date dateEnde)
    {
        this.intIdBerichte = intIdBerichte;
        this.intTeilnehmer = intTeilnehmer;
        this.strBezeichnung = strBezeichnung;
        this.intKm = intKm;
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

    public String getStrBezeichnung()
    {
        return strBezeichnung;
    }

    public void setStrBezeichnung(String strBezeichnung)
    {
        this.strBezeichnung = strBezeichnung;
    }

    public int getIntKm()
    {
        return intKm;
    }

    public void setIntKm(int intKm)
    {
        this.intKm = intKm;
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
        final Kurs other = (Kurs) obj;
        if (this.intIdBerichte != other.intIdBerichte)
        {
            return false;
        }
        if (this.intTeilnehmer != other.intTeilnehmer)
        {
            return false;
        }
        if (!Objects.equals(this.strBezeichnung, other.strBezeichnung))
        {
            return false;
        }
        if (this.intKm != other.intKm)
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
        if (strBezeichnung == null)
        {
            strBezeichnung = "";
        }
        if (strTaetigkeitsart == null)
        {
            strTaetigkeitsart = "";
        }
        if (strTaetigkeitsunterart == null)
        {
            strTaetigkeitsunterart = "";
        }

        String strHtml = "<tr><td>"
                + intTeilnehmer + "</td><td>"
                + strBezeichnung + "</td><td>"
                + intKm + "</td><td>"
                + strTaetigkeitsart + "</td><td>"
                + strTaetigkeitsunterart + "</td><td>"
                + dateBegin + "</td><td>"
                + dateEnde + "</td>"
                + "<td></td></tr>";

        return strHtml;
    }

}
