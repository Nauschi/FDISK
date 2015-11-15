/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Corinna
 */
public class TaetigkeitsberichtUeberschrift
{
    private int intIdBericht;
    private int intInstanznummer;
    private String strInstanzname;
    private String strTaetigkeitsart;
    private String strTaetigkeitsunterart;
    private String strNummer;
    private Date dateBeginn;
    private Date dateEnde;
    private String strStrasse;
    private String strNummerAdr;
    private String strStiege;
    private String strPlz;
    private String strOrt;
    private String strMeldung;
    private String strFehlalarm;
    private String strMsrepl_tran_version;

    public TaetigkeitsberichtUeberschrift(int intIdBericht, int intInstanznummer, String strInstanzname, String strTaetigkeitsart, String strTaetigkeitsunterart, String strNummer, Date dateBeginn, Date dateEnde, String strStrasse, String strNummerAdr, String strStiege, String strPlz, String strOrt, String strMeldung, String strFehlalarm, String strMsrepl_tran_version)
    {
        this.intIdBericht = intIdBericht;
        this.intInstanznummer = intInstanznummer;
        this.strInstanzname = strInstanzname;
        this.strTaetigkeitsart = strTaetigkeitsart;
        this.strTaetigkeitsunterart = strTaetigkeitsunterart;
        this.strNummer = strNummer;
        this.dateBeginn = dateBeginn;
        this.dateEnde = dateEnde;
        this.strStrasse = strStrasse;
        this.strNummerAdr = strNummerAdr;
        this.strStiege = strStiege;
        this.strPlz = strPlz;
        this.strOrt = strOrt;
        this.strMeldung = strMeldung;
        this.strFehlalarm = strFehlalarm;
        this.strMsrepl_tran_version = strMsrepl_tran_version;
    }

    public int getIntIdBericht()
    {
        return intIdBericht;
    }

    public void setIntIdBericht(int intIdBericht)
    {
        this.intIdBericht = intIdBericht;
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

    public void setStrNummer(String strNummer)
    {
        this.strNummer = strNummer;
    }

    public Date getDateBeginn()
    {
        return dateBeginn;
    }

    public void setDateBeginn(Date dateBeginn)
    {
        this.dateBeginn = dateBeginn;
    }

    public Date getDateEnde()
    {
        return dateEnde;
    }

    public void setDateEnde(Date dateEnde)
    {
        this.dateEnde = dateEnde;
    }

    public String getStrStrasse()
    {
        return strStrasse;
    }

    public void setStrStrasse(String strStrasse)
    {
        this.strStrasse = strStrasse;
    }

    public String getStrNummerAdr()
    {
        return strNummerAdr;
    }

    public void setStrNummerAdr(String strNummerAdr)
    {
        this.strNummerAdr = strNummerAdr;
    }

    public String getStrStiege()
    {
        return strStiege;
    }

    public void setStrStiege(String strStiege)
    {
        this.strStiege = strStiege;
    }

    public String getStrPlz()
    {
        return strPlz;
    }

    public void setStrPlz(String strPlz)
    {
        this.strPlz = strPlz;
    }

    public String getStrOrt()
    {
        return strOrt;
    }

    public void setStrOrt(String strOrt)
    {
        this.strOrt = strOrt;
    }

    public String getStrMeldung()
    {
        return strMeldung;
    }

    public void setStrMeldung(String strMeldung)
    {
        this.strMeldung = strMeldung;
    }

    public String getStrFehlalarm()
    {
        return strFehlalarm;
    }

    public void setStrFehlalarm(String strFehlalarm)
    {
        this.strFehlalarm = strFehlalarm;
    }

    public String getStrMsrepl_tran_version()
    {
        return strMsrepl_tran_version;
    }

    public void setStrMsrepl_tran_version(String strMsrepl_tran_version)
    {
        this.strMsrepl_tran_version = strMsrepl_tran_version;
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
        final TaetigkeitsberichtUeberschrift other = (TaetigkeitsberichtUeberschrift) obj;
        if (this.intIdBericht != other.intIdBericht)
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
        if (!Objects.equals(this.strNummer, other.strNummer))
        {
            return false;
        }
        if (!Objects.equals(this.dateBeginn, other.dateBeginn))
        {
            return false;
        }
        if (!Objects.equals(this.dateEnde, other.dateEnde))
        {
            return false;
        }
        if (!Objects.equals(this.strStrasse, other.strStrasse))
        {
            return false;
        }
        if (!Objects.equals(this.strNummerAdr, other.strNummerAdr))
        {
            return false;
        }
        if (!Objects.equals(this.strStiege, other.strStiege))
        {
            return false;
        }
        if (!Objects.equals(this.strPlz, other.strPlz))
        {
            return false;
        }
        if (!Objects.equals(this.strOrt, other.strOrt))
        {
            return false;
        }
        if (!Objects.equals(this.strMeldung, other.strMeldung))
        {
            return false;
        }
        if (!Objects.equals(this.strFehlalarm, other.strFehlalarm))
        {
            return false;
        }
        if (!Objects.equals(this.strMsrepl_tran_version, other.strMsrepl_tran_version))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "TaetigkeitsberichtUeberschrift{" + "intIdBericht=" + intIdBericht + ", intInstanznummer=" + intInstanznummer + ", strInstanzname=" + strInstanzname + ", strTaetigkeitsart=" + strTaetigkeitsart + ", strTaetigkeitsunterart=" + strTaetigkeitsunterart + ", strNummer=" + strNummer + ", dateBeginn=" + dateBeginn + ", dateEnde=" + dateEnde + ", strStrasse=" + strStrasse + ", strNummerAdr=" + strNummerAdr + ", strStiege=" + strStiege + ", strPlz=" + strPlz + ", strOrt=" + strOrt + ", strMeldung=" + strMeldung + ", strFehlalarm=" + strFehlalarm + ", strMsrepl_tran_version=" + strMsrepl_tran_version + '}';
    }
    
    
    
    
}
