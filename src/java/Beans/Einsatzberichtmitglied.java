/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.util.Objects;

/**
 *
 * @author Yvonne
 */
public class Einsatzberichtmitglied
{
    
    private int intIdEinsatzMitglied;
    private int intIdBericht;
    private int intInstanznummer;
    private String strName;
    private int intSBN;
    private int intIdMitgliedschaften;
    private String strVorname;
    private String strNachname;

    public Einsatzberichtmitglied(int intIdEinsatzMitglied, int intIdBericht, int intInstanznummer, String strName, int intSBN, int intIdMitgliedschaften, String strVorname, String strNachname)
    {
        this.intIdEinsatzMitglied = intIdEinsatzMitglied;
        this.intIdBericht = intIdBericht;
        this.intInstanznummer = intInstanznummer;
        this.strName = strName;
        this.intSBN = intSBN;
        this.intIdMitgliedschaften = intIdMitgliedschaften;
        this.strVorname = strVorname;
        this.strNachname = strNachname;
    }

    public int getIntIdEinsatzMitglied()
    {
        return intIdEinsatzMitglied;
    }

    public void setIntIdEinsatzMitglied(int intIdEinsatzMitglied)
    {
        this.intIdEinsatzMitglied = intIdEinsatzMitglied;
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

    public String getStrName()
    {
        return strName;
    }

    public void setStrName(String strName)
    {
        this.strName = strName;
    }

    public int getIntSBN()
    {
        return intSBN;
    }

    public void setIntSBN(int intSBN)
    {
        this.intSBN = intSBN;
    }

    public int getIntIdMitgliedschaften()
    {
        return intIdMitgliedschaften;
    }

    public void setIntIdMitgliedschaften(int intIdMitgliedschaften)
    {
        this.intIdMitgliedschaften = intIdMitgliedschaften;
    }

    public String getStrVorname()
    {
        return strVorname;
    }

    public void setStrVorname(String strVorname)
    {
        this.strVorname = strVorname;
    }

    public String getStrNachname()
    {
        return strNachname;
    }

    public void setStrNachname(String strNachname)
    {
        this.strNachname = strNachname;
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
        final Einsatzberichtmitglied other = (Einsatzberichtmitglied) obj;
        if (this.intIdEinsatzMitglied != other.intIdEinsatzMitglied)
        {
            return false;
        }
        if (this.intIdBericht != other.intIdBericht)
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
        if (this.intSBN != other.intSBN)
        {
            return false;
        }
        if (this.intIdMitgliedschaften != other.intIdMitgliedschaften)
        {
            return false;
        }
        if (!Objects.equals(this.strVorname, other.strVorname))
        {
            return false;
        }
        if (!Objects.equals(this.strNachname, other.strNachname))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        String strHtml = "<tr><td>"
                + strNachname.toUpperCase() + "</td><td>"
                + strVorname + "</td><td>"
                + intSBN + "</td></tr>";

        return strHtml;
    }
}
