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
public class Abschnitt {

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
        return "Abschnitt{" + "strName=" + strName + ", intAbschnittsNummer=" + intAbschnittsNummer + ", liFeuerwehren=" + liFeuerwehren + '}';
    }

}
