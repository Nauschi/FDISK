/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.util.Objects;

/**
 *
 * @author kinco_000
 */
public class Mitglied
{

    private int intId_Personen;
    public String strStammblattnummer;
    public String strDienstgrad;
    public String strTitel;
    public String strVorname;
    public String strZuname;

    public Mitglied(int intId_Personen, String strStammblattnummer, String strDienstgrad, String strTitel, String strVorname, String strZuname)
    {
        this.intId_Personen = intId_Personen;
        this.strStammblattnummer = strStammblattnummer;
        this.strDienstgrad = strDienstgrad;
        this.strTitel = strTitel;
        this.strVorname = strVorname;
        this.strZuname = strZuname;
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
        final Mitglied other = (Mitglied) obj;
        if (this.intId_Personen != other.intId_Personen)
        {
            return false;
        }
        if (!Objects.equals(this.strStammblattnummer, other.strStammblattnummer))
        {
            return false;
        }
        if (!Objects.equals(this.strDienstgrad, other.strDienstgrad))
        {
            return false;
        }
        if (!Objects.equals(this.strTitel, other.strTitel))
        {
            return false;
        }
        if (!Objects.equals(this.strVorname, other.strVorname))
        {
            return false;
        }
        if (!Objects.equals(this.strZuname, other.strZuname))
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

        String strHtml = "<tr><td>"
                + strStammblattnummer + "</td><td>"
                + strDienstgrad + "</td><td>"
                + strTitel + "</td><td>"
                + strVorname + "</td><td>"
                + strZuname + "</td><td></td></tr>";

        return strHtml;
    }

}
