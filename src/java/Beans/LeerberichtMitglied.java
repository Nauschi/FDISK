/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Database.DB_Access;
import java.util.Objects;

/**
 *
 * @author Yvonne
 */
public class LeerberichtMitglied
{

    private int intId_Personen;
    public String strStammblattnummer;
    public String strDienstgrad;
    public String strTitel;
    public String strVorname;
    public String strZuname;
    private DB_Access theInstance;

    public LeerberichtMitglied(int intId_Personen, String strStammblattnummer, String strDienstgrad, String strTitel, String strVorname, String strZuname) throws ClassNotFoundException
    {
        this.intId_Personen = intId_Personen;
        this.strStammblattnummer = strStammblattnummer;
        this.strDienstgrad = strDienstgrad;
        this.strTitel = strTitel;
        this.strVorname = strVorname;
        this.strZuname = strZuname;

        theInstance = DB_Access.getInstance();
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
        hash = 97 * hash + this.intId_Personen;
        hash = 97 * hash + Objects.hashCode(this.strStammblattnummer);
        hash = 97 * hash + Objects.hashCode(this.strDienstgrad);
        hash = 97 * hash + Objects.hashCode(this.strTitel);
        hash = 97 * hash + Objects.hashCode(this.strVorname);
        hash = 97 * hash + Objects.hashCode(this.strZuname);
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
        final LeerberichtMitglied other = (LeerberichtMitglied) obj;
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
        if (strVorname == null)
        {
            strVorname = "";
        }
        if (strZuname == null)
        {
            strZuname = "";
        }

//        strVorname = theInstance.capitalizeEachWord(strVorname);

        if (strTitel != null && !strTitel.isEmpty())
        {
            return "<td>&Omicron;&nbsp;" + strTitel + " " + strZuname.toUpperCase() + " " + strVorname + ", " + strStammblattnummer + "</td>";
        }
        String strHtml = "<td>&Omicron;&nbsp;" + strZuname.toUpperCase() + " " + strVorname + ", " + strStammblattnummer + "</td>";
        return strHtml;
    }
}
