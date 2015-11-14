/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.util.Objects;

/**
 *
 * @author Corinna
 */
public class Feuerwehr
{
    private String strName;
    private int intFeuerwehrNummer;

    public Feuerwehr(String strName, int intFeuerwehrNummer)
    {
        this.strName = strName;
        this.intFeuerwehrNummer = intFeuerwehrNummer;
    }

    public String getStrName()
    {
        return strName;
    }

    public void setStrName(String strName)
    {
        this.strName = strName;
    }

    public int getIntFeuerwehrNummer()
    {
        return intFeuerwehrNummer;
    }

    public void setIntFeuerwehrNummer(int intFeuerwehrNummer)
    {
        this.intFeuerwehrNummer = intFeuerwehrNummer;
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
        final Feuerwehr other = (Feuerwehr) obj;
        if (!Objects.equals(this.strName, other.strName))
        {
            return false;
        }
        if (this.intFeuerwehrNummer != other.intFeuerwehrNummer)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "Feuerwehr{" + "strName=" + strName + ", intFeuerwehrNummer=" + intFeuerwehrNummer + '}';
    }
    
    
}
