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
public class Geraetetraegermitglied extends Mitglied implements Serializable
{

    private int intInstanznr;
    private Date dateGeb;
    private Date dateUntersuchung;
    private Date dateNaechsteUntersuchung;
    private int intIdInstanzen;
    private int intAnzahl;
    private String strInstanzname; 
    
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm");
    private SimpleDateFormat sdfGebDate = new SimpleDateFormat("dd.MM.yyyy");
   private BL bl = new BL(); 

    public Geraetetraegermitglied(int intInstanznr, Date dateGeb, Date dateUntersuchung, Date dateNaechsteUntersuchung, int intIdInstanzen, int intAnzahl, int intId_Personen, String strStammblattnummer, String strDienstgrad, String strTitel, String strVorname, String strZuname, String strInstanzname, String strFubwehr) throws ClassNotFoundException
    {
        super(intId_Personen, strStammblattnummer, strDienstgrad, strTitel, strVorname, strZuname, strFubwehr);

        this.intInstanznr = intInstanznr;
        this.dateGeb = dateGeb;
        this.dateUntersuchung = dateUntersuchung;
        this.dateNaechsteUntersuchung = dateNaechsteUntersuchung;
        this.intIdInstanzen = intIdInstanzen;
        this.intAnzahl = intAnzahl;
        this.strInstanzname = strInstanzname; 

    }

    public Geraetetraegermitglied(int intId_Personen, String strStammblattnummer, String strDienstgrad, String strTitel, String strVorname, String strZuname, String strFubwehr) throws ClassNotFoundException
    {
        super(intId_Personen, strStammblattnummer, strDienstgrad, strTitel, strVorname, strZuname, strFubwehr);
    }

    public String getStrInstanzname()
    {
        return strInstanzname;
    }

    public void setStrInstanzname(String strInstanzname)
    {
        this.strInstanzname = strInstanzname;
    }

    
    
    
    public int getIntInstanznr()
    {
        return intInstanznr;
    }

    public void setIntInstanznr(int intInstanznr)
    {
        this.intInstanznr = intInstanznr;
    }

    public Date getDateGeb()
    {
        return dateGeb;
    }

    public void setDateGeb(Date dateGeb)
    {
        this.dateGeb = dateGeb;
    }

    public Date getDateUntersuchung()
    {
        return dateUntersuchung;
    }

    public void setDateUntersuchung(Date dateUntersuchung)
    {
        this.dateUntersuchung = dateUntersuchung;
    }

    public Date getDateNaechsteUntersuchung()
    {
        return dateNaechsteUntersuchung;
    }

    public void setDateNaechsteUntersuchung(Date dateNaechsteUntersuchung)
    {
        this.dateNaechsteUntersuchung = dateNaechsteUntersuchung;
    }

    public int getIntIdInstanzen()
    {
        return intIdInstanzen;
    }

    public void setIntIdInstanzen(int intIdInstanzen)
    {
        this.intIdInstanzen = intIdInstanzen;
    }

    public int getIntAnzahl()
    {
        return intAnzahl;
    }

    public void setIntAnzahl(int intAnzahl)
    {
        this.intAnzahl = intAnzahl;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
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
        final Geraetetraegermitglied other = (Geraetetraegermitglied) obj;
        if (this.intInstanznr != other.intInstanznr)
        {
            return false;
        }
        if (!Objects.equals(this.dateGeb, other.dateGeb))
        {
            return false;
        }
        if (!Objects.equals(this.dateUntersuchung, other.dateUntersuchung))
        {
            return false;
        }
        if (!Objects.equals(this.dateNaechsteUntersuchung, other.dateNaechsteUntersuchung))
        {
            return false;
        }
        if (this.intIdInstanzen != other.intIdInstanzen)
        {
            return false;
        }
        if (this.intAnzahl != other.intAnzahl)
        {
            return false;
        }
        if (!Objects.equals(this.strInstanzname, other.strInstanzname))
        {
            return false;
        }
        return true;
    }

 
    
    
    @Override
    public String toString()
    {
        if (strStammblattnummer == null)
        {
            strStammblattnummer = "";
        }
        if (strDienstgrad == null)
        {
            strDienstgrad = "";
        }
        if (strTitel == null)
        {
            strTitel = "";
        }
        if (strVorname == null)
        {
            strVorname = "";
        }
        if (strZuname == null)
        {
            strZuname = "";
        }

        strZuname = bl.formatiereAusgabe(strZuname);
        strVorname = bl.formatiereAusgabe(strVorname);

        String strHtml = "<tr>"
                + "<td id='multipleFb'>" + strFubwehr + "</td><td class='STB'>"
                + strStammblattnummer + "</td><td class='DGR'>"
                + strDienstgrad + "</td><td class='titel'>"
                + strTitel + "</td><td>"
                + strVorname + "</td><td>"
                + strZuname + "</td><td>"
                + sdfGebDate.format(dateGeb) + "</td><td>"
                + sdf.format(dateUntersuchung) + "</td><td>"
                + sdf.format(dateNaechsteUntersuchung) + "</td></tr>";

        return strHtml;
    }
}
