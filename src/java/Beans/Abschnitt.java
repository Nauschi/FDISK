/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author Corinna
 */
public class Abschnitt implements Serializable{

    private String strName;
    private int intAbschnittsNummer;
    private LinkedList<Feuerwehr> liFeuerwehren;

    public Abschnitt(String strName, int intAbschnittsNummer, LinkedList<Feuerwehr> liFeuerwehren) {
        this.strName = strName;
        this.intAbschnittsNummer = intAbschnittsNummer;
        this.liFeuerwehren = liFeuerwehren;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public int getIntAbschnittsNummer() {
        return intAbschnittsNummer;
    }

    public void setIntAbschnittsNummer(int intAbschnittsNummer) {
        this.intAbschnittsNummer = intAbschnittsNummer;
    }

    public LinkedList<Feuerwehr> getLiFeuerwehren() {
        return liFeuerwehren;
    }

    public void setLiFeuerwehren(LinkedList<Feuerwehr> liFeuerwehren) {
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
    public String toString() {
        String strHTML = "<option value='" + intAbschnittsNummer + "'>" + strName;
        strHTML +="</option>";
        return strHTML;
    }
    
    
    public String generiereHiddenDiv()
    {
        String strHTML = "<div style='display:none' id='div_"+intAbschnittsNummer+"' >";
        if (liFeuerwehren != null)
        {
            for (Feuerwehr feuerwehr : liFeuerwehren)
            {
                strHTML+=feuerwehr.toString();
            }
        }
        strHTML += "</div>";
        return strHTML;
    }

}
