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
public class Uebungsbericht
{
    private int intId_StmkUebungsberichte;
    private int intIdBerichte;
    private int intInstanznummer;
    private String strName;
    private String strUebungsart;
    private String strUebungsunterart;
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
    public Uebungsbericht(int intId_StmkUebungsberichte, int intIdBerichte, int intInstanznummer, String strName, String strUebungsart, String strUebungsunterart, String strNummer, Date dateBeginn, Date dateEnde, String strStrasse, String strNummerAdr, String strStiege, String strPlz, String strOrt, String strMeldung, String strFehlalarm)
    {
        this.intId_StmkUebungsberichte = intId_StmkUebungsberichte;
        this.intIdBerichte = intIdBerichte;
        this.intInstanznummer = intInstanznummer;
        this.strName = strName;
        this.strUebungsart = strUebungsart;
        this.strUebungsunterart = strUebungsunterart;
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
    }

    public int getIntId_StmkUebungsberichte()
    {
        return intId_StmkUebungsberichte;
    }

    public void setIntId_StmkUebungsberichte(int intId_StmkUebungsberichte)
    {
        this.intId_StmkUebungsberichte = intId_StmkUebungsberichte;
    }

    public int getIntIdBerichte()
    {
        return intIdBerichte;
    }

    public void setIntIdBerichte(int intIdBerichte)
    {
        this.intIdBerichte = intIdBerichte;
    }

    public int getIntInstanznummer()
    {
        return intInstanznummer;
    }

    public void setIntInstanznummer(int intInstanznummer)
    {
        this.intInstanznummer = intInstanznummer;
    }

    public String getStrName()
    {
        return strName;
    }

    public void setStrName(String strName)
    {
        this.strName = strName;
    }

    public String getStrUebungsart()
    {
        return strUebungsart;
    }

    public void setStrUebungsart(String strUebungsart)
    {
        this.strUebungsart = strUebungsart;
    }

    public String getStrUebungsunterart()
    {
        return strUebungsunterart;
    }

    public void setStrUebungsunterart(String strUebungsunterart)
    {
        this.strUebungsunterart = strUebungsunterart;
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

    @Override
    public int hashCode()
    {
        int hash = 5;
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
        final Uebungsbericht other = (Uebungsbericht) obj;
        if (this.intId_StmkUebungsberichte != other.intId_StmkUebungsberichte)
        {
            return false;
        }
        if (this.intIdBerichte != other.intIdBerichte)
        {
            return false;
        }
        if (this.intInstanznummer != other.intInstanznummer)
        {
            return false;
        }
        if (!Objects.equals(this.strName, other.strName))
        {
            return false;
        }
        if (!Objects.equals(this.strUebungsart, other.strUebungsart))
        {
            return false;
        }
        if (!Objects.equals(this.strUebungsunterart, other.strUebungsunterart))
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
        return true;
    }

 
    

    @Override
    public String toString()
    {
        return "UebungsberichtUeberschrift{" + "intId_StmkUebungsberichte=" + intId_StmkUebungsberichte + ", intIdBerichte=" + intIdBerichte + ", intInstanznummer=" + intInstanznummer + ", strName=" + strName + ", strUebungsart=" + strUebungsart + ", strUebungsunterart=" + strUebungsunterart + ", strNummer=" + strNummer + ", dateBeginn=" + dateBeginn + ", dateEnde=" + dateEnde + ", strStrasse=" + strStrasse + ", strNummerAdr=" + strNummerAdr + ", strStiege=" + strStiege + ", strPlz=" + strPlz + ", strOrt=" + strOrt + ", strMeldung=" + strMeldung + ", strFehlalarm=" + strFehlalarm +'}';
    }
    
    
    
}
