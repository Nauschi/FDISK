/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.util.LinkedList;
import java.util.Objects;

/**
 *
 * @author Corinna
 */
public class Abschnitt
{
    private String strName;
    private int intAbschnittsNummer;
    private LinkedList<Bezirk> liBezirke;

    public Abschnitt(String strName, int intAbschnittsNummer, LinkedList<Bezirk> liBezirke)
    {
        this.strName = strName;
        this.intAbschnittsNummer = intAbschnittsNummer;
        this.liBezirke = liBezirke;
    }

    public String getStrName()
    {
        return strName;
    }

    public void setStrName(String strName)
    {
        this.strName = strName;
    }

    public int getIntAbschnittsNummer()
    {
        return intAbschnittsNummer;
    }

    public void setIntAbschnittsNummer(int intAbschnittsNummer)
    {
        this.intAbschnittsNummer = intAbschnittsNummer;
    }

    public LinkedList<Bezirk> getLiBezirke()
    {
        return liBezirke;
    }

    public void setLiBezirke(LinkedList<Bezirk> liBezirke)
    {
        this.liBezirke = liBezirke;
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
        final Abschnitt other = (Abschnitt) obj;
        if (!Objects.equals(this.strName, other.strName))
        {
            return false;
        }
        if (this.intAbschnittsNummer != other.intAbschnittsNummer)
        {
            return false;
        }
        if (!Objects.equals(this.liBezirke, other.liBezirke))
        {
            return false;
        }
        return true;
    }
    
    

    public void addBezirk(Bezirk b)
    {
        if(!liBezirke.contains(b))
        {
            liBezirke.add(b);
        }
    }

    @Override
    public String toString()
    {
        return "Abschnitt{" + "strName=" + strName + ", intAbschnittsNummer=" + intAbschnittsNummer + ", liBezirke=" + liBezirke + '}';
    }

   
    
}

