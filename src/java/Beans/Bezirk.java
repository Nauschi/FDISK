/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Objects;

/**
 *
 * @author Corinna
 */
public class Bezirk implements Serializable
{

    private String strName;
    private int intBezirksNummer;
    private LinkedList<Abschnitt> liAbschnitte;

    public Bezirk(String strName, int intBezNr, LinkedList<Abschnitt> liAbschnitte)
    {
        this.strName = strName;
        this.intBezirksNummer = intBezNr;
        this.liAbschnitte = liAbschnitte;
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

    public LinkedList<Abschnitt> getLiAbschnitte()
    {
        return liAbschnitte;
    }

    public void setLiAbschnitte(LinkedList<Abschnitt> liAbschnitte)
    {
        this.liAbschnitte = liAbschnitte;
    }

    public void addAbschnitt(Abschnitt a)
    {
        if (!liAbschnitte.contains(a))
        {
            liAbschnitte.add(a);
        }
    }

    @Override
    public String toString()
    {
        String strHTML = "<option value='" + intBezirksNummer + "'>" + strName+"</option>";
        return strHTML;
    }
    
    public String toSelectedString()
    {
        String strHTML = "<option value='" + intBezirksNummer + "' selected>" + strName+"</option>";
        return strHTML;
    }
    
    public String generiereHiddenDiv()
    {
        System.out.println("Bezirk.generiereHiddenDiv: start");
        String strHTML = "<div style='display:none' id='div_"+intBezirksNummer+"' >";
        if (liAbschnitte != null)
        {
            System.out.println("Bezirk.generiereHiddenDiv: if+ size="+liAbschnitte.size());
            for (Abschnitt abschnitt : liAbschnitte)
            {
                strHTML+=abschnitt.toString();
            }
        }
        strHTML += "</div>";
        
        return strHTML;
    }
}
