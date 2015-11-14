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
public class Bezirk
{
    private String strName;
    private int intBezirksNummer;
    private LinkedList<Feuerwehr> liFeuerwehren;

    public Bezirk(String strName, int intBezNr, LinkedList<Feuerwehr> liFeuerwehren)
    {
        this.strName = strName;
        this.intBezirksNummer = intBezNr;
        this.liFeuerwehren = liFeuerwehren;
    }

    public String getStrName()
    {
        return strName;
    }

    public void setStrName(String strName)
    {
        this.strName = strName;
    }

    public int getIntBezirksNummer()
    {
        return intBezirksNummer;
    }

    public void setIntBezirksNummer(int intBezirksNummer)
    {
        this.intBezirksNummer = intBezirksNummer;
    }

    public LinkedList<Feuerwehr> getLiFeuerwehren()
    {
        return liFeuerwehren;
    }

    public void setLiFeuerwehren(LinkedList<Feuerwehr> liFeuerwehren)
    {
        this.liFeuerwehren = liFeuerwehren;
    }
    
    public void addFeuerwehr(Feuerwehr f)
    {
        if(!liFeuerwehren.contains(f))
        {
            liFeuerwehren.add(f);
        }
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
        final Bezirk other = (Bezirk) obj;
        if (!Objects.equals(this.strName, other.strName))
        {
            return false;
        }
        if (this.intBezirksNummer != other.intBezirksNummer)
        {
            return false;
        }
        if (!Objects.equals(this.liFeuerwehren, other.liFeuerwehren))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "Bezirk{" + "strName=" + strName + ", intBezirksNummer=" + intBezirksNummer + ", liFeuerwehren=" + liFeuerwehren + '}';
    } 
}


