package Beans;

import Database.DB_Access;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Yvonne
 */
public class Bericht
{

    private int intId_Berichte;
    private int intInstanznummer;
    private String strName;
    private String strArt;
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

    private DB_Access theInstance;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm");

    public Bericht(int intId_Berichte, int intInstanznummer, String strName, String strArt, String strNummer, Date dateBeginn, Date dateEnde, String strStrasse, String strNummerAdr, String strStiege, String strPlz, String strOrt, String strMeldung, String strFehlalarm) throws ClassNotFoundException
    {
        this.intId_Berichte = intId_Berichte;
        this.intInstanznummer = intInstanznummer;
        this.strName = strName;
        this.strArt = strArt;
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
        theInstance = DB_Access.getInstance();
    }

    public int getIntId_Berichte()
    {
        return intId_Berichte;
    }

    public void setIntId_Berichte(int intId_Berichte)
    {
        this.intId_Berichte = intId_Berichte;
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

    public String getStrArt()
    {
        return strArt;
    }

    public void setStrArt(String strArt)
    {
        this.strArt = strArt;
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
        final Bericht other = (Bericht) obj;
        if (this.intId_Berichte != other.intId_Berichte)
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
        if (!Objects.equals(this.strArt, other.strArt))
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
        if (strArt == null)
        {
            strArt = "";
        }

//        strArt = theInstance.capitalizeEachWord(strArt);

        String strHtml = "<tr><td>"
                + sdf.format(dateBeginn) + "</td><td>"
                + sdf.format(dateEnde) + "</td><td>"
                + strArt + "</td><td></td></tr>";

        return strHtml;
    }

}
