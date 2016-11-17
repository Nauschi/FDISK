/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import BL.BL;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author kinco_000
 */
public class Mitglied implements Serializable
{

    private int intId_Personen;
    public String strStammblattnummer;
    public String strDienstgrad;
    public String strTitel;
    public String strVorname;
    public String strZuname;
    public String strFubwehr;
    
    private BL bl = new BL(); 

    public Mitglied(int intId_Personen, String strStammblattnummer, String strDienstgrad, String strTitel, String strVorname, String strZuname, String strFubwehr) throws ClassNotFoundException
    {
        this.intId_Personen = intId_Personen;
        this.strStammblattnummer = strStammblattnummer;
        this.strDienstgrad = strDienstgrad;
        this.strTitel = strTitel;
        this.strVorname = strVorname;
        this.strZuname = strZuname;
        this.strFubwehr = strFubwehr;
    }

    public String getStrFubwehr() {
        return strFubwehr;
    }

    public void setStrFubwehr(String strFubwehr) {
        this.strFubwehr = strFubwehr;
    }

    public int getIntId_Personen()
    {
        return intId_Personen;
    }

    public void setIntId_Personen(int intId_Personen)
    {
        this.intId_Personen = intId_Personen;
    }

    public String getStrStammblattnummer()
    {
        return strStammblattnummer;
    }

    public void setIntStammblattnummer(String intStammblattnummer)
    {
        this.strStammblattnummer = strStammblattnummer;
    }

    public String getStrDienstgrad()
    {
        return strDienstgrad;
    }

    public void setStrDienstgrad(String strDienstgrad)
    {
        this.strDienstgrad = strDienstgrad;
    }

    public String getStrTitel()
    {
        return strTitel;
    }

    public void setStrTitel(String strTitel)
    {
        this.strTitel = strTitel;
    }

    public String getStrVorname()
    {
        return strVorname;
    }

    public void setStrVorname(String strVorname)
    {
        this.strVorname = strVorname;
    }

    public String getStrZuname()
    {
        return strZuname;
    }

    public void setStrZuname(String strZuname)
    {
        this.strZuname = strZuname;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + this.intId_Personen;
        hash = 53 * hash + Objects.hashCode(this.strStammblattnummer);
        hash = 53 * hash + Objects.hashCode(this.strDienstgrad);
        hash = 53 * hash + Objects.hashCode(this.strTitel);
        hash = 53 * hash + Objects.hashCode(this.strVorname);
        hash = 53 * hash + Objects.hashCode(this.strZuname);
        hash = 53 * hash + Objects.hashCode(this.strFubwehr);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Mitglied other = (Mitglied) obj;
        if (this.intId_Personen != other.intId_Personen) {
            return false;
        }
        if (!Objects.equals(this.strStammblattnummer, other.strStammblattnummer)) {
            return false;
        }
        if (!Objects.equals(this.strDienstgrad, other.strDienstgrad)) {
            return false;
        }
        if (!Objects.equals(this.strTitel, other.strTitel)) {
            return false;
        }
        if (!Objects.equals(this.strVorname, other.strVorname)) {
            return false;
        }
        if (!Objects.equals(this.strZuname, other.strZuname)) {
            return false;
        }
        if (!Objects.equals(this.strFubwehr, other.strFubwehr)) {
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
        if(strFubwehr == null){
            strFubwehr = "";
        }

        strZuname = bl.formatiereAusgabe(strZuname);
        strVorname = bl.formatiereAusgabe(strVorname);

        String strHtml = "<tr><td id='multipleFb'>"
                + strFubwehr + "</td><td>"
                + strStammblattnummer + "</td><td>"
                + strDienstgrad + "</td><td>"
                + strTitel + "</td><td>"
                + strVorname + "</td><td>"
                + strZuname + "</td><td class='bemerkung'></td></tr>";
    
        return strHtml;
    }

}
